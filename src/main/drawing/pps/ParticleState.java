/*
 * File:    ParticleState.java
 * Package: package main.pps;
 * Author:  Zachary Gill
 */

package main.drawing.pps;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import math.vector.Vector;

/**
 * Defines a configuration of particles.
 */
public class ParticleState {
    
    //Constants
    
    /**
     * The sensor angle for left neighbors.
     */
    private static final double sensorLeftAngle = Math.toRadians(-135);
    
    /**
     * The sensor angle for right neighbors.
     */
    private static final double sensorRightAngle = Math.toRadians(45);
    
    /**
     * The sensor angle of the aperture.
     */
    private static final double sensorAperture = Math.toRadians(179.9999);
    
    
    //Fields
    
    /**
     * The Primordial Particle System.
     */
    public PrimordialParticleSystem pps;
    
    /**
     * The list of Particles in the state.
     */
    private final List<Particle> particles = new ArrayList<>();
    
    /**
     * The reactive radius of Particles in the state.
     */
    private double reactiveRadius;
    
    
    //Constructors
    
    /**
     * The constructor for a Particle State.
     * 
     * @param pps               The Primordial Particle System.
     * @param populationDensity The density of Particles.
     * @param centerCount       The number of Particles to start in the center of the screen.
     * @param size              The size of a Particle.
     * @param speed             The speed of the Particles in the state.
     * @param alpha             The intrinsic rotation of the Particles in the state.
     * @param beta              The turn of the Particles in the state.
     * @param reactiveRadius    The reactive radius of Particles in the state.
     */
    public ParticleState(PrimordialParticleSystem pps, double populationDensity, int centerCount, double size, double speed, double alpha, double beta, double reactiveRadius) {
        this.pps = pps;
        this.reactiveRadius = reactiveRadius * size;
        
        int particleCount = (int) (pps.environment.screenX * pps.environment.screenY * populationDensity) - centerCount;
        
        for (int i = 0; i < particleCount; i++) {
            particles.add(new Particle(this, pps.environment.getRandomPosition(), getRandomOrientation(), size, speed, alpha, beta));
        }
        for (int i = 0; i < centerCount; i++) {
            particles.add(new Particle(this, pps.environment.getCenterPosition(), getRandomOrientation(), size, speed, alpha, beta));
        }
    }
    
    
    //Methods
    
    /**
     * Performs a time step.
     */
    public void step() {
        Collections.shuffle(particles);
        particles.forEach(Particle::step);
    }
    
    /**
     * Renders the Particle State.
     * 
     * @param img The render screen.
     */
    public void render(Graphics2D img) {
        for (Particle p : particles) {
            p.render(img);
        }
    }
    
    /**
     * Returns the number of neighbors of a Particle.
     * 
     * @return The number of neighbors of a Particle.
     */
    public NeighborState getNeighbors(Particle particle) {
        NeighborState neighborState = new NeighborState();
    
        double particleLeftAngle = particle.getOrientation() + sensorLeftAngle;
        double particleRightAngle = particle.getOrientation() + sensorRightAngle;
    
        for (Particle p : particles) {
            if (p.equals(particle)) {
                continue;
            }
            if (p.getPosition().distance(particle.getPosition()) > reactiveRadius) {
                continue;
            }
            neighborState.count++;
        
            Vector delta = p.getPosition().minus(particle.getPosition());

//            while (delta.getX() < -PrimordialParticleSystem.screenX / 2) {
//                delta.setX(delta.getX() + PrimordialParticleSystem.screenX);
//            }
//            while (delta.getY() < -PrimordialParticleSystem.screenY / 2) {
//                delta.setY(delta.getY() + PrimordialParticleSystem.screenY);
//            }
//            while (delta.getX() > PrimordialParticleSystem.screenX / 2) {
//                delta.setX(delta.getX() - PrimordialParticleSystem.screenX);
//            }
//            while (delta.getY() > PrimordialParticleSystem.screenY / 2) {
//                delta.setY(delta.getY() - PrimordialParticleSystem.screenY);
//            }
        
            double neighborAngle = Math.atan2(delta.getX(), delta.getY());
            if ((neighborAngle <= (particleLeftAngle + sensorAperture)) && (neighborAngle >= particleLeftAngle)) {
                neighborState.left++;
            }
            if ((neighborAngle <= (particleRightAngle + sensorAperture)) && (neighborAngle >= particleRightAngle)) {
                neighborState.right++;
            }
        }
        
        return neighborState;
    }
    
//    /**
//     * Returns the number of neighbors of a Particle.
//     *
//     * @return The number of neighbors of a Particle.
//     */
//    public NeighborState getNeighbors(Particle particle) {
//        NeighborState neighborState = new NeighborState();
//        for (Particle p : particles) {
//            if (p.equals(particle)) {
//                continue;
//            }
//            if (particle.getPosition().distance(p.getPosition()) <= reactiveRadius) {
//                neighborState.count++;
//                int side = findSide(particle, p);
//                if (side > 0) {
//                    neighborState.left++;
//                } else if (side < 0) {
//                    neighborState.right++;
//                }
//            }
//        }
//        return neighborState;
//    }
    
    /**
     * Determines the side of the orientation that a neighbor is on.
     *
     * @param particle The Particle.
     * @param neighbor The neighbor.
     * @return 1 if the neighbor is on the left, -1 if it is on the right, 0 if it is on the orientation.
     */
    private int findSide(Particle particle, Particle neighbor) {
        Vector pa = particle.getHeading().scale(-10);
        Vector pb = particle.getHeading().scale(10);
        Vector pc = neighbor.getPosition();
        
        double determinant = ((pb.getX() - pa.getX()) * (pc.getY() - pa.getY()) - (pb.getY() - pa.getY()) * (pc.getX() - pa.getX()));
        return (determinant < 0) ? -1 : ((determinant > 0) ? 1 : 0);
    }
    
    /**
     * Returns a random orientation for a Particle.
     *
     * @return A random orientation for a Particle.
     */
    private double getRandomOrientation() {
        return Math.random() * Math.PI * 2;
    }
    
}
