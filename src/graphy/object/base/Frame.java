/*
 * File:    Frame.java
 * Package: graphy.object
 * Author:  Zachary Gill
 */

package graphy.object.base;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import commons.graphics.DrawUtility;
import commons.math.component.vector.Vector;

/**
 * Defines a Frame Object for Objects.
 */
public class Frame extends Object {
    
    //Fields
    
    /**
     * The Object that the Frame was created for.
     */
    protected AbstractObject base;
    
    
    //Constructors
    
    /**
     * The constructor for a Frame.
     *
     * @param base  The Object to create a frame for.
     * @param color The color of the Frame.
     */
    public Frame(AbstractObject base, Color color) {
        super(base.getCenter(), color);
        
        this.base = base;
        base.registerFrame(this);
        setColor(color);
    }
    
    /**
     * The constructor for a Frame.
     *
     * @param base The Object to create a frame for.
     */
    public Frame(Object base) {
        this(base, Color.BLACK);
    }
    
    
    //Methods
    
    /**
     * Calculates the edges of the Frame.
     */
    @Override
    protected void calculate() {
    }
    
    /**
     * Prepares the Object to be rendered.
     *
     * @param perspective The perspective to render the Object for.
     * @return The list of BaseObjects that were prepared.
     */
    @Override
    public List<BaseObject> prepare(UUID perspective) {
        return new ArrayList<>();
    }
    
    /**
     * Renders the Object on the screen.
     *
     * @param g2 The 2D Graphics entity.
     * @param vs The list of vectors of the Object that owns the frame.
     */
    public void render(Graphics2D g2, List<Vector> vs) {
        if (!visible || base.displayMode != DisplayMode.FACE) {
            return;
        }
        
        DrawUtility.setColor(g2, color);
        
        if (vs.size() > 1) {
            for (int i = 1; i < vs.size(); i++) {
                DrawUtility.drawLine(g2, vs.get(i - 1), vs.get(i));
                if (i == vs.size() - 1 && i > 1) {
                    DrawUtility.drawLine(g2, vs.get(i), vs.get(0));
                }
            }
        }
    }
    
    /**
     * Renders the Object on the screen.
     *
     * @param perspective The perspective to render the Object for.
     * @param g2          The 2D Graphics entity.
     */
    @Override
    public void render(Graphics2D g2, UUID perspective) {
    }
    
}
