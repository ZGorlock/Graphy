/*
 * File:    Environment.java
 * Package: main
 * Author:  Zachary Gill
 */

package main;

import camera.Camera;
import math.matrix.Matrix3;
import math.vector.Vector;
import math.vector.Vector3;
import objects.Cube;
import objects.Pyramid;
import objects.TriangularPyramid;
import objects.base.*;
import objects.base.Frame;
import objects.base.Object;
import utility.ColorUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The main Environment.
 */
public class Environment
{
    
    //Constants
    
    /**
     * The number of frames to render per second.
     */
    public static final int FPS = 60;
    
    /**
     * The dimensions of the Window.
     */
    public static final int screenX = 2560;
    public static final int screenY = 1440;
    public static final int screenZ = 720;
    
    
    /**
     * The border from the edge of the Window.
     */
    public static final int screenBorder = 50;
    
    /**
     * The min and max coordinate values to render.
     */
    public static double xMin = -100.0;
    public static double xMax = 100.0;
    public static double yMin = -100.0;
    public static double yMax = 100.0;
    public static double zMin = -100.0;
    public static double zMax = 100.0;
    
    
    //Static Fields
    
    /**
     * The Frame of the Window.
     */
    public static JFrame frame;
    
    /**
     * The transformation matrix for pitch and yaw.
     */
    public static Matrix3 transform;
    
    /**
     * The list of Objects to be rendered in the Environment.
     */
    private static List<ObjectInterface> objects = new ArrayList<>();
    
    /**
     * The coordinates to center the Environment at.
     */
    public static Vector origin = new Vector(0, 0, 0);
    
    
    // Static Methods
    
    /**
     * Creates objects in the Environment.
     */
    private static void createObjects()
    {
//        Axes axes = new Axes(false);
//        objects.add(axes);
        
        
        //Example 1
//        Cube cube = new Cube(Environment.origin, Color.BLUE, 2);
//        cube.setDisplayMode(AbstractObject.DisplayMode.EDGE);
//        objects.add(cube);
        
        
        //Example 2
//        Cube cube = new Cube(Environment.origin, Color.BLUE, 2);
//        cube.addRotationAnimation(Math.PI / 4, Math.PI / 4, Math.PI / 4);
//
//        for (int f = 1; f < 6; f++) {
//            cube.setFaceColor(f, ColorUtility.getRandomColor());
//        }
////        Frame frame = cube.addFrame(Color.BLACK);
//
////        frame.addColorAnimation(5000, 2500);
////        cube.addColorAnimation(5000, 0);
//        objects.add(cube);

        
        
        //Example 3
        AtomicBoolean cameraInMotion = new AtomicBoolean(false);
        for (int i = 0; i < 1250; i++) {
            Cube cube = new Cube(Environment.origin, ColorUtility.getRandomColor(), .1);
            Frame frame = new Frame(cube);
            Timer waiter = new Timer();
            waiter.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    cube.addRotationAnimation((Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI, (Math.random() * 2 * Math.PI) - Math.PI);
                    cube.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
                    cube.setDisplayMode(AbstractObject.DisplayMode.EDGE);
                    cube.frame.setColor(new Color(0, 0, 0, 0));
                    cube.addProcess(new Runnable() {
                        private int count = 0;

                        @Override
                        public void run()
                        {
                            count++;

                            if (count == 14) {
                                if (cameraInMotion.compareAndSet(false, true)) {
                                    Camera.getActiveCameraView().addFluidTransition(0, Math.PI / 2, 0, 5000);
                                }
                            } else if (count == 20) {
                                cube.animationTimers.get(1).purge();
                                cube.animationTimers.get(1).cancel();
                                cube.animationTimers.remove(1);

                                double[] values = cube.movementAnimations.get(0);
                                cube.movementAnimations.remove(0);

                                cube.addMovementAnimation(values[0] * -20, values[1] * -20, values[2] * -20);

                            } else if (count == 21) {
                                cube.animationTimers.get(1).purge();
                                cube.animationTimers.get(1).cancel();
                                cube.animationTimers.remove(1);

                                cube.movementAnimations.remove(0);

                                cube.reposition(Environment.origin);
                                cube.registerFrame(frame);
                                if (cube.getDisplayMode() == AbstractObject.DisplayMode.EDGE) {
                                    cube.setDisplayMode(AbstractObject.DisplayMode.FACE);
                                    cube.frame.setColor(Color.BLACK);
                                    cube.registerFrame(frame);
                                } else {
                                    cube.setDisplayMode(AbstractObject.DisplayMode.EDGE);
                                    cube.frame.setColor(new Color(0, 0, 0, 0));
                                }
                                cube.addMovementAnimation(Math.random() - .5, Math.random() - .5, Math.random() - .5);
    
                                cameraInMotion.set(false);
                                count = 0;
                            }
                        }
                    }, 1000);
                    //cube.addColorAnimation(3000, Math.random() * 3000 + 1);
                }
            }, 0);
            cube.setDisplayMode(AbstractObject.DisplayMode.EDGE);
            //objects.add(cube);
            objects.add(cube);
        }
        
        
        //Example 4
