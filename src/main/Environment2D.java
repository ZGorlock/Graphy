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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import math.vector.Vector;
import objects.base.Drawing;

/**
 * The main Environment.
 */
public class Environment2D {
    
    //Fields
    
    /**
     * The number of frames to render per second.
     */
    public int FPS = 120;
    
    /**
     * The x dimension of the Window.
     */
    public int screenX = 2560;
    
    /**
     * The y dimension of the Window.
     */
    public int screenY = 1440;
    
    /**
     * The Frame of the Window.
     */
    public JFrame frame;
    
    /**
     * The coordinates to center the Environment at.
     */
    public Vector origin = new Vector(0, 0);
    
    /**
     * The Drawing to render.
     */
    public Drawing drawing = null;
    
    /**
     * The size of the Drawing to render.
     */
    public Vector drawingSize = new Vector(screenX, screenY);
    
    /**
     * The offset of the Drawing to render.
     */
    public Vector drawingOffset = new Vector(0, 0);
    
    /**
     * The background color of the Environment.
     */
    public Color background = Color.WHITE;
    
    
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
        
        frame.setSize(screenX, screenY);
        frame.setVisible(true);
    }
    
    /**
     * Runs the Environment.
     */
    public void run() {
        // panel to display render results
        JPanel renderPanel = new JPanel() {
        
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(background);
                g2.fillRect(0, 0, screenX, screenY);
                BufferedImage img = new BufferedImage((int) drawingSize.getX(), (int) drawingSize.getY(), BufferedImage.TYPE_INT_RGB);
            
                if (drawing != null) {
                    drawing.render(img);
                }
            
                g2.drawImage(img, (int) drawingOffset.getX(), (int) drawingOffset.getY(), null);
            }
        };
        frame.getContentPane().add(renderPanel, BorderLayout.CENTER);
        
        if (FPS == 0) {
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
            }, 0, 1000 / FPS);
        }
    }

    /**
     * Returns a random position on the screen.
     *
     * @return A random position on the screen.
     */
    public Vector getRandomPosition() {
        return new Vector(Math.random() * this.screenX, Math.random() * this.screenY);
    }
    
    /**
     * Returns the center position on the screen.
     *
     * @return The center position on the screen.
     */
    public Vector getCenterPosition() {
        return new Vector(this.screenX, this.screenY).scale(0.5);
    }
    
    
    //Setters
    
    /**
     * Sets the number of frames to render per second.
     * 
     * @param fps The number of frames to render per second.
     */
    public void setFPS(int fps) {
        this.FPS = fps;
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
     * Sets the Drawing to render.
     *
     * @param drawing The Drawing to render.
     */
    public void setDrawing(Drawing drawing) {
        this.drawing = drawing;
    }
    
    /**
     * Sets the size of the Drawing to render.
     *
     * @param size The size of the Drawing to render.
     */
    public void setDrawingSize(Vector size) {
        this.drawingSize = size;
        this.drawingOffset.setX((Environment.screenX > drawingSize.getX()) ? (Environment.screenX - drawingSize.getX()) / 2.0 : 0);
        this.drawingOffset.setY((Environment.screenY > drawingSize.getY()) ? (Environment.screenY - drawingSize.getY()) / 2.0 : 0);
    }
    
    /**
     * Sets the background color of the Environment.
     *
     * @param background The background color.
     */
    public void setBackground(Color background) {
        this.background = background;
    }
    
}
