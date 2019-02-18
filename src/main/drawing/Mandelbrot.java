/*
 * File:    Mandelbrot.java
 * Package: main.drawing
 * Author:  Zachary Gill
 */

package main.drawing;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import main.Environment2D;
import math.vector.Vector;
import objects.base.Drawing;
import objects.base.polygon.Rectangle;

/**
 * A Mandelbrot drawing.
 */
public class Mandelbrot extends Drawing {
    
    //Static Fields
    
    /**
     * The maximum number of iterations for determining if a point is in the set.
     */
    public static final int MAX_ITERATIONS = 100;
    
    /**
     * The percentage to zoom per step.
     */
    public static final double ZOOM_STEP = 0.75;
    
    /**
     * The percentage to zoom per step for slow zooms.
     */
    public static final double SLOW_ZOOM_STEP = 0.95;
    
    /**
     * The maximum bound.
     */
    public static final double MAX_BOUND = 2.0;
    
    /**
     * The minimum bound.
     */
    public static final double MIN_BOUND = -2.0;
    
    
    //Fields
    
    /**
     * The dimension of the drawing.
     */
    public int dimension;
    
    /**
     * The offset of the drawing.
     */
    public final Vector offset = new Vector(0, 0);
    
    /**
     * The mathematical bounds of the Mandelbrot.
     */
    public Rectangle bounds;
    
    /**
     * The colors for painting the Mandelbrot.
     */
    public final int[] colors = new int[MAX_ITERATIONS];
    
    /**
     * The location to slow zoom to.
     */
    private Vector slowZoomTo = new Vector(
            0.251004192545193613127858564129342013402481966322603088153880158130118342411377044460335903569109029974830577473040521791862202620804388057367031844851715,
            .0000000136723440278498956363855799786211940098275946182822890638711641266657225239686535941616043103142296320806428032888628485431058181507295587901452113878999);
    
    /**
     * A flag indicating whether or not to slow zoom.
     */
    private boolean slowZoomEnabled = true;
    
    /**
     * A flag indicating whether or not to capture the slow zoom.
     */
    private boolean captureZoom = true;
    
    /**
     * The index of the slow zoom capture.
     */
    private int captureIndex = 0;
    
    /**
     * A flag indicating whether or not the Mandelbrot is rendering.
     */
    private AtomicBoolean rendering = new AtomicBoolean(true);
    
    
    //Main Method
    
    /**
     * The main method of of the program.
     *
     * @param args Arguments to the main method.
     */
    public static void main(String[] args) {
        Environment2D environment = new Environment2D(1000, 1000);
        environment.setFPS(0);
        environment.setBackground(Color.BLACK);
        environment.setup();
        
        Mandelbrot mandelbrot = new Mandelbrot(environment);
        mandelbrot.setupControls();
        
        environment.run();
    }
    
    
    //Constructors
    
