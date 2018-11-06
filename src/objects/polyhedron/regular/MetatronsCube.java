/*
 * File:    MetatronsCube.java
 * Package: objects.polyhedron.regular
 * Author:  Zachary Gill
 */

package objects.polyhedron.regular;

import math.vector.Vector;
import objects.base.Object;
import objects.polyhedron.regular.platonic.*;

import java.awt.*;

/**
 * Defines a Metatron's Cube.
 */
public class MetatronsCube extends Object {
    
    //Constructors
    
    /**
     * Constructor for a Metatron's Cube.
     *
     * @param center            The center of the Object.
     * @param radius            The radius of the bounding sphere of the Object.
     * @param octahedronColor   The color of the Octahedron.
     * @param tetrahedronColor  The color of the Tetrahedron.
     * @param hexahedronColor   The color of the Hexahedron.
     * @param dodecahedronColor The color of the Dodecahedron.
     * @param icosahedronColor  The color of the Icosahedron.
     */
    public MetatronsCube(Vector center, double radius, Color octahedronColor, Color tetrahedronColor, Color hexahedronColor, Color dodecahedronColor, Color icosahedronColor) {
        super(center, Color.BLACK);
        
        new Octahedron(this, center, octahedronColor, radius);
        new Tetrahedron(this, center, tetrahedronColor, radius);
        new Hexahedron(this, center, hexahedronColor, radius);
        new Dodecahedron(this, center, dodecahedronColor, radius);
        new Icosahedron(this, center, icosahedronColor, radius);
    }
    
}
