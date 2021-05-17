/*
 * File:    MandelbrotOld.java
 * Package: graphy.main.drawing
 * Author:  Zachary Gill
 */

package graphy.main.drawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import commons.math.component.vector.IntVector;
import commons.math.component.vector.Vector;
import graphy.main.Environment2D;
import graphy.object.base.Drawing;
import graphy.object.base.polygon.Rectangle;

/**
 * A Mandelbrot drawing.
 */
public class MandelbrotOld extends Drawing {
    
    //Static Fields
    
    /**
     * The maximum number of iterations for determining if a point is in the set.
     */
    public static final int MAX_ITERATIONS = 1000;
    
    /**
     * The color factor to apply for the coloring scheme.
     */
    public static final int COLOR_FACTOR = 10;
    
    /**
     * The percentage to zoom per step.
     */
    public static final double ZOOM_STEP = 0.75;
    
    /**
     * The percentage to zoom per step for slow zooms.
     */
    public static final double SLOW_ZOOM_STEP = 0.99;
    
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
    public final IntVector offset = new IntVector(0, 0);
    
    /**
     * The mathematical bounds of the Mandelbrot.
     */
    public Rectangle bounds;
    
    /**
     * The colors for painting the Mandelbrot.
     */
    public final int[] colors = new int[MAX_ITERATIONS / COLOR_FACTOR];
    
    /**
     * The location to slow zoom to.
     */
    private Vector slowZoomTo = new Vector(
            0.251004192545193613127858564129342013402481966322603088153880158130118342411377044460335903569109029974830577473040521791862202620804388057367031844851715,
            .0000000136723440278498956363855799786211940098275946182822890638711641266657225239686535941616043103142296320806428032888628485431058181507295587901452113878999);
    
    /**
     * A flag indicating whether or not to allow zooming.
     */
    private boolean zoomEnabled = true;
    
    /**
     * A flag indicating whether or not to slow zoom.
     */
    private boolean slowZoomEnabled = false;
    
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
     * @throws Exception When the Drawing cannot be created.
     */
    public static void main(String[] args) throws Exception {
        runDrawing(MandelbrotOld.class);
    }
    
    
    //Constructors
    
    /**
     * Constructs a Mandelbrot.
     *
     * @param environment The Environment to render the Spirograph in.
     */
    public MandelbrotOld(Environment2D environment) {
        super(environment);
    }
    
    
    //Methods
    
    /**
     * Sets up components for the Mandelbrot.
     */
    @Override
    public void initComponents() {
        environment.setSize(1000, 1000);
        environment.setBackground(Color.BLACK);
    }
    
