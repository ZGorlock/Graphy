/*
 * File:    Metrics.java
 * Package: math.physics
 * Author:  Zachary Gill
 */

package math.physics;

import main.Environment;
import math.vector.Vector;
import objects.base.ObjectInterface;
import objects.base.polygon.Polygon;
import objects.polyhedron.Polyhedron;
import objects.polyhedron.regular.platonic.Icosahedron;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains physical metrics about a Polyhedron. 
 */
public class Metrics {
    
    //Constants
    
    /**
     * The sample set to use for approximating the volume of a Polyhedron.
     */
    private static long SAMPLE_SET_FOR_VOLUME_APPROXIMATION = 10000; 
    
    
    //Fields
    
    /**
     * The Polyhedron these metrics refer to.
     */
    private Polyhedron polyhedron;
    
    /**
     * The volume of the Polyhedron.
     */
    protected double volume;
    
    /**
     * The density of the Polyhedron.
     */
    protected double density = 1.0;
    
    public static void main(String[] args) {
        Polyhedron i = new Icosahedron(null, Environment.origin, new Color(0, 0, 0, 0), 1);
        
        long t = System.currentTimeMillis();
        SAMPLE_SET_FOR_VOLUME_APPROXIMATION = 1000;
        Metrics m = new Metrics(i);
        System.out.println(m.getVolume());
        System.out.println(System.currentTimeMillis() - t);
        
        t = System.currentTimeMillis();
        SAMPLE_SET_FOR_VOLUME_APPROXIMATION = 10000;
        m = new Metrics(i);
        System.out.println(m.getVolume());
        System.out.println(System.currentTimeMillis() - t);
        
        t = System.currentTimeMillis();
        SAMPLE_SET_FOR_VOLUME_APPROXIMATION = 100000;
        m = new Metrics(i);
        System.out.println(m.getVolume());
        System.out.println(System.currentTimeMillis() - t);
        
        t = System.currentTimeMillis();
        SAMPLE_SET_FOR_VOLUME_APPROXIMATION = 1000000;
        m = new Metrics(i);
        System.out.println(m.getVolume());
        System.out.println(System.currentTimeMillis() - t);
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Metrics of a Polyhedron.
     *
     * @param polyhedron The Polyhedron to construct the Metrics for.
     */
    public Metrics(Polyhedron polyhedron) {
        this.polyhedron = polyhedron;
        calculateVolume();
    }
    
    
    //Methods
    
    /**
     * Calculates the volume of the Polyhedron.
     */
    private void calculateVolume() {
        Vector b = determineBoundingCube();
        long within = 0L;
        for (long i = 0; i < SAMPLE_SET_FOR_VOLUME_APPROXIMATION; i++) {
            Vector p = new Vector(
                    Math.random() * (b.get(1) - b.get(0)) + b.get(0),
                    Math.random() * (b.get(3) - b.get(2)) + b.get(2),
                    Math.random() * (b.get(5) - b.get(4)) + b.get(4)
            );
            if (Space.pointInsidePolyhedron(polyhedron, p)) {
                within++;
            }
        }
        volume = ((double) within / SAMPLE_SET_FOR_VOLUME_APPROXIMATION) * ((b.get(1) - b.get(0)) * (b.get(3) - b.get(2)) * (b.get(5) - b.get(4)));
    }
    
    /**
     * Returns the mass of the Polyhedron.
     *
     * @return The mass of the Polyhedron.
     */
    public double getMass() {
        return volume * density;
    }
    
    /**
     * Returns a list of the faces of the Polyhedron.
     *
     * @return The list of faces of the Polyhedron.
     */
    public List<Polygon> getFaces() {
        List<Polygon> faces = new ArrayList<>();
        for (ObjectInterface object : polyhedron.getComponents()) {
            if (object instanceof Polygon) {
                faces.add((Polygon) object);
            }
        }
        return faces;
    }
    
    /**
     * Returns a list of the vertices of the Polyhedron.
     *
     * @return The list of vertices of the Polyhedron.
     */
    public List<Vector> getVertices() {
        List<Vector> vertices = new ArrayList<>();
        List<Polygon> faces = getFaces();
        for (Polygon face : faces) {
            vertices.addAll(Arrays.asList(face.getVertices()));
        }
        return vertices;
    }
    
    /**
     * Determines the bounding cube of the Polyhedron.
     *
     * @return The bounding cube of the Polyhedron.
     */
    private Vector determineBoundingCube() {
        List<Vector> vertices = getVertices();
        
        double xMin = 0, xMax = 0, yMin = 0, yMax = 0, zMin = 0, zMax = 0;
        for (Vector vertex : vertices) {
            if (vertex.getX() < xMin) {
                xMin = vertex.getX();
            }
            if (vertex.getX() > xMax) {
                xMax = vertex.getX();
            }
            if (vertex.getY() < yMin) {
                yMin = vertex.getY();
            }
            if (vertex.getY() > yMax) {
                yMax = vertex.getY();
            }
            if (vertex.getZ() < zMin) {
                zMin = vertex.getZ();
            }
            if (vertex.getZ() > zMax) {
                zMax = vertex.getZ();
            }
        }
        
        return new Vector(xMin, xMax, yMin, yMax, zMin, zMax);
    }
    
    
    //Getters
    
    /**
     * Returns the volume of the Polyhedron.
     *
     * @return The volume of the Polyhedron.
     */
    public double getVolume() {
        return volume;
    }
    
    /**
     * Returns the density of the Polyhedron.
     *
     * @return The density of the Polyhedron.
     */
    public double getDensity() {
        return density;
    }
    
    
    //Setters
    
    /**
     * Sets the density of the Polyhedron.
     * 
     * @param density The density of the Polyhedron.
     */
    public void setDensity(double density) {
        this.density = density;
    }
    
}
