/*
 * File:    PolyhedraExplosion.java
 * Package: objects.scene
 * Author:  Zachary Gill
 */

package objects.scene;

import camera.Camera;
import main.Environment;
import math.vector.Vector;
import objects.base.AbstractObject;
import objects.base.Frame;
import objects.base.Object;
import objects.polyhedron.regular.platonic.*;
import utility.ColorUtility;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Defines a Polyhedra explosion scene.
 */
public class PolyhedraExplosion extends Object
{
    
    //Fields
    
    /**
     * The number of each polyhedron species to include in the scene.
     */
    private int tetrahedronCount;
    private int hexahedronCount;
    private int octahedronCount;
    private int dodecahedronCount;
    private int icosahedronCount;
    
    /**
     * The color of each of polyhedron species, null for random colors.
     */
    private Color tetrahedronColor;
    private Color hexahedronColor;
    private Color octahedronColor;
    private Color dodecahedronColor;
    private Color icosahedronColor;
    
    /**
     * The radius of the enclosing sphere of each polyhedron.
     */
    private double radius;
    
    
    //Constructors
    
    /**
     * Constructor for a Polyhedra Explosion scene.
     *
     * @param center            The center of the scene.
     * @param radius            The radius of the sphere enclosing each polyhedron.
     * @param tetrahedronCount  The number of tetrahedrons to include.
     * @param tetrahedronColor  The color of the tetrahedrons, null for random colors.
     * @param hexahedronCount   The number of hexahedrons to include.
     * @param hexahedronColor   The color of the hexahedrons, null for random colors.
     * @param octahedronCount   The number of octahedrons to include.
     * @param octahedronColor   The color of the octahedrons, null for random colors.
     * @param dodecahedronCount The number of dodecahedrons to include.
     * @param dodecahedronColor The color of the dodecahedrons, null for random colors.
     * @param icosahedronCount  The number of icosahedrons to include.
     * @param icosahedronColor  The color of the icosahedrons, null for random colors.
     */
    public PolyhedraExplosion(Vector center, double radius, int tetrahedronCount, Color tetrahedronColor, int hexahedronCount, Color hexahedronColor, int octahedronCount, Color octahedronColor, int dodecahedronCount, Color dodecahedronColor, int icosahedronCount, Color icosahedronColor)
    {
        super(center, Color.BLACK);
        
        this.radius = radius;
        this.tetrahedronCount = tetrahedronCount;
        this.tetrahedronColor = tetrahedronColor;
        this.hexahedronCount = hexahedronCount;
        this.hexahedronColor = hexahedronColor;
        this.octahedronCount = octahedronCount;
        this.octahedronColor = octahedronColor;
        this.dodecahedronCount = dodecahedronCount;
        this.dodecahedronColor = dodecahedronColor;
        this.icosahedronCount = icosahedronCount;
        this.icosahedronColor = icosahedronColor;
        
        calculate();
    }
    
    
    //Methods
    
    /**
     * Calculates the entities of the Polyhedra Explosion scene.
     */
    @Override
    public void calculate()
    {
        AtomicBoolean cameraInMotion = new AtomicBoolean(false);
        final AtomicInteger[] loop = {new AtomicInteger(1)};
        for (int i = 0; i < hexahedronCount; i++) {
            Color c = (hexahedronColor == null) ? ColorUtility.getRandomColor() : hexahedronColor;
            Hexahedron hexahedron = new Hexahedron(this, center, c, radius);
            Frame frame = new Frame(hexahedron);
            Timer waiter = new Timer();
            waiter.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    hexahedron.addRotationAnimation((Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI);
                    hexahedron.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
                    hexahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
                    hexahedron.setFrameColor(new Color(0, 0, 0, 0));
                    hexahedron.addProcess(new Runnable() {
                        private int count = 0;

                        @Override
                        public void run()
                        {
                            count++;

                            if (count == 9) {
                                if (cameraInMotion.compareAndSet(false, true)) {
                                    if ((loop[0].compareAndSet(0, 1)) || (loop[0].compareAndSet(1, 2))) {
                                        Camera.getActiveCameraView().addFluidTransition(Math.PI / 4, Math.PI / 2, 0, 10000);
                                    } else if ((loop[0].compareAndSet(2, 3)) || (loop[0].compareAndSet(3, 0))) {
                                        Camera.getActiveCameraView().addFluidTransition(-Math.PI / 4, Math.PI / 2, 0, 10000);
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
            Color c = (tetrahedronColor == null) ? ColorUtility.getRandomColor() : tetrahedronColor;
            Tetrahedron tetrahedron = new Tetrahedron(this, center, c, radius);
            Frame frame = new Frame(tetrahedron);
            Timer waiter = new Timer();
            waiter.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    tetrahedron.addRotationAnimation((Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI);
                    tetrahedron.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
                    tetrahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
                    tetrahedron.setFrameColor(new Color(0, 0, 0, 0));
                    tetrahedron.addProcess(new Runnable() {
                        private int count = 0;

                        @Override
                        public void run()
                        {
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
            Color c = (octahedronColor == null) ? ColorUtility.getRandomColor() : octahedronColor;
            Octahedron octahedron = new Octahedron(this, center, c, radius);
            Frame frame = new Frame(octahedron);
            Timer waiter = new Timer();
            waiter.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    octahedron.addRotationAnimation((Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI);
                    octahedron.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
                    octahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
                    octahedron.setFrameColor(new Color(0, 0, 0, 0));
                    octahedron.addProcess(new Runnable() {
                        private int count = 0;

                        @Override
                        public void run()
                        {
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
            Color c = (dodecahedronColor == null) ? ColorUtility.getRandomColor() : dodecahedronColor;
            Dodecahedron dodecahedron = new Dodecahedron(this, center, c, radius);
            Frame frame = new Frame(dodecahedron);
            Timer waiter = new Timer();
            waiter.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    dodecahedron.addRotationAnimation((Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI);
                    dodecahedron.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
                    dodecahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
                    dodecahedron.setFrameColor(new Color(0, 0, 0, 0));
                    dodecahedron.addProcess(new Runnable() {
                        private int count = 0;

                        @Override
                        public void run()
                        {
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
            Color c = (icosahedronColor == null) ? ColorUtility.getRandomColor() : icosahedronColor;
            Icosahedron icosahedron = new Icosahedron(this, center, c, radius);
            Frame frame = new Frame(icosahedron);
            Timer waiter = new Timer();
            waiter.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    icosahedron.addRotationAnimation((Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI);
                    icosahedron.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
                    icosahedron.setDisplayMode(AbstractObject.DisplayMode.EDGE);
                    icosahedron.setFrameColor(new Color(0, 0, 0, 0));
                    icosahedron.addProcess(new Runnable() {
                        private int count = 0;

                        @Override
                        public void run()
                        {
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
    
}
