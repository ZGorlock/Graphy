/*
 * File:    Orbit.java
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
 * Defines an Orbit scene.
 */
public class Orbit extends Scene {
    
    //Main Methods
    
    /**
     * The main method for the Orbit scene.
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
        
        objects.add(new Axes(5));
        
        Hexahedron h1 = new Hexahedron(null, new Vector(0, 0, 0), Color.RED, .5);
        Hexahedron h2 = new Hexahedron(null, new Vector(1, 2, 3), Color.YELLOW, .5);
        h1.addFrame(Color.BLACK);
        h2.addFrame(Color.BLACK);
        objects.add(h1);
        objects.add(h2);
        
        h1.addMovementAnimation(.1, .1, .1);
        h2.addOrbitAnimation(h1, 1000);
//        h2.addOrbitTransformation(h1, .5, 2000);
        
        return objects;
    }
    
    /**
     * Sets up cameras for the scene.
     */
    public static void setupCameras() {
        Camera camera = new Camera(true, true);
        camera.setLocation(Math.PI / 4, 3 * Math.PI / 4, 10);
//        camera.setOffset(new Vector3(5, 2, 3));
    }
    
    /**
     * Sets up controls for the scene.
     */
    public static void setupControls() {
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Orbit Scene.
     *
     * @param center The center of the scene.
     */
    public Orbit(Vector center) {
        super(center);
    }
    
}
