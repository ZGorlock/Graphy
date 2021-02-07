/*
 * File:    VariablePlaneDemo.java
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
import objects.complex.VariablePlane;

/**
 * Defines a Variable Plane Demo scene.
 */
public class VariablePlaneDemo extends Scene {
    
    //Main Methods
    
    /**
     * The main method for the Variable Plane Demo scene.
     *
     * @param args The arguments to the main method.
     * @throws Exception When the Scene cannot be created.
     */
    public static void main(String[] args) throws Exception {
        runScene(VariablePlaneDemo.class);
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Variable Plane Demo scene.
     *
     * @param environment The Environment to render the Variable Plane Demo in.
     */
    public VariablePlaneDemo(Environment environment) {
        super(environment);
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Variable Plane Demo.
     */
    @Override
    public void calculate() {
        VariablePlane plane = new objects.complex.VariablePlane(Color.WHITE, new Rectangle(new Vector(-10, -10, 0), new Vector(-10, 10, 0), new Vector(10, 10, 0), new Vector(10, -10, 0)), 1, 0.5, 1.0);
        plane.addFrame(Color.BLACK);
        registerComponent(plane);
    }
    
    /**
     * Sets up components for the Variable Plane Demo scene.
     */
    @Override
    public void initComponents() {
    }
    
    /**
     * Sets up cameras for the Variable Plane Demo scene.
     */
    @Override
    public void setupCameras() {
        Camera camera = new Camera(this, environment.perspective, true, true);
        camera.setLocation(5, Math.PI / 4, Math.PI / 4);
    }
    
    /**
     * Sets up controls for the Variable Plane Demo scene.
     */
    @Override
    public void setupControls() {
    }
    
}
