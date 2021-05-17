/*
 * File:    MapPanningDemo.java
 * Package: graphy.main.scene
 * Author:  Zachary Gill
 */

package graphy.main.scene;

import java.awt.Color;

import commons.math.component.vector.Vector;
import graphy.camera.Camera;
import graphy.main.Environment;
import graphy.object.base.Scene;
import graphy.object.base.polygon.Square;

/**
 * Defines an Map Panning Demo scene.
 */
public class MapPanningDemo extends Scene {
    
    //Static Fields
    
    /**
     * The dimensions of the map.
     */
    public static final Vector MAP_DIM = new Vector(100, 100);
    
    /**
     * The size of each piece of the map.
     */
    public static final double PIECE_SIZE = 0.5;
    
    
    //Main Method
    
    /**
     * The main method for the Map Panning Demo scene.
     *
     * @param args The arguments to the main method.
     * @throws Exception When the Scene cannot be created.
     */
    public static void main(String[] args) throws Exception {
        runScene(MapPanningDemo.class);
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Map Panning Demo scene.
     *
     * @param environment The Environment to render the Map Panning Demo in.
     */
    public MapPanningDemo(Environment environment) {
        super(environment);
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Map Panning Demo.
     */
    @Override
    public void calculate() {
        for (int x = 0; x < MAP_DIM.getRawX(); x++) {
            for (int y = 0; y < MAP_DIM.getRawY(); y++) {
                Square square = new Square(Color.WHITE, new Vector((x - (MAP_DIM.getRawX() / 2)) * PIECE_SIZE, (y - (MAP_DIM.getRawY() / 2)) * PIECE_SIZE, 0), PIECE_SIZE);
                square.addFrame(Color.BLACK);
                registerComponent(square);
            }
        }
    }
    
    /**
     * Sets up components for the Map Panning Demo scene.
     */
    @Override
    public void initComponents() {
    }
    
    /**
     * Sets up cameras for the Map Panning Demo scene.
     */
    @Override
    public void setupCameras() {
        Camera camera = new Camera(this, environment.perspective, false, true);
        camera.setLocation(10, Math.PI / 2, 0);
        camera.setPanMode(true);
    }
    
    /**
     * Sets up controls for the Map Panning Demo scene.
     */
    @Override
    public void setupControls() {
    }
    
}
