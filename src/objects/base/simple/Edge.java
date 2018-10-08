/*
 * File:    Edge.java
 * Package: objects.base.simple
 * Author:  Zachary Gill
 */

package objects.base.simple;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import camera.Camera;
import math.vector.Vector;
import objects.base.AbstractObject;
import objects.base.BaseObject;

/**
 * Defines an Edge.
 */
public class Edge extends BaseObject {
    
    //Constructors
    
    /**
     * The constructor for an Edge.
     *
     * @param parent The parent of the Edge.
     * @param color  The color of the Edge.
     * @param v1     The starting point of the Edge.
     * @param v2     The ending point of the Edge.
     */
    public Edge(AbstractObject parent, Color color, Vector v1, Vector v2) {
        super(parent, color, v1.midpoint(v2), v1, v2);
    }
    
    /**
     * The constructor for an Edge.
     *
     * @param parent The parent of the Edge.
     * @param v1     The starting point of the Edge.
     * @param v2     The ending point of the Edge.
     */
    public Edge(AbstractObject parent, Vector v1, Vector v2) {
        this(parent, Color.BLACK, v1, v2);
    }
    
    /**
     * The constructor for an Edge.
     *
     * @param color The color of the Edge.
     * @param v1    The starting point of the Edge.
     * @param v2    The ending point of the Edge.
     */
    public Edge(Color color, Vector v1, Vector v2) {
        this(null, color, v1, v2);
    }
    
    /**
     * The constructor for an Edge.
     *
     * @param v1 The starting point of the Edge.
     * @param v2 The ending point of the Edge.
     */
    public Edge(Vector v1, Vector v2) {
        this(null, Color.BLACK, v1, v2);
    }
    
    
    //Methods
    
    /**
     * Prepares the Edge to be rendered.
     *
     * @return The list of BaseObjects that were prepared.
     */
    @Override
    public List<BaseObject> prepare() {
        List<BaseObject> preparedBases = new ArrayList<>();
        if (!visible) {
            return preparedBases;
        }
        
        prepared.clear();
        prepared.add(vertices[0].clone());
        prepared.add(vertices[1].clone());
        
        performRotationTransformation(prepared);
        
        preparedBases.add(this);
        return preparedBases;
    }
    
    /**
     * Renders the Edge on the screen.
     *
     * @param g2 The 2D Graphics entity.
     */
    @Override
    public void render(Graphics2D g2) {
        if (!visible || prepared.size() != 2) {
            return;
        }
        
        Camera.projectVectorToCamera(prepared);
        Camera.collapseVectorToViewport(prepared);
        
        if (!clippingEnabled || Camera.hasVectorInView(prepared, vertices)) {
            Camera.scaleVectorToScreen(prepared);
            
            g2.setColor(getColor());
            switch (displayMode) {
                case VERTEX:
                    g2.drawRect((int) prepared.get(0).getX(), (int) prepared.get(0).getY(), 0, 1);
                    g2.drawRect((int) prepared.get(1).getX(), (int) prepared.get(1).getY(), 0, 1);
                    break;
                case EDGE:
                case FACE:
                    g2.drawLine((int) prepared.get(0).get(0), (int) prepared.get(0).get(1), (int) prepared.get(1).get(0), (int) prepared.get(1).get(1));
                    break;
            }
            
            addFrame(g2);
        }
    }
    
    
    //Getters
    
    /**
     * Returns the starting point of the Edge.
     *
     * @return The starting point of the Edge.
     */
    public Vector getV1() {
        return vertices[0];
    }
    
    /**
     * Returns the ending point of the Edge.
     *
     * @return The ending point of the Edge.
     */
    public Vector getV2() {
        return vertices[1];
    }
    
    
    //Setters
    
    /**
     * Sets the point of the Edge.
     *
     * @param v1 The starting point of the Edge.
     * @param v2 The ending point of the Edge.
     */
    public void setPoints(Vector v1, Vector v2) {
        setV1(v1);
        setV2(v2);
    }
    
    /**
     * Sets the starting point of the Edge.
     *
     * @param v1 The starting point of the Edge.
     */
    public void setV1(Vector v1) {
        vertices[0] = v1;
    }
    
    /**
     * Sets the ending point of the Edge.
     *
     * @param v2 The ending point of the Edge.
     */
    public void setV2(Vector v2) {
        vertices[1] = v2;
    }
    
}
