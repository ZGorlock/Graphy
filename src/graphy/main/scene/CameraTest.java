/*
 * File:    CameraTest.java
 * Package: graphy.main.scene
 * Author:  Zachary Gill
 */

package graphy.main.scene;

import java.awt.Color;

import graphy.camera.Camera;
import graphy.main.Environment;
import graphy.math.vector.Vector;
import graphy.object.base.Scene;
import graphy.object.polyhedron.regular.RegularPolyhedron;
import graphy.object.polyhedron.regular.platonic.Icosahedron;
import graphy.object.system.Axes;

/**
 * Defines a Camera Test scene.
 */
public class CameraTest extends Scene {
    
    //Main Methods
    
    /**
     * The main method for the Camera Test scene.
     *
     * @param args The arguments to the main method.
     * @throws Exception When the Scene cannot be created.
     */
    public static void main(String[] args) throws Exception {
        runScene(CameraTest.class);
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Camera Test scene.
     *
     * @param environment The Environment to render the Camera Test in.
     */
    public CameraTest(Environment environment) {
        super(environment);
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Camera Test.
     */
    @Override
    public void calculate() {
        RegularPolyhedron i = new Icosahedron(null, Environment.origin, new Color(0, 0, 255, 128), 1);
        i.addFrame(java.awt.Color.BLACK);
        i.displayVertexIndices();
        i.displayFaceIndices();
        registerComponent(i);
        
        Axes axes = new Axes(5);
        registerComponent(axes);

//        Hexahedron h1 = new Hexahedron(null, new Vector(0, 0, 0), Color.RED, 1);
//        h1.addFrame(Color.BLACK);
//        registerComponent(h1);
    }
    
    /**
     * Sets up components for the Camera Test scene.
     */
    @Override
    public void initComponents() {
    }
    
    /**
     * Sets up cameras for the Camera Test scene.
     */
    @Override
    public void setupCameras() {
        Camera camera = new Camera(this, environment.perspective, true, true);
        camera.setLocation(5, 3 * Math.PI / 4, Math.PI / 4);
        camera.setOffset(new Vector(3, 2, 5));
        
        Camera camera2 = new Camera(this, environment.perspective, true, true);
        camera2.setLocation(50, 3 * Math.PI / 4, Math.PI / 4);
    }
    
    /**
     * Sets up controls for the Camera Test scene.
     */
    @Override
    public void setupControls() {
    }
    
}
