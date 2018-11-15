/*
 * File:    SphericalCoordinateUtility.java
 * Package: utility
 * Author:  Zachary Gill
 */

package utility;

import main.Environment;
import math.vector.Vector;

/**
 * Handles spherical coordinate conversions.
 */
public final class SphericalCoordinateUtility {
    
    //Functions
    
    /**
     * Converts spherical coordinates to a vector in cartesian coordinates.
     *
     * @param spherical The spherical vector.
     * @return The vector in cartesian coordinates.
     */
    public static Vector sphericalToCartesian(Vector spherical) {
        return sphericalToCartesian(spherical.getX(), spherical.getY(), spherical.getZ());
    }
    
    /**
     * Converts spherical coordinates to a vector in cartesian coordinates.
     *
     * @param phi   The phi angle.
     * @param theta The theta angle.
     * @param rho   The rho distance.
     * @return The vector in cartesian coordinates.
     */
    public static Vector sphericalToCartesian(double phi, double theta, double rho) {
        double x = rho * Math.sin(phi) * Math.cos(theta);
        double y = rho * Math.sin(phi) * Math.sin(theta);
        double z = rho * Math.cos(phi);
        return new Vector(x, y, z);
    }
    
    /**
     * Converts cartesian coordinates to a vector in spherical coordinates.
     *
     * @param cartesian The cartesian vector.
     * @return The vector in spherical coordinates.
     */
    public static Vector cartesianToSpherical(Vector cartesian) {
        return cartesianToSpherical(cartesian.getX(), cartesian.getY(), cartesian.getZ());
    }
    
    /**
     * Converts cartesian coordinates to a vector in spherical coordinates.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param z The z coordinate.
     * @return The vector in spherical coordinates.
     */
    public static Vector cartesianToSpherical(double x, double y, double z) {
        double rho = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
        double phi = Math.acos(z / rho);
        double theta = Math.atan2(y, x + ((Math.abs(x - y) < Environment.omega) ? Environment.omega : 0));
        return new Vector(phi, theta, rho);
    }
    
}
