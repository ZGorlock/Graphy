/*
 * File:    Matrix4.java
 * Package: math.matrix
 * Author:  Zachary Gill
 */

package math.matrix;

import math.vector.Vector;

/**
 * Defines a 4D Matrix.
 */
public class Matrix4 {
    
    //Fields
    
    /**
     * The elements of the matrix.
     */
    public double[] values;
    
    
    //Constructors
    
    /**
     * The constructor for a 4D Matrix.
     *
     * @param values The elements of the matrix.
     */
    public Matrix4(double[] values) {
        this.values = values;
    }
    
    
    //Methods
    
    /**
     * Multiplies the 4D matrix by another 4D matrix.
     *
     * @param other The other 4D matrix.
     * @return The 4D matrix result of the multiplication.
     */
    public Matrix4 multiply(Matrix4 other) {
        double[] result = new double[16];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                for (int i = 0; i < 4; i++) {
                    result[row * 4 + col] += values[row * 4 + i] * other.values[i * 4 + col];
                }
            }
        }
        return new Matrix4(result);
    }
    
    /**
     * Multiplies the 4D matrix by a vector.
     *
     * @param other The vector.
     * @return The vector result of the multiplication.
     * @throws ArithmeticException When the vector is not of the proper size.
     */
    public Vector multiply(Vector other) throws ArithmeticException {
        if (other.getDimension() != 4) {
            throw new ArithmeticException("The vector: " + other + " is of improper size for multiplication with a 4D matrix.");
        }
        
        double[] result = new double[4];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                result[row] += values[row * 4 + col] * other.get(col);
            }
        }
        return new Vector(result[0], result[1], result[2], result[3]);
    }
    
}
