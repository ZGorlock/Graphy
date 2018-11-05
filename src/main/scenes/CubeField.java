/*
 * File:    CubeField.java
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

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines a Cube Field scene.
 */
public class CubeField extends Scene {    
    
    //Main Methods
    
    /**
     * The main method for the Cube Field scene.
     *
     * @param args The arguments to the main method.
     */
    public static void main(String[] args) {
        String[] environmentArgs = new String[] {};
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

        for (int i = 0; i < 300; i++) {
            Hexahedron h = new Hexahedron(new Vector(Math.random() * 200 - 100, Math.random() * 200 - 100, Math.random() * 200 - 100), Color.BLUE, Math.random() * 3);
            h.addFrame(Color.BLACK);
            objects.add(h);
        }
        
        return objects;
    }
    
    /**
     * Sets up cameras for the scene.
     */
    public static void setupCameras() {
        Camera camera = new Camera(true, true);
        camera.setLocation(Math.PI / 2, 0, 4);
    }
    
    /**
     * Sets up controls for the scene.
     */
    public static void setupControls() {
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Cube Field Scene.
     *
     * @param center The center of the scene.
     */
    public CubeField(Vector center) {
        super(center);
    }
    
}
