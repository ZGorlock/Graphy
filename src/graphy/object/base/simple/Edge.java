/*
 * File:    Edge.java
 * Package: graphy.object.base.simple
 * Author:  Zachary Gill
 */

package graphy.object.base.simple;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import commons.graphics.DrawUtility;
import commons.math.component.vector.Vector;
import graphy.math.vector.JustificationUtil;
import graphy.object.base.AbstractObject;
import graphy.object.base.BaseObject;

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
     * @param perspective The perspective to prepare the Edge for.
     * @return The list of BaseObjects that were prepared.
     */
    @Override
    public List<BaseObject> prepare(UUID perspective) {
        List<BaseObject> preparedBases = new ArrayList<>();
        List<Vector> perspectivePrepared = prepared.get(perspective);
        
        perspectivePrepared.clear();
        perspectivePrepared.add(JustificationUtil.justify(vertices[0].cloned()));
        perspectivePrepared.add(JustificationUtil.justify(vertices[1].cloned()));
        
        performRotationTransformation(perspectivePrepared);
        
        preparedBases.add(this);
        return preparedBases;
    }
    
    /**
     * Renders the Edge on the screen.
     *
     * @param perspective The perspective to render the Edge for.
     * @param g2          The 2D Graphics entity.
     */
    @Override
    public void render(Graphics2D g2, UUID perspective) {
        DrawUtility.setColor(g2, getColor());
        switch (displayMode) {
            case VERTEX:
                DrawUtility.drawPoint(g2, prepared.get(perspective).get(0));
                DrawUtility.drawPoint(g2, prepared.get(perspective).get(1));
                break;
            case EDGE:
            case FACE:
                DrawUtility.drawLine(g2, prepared.get(perspective).get(0), prepared.get(perspective).get(1));
                break;
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
