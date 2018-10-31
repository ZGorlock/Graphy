/*
 * File:    SphereField.java
 * Package: main.scenes
 * Author:  Zachary Gill
 */

package main.scenes;

import camera.Camera;
import main.Environment;
import math.vector.Vector;
import objects.base.Frame;
import objects.base.Object;
import objects.base.Scene;
import objects.sphere.Sphere;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines a Sphere Field scene.
 */
public class SphereField extends Scene {
    
    //Main Methods
    
    /**
     * The main method for the Sphere Field scene.
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
                
        for (int i = 0; i < 10; i++) {
            Sphere sphere = new Sphere(null, new Vector(Math.random() * 20 - 10, Math.random() * 20 - 10, Math.random() * 20 - 10), Color.BLACK, Color.RED, (int) (Math.random() * 5) + 1, Math.PI / (4 * ((int) (Math.random() * 4) + 1)));
//            sphere.addRotationAnimation(Math.PI / (4 * ((int) (Math.random() * 8) + 1)), Math.PI / (4 * ((int) (Math.random() * 8) + 1)), Math.PI / (4 * ((int) (Math.random() * 8) + 1)));
            sphere.addRotationAnimation(0, 0, Math.PI / 16);
            Frame f = sphere.addFrame(Color.BLACK);
            f.addColorAnimation(5000, (int) (Math.random() * 5000));
            objects.add(sphere);
        }
        
        return objects;
    }
    
    /**
     * Sets up cameras for the scene.
     */
    public static void setupCameras() {
        Camera camera = new Camera(true, true);
        camera.setLocation(Math.PI / 2, 0, 50);
        Camera.setActiveCamera(0);
    }
    
    /**
     * Sets up controls for the scene.
     */
    public static void setupControls() {
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Sphere Field Scene.
     *
     * @param center The center of the scene.
     */
    public SphereField(Vector center) {
        super(center);
    }
    
}
