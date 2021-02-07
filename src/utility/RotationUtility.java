/*
 * File:    RotationUtility.java
 * Package: utility
 * Author:  Zachary Gill
 */

package utility;

import math.matrix.Matrix3;
import math.matrix.Matrix4;
import math.vector.Vector;

/**
 * Handles rotations operations.
 */
public final class RotationUtility {
    
    //Functions
    
    /**
     * Creates the rotation transformation matrix for an Object.
     *
     * @param roll  The roll angle to rotate by.
     * @param pitch The pitch angle to rotate by.
     * @param yaw   The yaw angle to rotate by.
     * @return The rotation transformation matrix.
     */
    public static Matrix3 getRotationMatrix(double roll, double pitch, double yaw) {
        Matrix3 rollRotation = new Matrix3(new double[] {
                1, 0, 0,
                0, Math.cos(roll), -Math.sin(roll),
                0, Math.sin(roll), Math.cos(roll)
        });
        Matrix3 pitchRotation = new Matrix3(new double[] {
                Math.cos(pitch), 0, Math.sin(pitch),
                0, 1, 0,
                -Math.sin(pitch), 0, Math.cos(pitch)
        });
        Matrix3 yawRotation = new Matrix3(new double[] {
                Math.cos(yaw), -Math.sin(yaw), 0,
                Math.sin(yaw), Math.cos(yaw), 0,
                0, 0, 1
        });
        return rollRotation.multiply(pitchRotation).multiply(yawRotation);
    }
    
    /**
     * Performs the rotation transformation on a Vector.
     *
     * @param vector         The Vector to rotate.
     * @param rotationMatrix The rotation transformation matrix to apply.
     * @param center         The center point to rotate about.
     * @return The rotated Vector.
     */
    public static Vector performRotation(Vector vector, Matrix3 rotationMatrix, Vector center) {
        Vector justifiedCenter = center.justify();
        
        Matrix4 translationMatrix = new Matrix4(new double[] {
                1, 0, 0, -justifiedCenter.getX(),
                0, 1, 0, -justifiedCenter.getY(),
                0, 0, 1, -justifiedCenter.getZ(),
                0, 0, 0, 1
        });
        Vector v4 = new Vector(vector, 1.0);
        v4 = translationMatrix.multiply(v4);
        
        Vector v = rotationMatrix.transform(new Vector(v4.getX(), v4.getY(), v4.getZ()));
        
        Matrix4 untranslationMatrix = new Matrix4(new double[] {
                1, 0, 0, justifiedCenter.getX(),
                0, 1, 0, justifiedCenter.getY(),
                0, 0, 1, justifiedCenter.getZ(),
                0, 0, 0, 1
        });
        v4 = new Vector(v, 1);
        v4 = untranslationMatrix.multiply(v4);
        
        return new Vector(v4.getX(), v4.getY(), v4.getZ());
    }
    
}
