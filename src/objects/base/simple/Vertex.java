/*
 * File:    Vertex.java
 * Package: objects.base.simple
 * Author:  Zachary Gill
 */

package objects.base.simple;

import camera.Camera;
import math.vector.Vector;
import objects.base.AbstractObject;
import objects.base.BaseObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines a Vertex.
 */
public class Vertex extends BaseObject
{
    
    //Constructors
    
    /**
     * The constructor for a Vertex.
     *
     * @param parent The parent of the Vertex.
     * @param color  The color of the Vertex.
     * @param v      The Vector defining the point of the Vertex.
     */
    public Vertex(AbstractObject parent, Color color, Vector v) {
        super(parent, color, v, v);
        center = v;
    }
    
    /**
     * The constructor for a Vertex from a 3-dimensional Vector.
     *
     * @param parent The parent of the Vertex.
     * @param v The Vector.
     */
    public Vertex(AbstractObject parent, Vector v) {
        this(parent, Color.BLACK, v);
    }
    
    /**
     * The constructor for a Vertex from a 3-dimensional Vector.
     *
     * @param color  The color of the Vertex.
     * @param v The Vector.
     */
    public Vertex(Color color, Vector v) {
        this(null, color, v);
    }
    
    /**
     * The constructor for a Vertex from a 3-dimensional Vector.
     *
     * @param v The Vector.
     */
    public Vertex(Vector v) {
        this(null, Color.BLACK, v);
    }
    
    
    //Methods
    
    /**
     * Prepares the Vertex to be rendered.
     *
     * @return The list of BaseObjects that were prepared.
     */
    @Override
    public List<BaseObject> prepare()
    {
        List<BaseObject> preparedBases = new ArrayList<>();
        if (!visible) {
            return preparedBases;
        }
    
        prepared.clear();
        prepared.add(vertices[0].clone());
    
        performRotationTransformation(prepared);
    
        preparedBases.add(this);
        return preparedBases;
    }
    
    /**
     * Renders the Vertex on the screen.
     *
     * @param g2 The 2D Graphics entity.
     */
    @Override
    public void render(Graphics2D g2)
    {
        if (!visible || prepared.size() != 1) {
            return;
        }
    
        Camera.projectVectorToCamera(prepared);
        Camera.collapseVectorToViewport(prepared);
        
        if (!clippingEnabled || Camera.hasVectorInView(prepared, vertices)) {
            Camera.scaleVectorToScreen(prepared);
    
            g2.setColor(getColor());
            g2.drawRect((int) prepared.get(0).getX(), (int) prepared.get(0).getY(), 1, 1);
        }
    }
    
    /**
     * Returns a string that represents the Vertex.
     *
     * @return A string that represents the Vertex.
     */
    @Override
    public String toString()
    {
        StringBuilder vertex = new StringBuilder();
        
        for (Double component : getPoint().getComponents()) {
            if (!vertex.toString().isEmpty()) {
                vertex.append(", ");
            }
            vertex.append(component);
        }
        
        return '(' + vertex.toString() + ')';
    }
    
    /**
     * Determines if another Vertex is equal to this Vertex.
     *
     * @param v The other Vertex.
     * @return Whether the two Vertices are equal or not.
     */
    public boolean equals(Vertex v)
    {
        return getPoint().equals(v.getPoint());
    }
    
    /**
     * Creates a cloned copy of the Vertex.
     *
     * @return The cloned Vertex.
     */
    @Override
    public Vertex clone()
    {
        return new Vertex(getPoint());
    }
    
    
    //Getters
    
    /**
     * Returns the point of the Vertex.
     *
     * @return The point of the Vertex.
     */
    public Vector getPoint()
    {
        return vertices[0];
    }
    
    
    //Setters
    
    /**
     * Sets the point of the Vertex.
     *
     * @param point The new point of the Vertex.
     */
    public void setPoint(Vector point)
    {
        vertices[0] = point;
    }
    
}