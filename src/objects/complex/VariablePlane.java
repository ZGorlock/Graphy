/*
 * File:    VariablePlane.java
 * Package: objects.complex
 * Author:  Zachary Gill
 */

package objects.complex;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import main.Environment;
import math.vector.UniqueVectorSet;
import math.vector.Vector;
import objects.base.AbstractObject;
import objects.base.Object;
import objects.base.polygon.Rectangle;
import objects.base.polygon.Triangle;

/**
 * Defines a Variable Plane.
 */
public class VariablePlane extends Object {
    
    //Fields
    
    /**
     * The bounds of the plane.
     */
    public Rectangle bounds;
    
    /**
     * The density of the plane.
     */
    public double density;
    
    /**
     * The range of variability in the plane.
     */
    public double variabilityRange;
    
    /**
     * The factor of speech at which the plane moves.
     */
    public double speed;
    
    
    //Constructors
    
    /**
     * The constructor for a Variable Plane.
     *
     * @param parent           The parent of the Variable Plane.
     * @param color            The color of the Variable Plane.
     * @param bounds           The bounds of the plane.
     * @param density          The density of the plane.
     * @param variabilityRange The range of variability in the plane.
     * @param speed            The factor of speech at which the plane moves.
     */
    public VariablePlane(AbstractObject parent, Color color, Rectangle bounds, double density, double variabilityRange, double speed) {
        super(parent, bounds.getCenter(), color);
        
        this.bounds = bounds;
        this.density = density;
        this.variabilityRange = variabilityRange;
        this.speed = speed;
        
        calculate();
    }
    
    /**
     * The constructor for a Variable Plane.
     *
     * @param color            The color of the Variable Plane.
     * @param bounds           The bounds of the plane.
     * @param density          The density of the plane.
     * @param variabilityRange The range of variability in the plane.
     * @param speed            The factor of speech at which the plane moves.
     */
    public VariablePlane(Color color, Rectangle bounds, double density, double variabilityRange, double speed) {
        this(null, color, bounds, density, variabilityRange, speed);
    }
    
    /**
     * The constructor for a Variable Plane.
     *
     * @param bounds           The bounds of the plane.
     * @param density          The density of the plane.
     * @param variabilityRange The range of variability in the plane.
     * @param speed            The factor of speech at which the plane moves.
     */
    public VariablePlane(Rectangle bounds, double density, double variabilityRange, double speed) {
        this(null, Color.BLACK, bounds, density, variabilityRange, speed);
    }
    
    /**
     * The constructor for a Variable Plane.
     *
     * @param bounds  The bounds of the plane.
     * @param density The density of the plane.
     */
    public VariablePlane(Rectangle bounds, double density) {
        this(null, Color.BLACK, bounds, density, density / 2, 1.0);
    }
    
    /**
     * The constructor for a Variable Plane.
     *
     * @param bounds The bounds of the plane.
     */
    public VariablePlane(Rectangle bounds) {
        this(null, Color.BLACK, bounds, 1.0, 0.5, 1.0);
    }
    
    
    //Methods
    
    /**
     * Calculates the structure of the Variable Plane.
     */
    @Override
    protected void calculate() {
        components.clear();
        
        Vector p1 = bounds.getP1();
        Vector p2 = bounds.getP3();
        
        Set<Vector> vs = new HashSet<>();
        UniqueVectorSet uniqueVectorSet = new UniqueVectorSet();
        Map<Vector, Double> vsm = new HashMap<>();
        Map<Vector, Double> vsmi = new HashMap<>();
        for (double x = p1.getX(); x <= p2.getX(); x += density) {
            for (double y = p1.getY(); y <= p2.getY(); y += density) {
                for (double n = -density; n <= density; n += density * 2) {
                    double z = p1.getZ() + ((x / (p2.getX() - p1.getX())) * (p2.getZ() - p1.getZ()));
                    
                    List<Vector> vt = new ArrayList<>();
                    vt.add(new Vector(x, y, z));
                    vt.add(new Vector(x + n, y + n, z));
                    vt.add(new Vector(new Vector(x + n, y, z)));
                    
                    uniqueVectorSet.alignVectorsToSet(vt);
                    
                    Triangle t = new Triangle(this, color, vt.get(0), vt.get(1), vt.get(2));
                    
                    vs.addAll(vt);
                }
            }
        }
        
        Environment.addTask(() -> {
            for (Vector v : vs) {
                if (vsm.containsKey(v)) {
                    double m = vsm.get(v);
                    v.setZ(v.getZ() + m);
                    
                    if (Math.abs(v.getZ() - vsmi.get(v)) >= variabilityRange) {
                        m = -m;
                        vsm.replace(v, m);
                    }
                } else {
                    vsmi.put(v, v.getZ());
                    vsm.put(v, (Math.random() - .5) / 400 * speed);
                }
            }
        });
        
        setVisible(visible);
    }
    
}
