/*
 * File:    VariablePlane.java
 * Package: graphy.objects.complex
 * Author:  Zachary Gill
 */

package graphy.object.complex;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import commons.math.component.vector.Vector;
import graphy.main.Environment;
import graphy.math.vector.UniqueVectorSet;
import graphy.object.base.AbstractObject;
import graphy.object.base.Object;
import graphy.object.base.polygon.Rectangle;
import graphy.object.base.polygon.Triangle;

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
        for (double x = p1.getRawX(); x <= p2.getRawX(); x += density) {
            for (double y = p1.getRawY(); y <= p2.getRawY(); y += density) {
                for (double n = -density; n <= density; n += density * 2) {
                    double z = p1.getRawZ() + ((x / (p2.getRawX() - p1.getRawX())) * (p2.getRawZ() - p1.getRawZ()));
                    
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
                    v.setZ(v.getRawZ() + m);
                    
                    if (Math.abs(v.getRawZ() - vsmi.get(v)) >= variabilityRange) {
                        m = -m;
                        vsm.replace(v, m);
                    }
                } else {
                    vsmi.put(v, v.getRawZ());
                    vsm.put(v, (Math.random() - .5) / 400 * speed);
                }
            }
        });
        
        setVisible(visible);
    }
    
}
