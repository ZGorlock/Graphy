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
import objects.base.group.RotationGroup;
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
    
//        RegularPolyhedron i = new Icosahedron(null, Environment.origin, new Color(0, 0, 0, 0), 1);
//        i.addFrame(java.awt.Color.BLACK);
//        i.displayVertexIndices();
//        i.displayFaceIndices();
//        objects.add(i);
    
//        objects.add(new Axes(5));
//        Sphere s = new Sphere(null, new Vector(2, 2, 2), Color.BLACK, Color.RED, 1, Math.PI / 16);
//        s.addRotationAnimation( Math.PI / 2, 0,0);
//        s.addRotationAnimation( 0, Math.PI / 2,0);
//        s.addRotationAnimation( 0, 0,Math.PI / 2);
//        objects.add(s);
    
        Axes axes = new Axes(5);
        objects.add(axes);
        
        Hexahedron h1 = new Hexahedron(null, new Vector(1, 1, 2), Color.RED, 1);
        Hexahedron h2 = new Hexahedron(null, new Vector(3, 1, 2), Color.YELLOW, 1);
//        h2.addRotationTransformation(2, 0, 0, 10000);
        Hexahedron h3 = new Hexahedron(null, new Vector(5, 1, 2), Color.BLUE, 1);
        List<Object> objects1 = new ArrayList<>();
        objects1.add(h1);
        objects1.add(h2);
        objects1.add(h3);
        objects.addAll(objects1);
    
        RotationGroup r = new RotationGroup(h2, objects1);
        r.addRotationTransformation(2, 0, 0, 10000);
        
        return objects;
    }
    
    /**
     * Sets up cameras for the scene.
     */
    public static void setupCameras() {
        Camera camera = new Camera(true, true);
        camera.setLocation(Math.PI / 4, 3 * Math.PI / 4, 20);
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
