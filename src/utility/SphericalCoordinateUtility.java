/*
 * File:    SphericalCoordinateUtility.java
 * Package: utility
 * Author:  Zachary Gill
 */

package utility;

import math.vector.Vector;

/**
 * Handles spherical coordinate conversions.
 */
public final class SphericalCoordinateUtility {
    
    //Functions
    
    /**
     * Converts spherical coordinates to a vector in cartesian coordinates.
     *
     * @param phi   The phi angle.
     * @param theta The theta angle.
     * @param rho   The rho distance.
     * @return The vector in cartesian coordinates.
     */
    public static Vector sphericalToCartesian(double phi, double theta, double rho) {
        double mx = rho * Math.sin(phi) * Math.cos(theta);
        double my = rho * Math.sin(phi) * Math.sin(theta);
        double mz = rho * Math.cos(phi);
        return new Vector(mx, my, mz);
    }
        
}
