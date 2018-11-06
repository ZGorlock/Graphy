/*
 * File:    Vector3.java
 * Package: math.vector
 * Author:  Zachary Gill
 */

package math.vector;

/**
 * Defines a 3-dimensional Vector.
 */
public class Vector3 extends Vector {
    
    //Constructors
    
    /**
     * The constructor for a Vector3.
     *
     * @param x The x component of the Vector.
     * @param y The y component of the Vector.
     * @param z The z component of the Vector.
     */
    public Vector3(double x, double y, double z) {
        super(x, y, z);
    }
    
    /**
     * Constructs a Vector3 from a Vector.
     *
     * @param v The Vector.
     * @throws ArithmeticException When the Vector is not in three dimensions.
     */
    public Vector3(Vector v) {
        super(v.getX(), v.getY(), v.getZ());
    }
    
    /**
     * Constructs a Vector3 by extending a Vector2.
     *
     * @param v2 The Vector2 to extend.
     * @param z  The z component of the Vector.
     */
    public Vector3(Vector2 v2, double z) {
        super(v2.getX(), v2.getY(), z);
    }
    
    
    //Methods
    
    /**
     * Calculates the cross product of this Vector with another Vector.
     *
     * @param v The other Vector.
     * @return The cross product.
     * @throws ArithmeticException When the two Vectors are not of the same dimension.
     */
    public Vector cross(Vector v) throws ArithmeticException {
        if (!dimensionsEqual(v)) {
            throw new ArithmeticException("The vectors: " + toString() + " and " + v.toString() + " are of different dimensions.");
        }
        
        return new Vector(
                getY() * v.getZ() - getZ() * v.getY(),
                getZ() * v.getX() - getX() * v.getZ(),
                getX() * v.getY() - getY() * v.getX()
        );
    }
    
}
