/*
 * File:    SphericalCoordinateUtility.java
 * Package: graphy.utility
 * Author:  Zachary Gill
 */

package graphy.utility;

import graphy.math.vector.Vector;

/**
 * Handles spherical coordinate conversions.
 */
public final class SphericalCoordinateUtility {
    
    //Functions
    
    /**
     * Converts spherical coordinates to a vector in cartesian coordinates.
     *
     * @param rho   The rho distance.
     * @param theta The theta angle.
     * @param phi   The phi angle.
     * @return The vector in cartesian coordinates.
     */
    public static Vector sphericalToCartesian(double rho, double theta, double phi) {
        double xCartesian = rho * Math.cos(theta) * Math.sin(phi);
        double yCartesian = rho * Math.sin(theta) * Math.sin(phi);
        double zCartesian = rho * Math.cos(phi);
        
        return new Vector(xCartesian, yCartesian, zCartesian);
    }
    
    /**
     * Converts spherical coordinates to a vector in cartesian coordinates.
     *
     * @param spherical The spherical vector.
     * @return The vector in cartesian coordinates.
     *
     * @see #sphericalToCartesian(double, double, double)
     */
    public static Vector sphericalToCartesian(Vector spherical) {
        return sphericalToCartesian(spherical.getX(), spherical.getY(), spherical.getZ());
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
        double rhoSpherical = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
        double thetaSpherical = (Math.atan2(y, x) + (2 * Math.PI)) % (2 * Math.PI);
        double phiSpherical = (rhoSpherical != 0) ? Math.acos(z / rhoSpherical) : 0;
        
        return new Vector(rhoSpherical, thetaSpherical, phiSpherical);
    }
    
    /**
     * Converts cartesian coordinates to a vector in spherical coordinates.
     *
     * @param cartesian The cartesian vector.
     * @return The vector in spherical coordinates.
     *
     * @see #cartesianToSpherical(double, double, double)
     */
    public static Vector cartesianToSpherical(Vector cartesian) {
        return cartesianToSpherical(cartesian.getX(), cartesian.getY(), cartesian.getZ());
    }
    
}
