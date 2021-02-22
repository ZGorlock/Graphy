/*
 * File:    Vector3.java
 * Package: commons.math.vector
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines a 3-Dimensional Vector.
 */
public class Vector3 extends Vector {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(Vector3.class);
    
    
    //Constants
    
    /**
     * The dimensionality of a 3D Vector.
     */
    public static final int DIMENSIONALITY = 3;
    
    
    //Constructors
    
    /**
     * The constructor for a 3D Vector.
     *
     * @param x The x component of the Vector.
     * @param y The y component of the Vector.
     * @param z The z component of the Vector.
     */
    public Vector3(double x, double y, double z) {
        super(x, y, z);
    }
    
    /**
     * Constructs a 3D Vector from a Vector.
     *
     * @param vector The Vector.
     */
    public Vector3(Vector vector) {
        super(vector.getX(), vector.getY(), vector.getZ());
    }
    
    /**
     * Constructs a 3D Vector by extending a 2D Vector.
     *
     * @param vector The 2D Vector to extend.
     * @param z      The z component of the Vector.
     */
    public Vector3(Vector2 vector, double z) {
        super(vector.getX(), vector.getY(), z);
    }
    
    
    //Methods
    
    /**
     * Calculates the cross product of this Vector with another Vector.
     *
     * @param vector The other Vector.
     * @return The cross product.
     */
    public Vector3 cross(Vector vector) throws ArithmeticException {
        if (vector.getDimensionality() != 3) {
            throw new ArithmeticException(dimensionalityNotEqualErrorMessage(vector, 3));
        }
        
        return new Vector3(
                getY() * vector.getZ() - getZ() * vector.getY(),
                getZ() * vector.getX() - getX() * vector.getZ(),
                getX() * vector.getY() - getY() * vector.getX()
        );
    }
    
    
    //Getters
    
    /**
     * Returns the dimensionality of the 3D Vector.
     *
     * @return The dimensionality of the 3D Vector.
     * @see Vector#getDimensionality()
     * @see Vector3#DIMENSIONALITY
     */
    @Override
    public int getDimensionality() {
        return DIMENSIONALITY;
    }
    
}
