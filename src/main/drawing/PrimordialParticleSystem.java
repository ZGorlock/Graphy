/*
 * File:    PrimordialParticleSystem.java
 * Package: main.drawing
 * Author:  Zachary Gill
 */

package main.drawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.Environment2D;
import math.vector.Vector;
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
    public BufferedImage draw() {
        BufferedImage img = new BufferedImage(Environment2D.width, Environment2D.height, BufferedImage.TYPE_INT_RGB);
        
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
    
    
    //Inner Classes
    
    /**
     * Defines a configuration of particles.
     */
    public class ParticleState {
        
        //Constants
        
        /**
         * The sensor angle for left neighbors.
         */
        private final double sensorLeftAngle = Math.toRadians(-135);
        
        /**
         * The sensor angle for right neighbors.
         */
        private final double sensorRightAngle = Math.toRadians(45);
        
        /**
         * The sensor angle of the aperture.
         */
        private final double sensorAperture = Math.toRadians(179.9999);
        
        
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
            
            int particleCount = (int) (Environment2D.screenWidth * Environment2D.screenHeight * populationDensity) - centerCount;
            
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
            position.setX(position.getX() % Environment2D.screenWidth);
            position.setY(position.getY() % Environment2D.screenHeight);
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
    
    /**
     * Defines a neighbor state.
     */
    public static class NeighborState {
        
        //Fields
        
        /**
         * The total number of neighbors.
         */
        public int count;
        
        /**
         * The number of neighbors to the right of the Particle.
         */
        public int right;
        
        /**
         * The number of neighbors to the left of the Particle.
         */
        public int left;
        
    }
    
}
