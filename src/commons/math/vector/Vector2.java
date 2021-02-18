/*
 * File:    Vector2.java
 * Package: commons.math.vector
 * Author:  Zachary Gill
 */

package commons.math.vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines a 2-Dimensional Vector.
 */
public class Vector2 extends Vector {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(Vector2.class);
    
    
    //Constants
    
    /**
     * The dimensionality of a 2D Vector.
     */
    public static final int DIMENSIONALITY = 2;
    
    
    //Constructors
    
    /**
     * The constructor for a 2D Vector.
     *
     * @param x The x component of the Vector.
     * @param y The y component of the Vector.
     */
    public Vector2(double x, double y) {
        super(x, y);
    }
    
    /**
     * Constructs a 2D Vector from a Vector.
     *
     * @param vector The Vector.
     */
    public Vector2(Vector vector) {
        this(vector.getX(), vector.getY());
    }
    
    
    //Functions
    
    /**
     * Calculates the dot flop of two Vectors.
     *
     * @param vector1 The first Vector.
     * @param vector2 The second Vector.
     * @return The dot flop of the Vectors.
     * @throws ArithmeticException When either of the Vectors do not have a dimensionality of at least 2.
     */
    public static Vector2 dotFlop(Vector vector1, Vector vector2) throws ArithmeticException {
        if (vector1.getDimensionality() != 2) {
            throw new ArithmeticException(dimensionalityNotEqualErrorMessage(vector1, 2));
        }
        if (vector2.getDimensionality() != 2) {
            throw new ArithmeticException(dimensionalityNotEqualErrorMessage(vector2, 2));
        }
        
        return new Vector2(
                (vector1.getX() * vector2.getX()) - (vector1.getY() * vector2.getY()),
                (vector1.getX() * vector2.getY()) + (vector1.getY() * vector2.getX())
        );
    }
    
    /**
     * Calculates the negative dot flop of two Vectors.
     *
     * @param vector1 The first Vector.
     * @param vector2 The second Vector.
     * @return The negative dot flop of the Vectors.
     * @throws ArithmeticException When either of the Vectors do not have a dimensionality of at least 2.
     */
    public static Vector2 dotFlopNegative(Vector vector1, Vector vector2) throws ArithmeticException {
        if (vector1.getDimensionality() != 2) {
            throw new ArithmeticException(dimensionalityNotEqualErrorMessage(vector1, 2));
        }
        if (vector2.getDimensionality() != 2) {
            throw new ArithmeticException(dimensionalityNotEqualErrorMessage(vector2, 2));
        }
        
        return new Vector2(
                (vector1.getX() * vector2.getX()) + (vector1.getY() * vector2.getY()),
                (vector1.getX() * vector2.getY()) - (vector1.getY() * vector2.getX())
        );
    }
    
    /**
     * Calculates the square sum of a Vector.
     *
     * @param vector The Vector.
     * @return The square sum of the Vector.
     */
    public static double squareSum(Vector vector) {
        if (vector.getDimensionality() != 2) {
            throw new ArithmeticException(dimensionalityNotEqualErrorMessage(vector, 2));
        }
        
        return Math.pow(vector.getX(), 2) + Math.pow(vector.getY(), 2);
    }
    
    /**
     * Calculates the square difference of a Vector.
     *
     * @param vector The Vector.
     * @return The square difference of the Vector.
     * @throws ArithmeticException When the Vector does not have a dimensionality of at least 2.
     */
    public static double squareDifference(Vector vector) throws ArithmeticException {
        if (vector.getDimensionality() != 2) {
            throw new ArithmeticException(dimensionalityNotEqualErrorMessage(vector, 2));
        }
        
        return Math.pow(vector.getX(), 2) - Math.pow(vector.getY(), 2);
    }
    
    
    //Getters
    
    /**
     * Returns the dimensionality of the 2D Vector.
     *
     * @return The dimensionality of the 2D Vector.
     * @see Vector#getDimensionality()
     * @see Vector2#DIMENSIONALITY
     */
    @Override
    public int getDimensionality() {
        return DIMENSIONALITY;
    }
    
}
