/*
 * File:    MapPanningDemo.java
 * Package: main.scenes
 * Author:  Zachary Gill
 */

package main.scenes;

import camera.Camera;
import main.Environment;
import math.vector.Vector;
import objects.base.Scene;
import objects.base.polygon.Square;

import java.awt.*;

/**
 * Defines an Map Panning Demo scene.
 */
public class MapPanningDemo extends Scene {
    
    //Static Fields
    
    /**
     * The dimensions of the map.
     */
    public static final Vector MAP_DIM = new Vector(250, 250);
    
    /**
     * The size of each piece of the map.
     */
    public static final double PIECE_SIZE = 0.5;
    
    
    //Main Method
    
    /**
     * The main method for the Map Panning Demo scene.
     *
     * @param args The arguments to the main method.
     */
    public static void main(String[] args) {
        Environment environment = new Environment();
        environment.setup();
        environment.setupMainKeyListener();
        
        MapPanningDemo mapMaker2D = new MapPanningDemo(environment);
        mapMaker2D.initComponents();
        mapMaker2D.setupCameras();
        mapMaker2D.setupControls();
        
        environment.addObject(mapMaker2D);
        environment.run();
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Map Panning Demo scene.
     *
     * @param environment The Environment to render the Map Panning Demo in.
     */
    public MapPanningDemo(Environment environment) {
        super(environment);
        
        calculate();
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Map Panning Demo.
     */
    @Override
    public void calculate() {
        for (int x = 0; x < MAP_DIM.getX(); x++) {
            for (int y = 0; y < MAP_DIM.getY(); y++) {
                Square square = new Square(Color.WHITE, new Vector((x - (MAP_DIM.getX() / 2)) * PIECE_SIZE, (y - (MAP_DIM.getY() / 2)) * PIECE_SIZE, 0), PIECE_SIZE);
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
        Camera camera = new Camera(this, false, true);
        camera.setLocation(0, Math.PI / 2, 10);
        camera.setPanMode(true);
    }
    
    /**
     * Sets up controls for the Map Panning Demo scene.
     */
    @Override
    public void setupControls() {
    }
    
}
