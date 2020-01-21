/*
 * File:    PictureFrame.java
 * Package: main.scenes
 * Author:  Zachary Gill
 */

package main.scenes;

import camera.Camera;
import main.Environment;
import math.vector.Vector;
import objects.base.Object;
import objects.base.Scene;
import objects.base.polygon.Rectangle;
import objects.complex.DrawingPane;
import objects.system.Axes;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Defines a Picture Frame scene.
 */
public class PictureFrame extends Scene {
    
    //Main Method
    
    /**
     * The main method for the Picture Frame scene.
     *
     * @param args The arguments to the main method.
     */
    public static void main(String[] args) {
        Environment environment = new Environment();
        environment.setup();
        environment.setupMainKeyListener();
        
        PictureFrame pictureFrame = new PictureFrame(environment);
        pictureFrame.initComponents();
        pictureFrame.setupCameras();
        pictureFrame.setupControls();
        
        environment.addObject(pictureFrame);
        environment.run();
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Picture Frame scene.
     *
     * @param environment The Environment to render the Picture Frame in.
     */
    public PictureFrame(Environment environment) {
        super(environment);
        
        calculate();
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Picture Frame.
     */
    @Override
    public void calculate() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("resource/tree.jpg"));
        } catch (IOException ignored) {
        }
        
        Object drawing = new Object(Color.BLACK);
        Rectangle bounds = new Rectangle(new Vector(-2, 0, 3), new Vector(2, 0, 3), new Vector(2, 0, -3), new Vector(-2, 0, -3));
        DrawingPane drawingPane = new DrawingPane(null, Color.BLACK, bounds, image);
        drawing.registerComponent(drawingPane);
//        drawing.addRotationAnimation(Math.PI / 2, Math.PI / 2, Math.PI / 2);
        
        registerComponent(drawing);
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
        Camera camera = new Camera(this, true, true);
        camera.setLocation(Math.PI / 2, Math.PI / 2, 30);
    }
    
    /**
     * Sets up controls for the Picture Frame scene.
     */
    @Override
    public void setupControls() {
    }
    
}
