/*
 * File:    Scene.java
 * Package: objects.base
 * Author:  Zachary Gill
 */

package objects.base;

import main.Environment;

import java.awt.*;

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
    
    
    //Methods
    
    /**
     * Sets up components for the Scene.
     */
    public void initComponents() {
    }
    
    /**
     * Sets up cameras for the Scene.
     */
    public void setupCameras() {
    }
    
    /**
     * Sets up controls for the Scene.
     */
    public void setupControls() {
    }
    
}