    /**
     * Sets up controls for the Mandelbrot.
     */
    @Override
    public void setupControls() {
        environment.frame.addMouseListener(new MouseListener() {
            
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                if (!zoomEnabled) {
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
     * Starts drawing the Mandelbrot.
     */
    @Override
    public void run() {
        this.dimension = Math.max(Environment2D.screenWidth, Environment2D.screenHeight);
        this.offset.setX((dimension > Environment2D.screenWidth) ? (Environment2D.screenWidth - dimension) / 2 : 0);
        this.offset.setY((dimension > Environment2D.screenHeight) ? (Environment2D.screenHeight - dimension) / 2 : 0);
        
        if (slowZoomEnabled) {
            zoomEnabled = false;
            bounds = new Rectangle(new Vector(MIN_BOUND, MAX_BOUND).plus(slowZoomTo), new Vector(MAX_BOUND, MAX_BOUND).plus(slowZoomTo), new Vector(MAX_BOUND, MIN_BOUND).plus(slowZoomTo), new Vector(MIN_BOUND, MIN_BOUND).plus(slowZoomTo));
            bounds.setCenter(slowZoomTo);
        } else {
            bounds = new Rectangle(new Vector(MIN_BOUND, MAX_BOUND), new Vector(MAX_BOUND, MAX_BOUND), new Vector(MAX_BOUND, MIN_BOUND), new Vector(MIN_BOUND, MIN_BOUND));
            bounds.setCenter(new Vector(0, 0));
        }
        
        populateColors();
        
        if (slowZoomEnabled) {
            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
                if (rendering.compareAndSet(false, true)) {
                    zoom(null, SLOW_ZOOM_STEP);
                }
            }, 0, 50, TimeUnit.MILLISECONDS);
        }
    }
    
    /**
     * Renders the Mandelbrot.
     *
     * @return The rendered Mandelbrot.
     */
    @Override
    public BufferedImage draw() {
        if (rendering == null) {
            return super.draw();
        }
        
        BufferedImage img = new BufferedImage(Environment2D.width, Environment2D.height, BufferedImage.TYPE_INT_RGB);
        
        rendering.set(true);
        System.out.println("Rendering");
        
        for (int row = 0; row < dimension; row++) {
            int trueRow = row + offset.getRawY();
            if ((trueRow < 0) || (trueRow >= Environment2D.screenHeight)) {
                continue;
            }
            
            for (int col = 0; col < dimension; col++) {
                int trueCol = col + offset.getRawX();
                if ((trueCol < 0) || (trueCol >= Environment2D.screenWidth)) {
                    continue;
                }
                
                Vector coordinate = screenToMandelbrot(new Vector(col, row));
                
                double x = 0;
                double y = 0;
                int iteration = 0;
                while ((((x * x) + (y * y)) < 4) && (iteration < MAX_ITERATIONS)) {
                    double xNew = ((x * x) - (y * y)) + coordinate.getRawX();
                    y = (2 * x * y) + coordinate.getRawY();
                    x = xNew;
                    iteration++;
                }
                
                int color;
                if (iteration < MAX_ITERATIONS) {
                    color = colors[iteration % (MAX_ITERATIONS / COLOR_FACTOR)];
                } else {
                    color = Color.BLACK.getRGB();
                }
                img.setRGB(trueCol, trueRow, color);
            }
        }
        
        if (slowZoomEnabled && captureZoom) {
            File outputFolder = new File("tmp");
            if (!outputFolder.exists()) {
                //noinspection ResultOfMethodCallIgnored
                outputFolder.mkdir();
            }
            File capture = new File(outputFolder, "capture_" + captureIndex++ + ".jpg");
            try {
                ImageIO.write(img, "jpg", capture);
            } catch (IOException e) {
                System.err.println("Unable to output zoom capture: " + captureIndex);
            }
        }
        
        System.out.println("Done Rendering");
        rendering.set(false);
        
        return img;
    }
    
    /**
     * Produces an overlay for the Mandelbrot.
     *
     * @param g The graphics output.
     */
    @Override
    public void overlay(Graphics2D g) {
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
        
        bounds.setP1(new Vector(bounds.getCenter().getRawX() - (newWidth / 2.0), bounds.getCenter().getRawY() + (newHeight / 2.0)));
        bounds.setP2(new Vector(bounds.getCenter().getRawX() + (newWidth / 2.0), bounds.getCenter().getRawY() + (newHeight / 2.0)));
        bounds.setP3(new Vector(bounds.getCenter().getRawX() + (newWidth / 2.0), bounds.getCenter().getRawY() - (newHeight / 2.0)));
        bounds.setP4(new Vector(bounds.getCenter().getRawX() - (newWidth / 2.0), bounds.getCenter().getRawY() - (newHeight / 2.0)));
        
        environment.run();
    }
    
    /**
     * Populates the color set for the Mandelbrot.
     */
    private void populateColors() {
        for (int i = 0; i < (MAX_ITERATIONS / COLOR_FACTOR); i++) {
            colors[i] = Color.HSBtoRGB(i / (float) (MAX_ITERATIONS / (double) COLOR_FACTOR), 1, i / (i + 8f));
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
                mapValue(point.getRawX(), 0, dimension, bounds.getP1().getRawX(), bounds.getP2().getRawX()),
                mapValue(point.getRawY(), 0, dimension, bounds.getP1().getRawY(), bounds.getP4().getRawY())
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
                mapValue(point.getRawX(), 0, Environment2D.screenWidth, bounds.getP1().getRawX(), bounds.getP2().getRawX()),
                mapValue(point.getRawY(), 0, Environment2D.screenHeight, bounds.getP1().getRawY(), bounds.getP4().getRawY())
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
                mapValue(point.getRawX(), bounds.getP1().getRawX(), bounds.getP2().getRawX(), 0, dimension),
                mapValue(point.getRawY(), bounds.getP1().getRawY(), bounds.getP4().getRawY(), 0, dimension)
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
