/*
 * File:    BigVector.java
 * Package: commons.math.component.vector
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.component.vector;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import commons.list.ArrayUtility;
import commons.list.ListUtility;
import commons.math.component.BigComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the base properties of a Big Vector.
 */
public class BigVector extends BigComponent<BigVector> implements VectorInterface<BigDecimal, BigVector> {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(BigVector.class);
    
    
    //Constructors
    
    /**
     * The constructor for a Big Vector from components.
     *
     * @param components The components that define the Big Vector.
     */
    public BigVector(BigDecimal... components) {
        super();
        setComponents(components);
    }
    
    /**
     * The constructor for a Big Vector from double components.
     *
     * @param components The double components that define the Big Vector.
     * @see #BigVector(BigDecimal...)
     */
    public BigVector(double... components) {
        this(Arrays.stream(components)
                .mapToObj(BigDecimal::valueOf).toArray(BigDecimal[]::new));
    }
    
    /**
     * The constructor for a Big Vector from string components.
     *
     * @param components The string components that define the Big Vector.
     * @throws NumberFormatException If the string components provided are not numbers.
     * @see #BigVector(BigDecimal...)
     */
    public BigVector(String... components) throws NumberFormatException {
        this(Arrays.stream(components)
                .map(BigDecimal::new).toArray(BigDecimal[]::new));
    }
    
    /**
     * The constructor for a Big Vector from a list of components.
     *
     * @param components The components that define the Big Vector, as a list.
     * @param <T>        The Number type of the components.
     * @see #BigVector(BigDecimal...)
     */
    public <T extends Number> BigVector(List<T> components) {
        this(components.stream()
                .map(e -> (e instanceof BigDecimal) ? (BigDecimal) e : BigDecimal.valueOf(e.doubleValue()))
                .toArray(BigDecimal[]::new));
    }
    
    /**
     * The constructor for a Big Vector from another Big Vector.
     *
     * @param vector The Big Vector.
     * @see #BigVector(BigDecimal...)
     */
    public BigVector(BigVector vector) {
        this(vector.getRawComponents());
        setMathContext(vector.getMathContext());
    }
    
    /**
     * The constructor for a Big Vector from a Vector.
     *
     * @param vector The Vector.
     * @see #BigVector(BigDecimal...)
     */
    public BigVector(Vector vector) {
        this(Arrays.stream(vector.getRawComponents())
                .map(BigDecimal::valueOf).toArray(BigDecimal[]::new));
    }
    
    /**
     * The constructor for a Big Vector from another Big Vector with added components.
     *
     * @param vector     The Big Vector.
     * @param components The components to add.
     * @see #BigVector(BigDecimal...)
     */
    public BigVector(BigVector vector, BigDecimal... components) {
        this(ArrayUtility.merge(
                vector.getRawComponents(),
                components, BigDecimal.class)
        );
        setMathContext(vector.getMathContext());
    }
    
    /**
     * The constructor for a Big Vector from another Big Vector with added components.
     *
     * @param vector     The Big Vector.
     * @param components The components to add.
     * @see #BigVector(BigDecimal...)
     */
    public BigVector(BigVector vector, double... components) {
        this(vector, Arrays.stream(components).mapToObj(BigDecimal::valueOf).toArray(BigDecimal[]::new));
    }
    
    /**
     * The constructor for a Big Vector from another Big Vector with added string components.
     *
     * @param vector     The Big Vector.
     * @param components The string components to add.
     * @see #BigVector(BigDecimal...)
     */
    public BigVector(BigVector vector, String... components) {
        this(vector, Arrays.stream(components).map(BigDecimal::new).toArray(BigDecimal[]::new));
    }
    
    /**
     * The constructor for a Big Vector from another Vector with added components.
     *
     * @param vector     The Vector.
     * @param components The components to add.
     * @see #BigVector(BigDecimal...)
     */
    public BigVector(Vector vector, BigDecimal... components) {
        this(ArrayUtility.merge(
                Arrays.stream(vector.getRawComponents()).map(BigDecimal::valueOf).toArray(BigDecimal[]::new),
                components, BigDecimal.class)
        );
    }
    
