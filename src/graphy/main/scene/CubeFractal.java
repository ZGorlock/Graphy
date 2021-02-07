/*
 * File:    CubeFractal.java
 * Package: graphy.main.scene
 * Author:  Zachary Gill
 */

package graphy.main.scene;

import java.awt.Color;

import graphy.camera.Camera;
import graphy.main.Environment;
import graphy.object.base.Scene;

/**
 * Defines a Cube Fractal scene.
 */
public class CubeFractal extends Scene {
    
    //Main Method
    
    /**
     * The main method for the Cube Fractal scene.
     *
     * @param args The arguments to the main method.
     * @throws Exception When the Scene cannot be created.
     */
    public static void main(String[] args) throws Exception {
        runScene(CubeFractal.class);
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Cube Fractal scene.
     *
     * @param environment The Environment to render the Cube Fractal in.
     */
    public CubeFractal(Environment environment) {
        super(environment);
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Cube Fractal.
     */
    @Override
    public void calculate() {
        graphy.object.fractals.CubeFractal cubeFractal = new graphy.object.fractals.CubeFractal(Environment.ORIGIN, Color.BLACK, .25, 2, 5);
        cubeFractal.addColorAnimation(10000, 0);
        cubeFractal.addFrame(Color.WHITE);
        registerComponent(cubeFractal);
    }
    
    /**
     * Sets up components for the Cube Fractal scene.
     */
    @Override
    public void initComponents() {
    }
    
    /**
     * Sets up cameras for the Cube Fractal scene.
     */
    @Override
    public void setupCameras() {
        Camera camera = new Camera(this, environment.perspective, true, true);
        camera.setLocation(1, 0, Math.PI / 2);
    }
    
    /**
     * Sets up controls for the Cube Fractal scene.
     */
    @Override
    public void setupControls() {
    }
    
}
