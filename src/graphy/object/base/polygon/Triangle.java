/*
 * File:    Triangle.java
 * Package: graphy.object.base.polygon
 * Author:  Zachary Gill
 */

package graphy.object.base.polygon;

import java.awt.Color;

import graphy.math.vector.Vector;
import graphy.object.base.AbstractObject;

/**
 * Defines a Triangle.
 */
public class Triangle extends Polygon {
    
    //Constructors
    
    /**
     * The constructor for a Triangle.
     *
     * @param parent The parent of the Triangle.
     * @param color  The color of the Triangle.
     * @param v1     The first point of the Triangle.
     * @param v2     The second point of the Triangle.
     * @param v3     The third point of the Triangle.
     */
    public Triangle(AbstractObject parent, Color color, Vector v1, Vector v2, Vector v3) {
        super(parent, color, v1, v2, v3);
    }
    
    /**
     * The constructor for a Triangle from three Vertices.
     *
     * @param parent The parent of the Triangle.
     * @param v1     The first point of the Triangle.
     * @param v2     The second point of the Triangle.
     * @param v3     The third point of the Triangle.
     */
    public Triangle(AbstractObject parent, Vector v1, Vector v2, Vector v3) {
        this(parent, Color.BLACK, v1, v2, v3);
    }
    
    /**
     * The constructor for a Triangle from three Vertices.
     *
     * @param color The color of the Triangle.
     * @param v1    The first point of the Triangle.
     * @param v2    The second point of the Triangle.
     * @param v3    The third point of the Triangle.
     */
    public Triangle(Color color, Vector v1, Vector v2, Vector v3) {
        this(null, color, v1, v2, v3);
    }
    
    /**
     * The constructor for a Triangle.
     *
     * @param v1 The first point of the Triangle.
     * @param v2 The second point of the Triangle.
     * @param v3 The third point of the Triangle.
     */
    public Triangle(Vector v1, Vector v2, Vector v3) {
        this(null, Color.BLACK, v1, v2, v3);
    }
    
    
    //Getters
    
    /**
     * Returns the first point of the Triangle.
     *
     * @return The first point of the Triangle.
     */
    public Vector getP1() {
        return getVertex(1);
    }
    
    /**
     * Returns the second point of the Triangle.
     *
     * @return The second point of the Triangle.
     */
    public Vector getP2() {
        return getVertex(2);
    }
    
    /**
     * Returns the third point of the Triangle.
     *
     * @return The third point of the Triangle.
     */
    public Vector getP3() {
        return getVertex(3);
    }
    
    
    //Setters
    
    /**
     * Sets the point of the Triangle.
     *
     * @param p1 The first point of the Triangle.
     * @param p2 The second point of the Triangle.
     * @param p3 The third point of the Triangle.
     */
    public void setPoints(Vector p1, Vector p2, Vector p3) {
        setP1(p1);
        setP2(p2);
        setP3(p3);
    }
    
    /**
     * Sets the first point of the Triangle.
     *
     * @param p1 The new first point of the Triangle.
     */
    public void setP1(Vector p1) {
        setVertex(1, p1);
    }
    
    /**
     * Sets the second point of the Triangle.
     *
     * @param p2 The new second point of the Triangle.
     */
    public void setP2(Vector p2) {
        setVertex(2, p2);
    }
    
    /**
     * Sets the third point of the Triangle.
     *
     * @param p3 The new third point of the Triangle.
     */
    public void setP3(Vector p3) {
        setVertex(3, p3);
    }
    
}