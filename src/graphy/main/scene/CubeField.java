/*
 * File:    CubeField.java
 * Package: graphy.main.scene
 * Author:  Zachary Gill
 */

package graphy.main.scene;

import java.awt.Color;

import graphy.camera.Camera;
import graphy.main.Environment;
import graphy.math.vector.Vector;
import graphy.object.base.Scene;
import graphy.object.polyhedron.regular.platonic.Hexahedron;

/**
 * Defines a Cube Field scene.
 */
public class CubeField extends Scene {
    
    //Main Method
    
    /**
     * The main method for the Cube Field scene.
     *
     * @param args The arguments to the main method.
     * @throws Exception When the Scene cannot be created.
     */
    public static void main(String[] args) throws Exception {
        runScene(CubeField.class);
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Cube Field scene.
     *
     * @param environment The Environment to render the Cube Field in.
     */
    public CubeField(Environment environment) {
        super(environment);
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Cube Field.
     */
    @Override
    public void calculate() {
        for (int i = 0; i < 300; i++) {
            Hexahedron h = new Hexahedron(new Vector(Math.random() * 200 - 100, Math.random() * 200 - 100, Math.random() * 200 - 100), Color.BLUE, Math.random() * 3);
            h.addFrame(Color.BLACK);
            registerComponent(h);
        }
    }
    
    /**
     * Sets up components for the Cube Field scene.
     */
    @Override
    public void initComponents() {
    }
    
    /**
     * Sets up cameras for the Cube Field scene.
     */
    @Override
    public void setupCameras() {
        Camera camera = new Camera(this, environment.perspective, true, true);
        camera.setLocation(4, 0, Math.PI / 2);
        camera.setMode(Camera.Perspective.FIRST_PERSON);
    }
    
    /**
     * Sets up controls for the Cube Field scene.
     */
    @Override
    public void setupControls() {
    }
    
}
