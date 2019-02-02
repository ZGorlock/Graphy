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
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import math.vector.Vector;
import objects.base.Drawing;

/**
 * The main Environment.
 */
public class Environment2D {
    
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
     * The border from the edge of the Window.
     */
    public static final int screenBorder = 50;
    
    /**
     * The acceptable rounding error for double precision.
     */
    public static final double omega = 0.0000001;
    
    
    //Static Fields
    
    /**
     * The Frame of the Window.
     */
    public static JFrame frame;
    
    /**
     * The list of Drawings to be rendered in the Environment.
     */
    private static List<Drawing> drawings = new ArrayList<>();
    
    /**
     * The coordinates to center the Environment at.
     */
    public static Vector origin = new Vector(0, 0);
    
    /**
     * The background color of the Environment.
     */
    public static Color background = Color.WHITE;
    
    
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
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(background);
                g2.fillRect(0, 0, getWidth(), getHeight());
                BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                
                for (Drawing drawing : drawings) {
                    drawing.render(g2);
                }
                
                g2.drawImage(img, 0, 0, null);
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
    
    
    //Setters
    
    /**
     * Sets the background color of the Environment.
     * 
     * @param background The background color.
     */
    public static void setBackground(Color background) {
        Environment2D.background = background;
    }
    
    
    //Functions
    
    /**
     * Adds a Drawing to the Environment at runtime.
     *
     * @param drawing The Drawing to add to the Environment.
     */
    public static void addDrawing(Drawing drawing) {
        drawings.add(drawing);
    }
    
    /**
     * Removes a Drawing from the Environment at runtime.
     *
     * @param drawing The Drawing to remove from the Environment.
     */
    public static void removeDrawing(Drawing drawing) {
        drawings.remove(drawing);
    }
    
    /**
     * Returns a random position on the screen.
     *
     * @return A random position on the screen.
     */
    public static Vector getRandomPosition() {
        return new Vector(Math.random() * Environment2D.screenX, Math.random() * Environment2D.screenY);
    }
    
    /**
     * Returns the center position on the screen.
     *
     * @return The center position on the screen.
     */
    public static Vector getCenterPosition() {
        return new Vector(Environment2D.screenX, Environment2D.screenY).scale(0.5);
    }
    
}
