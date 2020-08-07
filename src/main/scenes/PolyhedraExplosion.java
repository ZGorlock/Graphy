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
    
    //Static Fields
    
    /**
     * The number of each polyhedra species to put in the scene.
     */
    public static final int speciesCount = 150;
    
    /**
     * The radius of the enclosing sphere of each polyhedron.
     */
    public static final double speciesRadius = 0.1;
    
    /**
     * The alpha of the colors of each polyhedron.
     */
    public static final int speciesAlpha = 255;
    
    
    //Fields
    
    /**
     * The number of tetrahedron species to include in the scene.
     */
    private int tetrahedronCount = speciesCount;
    
    /**
     * The number of hexahedron species to include in the scene.
     */
    private int hexahedronCount = speciesCount;
    
    /**
     * The number of octahedron species to include in the scene.
     */
    private int octahedronCount = speciesCount;
    
    /**
     * The number of dodecahedron species to include in the scene.
     */
    private int dodecahedronCount = speciesCount;
    
    /**
     * The number of icosahedron species to include in the scene.
     */
    private int icosahedronCount = speciesCount;
    
    /**
     * The color of the tetrahedron species, null for random colors.
     */
    private Color tetrahedronColor;
    
    /**
     * The color of the hexahedron species, null for random colors.
     */
    private Color hexahedronColor;
    
    /**
     * The color of the octahedron species, null for random colors.
     */
    private Color octahedronColor;
    
    /**
     * The color of the dodecahedron species, null for random colors.
     */
    private Color dodecahedronColor;
    
    /**
     * The color of the icosahedron species, null for random colors.
     */
    private Color icosahedronColor;
    
    /**
     * The radius of the enclosing sphere of each polyhedron.
     */
    private double radius = speciesRadius;
    
    /**
     * The alpha of the colors of each polyhedron.
     */
    private int alpha = speciesAlpha;
    
    
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
        
        final List<RegularPolyhedron> species = new ArrayList<>();
        
        for (int i = 0; i < tetrahedronCount; i++) {
            Color c = (tetrahedronColor == null) ? ColorUtility.getRandomColor(alpha) : tetrahedronColor;
            Tetrahedron tetrahedron = new Tetrahedron(center, c, radius);
            registerComponent(tetrahedron);
            Frame frame = new Frame(tetrahedron);
            
            tetrahedron.addRotationAnimation((Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI);
            tetrahedron.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
            tetrahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
            tetrahedron.registerFrame(frame);
            tetrahedron.setFrameColor(new Color(0, 0, 0, 0));
            tetrahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
            species.add(tetrahedron);
        }
        
        for (int i = 0; i < hexahedronCount; i++) {
            Color c = (hexahedronColor == null) ? ColorUtility.getRandomColor(alpha) : hexahedronColor;
            Hexahedron hexahedron = new Hexahedron(center, c, radius);
            registerComponent(hexahedron);
            Frame frame = new Frame(hexahedron);
            
            hexahedron.addRotationAnimation((Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI);
            hexahedron.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
            hexahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
            hexahedron.registerFrame(frame);
            hexahedron.setFrameColor(new Color(0, 0, 0, 0));
            hexahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
            species.add(hexahedron);
        }
        
        for (int i = 0; i < octahedronCount; i++) {
            Color c = (octahedronColor == null) ? ColorUtility.getRandomColor(alpha) : octahedronColor;
            Octahedron octahedron = new Octahedron(center, c, radius);
            registerComponent(octahedron);
            Frame frame = new Frame(octahedron);
            
            octahedron.addRotationAnimation((Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI);
            octahedron.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
            octahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
            octahedron.registerFrame(frame);
            octahedron.setFrameColor(new Color(0, 0, 0, 0));
            octahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
            species.add(octahedron);
        }
        
        for (int i = 0; i < dodecahedronCount; i++) {
            Color c = (dodecahedronColor == null) ? ColorUtility.getRandomColor(alpha) : dodecahedronColor;
            Dodecahedron dodecahedron = new Dodecahedron(center, c, radius);
            registerComponent(dodecahedron);
            Frame frame = new Frame(dodecahedron);
            
            dodecahedron.addRotationAnimation((Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI);
            dodecahedron.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
            dodecahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
            dodecahedron.registerFrame(frame);
            dodecahedron.setFrameColor(new Color(0, 0, 0, 0));
            dodecahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
            species.add(dodecahedron);
        }
        
        for (int i = 0; i < icosahedronCount; i++) {
            Color c = (icosahedronColor == null) ? ColorUtility.getRandomColor(alpha) : icosahedronColor;
            Icosahedron icosahedron = new Icosahedron(center, c, radius);
            registerComponent(icosahedron);
            Frame frame = new Frame(icosahedron);
            
            icosahedron.addRotationAnimation((Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI);
            icosahedron.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
            icosahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
            icosahedron.registerFrame(frame);
            icosahedron.setFrameColor(new Color(0, 0, 0, 0));
            icosahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
            species.add(icosahedron);
        }
        
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
                            for (RegularPolyhedron speciesEntry : species) {
                                Environment.removeTask(speciesEntry.animationTasks.get(1));
                                speciesEntry.animationTasks.remove(1);
                                
                                double[] values = speciesEntry.movementAnimations.get(0);
                                speciesEntry.movementAnimations.remove(0);
                                
                                speciesEntry.addMovementAnimation(values[0] * -20, values[1] * -20, values[2] * -20);
                            }
                            stage.set(2);
                        }
                    
                    case 2:
                        if (currentTime > 21000) {
                            for (RegularPolyhedron speciesEntry : species) {
                                Environment.removeTask(speciesEntry.animationTasks.get(1));
                                speciesEntry.animationTasks.remove(1);
                                
                                speciesEntry.movementAnimations.remove(0);
                                
                                speciesEntry.reposition(center);
                                if (speciesEntry.getDisplayMode() == AbstractObject.DisplayMode.EDGE) {
                                    speciesEntry.setDisplayMode(AbstractObject.DisplayMode.FACE);
                                    speciesEntry.addFrame(Color.BLACK);
                                } else {
                                    speciesEntry.setDisplayMode(AbstractObject.DisplayMode.EDGE);
                                    speciesEntry.addFrame(new Color(0, 0, 0, 0));
                                }
                                speciesEntry.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
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
