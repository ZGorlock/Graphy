/*
 * File:    Tetrahedron.java
 * Package: objects.polyhedron.regular.platonic
 * Author:  Zachary Gill
 */

package objects.polyhedron.regular.platonic;

import java.awt.Color;

import math.vector.Vector;
import objects.base.AbstractObject;
import objects.base.polygon.Triangle;
import objects.polyhedron.regular.RegularPolyhedron;

/**
 * Defines a Tetrahedron.
 */
public class Tetrahedron extends RegularPolyhedron {
    
    //Constants
    
    /**
     * The number of faces of a Tetrahedron.
     */
    public static int TETRAHEDRON_FACES = 4;
    
    /**
     * The number of vertices of a Tetrahedron.
     */
    public static int TETRAHEDRON_VERTICES = 4;
    
    
    //Constructors
    
    /**
     * The constructor for an Tetrahedron.
     *
     * @param parent The parent of the Tetrahedron.
     * @param center The center point of the Tetrahedron.
     * @param color  The color of the Tetrahedron.
     * @param radius The radius of the bounding sphere of the Tetrahedron.
     */
    public Tetrahedron(AbstractObject parent, Vector center, Color color, double radius) {
        super(parent, center, color, TETRAHEDRON_FACES, TETRAHEDRON_VERTICES, radius);
    }
    
    /**
     * The constructor for an Tetrahedron.
     *
     * @param parent The parent of the Tetrahedron.
     * @param center The center point of the Tetrahedron.
     * @param radius The radius of the bounding sphere of the Tetrahedron.
     */
    public Tetrahedron(AbstractObject parent, Vector center, double radius) {
        this(parent, center, Color.BLACK, radius);
    }
    
    /**
     * The constructor for an Tetrahedron.
     *
     * @param center The center point of the Tetrahedron.
     * @param color  The color of the Tetrahedron.
     * @param radius The radius of the bounding sphere of the Tetrahedron.
     */
    public Tetrahedron(Vector center, Color color, double radius) {
        this(null, center, color, radius);
    }
    
    
    //Methods
    
    /**
     * Calculates the structure of the Tetrahedron.
     */
    @Override
    protected void calculate() {
        components.clear();
        
        vertices = new Vector[TETRAHEDRON_VERTICES];
        int v = 0;
        for (int i : new int[] {-1, 1}) {
            for (int j : new int[] {-1, 1}) {
                vertices[v++] = new Vector(i, j, (i == j) ? 1 : -1).scale(radius).plus(center);
            }
        }
        
        new Triangle(this, color,
                vertices[2],
                vertices[0],
                vertices[1]
        );
        new Triangle(this, color,
                vertices[0],
                vertices[3],
                vertices[1]
        );
        new Triangle(this, color,
                vertices[0],
                vertices[2],
                vertices[3]
        );
        new Triangle(this, color,
                vertices[2],
                vertices[1],
                vertices[3]
        );
        
        setVisible(visible);
    }
    
}
