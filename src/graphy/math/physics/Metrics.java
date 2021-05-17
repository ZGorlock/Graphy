/*
 * File:    Metrics.java
 * Package: graphy.math.physics
 * Author:  Zachary Gill
 */

package graphy.math.physics;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import commons.math.component.vector.Vector;
import graphy.main.Environment;
import graphy.object.base.ObjectInterface;
import graphy.object.base.polygon.Polygon;
import graphy.object.polyhedron.Polyhedron;
import graphy.object.polyhedron.regular.platonic.Icosahedron;

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
        Polyhedron i = new Icosahedron(null, Environment.ORIGIN, new Color(0, 0, 0, 0), 1);
        
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
                    Math.random() * (b.getRaw(1) - b.getRaw(0)) + b.getRaw(0),
                    Math.random() * (b.getRaw(3) - b.getRaw(2)) + b.getRaw(2),
                    Math.random() * (b.getRaw(5) - b.getRaw(4)) + b.getRaw(4)
            );
            if (Space.pointInsidePolyhedron(polyhedron, p)) {
                within++;
            }
        }
        volume = ((double) within / SAMPLE_SET_FOR_VOLUME_APPROXIMATION) * ((b.getRaw(1) - b.getRaw(0)) * (b.getRaw(3) - b.getRaw(2)) * (b.getRaw(5) - b.getRaw(4)));
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
            if (vertex.getRawX() < xMin) {
                xMin = vertex.getRawX();
            }
            if (vertex.getRawX() > xMax) {
                xMax = vertex.getRawX();
            }
            if (vertex.getRawY() < yMin) {
                yMin = vertex.getRawY();
            }
            if (vertex.getRawY() > yMax) {
                yMax = vertex.getRawY();
            }
            if (vertex.getRawZ() < zMin) {
                zMin = vertex.getRawZ();
            }
            if (vertex.getRawZ() > zMax) {
                zMax = vertex.getRawZ();
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
