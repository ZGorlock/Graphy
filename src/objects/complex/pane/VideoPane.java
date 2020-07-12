/*
 * File:    VideoPane.java
 * Package: objects.complex.pane
 * Author:  Zachary Gill
 */

package objects.complex.pane;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import objects.base.AbstractObject;
import objects.base.polygon.Rectangle;
import utility.ImageUtility;

/**
 * Defines a Video Pane.
 */
public class VideoPane extends Pane {
    
    //Constants
    
    /**
     * The default value of the frames per second to run the Video Pane at.
     */
    public static final int DEFAULT_FPS = 20;
    
    /**
     * The default value of the flag indicating whether or not to loop the Video Pane.
     */
    public static final boolean DEFAULT_LOOP = false;
    
    /**
     * The default value of the flag indicating whether or not to reverse the Video Pane on completion.
     */
    public static final boolean DEFAULT_REVERSE_ON_COMPLETION = false;
    
    
    //Fields
    
    /**
     * The frames per second to run the Video Pane at.
     */
    protected int fps;
    
    /**
     * A flag indicating whether or not to loop the Video Pane.
     */
    protected boolean loop;
    
    /**
     * A flag indicating whether or not to reverse the Video Pane on completion.
     */
    protected boolean reverseOnCompletion;
    
    /**
     * The directory containing the frames for the video of the Video Pane.
     */
    protected File frameDirectory;
    
    /**
     * The set of frames for the Video Pane.
     */
    protected List<BufferedImage> frames = new ArrayList<>();
    
    
    //Constructors
    
    /**
     * The constructor for a Video Pane.
     *
     * @param parent              The parent of the Video Pane.
     * @param color               The color of the Video Pane.
     * @param bounds              The bounds of the Video Pane.
     * @param frameDirectory      The directory containing the frames for the Video Pane.
     * @param fps                 The frames per second to run the Video Pane at.
     * @param loop                Whether or not to loop the Video Pane.
     * @param reverseOnCompletion Whether or not to reverse the Video Pane on completion.
     */
    public VideoPane(AbstractObject parent, Color color, Rectangle bounds, File frameDirectory, int fps, boolean loop, boolean reverseOnCompletion) {
        super(parent, color, bounds);
        
        this.frameDirectory = frameDirectory;
        this.fps = fps;
        this.loop = loop;
        this.reverseOnCompletion = reverseOnCompletion;
        
        loadFrames();
        startVideo();
    }
    
    /**
     * The constructor for a Video Pane.
     *
     * @param parent         The parent of the Video Pane.
     * @param color          The color of the Video Pane.
     * @param bounds         The bounds of the Video Pane.
     * @param frameDirectory The directory containing the frames for the Video Pane.
     * @param fps            The frames per second to run the Video Pane at.
     * @param loop           Whether or not to loop the Video Pane.
     */
    public VideoPane(AbstractObject parent, Color color, Rectangle bounds, File frameDirectory, int fps, boolean loop) {
        this(parent, color, bounds, frameDirectory, fps, loop, DEFAULT_REVERSE_ON_COMPLETION);
    }
    
    /**
     * The constructor for a Video Pane.
     *
     * @param parent         The parent of the Video Pane.
     * @param color          The color of the Video Pane.
     * @param bounds         The bounds of the Video Pane.
     * @param frameDirectory The directory containing the frames for the Video Pane.
     * @param fps            The frames per second to run the Video Pane at.
     */
    public VideoPane(AbstractObject parent, Color color, Rectangle bounds, File frameDirectory, int fps) {
        this(parent, color, bounds, frameDirectory, fps, DEFAULT_LOOP);
    }
    
    /**
     * The constructor for a Video Pane.
     *
     * @param parent         The parent of the Video Pane.
     * @param color          The color of the Video Pane.
     * @param bounds         The bounds of the Video Pane.
     * @param frameDirectory The directory containing the frames for the Video Pane.
     */
    public VideoPane(AbstractObject parent, Color color, Rectangle bounds, File frameDirectory) {
        this(parent, color, bounds, frameDirectory, DEFAULT_FPS);
    }
    
    
    //Methods
    
    /**
     * Loads the frames for the Video Pane from the frames directory.
     */
    private void loadFrames() {
        File[] frameList = frameDirectory.listFiles();
        if (frameList == null) {
            return;
        }
        
        for (File frameEntry : frameList) {
            BufferedImage image = ImageUtility.loadImage(frameEntry);
            if (image != null) {
                frames.add(image);
            }
        }
    }
    
    /**
     * Starts the video for the Video Pane.
     */
    private void startVideo() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int index = -1;
            
            int step = 1;
            
            @Override
            public void run() {
                index += step;
                if (index < 0 || index > (frames.size() - 1)) {
                    return;
                }
                setImage(frames.get(index));
                if (index == (frames.size() - 1)) {
                    if (reverseOnCompletion) {
                        step = -1;
                    } else if (loop) {
                        index = 0;
                    }
                }
                if ((index == 0) && (step == -1) && loop) {
                    step = 1;
                }
            }
        }, 0, 1000 / fps);
    }
    
}
