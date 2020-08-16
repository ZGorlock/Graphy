/*
 * File:    PolyhedraExplosion.java
 * Package: main.scenes
 * Author:  Zachary Gill
 */

package main.scenes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import camera.Camera;
import main.Environment;
import objects.base.AbstractObject;
import objects.base.Frame;
import objects.base.Scene;
import objects.polyhedron.regular.MetatronsCube;
import objects.polyhedron.regular.RegularPolyhedron;
import objects.polyhedron.regular.platonic.Dodecahedron;
import objects.polyhedron.regular.platonic.Hexahedron;
import objects.polyhedron.regular.platonic.Icosahedron;
import objects.polyhedron.regular.platonic.Octahedron;
import objects.polyhedron.regular.platonic.Tetrahedron;
import utility.ColorUtility;

/**
 * Defines a Polyhedra Explosion scene.
 */
public class PolyhedraExplosion extends Scene {
    
    //Constants
    
    /**
     * The number of each polyhedron species to put in the scene.
     */
    public static final int SPECIES_COUNT = 150;
    
    /**
     * The radius of the enclosing sphere of each polyhedron.
     */
    public static final double SPECIES_RADIUS = 0.1;
    
    /**
     * The alpha of the colors of each polyhedron.
     */
    public static final int SPECIES_ALPHA = 255;
    
    
    //Fields
    
    /**
     * The list of polyhedra in the scene.
     */
    private final List<RegularPolyhedron> polyhedra = new ArrayList<>();
    
    /**
     * The number of tetrahedron to include in the scene.
     */
    private int tetrahedronCount = SPECIES_COUNT;
    
    /**
     * The number of hexahedron to include in the scene.
     */
    private int hexahedronCount = SPECIES_COUNT;
    
    /**
     * The number of octahedron to include in the scene.
     */
    private int octahedronCount = SPECIES_COUNT;
    
    /**
     * The number of dodecahedron to include in the scene.
     */
    private int dodecahedronCount = SPECIES_COUNT;
    
    /**
     * The number of icosahedron to include in the scene.
     */
    private int icosahedronCount = SPECIES_COUNT;
    
    /**
     * The color of the tetrahedron, null for random colors.
     */
    private Color tetrahedronColor;
    
    /**
     * The color of the hexahedron, null for random colors.
     */
    private Color hexahedronColor;
    
    /**
     * The color of the octahedron, null for random colors.
     */
    private Color octahedronColor;
    
    /**
     * The color of the dodecahedron, null for random colors.
     */
    private Color dodecahedronColor;
    
    /**
     * The color of the icosahedron, null for random colors.
     */
    private Color icosahedronColor;
    
    /**
     * The radius of the enclosing sphere of each polyhedron.
     */
    private double radius = SPECIES_RADIUS;
    
    /**
     * The alpha of the colors of each polyhedron.
     */
    private int alpha = SPECIES_ALPHA;
    
    
    //Main Method
    
    /**
     * The main method for the Polyhedra Explosion scene.
     *
     * @param args The arguments to the main method.
     * @throws Exception When the Scene cannot be created.
     */
    public static void main(String[] args) throws Exception {
        runScene(PolyhedraExplosion.class);
    }
    
    
    //Constructors
    
