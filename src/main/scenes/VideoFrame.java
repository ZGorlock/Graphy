/*
 * File:    VideoFrame.java
 * Package: main.scenes
 * Author:  Zachary Gill
 */

package main.scenes;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;

import camera.Camera;
import main.Environment;
import math.vector.Vector;
import objects.base.Object;
import objects.base.Scene;
import objects.base.polygon.Rectangle;
import objects.complex.DrawingPane;
import objects.system.Axes;

/**
 * Defines a Video Frame scene.
 */
public class VideoFrame extends Scene {
    
    //Fields
    
    /**
     * The frames per second to play the video at.
     */
    private int fps = 20;
    
    /**
     * A flag indicating whether or not to loop the video.
     */
    private boolean loop = true;
    
    /**
     * A flag indicating whether or not to reverse the video on completion.
     */
    private boolean reverseOnCompletion = true;
    
    /**
     * The directory containing the frames for the video.
     */
    private File frameDirectory = new File("resource/MandelbrotZoom");
    
    /**
     * The set of frames for the video.
     */
    private List<BufferedImage> frames = new ArrayList<>();
    
    /**
     * The drawing pane.
     */
    private DrawingPane drawingPane;
    
    
    //Main Method
    
    /**
     * The main method for the Video Frame scene.
     *
     * @param args The arguments to the main method.
     * @throws Exception When the Scene class cannot be constructed.
     */
    public static void main(String[] args) throws Exception {
        runScene(VideoFrame.class);
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Video Frame scene.
     *
     * @param environment The Environment to render the Picture Frame in.
     */
    public VideoFrame(Environment environment) {
        super(environment);
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Video Frame.
     */
    @Override
    public void calculate() {
        File[] frameList = frameDirectory.listFiles();
        if (frameList == null) {
            return;
        }
        
        for (File frameEntry : frameList) {
            BufferedImage image;
            try {
                image = ImageIO.read(frameEntry);
                frames.add(image);
            } catch (IOException ignored) {
            }
        }
        
        Object drawing = new Object(Color.BLACK);
        Rectangle bounds = new Rectangle(new Vector(-3, 0, 2), new Vector(3, 0, 2), new Vector(3, 0, -2), new Vector(-3, 0, -2));
        drawingPane = new DrawingPane(null, Color.BLACK, bounds, null);
        drawing.registerComponent(drawingPane);
        
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
                drawingPane.setImage(frames.get(index));
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
        
        registerComponent(drawing);
        registerComponent(new Axes(5));
    }
    
    /**
     * Sets up components for the Video Frame scene.
     */
    @Override
    public void initComponents() {
    }
    
    /**
     * Sets up cameras for the Video Frame scene.
     */
    @Override
    public void setupCameras() {
        Camera camera = new Camera(this, true, true);
        camera.setLocation(Math.PI / 2, Math.PI / 2, 30);
    }
    
    /**
     * Sets up controls for the Video Frame scene.
     */
    @Override
    public void setupControls() {
    }
    
}
