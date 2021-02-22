/*
 * File:    Matrix4.java
 * Package: commons.math.matrix
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.matrix;

import commons.math.vector.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines a 4-Dimensional Matrix.
 */
public class Matrix4 extends Matrix {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(Matrix4.class);
    
    
    //Constants
    
    /**
     * The dimensionality of a 4D Matrix.
     */
    public static final int DIMENSIONALITY = 4;
    
    
    //Constructors
    
    /**
     * The constructor for a 4D Matrix.
     *
     * @param values The elements of the matrix.
     * @see Matrix#Matrix(double[])
     */
    public Matrix4(double[] values) {
        super(values);
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
                    result[row * 4 + col] += get(row * 4 + i) * other.get(i * 4 + col);
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
        if (other.getDimensionality() != getDimensionality()) {
            throw new ArithmeticException("The vector: " + other + " is of improper size for multiplication with a 4D matrix.");
        }
        
        double[] result = new double[4];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                result[row] += get(row * 4 + col) * other.get(col);
            }
        }
        return new Vector(result[0], result[1], result[2], result[3]);
    }
    
    
    //Getters
    
    /**
     * Returns the dimensionality of the 4D Matrix.
     *
     * @return The dimensionality of the 4D Matrix.
     * @see Matrix#getDimensionality()
     */
    @Override
    public int getDimensionality() {
        return DIMENSIONALITY;
    }
    
}
