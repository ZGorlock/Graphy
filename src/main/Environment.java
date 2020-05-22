/*
 * File:    Environment.java
 * Package: main
 * Author:  Zachary Gill
 */

package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import camera.Camera;
import math.vector.Vector;
import objects.base.AbstractObject;
import objects.base.BaseObject;
import objects.base.ObjectInterface;
import objects.base.Scene;
import utility.ScreenUtility;

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
    public static final int MAX_SCREEN_X = ScreenUtility.DISPLAY_WIDTH;
    
    /**
     * The maximum y dimension of the Window.
     */
    public static final int MAX_SCREEN_Y = ScreenUtility.DISPLAY_HEIGHT;
    
    /**
     * The maximum z dimension of the Window.
     */
    public static final int MAX_SCREEN_Z = 720;
    
    /**
     * The origin of the Environment at.
     */
    public static final Vector ORIGIN = new Vector(0, 0, 0);
    
    /**
     * A flag indicating whether or not render buffering should be utilized.
     */
    public static final boolean ENABLE_RENDER_BUFFERING = true;
    
    /**
     * The maximum distance to render.
     */
    public static final double MAX_RENDER_DISTANCE = 250.0;
    
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
     * The x dimension of the Scene.
     */
    public static int sceneX = MAX_SCREEN_X;
    
    /**
     * The y dimension of the Scene.
     */
    public static int sceneY = MAX_SCREEN_Y;
    
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
     * The Panel to render the Scene in.
     */
    public JPanel renderPanel;
    
    /**
     * The Scene to render.
     */
    public Scene scene = null;
    
    /**
     * The unique perspective id for the Environment.
     */
    public final UUID perspective = UUID.randomUUID();
    
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
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
        
        // panel to display render results
        renderPanel = new JPanel() {
            
            public void paintComponent(Graphics g) {
                
                Camera camera = Camera.getActiveCameraView(perspective);
                if (camera == null) {
                    return;
                }
                
                synchronized (camera.inUpdate) {
                    List<BaseObject> preparedBases = new ArrayList<>();
                    try {
                        for (ObjectInterface object : objects) {
                            preparedBases.addAll(object.doPrepare(perspective));
                        }
                    } catch (ConcurrentModificationException ignored) {
                        return;
                    }
                    
                    preparedBases.sort((o1, o2) -> Double.compare(o2.getRenderDistance(), o1.getRenderDistance()));
                    
                    Graphics2D g2 = (Graphics2D) g;
                    if (background != null) {
                        g2.setColor(background);
                        g2.fillRect(0, 0, getWidth(), getHeight());
                    }
                    
                    for (BaseObject preparedBase : preparedBases) {
                        preparedBase.doRender(g2, perspective);
                    }
                }
            }
        };
        frame.getContentPane().add(renderPanel);
        
        sizeWindow();
        
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * Sizes the window.
     */
    public void sizeWindow() {
        renderPanel.setSize(new Dimension(sceneX, sceneY));
        renderPanel.setPreferredSize(renderPanel.getSize());
        frame.setSize(new Dimension(screenX + ScreenUtility.BORDER_WIDTH, screenY + ScreenUtility.BORDER_HEIGHT));
        frame.setPreferredSize(frame.getSize());
        
        if ((screenX == MAX_SCREEN_X) && (screenY == MAX_SCREEN_Y)) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            frame.setExtendedState(JFrame.NORMAL);
        }
        Camera.setScreenSize(perspective, new Vector(screenX, screenY, screenZ));
    }
    
    /**
     * Runs the Environment.
     */
    public void run() {
        if (fps == 0) {
            renderPanel.repaint();
            
        } else {
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
        
        renderPanel.addKeyListener(new KeyListener() {
            
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
        Environment.fps = fps;
    }
    
    /**
     * Sets the dimensions of the Window and Scene.
     *
     * @param screenX The x dimension of the Window and Scene.
     * @param screenY The y dimension of the Window and Scene.
     */
    public void setSize(int screenX, int screenY) {
        Environment.screenX = Math.min(screenX, Environment.MAX_SCREEN_X);
        Environment.screenY = Math.min(screenY, Environment.MAX_SCREEN_Y);
        Environment.sceneX = Environment.screenX;
        Environment.sceneY = Environment.screenY;
        sizeWindow();
    }
    
    /**
     * Sets the dimensions of the Scene.
     *
     * @param sceneX The x dimension of the Scene.
     * @param sceneY The y dimension of the Scene.
     */
    public void setSceneSize(int sceneX, int sceneY) {
        Environment.sceneX = Math.min(sceneX, Environment.screenX);
        Environment.sceneY = Math.min(sceneY, Environment.screenY);
        sizeWindow();
    }
    
    /**
     * Sets the coordinates to center the Environment at.
     *
     * @param origin The coordinates to center the Environment at.
     */
    public void setOrigin(Vector origin) {
        Environment.origin = origin;
    }
    
    /**
     * Sets the Scene to render.
     *
     * @param scene The Scene to render.
     */
    public void setScene(Scene scene) {
        this.scene = scene;
        frame.setTitle(scene.getName());
    }
    
    /**
     * Sets the background color of the Environment.
     *
     * @param background The background color of the Environment.
     */
    public void setBackground(Color background) {
        this.background = background;
        frame.getContentPane().setBackground(background);
    }
    
}
