/*
 * File:    Particle.java
 * Package: main.pps
 * Author:  Zachary Gill
 */

package main.drawing.pps;

import java.awt.Color;
import java.awt.Graphics2D;

import math.vector.Vector;

/**
 * Defines a particle.
 */
public class Particle {
    
    //Fields
    
    /**
     * The position of the Particle.
     */
    private Vector position;
    
    /**
     * The orientation of the Particle.
     */
    private double phi;
    
    /**
     * The heading of the Particle.
     */
    private Vector heading;
    
    /**
     * The size of the Particle.
     */
    private double size;
    
    /**
     * The speed of the Particle.
     */
    private double speed;
    
    /**
     * The velocity of the Particle.
     */
    private Vector velocity;
    
    /**
     * The intrinsic rotation of the Particle.
     */
    private double alpha;
    
    /**
     * The turn of the Particle.
     */
    private double beta;
    
    /**
     * The state of neighbors near the Particle.
     */
    private NeighborState neighbors;
    
    /**
     * The color of the Particle.
     */
    private Color color;
    
    /**
     * The Particle State of the Particle.
     */
    private ParticleState state;
    
    
    //Constructor
    
    /**
     * The constructor for a Particle.
     * 
     * @param state    The Particle State of the Particle.
     * @param position The position of the Particle.
     * @param phi      The orientation of the Particle.
     * @param size     The size of the Particle.
     * @param speed    The speed of the Particle.
     * @param alpha    The intrinsic rotation of the Particle.
     * @param beta     The turn of the Particle.
     */
    public Particle(ParticleState state, Vector position, double phi, double size, double speed, double alpha, double beta) {
        this.state = state;
        this.position = position;
        this.phi = phi;
        this.size = size;
        this.speed = speed * size;
        this.velocity = new Vector(0, 0);
        this.alpha = alpha;
        this.beta = beta;
        this.heading = new Vector();
        this.neighbors = new NeighborState();
    }
    
    
    //Methods
    
    /**
     * Performs a time step.
     */
    public void step() {
        neighbors = state.getNeighbors(this);
        
        phi += alpha + beta * neighbors.count * (((neighbors.right > neighbors.left) ? 1 : -1));
//        while (phi <= -Math.PI) {
//            phi += Math.PI * 2;
//        }
//        while (phi > Math.PI) {
//            phi -= Math.PI * 2;
//        }
    
        heading = new Vector(Math.cos(phi), Math.sin(phi));
        velocity = heading.scale(speed);
        
        position = position.plus(velocity);
        position.setX(position.getX() % state.pps.environment.screenX);
        position.setY(position.getY() % state.pps.environment.screenY);
    }
    
    /**
     * Renders the Particle.
     *
     * @param img The render screen.
     */
    public void render(Graphics2D img) {
        calculateColor();
        
        Vector renderPosition = position;
        int renderSize = (int) size + 2;
        img.setColor(color);
        img.fillOval((int) (renderPosition.getX() - renderSize), (int) (renderPosition.getY() - renderSize), renderSize * 2, renderSize * 2);
//        g.setColor(Color.WHITE);
//        g.drawLine((int) renderPosition.getX(), (int) renderPosition.getY(), (int) (renderPosition.getX() + heading.getX() * 10), (int) (renderPosition.getY() + heading.getY() * 10));
    }
    
    /**
     * Calculates the color of the Particle.
     */
    private void calculateColor() {
        color = new Color(Integer.MAX_VALUE - Color.HSBtoRGB((float) (neighbors.count / 20.0), 1.0f, 1.0f));
    
        if (neighbors.count > 50) {
            color = Color.ORANGE;
        } else if (neighbors.count > 25) {
            color = Color.RED;
        } else if (neighbors.count > 20) {
            color = Color.MAGENTA;
        } else if (neighbors.count > 10) {
            color = Color.YELLOW;
        } else if (neighbors.count > 4) {
            color = Color.BLUE;
        } else {
            color = Color.GREEN;
        }
    }
    
    
    //Getters
    
    /**
     * Returns the position of the Particle.
     * 
     * @return The position of the Particle.
     */
    public Vector getPosition() {
        return position;
    }
    
    /**
     * Returns the orientation of the Particle.
     * 
     * @return The orientation of the Particle.
     */
    public double getOrientation() {
        return phi;
    }
    
    /**
     * Returns the heading of the Particle.
     *
     * @return The heading of the Particle.
     */
    public Vector getHeading() {
        return heading;
    }
    
}
