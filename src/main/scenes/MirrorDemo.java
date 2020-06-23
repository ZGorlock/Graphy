/*
 * File:    MirrorDemo.java
 * Package: main.scenes
 * Author:  Zachary Gill
 */

package main.scenes;

import java.awt.Color;

import camera.Camera;
import main.Environment;
import math.vector.Vector;
import objects.base.Scene;
import objects.base.polygon.Rectangle;
import objects.complex.pane.MirrorPane;
import objects.complex.pane.Pane;
import objects.system.Axes;

/**
 * Defines an Mirror Demo scene.
 */
public class MirrorDemo extends Scene {
    
    //Main Method
    
    /**
     * The main method for the Mirror Demo scene.
     *
     * @param args The arguments to the main method.
     * @throws Exception When the Scene class cannot be constructed.
     */
    public static void main(String[] args) throws Exception {
        runScene(MirrorDemo.class);
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Mirror Demo scene.
     *
     * @param environment The Environment to render the Mirror Demo in.
     */
    public MirrorDemo(Environment environment) {
        super(environment);
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Mirror Demo.
     */
    @Override
    public void calculate() {
        Rectangle bounds = new Rectangle(new Vector(-2, 0, 3), new Vector(2, 0, 3), new Vector(2, 0, -3), new Vector(-2, 0, -3));
        Pane mirror = new MirrorPane(null, Color.BLACK, bounds, environment.perspective);
//        mirror.setVisible(false);
        
        registerComponent(mirror);
        registerComponent(new Axes(5));
    }
    
    /**
     * Sets up components for the Mirror Demo scene.
     */
    @Override
    public void initComponents() {
    }
    
    /**
     * Sets up cameras for the Mirror Demo scene.
     */
    @Override
    public void setupCameras() {
        Camera camera = new Camera(this, environment.perspective, true, true);
        camera.setLocation(Math.PI / 2, Math.PI / 2, 10);
    }
    
    /**
     * Sets up controls for the Mirror Demo scene.
     */
    @Override
    public void setupControls() {
    }
    
}
