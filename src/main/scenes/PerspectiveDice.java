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
import objects.polyhedron.regular.platonic.Hexahedron;
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
    
        Origin origin = new Origin();
        objects.add(origin);
        
        Object floor = new Object(Color.BLACK);
        boolean black = true;
        for (int i = -10; i < 10; i++) {
            for (int j = -10; j < 10; j++) {
                Hexahedron floorPiece = new Hexahedron(floor, new Vector(-1, i, j), black ? Color.BLACK : Color.WHITE, 1 / Math.sqrt(2));
                black = !black;
            }
            black = !black;
        }
        objects.add(floor);
        
        return objects;
    }
    
    /**
     * Sets up cameras for the scene.
     */
    public static void setupCameras() {
        Camera camera = new Camera(true, true);
        camera.setLocation(Math.PI / 8, 0, 2);
        Camera.setActiveCamera(0);
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
     * @param center            The center of the scene.
     */
    public PerspectiveDice(Vector center) {
        super(center);
    }
    
}
