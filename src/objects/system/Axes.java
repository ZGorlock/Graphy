/*
 * File:    Axes.java
 * Package: objects.system
 * Author:  Zachary Gill
 */

package objects.system;

import main.Environment;
import math.vector.Vector;
import objects.base.Object;
import objects.base.simple.Edge;
import objects.base.simple.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines the coordinate axes.
 */
public class Axes extends Object {
    
    
    //Fields
    
    /**
     * The Edges that make up the main coordinate Axes.
     */
    public List<Edge> mainAxes;
    
    /**
     * The Edges that make up the sub Axes.
     */
    public List<Edge> subAxes;
    
    /**
     * The radius of the axes;
     */
    public int axesRadius;
    
    /**
     * Whether or not to display the sub Axes.
     */
    public boolean displaySubAxes;
    
    
    //Constructors
    
    /**
     * The constructor for the coordinate Axes.
     *
     * @param axesRadius     The radius of the axes.
     * @param displaySubAxes Whether or not to display the sub axes.
     */
    public Axes(int axesRadius, boolean displaySubAxes) {
        super(Environment.origin, Color.BLACK);
        
        this.axesRadius = axesRadius;
        this.displaySubAxes = displaySubAxes;
        
        calculate();
        setClippingEnabled(false);
    }
    
    /**
     * The constructor for the coordinate Axes.
     *
     * @param axesRadius The radius of the axes.
     */
    public Axes(int axesRadius) {
        this(axesRadius, false);
    }
    
    
    //Methods
    
    /**
     * Calculates the Objects for the Camera Object.
     */
    @Override
    protected void calculate() {
        mainAxes = new ArrayList<>();
        subAxes = new ArrayList<>();
        
        //main axes
        Edge x = new Edge(this, Color.RED,
                new Vector(-axesRadius, 0, 0),
                new Vector(axesRadius, 0, 0));
        Edge y = new Edge(this, Color.GREEN,
                new Vector(0, -axesRadius, 0),
                new Vector(0, axesRadius, 0));
        Edge z = new Edge(this, Color.BLUE,
                new Vector(0, 0, -axesRadius),
                new Vector(0, 0, axesRadius));
        mainAxes.add(x);
        mainAxes.add(y);
        mainAxes.add(z);
    
        components.add(new Text(Color.BLACK, x.getV1(), "-X"));
        components.add(new Text(Color.BLACK, x.getV2(), "X"));
        components.add(new Text(Color.BLACK, y.getV1(), "-Y"));
        components.add(new Text(Color.BLACK, y.getV2(), "Y"));
        components.add(new Text(Color.BLACK, z.getV1(), "-Z"));
        components.add(new Text(Color.BLACK, z.getV2(), "Z"));

        //sub axes
        if (displaySubAxes) {
            Color subAxis = new Color(128, 128, 128, 32);
            for (int xg = -axesRadius; xg <= axesRadius; xg += 1) {
                for (int yg = -axesRadius; yg <= axesRadius; yg += 1) {
                    if (xg != 0 || yg != 0) {
                        subAxes.add(new objects.base.simple.Edge(this, subAxis,
                                new Vector(xg, yg, -axesRadius),
                                new Vector(xg, yg, axesRadius)));
                    }
                }
                for (int zg = -axesRadius; zg <= axesRadius; zg += 1) {
                    if (xg != 0 || zg != 0) {
                        subAxes.add(new objects.base.simple.Edge(this, subAxis,
                                new Vector(xg, -axesRadius, zg),
                                new Vector(xg, axesRadius, zg)));
                    }
                }
            }
            for (int yg = -axesRadius; yg <= axesRadius; yg += 1) {
                for (int xg = -axesRadius; xg <= axesRadius; xg += 1) {
                    if (yg != 0 || xg != 0) {
                        subAxes.add(new objects.base.simple.Edge(this, subAxis,
                                new Vector(xg, yg, -axesRadius),
                                new Vector(xg, yg, axesRadius)));
                    }
                }
                for (int zg = -axesRadius; zg <= axesRadius; zg += 1) {
                    if (yg != 0 || zg != 0) {
                        subAxes.add(new objects.base.simple.Edge(this, subAxis,
                                new Vector(-axesRadius, yg, zg),
                                new Vector(axesRadius, yg, zg)));
                    }
                }
            }
        }
        
        setVisible(true);
    }
    
}
