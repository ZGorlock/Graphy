/*
 * File:    BigVertex.java
 * Package: objects.base.simple
 * Author:  Zachary Gill
 */

package objects.base.simple;

import camera.Camera;
import math.vector.Vector;
import objects.base.AbstractObject;

import java.awt.*;

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
     * The constructor for a Vertex.
     *
     * @param parent The parent of the Vertex.
     * @param color  The color of the Vertex.
     * @param v      The Vector defining the point of the Vertex.
     * @param size   The size of the Vertex.
     */
    public BigVertex(AbstractObject parent, Color color, Vector v, int size) {
        super(parent, color, v);
        this.size = size;
    }
    
    /**
     * The constructor for a Vertex.
     *
     * @param parent The parent of the Vertex.
     * @param v      The Vector defining the point of the Vertex.
     * @param size   The size of the Vertex.
     */
    public BigVertex(AbstractObject parent, Vector v, int size) {
        this(parent, Color.BLACK, v, size);
    }
    
    /**
     * The constructor for a Vertex.
     *
     * @param color The color of the Vertex.
     * @param v     The Vector defining the point of the Vertex.
     * @param size  The size of the Vertex.
     */
    public BigVertex(Color color, Vector v, int size) {
        this(null, color, v, size);
    }
    
    /**
     * The constructor for a Vertex.
     *
     * @param v    The Vector defining the point of the Vertex.
     * @param size The size of the Vertex.
     */
    public BigVertex(Vector v, int size) {
        this(null, Color.BLACK, v, size);
    }
    
    
    //Methods
    
    /**
     * Renders the Vertex on the screen.
     *
     * @param g2 The 2D Graphics entity.
     */
    @Override
    public void render(Graphics2D g2) {
        if (!visible || prepared.size() != 1) {
            return;
        }
        
        Camera.projectVectorToCamera(prepared);
        Camera.collapseVectorToViewport(prepared);
        
        if (!clippingEnabled || Camera.hasVectorInView(prepared, vertices)) {
            Camera.scaleVectorToScreen(prepared);
            
            g2.setColor(getColor());
            for (int i = -size; i <= size; i++) {
                for (int j = -size; j <= size; j++) {
                    g2.drawRect((int) prepared.get(0).getX() + i, (int) prepared.get(0).getY() + j, 1, 1);
                }
            }
        }
    }
    
}
