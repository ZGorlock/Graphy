/*
 * File:    Vector.java
 * Package: commons.math.component.vector
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.component.vector;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import commons.list.ArrayUtility;
import commons.list.ListUtility;
import commons.math.component.Component;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the base properties of a Vector.
 */
public class Vector extends Component<Vector> implements VectorInterface<Double, Vector> {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(Vector.class);
    
    
    //Constructors
    
    /**
     * The constructor for a Vector from components.
     *
     * @param components The components that define the Vector.
     */
    public Vector(double... components) {
        super();
        setComponents(ArrayUtils.toObject(components));
    }
    
    /**
     * The constructor for a Vector from a list of components.
     *
     * @param components The components that define the Vector, as a list.
     * @param <T>        The Number type of the components.
     * @see #Vector(double...)
     */
    public <T extends Number> Vector(List<T> components) {
        this(components.stream()
                .mapToDouble(Number::doubleValue).toArray());
    }
    
    /**
     * The constructor for a Vector from another Vector.
     *
     * @param vector The Vector.
     * @see #Vector(double...)
     */
    public Vector(Vector vector) {
        this(Arrays.stream(vector.getRawComponents())
                .mapToDouble(e -> e).toArray());
    }
    
    /**
     * The constructor for a Vector from another Vector with added components.
     *
     * @param vector     The Vector.
     * @param components The components to add.
     * @see #Vector(double...)
     */
    public Vector(Vector vector, double... components) {
        this(Arrays.stream(ArrayUtility.merge(
                vector.getRawComponents(),
                ArrayUtils.toObject(components), Double.class)
        ).mapToDouble(e -> e).toArray());
    }
    
    /**
     * The constructor for a Vector of a certain dimensionality.
     *
     * @param dim The dimensionality of the Vector.
     * @see #Vector(double...)
     */
    public Vector(int dim) {
        this(Collections.nCopies(dim, 0.0).stream()
                .mapToDouble(e -> e).toArray());
    }
    
    /**
     * The constructor for an empty Vector.
     *
     * @see #Vector(int)
     */
    public Vector() {
        this(0);
    }
    
    
    //Methods
    
    /**
     * Returns a string that represents the Vector.
     *
     * @return A string that represents the Vector.
     * @see VectorInterface#vectorString()
     */
    @Override
    public String toString() {
        return VectorInterface.super.vectorString();
    }
    
    /**
     * Creates a cloned copy of the Vector.
     *
     * @return The cloned Vector.
     * @see #Vector(Vector)
     */
    @Override
    public Vector cloned() {
        Vector clone = new Vector(this);
        copyMeta(clone);
        return clone;
    }
    
    /**
     * Creates an empty copy of the Vector.
     *
     * @return The empty copy of the Vector.
     * @see #Vector(int)
     */
    @Override
    public Vector emptyCopy() {
        return new Vector(getLength());
    }
    
    /**
     * Creates a new Vector instance of the specified dimensionality.
     *
     * @param dim The dimensionality of the new Vector.
     * @return The new Vector.
     * @see #Vector(int)
     */
    @Override
    public Vector createNewInstance(int dim) {
        return createInstance(Math.max(dim, 0));
    }
    
    
    //Getters
    
    /**
     * Returns the name of the type of Component.
     *
     * @return The name of the type of Component.
     */
    @Override
    public String getName() {
        return "Vector";
    }
    
    
    //Functions
    
    /**
     * Creates a new Vector instance of the specified dimensionality.
     *
     * @param dim The dimensionality of the new Vector.
     * @return The new Vector.
     * @see #Vector(int)
     */
    public static Vector createInstance(int dim) {
        return new Vector(Math.max(dim, 0));
    }
    
    /**
     * Creates an identity Vector of a certain dimensionality.
     *
     * @param dim The dimensionality of the identity Vector.
     * @return The identity Vector.
     * @see VectorInterface#identity(int, Class)
     */
    public static Vector identity(int dim) {
        return VectorInterface.identity(dim, Vector.class);
    }
    
    /**
     * Creates an origin Vector of a certain dimensionality.
     *
     * @param dim The dimensionality of the origin Vector.
     * @return The origin Vector.
     * @see VectorInterface#origin(int, Class)
     */
    public static Vector origin(int dim) {
        return VectorInterface.origin(dim, Vector.class);
    }
    
    /**
     * Calculates the average of a list of Vectors.
     *
     * @param vectors The list of Vectors.
     * @return The average of the Vectors.
     * @throws ArithmeticException When the Vectors do not all have the same dimensionality.
     * @see #average(List)
     */
    public static Vector averageVector(List<Vector> vectors) throws ArithmeticException {
        return vectors.isEmpty() ? new Vector() : (
                (vectors.size() == 1) ? vectors.get(0).cloned() :
                vectors.get(0).average(ListUtility.subList(vectors, 1)));
    }
    
    /**
     * Calculates the average of a set of Vectors.
     *
     * @param vectors The set of Vectors.
     * @return The average of the Vectors.
     * @throws ArithmeticException When the Vectors do not all have the same dimensionality.
     * @see #averageVector(List)
     */
    public static Vector averageVector(Vector... vectors) throws ArithmeticException {
        return averageVector(Arrays.asList(vectors));
    }
    
}
