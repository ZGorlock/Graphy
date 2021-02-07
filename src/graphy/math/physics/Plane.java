/*
 * File:    Plane.java
 * Package: graphy.math.physics
 * Author:  Zachary Gill
 */

package graphy.math.physics;

import graphy.main.Environment;
import graphy.math.vector.Vector;
import graphy.object.base.polygon.Polygon;

/**
 * Includes functions for Planes.
 */
public final class Plane {
    
    //Functions
    
    /**
     * Determines the point of intersection between a plane defined by a point and a normal vector and a line defined by a point and a direction vector.
     *
     * @param planePoint    A point on the plane.
     * @param planeNormal   The normal vector of the plane.
     * @param linePoint     A point on the line.
     * @param lineDirection The direction vector of the line.
     * @return The point of intersection between the line and the plane, null if the line is parallel to the plane.
     */
    public static Vector lineIntersection(Vector planePoint, Vector planeNormal, Vector linePoint, Vector lineDirection) {
        double t = lineScalarForPointOnPlane(planePoint, planeNormal, linePoint, lineDirection);
        if (t == Double.MAX_VALUE) {
            return null;
        }
        return linePoint.plus(lineDirection.normalize().scale(t));
    }
    
    /**
     * Determines the line scalar for the intersection between a plane defined by a point and a normal vector and a line defined by a point and a direction vector.
     *
     * @param planePoint    A point on the plane.
     * @param planeNormal   The normal vector of the plane.
     * @param linePoint     A point on the line.
     * @param lineDirection The direction vector of the line.
     * @return The line scalar for the intersection between the line and the plane, Double.MAX_VALUE if the line is parallel to the plane.
     */
    public static double lineScalarForPointOnPlane(Vector planePoint, Vector planeNormal, Vector linePoint, Vector lineDirection) {
        if (planeNormal.dot(lineDirection.normalize()) == 0) {
            return Double.MAX_VALUE;
        }
        return (planeNormal.dot(planePoint) - planeNormal.dot(linePoint)) / planeNormal.dot(lineDirection.normalize());
    }
    
    /**
     * Determines if a point is inside a Polygon.
     *
     * @param polygon The Polygon.
     * @param point   The point.
     * @return Whether the point is inside the Polygon or not.
     */
    public static boolean pointInsidePolygon(Polygon polygon, Vector point) {
        double angleSum = 0;
        int n = polygon.getVertices().length;
        for (int i = 0; i < n; i++) {
            Vector v1 = polygon.getVertex(i + 1).minus(point);
            Vector v2 = polygon.getVertex((i + 1) % n + 1).minus(point);
            double d1 = v1.hypotenuse();
            double d2 = v2.hypotenuse();
            if (d1 * d2 <= Environment.OMEGA) { //on a vertex
                return true;
            } else {
                angleSum += Math.acos(v1.dot(v2) / (d1 * d2));
            }
        }
        return Math.abs(angleSum - (Math.PI * 2)) <= Environment.OMEGA;
    }
    
}
