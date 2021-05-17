/*
 * File:    BigMatrix.java
 * Package: commons.math.component.matrix
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.component.matrix;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import commons.math.MathUtility;
import commons.math.component.BigComponent;
import commons.math.component.vector.BigVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the base properties of a Big Matrix.
 */
public class BigMatrix extends BigComponent<BigMatrix> implements MatrixInterface<BigDecimal, BigMatrix> {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(BigMatrix.class);
    
    
    //Constructors
    
    /**
     * The constructor for a Big Matrix from components.
     *
     * @param components The components that define the Big Matrix.
     * @throws ArithmeticException When the number of components that define the Big Matrix is not a perfect square.
     */
    public BigMatrix(BigDecimal... components) throws ArithmeticException {
        super();
        setComponents(components);
        
        if (!MathUtility.isSquare(components.length)) {
            throw new ArithmeticException(getErrorHandler().componentLengthNotSquareErrorMessage(components));
        }
    }
    
    /**
     * The constructor for a Big Matrix from double components.
     *
     * @param components The double components that define the Big Matrix.
     * @see #BigMatrix(BigDecimal...)
     */
    public BigMatrix(double... components) {
        this(Arrays.stream(components)
                .mapToObj(BigDecimal::valueOf).toArray(BigDecimal[]::new));
    }
    
    /**
     * The constructor for a Big Matrix from string components.
     *
     * @param components The string components that define the Big Matrix.
     * @throws NumberFormatException If the string components provided are not numbers.
     * @see #BigMatrix(BigDecimal...)
     */
    public BigMatrix(String... components) throws NumberFormatException {
        this(Arrays.stream(components)
                .map(BigDecimal::new).toArray(BigDecimal[]::new));
    }
    
    /**
     * The constructor for a Big Matrix from a list of components.
     *
     * @param components The components that define the Big Matrix, as a list.
     * @param <T>        The Number type of the components.
     * @throws ArithmeticException When the length of the list of components that define the Big Matrix is not a perfect square.
     * @see #BigMatrix(BigDecimal...)
     */
    public <T extends Number> BigMatrix(List<T> components) throws ArithmeticException {
        this(components.stream()
                .map(e -> (e instanceof BigDecimal) ? (BigDecimal) e : BigDecimal.valueOf(e.doubleValue()))
                .toArray(BigDecimal[]::new));
    }
    
    /**
     * The constructor for a Big Matrix from another Big Matrix.
     *
     * @param matrix The Big Matrix.
     * @see #BigMatrix(BigDecimal...)
     */
    public BigMatrix(BigMatrix matrix) {
        this(matrix.getRawComponents());
        setMathContext(matrix.getMathContext());
    }
    
    /**
     * The constructor for a Big Matrix from a Matrix.
     *
     * @param matrix The Matrix.
     * @see #BigMatrix(BigDecimal...)
     */
    public BigMatrix(Matrix matrix) {
        this(Arrays.stream(matrix.getRawComponents())
                .map(BigDecimal::new).toArray(BigDecimal[]::new));
    }
    
    /**
     * The constructor for a Big Matrix of a certain dimensionality.
     *
     * @param dim The dimensionality of the Big Matrix.
     * @see #BigMatrix(BigDecimal...)
     */
    public BigMatrix(int dim) {
        this(Collections.nCopies(dim * dim, BigDecimal.ZERO)
                .toArray(BigDecimal[]::new));
    }
    
    /**
     * The constructor for an empty Big Matrix.
     *
     * @see #BigMatrix(int)
     */
    public BigMatrix() {
        this(0);
    }
    
    
    //Methods
    
    /**
     * Returns a string that represents the Big Matrix.
     *
     * @return A string that represents the Big Matrix.
     * @see MatrixInterface#matrixString()
     */
    @Override
    public String toString() {
        return MatrixInterface.super.matrixString();
    }
    
    /**
     * Creates a cloned copy of the Big Matrix.
     *
     * @return The cloned Big Matrix.
     * @see #BigMatrix(BigMatrix)
     */
    @Override
    public BigMatrix cloned() {
        BigMatrix clone = new BigMatrix(this);
        copyMeta(clone);
        return clone;
    }
    
