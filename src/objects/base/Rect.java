/*
 * File:    Rect.java
 * Package: objects.base.piece
 * Author:  Zachary Gill
 */

package objects.base;

import camera.Camera;
import main.Environment;
import math.vector.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines an Rectangle.
 */
public class Rect extends BaseObject
{
    
    //Constructors
    
    /**
     * The constructor for a Rect.
     *
     * @param parent The parent of the Rect.
     * @param color  The color of the Rect.
     * @param v1     The first point of the Rect.
     * @param v2     The second point of the Rect.
     * @param v3     The third point of the Rect.
     * @param v4     The third point of the Rect.
     */
    public Rect(AbstractObject parent, Color color, Vector v1, Vector v2, Vector v3, Vector v4)
    {
        super(color, v1, v2, v3, v4);
        center = v1.average(v2, v3, v4);
        type = Rect.class;
        setParent(parent);
    }
    
    /**
     * The constructor for a Rect.
     *
     * @param parent The parent of the Rect.
     * @param v1     The first point of the Rect.
     * @param v2     The second point of the Rect.
     * @param v3     The third point of the Rect.
     * @param v4     The third point of the Rect.
     */
    public Rect(AbstractObject parent, Vector v1, Vector v2, Vector v3, Vector v4)
    {
        this(parent, Color.BLACK, v1, v2, v3, v4);
    }
    
    /**
     * The constructor for a Rect.
     *
     * @param color The color of the Rect.
     * @param v1    The first point of the Rect.
     * @param v2    The second point of the Rect.
     * @param v3    The third point of the Rect.
     * @param v4    The third point of the Rect.
     */
    public Rect(Color color, Vector v1, Vector v2, Vector v3, Vector v4)
    {
        this(null, color, v1, v2, v3, v4);
    }
    
    /**
     * The constructor for a Rect.
     *
     * @param v1 The first point of the Rect.
     * @param v2 The second point of the Rect.
     * @param v3 The third point of the Rect.
     * @param v4 The third point of the Rect.
     */
    public Rect(Vector v1, Vector v2, Vector v3, Vector v4)
    {
        this(null, Color.BLACK, v1, v2, v3, v4);
    }
    
    
    //Methods
    
    /**
     * Prepares the Rect to be rendered.
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
        prepared.add(vertices[1].clone());
        prepared.add(vertices[2].clone());
        prepared.add(vertices[3].clone());
    
        performRotationTransformation(prepared);
        
        preparedBases.add(this);
        return preparedBases;
    }
    
    /**
     * Renders the Rect on the screen.
     *
     * @param g2     The 2D Graphics entity.
     */
    @Override
    public void render(Graphics2D g2)
    {
        if (!visible) {
            return;
        }
    
        Camera.projectVectorToCamera(prepared);
        Camera.collapseVectorToViewport(prepared);
    
        if (!clippingEnabled || Camera.hasVectorInView(prepared, vertices)) {
            Camera.scaleVectorToScreen(prepared);
        
            g2.setColor(getColor());
            switch (displayMode) {
                case VERTEX:
                    g2.drawRect((int) prepared.get(0).getX(), (int) prepared.get(0).getY(), 1, 1);
                    g2.drawRect((int) prepared.get(1).getX(), (int) prepared.get(1).getY(), 1, 1);
                    g2.drawRect((int) prepared.get(2).getX(), (int) prepared.get(2).getY(), 1, 1);
                    g2.drawRect((int) prepared.get(3).getX(), (int) prepared.get(3).getY(), 1, 1);
                    break;
                case EDGE:
                    g2.drawLine((int) prepared.get(0).getX(), (int) prepared.get(0).getY(), (int) prepared.get(1).getX(), (int) prepared.get(1).getY());
                    g2.drawLine((int) prepared.get(1).getX(), (int) prepared.get(1).getY(), (int) prepared.get(2).getX(), (int) prepared.get(2).getY());
                    g2.drawLine((int) prepared.get(2).getX(), (int) prepared.get(2).getY(), (int) prepared.get(3).getX(), (int) prepared.get(3).getY());
                    g2.drawLine((int) prepared.get(3).getX(), (int) prepared.get(3).getY(), (int) prepared.get(0).getX(), (int) prepared.get(0).getY());
                    break;
                case FACE:
                    Polygon face = new Polygon(
                            new int[] {(int) prepared.get(0).getX(), (int) prepared.get(1).getX(), (int) prepared.get(2).getX(), (int) prepared.get(3).getX()},
                            new int[] {(int) prepared.get(0).getY(), (int) prepared.get(1).getY(), (int) prepared.get(2).getY(), (int) prepared.get(3).getY()},
                            4
                    );
                    g2.fillPolygon(face);
                    break;
            }
            
            addFrame(g2);
        }
    }
    
    
    //Getters
    
    /**
     * Returns the first point of the Rectangle.
     *
     * @return The first point of the Rectangle.
     */
    public Vector getP1()
    {
        return vertices[0];
    }
    
    /**
     * Returns the second point of the Rectangle.
     *
     * @return The second point of the Rectangle.
     */
    public Vector getP2()
    {
        return vertices[1];
    }
    
    /**
     * Returns the third point of the Rectangle.
     *
     * @return The third point of the Rectangle.
     */
    public Vector getP3()
    {
        return vertices[2];
    }
    
    /**
     * Returns the second point of the Rectangle.
     *
     * @return The second point of the Rectangle.
     */
    public Vector getP4()
    {
        return vertices[3];
    }
    
    
    //Setters
    
    /**
     * Sets the point of the Rectangle.
     *
     * @param p1 The first point of the Rectangle.
     * @param p2 The second point of the Rectangle.
     * @param p3 The third point of the Rectangle.
     * @param p4 The fourth point of the Rectangle.
     */
    public void setPoints(Vector p1, Vector p2, Vector p3, Vector p4)
    {
        setP1(p1);
        setP2(p2);
        setP3(p3);
        setP4(p4);
    }
    
    /**
     * Sets the first point of the Rectangle.
     *
     * @param p1 The new first point of the Rectangle.
     */
    public void setP1(Vector p1)
    {
        vertices[0] = p1;
    }
    
    /**
     * Sets the second point of the Rectangle.
     *
     * @param p2 The new second point of the Rectangle.
     */
    public void setP2(Vector p2)
    {
        vertices[1] = p2;
    }
    
    /**
     * Sets the third point of the Rectangle.
     *
     * @param p3 The new third point of the Rectangle.
     */
    public void setP3(Vector p3)
    {
        vertices[2] = p3;
    }
    
    /**
     * Sets the fourth point of the Rectangle.
     *
     * @param p4 The new third fourth of the Rectangle.
     */
    public void setP4(Vector p4)
    {
        vertices[3] = p4;
    }
    
}