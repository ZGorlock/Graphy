/*
 * File:    Axes.java
 * Package: objects.system
 * Author:  Zachary Gill
 */

package objects.system;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import main.Environment;
import math.vector.Vector;
import objects.base.Object;
import objects.base.simple.Edge;

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
     * Whether or not to display the sub Axes.
     */
    public boolean displaySubAxes = false;
    
    
    //Constructors
    
    /**
     * The constructor for the coordinate Axes.
     *
     * @param displaySubAxes Whether or not to display the sub Axes.
     */
    public Axes(boolean displaySubAxes) {
        super(Environment.origin, Color.BLACK);
        
        this.displaySubAxes = displaySubAxes;
        
        calculate();
        
        setClippingEnabled(false);
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
        mainAxes.add(new Edge(this, Color.RED,
                new Vector(Environment.xMin, 0, 0),
                new Vector(Environment.xMax, 0, 0)));
        mainAxes.add(new Edge(this, Color.GREEN,
                new Vector(0, Environment.yMin, 0),
                new Vector(0, Environment.yMin, 0)));
        mainAxes.add(new Edge(this, Color.BLUE,
                new Vector(0, 0, Environment.zMax),
                new Vector(0, 0, Environment.zMin)));

//        //sub axes
        if (displaySubAxes) {
//        Color subAxis = new Color(128, 128, 128, 32);
//        for (int xg = xMin; xg <= xMax; xg += 1) {
//            for (int yg = yMin; yg <= yMax; yg += 1) {
//                if (xg != 0 || yg != 0) {
//                    edges.add(new objects.base.simple.Edge(new Vertex(xg, yg, zMin),
//                            new Vertex(xg, yg, zMax), subAxis));
//                }
//            }
//            for (int zg = zMin; zg <= zMax; zg += 1) {
//                if (xg != 0 || zg != 0) {
//                    edges.add(new objects.base.simple.Edge(new Vertex(xg, yMin, zg),
//                            new Vertex(xg, yMax, zg), subAxis));
//                }
//            }
//        }
//        for (int yg = yMin; yg <= yMax; yg += 1) {
//            for (int xg = xMin; xg <= xMax; xg += 1) {
//                if (yg != 0 || xg != 0) {
//                    edges.add(new objects.base.simple.Edge(new Vertex(xg, yg, zMin),
//                            new Vertex(xg, yg, zMax), subAxis));
//                }
//            }
//            for (int zg = zMin; zg <= zMax; zg += 1) {
//                if (yg != 0 || zg != 0) {
//                    edges.add(new objects.base.simple.Edge(new Vertex(xMin, yg, zg),
//                            new Vertex(xMax, yg, zg), subAxis));
//                }
//            }
//        }
        }
    }
    
}
