/*
 * File:    CoordinateUtility.java
 * Package: commons.math
 * Author:  Zachary Gill
 */

package commons.math;

import commons.math.vector.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles spherical coordinate conversions.
 */
@SuppressWarnings("UnnecessaryLocalVariable")
public final class CoordinateUtility {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(CoordinateUtility.class);
    
    
    //Enums
    
    /**
     * An enumeration of supported Coordinate Systems.
     */
    public enum CoordinateSystem {
        
        //Values
        
        CARTESIAN,
        //x             y               [z]
        //horizontal    vertical        height
        //[-∞, ∞]      [-∞, ∞]        [-∞, ∞]
        
        SPHERICAL,
        //rho           theta           phi
        //radius        azimuth angle   polar angle
        //[0, ∞]       [0, 2π)         [0, π]
        
        CYLINDRICAL,
        //rho           theta           z
        //radius        azimuth angle   height
        //[0, ∞]       [0, 2π)         [-∞, ∞]
        
        POLAR
        //rho           theta
        //radius        azimuth angle
        //[0, ∞]       [0, 2π)
        
    }
    
    
    //Functions
    
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
     * @param cartesian The cartesian coordinate vector.
     * @return The vector in spherical coordinates.
     * @see #cartesianToSpherical(double, double, double)
     */
    public static Vector cartesianToSpherical(Vector cartesian) {
        return cartesianToSpherical(cartesian.getX(), cartesian.getY(), cartesian.getZ());
    }
    
    /**
     * Converts cartesian coordinates to a vector in cylindrical coordinates.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param z The z coordinate.
     * @return The vector in cylindrical coordinates.
     */
    public static Vector cartesianToCylindrical(double x, double y, double z) {
        double rCylindrical = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        double thetaCylindrical = (Math.atan2(y, x) + (2 * Math.PI)) % (2 * Math.PI);
        double zCylindrical = z;
        
        return new Vector(rCylindrical, thetaCylindrical, zCylindrical);
    }
    
    /**
     * Converts cartesian coordinates to a vector in cylindrical coordinates.
     *
     * @param cartesian The cartesian coordinate vector.
     * @return The vector in cylindrical coordinates.
     * @see #cartesianToCylindrical(double, double, double)
     */
    public static Vector cartesianToCylindrical(Vector cartesian) {
        return cartesianToCylindrical(cartesian.getX(), cartesian.getY(), cartesian.getZ());
    }
    
    /**
     * Converts cartesian coordinates to a vector in polar coordinates.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The vector in polar coordinates.
     */
    public static Vector cartesianToPolar(double x, double y) {
        double rPolar = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        double thetaPolar = (Math.atan2(y, x) + (2 * Math.PI)) % (2 * Math.PI);
        
        return new Vector(rPolar, thetaPolar);
    }
    
    /**
     * Converts cartesian coordinates to a vector in polar coordinates.
     *
     * @param cartesian The cartesian coordinate vector.
     * @return The vector in polar coordinates.
     * @see #cartesianToPolar(double, double)
     */
    public static Vector cartesianToPolar(Vector cartesian) {
        return cartesianToPolar(cartesian.getX(), cartesian.getY());
    }
    
    /**
     * Converts spherical coordinates to a vector in cartesian coordinates.
     *
     * @param rho   The radial distance.
     * @param theta The polar angle.
     * @param phi   The azimuth angle.
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
     * @param spherical The spherical coordinate vector.
     * @return The vector in cartesian coordinates.
     * @see #sphericalToCartesian(double, double, double)
     */
    public static Vector sphericalToCartesian(Vector spherical) {
        return sphericalToCartesian(spherical.getX(), spherical.getY(), spherical.getZ());
    }
    
    /**
     * Converts spherical coordinates to a vector in cylindrical coordinates.
     *
     * @param rho   The radial distance.
     * @param theta The polar angle.
     * @param phi   The azimuth angle.
     * @return The vector in cylindrical coordinates.
     */
    public static Vector sphericalToCylindrical(double rho, double theta, double phi) {
        double rCylindrical = Math.abs(rho * Math.sin(phi));
        double thetaCylindrical = (((theta + (((rho * Math.sin(phi)) < 0) ? Math.PI : 0)) % (2 * Math.PI)) + (2 * Math.PI)) % (2 * Math.PI);
        double zCylindrical = rho * Math.cos(phi);
        
        return new Vector(rCylindrical, thetaCylindrical, zCylindrical);
    }
    
    /**
     * Converts spherical coordinates to a vector in cylindrical coordinates.
     *
     * @param spherical The spherical coordinate vector.
     * @return The vector in cylindrical coordinates.
     * @see #sphericalToCylindrical(double, double, double)
     */
    public static Vector sphericalToCylindrical(Vector spherical) {
        return sphericalToCylindrical(spherical.getX(), spherical.getY(), spherical.getZ());
    }
    
