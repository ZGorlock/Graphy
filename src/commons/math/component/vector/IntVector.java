/*
 * File:    IntVector.java
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
import commons.math.component.IntComponent;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the base properties of an Integer Vector.
 */
public class IntVector extends IntComponent<IntVector> implements VectorInterface<Integer, IntVector> {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(IntVector.class);
    
    
    //Constructors
    
    /**
     * The constructor for an Integer Vector from components.
     *
     * @param components The components that define the Integer Vector.
     */
    public IntVector(int... components) {
        super();
        setComponents(ArrayUtils.toObject(components));
    }
    
    /**
     * The constructor for an Integer Vector from a list of components.
     *
     * @param components The components that define the Integer Vector, as a list.
     * @param <T>        The Number type of the components.
     * @see #IntVector(int...)
     */
    public <T extends Number> IntVector(List<T> components) {
        this(components.stream()
                .mapToInt(Number::intValue).toArray());
    }
    
    /**
     * The constructor for an Integer Vector from another Integer Vector.
     *
     * @param vector The Integer Vector.
     * @see #IntVector(int...)
     */
    public IntVector(IntVector vector) {
        this(Arrays.stream(vector.getRawComponents())
                .mapToInt(e -> e).toArray());
    }
    
    /**
     * The constructor for an Integer Vector from a Vector.
     *
     * @param vector The Vector.
     * @see #IntVector(int...)
     */
    public IntVector(Vector vector) {
        this(Arrays.stream(vector.getRawComponents())
                .mapToInt(Double::intValue).toArray());
    }
    
    /**
     * The constructor for an Integer Vector from another Integer Vector with added components.
     *
     * @param vector     The Integer Vector.
     * @param components The components to add.
     * @see #IntVector(int...)
     */
    public IntVector(IntVector vector, int... components) {
        this(Arrays.stream(ArrayUtility.merge(
                vector.getRawComponents(),
                ArrayUtils.toObject(components), Integer.class)
        ).mapToInt(e -> e).toArray());
    }
    
    /**
     * The constructor for an Integer Vector from a Vector with added components.
     *
     * @param vector     The Vector.
     * @param components The components to add.
     * @see #IntVector(int...)
     */
    public IntVector(Vector vector, int... components) {
        this(Arrays.stream(ArrayUtility.merge(
                ArrayUtils.toObject(Arrays.stream(vector.getRawComponents()).mapToInt(Double::intValue).toArray()),
                ArrayUtils.toObject(components), Integer.class)
        ).mapToInt(e -> e).toArray());
    }
    
    /**
     * The constructor for an Integer Vector of a certain dimensionality.
     *
     * @param dim The dimensionality of the Integer Vector.
     * @see #IntVector(int...)
     */
    public IntVector(int dim) {
        this(Collections.nCopies(dim, 0).stream()
                .mapToInt(e -> e).toArray());
    }
    
    /**
     * The constructor for an empty Integer Vector.
     *
     * @see #IntVector(int)
     */
    public IntVector() {
        this(0);
    }
    
    
    //Methods
    
    /**
     * Returns a string that represents the Integer Vector.
     *
     * @return A string that represents the Integer Vector.
     * @see VectorInterface#vectorString()
     */
    @Override
    public String toString() {
        return VectorInterface.super.vectorString();
    }
    
    /**
     * Creates a cloned copy of the Integer Vector.
     *
     * @return The cloned Integer Vector.
     * @see #IntVector(IntVector)
     */
    @Override
    public IntVector cloned() {
        IntVector clone = new IntVector(this);
        copyMeta(clone);
        return clone;
    }
    
    /**
     * Creates an empty copy of the Integer Vector.
     *
     * @return The empty copy of the Integer Vector.
     * @see #IntVector(int)
     */
    @Override
    public IntVector emptyCopy() {
        return new IntVector(getLength());
    }
    
    /**
     * Creates a new Integer Vector instance of the specified dimensionality.
     *
     * @param dim The dimensionality of the new Integer Vector.
     * @return The new Integer Vector.
     * @see #IntVector(int)
     */
    @Override
    public IntVector createNewInstance(int dim) {
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
        return "Integer Vector";
    }
    
    
    //Functions
    
    /**
     * Creates a new Integer Vector instance of the specified dimensionality.
     *
     * @param dim The dimensionality of the new Integer Vector.
     * @return The new Integer Vector.
     * @see #IntVector(int)
     */
    public static IntVector createInstance(int dim) {
        return new IntVector(Math.max(dim, 0));
    }
    
    /**
     * Creates an identity Integer Vector of a certain dimensionality.
     *
     * @param dim The dimensionality of the identity Integer Vector.
     * @return The identity Integer Vector.
     * @see VectorInterface#identity(int, Class)
     */
    public static IntVector identity(int dim) {
        return VectorInterface.identity(dim, IntVector.class);
    }
    
    /**
     * Creates an origin Integer Vector of a certain dimensionality.
     *
     * @param dim The dimensionality of the origin Integer Vector.
     * @return The origin Integer Vector.
     * @see VectorInterface#origin(int, Class)
     */
    public static IntVector origin(int dim) {
        return VectorInterface.origin(dim, IntVector.class);
    }
    
    /**
     * Calculates the average of a list of Integer Vectors.
     *
     * @param vectors The list of Integer Vectors.
     * @return The average of the Integer Vectors.
     * @throws ArithmeticException When the Integer Vectors do not all have the same dimensionality.
     * @see #average(List)
     */
    public static IntVector averageVector(List<IntVector> vectors) throws ArithmeticException {
        return vectors.isEmpty() ? new IntVector() : (
                (vectors.size() == 1) ? vectors.get(0).cloned() :
                vectors.get(0).average(ListUtility.subList(vectors, 1)));
    }
    
    /**
     * Calculates the average of a set of Integer Vectors.
     *
     * @param vectors The set of Integer Vectors.
     * @return The average of the Integer Vectors.
     * @throws ArithmeticException When the Integer Vectors do not all have the same dimensionality.
     * @see #averageVector(List)
     */
    public static IntVector averageVector(IntVector... vectors) throws ArithmeticException {
        return averageVector(Arrays.asList(vectors));
    }
    
}