    /**
     * Creates an empty copy of the Big Matrix.
     *
     * @return The empty copy of the Big Matrix.
     * @see #BigMatrix(int)
     */
    @Override
    public BigMatrix emptyCopy() {
        return new BigMatrix(getDimensionality());
    }
    
    /**
     * Creates a new Vector instance for the Big Matrix.
     *
     * @return The new Vector.
     * @see BigVector#BigVector(int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public BigVector newVector() {
        return new BigVector(getDimensionality());
    }
    
    /**
     * Creates a new Big Matrix instance of the specified dimensionality.
     *
     * @param dim The dimensionality of the new Big Matrix.
     * @return The new Big Matrix.
     * @see #createInstance(int)
     */
    @Override
    public BigMatrix createNewInstance(int dim) {
        return createInstance(Math.max(dim, 0));
    }
    
    /**
     * Calculates the product of this Big Matrix and another Big Matrix.
     *
     * @param other The other Big Matrix.
     * @return The Big Matrix produced as a result of the multiplication.
     * @throws ArithmeticException When the two Matrices do not have the same dimensionality.
     * @see MatrixInterface#times(MatrixInterface)
     */
    @Override
    public BigMatrix times(BigMatrix other) throws ArithmeticException {
        return MatrixInterface.super.times(other);
    }
    
    /**
     * Resizes the Big Matrix.
     *
     * @param newDim The new dimensionality of the Big Matrix.
     * @see MatrixInterface#redim(int)
     */
    @Override
    public void redim(int newDim) {
        MatrixInterface.super.redim(newDim);
    }
    
    /**
     * Calculates the Big Matrix's length from its dimensionality.
     *
     * @param dim The dimensionality of the Big Matrix.
     * @return The Big Matrix's length.
     * @see MatrixInterface#dimensionalityToLength(int)
     */
    @Override
    public int dimensionalityToLength(int dim) {
        return MatrixInterface.super.dimensionalityToLength(dim);
    }
    
    /**
     * Calculates the Big Matrix's dimensionality from its length.
     *
     * @param length The length of the Big Matrix.
     * @return The Big Matrix's dimensionality.
     * @see MatrixInterface#lengthToDimensionality(int)
     */
    @Override
    public int lengthToDimensionality(int length) {
        return MatrixInterface.super.lengthToDimensionality(length);
    }
    
    
    //Getters
    
    /**
     * Returns the name of the type of Component.
     *
     * @return The name of the type of Component.
     */
    @Override
    public String getName() {
        return "Big Matrix";
    }
    
    
    //Functions
    
    /**
     * Creates a new Big Matrix instance of the specified dimensionality.
     *
     * @param dim The dimensionality of the new Big Matrix.
     * @return The new Big Matrix.
     * @see #BigMatrix(int)
     */
    public static BigMatrix createInstance(int dim) {
        return new BigMatrix(Math.max(dim, 0));
    }
    
    /**
     * Creates an identity Big Matrix of a certain dimensionality.
     *
     * @param dim The dimensionality of the identity Big Matrix.
     * @return The identity Big Matrix.
     * @see MatrixInterface#identity(int, Class)
     */
    public static BigMatrix identity(int dim) {
        return MatrixInterface.identity(dim, BigMatrix.class);
    }
    
    /**
     * Creates an origin Big Matrix of a certain dimensionality.
     *
     * @param dim The dimensionality of the origin Big Matrix.
     * @return The origin Big Matrix.
     * @see MatrixInterface#origin(int, Class)
     */
    public static BigMatrix origin(int dim) {
        return MatrixInterface.origin(dim, BigMatrix.class);
    }
    
    /**
     * Creates a sign chart Big Matrix of a certain dimensionality.
     *
     * @param dim The dimensionality of the sign chart Big Matrix.
     * @return The sign chart Big Matrix.
     * @see MatrixInterface#signChart(int, Class)
     */
    public static BigMatrix signChart(int dim) {
        return MatrixInterface.signChart(dim, BigMatrix.class);
    }
    
}
