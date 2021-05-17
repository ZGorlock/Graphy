/*
 * File:    HoledSphere.java
 * Package: graphy.objects.sphere
 * Author:  Zachary Gill
 */

package graphy.object.sphere;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import commons.math.CoordinateUtility;
import commons.math.component.vector.Vector;
import graphy.main.Environment;
import graphy.object.base.AbstractObject;
import graphy.object.base.Object;
import graphy.object.base.polygon.Triangle;

/**
 * Defines a Holed Sphere.
 */
public class HoledSphere extends Object {
    
    //Fields
    
    /**
     * The radius of the Holed Sphere.
     */
    private double radius;
    
    /**
     * The step with which to calculate the Holed Sphere.
     */
    private double step;
    
    
    //Constructors
    
    /**
     * The constructor for a Holed Sphere.
     *
     * @param parent The parent of the Holed Sphere.
     * @param center The center point of the Holed Sphere.
     * @param color  The color of the Holed Sphere.
     * @param radius The radius of the bounding sphere of the Holed Sphere.
     * @param step   The step with which to calculate the Holed Sphere.
     */
    public HoledSphere(AbstractObject parent, Vector center, Color color, double radius, double step) {
        super(parent, center, color);
        this.radius = radius;
        this.step = step;
        
        calculate();
    }
    
    /**
     * The constructor for a Holed Sphere.
     *
     * @param parent The parent of the Holed Sphere.
     * @param center The center point of the Holed Sphere.
     * @param radius The radius of the bounding sphere of the Holed Sphere.
     * @param step   The step with which to calculate the Holed Sphere.
     */
    public HoledSphere(AbstractObject parent, Vector center, double radius, double step) {
        this(parent, center, Color.BLACK, radius, step);
    }
    
    /**
     * The constructor for a Holed Sphere.
     *
     * @param center The center point of the Holed Sphere.
     * @param color  The color of the Holed Sphere.
     * @param radius The radius of the bounding sphere of the Holed Sphere.
     * @param step   The step with which to calculate the Holed Sphere.
     */
    public HoledSphere(Vector center, Color color, double radius, double step) {
        this(null, center, color, radius, step);
    }
    
    
    //Methods
    
    /**
     * Calculates the structure of the Holed Sphere.
     */
    @Override
    protected void calculate() {
        components.clear();
        
        int layer = 0;
        List<List<Vector>> vertices = new ArrayList<>();
        boolean offset = false;
        for (double phi = 0; Math.PI - phi > -Environment.OMEGA; phi += step / 2) {
            vertices.add(new ArrayList<>());
            for (double theta = 0; Math.PI * 2 - theta > -Environment.OMEGA; theta += step) {
                Vector cartesian = CoordinateUtility.sphericalToCartesian(radius, theta + (offset ? step / 2 : 0), phi);
                vertices.get(layer).add(cartesian.plus(center));
            }
            offset = !offset;
            layer++;
        }
        
        for (int i = 0; i < layer - 1; i++) {
            for (int j = 0; j < layer; j++) {
                new Triangle(this, color, vertices.get(i).get(j), vertices.get(i + 1).get(j), vertices.get(i + 1).get((j + 1) % layer));
            }
        }
        
        setVisible(visible);
    }
    
}
