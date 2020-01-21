/*
 * File:    Space.java
 * Package: math.physics
 * Author:  Zachary Gill
 */

package math.physics;

import java.util.ArrayList;
import java.util.List;

import math.vector.Vector;
import math.vector.Vector3;
import objects.base.ObjectInterface;
import objects.base.polygon.Polygon;
import objects.polyhedron.Polyhedron;

/**
 * Includes functions for Space.
 */
public final class Space {
    
    //Functions
    
    /**
     * Determines if a point is inside a Polyhedron.
     *
     * @param polyhedron The Polyhedron.
     * @param point      The point.
     * @return Whether the point is inside the Polyhedron or not.
     */
    public static boolean pointInsidePolyhedron(Polyhedron polyhedron, Vector point) {
        List<Polygon> faces = new ArrayList<>();
        for (ObjectInterface object : polyhedron.getComponents()) {
            if (object instanceof Polygon) {
                faces.add((Polygon) object);
            }
        }
        
        Vector travel = new Vector(Math.random(), Math.random(), Math.random());
        int intersections = 0;
        for (Polygon face : faces) {
            Vector v1 = face.getVertex(2).minus(face.getVertex(1));
            Vector v2 = face.getVertex(3).minus(face.getVertex(1));
            Vector n = new Vector3(v1).cross(v2);
            
            double t = Plane.lineScalarForPointOnPlane(face.getCenter(), n, point, travel);
            if (t >= 0 && t != Double.MAX_VALUE) {
                if (Plane.pointInsidePolygon(face, Plane.lineIntersection(face.getCenter(), n, point, travel))) {
                    intersections++;
                }
            }
        }
        
        return (intersections % 2) == 1;
    }
    
}
