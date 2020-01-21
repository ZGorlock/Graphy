/*
 * File:    Scene.java
 * Package: objects.base
 * Author:  Zachary Gill
 */

package objects.base;

import java.awt.Color;

import main.Environment;

/**
 * Defines a Scene to render.
 */
public abstract class Scene extends Object {
    
    //Fields
    
    /**
     * The Environment to render the Scene.
     */
    public Environment environment;
    
    
    //Constructors
    
    /**
     * Constructs a Scene.
     *
     * @param environment The Environment to render the Scene in.
     */
    public Scene(Environment environment) {
        super(Environment.ORIGIN, Color.BLACK);
        this.environment = environment;
        environment.setScene(this);
    }
    
    
    //Static Methods
    
    /**
     * Sets up cameras for the scene.
     */
    public void setupCameras() {
    }
    
    /**
     * Sets up controls for the scene.
     */
    public void setupControls() {
    }
    
}
