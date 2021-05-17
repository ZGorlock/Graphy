/*
 * File:    MirrorDemo.java
 * Package: graphy.main.scene
 * Author:  Zachary Gill
 */

package graphy.main.scene;

import java.awt.Color;

import commons.math.component.vector.Vector;
import graphy.camera.Camera;
import graphy.main.Environment;
import graphy.object.base.Scene;
import graphy.object.base.polygon.Rectangle;
import graphy.object.complex.pane.MirrorPane;
import graphy.object.complex.pane.Pane;
import graphy.object.polyhedron.regular.platonic.Hexahedron;
import graphy.object.system.Axes;

/**
 * Defines an Mirror Demo scene.
 */
public class MirrorDemo extends Scene {
    
    //Main Method
    
    /**
     * The main method for the Mirror Demo scene.
     *
     * @param args The arguments to the main method.
     * @throws Exception When the Scene cannot be created.
     */
    public static void main(String[] args) throws Exception {
        runScene(MirrorDemo.class);
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Mirror Demo scene.
     *
     * @param environment The Environment to render the Mirror Demo in.
     */
    public MirrorDemo(Environment environment) {
        super(environment);
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Mirror Demo.
     */
    @Override
    public void calculate() {
        Rectangle bounds = new Rectangle(new Vector(-20, 0, 30), new Vector(20, 0, 30), new Vector(20, 0, -30), new Vector(-20, 0, -30));
        Pane mirror = new MirrorPane(null, Color.BLACK, bounds, environment.perspective);
        registerComponent(mirror);
        
        for (int i = 0; i < 300; i++) {
            Hexahedron h = new Hexahedron(new Vector(Math.random() * 200 - 100, Math.random() * 200 - 100, Math.random() * 200 - 100), Color.BLUE, Math.random() * 3);
            h.addFrame(Color.BLACK);
            registerComponent(h);
        }
        
        registerComponent(new Axes(5));
    }
    
    /**
     * Sets up components for the Mirror Demo scene.
     */
    @Override
    public void initComponents() {
    }
    
    /**
     * Sets up cameras for the Mirror Demo scene.
     */
    @Override
    public void setupCameras() {
        Camera camera = new Camera(this, environment.perspective, true, true);
        camera.setLocation(100, Math.PI / 4, Math.PI / 2);
    }
    
    /**
     * Sets up controls for the Mirror Demo scene.
     */
    @Override
    public void setupControls() {
    }
    
}
