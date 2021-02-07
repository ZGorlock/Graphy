/*
 * File:    AnimatedCube.java
 * Package: graphy.main.scene
 * Author:  Zachary Gill
 */

package graphy.main.scene;

import java.awt.Color;

import graphy.camera.Camera;
import graphy.main.Environment;
import graphy.object.base.Frame;
import graphy.object.base.Scene;
import graphy.object.polyhedron.regular.platonic.Hexahedron;
import graphy.utility.ColorUtility;

/**
 * Defines a Animated Cube scene.
 */
public class AnimatedCube extends Scene {
    
    //Main Method
    
    /**
     * The main method for the Animated Cube scene.
     *
     * @param args The arguments to the main method.
     * @throws Exception When the Scene cannot be created.
     */
    public static void main(String[] args) throws Exception {
        runScene(AnimatedCube.class);
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Animated Cube scene.
     *
     * @param environment The Environment to render the Animated Cube in.
     */
    public AnimatedCube(Environment environment) {
        super(environment);
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Animated Cube.
     */
    @Override
    public void calculate() {
        Hexahedron cube = new Hexahedron(Environment.ORIGIN, Color.BLUE, 2);
        cube.addRotationAnimation(Math.PI / 4, Math.PI / 4, Math.PI / 4);
        
        for (int f = 1; f < 6; f++) {
            cube.setFaceColor(f, ColorUtility.getRandomColor());
        }
        Frame frame = cube.addFrame(Color.BLACK);
        
        frame.addColorAnimation(5000, 2500);
        cube.addColorAnimation(5000, 0);
        registerComponent(cube);
    }
    
    /**
     * Sets up components for the Animated Cube scene.
     */
    @Override
    public void initComponents() {
    }
    
    /**
     * Sets up cameras for the Animated Cube scene.
     */
    @Override
    public void setupCameras() {
        Camera camera = new Camera(this, environment.perspective, true, true);
        camera.setLocation(8, 0, Math.PI / 2);
    }
    
    /**
     * Sets up controls for the Animated Cube scene.
     */
    @Override
    public void setupControls() {
    }
    
}
