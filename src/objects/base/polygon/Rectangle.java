/*
 * File:    Rectangle.java
 * Package: objects.base.polygon
 * Author:  Zachary Gill
 */

package objects.base.polygon;

import camera.Camera;
import math.vector.Vector;
import objects.base.AbstractObject;
import objects.base.BaseObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines an Rectangle.
 */
public class Rectangle extends Polygon
{
    
    //Constructors
    
    /**
     * The constructor for a Rectangle.
     *
     * @param parent The parent of the Rectangle.
     * @param color  The color of the Rectangle.
     * @param v1     The first point of the Rectangle.
     * @param v2     The second point of the Rectangle.
     * @param v3     The third point of the Rectangle.
     * @param v4     The fourth point of the Rectangle.
     */
    public Rectangle(AbstractObject parent, Color color, Vector v1, Vector v2, Vector v3, Vector v4)
    {
        super(parent, color, v1, v2, v3, v4);
    }
    
    /**
     * The constructor for a Rectangle.
     *
     * @param parent The parent of the Rectangle.
     * @param v1     The first point of the Rectangle.
     * @param v2     The second point of the Rectangle.
     * @param v3     The third point of the Rectangle.
     * @param v4     The fourth point of the Rectangle.
     */
    public Rectangle(AbstractObject parent, Vector v1, Vector v2, Vector v3, Vector v4)
    {
        this(parent, Color.BLACK, v1, v2, v3, v4);
    }
    
    /**
     * The constructor for a Rectangle.
     *
     * @param color The color of the Rectangle.
     * @param v1    The first point of the Rectangle.
     * @param v2    The second point of the Rectangle.
     * @param v3    The third point of the Rectangle.
     * @param v4    The fourth point of the Rectangle.
     */
    public Rectangle(Color color, Vector v1, Vector v2, Vector v3, Vector v4)
    {
        this(null, color, v1, v2, v3, v4);
    }
    
    /**
     * The constructor for a Rectangle.
     *
     * @param v1 The first point of the Rectangle.
     * @param v2 The second point of the Rectangle.
     * @param v3 The third point of the Rectangle.
     * @param v4 The fourth point of the Rectangle.
     */
    public Rectangle(Vector v1, Vector v2, Vector v3, Vector v4)
    {
        this(null, Color.BLACK, v1, v2, v3, v4);
    }
    
    
    //Getters
    
    /**
     * Returns the first point of the Rectangle.
     *
     * @return The first point of the Rectangle.
     */
    public Vector getP1()
    {
        return getVertex(1);
    }
    
    /**
     * Returns the second point of the Rectangle.
     *
     * @return The second point of the Rectangle.
     */
    public Vector getP2()
    {
        return getVertex(2);
    }
    
    /**
     * Returns the third point of the Rectangle.
     *
     * @return The third point of the Rectangle.
     */
    public Vector getP3()
    {
        return getVertex(3);
    }
    
    /**
     * Returns the fourth point of the Rectangle.
     *
     * @return The fourth point of the Rectangle.
     */
    public Vector getP4()
    {
        return getVertex(4);
    }
    
    
    //Setters
    
    /**
     * Sets the point of the Rectangle.
     *
     * @param p1 The first point of the Rectangle.
     * @param p2 The second point of the Rectangle.
     * @param p3 The third point of the Rectangle.
     * @param p4 The fourth point of the Rectangle.
     */
    public void setPoints(Vector p1, Vector p2, Vector p3, Vector p4)
    {
        setP1(p1);
        setP2(p2);
        setP3(p3);
        setP4(p4);
    }
    
    /**
     * Sets the first point of the Rectangle.
     *
     * @param p1 The new first point of the Rectangle.
     */
    public void setP1(Vector p1)
    {
        setVertex(1, p1);
    }
    
    /**
     * Sets the second point of the Rectangle.
     *
     * @param p2 The new second point of the Rectangle.
     */
    public void setP2(Vector p2)
    {
        setVertex(2, p2);
    }
    
    /**
     * Sets the third point of the Rectangle.
     *
     * @param p3 The new third point of the Rectangle.
     */
    public void setP3(Vector p3)
    {
        setVertex(3, p3);
    }
    
    /**
     * Sets the fourth point of the Rectangle.
     *
     * @param p4 The new fourth point of the Rectangle.
     */
    public void setP4(Vector p4)
    {
        setVertex(4, p4);
    }
    
}