    /**
     * Constructor for a Polyhedra Explosion scene.
     *
     * @param environment The Environment to render the Polyhedra Explosion in.
     */
    public PolyhedraExplosion(Environment environment) {
        super(environment);
        
        Environment.enableRenderBuffering = false;
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Polyhedra Explosion.
     */
    @Override
    public void calculate() {
        MetatronsCube metatronsCube = new MetatronsCube(Environment.ORIGIN, 1, new Color(255, 0, 0, 64), new Color(255, 165, 0, 64), new Color(0, 255, 0, 64), new Color(0, 0, 255, 64), new Color(165, 0, 165, 64));
        metatronsCube.addFrame(Color.BLACK);
        registerComponent(metatronsCube);
        
        IntStream.range(0, tetrahedronCount).boxed().forEach(e -> polyhedra.add(new Tetrahedron(center, ((tetrahedronColor == null) ? ColorUtility.getRandomColor(alpha) : tetrahedronColor), radius)));
        IntStream.range(0, hexahedronCount).boxed().forEach(e -> polyhedra.add(new Hexahedron(center, ((hexahedronColor == null) ? ColorUtility.getRandomColor(alpha) : hexahedronColor), radius)));
        IntStream.range(0, octahedronCount).boxed().forEach(e -> polyhedra.add(new Octahedron(center, ((octahedronColor == null) ? ColorUtility.getRandomColor(alpha) : octahedronColor), radius)));
        IntStream.range(0, dodecahedronCount).boxed().forEach(e -> polyhedra.add(new Dodecahedron(center, ((dodecahedronColor == null) ? ColorUtility.getRandomColor(alpha) : dodecahedronColor), radius)));
        IntStream.range(0, icosahedronCount).boxed().forEach(e -> polyhedra.add(new Icosahedron(center, ((icosahedronColor == null) ? ColorUtility.getRandomColor(alpha) : icosahedronColor), radius)));
        
        for (RegularPolyhedron polyhedron : polyhedra) {
            registerComponent(polyhedron);
            polyhedron.addRotationAnimation((Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI);
            polyhedron.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
            polyhedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
            polyhedron.registerFrame(new Frame(polyhedron));
            polyhedron.setFrameColor(new Color(0, 0, 0, 0));
            polyhedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
        }
        
        animate();
    }
    
    /**
     * Animates the Polyhedra Explosion scene.
     */
    private void animate() {
        final AtomicLong timeOffset = new AtomicLong(Environment.currentTimeMillis());
        final AtomicInteger stage = new AtomicInteger(0);
        final AtomicInteger cameraStage = new AtomicInteger(1);
        final AtomicBoolean processing = new AtomicBoolean(false);
        
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            if (processing.compareAndSet(false, true)) {
                long currentTime = Environment.currentTimeMillis() - timeOffset.get();
                
                switch (stage.get()) {
                    case 0:
                        if (currentTime > 9000) {
                            Camera camera = Camera.getActiveCameraView(environment.perspective);
                            if (camera != null) {
                                if ((cameraStage.compareAndSet(0, 1)) || (cameraStage.compareAndSet(1, 2))) {
                                    camera.addFluidTransition(-Math.PI / 4, -Math.PI / 2, 0, 10000);
                                } else if ((cameraStage.compareAndSet(2, 3)) || (cameraStage.compareAndSet(3, 0))) {
                                    camera.addFluidTransition(Math.PI / 4, -Math.PI / 2, 0, 10000);
                                }
                            }
                            stage.set(1);
                        }
                        break;
                    
                    case 1:
                        if (currentTime > 20000) {
                            for (RegularPolyhedron polyhedron : polyhedra) {
                                Environment.removeTask(polyhedron.animationTasks.get(1));
                                polyhedron.animationTasks.remove(1);
                                
                                double[] values = polyhedron.movementAnimations.get(0);
                                polyhedron.movementAnimations.remove(0);
                                
                                polyhedron.addMovementAnimation(values[0] * -20, values[1] * -20, values[2] * -20);
                            }
                            stage.set(2);
                        }
                    
                    case 2:
                        if (currentTime > 21000) {
                            for (RegularPolyhedron polyhedron : polyhedra) {
                                Environment.removeTask(polyhedron.animationTasks.get(1));
                                polyhedron.animationTasks.remove(1);
                                
                                polyhedron.movementAnimations.remove(0);
                                
                                polyhedron.reposition(center);
                                if (polyhedron.getDisplayMode() == AbstractObject.DisplayMode.EDGE) {
                                    polyhedron.setDisplayMode(AbstractObject.DisplayMode.FACE);
                                    polyhedron.addFrame(Color.BLACK);
                                } else {
                                    polyhedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
                                    polyhedron.addFrame(new Color(0, 0, 0, 0));
                                }
                                polyhedron.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
                            }
                            timeOffset.addAndGet(currentTime);
                            stage.set(0);
                        }
                }
                processing.set(false);
            }
        }, 0, 10, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Sets up components for the Polyhedra Explosion scene.
     */
    @Override
    public void initComponents() {
        environment.setBackground(Color.BLACK);
    }
    
    /**
     * Sets up cameras for the Polyhedra Explosion scene.
     */
    @Override
    public void setupCameras() {
        Camera camera = new Camera(this, environment.perspective, true, true);
        camera.setLocation(Math.PI / 2, 0, 5);
    }
    
    /**
     * Sets up controls for the Polyhedra Explosion scene.
     */
    @Override
    public void setupControls() {
    }
    
}
