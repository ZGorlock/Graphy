/*
 * File:    RegularPolyhedron.java
 * Package: objects.polyhedron.regular
 * Author:  Zachary Gill
 */

package objects.polyhedron.regular;

import math.vector.Vector;
import objects.base.AbstractObject;
import objects.polyhedron.Polyhedron;

import java.awt.*;

/**
 * Defines a Regular Polyhedron.
 */
public class RegularPolyhedron extends Polyhedron
{
    
    //Fields
    
    /**
     * The number of faces of the Polyhedron.
     */
    protected int numFaces;
    
    /**
     * The radius of the bounding sphere of the Polyhedron.
     */
    protected double radius;
    
    
    //Constructors
    
    /**
     * The constructor for a RegularPolyhedron.
     *
     * @param parent   The parent of the RegularPolyhedron.
     * @param center   The center of the RegularPolyhedron.
     * @param color    The color of the RegularPolyhedron.
     * @param numFaces The number of faces of the RegularPolyhedron
     * @param radius   The radius of the bounding sphere of the RegularPolyhedron.
     */
    public RegularPolyhedron(AbstractObject parent, Vector center, Color color, int numFaces, double radius)
    {
        super(center, color);
        
        this.numFaces = numFaces;
        this.radius = radius;
        setParent(parent);
        calculate();
    }
    
    
    //Getters
    
    /**
     * Returns the number of faces of the Polyhedron.
     *
     * @return The number of faces of the Polyhedron.
     */
    public int getNumFaces()
    {
        return numFaces;
    }
    
    /**
     * Returns the radius of the bounding sphere of the Polyhedron.
     *
     * @return The radius of the bounding sphere of the Polyhedron.
     */
    public double getRadius()
    {
        return radius;
    }
    
    
    //Setters
    
    /**
     * Sets the color of a face of the Polyhedron.
     *
     * @param face  The index of the face of the Polyhedron.
     * @param color The color.
     */
    public void setFaceColor(int face, Color color)
    {
        if (face < 1 || face > numFaces) {
            return;
        }
        components.get(face - 1).setColor(color);
    }
    
    /**
     * Sets the radius of the bounding sphere of the Polyhedron.
     *
     * @param radius The new radius of the bounding sphere of the Polyhedron.
     */
    public void setRadius(double radius)
    {
        this.radius = radius;
        calculate();
    }
    
}
