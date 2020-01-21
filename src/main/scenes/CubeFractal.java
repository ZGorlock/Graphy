/*
 * File:    CubeFractal.java
 * Package: main.scenes
 * Author:  Zachary Gill
 */

package main.scenes;

import java.awt.Color;

import camera.Camera;
import main.Environment;
import objects.base.Scene;

/**
 * Defines a Cube Fractal scene.
 */
public class CubeFractal extends Scene {
    
    //Main Method
    
    /**
     * The main method for the Cube Fractal scene.
     *
     * @param args The arguments to the main method.
     */
    public static void main(String[] args) {
        Environment environment = new Environment();
        environment.setup();
        environment.setupMainKeyListener();
        
        CubeFractal cubeFractal = new CubeFractal(environment);
        cubeFractal.setupCameras();
        cubeFractal.setupControls();
        
        environment.addObject(cubeFractal);
        environment.run();
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Cube Fractal scene.
     *
     * @param environment The Environment to render the Cube Fractal in.
     */
    public CubeFractal(Environment environment) {
        super(environment);
        
        calculate();
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Cube Fractal.
     */
    @Override
    public void calculate() {
        objects.fractals.CubeFractal cubeFractal = new objects.fractals.CubeFractal(Environment.ORIGIN, Color.BLACK, .25, 2, 5);
        cubeFractal.addColorAnimation(10000, 0);
        cubeFractal.addFrame(Color.WHITE);
        registerComponent(cubeFractal);
    }
    
    /**
     * Sets up cameras for the Cube Fractal scene.
     */
    @Override
    public void setupCameras() {
        Camera camera = new Camera(this, true, true);
        camera.setLocation(Math.PI / 2, 0, 1);
    }
    
    /**
     * Sets up controls for the Cube Fractal scene.
     */
    @Override
    public void setupControls() {
    }
    
}
