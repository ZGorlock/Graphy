/*
 * File:    VariablePlaneDemo.java
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
import objects.complex.VariablePlane;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines a Variable Plane Demo scene.
 */
public class VariablePlaneDemo extends Scene {
    
    //Main Methods
    
    /**
     * The main method for the Variable Plane Demo scene.
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
        
        VariablePlane plane = new objects.complex.VariablePlane(Color.WHITE, new Rectangle(new Vector(-10, -10, 0), new Vector(-10, 10, 0), new Vector(10, 10, 0), new Vector(10, -10, 0)), 1, 0.5, 1.0);
        plane.addFrame(Color.BLACK);
        objects.add(plane);
        
        return objects;
    }
    
    /**
     * Sets up cameras for the scene.
     */
    public static void setupCameras() {
        Camera camera = new Camera(true, true);
        camera.setLocation(Math.PI / 4, Math.PI / 4, 5);
    }
    
    /**
     * Sets up controls for the scene.
     */
    public static void setupControls() {
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Variable Plane Demo Scene.
     *
     * @param center The center of the scene.
     */
    public VariablePlaneDemo(Vector center) {
        super(center);
    }
    
}