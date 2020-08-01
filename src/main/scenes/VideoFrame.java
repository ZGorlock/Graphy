/*
 * File:    VideoFrame.java
 * Package: main.scenes
 * Author:  Zachary Gill
 */

package main.scenes;

import java.awt.Color;
import java.io.File;

import camera.Camera;
import main.Environment;
import math.vector.Vector;
import objects.base.Scene;
import objects.base.polygon.Rectangle;
import objects.complex.pane.Pane;
import objects.complex.pane.VideoPane;
import objects.system.Axes;

/**
 * Defines a Video Frame scene.
 */
public class VideoFrame extends Scene {
    
    //Static Fields
    
    /**
     * The directory containing the frames for the video of the Video Frame.
     */
    public static File frameDirectory = new File("resource/MandelbrotZoom");
    
    
    //Main Method
    
    /**
     * The main method for the Video Frame scene.
     *
     * @param args The arguments to the main method.
     * @throws Exception When the Scene cannot be created.
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
        Rectangle bounds = new Rectangle(new Vector(-3, 0, 2), new Vector(3, 0, 2), new Vector(3, 0, -2), new Vector(-3, 0, -2));
        Pane videoFrame = new VideoPane(null, Color.BLACK, bounds, frameDirectory, 20, true, true);
//        videoFrame.addRotationAnimation(Math.PI / 2, Math.PI / 2, Math.PI / 2);
        
        registerComponent(videoFrame);
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
        Camera camera = new Camera(this, environment.perspective, true, true);
        camera.setLocation(Math.PI / 2, Math.PI / 2, 30);
    }
    
    /**
     * Sets up controls for the Video Frame scene.
     */
    @Override
    public void setupControls() {
    }
    
}
