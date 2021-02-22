/*
 * File:    Matrix2.java
 * Package: commons.math.matrix
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.matrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines a 2-Dimensional Matrix.
 */
public class Matrix2 extends Matrix {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(Matrix2.class);
    
    
    //Constants
    
    /**
     * The dimensionality of a 2D Matrix.
     */
    public static final int DIMENSIONALITY = 2;
    
    
    //Constructors
    
    /**
     * The constructor for a 2D Matrix.
     *
     * @param values The elements of the matrix.
     * @see Matrix#Matrix(double[])
     */
    public Matrix2(double[] values) {
        super(values);
    }
    
    
    //Methods
    
    /**
     * Determines the determinant of the matrix.
     *
     * @return The determinant of the matrix.
     */
    public double determinant() {
        return (get(0) * get(3)) - (get(1) * get(2));
    }
    
    
    //Getters
    
    /**
     * Returns the dimensionality of the 2D Matrix.
     *
     * @return The dimensionality of the 2D Matrix.
     * @see Matrix#getDimensionality()
     * @see Matrix2#DIMENSIONALITY
     */
    @Override
    public int getDimensionality() {
        return DIMENSIONALITY;
    }
    
}
