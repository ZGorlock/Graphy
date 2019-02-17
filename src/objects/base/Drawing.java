/*
 * File:    Drawing.java
 * Package: objects.base
 * Author:  Zachary Gill
 */

package objects.base;

import java.awt.image.BufferedImage;

import main.Environment2D;

/**
 * Defines a Drawing to render.
 */
public class Drawing {
    
    //Fields
    
    /**
     * The Environment to draw in.
     */
    public Environment2D environment;
    
    
    //Constructors
    
    /**
     * Constructs a Drawing.
     * 
     * @param environment The Environment to render the Spirograph in.
     */
    public Drawing(Environment2D environment) {
        this.environment = environment;
        environment.setDrawing(this);
    }
    
    
    //Static Methods
    
    /**
     * Sets up controls for the drawing.
     */
    public static void setupControls() {
    }
    
    
    //Methods
    
    /**
     * Renders the Drawing.
     * 
     * @param img The graphics output.
     */
    public void render(BufferedImage img) {
    }
    
}
