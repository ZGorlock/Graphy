/*
 * File:    AnimatedCube.java
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
import objects.base.Frame;
import objects.base.Object;
import objects.base.Scene;
import objects.polyhedron.regular.platonic.Hexahedron;
import utility.ColorUtility;

/**
 * Defines a Animated Cube scene.
 */
public class AnimatedCube extends Scene {
    
    //Main Methods
    
    /**
     * The main method for the Animated Cube scene.
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
        
        Hexahedron cube = new Hexahedron(Environment.origin, Color.BLUE, 2);
        cube.addRotationAnimation(Math.PI / 4, Math.PI / 4, Math.PI / 4);
        
        for (int f = 1; f < 6; f++) {
            cube.setFaceColor(f, ColorUtility.getRandomColor());
        }
        Frame frame = cube.addFrame(Color.BLACK);
        
        frame.addColorAnimation(5000, 2500);
        cube.addColorAnimation(5000, 0);
        objects.add(cube);
        
        return objects;
    }
    
    /**
     * Sets up cameras for the scene.
     */
    public static void setupCameras() {
        Camera camera = new Camera(true, true);
        camera.setLocation(Math.PI / 2, 0, 12);
    }
    
    /**
     * Sets up controls for the scene.
     */
    public static void setupControls() {
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Animated Cube Scene.
     *
     * @param center The center of the scene.
     */
    public AnimatedCube(Vector center) {
        super(center);
    }
    
}
