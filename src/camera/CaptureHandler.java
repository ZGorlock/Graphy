/*
 * File:    CaptureHandler.java
 * Package: camera
 * Author:  Zachary Gill
 */

package camera;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.Environment;
import main.EnvironmentBase;
import utility.CmdLineUtility;
import utility.ImageUtility;

/**
 * Handles captures and recordings of the Environment.
 */
public class CaptureHandler {
    
    //Constants
    
    /**
     * The directory to save captures and recordings in.
     */
    public static final File CAPTURE_DIR = new File("capture");
    
    /**
     * The regex pattern for a frame name in a recording.
     */
    private static final Pattern FRAME_NAME_PATTERN = Pattern.compile("[^~]+~(?<timestamp>[0-9]+)~[0-9]+\\.jpg");
    
    
    //Enums
    
    /**
     * An enumeration of frame rate modes for recordings.
     */
    public enum FrameRateMode {
        ENVIRONMENT_FPS,
        CUSTOM_FPS,
        TIME_BASED
    }
    
    
    //Static Fields
    
    /**
     * The frame rate mode to use when recording.
     */
    private static FrameRateMode frameRateMode = FrameRateMode.ENVIRONMENT_FPS;
    
    /**
     * The custom frame rate to use when the frame rate mode is set to CUSTOM_FPS.
     */
    private static double customFps = 29.97;
    
    
    //Fields
    
    /**
     * The Environment.
     */
    private final EnvironmentBase environment;
    
    /**
     * A flag indicating whether or not a capture should be taken.
     */
    public final AtomicBoolean capture = new AtomicBoolean(false);
    
    /**
     * A flag indicating whether or not, if a capture should be taken, the capture should be copied to the clipboard.
     */
    private final AtomicBoolean copyCapture = new AtomicBoolean(false);
    
    /**
     * The Task that takes the capture.
     */
    private UUID captureTask = null;
    
    /**
     * A flag indicating whether or not a recording is in progress.
     */
    public final AtomicBoolean recording = new AtomicBoolean(false);
    
    /**
     * The temporary directory to store frames during a recording.
     */
    private File recordingDir = null;
    
    /**
     * The current frame count during a recording.
     */
    private final AtomicInteger recordingFrame = new AtomicInteger(0);
    
    /**
     * The Task that takes the recording.
     */
    private UUID recordingTask = null;
    
    
    //Constructors
    
    /**
     * Constructs a CaptureHandler.
     *
     * @param environment The Environment.
     */
    public CaptureHandler(EnvironmentBase environment) {
        this.environment = environment;
    }
    
    
    //Methods
    
    /**
     * Listens for capture events.
     *
     * @param copy Whether or not to copy the capture to the clipboard.
     */
    public void captureListener(boolean copy) {
        if (!capture.get()) {
            capture.set(true);
            copyCapture.set(copy);
            new Thread(this::initializeCapture).start();
        }
    }
    
    /**
     * Listens for recording events.
     */
    public void recordingListener() {
        if (!recording.get()) {
            new Thread(this::initializeRecording).start();
        } else {
            new Thread(this::finalizeRecording).start();
        }
    }
    
