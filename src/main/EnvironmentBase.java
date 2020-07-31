/*
 * File:    EnvironmentBase.java
 * Package: main
 * Author:  Zachary Gill
 */

package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager2;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import camera.CaptureHandler;
import math.vector.Vector;
import utility.ScreenUtility;

/**
 * The base of the main Environment.
 */
public abstract class EnvironmentBase {
    
    //Constants
    
    /**
     * The maximum number of frames to render per second.
     */
    public static final int MAX_FPS = 120;
    
    /**
     * The display width of the screen.
     */
    private static final int DISPLAY_WIDTH = ScreenUtility.DISPLAY_WIDTH;
    
    /**
     * The display height of the screen.
     */
    private static final int DISPLAY_HEIGHT = ScreenUtility.DISPLAY_HEIGHT;
    
    /**
     * The maximum x dimension of the Window.
     */
    public static final int MAX_SCREEN_X = DISPLAY_WIDTH - ((DISPLAY_WIDTH % 2 == 0) ? 0 : 1);
    
    /**
     * The maximum y dimension of the Window.
     */
    public static final int MAX_SCREEN_Y = DISPLAY_HEIGHT - ((DISPLAY_HEIGHT % 2 == 0) ? 0 : 1);
    
    /**
     * The maximum z dimension of the Window.
     */
    public static final int MAX_SCREEN_Z = 720;
    
    /**
     * The origin of the Environment.
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
     * The x dimension of the Render Panel.
     */
    public static int width = MAX_SCREEN_X;
    
    /**
     * The y dimension of the Render Panel.
     */
    public static int height = MAX_SCREEN_Y;
    
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
     * The Canvas to render the Environment in.
     */
    public Canvas renderPanel;
    
    /**
     * The Layout Manager to use for the Window.
     */
    public LayoutManager2 layout;
    
    /**
     * The background color of the Environment.
     */
    public Color background;
    
    /**
     * The capture handler for the Environment.
     */
    protected CaptureHandler captureHandler;
    
    /**
     * Whether the main KeyListener has been set up or not.
     */
    protected AtomicBoolean hasSetupMainKeyListener = new AtomicBoolean(false);
    
    
    //Methods
    
    /**
     * Sets up the Environment.
     */
    public final void setup() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(layout);
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
        
        // panel to display render results
        renderPanel = new Canvas() {
            
            @Override
            public void update(Graphics g) {
                paint(g);
            }
            
            @Override
            public void paint(Graphics g) {
                do {
                    Graphics2D g2 = null;
                    try {
                        g2 = (Graphics2D) getBufferStrategy().getDrawGraphics();
                        print(g2);
                    } finally {
                        if (g2 != null) {
                            g2.dispose();
                        }
                    }
                    getBufferStrategy().show();
                } while (getBufferStrategy().contentsLost());
            }
            
            @Override
            public void print(Graphics g) {
                render((Graphics2D) g);
            }
            
        };
        frame.getContentPane().add(renderPanel);
        
        setSize(screenX, screenY, width, height);
        
        frame.pack();
        frame.setVisible(true);
        
        renderPanel.setIgnoreRepaint(true);
        renderPanel.createBufferStrategy(2);
        renderPanel.setFocusable(false);
        
        captureHandler = new CaptureHandler(this);
    }
    
    /**
     * Runs the Environment.
     */
    public final void run() {
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
     * Renders the Environment.
     *
     * @param g2 The 2D Graphics entity.
     */
    protected abstract void render(Graphics2D g2);
    
    /**
     * Sizes the Window.
     */
    protected void sizeWindow() {
        renderPanel.setSize(new Dimension(width, height));
        renderPanel.setPreferredSize(new Dimension(renderPanel.getSize()));
        frame.setSize(new Dimension(screenX + ScreenUtility.BORDER_WIDTH, screenY + ScreenUtility.BORDER_HEIGHT));
        frame.setPreferredSize(frame.getSize());
        
        if ((screenX == MAX_SCREEN_X) && (screenY == MAX_SCREEN_Y)) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            frame.setExtendedState(JFrame.NORMAL);
        }
    }
    
    /**
     * Colors the background of the Environment.
     *
     * @param g2 The 2D Graphics entity.
     */
    public void colorBackground(Graphics2D g2) {
        if (background != null) {
            Color cacheColor = g2.getColor();
            g2.setColor(background);
            g2.fillRect(0, 0, renderPanel.getWidth(), renderPanel.getHeight());
            g2.setColor(cacheColor);
        }
    }
    
    /**
     * Sets up the KeyListener for the main Environment controls.
     */
    public final void setupMainKeyListener() {
        if (!hasSetupMainKeyListener.compareAndSet(false, true)) {
            return;
        }
        addMainKeyListener();
    }
    
    /**
     * Adds the KeyListener for the main Environment controls.
     */
    protected void addMainKeyListener() {
        frame.addKeyListener(new KeyListener() {
            
            @Override
            public void keyTyped(KeyEvent e) {
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                
                if (key == KeyEvent.VK_C) {
                    captureHandler.captureListener(e.isControlDown());
                }
                if (key == KeyEvent.VK_V) {
                    captureHandler.recordingListener();
                }
                if (key == KeyEvent.VK_B) {
                    captureHandler.openCaptureDir();
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
            }
            
        });
    }
    
    
    //Setters
    
    /**
     * Sets the number of frames to render per second.
     *
     * @param fps The number of frames to render per second.
     */
    public void setFps(int fps) {
        Environment2D.fps = fps;
    }
    
    /**
     * Sets the dimensions of the Window and the Render Panel.
     *
     * @param screenX The width of the Window.
     * @param screenY The height of the Window.
     * @param width   The width of the Render Panel.
     * @param height  The height of the Render Panel.
     */
    public void setSize(int screenX, int screenY, int width, int height) {
        if (screenX > 0 && screenY > 0) {
            screenX -= (((screenX % 2) == 0) ? 0 : 1);
            screenY -= (((screenY % 2) == 0) ? 0 : 1);
            EnvironmentBase.screenX = Math.min(screenX, EnvironmentBase.MAX_SCREEN_X);
            EnvironmentBase.screenY = Math.min(screenY, EnvironmentBase.MAX_SCREEN_Y);
        }
        if (width > 0 && height > 0) {
            width -= (((width % 2) == 0) ? 0 : 1);
            height -= (((height % 2) == 0) ? 0 : 1);
            Environment.width = Math.min(width, Environment.screenX);
            Environment.height = Math.min(height, Environment.screenY);
        }
        sizeWindow();
    }
    
    /**
     * Sets the dimensions of the Window and the Render Panel.
     *
     * @param width  The width of the Window and the Render Panel.
     * @param height The height of the Window and the Render Panel.
     */
    public void setSize(int width, int height) {
        setSize(width, height, width, height);
    }
    
    /**
     * Sets the dimensions of the Window.
     *
     * @param screenX The width of the Window.
     * @param screenY The height of the Window.
     */
    public void setWindowSize(int screenX, int screenY) {
        setSize(-1, -1, screenX, screenY);
    }
    
    /**
     * Sets the dimensions of the Render Panel.
     *
     * @param width  The width of the Render Panel.
     * @param height The height of the Render Panel.
     */
    public void setRenderSize(int width, int height) {
        setSize(width, height, -1, -1);
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
     * Sets the background color of the Environment.
     *
     * @param background The background color of the Environment.
     */
    public void setBackground(Color background) {
        this.background = background;
        frame.getContentPane().setBackground(background);
    }
    
}
