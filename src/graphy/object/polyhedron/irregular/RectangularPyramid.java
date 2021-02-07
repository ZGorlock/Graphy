/*
 * File:    Pyramid.java
 * Package: graphy.objects.polyhedron.irregular
 * Author:  Zachary Gill
 */

package graphy.object.polyhedron.irregular;

import java.awt.Color;
import java.util.Arrays;

import graphy.math.vector.Vector;
import graphy.object.base.AbstractObject;
import graphy.object.base.Object;
import graphy.object.base.polygon.Rectangle;
import graphy.object.base.polygon.Triangle;

/**
 * Defines a Pyramid Object.
 */
public class RectangularPyramid extends Object {
    
    //Fields
    
    /**
     * The Rectangle defining the base of the Pyramid.
     */
    protected Rectangle base;
    
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
     * @param base   The Rectangle defining the base of the Pyramid.
     * @param apex   The Vector defining the apex of the Pyramid.
     */
    public RectangularPyramid(AbstractObject parent, Color color, Rectangle base, Vector apex) {
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
     * @param base   The Rectangle defining the base of the Pyramid.
     * @param apex   The Vector defining the apex of the Pyramid.
     */
    public RectangularPyramid(AbstractObject parent, Rectangle base, Vector apex) {
        this(null, Color.BLACK, base, apex);
    }
    
    /**
     * The constructor for an Pyramid.
     *
     * @param color The color of the Pyramid.
     * @param base  The Rectangle defining the base of the Pyramid.
     * @param apex  The Vector defining the apex of the Pyramid.
     */
    public RectangularPyramid(Color color, Rectangle base, Vector apex) {
        this(null, color, base, apex);
    }
    
    
    //Methods
    
    /**
     * Calculates the structure of the Pyramid.
     */
    @Override
    protected void calculate() {
        components.clear();
        
        this.center = apex.average(base.getVertices()[0].average(Arrays.copyOfRange(base.getVertices(), 1, base.getVertices().length)));
        
        new Triangle(this, color,
                apex,
                base.getP1(),
                base.getP2()
        );
        new Triangle(this, color,
                apex,
                base.getP2(),
                base.getP3()
        );
        new Triangle(this, color,
                apex,
                base.getP3(),
                base.getP4()
        );
        new Triangle(this, color,
                apex,
                base.getP4(),
                base.getP1()
        );
        new Rectangle(this, color,
                base.getP1(),
                base.getP2(),
                base.getP3(),
                base.getP4()
        );
        
        setVisible(visible);
    }
    
    /**
     * Recalculates the structure of the Pyramid.
     */
    protected void recalculate() {
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
                base.getP4()
        );
        ((Triangle) components.get(3)).setPoints(
                apex,
                base.getP4(),
                base.getP1()
        );
        ((Rectangle) components.get(4)).setPoints(
                base.getP1(),
                base.getP2(),
                base.getP3(),
                base.getP4()
        );
    }
    
    
    //Getters
    
    /**
     * Returns the Rectangle that defines the base of the Pyramid.
     *
     * @return The Rectangle that defines the base of the Pyramid.
     */
    public Rectangle getBase() {
        return base;
    }
    
    /**
     * Returns the Vector that defines the apex of the Pyramid.
     *
     * @return The Vector that defines the apex of the Pyramid.
     */
    public Vector getApex() {
        return apex;
    }
    
    
    //Setters
    
    /**
     * Sets the components that defines the Pyramid.
     *
     * @param base The Rectangle that defines the base of the Pyramid.
     * @param apex The Vector that defines the apex of the Pyramid.
     */
    public void setComponents(Rectangle base, Vector apex) {
        this.base = base;
        this.apex = apex;
        recalculate();
    }
    
    /**
     * Sets the Rectangle that defines the base of the Pyramid.
     *
     * @param base The Rectangle that defines the base of the Pyramid.
     */
    public void setBase(Rectangle base) {
        this.base = base;
        recalculate();
    }
    
    /**
     * Returns the Vector that defines the apex of the Pyramid.
     *
     * @param apex The Vector that defines the apex of the Pyramid.
     */
    public void setApex(Vector apex) {
        this.apex = apex;
        recalculate();
    }
    
    /**
     * Sets the color of a face of the Pyramid.
     *
     * @param face  The face of the Pyramid.
     * @param color The color.
     */
    public void setFaceColor(int face, Color color) {
        if (face < 1 || face > 5) {
            return;
        }
        face--;
        components.get(face).setColor(color);
    }
    
}
