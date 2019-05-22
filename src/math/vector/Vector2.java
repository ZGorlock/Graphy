/*
 * File:    Vector2.java
 * Package: math.vector
 * Author:  Zachary Gill
 */

package math.vector;

/**
 * Defines a 2-dimensional Vector.
 */
public class Vector2 extends Vector {
    
    //Constructors
    
    /**
     * The constructor for a Vector2.
     *
     * @param x The x component of the Vector.
     * @param y The y component of the Vector.
     */
    public Vector2(double x, double y) {
        super(x, y);
    }
    
    
    //Functions
    
    /**
     * Calculates the dot flop of two 2D Vectors.
     *
     * @param v1 The first Vector.
     * @param v2 The second Vector.
     * @return The dot flop of the Vectors.
     */
    public static Vector dotFlop(Vector v1, Vector v2) throws ArithmeticException {
        return new Vector2(
                (v1.getX() * v2.getX()) - (v1.getY() * v2.getY()),
                (v1.getX() * v2.getY()) + (v1.getY() * v2.getX())
        );
    }
    
    /**
     * Calculates the negative dot flop of two 2D Vectors.
     *
     * @param v1 The first Vector.
     * @param v2 The second Vector.
     * @return The negative dot flop of the Vectors.
     */
    public static Vector dotFlopNegative(Vector v1, Vector v2) throws ArithmeticException {
        return new Vector2(
                (v1.getX() * v2.getX()) + (v1.getY() * v2.getY()),
                (v1.getX() * v2.getY()) - (v1.getY() * v2.getX())
        );
    }
    
    /**
     * Calculates the square sum of a Vector.
     *
     * @param v The Vector.
     * @return The square sum of the Vector.
     */
    public static double squareSum(Vector v) throws ArithmeticException {
        return Math.pow(v.getX(), 2) + Math.pow(v.getY(), 2);
    }
    
    /**
     * Calculates the square difference of a Vector.
     *
     * @param v The Vector.
     * @return The square difference of the Vector.
     */
    public static double squareDifference(Vector v) throws ArithmeticException {
        return Math.pow(v.getX(), 2) - Math.pow(v.getY(), 2);
    }
    
}
