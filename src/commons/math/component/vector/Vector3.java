/*
 * File:    Vector3.java
 * Package: commons.math.component.vector
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.component.vector;

import commons.math.component.handler.error.ComponentErrorHandlerProvider;
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
     * The constructor for a 3D Vector from components.
     *
     * @param x The x component of the Vector.
     * @param y The y component of the Vector.
     * @param z The z component of the Vector.
     * @see Vector#Vector(double...)
     */
    public Vector3(double x, double y, double z) {
        super(x, y, z);
    }
    
    /**
     * Constructs a 3D Vector from a Vector.
     *
     * @param vector The Vector.
     * @see #Vector3(double, double, double)
     */
    public Vector3(Vector vector) {
        this(vector.getRawX(), vector.getRawY(), vector.getRawZ());
    }
    
    /**
     * Constructs a 3D Vector by extending a 2D Vector.
     *
     * @param vector The 2D Vector to extend.
     * @param z      The z component of the Vector.
     * @see #Vector3(double, double, double)
     */
    public Vector3(Vector2 vector, double z) {
        this(vector.getRawX(), vector.getRawY(), z);
    }
    
    /**
     * The constructor for an empty 3D Vector.
     *
     * @see Vector#Vector(int)
     */
    public Vector3() {
        super(DIMENSIONALITY);
    }
    
    /**
     * The protected constructor for a 3D Vector with a dimensionality argument.
     *
     * @param dim The dimensionality argument. *Ignored for Vector3*
     * @see #Vector3()
     */
    protected Vector3(int dim) {
        this();
    }
    
    
    //Methods
    
    /**
     * Creates a cloned copy of the Vector.
     *
     * @return The cloned Vector.
     * @see #Vector3(Vector)
     */
    @Override
    public Vector3 cloned() {
        Vector3 clone = new Vector3(this);
        copyMeta(clone);
        return clone;
    }
    
    /**
     * Creates an empty copy of the Vector.
     *
     * @return The empty copy of the Vector.
     * @see #Vector3()
     */
    @Override
    public Vector3 emptyCopy() {
        return new Vector3();
    }
    
    /**
     * Creates a new Vector instance.
     *
     * @param dim *Ignored for Vector3*
     * @return The new Vector.
     * @see #Vector3()
     */
    @Override
    public Vector3 createNewInstance(int dim) {
        return createInstance(DIMENSIONALITY);
    }
    
    /**
     * Calculates the cross product between this Vector with another 3D Vector.
     *
     * @param other The other Vector.
     * @return The cross product between the Vectors.
     * @throws ArithmeticException When the other Vector does not have a dimensionality of 3.
     * @see #cross(Vector, Vector)
     */
    public Vector cross(Vector other) throws ArithmeticException {
        return cross(this, other);
    }
    
    
    //Getters
    
    /**
     * Returns the name of the type of Component.
     *
     * @return The name of the type of Component.
     */
    @Override
    public String getName() {
        return "3D Vector";
    }
    
    /**
     * Returns whether this Component is resizeable or not.
     *
     * @return Whether this Component is resizeable or not.
     */
    @Override
    public boolean isResizeable() {
        return false;
    }
    
    
    //Functions
    
    /**
     * Creates a new 3D Vector instance.
     *
     * @return The new Vector.
     * @see #Vector3()
     */
    public static Vector3 createInstance() {
        return new Vector3();
    }
    
    /**
     * Creates a new 3D Vector instance.
     *
     * @param dim *Ignored for Vector3*
     * @return The new Vector.
     * @see #createInstance()
     */
    public static Vector3 createInstance(int dim) {
        return createInstance();
    }
    
    /**
     * Creates a 3D identity Vector.
     *
     * @return The identity Vector.
     * @see VectorInterface#identity(int, Class)
     */
    public static Vector3 identity() {
        return VectorInterface.identity(DIMENSIONALITY, Vector3.class);
    }
    
    /**
     * Creates a 3D identity Vector.
     *
     * @param dim *Ignored for Vector3*
     * @return The identity Vector.
     * @see #identity()
     */
    public static Vector3 identity(int dim) {
        return identity();
    }
    
    /**
     * Creates a 3D origin Vector.
     *
     * @return The origin Vector.
     * @see VectorInterface#origin(int, Class)
     */
    public static Vector3 origin() {
        return VectorInterface.origin(DIMENSIONALITY, Vector3.class);
    }
    
    /**
     * Creates a 3D origin Vector.
     *
     * @param dim *Ignored for Vector3*
     * @return The origin Vector.
     * @see #origin()
     */
    public static Vector3 origin(int dim) {
        return origin();
    }
    
    /**
     * Calculates the cross product between two 3D Vectors.
     *
     * @param vector2 The other Vector.
     * @return The cross product.
     * @throws ArithmeticException When either of the Vectors do not have a dimensionality of 3.
     */
    public static Vector cross(Vector vector1, Vector vector2) throws ArithmeticException {
        ComponentErrorHandlerProvider.assertDimensionalityEqual(vector1, DIMENSIONALITY);
        ComponentErrorHandlerProvider.assertDimensionalityEqual(vector2, DIMENSIONALITY);
        return new Vector3(
                vector1.getRawComponents()[1] * vector2.getRawComponents()[2] - vector1.getRawComponents()[2] * vector2.getRawComponents()[1],
                vector1.getRawComponents()[2] * vector2.getRawComponents()[0] - vector1.getRawComponents()[0] * vector2.getRawComponents()[2],
                vector1.getRawComponents()[0] * vector2.getRawComponents()[1] - vector1.getRawComponents()[1] * vector2.getRawComponents()[0]
        );
    }
    
}
