/*
 * File:    BigVertex.java
 * Package: graphy.object.base.simple
 * Author:  Zachary Gill
 */

package graphy.object.base.simple;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.UUID;

import commons.graphics.DrawUtility;
import commons.math.component.vector.Vector;
import graphy.object.base.AbstractObject;

/**
 * Defines a Vertex of a certain size.
 */
public class BigVertex extends Vertex {
    
    //Fields
    
    /**
     * The size of the Vertex.
     */
    protected int size;
    
    
    //Constructor
    
    /**
     * The constructor for a Big Vertex.
     *
     * @param parent The parent of the Big Vertex.
     * @param color  The color of the Big Vertex.
     * @param v      The Vector defining the point of the Big Vertex.
     * @param size   The size of the Big Vertex.
     */
    public BigVertex(AbstractObject parent, Color color, Vector v, int size) {
        super(parent, color, v);
        this.size = size;
    }
    
    /**
     * The constructor for a Big Vertex.
     *
     * @param parent The parent of the Big Vertex.
     * @param v      The Vector defining the point of the Big Vertex.
     * @param size   The size of the Big Vertex.
     */
    public BigVertex(AbstractObject parent, Vector v, int size) {
        this(parent, Color.BLACK, v, size);
    }
    
    /**
     * The constructor for a Big Vertex.
     *
     * @param color The color of the Big Vertex.
     * @param v     The Vector defining the point of the Big Vertex.
     * @param size  The size of the Big Vertex.
     */
    public BigVertex(Color color, Vector v, int size) {
        this(null, color, v, size);
    }
    
    /**
     * The constructor for a Big Vertex.
     *
     * @param v    The Vector defining the point of the Big Vertex.
     * @param size The size of the Big Vertex.
     */
    public BigVertex(Vector v, int size) {
        this(null, Color.BLACK, v, size);
    }
    
    
    //Methods
    
    /**
     * Renders the Big Vertex on the screen.
     *
     * @param perspective The perspective to render the Big Vertex for.
     * @param g2          The 2D Graphics entity.
     */
    @Override
    public void render(Graphics2D g2, UUID perspective) {
        DrawUtility.setColor(g2, getColor());
        for (int i = -size; i <= size; i++) {
            for (int j = -size; j <= size; j++) {
                DrawUtility.drawPoint(g2, prepared.get(perspective).get(0));
            }
        }
    }
    
}
