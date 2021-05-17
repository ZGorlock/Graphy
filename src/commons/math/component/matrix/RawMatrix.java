/*
 * File:    RawMatrix.java
 * Package: commons.math.component.matrix
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.component.matrix;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import commons.math.MathUtility;
import commons.math.component.RawComponent;
import commons.math.component.vector.IntVector;
import commons.math.component.vector.RawVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the base properties of a Raw Matrix.
 */
public class RawMatrix extends RawComponent<RawMatrix> implements MatrixInterface<Number, RawMatrix> {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(RawMatrix.class);
    
    
    //Constructors
    
    /**
     * The constructor for a Raw Matrix from components.
     *
     * @param components The components that define the Raw Matrix.
     * @throws ArithmeticException When the number of components that define the Raw Matrix is not a perfect square.
     */
    public RawMatrix(Number... components) throws ArithmeticException {
        super();
        setComponents(components);
        
        if (!MathUtility.isSquare(components.length)) {
            throw new ArithmeticException(getErrorHandler().componentLengthNotSquareErrorMessage(components));
        }
    }
    
    /**
     * The constructor for a Raw Matrix from a list of components.
     *
     * @param components The components that define the Raw Matrix, as a list.
     * @param <T>        The Number type of the components.
     * @throws ArithmeticException When the length of the list of components that define the Raw Matrix is not a perfect square.
     * @see #RawMatrix(Number...)
     */
    public <T extends Number> RawMatrix(List<T> components) throws ArithmeticException {
        this(components.stream()
                .map(Number.class::cast).toArray(Number[]::new));
    }
    
    /**
     * The constructor for a Raw Matrix from another Raw Matrix.
     *
     * @param matrix The Raw Matrix.
     * @see #RawMatrix(Number...)
     */
    public RawMatrix(RawMatrix matrix) {
        this(Arrays.stream(matrix.getRawComponents())
                .toArray(Number[]::new));
    }
    
    /**
     * The constructor for a Raw Matrix from a Matrix.
     *
     * @param matrix The Matrix.
     * @see #RawMatrix(Number...)
     */
    public RawMatrix(Matrix matrix) {
        this(Arrays.stream(matrix.getRawComponents())
                .map(Number.class::cast).toArray(Number[]::new));
    }
    
    /**
     * The constructor for a Raw Matrix of a certain dimensionality.
     *
     * @param dim The dimensionality of the Raw Matrix.
     * @see #RawMatrix(Number...)
     */
    public RawMatrix(int dim) {
        this(Collections.nCopies(dim * dim, 0.0).stream()
                .map(Number.class::cast).toArray(Number[]::new));
    }
    
    /**
     * The constructor for an empty Raw Matrix.
     *
     * @see #RawMatrix(int)
     */
    public RawMatrix() {
        this(0);
    }
    
    
    //Methods
    
    /**
     * Returns a string that represents the Raw Matrix.
     *
     * @return A string that represents the Raw Matrix.
     * @see MatrixInterface#matrixString()
     */
    @Override
    public String toString() {
        return MatrixInterface.super.matrixString();
    }
    
    /**
     * Creates a cloned copy of the Raw Matrix.
     *
     * @return The cloned Raw Matrix.
     * @see #RawMatrix(RawMatrix)
     */
    @Override
    public RawMatrix cloned() {
        RawMatrix clone = new RawMatrix(this);
        copyMeta(clone);
        return clone;
    }
    
    /**
     * Creates an empty copy of the Raw Matrix.
     *
     * @return The empty copy of the Raw Matrix.
     * @see #RawMatrix(int)
     */
    @Override
    public RawMatrix emptyCopy() {
        return new RawMatrix(getDimensionality());
    }
    
    /**
     * Creates a new Vector instance for the Raw Matrix.
     *
     * @return The new Vector.
     * @see IntVector#IntVector(int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public RawVector newVector() {
        return new RawVector(getDimensionality());
    }
    
    /**
     * Creates a new Raw Matrix instance of the specified dimensionality.
     *
     * @param dim The dimensionality of the new Raw Matrix.
     * @return The new Raw Matrix.
     * @see #createInstance(int)
     */
    @Override
    public RawMatrix createNewInstance(int dim) {
        return createInstance(Math.max(dim, 0));
    }
    
    /**
     * Calculates the product of this Raw Matrix and another Raw Matrix.
     *
     * @param other The other Raw Matrix.
     * @return The Raw Matrix produced as a result of the multiplication.
     * @throws ArithmeticException When the two Matrices do not have the same dimensionality.
     * @see MatrixInterface#times(MatrixInterface)
     */
    @Override
    public RawMatrix times(RawMatrix other) throws ArithmeticException {
        return MatrixInterface.super.times(other);
    }
    
    /**
     * Resizes the Raw Matrix.
     *
     * @param newDim The new dimensionality of the Raw Matrix.
     * @see MatrixInterface#redim(int)
     */
    @Override
    public void redim(int newDim) {
        MatrixInterface.super.redim(newDim);
    }
    
    /**
     * Calculates the Raw Matrix's length from its dimensionality.
     *
     * @param dim The dimensionality of the Raw Matrix.
     * @return The Raw Matrix's length.
     * @see MatrixInterface#dimensionalityToLength(int)
     */
    @Override
    public int dimensionalityToLength(int dim) {
        return MatrixInterface.super.dimensionalityToLength(dim);
    }
    
    /**
     * Calculates the Raw Matrix's dimensionality from its length.
     *
     * @param length The length of the Raw Matrix.
     * @return The Raw Matrix's dimensionality.
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
        return "Raw Matrix";
    }
    
    
    //Functions
    
    /**
     * Creates a new Raw Matrix instance of the specified dimensionality.
     *
     * @param dim The dimensionality of the new Raw Matrix.
     * @return The new Raw Matrix.
     * @see #RawMatrix(int)
     */
    public static RawMatrix createInstance(int dim) {
        return new RawMatrix(Math.max(dim, 0));
    }
    
    /**
     * Creates an identity Raw Matrix of a certain dimensionality.
     *
     * @param dim The dimensionality of the identity Raw Matrix.
     * @return The identity Raw Matrix.
     * @see MatrixInterface#identity(int, Class)
     */
    public static RawMatrix identity(int dim) {
        return MatrixInterface.identity(dim, RawMatrix.class);
    }
    
    /**
     * Creates an origin Raw Matrix of a certain dimensionality.
     *
     * @param dim The dimensionality of the origin Raw Matrix.
     * @return The origin Raw Matrix.
     * @see MatrixInterface#origin(int, Class)
     */
    public static RawMatrix origin(int dim) {
        return MatrixInterface.origin(dim, RawMatrix.class);
    }
    
    /**
     * Creates a sign chart Raw Matrix of a certain dimensionality.
     *
     * @param dim The dimensionality of the sign chart Raw Matrix.
     * @return The sign chart Raw Matrix.
     * @see MatrixInterface#signChart(int, Class)
     */
    public static RawMatrix signChart(int dim) {
        return MatrixInterface.signChart(dim, RawMatrix.class);
    }
    
}
