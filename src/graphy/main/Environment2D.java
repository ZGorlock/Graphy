/*
 * File:    Environment.java
 * Package: graphy.main
 * Author:  Zachary Gill
 */

package graphy.main;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import javax.management.InstanceAlreadyExistsException;

import commons.math.component.vector.Vector;
import graphy.object.base.Drawing;

/**
 * The main Environment.
 */
public class Environment2D extends EnvironmentBase {
    
    
    //Fields
    
    /**
     * The Drawing to render.
     */
    public Drawing drawing = null;
    
    
    //Constructors
    
    /**
     * The default constructor for an Environment2D.
     *
     * @throws InstanceAlreadyExistsException When an Environment is already defined.
     */
    public Environment2D() throws InstanceAlreadyExistsException {
        if (!instanced.compareAndSet(false, true)) {
            throw new InstanceAlreadyExistsException("Environment is already defined");
        }
        
        background = null;
        layout = new BorderLayout();
        
        initializeCommons();
    }
    
    
    //Methods
    
    /**
     * Renders the Environment.
     *
     * @param g2 The 2D Graphics entity.
     */
    @Override
    protected void render(Graphics2D g2) {
        Drawing.doRender(drawing, g2);
    }
    
    
    //Setters
    
    /**
     * Sets the Drawing to render.
     *
     * @param drawing The Drawing to render.
     */
    public void setDrawing(Drawing drawing) {
        this.drawing = drawing;
    }
    
    
    //Static Methods
    
    /**
     * Returns a random position on the screen.
     *
     * @return A random position on the screen.
     */
    public static Vector getRandomPosition() {
        return new Vector(width, height).times(new Vector(Math.random(), Math.random()));
    }
    
    /**
     * Returns the center position on the screen.
     *
     * @return The center position on the screen.
     */
    public static Vector getCenterPosition() {
        return new Vector(width, height).scale(0.5);
    }
    
}
