/*
 * File:    Pentagon.java
 * Package: graphy.object.base.polygon
 * Author:  Zachary Gill
 */

package graphy.object.base.polygon;

import java.awt.Color;

import commons.math.component.vector.Vector;
import graphy.object.base.AbstractObject;

/**
 * Defines an Pentagon.
 */
public class Pentagon extends Polygon {
    
    //Constructors
    
    /**
     * The constructor for a Pentagon.
     *
     * @param parent The parent of the Pentagon.
     * @param color  The color of the Pentagon.
     * @param v1     The first point of the Pentagon.
     * @param v2     The second point of the Pentagon.
     * @param v3     The third point of the Pentagon.
     * @param v4     The fourth point of the Pentagon.
     * @param v5     The fifth point of the Pentagon.
     */
    public Pentagon(AbstractObject parent, Color color, Vector v1, Vector v2, Vector v3, Vector v4, Vector v5) {
        super(parent, color, v1, v2, v3, v4, v5);
    }
    
    /**
     * The constructor for a Pentagon.
     *
     * @param parent The parent of the Pentagon.
     * @param v1     The first point of the Pentagon.
     * @param v2     The second point of the Pentagon.
     * @param v3     The third point of the Pentagon.
     * @param v4     The fourth point of the Pentagon.
     * @param v5     The fifth point of the Pentagon.
     */
    public Pentagon(AbstractObject parent, Vector v1, Vector v2, Vector v3, Vector v4, Vector v5) {
        this(parent, Color.BLACK, v1, v2, v3, v4, v5);
    }
    
    /**
     * The constructor for a Pentagon.
     *
     * @param color The color of the Pentagon.
     * @param v1    The first point of the Pentagon.
     * @param v2    The second point of the Pentagon.
     * @param v3    The third point of the Pentagon.
     * @param v4    The fourth point of the Pentagon.
     * @param v5    The fifth point of the Pentagon.
     */
    public Pentagon(Color color, Vector v1, Vector v2, Vector v3, Vector v4, Vector v5) {
        this(null, color, v1, v2, v3, v4, v5);
    }
    
    /**
     * The constructor for a Pentagon.
     *
     * @param v1 The first point of the Pentagon.
     * @param v2 The second point of the Pentagon.
     * @param v3 The third point of the Pentagon.
     * @param v4 The fourth point of the Pentagon.
     * @param v5 The fifth point of the Pentagon.
     */
    public Pentagon(Vector v1, Vector v2, Vector v3, Vector v4, Vector v5) {
        this(null, Color.BLACK, v1, v2, v3, v4, v5);
    }
    
    
    //Getters
    
    /**
     * Returns the first point of the Pentagon.
     *
     * @return The first point of the Pentagon.
     */
    public Vector getP1() {
        return getVertex(1);
    }
    
    /**
     * Returns the second point of the Pentagon.
     *
     * @return The second point of the Pentagon.
     */
    public Vector getP2() {
        return getVertex(2);
    }
    
    /**
     * Returns the third point of the Pentagon.
     *
     * @return The third point of the Pentagon.
     */
    public Vector getP3() {
        return getVertex(3);
    }
    
    /**
     * Returns the fourth point of the Pentagon.
     *
     * @return The fourth point of the Pentagon.
     */
    public Vector getP4() {
        return getVertex(4);
    }
    
    /**
     * Returns the fifth point of the Pentagon.
     *
     * @return The fifth point of the Pentagon.
     */
    public Vector getP5() {
        return getVertex(5);
    }
    
    
    //Setters
    
    /**
     * Sets the point of the Pentagon.
     *
     * @param p1 The first point of the Pentagon.
     * @param p2 The second point of the Pentagon.
     * @param p3 The third point of the Pentagon.
     * @param p4 The fourth point of the Pentagon.
     * @param p5 The fifth point of the Pentagon.
     */
    public void setPoints(Vector p1, Vector p2, Vector p3, Vector p4, Vector p5) {
        setP1(p1);
        setP2(p2);
        setP3(p3);
        setP4(p4);
        setP5(p5);
    }
    
    /**
     * Sets the first point of the Pentagon.
     *
     * @param p1 The new first point of the Pentagon.
     */
    public void setP1(Vector p1) {
        setVertex(1, p1);
    }
    
    /**
     * Sets the second point of the Pentagon.
     *
     * @param p2 The new second point of the Pentagon.
     */
    public void setP2(Vector p2) {
        setVertex(2, p2);
    }
    
    /**
     * Sets the third point of the Pentagon.
     *
     * @param p3 The new third point of the Pentagon.
     */
    public void setP3(Vector p3) {
        setVertex(3, p3);
    }
    
    /**
     * Sets the fourth point of the Pentagon.
     *
     * @param p4 The new fourth point of the Pentagon.
     */
    public void setP4(Vector p4) {
        setVertex(4, p4);
    }
    
    /**
     * Sets the fifth point of the Pentagon.
     *
     * @param p5 The new fifth point of the Pentagon.
     */
    public void setP5(Vector p5) {
        setVertex(5, p5);
    }
    
}