/*
 * File:    Vector4.java
 * Package: commons.math.component.vector
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.component.vector;

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
     * The constructor for a 4D Vector from components.
     *
     * @param x The x component of the Vector.
     * @param y The y component of the Vector.
     * @param z The z component of the Vector.
     * @param w The w component of the Vector.
     * @see Vector#Vector(double...)
     */
    public Vector4(double x, double y, double z, double w) {
        super(x, y, z, w);
    }
    
    /**
     * Constructs a 4D Vector from a Vector.
     *
     * @param vector The Vector.
     * @see #Vector4(double, double, double, double)
     */
    public Vector4(Vector vector) {
        this(vector.getRawX(), vector.getRawY(), vector.getRawZ(), vector.getRawW());
    }
    
    /**
     * Constructs a 4D Vector by extending a 3D Vector.
     *
     * @param vector The 3D Vector to extend.
     * @param w      The w component of the Vector.
     * @see #Vector4(double, double, double, double)
     */
    public Vector4(Vector3 vector, double w) {
        this(vector.getRawX(), vector.getRawY(), vector.getRawZ(), w);
    }
    
    /**
     * The constructor for an empty 4D Vector.
     *
     * @see Vector#Vector(int)
     */
    public Vector4() {
        super(DIMENSIONALITY);
    }
    
    /**
     * The protected constructor for a 4D Vector with a dimensionality argument.
     *
     * @param dim The dimensionality argument. *Ignored for Vector4*
     * @see #Vector4()
     */
    protected Vector4(int dim) {
        this();
    }
    
    
    //Methods
    
    /**
     * Creates a cloned copy of the Vector.
     *
     * @return The cloned Vector.
     * @see #Vector4(Vector)
     */
    @Override
    public Vector4 cloned() {
        Vector4 clone = new Vector4(this);
        copyMeta(clone);
        return clone;
    }
    
    /**
     * Creates an empty copy of the Vector.
     *
     * @return The empty copy of the Vector.
     * @see #Vector4()
     */
    @Override
    public Vector4 emptyCopy() {
        return new Vector4();
    }
    
    /**
     * Creates a new Vector instance.
     *
     * @param dim *Ignored for Vector4*
     * @return The new Vector.
     * @see #Vector4()
     */
    @Override
    public Vector4 createNewInstance(int dim) {
        return createInstance(DIMENSIONALITY);
    }
    
    
    //Getters
    
    /**
     * Returns the name of the type of Component.
     *
     * @return The name of the type of Component.
     */
    @Override
    public String getName() {
        return "4D Vector";
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
     * Creates a new 4D Vector instance.
     *
     * @return The new Vector.
     * @see #Vector4()
     */
    public static Vector4 createInstance() {
        return new Vector4();
    }
    
    /**
     * Creates a new 4D Vector instance.
     *
     * @param dim *Ignored for Vector4*
     * @return The new Vector.
     * @see #createInstance()
     */
    public static Vector4 createInstance(int dim) {
        return createInstance();
    }
    
    /**
     * Creates a 4D identity Vector.
     *
     * @return The identity Vector.
     * @see VectorInterface#identity(int, Class)
     */
    public static Vector4 identity() {
        return VectorInterface.identity(DIMENSIONALITY, Vector4.class);
    }
    
    /**
     * Creates a 4D identity Vector.
     *
     * @param dim *Ignored for Vector4*
     * @return The identity Vector.
     * @see #identity()
     */
    public static Vector4 identity(int dim) {
        return identity();
    }
    
    /**
     * Creates a 4D origin Vector.
     *
     * @return The origin Vector.
     * @see VectorInterface#origin(int, Class)
     */
    public static Vector4 origin() {
        return VectorInterface.origin(DIMENSIONALITY, Vector4.class);
    }
    
    /**
     * Creates a 4D origin Vector.
     *
     * @param dim *Ignored for Vector4*
     * @return The origin Vector.
     * @see #origin()
     */
    public static Vector4 origin(int dim) {
        return origin();
    }
    
}
