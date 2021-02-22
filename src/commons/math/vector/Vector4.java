/*
 * File:    Vector4.java
 * Package: commons.math.vector
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines a 4-Dimensional Vector.
 */
public class Vector4 extends Vector {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(Vector4.class);
    
    
    //Constants
    
    /**
     * The dimensionality of a 4D Vector.
     */
    public static final int DIMENSIONALITY = 4;
    
    
    //Constructors
    
    /**
     * The constructor for a 4D Vector.
     *
     * @param x The x component of the Vector.
     * @param y The y component of the Vector.
     * @param z The z component of the Vector.
     * @param w The w component of the Vector.
     */
    public Vector4(double x, double y, double z, double w) {
        super(x, y, z, w);
    }
    
    /**
     * Constructs a 4D Vector from a Vector.
     *
     * @param vector The Vector.
     */
    public Vector4(Vector vector) {
        super(vector.getX(), vector.getY(), vector.getZ(), vector.getW());
    }
    
    /**
     * Constructs a 4D Vector by extending a 3D Vector.
     *
     * @param vector The 3D Vector to extend.
     * @param w      The w component of the Vector.
     */
    public Vector4(Vector3 vector, double w) {
        super(vector.getX(), vector.getY(), vector.getZ(), w);
    }
    
    
    //Getters
    
    /**
     * Returns the dimensionality of the 4D Vector.
     *
     * @return The dimensionality of the 4D Vector.
     * @see Vector#getDimensionality()
     * @see Vector4#DIMENSIONALITY
     */
    @Override
    public int getDimensionality() {
        return DIMENSIONALITY;
    }
    
}
