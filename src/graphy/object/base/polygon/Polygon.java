/*
 * File:    Polygon.java
 * Package: graphy.object.base.polygon
 * Author:  Zachary Gill
 */

package graphy.object.base.polygon;

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
     * @param perspective The perspective to prepare the Polygon for.
     * @return The list of BaseObjects that were prepared.
     */
    @Override
    public List<BaseObject> prepare(UUID perspective) {
        List<BaseObject> preparedBases = new ArrayList<>();
        List<Vector> perspectivePrepared = prepared.get(perspective);
        
        perspectivePrepared.clear();
        for (Vector vertex : vertices) {
            perspectivePrepared.add(JustificationUtil.justify(vertex.cloned()));
        }
        
        performRotationTransformation(perspectivePrepared);
        
        preparedBases.add(this);
        return preparedBases;
    }
    
    /**
     * Renders the Polygon on the screen.
     *
     * @param perspective The perspective to render the Polygon for.
     * @param g2          The 2D Graphics entity.
     */
    @Override
    public void render(Graphics2D g2, UUID perspective) {
        DrawUtility.setColor(g2, getColor());
        switch (displayMode) {
            case VERTEX:
                for (Vector v : prepared.get(perspective)) {
                    DrawUtility.drawPoint(g2, v);
                }
                break;
            
            case EDGE:
                if (numVertices < 2) {
                    break;
                }
                
                for (int i = 1; i < numVertices; i++) {
                    g2.drawLine((int) prepared.get(perspective).get(i - 1).getRawX().intValue(), (int) prepared.get(perspective).get(i - 1).getRawY().intValue(), (int) prepared.get(perspective).get(i).getRawX().intValue(), (int) prepared.get(perspective).get(i).getRawY().intValue());
                }
                g2.drawLine((int) prepared.get(perspective).get(numVertices - 1).getRawX().intValue(), (int) prepared.get(perspective).get(numVertices - 1).getRawY().intValue(), (int) prepared.get(perspective).get(0).getRawX().intValue(), (int) prepared.get(perspective).get(0).getRawY().intValue());
                break;
            
            case FACE:
                if (numVertices < 3) {
                    break;
                }
                
                DrawUtility.fillPolygon(g2, prepared.get(perspective));
                break;
        }
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
        return vertices[n - 1].cloned();
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
