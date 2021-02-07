/*
 * File:    Icosahedron.java
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
 * Defines an Icosahedron.
 */
public class Icosahedron extends RegularPolyhedron {
    
    //Constants
    
    /**
     * The number of faces of a Icosahedron.
     */
    public static int ICOSAHEDRON_FACES = 20;
    
    /**
     * The number of vertices of a Icosahedron.
     */
    public static int ICOSAHEDRON_VERTICES = 12;
    
    
    //Constructors
    
    /**
     * The constructor for a Icosahedron.
     *
     * @param parent The parent of the Icosahedron.
     * @param center The center point of the Icosahedron.
     * @param color  The color of the Icosahedron.
     * @param radius The radius of the bounding sphere of the Icosahedron.
     */
    public Icosahedron(AbstractObject parent, Vector center, Color color, double radius) {
        super(parent, center, color, ICOSAHEDRON_FACES, ICOSAHEDRON_VERTICES, radius);
    }
    
    /**
     * The constructor for a Icosahedron.
     *
     * @param parent The parent of the Icosahedron.
     * @param center The center point of the Icosahedron.
     * @param radius The radius of the bounding sphere of the Icosahedron.
     */
    public Icosahedron(AbstractObject parent, Vector center, double radius) {
        this(parent, center, Color.BLACK, radius);
    }
    
    /**
     * The constructor for a Icosahedron.
     *
     * @param center The center point of the Icosahedron.
     * @param color  The color of the Icosahedron.
     * @param radius The radius of the bounding sphere of the Icosahedron.
     */
    public Icosahedron(Vector center, Color color, double radius) {
        this(null, center, color, radius);
    }
    
    
    //Methods
    
    /**
     * Calculates the structure of the Icosahedron.
     */
    @Override
    protected void calculate() {
        components.clear();
        
        double b = 1 + GOLDEN_RATIO;
        
        vertices = new Vector[ICOSAHEDRON_VERTICES];
        int v = 0;
        for (int i : new int[] {-1, 1}) {
            for (int j : new int[] {-1, 1}) {
                vertices[v++] = new Vector(0, i, b * j).scale(radius).plus(center);
                vertices[v++] = new Vector(i, b * j, 0).scale(radius).plus(center);
                vertices[v++] = new Vector(b * i, 0, j).scale(radius).plus(center);
            }
        }
        
        new Triangle(this, color,
                vertices[1],
                vertices[5],
                vertices[2]
        );
        new Triangle(this, color,
                vertices[1],
                vertices[3],
                vertices[5]
        );
        new Triangle(this, color,
                vertices[7],
                vertices[11],
                vertices[3]
        );
        new Triangle(this, color,
                vertices[7],
                vertices[8],
                vertices[11]
        );
        new Triangle(this, color,
                vertices[7],
                vertices[0],
                vertices[8]
        );
        new Triangle(this, color,
                vertices[1],
                vertices[2],
                vertices[0]
        );
        new Triangle(this, color,
                vertices[3],
                vertices[1],
                vertices[7]
        );
        new Triangle(this, color,
                vertices[0],
                vertices[7],
                vertices[1]
        );
        new Triangle(this, color,
                vertices[9],
                vertices[5],
                vertices[3]
        );
        new Triangle(this, color,
                vertices[9],
                vertices[3],
                vertices[11]
        );
        new Triangle(this, color,
                vertices[6],
                vertices[8],
                vertices[0]
        );
        new Triangle(this, color,
                vertices[6],
                vertices[0],
                vertices[2]
        );
        new Triangle(this, color,
                vertices[4],
                vertices[2],
                vertices[5]
        );
        new Triangle(this, color,
                vertices[4],
                vertices[5],
                vertices[9]
        );
        new Triangle(this, color,
                vertices[10],
                vertices[9],
                vertices[11]
        );
        new Triangle(this, color,
                vertices[10],
                vertices[11],
                vertices[8]
        );
        new Triangle(this, color,
                vertices[10],
                vertices[8],
                vertices[6]
        );
        new Triangle(this, color,
                vertices[4],
                vertices[6],
                vertices[2]
        );
        new Triangle(this, color,
                vertices[6],
                vertices[4],
                vertices[10]
        );
        new Triangle(this, color,
                vertices[9],
                vertices[10],
                vertices[4]
        );
        
        setVisible(visible);
    }
    
}