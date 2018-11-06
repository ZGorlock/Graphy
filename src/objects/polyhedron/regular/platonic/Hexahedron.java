/*
 * File:    Hexahedron.java
 * Package: objects.polyhedron.regular.platonic
 * Author:  Zachary Gill
 */

package objects.polyhedron.regular.platonic;

import math.vector.Vector;
import objects.base.AbstractObject;
import objects.base.polygon.Rectangle;
import objects.polyhedron.regular.RegularPolyhedron;

import java.awt.*;

/**
 * Defines a Hexahedron.
 */
public class Hexahedron extends RegularPolyhedron {
    
    //Constants
    
    /**
     * The number of faces of a Hexahedron.
     */
    public static int HEXAHEDRON_FACES = 6;
    
    /**
     * The number of vertices of a Hexahedron.
     */
    public static int HEXAHEDRON_VERTICES = 8;
    
    
    //Constructors
    
    /**
     * The constructor for a Hexahedron.
     *
     * @param parent The parent of the Hexahedron.
     * @param center The center point of the Hexahedron.
     * @param color  The color of the Hexahedron.
     * @param radius The radius of the bounding sphere of the Hexahedron.
     */
    public Hexahedron(AbstractObject parent, Vector center, Color color, double radius) {
        super(parent, center, color, HEXAHEDRON_FACES, HEXAHEDRON_VERTICES, radius);
    }
    
    /**
     * The constructor for a Hexahedron.
     *
     * @param parent The parent of the Hexahedron.
     * @param center The center point of the Hexahedron.
     * @param radius The radius of the bounding sphere of the Hexahedron.
     */
    public Hexahedron(AbstractObject parent, Vector center, double radius) {
        this(parent, center, Color.BLACK, radius);
    }
    
    /**
     * The constructor for a Hexahedron.
     *
     * @param center The center point of the Hexahedron.
     * @param color  The color of the Hexahedron.
     * @param radius The radius of the bounding sphere of the Hexahedron.
     */
    public Hexahedron(Vector center, Color color, double radius) {
        this(null, center, color, radius);
    }
    
    
    //Methods
    
    /**
     * Calculates the structure of the Hexahedron.
     */
    @Override
    protected void calculate() {
        components.clear();
        
        vertices = new Vector[HEXAHEDRON_VERTICES];
        int v = 0;
        for (int i : new int[]{-1, 1}) {
            for (int j : new int[]{-1, 1}) {
                for (int k : new int[]{-1, 1}) {
                    vertices[v++] = new Vector(i, j, k).scale(radius).plus(center);
                }
            }
        }
        
        new Rectangle(this, color,
                vertices[0],
                vertices[1],
                vertices[3],
                vertices[2]
        );
        new Rectangle(this, color,
                vertices[1],
                vertices[5],
                vertices[7],
                vertices[3]
        );
        new Rectangle(this, color,
                vertices[5],
                vertices[4],
                vertices[6],
                vertices[7]
        );
        new Rectangle(this, color,
                vertices[4],
                vertices[0],
                vertices[2],
                vertices[6]
        );
        new Rectangle(this, color,
                vertices[4],
                vertices[5],
                vertices[1],
                vertices[0]
        );
        new Rectangle(this, color,
                vertices[2],
                vertices[3],
                vertices[7],
                vertices[6]
        );
        
        setVisible(visible);
    }
    
}