    /**
     * The constructor for a Big Vector from another Vector with added components.
     *
     * @param vector     The Vector.
     * @param components The components to add.
     * @see #BigVector(BigDecimal...)
     */
    public BigVector(Vector vector, double... components) {
        this(vector, Arrays.stream(components).mapToObj(BigDecimal::valueOf).toArray(BigDecimal[]::new));
    }
    
    /**
     * The constructor for a Big Vector from another Vector with added string components.
     *
     * @param vector     The Vector.
     * @param components The string components to add.
     * @see #BigVector(BigDecimal...)
     */
    public BigVector(Vector vector, String... components) {
        this(vector, Arrays.stream(components).map(BigDecimal::new).toArray(BigDecimal[]::new));
    }
    
    /**
     * The constructor for a Big Vector of a certain dimensionality.
     *
     * @param dim The dimensionality of the Big Vector.
     * @see #BigVector(BigDecimal...)
     */
    public BigVector(int dim) {
        this(Collections.nCopies(dim, BigDecimal.ZERO)
                .toArray(BigDecimal[]::new));
    }
    
    /**
     * The default no-argument constructor for a Big Vector.
     *
     * @see #BigVector(int)
     */
    public BigVector() {
        this(0);
    }
    
    
    //Methods
    
    /**
     * Returns a string that represents the Big Vector.
     *
     * @return A string that represents the Big Vector.
     * @see VectorInterface#vectorString()
     */
    @Override
    public String toString() {
        return VectorInterface.super.vectorString();
    }
    
    /**
     * Creates a cloned copy of the Big Vector.
     *
     * @return The cloned Big Vector.
     * @see #BigVector(BigVector)
     */
    @Override
    public BigVector cloned() {
        BigVector clone = new BigVector(this);
        copyMeta(clone);
        return clone;
    }
    
    /**
     * Creates an empty copy of the Big Vector.
     *
     * @return The empty copy of the Big Vector.
     * @see #BigVector(int)
     */
    @Override
    public BigVector emptyCopy() {
        return new BigVector(getLength());
    }
    
    /**
     * Creates a new Big Vector instance of the specified dimensionality.
     *
     * @param dim The dimensionality of the new Big Vector.
     * @return The new Big Vector.
     * @see #createInstance(int)
     */
    @Override
    public BigVector createNewInstance(int dim) {
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
        return "Big Vector";
    }
    
    
    //Functions
    
    /**
     * Creates a new Big Vector instance of the specified dimensionality.
     *
     * @param dim The dimensionality of the new Big Vector.
     * @return The new Big Vector.
     * @see #BigVector(int)
     */
    public static BigVector createInstance(int dim) {
        return new BigVector(Math.max(dim, 0));
    }
    
    /**
     * Creates an identity Big Vector of a certain dimensionality.
     *
     * @param dim The dimensionality of the identity Big Vector
     * @return The identity Big Vector.
     * @see VectorInterface#identity(int, Class)
     */
    public static BigVector identity(int dim) {
        return VectorInterface.identity(dim, BigVector.class);
    }
    
    /**
     * Creates an origin Big Vector of a certain dimensionality.
     *
     * @param dim The dimensionality of the unit Big Vector
     * @return The origin Big Vector.
     * @see VectorInterface#origin(int, Class)
     */
    public static BigVector origin(int dim) {
        return VectorInterface.origin(dim, BigVector.class);
    }
    
    /**
     * Calculates the average of a list of Big Vectors.
     *
     * @param vectors The list of Big Vectors.
     * @return The average of the Big Vectors.
     * @throws ArithmeticException When the Big Vectors do not all have the same dimensionality.
     * @see #average(List)
     */
    public static BigVector averageVector(List<BigVector> vectors) throws ArithmeticException {
        return vectors.isEmpty() ? new BigVector() : (
                (vectors.size() == 1) ? vectors.get(0).cloned() :
                vectors.get(0).average(ListUtility.subList(vectors, 1)));
    }
    
    /**
     * Calculates the average of a set of Big Vectors.
     *
     * @param vectors The set of Big Vectors.
     * @return The average of the Big Vectors.
     * @throws ArithmeticException When the Big Vectors do not all have the same dimensionality.
     * @see #averageVector(List)
     */
    public static BigVector averageVector(BigVector... vectors) throws ArithmeticException {
        return averageVector(Arrays.asList(vectors));
    }
    
}