    /**
     * Converts spherical coordinates to a vector in polar coordinates.
     *
     * @param rho   The radial distance.
     * @param theta The polar angle.
     * @param phi   The azimuth angle.
     * @return The vector in polar coordinates.
     */
    public static Vector sphericalToPolar(double rho, double theta, double phi) {
        double rPolar = Math.abs(rho * Math.sin(phi));
        double thetaPolar = (((theta + (((rho * Math.sin(phi)) < 0) ? Math.PI : 0)) % (2 * Math.PI)) + (2 * Math.PI)) % (2 * Math.PI);
        
        return new Vector(rPolar, thetaPolar);
    }
    
    /**
     * Converts spherical coordinates to a vector in polar coordinates.
     *
     * @param spherical The spherical coordinate vector.
     * @return The vector in polar coordinates.
     * @see #sphericalToPolar(double, double, double)
     */
    public static Vector sphericalToPolar(Vector spherical) {
        return sphericalToPolar(spherical.getX(), spherical.getY(), spherical.getZ());
    }
    
    /**
     * Converts cylindrical coordinates to a vector in cartesian coordinates.
     *
     * @param r     The radial distance.
     * @param theta The polar angle.
     * @param z     The z coordinate.
     * @return The vector in cartesian coordinates.
     */
    public static Vector cylindricalToCartesian(double r, double theta, double z) {
        double xCartesian = r * Math.cos(theta);
        double yCartesian = r * Math.sin(theta);
        double zCartesian = z;
        
        return new Vector(xCartesian, yCartesian, zCartesian);
    }
    
    /**
     * Converts cylindrical coordinates to a vector in cartesian coordinates.
     *
     * @param cylindrical The cylindrical coordinate vector.
     * @return The vector in cartesian coordinates.
     * @see #cylindricalToCartesian(double, double, double)
     */
    public static Vector cylindricalToCartesian(Vector cylindrical) {
        return cylindricalToCartesian(cylindrical.getX(), cylindrical.getY(), cylindrical.getZ());
    }
    
    /**
     * Converts cylindrical coordinates to a vector in spherical coordinates.
     *
     * @param r     The radial distance.
     * @param theta The polar angle.
     * @param z     The z coordinate.
     * @return The vector in spherical coordinates.
     */
    public static Vector cylindricalToSpherical(double r, double theta, double z) {
        double rhoSpherical = Math.sqrt(Math.pow(r, 2) + Math.pow(z, 2));
        double thetaSpherical = ((theta % (2 * Math.PI)) + (2 * Math.PI)) % (2 * Math.PI);
        double phiSpherical = (rhoSpherical != 0) ? Math.acos(z / rhoSpherical) : 0;
        
        return new Vector(rhoSpherical, thetaSpherical, phiSpherical);
    }
    
    /**
     * Converts cylindrical coordinates to a vector in spherical coordinates.
     *
     * @param cylindrical The cylindrical coordinate vector.
     * @return The vector in spherical coordinates.
     * @see #cylindricalToSpherical(double, double, double)
     */
    public static Vector cylindricalToSpherical(Vector cylindrical) {
        return cylindricalToSpherical(cylindrical.getX(), cylindrical.getY(), cylindrical.getZ());
    }
    
    /**
     * Converts cylindrical coordinates to a vector in polar coordinates.
     *
     * @param r     The radial distance.
     * @param theta The polar angle.
     * @param z     The z coordinate.
     * @return The vector in polar coordinates.
     */
    public static Vector cylindricalToPolar(double r, double theta, double z) {
        double rPolar = Math.abs(r);
        double thetaPolar = (((theta + ((r < 0) ? Math.PI : 0)) % (2 * Math.PI)) + (2 * Math.PI)) % (2 * Math.PI);
        
        return new Vector(rPolar, thetaPolar);
    }
    
    /**
     * Converts cylindrical coordinates to a vector in polar coordinates.
     *
     * @param cylindrical The cylindrical coordinate vector.
     * @return The vector in polar coordinates.
     * @see #cylindricalToPolar(double, double, double)
     */
    public static Vector cylindricalToPolar(Vector cylindrical) {
        return cylindricalToPolar(cylindrical.getX(), cylindrical.getY(), cylindrical.getZ());
    }
    
    /**
     * Converts polar coordinates to a vector in cartesian coordinates.
     *
     * @param r     The radial distance.
     * @param theta The polar angle.
     * @return The vector in cartesian coordinates.
     */
    public static Vector polarToCartesian(double r, double theta) {
        double xCartesian = r * Math.cos(theta);
        double yCartesian = r * Math.sin(theta);
        
        return new Vector(xCartesian, yCartesian);
    }
    
    /**
     * Converts polar coordinates to a vector in cartesian coordinates.
     *
     * @param polar The polar coordinate vector.
     * @return The vector in cartesian coordinates.
     * @see #polarToCartesian(double, double)
     */
    public static Vector polarToCartesian(Vector polar) {
        return polarToCartesian(polar.getX(), polar.getY());
    }
    
