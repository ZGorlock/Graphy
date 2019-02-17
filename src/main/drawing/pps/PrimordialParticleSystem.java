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
     */
    public static void main(String[] args) {
        Environment2D environment = new Environment2D();
        environment.setFPS(120);
        environment.setScreenX(1000);
        environment.setScreenY(1000);
        environment.setBackground(Color.BLACK);
        environment.setup();
        
        PrimordialParticleSystem pps = new PrimordialParticleSystem(environment);
    
        setupControls();
    
        environment.run();
    }
    
    
    //Constructors
    
    /**
     * Constructor for a Primordial Particle System.
     *
     * @param environment The Environment to render the Primordial Particle System in.
     */
    public PrimordialParticleSystem(Environment2D environment) {
        super(environment);
        
        particles = new ParticleState(this, 0.0011, 0, 4.0, 0.67, Math.toRadians(180), Math.toRadians(17), 7.0);
    }
    
    
    //Methods
    
    /**
     * Renders the Primordial Particle System.
     *
     * @param img The graphics output.
     */
    @Override
    public void render(BufferedImage img) {
        Graphics2D g2 = (Graphics2D) img.getGraphics();
        particles.step();
        particles.render(g2);
    }
    
}
