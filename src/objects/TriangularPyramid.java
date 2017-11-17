/*
 * File:    TriangularPyramid.java
 * Package: objects
 * Author:  Zachary Gill
 */

package objects;

import main.Environment;
import math.vector.Vector;
import math.vector.Vector3;
import objects.base.AbstractObject;
import objects.base.Object;
import objects.base.Rect;
import objects.base.Triangle;

import java.awt.*;
import java.util.Arrays;

/**
 * Defines a Triangular Pyramid Object.
 */
public class TriangularPyramid extends Object
{
    
    //Fields
    
    /**
     * The Triangle defining the base of the Pyramid.
     */
    protected Triangle base;
    
    /**
     * The Vector defining the apex of the Pyramid.
     */
    protected Vector apex;
    
    
    //Constructors
    
    /**
     * The constructor for an Pyramid.
     *
     * @param parent The parent of the Pyramid.
     * @param color  The color of the Pyramid.
     * @param base   The Triangle defining the base of the Pyramid.
     * @param apex   The Vector defining the apex of the Pyramid.
     */
    public TriangularPyramid(AbstractObject parent, Color color, Triangle base, Vector apex)
    {
        super(color);
        
        this.base = base;
        this.apex = apex;
        setParent(parent);
        calculate();
    }
    
    /**
     * The constructor for an Pyramid.
     *
     * @param parent The parent of the Pyramid.
     * @param base   The Triangle defining the base of the Pyramid.
     * @param apex   The Vector defining the apex of the Pyramid.
     */
    public TriangularPyramid(AbstractObject parent, Triangle base, Vector apex)
    {
        this(null, Color.BLACK, base, apex);
    }
    
    /**
     * The constructor for an Pyramid.
     *
     * @param color The color of the Pyramid.
     * @param base  The Triangle defining the base of the Pyramid.
     * @param apex  The Vector defining the apex of the Pyramid.
     */
    public TriangularPyramid(Color color, Triangle base, Vector apex)
    {
        this(null, color, base, apex);
    }
    
    /**
     * The constructor for an Pyramid.
     *
     * @param parent The parent of the Pyramid.
     * @param color  The color of the Pyramid.
     * @param base   The Triangle defining the base of the Pyramid.
     * @param height The height of the apex of the Pyramid.
     */
    public TriangularPyramid(AbstractObject parent, Color color, Triangle base, double height)
    {
        this(parent, color, base, TriangularPyramid.calculateApexFromHeight(base, height));
    }
    
    
    
    //Methods
    
    /**
     * Calculates the structure of the Triangular Pyramid.
     */
    @Override
    protected void calculate()
    {
        components.clear();
        
        this.center = apex.average(base.getVertices()[0].average(Arrays.copyOfRange(base.getVertices(), 1, base.getVertices().length - 1)));
        
        new Triangle(this,
                apex,
                base.getP1(),
                base.getP2()
        );
        new Triangle(this,
                apex,
                base.getP2(),
                base.getP3()
        );
        new Triangle(this,
                apex,
                base.getP3(),
                base.getP1()
        );
        new Triangle(this,
                base.getP1(),
                base.getP2(),
                base.getP3()
        );
        
        setColor(color);
        setVisible(visible);
    }
    
    /**
     * Recalculates the structure of the Triangular Pyramid.
     */
    protected void recalculate()
    {
        this.center = apex.average(base.getVertices()[0].average(Arrays.copyOfRange(base.getVertices(), 1, base.getVertices().length - 1)));
        
        ((Triangle) components.get(0)).setPoints(
                apex,
                base.getP1(),
                base.getP2()
        );
        ((Triangle) components.get(1)).setPoints(
                apex,
                base.getP2(),
                base.getP3()
        );
        ((Triangle) components.get(2)).setPoints(
                apex,
                base.getP3(),
                base.getP1()
        );
        ((Triangle) components.get(4)).setPoints(
                base.getP1(),
                base.getP2(),
                base.getP3()
        );
    }
    
    public static Vector calculateApexFromHeight(Triangle base, double height) //TODO
    {
        Vector a = base.getP2().minus(base.getP1());
        Vector b = base.getP3().minus(base.getP1());
        Vector c = new Vector3(a).cross(b);
        
        Vector m = base.getP1().average(base.getP2(), base.getP3());
        
        c = c.normalize();
        if ((m.plus(c)).distance(Environment.origin) < m.distance(Environment.origin)) {
            c = c.scale(-1);
        }
        
        return m.plus(c.scale(height));
    }
    
    
    //Getters
    
    /**
     * Returns the Triangle that defines the base of the Pyramid.
     *
     * @return The Triangle that defines the base of the Pyramid.
     */
    public Triangle getBase()
    {
        return base;
    }
    
    /**
     * Returns the Vector that defines the apex of the Pyramid.
     *
     * @return The Vector that defines the apex of the Pyramid.
     */
    public Vector getApex()
    {
        return apex;
    }
    
    
    //Setters
    
    /**
     * Sets the components that defines the Pyramid.
     *
     * @param base The Triangle that defines the base of the Pyramid.
     * @param apex The Vector that defines the apex of the Pyramid.
     */
    public void setComponents(Triangle base, Vector apex)
    {
        this.base = base;
        this.apex = apex;
        recalculate();
    }
    
    /**
     * Sets the Triangle that defines the base of the Pyramid.
     *
     * @param base The Rectangle that defines the base of the Pyramid.
     */
    public void setBase(Triangle base)
    {
        this.base = base;
        recalculate();
    }
    
    /**
     * Returns the Vector that defines the apex of the Pyramid.
     *
     * @param apex The Vector that defines the apex of the Pyramid.
     */
    public void setApex(Vector apex)
    {
        this.apex = apex;
        recalculate();
    }
    
    /**
     * Sets the color of a face of the Pyramid.
     *
     * @param face  The face of the Pyramid.
     * @param color The color.
     */
    public void setFaceColor(int face, Color color)
    {
        if (face < 1 || face > 4) {
            return;
        }
        face--;
        components.get(face).setColor(color);
    }
    
}
