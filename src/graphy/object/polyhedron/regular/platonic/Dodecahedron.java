/*
 * File:    Dodecahedron.java
 * Package: graphy.objects.polyhedron.regular.platonic
 * Author:  Zachary Gill
 */

package graphy.object.polyhedron.regular.platonic;

import java.awt.Color;

import commons.math.vector.Vector;
import graphy.object.base.AbstractObject;
import graphy.object.base.polygon.Pentagon;
import graphy.object.polyhedron.regular.RegularPolyhedron;

/**
 * Defines a Dodecahedron.
 */
public class Dodecahedron extends RegularPolyhedron {
    
    //Constants
    
    /**
     * The number of faces of a Dodecahedron.
     */
    public static int DODECAHEDRON_FACES = 12;
    
    /**
     * The number of vertices of a Dodecahedron.
     */
    public static int DODECAHEDRON_VERTICES = 20;
    
    
    //Constructors
    
    /**
     * The constructor for a Dodecahedron.
     *
     * @param parent The parent of the Dodecahedron.
     * @param center The center point of the Dodecahedron.
     * @param color  The color of the Dodecahedron.
     * @param radius The radius of the bounding sphere of the Dodecahedron.
     */
    public Dodecahedron(AbstractObject parent, Vector center, Color color, double radius) {
        super(parent, center, color, DODECAHEDRON_FACES, DODECAHEDRON_VERTICES, radius);
    }
    
    /**
     * The constructor for a Dodecahedron.
     *
     * @param parent The parent of the Dodecahedron.
     * @param center The center point of the Dodecahedron.
     * @param radius The radius of the bounding sphere of the Dodecahedron.
     */
    public Dodecahedron(AbstractObject parent, Vector center, double radius) {
        this(parent, center, Color.BLACK, radius);
    }
    
    /**
     * The constructor for a Dodecahedron.
     *
     * @param center The center point of the Dodecahedron.
     * @param color  The color of the Dodecahedron.
     * @param radius The radius of the bounding sphere of the Dodecahedron.
     */
    public Dodecahedron(Vector center, Color color, double radius) {
        this(null, center, color, radius);
    }
    
    
    //Methods
    
    /**
     * Calculates the structure of the Dodecahedron.
     */
    @Override
    protected void calculate() {
        components.clear();
        
        double a = GOLDEN_RATIO;
        double b = 1 + a;
        
        vertices = new Vector[DODECAHEDRON_VERTICES];
        int v = 0;
        for (int i : new int[] {-1, 1}) {
            for (int j : new int[] {-1, 1}) {
                vertices[v++] = new Vector(0, a * i, b * j).scale(radius).plus(center);
                vertices[v++] = new Vector(a * i, b * j, 0).scale(radius).plus(center);
                vertices[v++] = new Vector(b * i, 0, a * j).scale(radius).plus(center);
                
                for (int k : new int[] {-1, 1}) {
                    vertices[v++] = new Vector(i, j, k).scale(radius).plus(center);
                }
            }
        }
        
        new Pentagon(this, color,
                vertices[1],
                vertices[4],
                vertices[7],
                vertices[2],
                vertices[3]
        );
        new Pentagon(this, color,
                vertices[5],
                vertices[4],
                vertices[1],
                vertices[11],
                vertices[14]
        );
        new Pentagon(this, color,
                vertices[11],
                vertices[13],
                vertices[12],
                vertices[17],
                vertices[14]
        );
        new Pentagon(this, color,
                vertices[0],
                vertices[13],
                vertices[11],
                vertices[1],
                vertices[3]
        );
        new Pentagon(this, color,
                vertices[4],
                vertices[5],
                vertices[15],
                vertices[9],
                vertices[7]
        );
        new Pentagon(this, color,
                vertices[14],
                vertices[17],
                vertices[19],
                vertices[15],
                vertices[5]
        );
        new Pentagon(this, color,
                vertices[13],
                vertices[0],
                vertices[10],
                vertices[18],
                vertices[12]
        );
        new Pentagon(this, color,
                vertices[3],
                vertices[2],
                vertices[8],
                vertices[10],
                vertices[0]
        );
        new Pentagon(this, color,
                vertices[6],
                vertices[8],
                vertices[2],
                vertices[7],
                vertices[9]
        );
        new Pentagon(this, color,
                vertices[15],
                vertices[19],
                vertices[16],
                vertices[6],
                vertices[9]
        );
        new Pentagon(this, color,
                vertices[16],
                vertices[19],
                vertices[17],
                vertices[12],
                vertices[18]
        );
        new Pentagon(this, color,
                vertices[10],
                vertices[18],
                vertices[16],
                vertices[6],
                vertices[8]
        );
        
        setVisible(visible);
    }
    
}