/*
 * File:    PrimordialParticleSystem.java
 * Package: graphy.main.drawing
 * Author:  Zachary Gill
 */

package graphy.main.drawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import commons.graphics.DrawUtility;
import commons.math.component.vector.Vector;
import graphy.main.Environment2D;
import graphy.main.EnvironmentBase;
import graphy.object.base.Drawing;

/**
 * The Primordial Particle System.
 */
public class PrimordialParticleSystem extends Drawing {
    
    //Fields
    
    /**
     * The Particle State.
     */
    private ParticleState particleState;
    
    
    //Main Method
    
    /**
     * The main method of of the program.
     *
     * @param args Arguments to the main method.
     * @throws Exception When the Drawing cannot be created.
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
        environment.frame.setTitle("Primordial Particle System");
        
        EnvironmentBase.setFps(60);
        environment.setDoubleBuffering(false);
        environment.setSize(1024, 1024);
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
//        particleState = new ParticleState(0.0015, 0, 4.0, 10.72, 180, 17, 550);
        particleState = new ParticleState(0.0015, 0, 180, 17, 550);
    }
    
    /**
     * Renders the Primordial Particle System.
     *
     * @return The rendered Primordial Particle System.
     */
    @Override
    public BufferedImage draw() {
        BufferedImage img = new BufferedImage(Environment2D.width, Environment2D.height, BufferedImage.TYPE_INT_RGB);
        
        if (particleState != null) {
            Graphics2D g2 = img.createGraphics();
            particleState.step();
            particleState.render(g2);
            DrawUtility.dispose(g2);
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
        
        //Fields
        
        /**
         * The list of Particles in the state.
         */
        private final List<Particle> particles = new ArrayList<>();
        
        
        //Constructors
        
        /**
         * The constructor for a Particle State.
         *
         * @param populationDensity The density of Particles.
         * @param centerCount       The number of Particles to start in the center of the screen.
         * @param size              The size of a Particle.
         * @param speed             The speed of the Particles in the state.
         * @param alpha             The intrinsic rotation of the Particles in the state.
         * @param beta              The intrinsic turn of the Particles in the state.
         * @param gamma             The intrinsic attraction of Particles in the state.
         */
        public ParticleState(double populationDensity, int centerCount, double size, double speed, double alpha, double beta, double gamma) {
            int population = (int) (Environment2D.screenWidth * Environment2D.screenHeight * populationDensity);
            for (int i = 0; i < population; i++) {
                this.particles.add(new Particle(this,
                        ((centerCount-- > 0) ? Environment2D.getCenterPosition() : Environment2D.getRandomPosition()),
                        (Math.random() * Math.PI * 2),
                        (size > 0) ? size : (Math.random() * 4 + 2),
                        (speed > 0) ? speed : (Math.random() * 15 + 1),
                        Math.toRadians(alpha), Math.toRadians(beta), Math.toRadians(gamma)));
            }
        }
        
        /**
         * The constructor for a Particle State.
         *
         * @param populationDensity The density of Particles.
         * @param centerCount       The number of Particles to start in the center of the screen.
         * @param alpha             The intrinsic rotation of the Particles in the state.
         * @param beta              The intrinsic turn of the Particles in the state.
         * @param gamma             The intrinsic attraction of Particles in the state.
         */
        public ParticleState(double populationDensity, int centerCount, double alpha, double beta, double gamma) {
            this(populationDensity, centerCount, -1, -1, alpha, beta, gamma);
        }
        
        
        //Methods
        
        /**
         * Performs a time step.
         */
        public void step() {
            particles.forEach(Particle::step);
        }
        
        /**
         * Renders the Particle State.
         *
         * @param img The render screen.
         */
        public void render(Graphics2D img) {
            particles.forEach(p -> p.render(img));
        }
        
        /**
         * Returns the number of neighbors of a Particle.
         *
         * @return The number of neighbors of a Particle.
         */
        public NeighborState getNeighbors(Particle particle) {
            NeighborState neighborState = new NeighborState();
            
            particles.stream()
                    .filter(p -> !p.equals(particle))
                    .filter(p -> (p.position.distance(particle.position) < (p.gamma * p.size)))
                    .forEach(p -> {
                        neighborState.count++;
                        if (findSide(particle, p) > 0) {
                            neighborState.left++;
                        } else {
                            neighborState.right++;
                        }
                    });
            
            return neighborState;
        }
        
        /**
         * Determines the side of the orientation that a neighbor is on.
         *
         * @param particle The Particle.
         * @param neighbor The neighbor.
         * @return 1 if the neighbor is on the left, -1 if it is on the right, 0 if it is on the orientation.
         */
        private int findSide(Particle particle, Particle neighbor) {
            Vector pLine = particle.heading;
            Vector nLine = neighbor.position.minus(particle.position);
            
            double determinant = ((pLine.getRawX() * nLine.getRawY()) - (pLine.getRawY() * nLine.getRawX()));
            return (int) Math.signum(determinant);
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
        private double orientation;
        
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
         * The intrinsic turn of the Particle.
         */
        private double beta;
        
        /**
         * The intrinsic attraction of the Particle.
         */
        private double gamma;
        
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
         * @param state       The Particle State of the Particle.
         * @param position    The position of the Particle.
         * @param orientation The orientation of the Particle.
         * @param size        The size of the Particle.
         * @param speed       The speed of the Particle.
         * @param alpha       The intrinsic rotation of the Particle.
         * @param beta        The intrinsic turn of the Particle.
         * @param gamma       The intrinsic attraction of the Particle.
         */
        public Particle(ParticleState state, Vector position, double orientation, double size, double speed, double alpha, double beta, double gamma) {
            this.state = state;
            this.position = position;
            this.orientation = orientation;
            this.size = size;
            this.speed = speed / size;
            this.velocity = new Vector(0, 0);
            this.heading = new Vector(0, 0);
            this.alpha = alpha;
            this.beta = beta;
            this.gamma = gamma;
            this.neighbors = new NeighborState();
        }
        
        
        //Methods
        
        /**
         * Performs a time step.
         */
        public void step() {
            neighbors = state.getNeighbors(this);
            
            orientation += (alpha + (beta * Math.signum(neighbors.right - neighbors.left) * neighbors.count));
            
            heading = new Vector(Math.cos(orientation), Math.sin(orientation));
            velocity = heading.scale(speed);
            
            move();
        }
        
        /**
         * Performs a move.
         */
        public void move() {
            position = position.plus(velocity);
            position.setX(position.getRawX() % Environment2D.screenWidth);
            position.setY(position.getRawY() % Environment2D.screenHeight);
        }
        
        /**
         * Renders the Particle.
         *
         * @param img The render screen.
         */
        public void render(Graphics2D img) {
            calculateColor();
            
            DrawUtility.setColor(img, color);
            DrawUtility.fillCircle(img, position, (int) (size + 2));
//            DrawUtility.drawLine(img, position, position.plus(heading.times(velocity)));
        }
        
        /**
         * Calculates the color of the Particle.
         */
        private void calculateColor() {
//            color = new Color(Integer.MAX_VALUE - Color.HSBtoRGB((float) (neighbors.count / 20.0), 1.0f, 1.0f));
            
            if (neighbors.count > 30) {
                color = Color.RED;
            } else if (neighbors.count > 20) {
                color = Color.ORANGE;
            } else if (neighbors.count > 15) {
                color = Color.YELLOW;
            } else if (neighbors.count > 7) {
                color = Color.MAGENTA;
            } else if (neighbors.count > 3) {
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
    private static class NeighborState {
        
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
