/*
 * File:    PerspectiveDice.java
 * Package: main.scenes
 * Author:  Zachary Gill
 */

package main.scenes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import camera.Camera;
import main.Environment;
import math.vector.Vector;
import objects.base.Object;
import objects.base.Scene;
import objects.base.polygon.Rectangle;
import objects.complex.VariablePlane;
import objects.system.Axes;
import objects.system.Origin;

public class PerspectiveDice extends Scene {
    
    //Main Methods
    
    /**
     * The main method for the Rubik's Cube scene.
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
        objects.add(new Axes(5));
        
        Origin origin = new Origin();
        objects.add(origin);
        
        Rectangle floorBounds = new Rectangle(new Vector(-10, -10, -.25), new Vector(-10, 10, -.25), new Vector(10, 10, -.25), new Vector(10, -10, -.25));
        VariablePlane floor = new VariablePlane(Color.WHITE, floorBounds, 0.5, .065, 1.5);
        floor.addFrame(Color.BLACK);
        objects.add(floor);
        
        return objects;
    }
    
    /**
     * Sets up cameras for the scene.
     */
    public static void setupCameras() {
        Camera camera = new Camera(true, true);
        camera.setLocation(0, Math.PI / 2, 3);
    }
    
    /**
     * Sets up controls for the scene.
     */
    public static void setupControls() {
    }
    
    
    //Constructors
    
    /**
     * Constructor for a Perspective Dice scene.
     *
     * @param center The center of the scene.
     */
    public PerspectiveDice(Vector center) {
        super(center);
    }
    
}
