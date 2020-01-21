/*
 * File:    PolyhedraExplosion.java
 * Package: main.scenes
 * Author:  Zachary Gill
 */

package main.scenes;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import camera.Camera;
import main.Environment;
import objects.base.AbstractObject;
import objects.base.Frame;
import objects.base.Scene;
import objects.polyhedron.regular.MetatronsCube;
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
    public static final int speciesCount = 100;
    
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
     * The main method for the Rubik's Cube scene.
     *
     * @param args The arguments to the main method.
     */
    public static void main(String[] args) {
        Environment environment = new Environment();
        environment.setup();
        environment.setupMainKeyListener();
        
        PolyhedraExplosion polyhedraExplosion = new PolyhedraExplosion(environment);
        polyhedraExplosion.setupCameras();
        polyhedraExplosion.setupControls();
        
        environment.addObject(polyhedraExplosion);
        environment.run();
    }
    
    
    //Constructors
    
    /**
     * Constructor for a Polyhedra Explosion scene.
     *
     * @param environment The Environment to render the Polyhedra Explosion in.
     */
    public PolyhedraExplosion(Environment environment) {
        super(environment);
        
        calculate();
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
        
        AtomicBoolean cameraInMotion = new AtomicBoolean(false);
        final AtomicInteger[] loop = {new AtomicInteger(1)};
        for (int i = 0; i < hexahedronCount; i++) {
            Color c = (hexahedronColor == null) ? ColorUtility.getRandomColor(alpha) : hexahedronColor;
            Hexahedron hexahedron = new Hexahedron(center, c, radius);
            registerComponent(hexahedron);
            Frame frame = new Frame(hexahedron);
            Timer waiter = new Timer();
            waiter.schedule(new TimerTask() {
                @Override
                public void run() {
                    hexahedron.addRotationAnimation((Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI);
                    hexahedron.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
                    hexahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
                    hexahedron.setFrameColor(new Color(0, 0, 0, 0));
                    hexahedron.addProcess(new Runnable() {
                        private int count = 0;
                        
                        @Override
                        public void run() {
                            count++;
                            
                            if (count == 9) {
                                if (cameraInMotion.compareAndSet(false, true)) {
                                    if ((loop[0].compareAndSet(0, 1)) || (loop[0].compareAndSet(1, 2))) {
                                        Camera.getActiveCameraView().addFluidTransition(-Math.PI / 4, -Math.PI / 2, 0, 10000);
                                    } else if ((loop[0].compareAndSet(2, 3)) || (loop[0].compareAndSet(3, 0))) {
                                        Camera.getActiveCameraView().addFluidTransition(Math.PI / 4, -Math.PI / 2, 0, 10000);
                                    }
                                }
                            } else if (count == 20) {
                                hexahedron.animationTimers.get(1).purge();
                                hexahedron.animationTimers.get(1).cancel();
                                hexahedron.animationTimers.remove(1);
                                
                                double[] values = hexahedron.movementAnimations.get(0);
                                hexahedron.movementAnimations.remove(0);
                                
                                hexahedron.addMovementAnimation(values[0] * -20, values[1] * -20, values[2] * -20);
                                
                            } else if (count == 21) {
                                hexahedron.animationTimers.get(1).purge();
                                hexahedron.animationTimers.get(1).cancel();
                                hexahedron.animationTimers.remove(1);
                                
                                hexahedron.movementAnimations.remove(0);
                                
                                hexahedron.reposition(center);
                                hexahedron.registerFrame(frame);
                                if (hexahedron.getDisplayMode() == AbstractObject.DisplayMode.EDGE) {
                                    hexahedron.setDisplayMode(AbstractObject.DisplayMode.FACE);
                                    hexahedron.setFrameColor(Color.BLACK);
                                    hexahedron.registerFrame(frame);
                                } else {
                                    hexahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
                                    hexahedron.setFrameColor(new Color(0, 0, 0, 0));
                                }
                                hexahedron.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
                                
                                cameraInMotion.set(false);
                                count = 0;
                            }
                        }
                    }, 1000);
                }
            }, 0);
            hexahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
        }
        
        for (int i = 0; i < tetrahedronCount; i++) {
            Color c = (tetrahedronColor == null) ? ColorUtility.getRandomColor(alpha) : tetrahedronColor;
            Tetrahedron tetrahedron = new Tetrahedron(center, c, radius);
            registerComponent(tetrahedron);
            Frame frame = new Frame(tetrahedron);
            Timer waiter = new Timer();
            waiter.schedule(new TimerTask() {
                @Override
                public void run() {
                    tetrahedron.addRotationAnimation((Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI);
                    tetrahedron.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
                    tetrahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
                    tetrahedron.setFrameColor(new Color(0, 0, 0, 0));
                    tetrahedron.addProcess(new Runnable() {
                        private int count = 0;
                        
                        @Override
                        public void run() {
                            count++;
                            
                            if (count == 20) {
                                tetrahedron.animationTimers.get(1).purge();
                                tetrahedron.animationTimers.get(1).cancel();
                                tetrahedron.animationTimers.remove(1);
                                
                                double[] values = tetrahedron.movementAnimations.get(0);
                                tetrahedron.movementAnimations.remove(0);
                                
                                tetrahedron.addMovementAnimation(values[0] * -20, values[1] * -20, values[2] * -20);
                                
                            } else if (count == 21) {
                                tetrahedron.animationTimers.get(1).purge();
                                tetrahedron.animationTimers.get(1).cancel();
                                tetrahedron.animationTimers.remove(1);
                                
                                tetrahedron.movementAnimations.remove(0);
                                
                                tetrahedron.reposition(center);
                                tetrahedron.registerFrame(frame);
                                if (tetrahedron.getDisplayMode() == AbstractObject.DisplayMode.EDGE) {
                                    tetrahedron.setDisplayMode(AbstractObject.DisplayMode.FACE);
                                    tetrahedron.setFrameColor(Color.BLACK);
                                    tetrahedron.registerFrame(frame);
                                } else {
                                    tetrahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
                                    tetrahedron.setFrameColor(new Color(0, 0, 0, 0));
                                }
                                tetrahedron.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
                                
                                count = 0;
                            }
                        }
                    }, 1000);
                }
            }, 0);
            tetrahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
        }
        
        for (int i = 0; i < octahedronCount; i++) {
            Color c = (octahedronColor == null) ? ColorUtility.getRandomColor(alpha) : octahedronColor;
            Octahedron octahedron = new Octahedron(center, c, radius);
            registerComponent(octahedron);
            Frame frame = new Frame(octahedron);
            Timer waiter = new Timer();
            waiter.schedule(new TimerTask() {
                @Override
                public void run() {
                    octahedron.addRotationAnimation((Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI);
                    octahedron.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
                    octahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
                    octahedron.setFrameColor(new Color(0, 0, 0, 0));
                    octahedron.addProcess(new Runnable() {
                        private int count = 0;
                        
                        @Override
                        public void run() {
                            count++;
                            
                            if (count == 20) {
                                octahedron.animationTimers.get(1).purge();
                                octahedron.animationTimers.get(1).cancel();
                                octahedron.animationTimers.remove(1);
                                
                                double[] values = octahedron.movementAnimations.get(0);
                                octahedron.movementAnimations.remove(0);
                                
                                octahedron.addMovementAnimation(values[0] * -20, values[1] * -20, values[2] * -20);
                                
                            } else if (count == 21) {
                                octahedron.animationTimers.get(1).purge();
                                octahedron.animationTimers.get(1).cancel();
                                octahedron.animationTimers.remove(1);
                                
                                octahedron.movementAnimations.remove(0);
                                
                                octahedron.reposition(center);
                                octahedron.registerFrame(frame);
                                if (octahedron.getDisplayMode() == AbstractObject.DisplayMode.EDGE) {
                                    octahedron.setDisplayMode(AbstractObject.DisplayMode.FACE);
                                    octahedron.setFrameColor(Color.BLACK);
                                    octahedron.registerFrame(frame);
                                } else {
                                    octahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
                                    octahedron.setFrameColor(new Color(0, 0, 0, 0));
                                }
                                octahedron.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
                                
                                count = 0;
                            }
                        }
                    }, 1000);
                }
            }, 0);
            octahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
        }
        
        for (int i = 0; i < dodecahedronCount; i++) {
            Color c = (dodecahedronColor == null) ? ColorUtility.getRandomColor(alpha) : dodecahedronColor;
            Dodecahedron dodecahedron = new Dodecahedron(center, c, radius);
            registerComponent(dodecahedron);
            Frame frame = new Frame(dodecahedron);
            Timer waiter = new Timer();
            waiter.schedule(new TimerTask() {
                @Override
                public void run() {
                    dodecahedron.addRotationAnimation((Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI);
                    dodecahedron.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
                    dodecahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
                    dodecahedron.setFrameColor(new Color(0, 0, 0, 0));
                    dodecahedron.addProcess(new Runnable() {
                        private int count = 0;
                        
                        @Override
                        public void run() {
                            count++;
                            
                            if (count == 20) {
                                dodecahedron.animationTimers.get(1).purge();
                                dodecahedron.animationTimers.get(1).cancel();
                                dodecahedron.animationTimers.remove(1);
                                
                                double[] values = dodecahedron.movementAnimations.get(0);
                                dodecahedron.movementAnimations.remove(0);
                                
                                dodecahedron.addMovementAnimation(values[0] * -20, values[1] * -20, values[2] * -20);
                                
                            } else if (count == 21) {
                                dodecahedron.animationTimers.get(1).purge();
                                dodecahedron.animationTimers.get(1).cancel();
                                dodecahedron.animationTimers.remove(1);
                                
                                dodecahedron.movementAnimations.remove(0);
                                
                                dodecahedron.reposition(center);
                                dodecahedron.registerFrame(frame);
                                if (dodecahedron.getDisplayMode() == AbstractObject.DisplayMode.EDGE) {
                                    dodecahedron.setDisplayMode(AbstractObject.DisplayMode.FACE);
                                    dodecahedron.setFrameColor(Color.BLACK);
                                    dodecahedron.registerFrame(frame);
                                } else {
                                    dodecahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
                                    dodecahedron.setFrameColor(new Color(0, 0, 0, 0));
                                }
                                dodecahedron.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
                                
                                count = 0;
                            }
                        }
                    }, 1000);
                }
            }, 0);
            dodecahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
        }
        
        for (int i = 0; i < icosahedronCount; i++) {
            Color c = (icosahedronColor == null) ? ColorUtility.getRandomColor(alpha) : icosahedronColor;
            Icosahedron icosahedron = new Icosahedron(center, c, radius);
            registerComponent(icosahedron);
            Frame frame = new Frame(icosahedron);
            Timer waiter = new Timer();
            waiter.schedule(new TimerTask() {
                @Override
                public void run() {
                    icosahedron.addRotationAnimation((Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI);
                    icosahedron.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
                    icosahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
                    icosahedron.setFrameColor(new Color(0, 0, 0, 0));
                    icosahedron.addProcess(new Runnable() {
                        private int count = 0;
                        
                        @Override
                        public void run() {
                            count++;
                            
                            if (count == 20) {
                                icosahedron.animationTimers.get(1).purge();
                                icosahedron.animationTimers.get(1).cancel();
                                icosahedron.animationTimers.remove(1);
                                
                                double[] values = icosahedron.movementAnimations.get(0);
                                icosahedron.movementAnimations.remove(0);
                                
                                icosahedron.addMovementAnimation(values[0] * -20, values[1] * -20, values[2] * -20);
                                
                            } else if (count == 21) {
                                icosahedron.animationTimers.get(1).purge();
                                icosahedron.animationTimers.get(1).cancel();
                                icosahedron.animationTimers.remove(1);
                                
                                icosahedron.movementAnimations.remove(0);
                                
                                icosahedron.reposition(center);
                                icosahedron.registerFrame(frame);
                                if (icosahedron.getDisplayMode() == AbstractObject.DisplayMode.EDGE) {
                                    icosahedron.setDisplayMode(AbstractObject.DisplayMode.FACE);
                                    icosahedron.setFrameColor(Color.BLACK);
                                    icosahedron.registerFrame(frame);
                                } else {
                                    icosahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
                                    icosahedron.setFrameColor(new Color(0, 0, 0, 0));
                                }
                                icosahedron.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
                                
                                count = 0;
                            }
                        }
                    }, 1000);
                }
            }, 0);
            icosahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
        }
    }
    
    /**
     * Sets up cameras for the Polyhedra Explosion scene.
     */
    @Override
    public void setupCameras() {
        Camera camera = new Camera(this, true, true);
        camera.setLocation(Math.PI / 2, 0, 5);
    }
    
    /**
     * Sets up controls for the Polyhedra Explosion scene.
     */
    @Override
    public void setupControls() {
    }
    
}
