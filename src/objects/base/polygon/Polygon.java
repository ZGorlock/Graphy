/*
 * File:    Polygon.java
 * Package: objects.base.polygon
 * Author:  Zachary Gill
 */

package objects.base.polygon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import math.vector.Vector;
import objects.base.AbstractObject;
import objects.base.BaseObject;

/**
 * Defines a Polygon.
 */
public class Polygon extends BaseObject {
    
    //Fields
    
    /**
     * The number of vertices that make up the Polygon.
     */
    protected int numVertices;
    
    
    //Constructors
    
    /**
     * The constructor for a Polygon.
     *
     * @param parent The parent of the Polygon.
     * @param color  The color of the Polygon.
     * @param vs     The vertices of the Polygon.
     */
    public Polygon(AbstractObject parent, Color color, Vector... vs) {
        super(parent, color, Vector.averageVector(vs), vs);
        numVertices = vs.length;
    }
    
    
    //Methods
    
    /**
     * Prepares the Polygon to be rendered.
     *
     * @return The list of BaseObjects that were prepared.
     */
    @Override
    public List<BaseObject> prepare() {
        if (!prePrepare()) {
            return new ArrayList<>();
        }
        
        List<BaseObject> preparedBases = new ArrayList<>();
        
        prepared.clear();
        for (Vector vertex : vertices) {
            prepared.add(vertex.clone().justify());
        }
        
        performRotationTransformation(prepared);
        
        preparedBases.add(this);
        return preparedBases;
    }
    
    /**
     * Renders the Polygon on the screen.
     *
     * @param g2 The 2D Graphics entity.
     */
    @Override
    public void render(Graphics2D g2) {
        if (!preRender(prepared, vertices, numVertices)) {
            return;
        }
        
        g2.setColor(getColor());
        switch (displayMode) {
            case VERTEX:
                for (Vector v : prepared) {
                    g2.drawRect((int) v.getX(), (int) v.getY(), 1, 1);
                }
                break;
            
            case EDGE:
                if (numVertices < 2) {
                    break;
                }
                
                for (int i = 1; i < numVertices; i++) {
                    g2.drawLine((int) prepared.get(i - 1).getX(), (int) prepared.get(i - 1).getY(), (int) prepared.get(i).getX(), (int) prepared.get(i).getY());
                }
                g2.drawLine((int) prepared.get(numVertices - 1).getX(), (int) prepared.get(numVertices - 1).getY(), (int) prepared.get(0).getX(), (int) prepared.get(0).getY());
                break;
            
            case FACE:
                if (numVertices < 3) {
                    break;
                }
                
                int[] xPoints = new int[numVertices];
                int[] yPoints = new int[numVertices];
                for (int i = 0; i < numVertices; i++) {
                    xPoints[i] = (int) prepared.get(i).getX();
                    yPoints[i] = (int) prepared.get(i).getY();
                }
                
                java.awt.Polygon face = new java.awt.Polygon(
                        xPoints,
                        yPoints,
                        numVertices
                );
                g2.fillPolygon(face);
                break;
        }
        
        renderFrame(g2);
    }
    
    
    //Getters
    
    /**
     * Returns a point of the Polygon.
     *
     * @param n The index of the point to return.
     * @return The first point of the Triangle.
     */
    public Vector getVertex(int n) {
        if (n < 1 || n > numVertices) {
            return new Vector(0, 0, 0);
        }
        return vertices[n - 1].clone();
    }
    
    
    //Setters
    
    /**
     * Sets a point of the Polygon.
     *
     * @param n The index of the point to set.
     * @param p The new point.
     */
    public void setVertex(int n, Vector p) {
        if (n < 1 || n > numVertices) {
            return;
        }
        vertices[n - 1] = p;
    }
    
}
