/*
 * File:    RotationEquivalenceDemo.java
 * Package: main.scenes
 * Author:  Zachary Gill
 */

package main.scenes;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

import camera.Camera;
import main.Environment;
import math.vector.Vector;
import objects.base.Scene;
import objects.polyhedron.regular.platonic.Hexahedron;
import objects.system.Axes;
import utility.ColorUtility;

/**
 * Defines a Rotation Equivalence Demo scene.
 */
public class RotationEquivalenceDemo extends Scene {
    
    //Main Method
    
    /**
     * The main method for the Rotation Equivalence Demo scene.
     *
     * @param args The arguments to the main method.
     * @throws Exception When the Scene class cannot be constructed.
     */
    public static void main(String[] args) throws Exception {
        runScene(RotationEquivalenceDemo.class);
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Rotation Equivalence Demo Scene.
     *
     * @param environment The Environment to render the Rotation Equivalence Demo in.
     */
    public RotationEquivalenceDemo(Environment environment) {
        super(environment);
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Rotation Equivalence Demo.
     */
    @Override
    public void calculate() {
        Hexahedron cube1 = new Hexahedron(Environment.ORIGIN.plus(new Vector(0, 4, 0)), Color.BLUE, 2);
        Hexahedron cube2 = new Hexahedron(Environment.ORIGIN.plus(new Vector(0, -4, 0)), Color.BLUE, 2);
        
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
        
        registerComponent(cube1);
        registerComponent(cube2);
        registerComponent(new Axes(5));
    }
    
    /**
     * Sets up components for the Rotation Equivalence Demo scene.
     */
    @Override
    public void initComponents() {
    }
    
    /**
     * Sets up cameras for the Rotation Equivalence Demo scene.
     */
    @Override
    public void setupCameras() {
        Camera camera = new Camera(this, true, true);
        camera.setLocation(Math.PI / 2, 0, 12);
    }
    
    /**
     * Sets up controls for the Rotation Equivalence Demo scene.
     */
    @Override
    public void setupControls() {
    }
    
}
