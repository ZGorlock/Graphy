/*
 * File:    MetatronsCube.java
 * Package: graphy.objects.polyhedron.regular
 * Author:  Zachary Gill
 */

package graphy.object.polyhedron.regular;

import java.awt.Color;

import graphy.math.vector.Vector;
import graphy.object.base.Object;
import graphy.object.polyhedron.regular.platonic.Dodecahedron;
import graphy.object.polyhedron.regular.platonic.Hexahedron;
import graphy.object.polyhedron.regular.platonic.Icosahedron;
import graphy.object.polyhedron.regular.platonic.Octahedron;
import graphy.object.polyhedron.regular.platonic.Tetrahedron;

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