    /**
     * Constructs a Mandelbrot.
     *
     * @param environment The Environment to render the Spirograph in.
     */
    public Mandelbrot(Environment2D environment) {
        super(environment);
        
        this.dimension = Math.max(environment.screenX, environment.screenY);
        this.offset.setX((dimension > environment.screenX) ? (environment.screenX - dimension) / 2.0 : 0);
        this.offset.setY((dimension > environment.screenY) ? (environment.screenY - dimension) / 2.0 : 0);
        
        if (slowZoomEnabled) {
            bounds = new Rectangle(new Vector(MIN_BOUND, MAX_BOUND).plus(slowZoomTo), new Vector(MAX_BOUND, MAX_BOUND).plus(slowZoomTo), new Vector(MAX_BOUND, MIN_BOUND).plus(slowZoomTo), new Vector(MIN_BOUND, MIN_BOUND).plus(slowZoomTo));
            bounds.setCenter(slowZoomTo);
        } else {
            bounds = new Rectangle(new Vector(MIN_BOUND, MAX_BOUND), new Vector(MAX_BOUND, MAX_BOUND), new Vector(MAX_BOUND, MIN_BOUND), new Vector(MIN_BOUND, MIN_BOUND));
            bounds.setCenter(new Vector(0, 0));
        }
        
        populateColors();
        
        if (slowZoomEnabled) {
            Timer slowZoom = new Timer();
            slowZoom.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (rendering.compareAndSet(false, true)) {
                        zoom(null, SLOW_ZOOM_STEP);
                    }
                }
            }, 0, 50);
        }
    }
    
    
    //Methods
    
    /**
     * Renders the Mandelbrot.
     *
     * @param img The graphics output.
     */
    @Override
    public void render(BufferedImage img) {
        if (rendering == null) {
            return;
        }
        
        rendering.set(true);
        
        for (int row = 0; row < dimension; row++) {
            int trueRow = row + (int) offset.getY();
            if ((trueRow < 0) || (trueRow >= environment.screenY)) {
                continue;
            }
            
            for (int col = 0; col < dimension; col++) {
                int trueCol = col + (int) offset.getX();
                if ((trueCol < 0) || (trueCol >= environment.screenX)) {
                    continue;
                }
                
                Vector coordinate = screenToMandelbrot(new Vector(col, row));
                
                double x = 0;
                double y = 0;
                int iteration = 0;
                while ((((x * x) + (y * y)) < 4) && (iteration < MAX_ITERATIONS)) {
                    double xNew = ((x * x) - (y * y)) + coordinate.getX();
                    y = (2 * x * y) + coordinate.getY();
                    x = xNew;
                    iteration++;
                }
                
                int color;
                if (iteration < MAX_ITERATIONS) {
                    color = colors[iteration];
                } else {
                    color = Color.BLACK.getRGB();
                }
                img.setRGB(trueCol, trueRow, color);
            }
        }
        
        if (slowZoomEnabled && captureZoom) {
            File outputFolder = new File("tmp");
            if (!outputFolder.exists()) {
                outputFolder.mkdir();
            }
            File capture = new File(outputFolder, "capture_" + captureIndex++ + ".jpg");
            try {
                ImageIO.write(img, "jpg", capture);
            } catch (IOException e) {
                System.err.println("Unable to output zoom capture: " + captureIndex);
            }
        }
        
        rendering.set(false);
    }
    
    /**
     * Sets up controls for the drawing.
     */
    @Override
    public void setupControls() {
        environment.frame.getContentPane().getComponent(0).addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                if (slowZoomEnabled) {
                    return;
                }
                
                double zoom;
                if (SwingUtilities.isLeftMouseButton(e)) {
                    zoom = ZOOM_STEP;
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    zoom = 1.0 / ZOOM_STEP;
                } else {
                    return;
                }
                
                Vector coordinate = clickToMandelbrot(new Vector(e.getX(), e.getY()));
                
                zoom(coordinate, zoom);
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                
            }
        });
    }
    
    /**
     * Zooms in on the Mandelbrot.
     * 
     * @param newCenter The new center of the rendering.
     * @param zoom      The factor to zoom by.
     */
    private void zoom(Vector newCenter, double zoom) {
        if (newCenter != null) {
            bounds.setCenter(newCenter);
        }
    
        double newWidth = bounds.getP1().distance(bounds.getP2()) * zoom;
        double newHeight = bounds.getP1().distance(bounds.getP4()) * zoom;
    
        bounds.setP1(new Vector(bounds.getCenter().getX() - (newWidth / 2.0), bounds.getCenter().getY() + (newHeight / 2.0)));
        bounds.setP2(new Vector(bounds.getCenter().getX() + (newWidth / 2.0), bounds.getCenter().getY() + (newHeight / 2.0)));
        bounds.setP3(new Vector(bounds.getCenter().getX() + (newWidth / 2.0), bounds.getCenter().getY() - (newHeight / 2.0)));
        bounds.setP4(new Vector(bounds.getCenter().getX() - (newWidth / 2.0), bounds.getCenter().getY() - (newHeight / 2.0)));
    
        environment.run();
    }
    
    /**
     * Populates the color set for the Mandelbrot.
     */
    private void populateColors() {
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            colors[i] = Color.HSBtoRGB(i / 256f, 1, i / (i + 8f));
        }
    }
    
    /**
     * Converts a screen coordinate to a Mandelbrot coordinate.
     * 
     * @param point The screen coordinate.
     * @return The corresponding Mandelbrot coordinate.
     */
    public Vector screenToMandelbrot(Vector point) {
        return new Vector(
                mapValue(point.getX(), 0, dimension, bounds.getP1().getX(), bounds.getP2().getX()),
                mapValue(point.getY(), 0, dimension, bounds.getP1().getY(), bounds.getP4().getY())
        );
    }
    
    /**
     * Converts a click coordinate to a Mandelbrot coordinate.
     *
     * @param point The click coordinate.
     * @return The corresponding Mandelbrot coordinate.
     */
    public Vector clickToMandelbrot(Vector point) {
        return new Vector(
                mapValue(point.getX(), 0, environment.screenX, bounds.getP1().getX(), bounds.getP2().getX()),
                mapValue(point.getY(), 0, environment.screenY, bounds.getP1().getY(), bounds.getP4().getY())
        );
    }
    
    /**
     * Converts a Mandelbrot coordinate to a screen coordinate.
     *
     * @param point The Mandelbrot coordinate.
     * @return The corresponding screen coordinate.
     */
    public Vector mandelbrotToScreen(Vector point) {
        return new Vector(
                mapValue(point.getX(), bounds.getP1().getX(), bounds.getP2().getX(), 0, dimension),
                mapValue(point.getY(), bounds.getP1().getY(), bounds.getP4().getY(), 0, dimension)
        );
    }
    
    /**
     * Maps a value from one range to another.
     *
     * @param value       The value.
     * @param inputStart  The start of the input range.
     * @param inputEnd    The end of the input range.
     * @param outputStart The start of the output range.
     * @param outputEnd   The end of the output range.
     * @return The value after it has been mapped from the input range to the output range.
     */
    public double mapValue(double value, double inputStart, double inputEnd, double outputStart, double outputEnd) {
        if (value < inputStart) {
            return outputStart;
        }
        if (value > inputEnd) {
            return outputEnd;
        }
        return (value - inputStart) / (inputEnd - inputStart) * (outputEnd - outputStart) + outputStart;
    }
    
}
