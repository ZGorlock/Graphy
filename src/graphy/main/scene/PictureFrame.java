/*
 * File:    PictureFrame.java
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
import graphy.object.complex.pane.PicturePane;
import graphy.object.system.Axes;

/**
 * Defines a Picture Frame scene.
 */
public class PictureFrame extends Scene {
    
    //Static Fields
    
    /**
     * The picture to display on the Picture Frame.
     */
    public static File picture = new File("resources/graphy/tree.jpg");
    
    
    //Main Method
    
    /**
     * The main method for the Picture Frame scene.
     *
     * @param args The arguments to the main method.
     * @throws Exception When the Scene cannot be created.
     */
    public static void main(String[] args) throws Exception {
        runScene(PictureFrame.class);
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Picture Frame scene.
     *
     * @param environment The Environment to render the Picture Frame in.
     */
    public PictureFrame(Environment environment) {
        super(environment);
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Picture Frame.
     */
    @Override
    public void calculate() {
        Rectangle bounds = new Rectangle(new Vector(-2, 0, 3), new Vector(2, 0, 3), new Vector(2, 0, -3), new Vector(-2, 0, -3));
        Pane pictureFrame = new PicturePane(null, Color.BLACK, bounds, picture);
//        pictureFrame.addRotationAnimation(Math.PI / 2, Math.PI / 2, Math.PI / 2);
        
        registerComponent(pictureFrame);
        registerComponent(new Axes(5));
    }
    
    /**
     * Sets up components for the Picture Frame scene.
     */
    @Override
    public void initComponents() {
    }
    
    /**
     * Sets up cameras for the Picture Frame scene.
     */
    @Override
    public void setupCameras() {
        Camera camera = new Camera(this, environment.perspective, true, true);
        camera.setLocation(30, Math.PI / 2, Math.PI / 2);
    }
    
    /**
     * Sets up controls for the Picture Frame scene.
     */
    @Override
    public void setupControls() {
    }
    
}
