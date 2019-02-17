/*
 * File:    Spirograph.java
 * Package: main.drawing
 * Author:  Zachary Gill
 */

package main.drawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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
    private int pointCount = 2000;
    
    /**
     * The set of points around the circle.
     */
    private final List<Vector> points = new ArrayList<>();
    
    /**
     * The multiplier for the graph.
     */
    private double multiplier = Math.random() * 1000.0 + 2.0;
    
    /**
     * The amount to increase the multiplier by over time.
     */
    private double multiplierStep = .00125;
    
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
        Environment2D environment = new Environment2D();
        environment.setFPS(120);
        environment.setScreenX(1000);
        environment.setScreenY(1000);
        environment.setBackground(Color.BLACK);
        environment.setup();
        
        Spirograph spirograph = new Spirograph(environment);
    
        setupControls();
    
        environment.run();
    }
    
    
    //Constructors
    
    /**
     * Constructs a Spirograph.
     * 
     * @param environment The Environment to render the Spirograph in.
     */
    public Spirograph(Environment2D environment) {
        super(environment);
        
        double slice = 2 * Math.PI / pointCount;
    
        for (int i = 0; i < pointCount; i++) {
            points.add(environment.getCenterPosition().plus(new Vector(Math.cos(slice * i), Math.sin(slice * i)).scale(radius)));
        }
        
        colorAnimation = new Object(Color.BLACK);
        colorAnimation.addColorAnimation(50000, 0);
    }
    
    
    //Methods
    
    int np = 255;
    int npp = 0;
    int nppp = 0;
    int mppp = 0;
    Color cp = Color.RED;
    
    /**
     * Renders the Spirograph.
     *
     * @param img The graphics output.
     */
    @Override
    public void render(BufferedImage img) {
        Graphics2D graphics = (Graphics2D) img.getGraphics();
        
        double slice = 2 * Math.PI / pointCount;
        Vector c = environment.getCenterPosition();
        
//        np -= ((nppp % 2 == 0) ? 1 : -1);
//        if (np == 0 || np == 255) {
//            nppp++;
//        }
//        npp += ((mppp % 2 == 0) ? 1 : -1);
//        if (npp == 0 || npp == 255) {
//            mppp++;
//        }
//        graphics.setColor(new Color(npp, np, np)); //colorAnimation.getColor()
        
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
