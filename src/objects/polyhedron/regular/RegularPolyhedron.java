/*
 * File:    RegularPolyhedron.java
 * Package: objects.polyhedron.regular
 * Author:  Zachary Gill
 */

package objects.polyhedron.regular;

import math.vector.Vector;
import objects.base.AbstractObject;
import objects.base.polygon.Polygon;
import objects.base.simple.Text;
import objects.polyhedron.Polyhedron;

import java.awt.*;

/**
 * Defines a Regular Polyhedron.
 */
public class RegularPolyhedron extends Polyhedron {
    
    //Fields
    
    /**
     * The number of faces of the Polyhedron.
     */
    protected int numFaces;
    
    /**
     * The number of vertices of the Polyhedron.
     */
    protected int numVertices;
    
    /**
     * The radius of the bounding sphere of the Polyhedron.
     */
    protected double radius;
    
    /**
     * The list of vertices of the Polyhedron.
     */
    protected Vector[] vertices;
    
    /**
     * The list of vertex indices of the Polyhedron.
     */
    protected Text[] vertexIndices;
    
    /**
     * The list of face indices of the Polyhedron.
     */
    protected Text[] faceIndices;
    
    
    //Constructors
    
    /**
     * The constructor for a RegularPolyhedron.
     *
     * @param parent      The parent of the RegularPolyhedron.
     * @param center      The center of the RegularPolyhedron.
     * @param color       The color of the RegularPolyhedron.
     * @param numFaces    The number of faces of the RegularPolyhedron
     * @param numVertices The number of vertices of the RegularPolyhedron
     * @param radius      The radius of the bounding sphere of the RegularPolyhedron.
     */
    public RegularPolyhedron(AbstractObject parent, Vector center, Color color, int numFaces, int numVertices, double radius) {
        super(center, color);
        
        this.numFaces = numFaces;
        this.numVertices = numVertices;
        this.center = center;
        this.radius = radius;
        setParent(parent);
        calculate();
    }
    
    
    //Methods
    
    /**
     * Displays the indices of the vertices of the Polyhedron.
     */
    public void displayVertexIndices() {
        if (vertexIndices == null) {
            vertexIndices = new Text[numVertices];
            
            for (int i = 0; i < numVertices; i++) {
                vertexIndices[i] = new Text(this, Color.BLACK, vertices[i], String.valueOf(i));
            }
        }
        
        for (Text vertex : vertexIndices) {
            vertex.setVisible(true);
        }
    }
    
    /**
     * Hides the indices of the vertices of the Polyhedron.
     */
    public void hideVertexIndices() {
        if (vertexIndices != null) {
            for (Text vertex : vertexIndices) {
                vertex.setVisible(false);
            }
        }
    }
    
    /**
     * Displays the indices of the faces of the Polyhedron.
     */
    public void displayFaceIndices() {
        if (faceIndices == null) {
            faceIndices = new Text[numFaces];
            
            for (int i = 0; i < numFaces; i++) {
                if (components.get(i) instanceof Polygon) {
                    faceIndices[i] = new Text(this, Color.RED, Vector.averageVector(((Polygon) components.get(i)).getVertices()), String.valueOf(i));
                }
            }
        }
        
        for (Text face : faceIndices) {
            face.setVisible(true);
        }
    }
    
    /**
     * Hides the indices of the faces of the Polyhedron.
     */
    public void hideFaceIndices() {
        if (faceIndices != null) {
            for (Text face : faceIndices) {
                face.setVisible(false);
            }
        }
    }
    
    
    //Getters
    
    /**
     * Returns the number of faces of the Polyhedron.
     *
     * @return The number of faces of the Polyhedron.
     */
    public int getNumFaces() {
        return numFaces;
    }
    
    /**
     * Returns the radius of the bounding sphere of the Polyhedron.
     *
     * @return The radius of the bounding sphere of the Polyhedron.
     */
    public double getRadius() {
        return radius;
    }
    
    
    //Setters
    
    /**
     * Sets the color of a face of the Polyhedron.
     *
     * @param face  The index of the face of the Polyhedron.
     * @param color The color.
     */
    public void setFaceColor(int face, Color color) {
        if (face < 1 || face > numFaces) {
            return;
        }
        components.get(face - 1).setColor(color);
    }
    
    /**
     * Sets the radius of the bounding sphere of the Polyhedron.
     *
     * @param radius The new radius of the bounding sphere of the Polyhedron.
     */
    public void setRadius(double radius) {
        this.radius = radius;
        calculate();
    }
    
}
