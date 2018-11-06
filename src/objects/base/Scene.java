/*
 * File:    Scene.java
 * Package: objects.base
 * Author:  Zachary Gill
 */

package objects.base;

import math.vector.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines a Scene to render.
 */
public abstract class Scene extends Object {
    
    //Constructors
    
    /**
     * Constructor for a Scene.
     *
     * @param center The center of the scene.
     */
    public Scene(Vector center) {
        super(center, Color.BLACK);
    }
    
    
    //Static Methods
    
    /**
     * Creates objects for the scene.
     */
    public static List<Object> createObjects() {
        return new ArrayList<>();
    }
    
    /**
     * Sets up cameras for the scene.
     */
    public static void setupCameras() {
    }
    
    /**
     * Sets up controls for the scene.
     */
    public static void setupControls() {
    }
    
}
