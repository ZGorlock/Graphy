/*
 * File:    Environment.java
 * Package: main
 * Author:  Zachary Gill
 */

package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import math.vector.Vector;
import objects.base.Drawing;
import utility.ScreenUtility;

/**
 * The main Environment.
 */
public class Environment2D {
    
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
     * The x dimension of the Drawing.
     */
    public static int drawingX = MAX_SCREEN_X;
    
    /**
     * The y dimension of the Drawing.
     */
    public static int drawingY = MAX_SCREEN_Y;
    
    
    //Fields
    
    /**
     * The Frame of the Window.
     */
    public JFrame frame;
    
    /**
     * The Panel to render the Drawing in.
     */
    public JPanel renderPanel;
    
    /**
     * The coordinates to center the Environment at.
     */
    public Vector origin = new Vector(0, 0);
    
    /**
     * The Drawing to render.
     */
    public Drawing drawing = null;
    
    /**
     * The background color of the Environment.
     */
    public Color background = null;
    
    
    //Constructors
    
    /**
     * The default constructor for an Environment2D.
     */
    public Environment2D() {
    }
    
    
    //Methods
    
    /**
     * Sets up the Environment.
     */
    public void setup() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        if ((screenX == MAX_SCREEN_X) && (screenY == MAX_SCREEN_Y)) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
        
        // panel to display render results
        renderPanel = new JPanel() {
            
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                if (background != null) {
                    g2.setColor(background);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
                
                if (drawing != null) {
                    BufferedImage img = drawing.render();
                    g2.drawImage(img, 0, 0, null);
                    drawing.overlay(g2);
                }
            }
        };
        frame.getContentPane().add(renderPanel, BorderLayout.CENTER);
        
        sizeWindow();
        
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * Sizes the window.
     */
    public void sizeWindow() {
        renderPanel.setSize(new Dimension(drawingX, drawingY));
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
     * Returns a random position on the screen.
     *
     * @return A random position on the screen.
     */
    public Vector getRandomPosition() {
        return new Vector(Math.random() * Environment2D.drawingX, Math.random() * Environment2D.drawingY);
    }
    
    /**
     * Returns the center position on the screen.
     *
     * @return The center position on the screen.
     */
    public Vector getCenterPosition() {
        return new Vector(Environment2D.drawingX, Environment2D.drawingY).scale(0.5);
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
     * Sets the dimensions of the Window and Drawing.
     *
     * @param screenX The x dimension of the Window and Drawing.
     * @param screenY The y dimension of the Window and Drawing.
     */
    public void setSize(int screenX, int screenY) {
        Environment2D.screenX = Math.min(screenX, Environment2D.MAX_SCREEN_X);
        Environment2D.screenY = Math.min(screenY, Environment2D.MAX_SCREEN_Y);
        Environment2D.drawingX = Environment2D.screenX;
        Environment2D.drawingY = Environment2D.screenY;
        sizeWindow();
    }
    
    /**
     * Sets the dimensions of the Drawing.
     *
     * @param drawingX The x dimension of the Drawing.
     * @param drawingY The y dimension of the Drawing.
     */
    public void setDrawingSize(int drawingX, int drawingY) {
        Environment2D.drawingX = Math.min(drawingX, Environment2D.screenX);
        Environment2D.drawingY = Math.min(drawingY, Environment2D.screenY);
        sizeWindow();
    }
    
    /**
     * Sets the Drawing to render.
     *
     * @param drawing The Drawing to render.
     */
    public void setDrawing(Drawing drawing) {
        this.drawing = drawing;
    }
    
    /**
     * Sets the background color of the Environment.
     *
     * @param background The background color.
     */
    public void setBackground(Color background) {
        this.background = background;
        frame.getContentPane().setBackground(background);
    }
    
}
