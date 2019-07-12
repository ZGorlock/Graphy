/*
 * File:    Drawing.java
 * Package: main.scenes
 * Author:  Zachary Gill
 */

package main.scenes;

import camera.Camera;
import main.Environment;
import math.vector.Vector;
import objects.base.Object;
import objects.base.Scene;
import objects.base.polygon.Rectangle;
import objects.complex.DrawingPane;
import objects.system.Axes;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines a Drawing scene.
 */
public class Drawing extends Scene {
    
    //Main Methods
    
    /**
     * The main method for the Drawing scene.
     *
     * @param args The arguments to the main method.
     */
    public static void main(String[] args) {
        String[] environmentArgs = new String[]{};
        Environment.main(environmentArgs);
        Environment.setupMainKeyListener();
        
        List<Object> objects = createObjects();
        for (Object object : objects) {
            Environment.addObject(object);
        }
        
        setupCameras();
        
        setupControls();
    }
    
    /**
     * Creates objects for the scene.
     *
     * @return A list of Objects that were created for the scene.
     */
    public static List<Object> createObjects() {
        List<Object> objects = new ArrayList<>();
        
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("resource/a.jpg"));
        } catch (IOException ignored) {
        }
        
        Object drawing = new Object(Color.BLACK);
        Rectangle bounds = new Rectangle(new Vector(-2, 0, 3), new Vector(2, 0, 3), new Vector(2, 0, -3), new Vector(-2, 0, -3));
        DrawingPane drawingPane = new DrawingPane(null, Color.BLACK, bounds, image);
        drawing.registerComponent(drawingPane);
        objects.add(drawing);
        
        objects.add(new Axes(5));
        
        return objects;
    }
    
    /**
     * Sets up cameras for the scene.
     */
    public static void setupCameras() {
        Camera camera = new Camera(true, true);
        camera.setLocation(Math.PI / 2, Math.PI / 2, 30);
    }
    
    /**
     * Sets up controls for the scene.
     */
    public static void setupControls() {
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Drawing Scene.
     *
     * @param center The center of the scene.
     */
    public Drawing(Vector center) {
        super(center);
    }
    
}
