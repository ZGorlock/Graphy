/*
 * File:    Environment.java
 * Package: main
 * Author:  Zachary Gill
 */

package main;

import camera.Camera;
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
public class Environment {
    
    //Constants
    
    /**
     * The number of frames to render per second.
     */
    public static final int FPS = 120;
    
    /**
     * The x dimension of the Window.
     */
    public static final int screenX = 2560;
    
    /**
     * The y dimension of the Window.
     */
    public static final int screenY = 1440;
    
    /**
     * The z dimension of the Window.
     */
    public static final int screenZ = 720;
    
    /**
     * The border from the edge of the Window.
     */
    public static final int screenBorder = 50;
    
    /**
     * The acceptable rounding error for double precision.
     */
    public static final double epsilon = 0.0000001;
    
    
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
     * The background color of the Environment.
     */
    public static Color background = Color.WHITE;
    
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
                
                Camera camera = Camera.getActiveCameraView();
                if (camera == null) {
                    return;
                }
                
                synchronized (camera.inUpdate) {
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
                    } catch (IllegalArgumentException ignored) {
                    }
                    
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(background);
                    g2.fillRect(0, 0, screenX, screenY);
                    BufferedImage img = new BufferedImage(screenX, screenY, BufferedImage.TYPE_INT_ARGB);
                    
                    for (BaseObject preparedBase : preparedBases) {
                        preparedBase.render(g2);
                    }
                    
                    g2.drawImage(img, 0, 0, null);
                }
            }
        };
        pane.add(renderPanel, BorderLayout.CENTER);
        
        
        frame.setSize(screenX, screenY);
        frame.setVisible(true);
        
        
        Timer renderTimer = new Timer();
        renderTimer.scheduleAtFixedRate(new TimerTask() {
            private AtomicBoolean rendering = new AtomicBoolean(false);
            
            @Override
            public void run() {
                if (rendering.compareAndSet(false, true)) {
                    renderPanel.repaint();
                    rendering.set(false);
                }
            }
        }, 0, 1000 / FPS);
    }
    
    
    //Static Methods
    
    /**
     * Adds the KeyListener for the Camera main environment controls.
     */
    public static void setupMainKeyListener() {
        if (!hasSetupMainKeyListener.compareAndSet(false, true)) {
            return;
        }
        
        Environment.frame.addKeyListener(new KeyListener() {
            
            @Override
            public void keyTyped(KeyEvent e) {
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
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
            public void keyReleased(KeyEvent e) {
            }
            
        });
    }
    
    
    //Setters
    
    /**
     * Sets the background color of the Environment.
     *
     * @param background The background color.
     */
    public static void setBackground(Color background) {
        Environment.background = background;
    }
    
    
    //Functions
    
    /**
     * Adds an Object to the Environment at runtime.
     *
     * @param object The Object to add to the Environment.
     */
    public static void addObject(ObjectInterface object) {
        objects.add(object);
    }
    
    /**
     * Removes an Object from the Environment at runtime.
     *
     * @param object The Object to remove from the Environment.
     */
    public static void removeObject(ObjectInterface object) {
        objects.remove(object);
    }
    
}
