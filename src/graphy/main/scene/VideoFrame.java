/*
 * File:    VideoFrame.java
 * Package: graphy.main.scene
 * Author:  Zachary Gill
 */

package graphy.main.scene;

import java.awt.Color;
import java.io.File;

import commons.math.vector.Vector;
import graphy.camera.Camera;
import graphy.main.Environment;
import graphy.object.base.Scene;
import graphy.object.base.polygon.Rectangle;
import graphy.object.complex.pane.Pane;
import graphy.object.complex.pane.VideoPane;
import graphy.object.system.Axes;

/**
 * Defines a Video Frame scene.
 */
public class VideoFrame extends Scene {
    
    //Static Fields
    
    /**
     * The directory containing the frames for the video of the Video Frame.
     */
    public static File frameDirectory = new File("resources/graphy/MandelbrotZoom");
    
    
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
        camera.setLocation(30, Math.PI / 2, Math.PI / 2);
    }
    
    /**
     * Sets up controls for the Video Frame scene.
     */
    @Override
    public void setupControls() {
    }
    
}
