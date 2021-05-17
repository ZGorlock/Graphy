/*
 * File:    SphereField.java
 * Package: graphy.main.scene
 * Author:  Zachary Gill
 */

package graphy.main.scene;

import java.awt.Color;

import commons.math.component.vector.Vector;
import graphy.camera.Camera;
import graphy.main.Environment;
import graphy.object.base.Frame;
import graphy.object.base.Scene;
import graphy.object.sphere.Sphere;

/**
 * Defines a Sphere Field scene.
 */
public class SphereField extends Scene {
    
    //Static Fields
    
    /**
     * A flag indicating whether or not to run the Sphere Field in simple mode or not.
     */
    public static boolean simple = false;
    
    
    //Main Method
    
    /**
     * The main method for the Sphere Field scene.
     *
     * @param args The arguments to the main method.
     * @throws Exception When the Scene cannot be created.
     */
    public static void main(String[] args) throws Exception {
        runScene(SphereField.class);
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Sphere Field scene.
     *
     * @param environment The Environment to render the Sphere Field in.
     */
    public SphereField(Environment environment) {
        super(environment);
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
                sphere.addRotationAnimation(0, 0, Math.PI / 16);
                Frame f = sphere.addFrame(Color.BLACK);
                f.addColorAnimation(5000, (int) (Math.random() * 5000));
            }
            registerComponent(sphere);
        }
    }
    
    /**
     * Sets up components for the Sphere Field scene.
     */
    @Override
    public void initComponents() {
    }
    
    /**
     * Sets up cameras for the Sphere Field scene.
     */
    @Override
    public void setupCameras() {
        Camera camera = new Camera(this, environment.perspective, true, true);
        camera.setLocation(30, 0, Math.PI / 2);
    }
    
    /**
     * Sets up controls for the Sphere Field scene.
     */
    @Override
    public void setupControls() {
    }
    
}