//        Object group = new Object(Environment.origin, Color.YELLOW);
//        Cube cube = new Cube(group, Environment.origin, ColorUtility.getRandomColor(), 1);
//
//        Color color2 = ColorUtility.getRandomColor();
//        List<Pyramid> pyramids = new ArrayList<>();
//        pyramids.add(new Pyramid(group, color2, (Rect) cube.getComponents().get(0), new Vector(0, 0, 1.5)));
//        pyramids.add(new Pyramid(group, color2, (Rect) cube.getComponents().get(1), new Vector(0, 0, -1.5)));
//        pyramids.add(new Pyramid(group, color2, (Rect) cube.getComponents().get(2), new Vector(0, 1.5, 0)));
//        pyramids.add(new Pyramid(group, color2, (Rect) cube.getComponents().get(3), new Vector(0, -1.5, 0)));
//        pyramids.add(new Pyramid(group, color2, (Rect) cube.getComponents().get(4), new Vector(1.5, 0, 0)));
//        pyramids.add(new Pyramid(group, color2, (Rect) cube.getComponents().get(5), new Vector(-1.5, 0, 0)));
//
//        Color color3 = ColorUtility.getRandomColor();
//        List<TriangularPyramid> subPyramids = new ArrayList<>();
//        for (Pyramid pyramid : pyramids) {
//            for (int i = 0; i < 4; i++) {
//                Triangle face = (Triangle) pyramid.getComponents().get(i);
//
//                Vector a = face.getP2().minus(face.getP1());
//                Vector b = face.getP3().minus(face.getP1());
//                Vector c = new Vector3(a).cross(b);
//
//                Vector m = face.getP1().average(face.getP2(), face.getP3());
//
//                c = c.normalize();
//                if ((m.plus(c)).distance(Environment.origin) < m.distance(Environment.origin)) {
//                    c = c.scale(-1);
//                }
//
//                Vector faceCenter = m.plus(c.scale(.01));
//                subPyramids.add(new TriangularPyramid(group, color3, new Triangle(face.getP1().midpoint(faceCenter), face.getP2().midpoint(faceCenter), face.getP3().midpoint(faceCenter)), .75));
//            }
//        }
//
//        Color color4 = ColorUtility.getRandomColor();
//        for (TriangularPyramid subPyramid : subPyramids) {
//            for (int i = 0; i < 3; i++) {
//                Triangle face = (Triangle) subPyramid.getComponents().get(i);
//
//                Vector a = face.getP2().minus(face.getP1());
//                Vector b = face.getP3().minus(face.getP1());
//                Vector c = new Vector3(a).cross(b);
//
//                Vector m = face.getP1().average(face.getP2(), face.getP3());
//
//                c = c.normalize();
//                if ((m.plus(c)).distance(Environment.origin) < m.distance(Environment.origin)) {
//                    c = c.scale(-1);
//                }
//
//                Vector faceCenter = m.plus(c.scale(.01));
//                new TriangularPyramid(group, color4, new Triangle(face.getP1().midpoint(faceCenter), face.getP2().midpoint(faceCenter), face.getP3().midpoint(faceCenter)), .25);
//            }
//        }
//
//        Frame frame = new Frame(group);
//
//        objects.add(group);
    
    }
    
    
    /**
     * The main method of of the program.
     *
     * @param args Arguments to the main method.
     */
    public static void main(String[] args) {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
        
        
        //add KeyListener for main controls
        setupMainKeyListener();
    
    
        //add cameras
        Camera camera = new Camera();
        Camera camera2 = new Camera();
        camera2.setLocation(Math.PI * 3 / 4, Math.PI * 3 / 4, 20);
        Camera.setActiveCamera(0);
        
        
        //add objects
        objects.clear();
        createObjects();
        
        
        // panel to display render results
        JPanel renderPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                
                
                List<BaseObject> preparedBases = new ArrayList<>();
                for (ObjectInterface object : objects) {
                    preparedBases.addAll(object.prepare());
                }
    
                preparedBases.sort((o1, o2) -> {
                    double d1 = o1.calculatePreparedDistance();
                    double d2 = o2.calculatePreparedDistance();
                    return Double.compare(d2, d1);
                });
                
                for (BaseObject preparedBase : preparedBases) {
                    preparedBase.render(g2);
                }
                
                
                g2.drawImage(img, 0, 0, null);
            }
        };
        pane.add(renderPanel, BorderLayout.CENTER);
        
        
        frame.setSize(screenX, screenY);
        frame.setVisible(true);
        
        
        Timer renderTimer = new Timer();
        renderTimer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                renderPanel.repaint();
            }
        }, 0, 1000 / FPS);
    }
    
    /**
     * Adds the KeyListener for the Camera main environment controls.
     */
    private static void setupMainKeyListener()
    {
        Environment.frame.addKeyListener(new KeyListener()
        {
            
            @Override
            public void keyTyped(KeyEvent e)
            {
            }
            
            @Override
            public void keyPressed(KeyEvent e)
            {
                int key = e.getKeyCode();
                
                if (key == KeyEvent.VK_DIVIDE) {
                    for (ObjectInterface object : objects) {
                        object.setDisplayMode(AbstractObject.DisplayMode.EDGE);
                    }
                }
                if (key == KeyEvent.VK_MULTIPLY) {
                    for (ObjectInterface object : objects) {
                        object.setDisplayMode(AbstractObject.DisplayMode.VERTEX);
                    }
                }
                if (key == KeyEvent.VK_SUBTRACT) {
                    for (ObjectInterface object : objects) {
                        object.setDisplayMode(AbstractObject.DisplayMode.FACE);
                    }
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e)
            {
            }
            
        });
    }
    
    
    //Functions
    
    /**
     * Adds an Object to the Environment at runtime.
     *
     * @param object The Object to add to the Environment.
     */
    public static void addObject(ObjectInterface object)
    {
        objects.add(object);
    }
    
    /**
     * Removes an Object from the Environment at runtime.
     *
     * @param object The Object to remove from the Environment.
     */
    public static void removeObject(ObjectInterface object)
    {
        objects.remove(object);
    }
    
}
