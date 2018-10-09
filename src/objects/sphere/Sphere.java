/*
 * File:    Sphere.java
 * Package: objects.sphere
 * Author:  Zachary Gill
 */

package objects.sphere;

import main.Environment;
import math.vector.Vector;
import objects.base.AbstractObject;
import objects.base.Object;
import objects.base.polygon.Triangle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines a Sphere.
 */
public class Sphere extends Object {
    
    //Fields
    
    /**
     * The radius of the Sphere.
     */
    private double radius;
    
    /**
     * The step with which to calculate the Sphere.
     */
    private double step;
    
    
    //Constructors
    
    /**
     * The constructor for an Sphere.
     *
     * @param parent The parent of the Sphere.
     * @param center The center point of the Sphere.
     * @param color  The color of the Sphere.
     * @param radius The radius of the bounding sphere of the Sphere.
     * @param step   The step with which to calculate the Sphere.
     */
    public Sphere(AbstractObject parent, Vector center, Color color, double radius, double step) {
        super(parent, center, color);
        this.radius = radius;
        this.step = step;
        
        calculate();
    }
    
    /**
     * The constructor for an Sphere.
     *
     * @param parent The parent of the Sphere.
     * @param center The center point of the Sphere.
     * @param radius The radius of the bounding sphere of the Sphere.
     * @param step   The step with which to calculate the Sphere.
     */
    public Sphere(AbstractObject parent, Vector center, double radius, double step) {
        this(parent, center, Color.BLACK, radius, step);
    }

    /**
     * The constructor for an Sphere.
     *
     * @param center The center point of the Sphere.
     * @param color  The color of the Sphere.
     * @param radius The radius of the bounding sphere of the Sphere.
     * @param step   The step with which to calculate the Sphere.
     */
    public Sphere(Vector center, Color color, double radius, double step) {
        this(null, center, color, radius, step);
    }
    
    
    //Methods
    
    /**
     * Calculates the structure of the Sphere.
     */
    @Override
    protected void calculate() {
        components.clear();
    
        int layer = 0;
        List<List<Vector>> vertices = new ArrayList<>();
        boolean offset = false;
        for (double phi = 0; Math.PI - phi > -Environment.omega; phi += step / 2) {
            vertices.add(new ArrayList<>());
            for (double theta = 0; Math.PI * 2 - theta > -Environment.omega; theta += step) {
                double x = radius * Math.sin(phi) * Math.cos(theta + (offset ? step / 2 : 0));
                double y = radius * Math.cos(phi);
                double z = radius * Math.sin(phi) * Math.sin(theta + (offset ? step / 2 : 0));
                
                vertices.get(layer).add(new Vector(x, y, z).plus(center));
            }
            offset = !offset;
            layer++;
        }
        
        for (int i = 0; i < layer - 1; i++) {
            for (int j = 0; j < layer; j++) {
                Triangle t = new Triangle(this, color, vertices.get(i).get(j), vertices.get(i + 1).get(j), vertices.get(i + 1).get((j + 1) % layer));
            }
        }
        for (int i = layer - 1; i > 0; i--) {
            for (int j = 0; j < layer; j++) {
                Triangle t = new Triangle(this, color, vertices.get(i).get(j), vertices.get(i - 1).get(j), vertices.get(i - 1).get((j == 0) ? layer - 1 : j - 1));
            }
        }
        
        setVisible(visible);
    }
    
}
