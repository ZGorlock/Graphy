/*
 * File:    Drawing.java
 * Package: objects.base
 * Author:  Zachary Gill
 */

package objects.base;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import javax.management.InstanceAlreadyExistsException;

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
     * @param g The graphics output.
     */
    public void render(Graphics2D g) {
        environment.colorBackground(g);
        
        BufferedImage img = draw();
        g.drawImage(img, 0, 0, null);
        overlay(g);
    }
    
    /**
     * Draws the Drawing.
     *
     * @return The image.
     */
    public BufferedImage draw() {
        return new BufferedImage(Environment2D.width, Environment2D.height, BufferedImage.TYPE_INT_RGB);
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
    
    
    //Functions
    
    /**
     * Runs a Drawing.
     *
     * @param drawingClass The class of Drawing to run.
     * @throws InstanceAlreadyExistsException When an Environment is already defined.
     * @throws Exception                      When the Drawing class cannot be constructed.
     */
    protected static void runDrawing(Class<? extends Drawing> drawingClass) throws InstanceAlreadyExistsException, Exception {
        Environment2D environment = new Environment2D();
        environment.setFps(0);
        environment.setDoubleBuffering(false);
        environment.setup();
        environment.setupMainKeyListener();
        
        Constructor<? extends Drawing> constructor = drawingClass.getDeclaredConstructor(Environment2D.class);
        Drawing drawing = constructor.newInstance(environment);
        environment.frame.setTitle(drawingClass.getSimpleName());
        drawing.initComponents();
        drawing.setupControls();
        drawing.run();
        
        environment.run();
    }
    
    /**
     * Renders a Drawing.
     *
     * @param drawing The Drawing.
     * @param g2      The 2D Graphics entity.
     */
    public static void doRender(Drawing drawing, Graphics2D g2) {
        if (drawing != null) {
            drawing.render(g2);
        }
    }
    
}
