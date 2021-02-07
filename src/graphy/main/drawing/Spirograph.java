/*
 * File:    Spirograph.java
 * Package: graphy.main.drawing
 * Author:  Zachary Gill
 */

package graphy.main.drawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import commons.math.vector.Vector;
import graphy.main.Environment2D;
import graphy.object.base.Drawing;
import graphy.object.base.Object;

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
     * @throws Exception When the Drawing cannot be created.
     */
    public static void main(String[] args) throws Exception {
        runDrawing(Spirograph.class);
    }
    
    
    //Constructors
    
    /**
     * Constructs a Spirograph.
     *
     * @param environment The Environment to render the Spirograph in.
     */
    public Spirograph(Environment2D environment) {
        super(environment);
    }
    
    
    //Methods
    
    /**
     * Sets up components for the Spirograph.
     */
    @Override
    public void initComponents() {
        environment.frame.setTitle("Spirograph");
        
        environment.setFps(60);
        environment.setDoubleBuffering(true);
        environment.setSize(1250, 1250);
        environment.setBackground(Color.BLACK);
    }
    
    /**
     * Sets up controls for the Spirograph.
     */
    @Override
    public void setupControls() {
    }
    
    /**
     * Starts drawing the Spirograph.
     */
    @Override
    public void run() {
        double slice = 2 * Math.PI / pointCount;
        
        for (int i = 0; i < pointCount; i++) {
            points.add(environment.getCenterPosition().plus(new Vector(Math.cos(slice * i), Math.sin(slice * i)).scale(radius)));
        }
        
        colorAnimation = new Object(Color.BLACK);
        colorAnimation.addColorAnimation(50000, 0);
    }
    
    /**
     * Renders the Spirograph.
     *
     * @return The rendered Spirograph.
     */
    @Override
    public BufferedImage draw() {
        BufferedImage img = new BufferedImage(Environment2D.width, Environment2D.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = img.createGraphics();
        
        double slice = 2 * Math.PI / pointCount;
        Vector c = environment.getCenterPosition();
        
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
        
        graphics.dispose();
        return img;
    }
    
    /**
     * Produces an overlay for the Spirograph.
     *
     * @param g The graphics output.
     */
    @Override
    public void overlay(Graphics2D g) {
    }
    
}
