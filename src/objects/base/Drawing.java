/*
 * File:    Drawing.java
 * Package: objects.base
 * Author:  Zachary Gill
 */

package objects.base;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;

import main.Environment2D;

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
     * Sets up controls for the Drawing.
     */
    public void setupControls() {
    }
    
    /**
     * Starts drawing the Drawing.
     */
    public void run() {
    }
    
    /**
     * Renders the Drawing.
     *
     * @return The rendered image.
     */
    public BufferedImage render() {
        return new BufferedImage(Environment2D.drawingX, Environment2D.drawingY, BufferedImage.TYPE_INT_RGB);
    }
    
    /**
     * Produces an overlay for the Drawing.
     *
     * @param g The graphics output.
     */
    public void overlay(Graphics2D g) {
    }
    
    /**
     * Determines the name of the Drawing.
     *
     * @return The name of the Drawing.
     */
    public String getName() {
        return getClass().getSimpleName().replaceAll("(?<![0-9])([A-Z0-9])", " $1");
    }
    
    
    //Static Methods
    
    /**
     * Runs a Drawing.
     *
     * @param drawingClass The class of Drawing to run.
     * @throws Exception When the Drawing class cannot be constructed.
     */
    protected static void runDrawing(Class<? extends Drawing> drawingClass) throws Exception {
        Environment2D environment = new Environment2D();
        environment.setFps(0);
        environment.setup();
        
        Constructor<? extends Drawing> constructor = drawingClass.getDeclaredConstructor(Environment2D.class);
        Drawing drawing = constructor.newInstance(environment);
        drawing.initComponents();
        drawing.setupControls();
        drawing.run();
        
        environment.run();
    }
    
}
