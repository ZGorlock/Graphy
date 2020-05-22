/*
 * File:    Orbit.java
 * Package: main.scenes
 * Author:  Zachary Gill
 */

package main.scenes;

import java.awt.Color;

import camera.Camera;
import main.Environment;
import math.vector.Vector;
import objects.base.Scene;
import objects.polyhedron.regular.platonic.Hexahedron;
import objects.system.Axes;

/**
 * Defines an Orbit Demo scene.
 */
public class OrbitDemo extends Scene {
    
    //Main Method
    
    /**
     * The main method for the Orbit Demo scene.
     *
     * @param args The arguments to the main method.
     * @throws Exception When the Scene class cannot be constructed.
     */
    public static void main(String[] args) throws Exception {
        runScene(OrbitDemo.class);
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Orbit Demo scene.
     *
     * @param environment The Environment to render the Orbit Demo in.
     */
    public OrbitDemo(Environment environment) {
        super(environment);
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Orbit Demo.
     */
    @Override
    public void calculate() {
        registerComponent(new Axes(5));
        
        Hexahedron h1 = new Hexahedron(null, new Vector(0, 0, 0), Color.RED, .5);
        Hexahedron h2 = new Hexahedron(null, new Vector(0, 5, 3), Color.YELLOW, .5);
        Hexahedron h3 = new Hexahedron(null, new Vector(0, 7, 1), Color.BLUE, .5);
        h1.addFrame(Color.BLACK);
        h2.addFrame(Color.BLACK);
        h3.addFrame(Color.BLACK);
        registerComponent(h1);
        registerComponent(h2);
        registerComponent(h3);

//        h1.addMovementAnimation(.1, .1, .1);
        h3.addOrbitAnimation(h2, 2500);
        h2.addOrbitAnimation(h1, 10000);
//        h2.addOrbitTransformation(h1, .5, 2000);
    }
    
    /**
     * Sets up components for the Orbit Demo scene.
     */
    @Override
    public void initComponents() {
    }
    
    /**
     * Sets up cameras for the Orbit Demo scene.
     */
    @Override
    public void setupCameras() {
        Camera camera = new Camera(this, environment.perspective, true, true);
        camera.setLocation(Math.PI / 4, 3 * Math.PI / 4, 10);
//        camera.setOffset(new Vector3(5, 2, 3));
    }
    
    /**
     * Sets up controls for the Orbit Demo scene.
     */
    @Override
    public void setupControls() {
    }
    
}
