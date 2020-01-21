/*
 * File:    Environment.java
 * Package: main
 * Author:  Zachary Gill
 */

package main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import camera.Camera;
import math.vector.Vector;
import objects.base.AbstractObject;
import objects.base.BaseObject;
import objects.base.ObjectInterface;
import objects.base.Scene;

/**
 * The main Environment.
 */
public class Environment {
    
    //Constants
    
    /**
     * The maximum number of frames to render per second.
     */
    public static final int MAX_FPS = 120;
    
    /**
     * The maximum x dimension of the Window.
     */
    public static final int MAX_SCREEN_X = Toolkit.getDefaultToolkit().getScreenSize().width;
    
    /**
     * The maximum y dimension of the Window.
     */
    public static final int MAX_SCREEN_Y = Toolkit.getDefaultToolkit().getScreenSize().height;
    
    /**
     * The maximum z dimension of the Window.
     */
    public static final int MAX_SCREEN_Z = 720;
    
    /**
     * The border from the edge of the Window.
     */
    public static final int SCREEN_BORDER = 50;
    
    /**
     * The origin of the Environment at.
     */
    public static final Vector ORIGIN = new Vector(0, 0, 0);
    
    /**
     * The acceptable rounding error for double precision.
     */
    public static final double OMEGA = 0.0000001;
    
    
    //Static Fields
    
    /**
     * The number of frames to render per second.
     */
    public static int fps = MAX_FPS;
    
    /**
     * The x dimension of the Window.
     */
    public static int screenX = MAX_SCREEN_X;
    
    /**
     * The y dimension of the Window.
     */
    public static int screenY = MAX_SCREEN_Y;
    
    /**
     * The z dimension of the Window.
     */
    public static int screenZ = MAX_SCREEN_Z;
    
    /**
     * The coordinates to center the Environment at.
     */
    public static Vector origin = ORIGIN.clone();
    
    
    //Fields
    
    /**
     * The Frame of the Window.
     */
    public JFrame frame;
    
    /**
     * The Scene to render.
     */
    public Scene scene = null;
    
    /**
     * The list of Objects to be rendered in the Environment.
     */
    public List<ObjectInterface> objects = new ArrayList<>();
    
    /**
     * The background color of the Environment.
     */
    public Color background = Color.WHITE;
    
    /**
     * Whether the main KeyListener has been set up or not.
     */
    private AtomicBoolean hasSetupMainKeyListener = new AtomicBoolean(false);
    
    
    //Constructors
    
    /**
     * Constructs an Environment.
     *
     * @param screenX The x dimension of the screen.
     * @param screenY The y dimension of the screen.
     */
    public Environment(int screenX, int screenY) {
        Environment.screenX = screenX;
        Environment.screenY = screenY;
    }
    
    /**
     * The default constructor for an Environment.
     */
    public Environment() {
    }
    
    
    //Methods
    
    /**
     * Sets up the Environment.
     */
    public void setup() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setLayout(new BorderLayout());
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
                    if (background != null) {
                        g2.setColor(background);
                        g2.fillRect(0, 0, screenX, screenY);
                    }
                    
                    for (BaseObject preparedBase : preparedBases) {
                        preparedBase.render(g2);
                    }
                }
            }
        };
        frame.getContentPane().add(renderPanel, BorderLayout.CENTER);
        
        renderPanel.setSize(screenX, screenY);
        frame.setSize(new Dimension(screenX + 16, screenY + 39));
        frame.setPreferredSize(new Dimension(screenX + 16, screenY + 39));
        
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * Runs the Environment.
     */
    public void run() {
        if (fps == 0) {
            frame.getContentPane().getComponent(0).repaint();
            
        } else {
            Timer renderTimer = new Timer();
            renderTimer.scheduleAtFixedRate(new TimerTask() {
                private AtomicBoolean rendering = new AtomicBoolean(false);
                
                @Override
                public void run() {
                    if (rendering.compareAndSet(false, true)) {
                        frame.getContentPane().getComponent(0).repaint();
                        rendering.set(false);
                    }
                }
            }, 0, 1000 / fps);
        }
    }
    
    /**
     * Adds the KeyListener for the main environment controls.
     */
    public void setupMainKeyListener() {
        if (!hasSetupMainKeyListener.compareAndSet(false, true)) {
            return;
        }
        
        frame.getContentPane().getComponent(0).addKeyListener(new KeyListener() {
            
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
    
    /**
     * Adds an Object to the Environment at runtime.
     *
     * @param object The Object to add to the Environment.
     */
    public void addObject(ObjectInterface object) {
        objects.add(object);
    }
    
    /**
     * Removes an Object from the Environment at runtime.
     *
     * @param object The Object to remove from the Environment.
     */
    public void removeObject(ObjectInterface object) {
        objects.remove(object);
    }
    
    
    //Setters
    
    /**
     * Sets the number of frames to render per second.
     *
     * @param fps The number of frames to render per second.
     */
    public void setFps(int fps) {
        this.fps = fps;
    }
    
    /**
     * Sets the x dimension of the Window.
     *
     * @param screenX The x dimension of the Window.
     */
    public void setScreenX(int screenX) {
        this.screenX = screenX;
    }
    
    /**
     * Sets the y dimension of the Window.
     *
     * @param screenY The y dimension of the Window.
     */
    public void setScreenY(int screenY) {
        this.screenY = screenY;
    }
    
    /**
     * Sets the z dimension of the Window.
     *
     * @param screenZ The z dimension of the Window.
     */
    public void setScreenZ(int screenZ) {
        this.screenZ = screenZ;
    }
    
    /**
     * Sets the coordinates to center the Environment at.
     *
     * @param origin The coordinates to center the Environment at.
     */
    public void setOrigin(Vector origin) {
        this.origin = origin;
    }
    
    /**
     * Sets the Scene to render.
     *
     * @param scene The Scene to render.
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }
    
    /**
     * Sets the background color of the Environment.
     *
     * @param background The background color of the Environment.
     */
    public void setBackground(Color background) {
        this.background = background;
    }
    
}
