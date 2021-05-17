/*
 * File:    RawVector.java
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
import commons.math.component.RawComponent;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the base properties of a Raw Vector.
 */
public class RawVector extends RawComponent<RawVector> implements VectorInterface<Number, RawVector> {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(RawVector.class);
    
    
    //Constructors
    
    /**
     * The constructor for a Raw Vector from components.
     *
     * @param components The components that define the Raw Vector.
     */
    public RawVector(Number... components) {
        super();
        setComponents(components);
    }
    
    /**
     * The constructor for a Raw Vector from a list of components.
     *
     * @param components The components that define the Raw Vector, as a list.
     * @param <T>        The Number type of the components.
     * @see #RawVector(Number...)
     */
    public <T extends Number> RawVector(List<T> components) {
        this(components.stream()
                .map(Number.class::cast).toArray(Number[]::new));
    }
    
    /**
     * The constructor for a Raw Vector from another Raw Vector.
     *
     * @param vector The Raw Vector.
     * @see #RawVector(Number...)
     */
    public RawVector(RawVector vector) {
        this(Arrays.stream(vector.getRawComponents())
                .toArray(Number[]::new));
    }
    
    /**
     * The constructor for a Raw Vector from a Vector.
     *
     * @param vector The Vector.
     * @see #RawVector(Number...)
     */
    public RawVector(Vector vector) {
        this(Arrays.stream(vector.getRawComponents())
                .map(Number.class::cast).toArray(Number[]::new));
    }
    
    /**
     * The constructor for a Raw Vector from another Raw Vector with added components.
     *
     * @param vector     The Raw Vector.
     * @param components The components to add.
     * @see #RawVector(Number...)
     */
    public RawVector(RawVector vector, Number... components) {
        this(Arrays.stream(ArrayUtility.merge(
                vector.getRawComponents(),
                components, Number.class)
        ).toArray(Number[]::new));
    }
    
    /**
     * The constructor for a Raw Vector from a Vector with added components.
     *
     * @param vector     The Vector.
     * @param components The components to add.
     * @see #RawVector(Number...)
     */
    public RawVector(Vector vector, Number... components) {
        this(Arrays.stream(ArrayUtility.merge(
                ArrayUtils.toObject(Arrays.stream(vector.getRawComponents()).mapToDouble(Double::doubleValue).toArray()),
                components, Number.class)
        ).toArray(Number[]::new));
    }
    
    /**
     * The constructor for a Raw Vector of a certain dimensionality.
     *
     * @param dim The dimensionality of the Raw Vector.
     * @see #RawVector(Number...)
     */
    public RawVector(int dim) {
        this(Collections.nCopies(dim, 0.0).stream()
                .map(Number.class::cast).toArray(Number[]::new));
    }
    
    /**
     * The constructor for an empty Raw Vector.
     *
     * @see #RawVector(int)
     */
    public RawVector() {
        this(0);
    }
    
    
    //Methods
    
    /**
     * Returns a string that represents the Raw Vector.
     *
     * @return A string that represents the Raw Vector.
     * @see VectorInterface#vectorString()
     */
    @Override
    public String toString() {
        return VectorInterface.super.vectorString();
    }
    
    /**
     * Creates a cloned copy of the Raw Vector.
     *
     * @return The cloned Raw Vector.
     * @see #RawVector(RawVector)
     */
    @Override
    public RawVector cloned() {
        RawVector clone = new RawVector(this);
        copyMeta(clone);
        return clone;
    }
    
    /**
     * Creates an empty copy of the Raw Vector.
     *
     * @return The empty copy of the Raw Vector.
     * @see #RawVector(int)
     */
    @Override
    public RawVector emptyCopy() {
        return new RawVector(getLength());
    }
    
    /**
     * Creates a new Raw Vector instance of the specified dimensionality.
     *
     * @param dim The dimensionality of the new Raw Vector.
     * @return The new Raw Vector.
     * @see #RawVector(int)
     */
    @Override
    public RawVector createNewInstance(int dim) {
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
        return "Raw Vector";
    }
    
    
    //Functions
    
    /**
     * Creates a new Raw Vector instance of the specified dimensionality.
     *
     * @param dim The dimensionality of the new Raw Vector.
     * @return The new Raw Vector.
     * @see #RawVector(int)
     */
    public static RawVector createInstance(int dim) {
        return new RawVector(Math.max(dim, 0));
    }
    
    /**
     * Creates an identity Raw Vector of a certain dimensionality.
     *
     * @param dim The dimensionality of the identity Raw Vector.
     * @return The identity Raw Vector.
     * @see VectorInterface#identity(int, Class)
     */
    public static RawVector identity(int dim) {
        return VectorInterface.identity(dim, RawVector.class);
    }
    
    /**
     * Creates an origin Raw Vector of a certain dimensionality.
     *
     * @param dim The dimensionality of the origin Raw Vector.
     * @return The origin Raw Vector.
     * @see VectorInterface#origin(int, Class)
     */
    public static RawVector origin(int dim) {
        return VectorInterface.origin(dim, RawVector.class);
    }
    
    /**
     * Calculates the average of a list of Raw Vectors.
     *
     * @param vectors The list of Raw Vectors.
     * @return The average of the Raw Vectors.
     * @throws ArithmeticException When the Raw Vectors do not all have the same dimensionality.
     * @see #average(List)
     */
    public static RawVector averageVector(List<RawVector> vectors) throws ArithmeticException {
        return vectors.isEmpty() ? new RawVector() : (
                (vectors.size() == 1) ? vectors.get(0).cloned() :
                vectors.get(0).average(ListUtility.subList(vectors, 1)));
    }
    
    /**
     * Calculates the average of a set of Raw Vectors.
     *
     * @param vectors The set of Raw Vectors.
     * @return The average of the Raw Vectors.
     * @throws ArithmeticException When the Raw Vectors do not all have the same dimensionality.
     * @see #averageVector(List)
     */
    public static RawVector averageVector(RawVector... vectors) throws ArithmeticException {
        return averageVector(Arrays.asList(vectors));
    }
    
}