    /**
     * Converts polar coordinates to a vector in spherical coordinates.
     *
     * @param r     The radial distance.
     * @param theta The polar angle.
     * @return The vector in spherical coordinates.
     */
    public static Vector polarToSpherical(double r, double theta) {
        double rhoSpherical = Math.abs(r);
        double thetaSpherical = (((theta + ((r < 0) ? Math.PI : 0)) % (2 * Math.PI)) + (2 * Math.PI)) % (2 * Math.PI);
        double phiSpherical = Math.PI / 2;
        
        return new Vector(rhoSpherical, thetaSpherical, phiSpherical);
    }
    
    /**
     * Converts polar coordinates to a vector in spherical coordinates.
     *
     * @param polar The polar coordinate vector.
     * @return The vector in spherical coordinates.
     * @see #polarToSpherical(double, double)
     */
    public static Vector polarToSpherical(Vector polar) {
        return polarToSpherical(polar.getX(), polar.getY());
    }
    
    /**
     * Converts polar coordinates to a vector in cylindrical coordinates.
     *
     * @param r     The radial distance.
     * @param theta The polar angle.
     * @return The vector in cylindrical coordinates.
     */
    public static Vector polarToCylindrical(double r, double theta) {
        double rCylindrical = Math.abs(r);
        double thetaCylindrical = (((theta + ((r < 0) ? Math.PI : 0)) % (2 * Math.PI)) + (2 * Math.PI)) % (2 * Math.PI);
        double zCylindrical = 0;
        
        return new Vector(rCylindrical, thetaCylindrical, zCylindrical);
    }
    
    /**
     * Converts polar coordinates to a vector in cylindrical coordinates.
     *
     * @param polar The polar coordinate vector.
     * @return The vector in cylindrical coordinates.
     * @see #polarToCylindrical(double, double)
     */
    public static Vector polarToCylindrical(Vector polar) {
        return polarToCylindrical(polar.getX(), polar.getY());
    }
    
    /**
     * Converts coordinates from one coordinate system to another.
     *
     * @param from        The Coordinate System of the original coordinates.
     * @param to          The Coordinate System to convert to.
     * @param coordinates The coordinate vector.
     * @return The vector in the specified coordinate system.
     */
    public static Vector convert(CoordinateSystem from, CoordinateSystem to, Vector coordinates) {
        switch (from) {
            case CARTESIAN:
                switch (to) {
                    case CARTESIAN:
                        return coordinates;
                    case SPHERICAL:
                        return cartesianToSpherical(coordinates);
                    case CYLINDRICAL:
                        return cartesianToCylindrical(coordinates);
                    case POLAR:
                        return cartesianToPolar(coordinates);
                    default:
                        return null;
                }
            case SPHERICAL:
                switch (to) {
                    case CARTESIAN:
                        return sphericalToCartesian(coordinates);
                    case SPHERICAL:
                        return coordinates;
                    case CYLINDRICAL:
                        return sphericalToCylindrical(coordinates);
                    case POLAR:
                        return sphericalToPolar(coordinates);
                    default:
                        return null;
                }
            case CYLINDRICAL:
                switch (to) {
                    case CARTESIAN:
                        return cylindricalToCartesian(coordinates);
                    case SPHERICAL:
                        return cylindricalToSpherical(coordinates);
                    case CYLINDRICAL:
                        return coordinates;
                    case POLAR:
                        return cylindricalToPolar(coordinates);
                    default:
                        return null;
                }
            case POLAR:
                switch (to) {
                    case CARTESIAN:
                        return polarToCartesian(coordinates);
                    case SPHERICAL:
                        return polarToSpherical(coordinates);
                    case CYLINDRICAL:
                        return polarToCylindrical(coordinates);
                    case POLAR:
                        return coordinates;
                    default:
                        return null;
                }
            default:
                return null;
        }
    }
    
    /**
     * Converts coordinates from one coordinate system to another.
     *
     * @param from        The Coordinate System of the original coordinates.
     * @param to          The Coordinate System to convert to.
     * @param coordinate1 The first coordinate.
     * @param coordinate2 The second coordinate.
     * @param coordinate3 The third coordinate.
     * @return The vector in the specified coordinate system.
     * @see #convert(CoordinateSystem, CoordinateSystem, Vector)
     */
    public static Vector convert(CoordinateSystem from, CoordinateSystem to, double coordinate1, double coordinate2, double coordinate3) {
        return convert(from, to, new Vector(coordinate1, coordinate2, coordinate3));
    }
    
    /**
     * Converts coordinates from one coordinate system to another.
     *
     * @param from        The Coordinate System of the original coordinates.
     * @param to          The Coordinate System to convert to.
     * @param coordinate1 The first coordinate.
     * @param coordinate2 The second coordinate.
     * @return The vector in the specified coordinate system.
     * @see #convert(CoordinateSystem, CoordinateSystem, double, double, double)
     */
    public static Vector convert(CoordinateSystem from, CoordinateSystem to, double coordinate1, double coordinate2) {
        return convert(from, to, coordinate1, coordinate2, 0);
    }
    
}
