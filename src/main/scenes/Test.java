/*
 * File:    Test.java
 * Package: main.scenes
 * Author:  Zachary Gill
 */

package main.scenes;

import camera.Camera;
import main.Environment;
import math.vector.Vector;
import objects.base.Object;
import objects.base.Scene;
import objects.polyhedron.regular.platonic.Hexahedron;
import objects.system.Axes;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines a Test scene.
 */
public class Test extends Scene {
    
    //Main Methods
    
    /**
     * The main method for the Rubik's Cube scene.
     *
     * @param args The arguments to the main method.
     */
    public static void main(String[] args) {
        String[] environmentArgs = new String[]{};
        Environment.main(environmentArgs);
        Environment.setupMainKeyListener();
        
        List<Object> objects = createObjects();
        for (Object object : objects) {
            Environment.addObject(object);
        }
        
        setupCameras();
        
        setupControls();
    }
    
    /**
     * Creates objects for the scene.
     *
     * @return A list of Objects that were created for the scene.
     */
    public static List<Object> createObjects() {
        List<Object> objects = new ArrayList<>();

//        RegularPolyhedron i = new Icosahedron(null, Environment.origin, new Color(0, 0, 0, 0), 1);
//        i.addFrame(java.awt.Color.BLACK);
//        i.displayVertexIndices();
//        i.displayFaceIndices();
//        objects.add(i);
        
        Axes axes = new Axes(5);
        objects.add(axes);
        
        Hexahedron h1 = new Hexahedron(null, new Vector(0, 0, 0), Color.RED, 1);
        Hexahedron h2 = new Hexahedron(null, new Vector(5, 2, 3), Color.YELLOW, 1);
        objects.add(h1);
        objects.add(h2);
        
        return objects;
    }
    
    /**
     * Sets up cameras for the scene.
     */
    public static void setupCameras() {
        Camera camera = new Camera(true, true);
        camera.setLocation(Math.PI / 4, 3 * Math.PI / 4, 20);
        camera.setOffset(new Vector(5, 2, 3));
        
        Camera camera2 = new Camera(true, true);
        camera2.setLocation(Math.PI / 4, 3 * Math.PI / 4, 100);
    }
    
    /**
     * Sets up controls for the scene.
     */
    public static void setupControls() {
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Test Scene.
     *
     * @param center The center of the scene.
     */
    public Test(Vector center) {
        super(center);
    }
    
}
