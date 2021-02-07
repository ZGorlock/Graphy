/*
 * File:    Octahedron.java
 * Package: graphy.objects.polyhedron.regular.platonic
 * Author:  Zachary Gill
 */

package graphy.object.polyhedron.regular.platonic;

import java.awt.Color;

import commons.math.vector.Vector;
import graphy.object.base.AbstractObject;
import graphy.object.base.polygon.Triangle;
import graphy.object.polyhedron.regular.RegularPolyhedron;

/**
 * Defines an Octahedron.
 */
public class Octahedron extends RegularPolyhedron {
    
    //Constants
    
    /**
     * The number of faces of an Octahedron.
     */
    public static int OCTAHEDRON_FACES = 8;
    
    /**
     * The number of vertices of an Octahedron.
     */
    public static int OCTAHEDRON_VERTICES = 6;
    
    
    //Constructors
    
    /**
     * The constructor for an Octahedron.
     *
     * @param parent The parent of the Octahedron.
     * @param center The center point of the Octahedron.
     * @param color  The color of the Octahedron.
     * @param radius The radius of the bounding sphere of the Octahedron.
     */
    public Octahedron(AbstractObject parent, Vector center, Color color, double radius) {
        super(parent, center, color, OCTAHEDRON_FACES, OCTAHEDRON_VERTICES, radius);
    }
    
    /**
     * The constructor for an Octahedron.
     *
     * @param parent The parent of the Octahedron.
     * @param center The center point of the Octahedron.
     * @param radius The radius of the bounding sphere of the Octahedron.
     */
    public Octahedron(AbstractObject parent, Vector center, double radius) {
        this(parent, center, Color.BLACK, radius);
    }
    
    /**
     * The constructor for an Octahedron.
     *
     * @param center The center point of the Octahedron.
     * @param color  The color of the Octahedron.
     * @param radius The radius of the bounding sphere of the Octahedron.
     */
    public Octahedron(Vector center, Color color, double radius) {
        this(null, center, color, radius);
    }
    
    
    //Methods
    
    /**
     * Calculates the structure of the Octahedron.
     */
    @Override
    protected void calculate() {
        components.clear();
        
        vertices = new Vector[OCTAHEDRON_VERTICES];
        int v = 0;
        for (int i : new int[] {-1, 1}) {
            vertices[v++] = new Vector(0, 0, i).scale(radius).plus(center);
            vertices[v++] = new Vector(0, i, 0).scale(radius).plus(center);
            vertices[v++] = new Vector(i, 0, 0).scale(radius).plus(center);
        }
        
        new Triangle(this, color,
                vertices[1],
                vertices[3],
                vertices[2]
        );
        new Triangle(this, color,
                vertices[1],
                vertices[5],
                vertices[3]
        );
        new Triangle(this, color,
                vertices[1],
                vertices[0],
                vertices[5]
        );
        new Triangle(this, color,
                vertices[1],
                vertices[2],
                vertices[0]
        );
        new Triangle(this, color,
                vertices[4],
                vertices[2],
                vertices[3]
        );
        new Triangle(this, color,
                vertices[4],
                vertices[3],
                vertices[5]
        );
        new Triangle(this, color,
                vertices[4],
                vertices[5],
                vertices[0]
        );
        new Triangle(this, color,
                vertices[4],
                vertices[0],
                vertices[2]
        );
        
        setVisible(visible);
    }
    
}