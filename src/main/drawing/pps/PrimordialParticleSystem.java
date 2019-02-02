/*
 * File:    PrimordialParticleSystem.java
 * Package: main.drawing.pps
 * Author:  Zachary Gill
 */

package main.drawing.pps;

import java.awt.Color;
import java.awt.Graphics2D;

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
        String[] environmentArgs = new String[]{};
        Environment2D.main(environmentArgs);
        Environment2D.setBackground(Color.BLACK);
        
        setupControls();
        
        PrimordialParticleSystem pps = new PrimordialParticleSystem();
        Environment2D.addDrawing(pps);
    }
    
    
    //Constructors
    
    /**
     * Constructor for a Primordial Particle System.
     */
    public PrimordialParticleSystem() {
        particles = new ParticleState(0.0011, 0, 4.0, 0.67, Math.toRadians(180), Math.toRadians(17), 7.0);
    }
    
    
    //Methods
    
    /**
     * Renders the Primordial Particle System.
     *
     * @param graphics The graphics output.
     */
    @Override
    public void render(Graphics2D graphics) {
        particles.step();
        particles.render(graphics);
    }
    
}
