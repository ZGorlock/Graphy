/*
 * File:    Environment.java
 * Package: main
 * Author:  Zachary Gill
 */

package main;

import math.matrix.Matrix3;
import math.vector.Vector;
import objects.base.AbstractObject;
import objects.base.BaseObject;
import objects.base.ObjectInterface;

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
    public static final int screenX = 1920;
    public static final int screenY = 1080;
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
    
    /**
     * Whether the main KeyListener has been set up or not.
     */
    private static AtomicBoolean hasSetupMainKeyListener = new AtomicBoolean(false);
    
    
    
    //Main Method
    
    /**
     * The main method of of the program.
     *
     * @param args Arguments to the main method.
     */
    public static void main(String[] args) {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
        
        
        // panel to display render results
        JPanel renderPanel = new JPanel() {
    
            public void paintComponent(Graphics g) {
                List<BaseObject> preparedBases = new ArrayList<>();
                try {
                    for (ObjectInterface object : objects) {
                        preparedBases.addAll(object.prepare());
                    }
                } catch (ConcurrentModificationException ignored) {
                    return;
                }

                try {
                    preparedBases.sort((o1, o2) -> {
                        double d1 = o1.calculatePreparedDistance();
                        double d2 = o2.calculatePreparedDistance();
                        return Double.compare(d2, d1);
                    });
                } catch (IllegalArgumentException e) {
                    //TODO handle this
                }


                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);


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
            private AtomicBoolean rendering = new AtomicBoolean(false);
            
            @Override
            public void run()
            {
                if (rendering.compareAndSet(false, true)) {
                    renderPanel.repaint();
                    rendering.set(false);
                }
            }
        }, 0, 1000 / FPS);
    }
    
    
    //Static Methods
    
    /**
     * Creates objects in the Environment.
     */
    private static void createObjects()
    {
        
        //Cube Fractal
//        CubeFractal cubeFractal = new CubeFractal(Environment.origin, Color.BLACK, .25, 2, 5);
//        cubeFractal.addColorAnimation(10000, 0);
//        cubeFractal.addFrame(Color.WHITE);
//        objects.add(cubeFractal);
        
        
        //Cube Field
//        for (int i = 0; i < 200; i++) {
//            Hexahedron h = new Hexahedron(new Vector(Math.random() * 200 - 100, Math.random() * 200 - 100, Math.random() * 200 - 100), Color.BLUE, Math.random() * 3);
//            h.addFrame(Color.BLACK);
//            objects.add(h);
//        }
        
        
        //Animated Cube
//        Hexahedron cube = new Hexahedron(Environment.origin, Color.BLUE, 2);
//        cube.addRotationAnimation(Math.PI / 4, Math.PI / 4, Math.PI / 4);
//
////        for (int f = 1; f < 6; f++) {
////            cube.setFaceColor(f, ColorUtility.getRandomColor());
////        }
//        Frame frame = cube.addFrame(Color.BLACK);
//
//        frame.addColorAnimation(5000, 2500);
//        cube.addColorAnimation(5000, 0);
//        objects.add(cube);
    }
    
    /**
     * Adds the KeyListener for the Camera main environment controls.
     */
    public static void setupMainKeyListener()
    {
        if (!hasSetupMainKeyListener.compareAndSet(false, true)) {
            return;
        }
        
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
