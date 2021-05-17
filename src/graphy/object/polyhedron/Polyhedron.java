/*
 * File:    Polyhedron.java
 * Package: graphy.objects.polyhedron
 * Author:  Zachary Gill
 */

package graphy.object.polyhedron;

import java.awt.Color;

import commons.math.component.vector.Vector;
import graphy.object.base.Object;

/**
 * Defines a Polyhedron.
 */
public abstract class Polyhedron extends Object {
    
    //Constants
    
    /**
     * The Golden Ratio.
     */
    public static final double GOLDEN_RATIO = (Math.sqrt(5) - 1) / 2;
    
    
    //Constructors
    
    /**
     * The constructor for a Polyhedron.
     *
     * @param center The center of the Polyhedron.
     * @param color  The color of the Polyhedron.
     */
    public Polyhedron(Vector center, Color color) {
        super(center, color);
    }
    
}
