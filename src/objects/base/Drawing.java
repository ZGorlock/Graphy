/*
 * File:    Drawing.java
 * Package: objects.base
 * Author:  Zachary Gill
 */

package objects.base;

import main.Environment2D;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Defines a Drawing to render.
 */
public class Drawing {
    
    //Fields
    
    /**
     * The Environment to draw the Drawing.
     */
    public Environment2D environment;
    
    
    //Constructors
    
    /**
     * Constructs a Drawing.
     *
     * @param environment The Environment to render the Drawing in.
     */
    public Drawing(Environment2D environment) {
        this.environment = environment;
        environment.setDrawing(this);
    }
    
    
    //Methods
    
    /**
     * Sets up components for the Drawing.
     */
    public void initComponents() {
    }
    
    /**
     * Renders the Drawing.
     *
     * @return The rendered image.
     */
    public BufferedImage render() {
        return new BufferedImage((int) environment.drawingSize.getX(), (int) environment.drawingSize.getY(), BufferedImage.TYPE_INT_RGB);
    }
    
    /**
     * Produces an overlay for the Drawing.
     *
     * @param g The graphics output.
     */
    public void overlay(Graphics2D g) {
    }
    
    /**
     * Sets up controls for the Drawing.
     */
    public void setupControls() {
    }
    
}
