/*
 * File:    Test.java
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
 * Defines a Test scene.
 */
public class Test extends Scene {
    
    //Main Methods
    
    /**
     * The main method for the Test scene.
     *
     * @param args The arguments to the main method.
     * @throws Exception When the Scene class cannot be constructed.
     */
    public static void main(String[] args) throws Exception {
        runScene(Test.class);
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Test scene.
     *
     * @param environment The Environment to render the Test in.
     */
    public Test(Environment environment) {
        super(environment);
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Test.
     */
    @Override
    public void calculate() {
//        RegularPolyhedron i = new Icosahedron(null, Environment.origin, new Color(0, 0, 0, 0), 1);
//        i.addFrame(java.awt.Color.BLACK);
//        i.displayVertexIndices();
//        i.displayFaceIndices();
//        registerComponent(i);
        
        Axes axes = new Axes(5);
        registerComponent(axes);
        
        Hexahedron h1 = new Hexahedron(null, new Vector(0, 0, 0), Color.RED, 1);
//        Hexahedron h2 = new Hexahedron(null, new Vector(5, 2, 3), Color.YELLOW, 1);
        registerComponent(h1);
//        registerComponent(h2);
    }
    
    /**
     * Sets up components for the Test scene.
     */
    @Override
    public void initComponents() {
    }
    
    /**
     * Sets up cameras for the Test scene.
     */
    @Override
    public void setupCameras() {
        Camera camera = new Camera(this, true, true);
        camera.setLocation(Math.PI / 4, 3 * Math.PI / 4, 5);
        camera.setOffset(new Vector(5, 2, 3));
        
        Camera camera2 = new Camera(this, true, true);
        camera2.setLocation(Math.PI / 4, 3 * Math.PI / 4, 50);
    }
    
    /**
     * Sets up controls for the Test scene.
     */
    @Override
    public void setupControls() {
    }
    
}
