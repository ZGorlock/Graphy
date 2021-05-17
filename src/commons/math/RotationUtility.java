/*
 * File:    RotationUtility.java
 * Package: commons.math
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math;

import commons.math.component.matrix.Matrix;
import commons.math.component.matrix.Matrix3;
import commons.math.component.vector.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles rotations operations.
 */
public final class RotationUtility {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(RotationUtility.class);
    
    
    //Functions
    
    /**
     * Calculates a rotation transformation matrix.
     *
     * @param roll  The roll angle to rotate by.
     * @param pitch The pitch angle to rotate by.
     * @param yaw   The yaw angle to rotate by.
     * @return The rotation transformation matrix.
     */
    public static Matrix3 getRotationMatrix(double roll, double pitch, double yaw) {
        Matrix rollRotation = new Matrix3(
                1, 0, 0,
                0, Math.cos(roll), -Math.sin(roll),
                0, Math.sin(roll), Math.cos(roll)
        );
        Matrix pitchRotation = new Matrix3(
                Math.cos(pitch), 0, Math.sin(pitch),
                0, 1, 0,
                -Math.sin(pitch), 0, Math.cos(pitch)
        );
        Matrix yawRotation = new Matrix3(
                Math.cos(yaw), -Math.sin(yaw), 0,
                Math.sin(yaw), Math.cos(yaw), 0,
                0, 0, 1
        );
        return (Matrix3) rollRotation.times(pitchRotation).times(yawRotation);
    }
    
    /**
     * Performs a rotation transformation on a Vector.
     *
     * @param vector         The Vector to rotate.
     * @param rotationMatrix The rotation transformation matrix to apply.
     * @param center         The center point to rotate about.
     * @return The rotated Vector.
     */
    public static Vector performRotation(Vector vector, Matrix3 rotationMatrix, Vector center) {
        return rotationMatrix.transform(vector.minus(center)).plus(center);
    }
    
}
