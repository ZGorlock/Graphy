/*
 * File:    Spirograph.java
 * Package: main.drawing
 * Author:  Zachary Gill
 */

package main.drawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import main.Environment2D;
import math.vector.Vector;
import objects.base.Drawing;
import objects.base.Object;

/**
 * A Spirograph drawing.
 */
public class Spirograph extends Drawing {
    
    //Fields
    
    /**
     * The radius of the circle.
     */
    private int radius = 600;
    
    /**
     * The number of points around the circle.
     */
    private int pointCount = 250;
    
    /**
     * The set of points around the circle.
     */
    private final List<Vector> points = new ArrayList<>();
    
    /**
     * The multiplier for the graph.
     */
    private double multiplier = 2.0;
    
    /**
     * The amount to increase the multiplier by over time.
     */
    private double multiplierStep = .0025;
    
    /**
     * The Objects used to hold the color animation.
     */
    private Object colorAnimation;
    
    
    //Main Method
    
    /**
     * The main method of of the program.
     *
     * @param args Arguments to the main method.
     */
    public static void main(String[] args) {
        String[] environmentArgs = new String[]{};
        Environment2D.main(environmentArgs);
        Environment2D.setBackground(Color.BLACK);
        
        setupControls();
    
        Spirograph spirograph = new Spirograph();
        Environment2D.addDrawing(spirograph);
    }
    
    
    //Constructors
    
    /**
     * Constructs a Spirograph.
     */
    public Spirograph() {
        double slice = 2 * Math.PI / pointCount;
    
        for (int i = 0; i < pointCount; i++) {
            points.add(Environment2D.getCenterPosition().plus(new Vector(Math.cos(slice * i), Math.sin(slice * i)).scale(radius)));
        }
        
        colorAnimation = new Object(Color.BLACK);
        colorAnimation.addColorAnimation(50000, 0);
    }
    
    
    //Methods
    
    /**
     * Renders the Spirograph.
     *
     * @param graphics The graphics output.
     */
    @Override
    public void render(Graphics2D graphics) {
        double slice = 2 * Math.PI / pointCount;
        Vector c = Environment2D.getCenterPosition();
        
        graphics.setColor(colorAnimation.getColor());
        for (int i = 0; i < points.size(); i++) {
            Vector p = points.get(i);
            
            double value = i * multiplier;
            value %= pointCount;
            Vector destination = new Vector(Math.cos(slice * value), Math.sin(slice * value)).scale(radius).plus(c);
            
            graphics.drawLine((int) p.getX(), (int) p.getY(), (int) destination.getX(), (int) destination.getY());
        }
    
        graphics.setColor(Color.WHITE);
        graphics.drawOval((int) c.getX() - radius, (int) c.getY() - radius, radius * 2, radius * 2);
        
        multiplier += multiplierStep;
    }
    
}
