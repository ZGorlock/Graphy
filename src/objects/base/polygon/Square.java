/*
 * File:    Rectangle.java
 * Package: objects.base.polygon
 * Author:  Zachary Gill
 */

package objects.base.polygon;

import math.vector.Vector;
import objects.base.AbstractObject;

import java.awt.*;

/**
 * Defines an Square.
 */
public class Square extends Polygon {
    
    //Fields
    
    /**
     * The side length of the Square.
     */
    protected double side;
    
    
    //Constructors
    
    /**
     * The constructor for a Square.
     *
     * @param parent The parent of the Square.
     * @param color  The color of the Square.
     * @param v1     The first point of the Square.
     * @param side   The side length of the Square.
     */
    public Square(AbstractObject parent, Color color, Vector v1, double side) {
        super(parent, color, v1, v1.plus(new Vector(side, 0, 0)), v1.plus(new Vector(side, side, 0)), v1.plus(new Vector(0, side, 0)));
    }
    
    /**
     * The constructor for a Square.
     *
     * @param parent The parent of the Square.
     * @param v1     The first point of the Square.
     * @param side   The side length of the Square.
     */
    public Square(AbstractObject parent, Vector v1, double side) {
        this(parent, Color.BLACK, v1, side);
    }
    
    /**
     * The constructor for a Square.
     *
     * @param color The color of the Square.
     * @param v1    The first point of the Square.
     * @param side  The side length of the Square.
     */
    public Square(Color color, Vector v1, double side) {
        this(null, color, v1, side);
    }
    
    /**
     * The constructor for a Square.
     *
     * @param v1   The first point of the Square.
     * @param side The side length of the Square.
     */
    public Square(Vector v1, double side) {
        this(null, Color.BLACK, v1, side);
    }
    
    
    //Getters
    
    /**
     * Returns the first point of the Square.
     *
     * @return The first point of the Square.
     */
    public Vector getP1() {
        return getVertex(1);
    }
    
    /**
     * Returns the side length of the Square.
     *
     * @return The side length of the Square.
     */
    public double getSide() {
        return side;
    }
    
    
    //Setters
    
    /**
     * Sets the first point of the Square.
     *
     * @param p1 The new first point of the Square.
     */
    public void setP1(Vector p1) {
        setVertex(1, p1);
        setVertex(2, p1.plus(new Vector(0, side, 0)));
        setVertex(3, p1.plus(new Vector(side, side, 0)));
        setVertex(4, p1.plus(new Vector(side, 0, 0)));
    }
    
    /**
     * Sets the side length of the Square.
     *
     * @param side The side length of the Square.
     */
    public void setSide(double side) {
        this.side = side;
        setP1(getP1());
    }
    
}