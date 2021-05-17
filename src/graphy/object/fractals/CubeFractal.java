/*
 * File:    CubeFractal.java
 * Package: graphy.objects.fractals
 * Author:  Zachary Gill
 */

package graphy.object.fractals;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import commons.math.component.vector.Vector;
import graphy.object.base.Object;
import graphy.object.polyhedron.regular.platonic.Hexahedron;

/**
 * Defines a Cube Fractal.
 */
public class CubeFractal extends Object {
    
    //Fields
    
    /**
     * The radius of the bounding sphere of the root Cube.
     */
    private double initialSize;
    
    /**
     * The scaling factor of child Cubes.
     */
    private double scalingFactor;
    
    /**
     * The number of iterations to render.
     */
    private int renderDepth;
    
    
    //Constructors
    
    /**
     * Constructor for a Cube Fractal.
     *
     * @param center        The center of the fractal.
     * @param color         The color of the fractal.
     * @param initialSize   The radius of the bounding sphere of the root Cube.
     * @param scalingFactor The scaling factor of child Cubes.
     * @param renderDepth   The number of iterations to render.
     */
    public CubeFractal(Vector center, Color color, double initialSize, double scalingFactor, int renderDepth) {
        super(center, color);
        
        this.initialSize = initialSize;
        this.scalingFactor = scalingFactor;
        this.renderDepth = renderDepth;
        calculate();
    }
    
    
    //Methods
    
    /**
     * Calculates the entities of the Cube Fractal.
     */
    @Override
    public void calculate() {
        int c = 0;
        double w = initialSize;
        
        List<Hexahedron> a = new ArrayList<>();
        List<Hexahedron> b = new ArrayList<>();
        List<Integer> aType = new ArrayList<>();
        List<Integer> bType = new ArrayList<>();
        
        Hexahedron o = new Hexahedron(this, center, color, w);
        b.add(o);
        bType.add(0);
        
        while (c < renderDepth) {
            c++;
            w /= scalingFactor;
            
            a.clear();
            a.addAll(b);
            aType.clear();
            aType.addAll(bType);
            b.clear();
            bType.clear();
            
            for (int i = 0; i < a.size(); i++) {
                Hexahedron as = a.get(i);
                int type = aType.get(i);
                
                if (type != 4) {
                    b.add(new Hexahedron(as, as.getCenter().plus(new Vector(-(w + as.getRadius()), 0, 0)), color, w));
                    bType.add(1);
                }
                if (type != 5) {
                    b.add(new Hexahedron(as, as.getCenter().plus(new Vector(0, -(w + as.getRadius()), 0)), color, w));
                    bType.add(2);
                }
                if (type != 6) {
                    b.add(new Hexahedron(as, as.getCenter().plus(new Vector(0, 0, -(w + as.getRadius()))), color, w));
                    bType.add(3);
                }
                if (type != 1) {
                    b.add(new Hexahedron(as, as.getCenter().plus(new Vector((w + as.getRadius()), 0, 0)), color, w));
                    bType.add(4);
                }
                if (type != 2) {
                    b.add(new Hexahedron(as, as.getCenter().plus(new Vector(0, (w + as.getRadius()), 0)), color, w));
                    bType.add(5);
                }
                if (type != 3) {
                    b.add(new Hexahedron(as, as.getCenter().plus(new Vector(0, 0, (w + as.getRadius()))), color, w));
                    bType.add(6);
                }
            }
            
        }
    }
    
}
