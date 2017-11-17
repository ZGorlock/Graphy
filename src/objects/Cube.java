/*
 * File:    Cube.java
 * Package: models
 * Author:  Zachary Gill
 */

package objects;

import math.vector.Vector;
import objects.base.*;
import objects.base.Object;

import java.awt.*;

/**
 * Defines a Cube Object.
 */
public class Cube extends Object
{
    
    //Fields
    
    /**
     * The side length of the Cube.
     */
    protected double sideLength;
    
    
    //Constructors
    
    /**
     * The constructor for a Cube.
     *
     * @param parent     The parent of the Cube.
     * @param center     The center point of the Cube.
     * @param color      The color of the Cube.
     * @param sideLength The side length of the Cube.
     */
    public Cube(AbstractObject parent, Vector center, Color color, double sideLength)
    {
        super(center, color);
    
        this.sideLength = sideLength;
        setParent(parent);
        calculate();
    }
    
    /**
     * The constructor for a Cube.
     *
     * @param parent     The parent of the Cube.
     * @param center     The center point of the Cube.
     * @param sideLength The side length of the Cube.
     */
    public Cube(AbstractObject parent, Vector center, double sideLength)
    {
        this(parent, center, Color.BLACK, sideLength);
    }
    
    /**
     * The constructor for a Cube.
     *
     * @param center     The center point of the Cube.
     * @param color      The color of the Cube.
     * @param sideLength The side length of the Cube.
     */
    public Cube(Vector center, Color color, double sideLength)
    {
        this(null, center, color, sideLength);
    }
    
    
    //Methods
    
    /**
     * Calculates the structure of the Cube.
     */
    @Override
    protected void calculate()
    {
        components.clear();
        
        double halfSideLength = (sideLength / 2);
    
        new Rect(this,
                new Vector(halfSideLength, halfSideLength, halfSideLength),
                new Vector(halfSideLength, -halfSideLength, halfSideLength),
                new Vector(-halfSideLength, -halfSideLength, halfSideLength),
                new Vector(-halfSideLength, halfSideLength, halfSideLength)
        );
        new Rect(this,
                new Vector(halfSideLength, halfSideLength, -halfSideLength),
                new Vector(halfSideLength, -halfSideLength, -halfSideLength),
                new Vector(-halfSideLength, -halfSideLength, -halfSideLength),
                new Vector(-halfSideLength, halfSideLength, -halfSideLength)
        );
        new Rect(this,
                new Vector(halfSideLength, halfSideLength, halfSideLength),
                new Vector(halfSideLength, halfSideLength, -halfSideLength),
                new Vector(-halfSideLength, halfSideLength, -halfSideLength),
                new Vector(-halfSideLength, halfSideLength, halfSideLength)
        );
        new Rect(this,
                new Vector(halfSideLength, -halfSideLength, halfSideLength),
                new Vector(halfSideLength, -halfSideLength, -halfSideLength),
                new Vector(-halfSideLength, -halfSideLength, -halfSideLength),
                new Vector(-halfSideLength, -halfSideLength, halfSideLength)
        );
        new Rect(this,
                new Vector(halfSideLength, halfSideLength, halfSideLength),
                new Vector(halfSideLength, halfSideLength, -halfSideLength),
                new Vector(halfSideLength, -halfSideLength, -halfSideLength),
                new Vector(halfSideLength, -halfSideLength, halfSideLength)
        );
        new Rect(this,
                new Vector(-halfSideLength, halfSideLength, halfSideLength),
                new Vector(-halfSideLength, halfSideLength, -halfSideLength),
                new Vector(-halfSideLength, -halfSideLength, -halfSideLength),
                new Vector(-halfSideLength, -halfSideLength, halfSideLength)
        );
    
        setColor(this.color);
    }
    
    
    //Getters
    
    /**
     * Returns the side length of the Cube.
     *
     * @return The side length of the Cube.
     */
    public double getSideLength()
    {
        return sideLength;
    }
    
    
    //Setters
    
    /**
     * Sets the color of a face of the Cube.
     *
     * @param face  The face of the Cube.
     * @param color The color.
     */
    public void setFaceColor(int face, Color color)
    {
        if (face < 1 || face > 6) {
            return;
        }
        face--;
        components.get(face).setColor(color);
    }
    
}
