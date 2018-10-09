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
import objects.polyhedron.regular.platonic.Dodecahedron;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Test extends Scene {
    
    /**
     * Constructor for a Scene.
     *
     * @param center The center of the scene.
     */
    public Test(Vector center) {
        super(center);
    }
    
    
    //Main Method
    
    /**
     * The main method for the Rubik's Cube scene.
     *
     * @param args The arguments to the main method.
     */
    public static void main(String[] args) {
        String[] environmentArgs = new String[] {};
        Environment.main(environmentArgs);
        
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
    
        Dodecahedron i = new Dodecahedron(null, Environment.origin, new Color(0, 0, 0, 0), 3);
        i.addFrame(java.awt.Color.BLACK);
        i.displayVertexIndices();
        i.displayFaceIndices();
        objects.add(i);
        
        return objects;
    }
    
    /**
     * Sets up cameras for the scene.
     */
    public static void setupCameras() {
        Camera camera = new Camera(true, true);
        camera.setLocation(Math.PI / 2, 0, 5);
        Camera.setActiveCamera(0);
    }
    
    /**
     * Sets up controls for the scene.
     */
    public static void setupControls() {
        
    }
    
}
