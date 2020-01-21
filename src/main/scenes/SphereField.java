/*
 * File:    SphereField.java
 * Package: main.scenes
 * Author:  Zachary Gill
 */

package main.scenes;

import java.awt.Color;

import camera.Camera;
import main.Environment;
import math.vector.Vector;
import objects.base.Frame;
import objects.base.Scene;
import objects.sphere.Sphere;

/**
 * Defines a Sphere Field scene.
 */
public class SphereField extends Scene {
    
    //Static Fields
    
    /**
     * A flag indicating whether or not to run the Sphere Field in simple mode or not.
     */
    public static boolean simple = true;
    
    
    //Main Method
    
    /**
     * The main method for the Sphere Field scene.
     *
     * @param args The arguments to the main method.
     */
    public static void main(String[] args) {
        Environment environment = new Environment();
        environment.setup();
        environment.setupMainKeyListener();
        
        SphereField sphereField = new SphereField(environment);
        sphereField.setupCameras();
        sphereField.setupControls();
        
        environment.addObject(sphereField);
        environment.run();
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Sphere Field scene.
     *
     * @param environment The Environment to render the Sphere Field in.
     */
    public SphereField(Environment environment) {
        super(environment);
        
        calculate();
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Sphere Field.
     */
    @Override
    public void calculate() {
        for (int i = 0; i < 10; i++) {
            Sphere sphere;
            if (simple) {
                sphere = new Sphere(null, new Vector(Math.random() * 20 - 10, Math.random() * 20 - 10, Math.random() * 20 - 10), Color.BLACK, (int) (Math.random() * 5) + 1, Math.PI / (4 * ((int) (Math.random() * 4) + 1)));
                sphere.setDisplayMode(DisplayMode.EDGE);
            } else {
                sphere = new Sphere(null, new Vector(Math.random() * 20 - 10, Math.random() * 20 - 10, Math.random() * 20 - 10), Color.BLACK, Color.RED, (int) (Math.random() * 5) + 1, Math.PI / (4 * ((int) (Math.random() * 4) + 1)));
//                sphere.addRotationAnimation(Math.PI / (4 * ((int) (Math.random() * 8) + 1)), Math.PI / (4 * ((int) (Math.random() * 8) + 1)), Math.PI / (4 * ((int) (Math.random() * 8) + 1)));
                sphere.addRotationAnimation(Math.PI / 16, 0, 0);
                Frame f = sphere.addFrame(Color.BLACK);
                f.addColorAnimation(5000, (int) (Math.random() * 5000));
            }
            registerComponent(sphere);
        }
    }
    
    /**
     * Sets up cameras for the Sphere Field scene.
     */
    @Override
    public void setupCameras() {
        Camera camera = new Camera(this, true, true);
        camera.setLocation(Math.PI / 2, 0, 30);
    }
    
    /**
     * Sets up controls for the Sphere Field scene.
     */
    @Override
    public void setupControls() {
    }
    
}
