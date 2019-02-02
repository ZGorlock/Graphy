/*
 * File:    Drawing.java
 * Package: objects.base
 * Author:  Zachary Gill
 */

package objects.base;

import java.awt.Graphics2D;

/**
 * Defines a Drawing to render.
 */
public abstract class Drawing {
    
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
     * @param graphics The graphics output.
     */
    public abstract void render(Graphics2D graphics);
    
}
