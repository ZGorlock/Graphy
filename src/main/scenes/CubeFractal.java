/*
 * File:    CubeFractal.java
 * Package: main.scenes
 * Author:  Zachary Gill
 */

package main.scenes;

import camera.Camera;
import main.Environment;
import math.vector.Vector;
import objects.base.Object;
import objects.base.Scene;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines a Cube Fractal scene.
 */
public class CubeFractal extends Scene {    
    
    //Main Methods
    
    /**
     * The main method for the Cube Fractal scene.
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
    
        objects.fractals.CubeFractal cubeFractal = new objects.fractals.CubeFractal(Environment.origin, Color.BLACK, .25, 2, 5);
        cubeFractal.addColorAnimation(10000, 0);
        cubeFractal.addFrame(Color.WHITE);
        objects.add(cubeFractal);
        
        return objects;
    }
    
    /**
     * Sets up cameras for the scene.
     */
    public static void setupCameras() {
        Camera camera = new Camera(true, true);
        camera.setLocation(Math.PI / 2, 0, 2);
    }
    
    /**
     * Sets up controls for the scene.
     */
    public static void setupControls() {
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Cube Fractal Scene.
     *
     * @param center The center of the scene.
     */
    public CubeFractal(Vector center) {
        super(center);
    }
    
}
