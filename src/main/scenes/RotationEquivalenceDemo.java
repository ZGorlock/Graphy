/*
 * File:    RotationEquivalenceDemo.java
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
import utility.ColorUtility;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Defines a Rotation Equivalence Demo scene.
 */
public class RotationEquivalenceDemo extends Scene {
    
    //Main Methods
    
    /**
     * The main method for the Rotation Equivalence Demo scene.
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
        
        Hexahedron cube1 = new Hexahedron(Environment.origin.plus(new Vector(0, 4, 0)), Color.BLUE, 2);
        Hexahedron cube2 = new Hexahedron(Environment.origin.plus(new Vector(0, -4, 0)), Color.BLUE, 2);
        
        cube1.addRotationTransformation(Math.PI, 0, 0, 2500);
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            boolean done = false;
            
            @Override
            public void run() {
                if (!done && !cube1.inRotationTransformation.get()) {
                    done = true;
                    cube2.addRotationTransformation(0, Math.PI, 0, 2500);
                    
                    Timer t2 = new Timer();
                    t2.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            if (!cube2.inRotationTransformation.get()) {
                                cube2.addRotationTransformation(0, 0, Math.PI, 2500);
                                t2.purge();
                                t2.cancel();
                                t.purge();
                                t.cancel();
                            }
                        }
                    }, 0, 20);
                }
            }
        }, 0, 20);
        
        for (int f = 1; f < 6; f++) {
            Color c = ColorUtility.getRandomColor();
            cube1.setFaceColor(f, c);
            cube2.setFaceColor(f, c);
        }
        cube1.addFrame(Color.BLACK);
        cube2.addFrame(Color.BLACK);
        
        objects.add(cube1);
        objects.add(cube2);
        objects.add(new Axes(5));
        
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
     * Constructor for the Rotation Equivalence Demo Scene.
     *
     * @param center The center of the scene.
     */
    public RotationEquivalenceDemo(Vector center) {
        super(center);
    }
    
}
