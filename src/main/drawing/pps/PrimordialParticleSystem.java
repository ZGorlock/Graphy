/*
 * File:    PrimordialParticleSystem.java
 * Package: main.drawing.pps
 * Author:  Zachary Gill
 */

package main.drawing.pps;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.Environment2D;
import objects.base.Drawing;

/**
 * The Primordial Particle System.
 */
public class PrimordialParticleSystem extends Drawing {
    
    //Fields
    
    /**
     * The particle state.
     */
    public ParticleState particles;
    
    
    //Main Method
    
    /**
     * The main method of of the program.
     *
     * @param args Arguments to the main method.
     * @throws Exception When the Drawing class cannot be constructed.
     */
    public static void main(String[] args) throws Exception {
        runDrawing(PrimordialParticleSystem.class);
    }
    
    
    //Constructors
    
    /**
     * Constructor for a Primordial Particle System.
     *
     * @param environment The Environment to render the Primordial Particle System in.
     */
    public PrimordialParticleSystem(Environment2D environment) {
        super(environment);
    }
    
    
    //Methods
    
    /**
     * Sets up components for the Primordial Particle System.
     */
    @Override
    public void initComponents() {
        environment.setFps(120);
        environment.setSize(1000, 1000);
        environment.setBackground(Color.BLACK);
    }
    
    /**
     * Sets up controls for the Primordial Particle System.
     */
    @Override
    public void setupControls() {
    }
    
    /**
     * Starts drawing the Primordial Particle System.
     */
    @Override
    public void run() {
        particles = new ParticleState(this, 0.0011, 0, 4.0, 0.67, Math.toRadians(180), Math.toRadians(17), 10.0);
    }
    
    /**
     * Renders the Primordial Particle System.
     *
     * @return The rendered Primordial Particle System.
     */
    @Override
    public BufferedImage render() {
        BufferedImage img = new BufferedImage(Environment2D.drawingX, Environment2D.drawingY, BufferedImage.TYPE_INT_RGB);
        
        if (particles != null) {
            Graphics2D g2 = img.createGraphics();
            particles.step();
            particles.render(g2);
            g2.dispose();
        }
        
        return img;
    }
    
    /**
     * Produces an overlay for the Primordial Particle System.
     *
     * @param g The graphics output.
     */
    @Override
    public void overlay(Graphics2D g) {
    }
    
}