    /**
     * Opens the directory that captures and recordings are saved in.
     */
    public void openCaptureDir() {
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.open(CAPTURE_DIR);
        } catch (Exception ignored) {
        }
    }
    
    /**
     * Shuts down the Capture Handler.
     */
    public void shutdown() {
        if (recording.get()) {
            finalizeRecording(true);
        }
    }
    
    /**
     * Initializes a capture.
     */
    private synchronized void initializeCapture() {
        if (!capture.get()) {
            return;
        }
        
        captureTask = Environment.addTask(this::handleCapture);
    }
    
    /**
     * Finalizes a capture.
     */
    private synchronized void finalizeCapture() {
        if (!capture.get() || (captureTask == null)) {
            return;
        }
        
        Environment.removeTask(captureTask);
        captureTask = null;
        
        capture.set(false);
        copyCapture.set(false);
    }
    
    /**
     * Initialized a recording.
     */
    private synchronized void initializeRecording() {
        if (recording.get()) {
            return;
        }
        
        recordingDir = null;
        recordingDir = new File(CAPTURE_DIR, getCaptureName(true));
        if (!recordingDir.mkdirs()) {
            recordingDir = null;
            return;
        }
        
        recordingFrame.set(0);
        recording.set(true);
        
        Environment.fps = Environment.fps / 2;
        recordingTask = Environment.addTask(this::handleRecording);
    }
    
    /**
     * Finalizes a recording.
     *
     * @param wait A flag indicating whether or not to wait for the recording to finish encoding.
     */
    private synchronized void finalizeRecording(boolean wait) {
        if (!recording.get() || (recordingTask == null) || (recordingDir == null) || !recordingDir.exists() || !recordingDir.isDirectory()) {
            return;
        }
        
        Environment.removeTask(recordingTask);
        recordingTask = null;
        
        Environment.fps = Environment.fps * 2;
        
        if (wait) {
            encodeRecording();
        } else {
            Thread encodeRecording = new Thread(this::encodeRecording);
            encodeRecording.start();
        }
    }
    
    /**
     * Finalizes a recording.
     */
    private synchronized void finalizeRecording() {
        finalizeRecording(false);
    }
    
    /**
     * Handles the processing of captures.
     */
    private synchronized void handleCapture() {
        if (capture.get()) {
            BufferedImage screenshot = screenshot(recording.get());
            if (screenshot != null) {
                ImageUtility.saveImage(screenshot, new File(CAPTURE_DIR, getCaptureName(false) + ".jpg"));
                if (copyCapture.get()) {
                    ImageUtility.copyImageToClipboard(screenshot);
                }
                finalizeCapture();
            }
        }
    }
    
    /**
     * Handles the processing of recordings.
     */
    private synchronized void handleRecording() {
        if (recording.get()) {
            BufferedImage screenshot;
            do {
                screenshot = screenshot(capture.get());
                if (screenshot != null) {
                    String frameName = String.format("%08d", recordingFrame.incrementAndGet());
                    ImageUtility.saveImage(screenshot, new File(recordingDir, recordingDir.getName() + "~" + frameName + ".jpg"));
                }
            } while (!capture.get() && (screenshot != null));
        }
    }
    
    /**
     * Returns whether or not the Capture Handler needs a buffer from the Environment.
     *
     * @return Whether or not the Capture Handler needs a buffer from the Environment.
     */
    public boolean needsBuffer() {
        return (captureTask != null) || (recordingTask != null);
    }
    
    /**
     * Retrieves a screenshot of the Render Panel.
     *
     * @param leaveBuffer A flag indicating whether or not to leave the buffer.
     * @return The screenshot.
     */
    private synchronized BufferedImage screenshot(boolean leaveBuffer) {
        if (leaveBuffer) {
            return environment.buffer.peek();
        } else {
            return environment.buffer.poll();
        }
    }
    
    /**
     * Encodes the recording.
     */
    private void encodeRecording() {
        File recordingVideo = new File(CAPTURE_DIR, recordingDir.getName() + ".mp4");
        File concatDemuxer = new File(recordingDir, "concatDemuxer.txt");
        
        File[] frames = recordingDir.listFiles();
        if (frames == null) {
            return;
        }
        
        String frameRate;
        String input;
        switch (frameRateMode) {
            case ENVIRONMENT_FPS:
                frameRate = "-framerate " + (Environment.fps / 2);
                input = "-i \"" + recordingDir.getAbsolutePath() + "/" + recordingDir.getName() + "~%08d.jpg\"";
                break;
            
            case CUSTOM_FPS:
                frameRate = "-framerate " + customFps;
                input = "-i \"" + recordingDir.getAbsolutePath() + "/" + recordingDir.getName() + "~%08d.jpg\"";
                break;
            
            case TIME_BASED:
                frameRate = "-f concat -safe 0";
                input = "-i \"" + concatDemuxer.getAbsolutePath() + "\" -vsync vfr";
                
                List<String> concatDemuxerLines = new ArrayList<>();
                String frameName = "";
                long lastTimestamp = 0;
                double totalDuration = 0.0;
                for (File frame : frames) {
                    Matcher frameNameMatcher = FRAME_NAME_PATTERN.matcher(frame.getName());
                    if (frameNameMatcher.matches()) {
                        long timestamp = Long.parseLong(frameNameMatcher.group("timestamp"));
                        if (lastTimestamp > 0) {
                            double duration = (timestamp - lastTimestamp) / 1000.0;
                            concatDemuxerLines.add("duration " + duration);
                            totalDuration += duration;
                        }
                        lastTimestamp = timestamp;
                        
                        frameName = frame.getAbsolutePath();
                        concatDemuxerLines.add("file '" + frameName + "'");
                    }
                }
                concatDemuxerLines.add("duration " + (totalDuration / frames.length));
                concatDemuxerLines.add("file '" + frameName + "'");
                try {
                    Files.write(concatDemuxer.toPath(), concatDemuxerLines, Charset.defaultCharset());
                } catch (Exception ignored) {
                    return;
                }
                break;
            
            default:
                return;
        }
        
        String cmd = "ffmpeg " + frameRate + " " + input + " \"" + recordingVideo.getAbsolutePath() + "\"";
        String log = CmdLineUtility.executeCmd(cmd, true);
        
        try {
            for (File frame : frames) {
                while (frame.exists()) {
                    Files.delete(frame.toPath());
                }
            }
            if (frameRateMode == FrameRateMode.TIME_BASED) {
                Files.delete(concatDemuxer.toPath());
            }
            Files.delete(recordingDir.toPath());
        } catch (Exception ignored) {
        }
        recordingDir = null;
        
        recordingFrame.set(0);
        recording.set(false);
    }
    
    /**
     * Returns the name for a capture or recording.
     *
     * @param recording A flag indicating whether or not this is a recording.
     * @return The name for the capture or recording.
     */
    private String getCaptureName(boolean recording) {
        String title;
        title = environment.frame.getTitle().trim().replaceAll("\\s+", "_");
        if (title.isEmpty()) {
            title = recording ? "recording" : "capture";
        }
        title += "~" + System.currentTimeMillis();
        return title;
    }
    
}
