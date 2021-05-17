/*
 * File:    IntMatrix.java
 * Package: commons.math.component.matrix
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.component.matrix;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import commons.math.MathUtility;
import commons.math.component.IntComponent;
import commons.math.component.vector.IntVector;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the base properties of an Integer Matrix.
 */
public class IntMatrix extends IntComponent<IntMatrix> implements MatrixInterface<Integer, IntMatrix> {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(IntMatrix.class);
    
    
    //Constructors
    
    /**
     * The constructor for an Integer Matrix from components.
     *
     * @param components The components that define the Integer Matrix.
     * @throws ArithmeticException When the number of components that define the Integer Matrix is not a perfect square.
     */
    public IntMatrix(int... components) throws ArithmeticException {
        super();
        setComponents(ArrayUtils.toObject(components));
        
        if (!MathUtility.isSquare(components.length)) {
            throw new ArithmeticException(getErrorHandler().componentLengthNotSquareErrorMessage(ArrayUtils.toObject(components)));
        }
    }
    
    /**
     * The constructor for an Integer Matrix from a list of components.
     *
     * @param components The components that define the Integer Matrix, as a list.
     * @param <T>        The Number type of the components.
     * @throws ArithmeticException When the length of the list of components that define the Integer Matrix is not a perfect square.
     * @see #IntMatrix(int...)
     */
    public <T extends Number> IntMatrix(List<T> components) throws ArithmeticException {
        this(components.stream()
                .mapToInt(Number::intValue).toArray());
    }
    
    /**
     * The constructor for an Integer Matrix from another Integer Matrix.
     *
     * @param matrix The Integer Matrix.
     * @see #IntMatrix(int...)
     */
    public IntMatrix(IntMatrix matrix) {
        this(Arrays.stream(matrix.getRawComponents())
                .mapToInt(e -> e).toArray());
    }
    
    /**
     * The constructor for an Integer Matrix from a Matrix.
     *
     * @param matrix The Matrix.
     * @see #IntMatrix(int...)
     */
    public IntMatrix(Matrix matrix) {
        this(Arrays.stream(matrix.getRawComponents())
                .mapToInt(Double::intValue).toArray());
    }
    
    /**
     * The constructor for an Integer Matrix of a certain dimensionality.
     *
     * @param dim The dimensionality of the Integer Matrix.
     * @see #IntMatrix(int...)
     */
    public IntMatrix(int dim) {
        this(Collections.nCopies(dim * dim, 0).stream()
                .mapToInt(e -> e).toArray());
    }
    
    /**
     * The constructor for an empty Integer Matrix.
     *
     * @see #IntMatrix(int)
     */
    public IntMatrix() {
        this(0);
    }
    
    
    //Methods
    
    /**
     * Returns a string that represents the Integer Matrix.
     *
     * @return A string that represents the Integer Matrix.
     * @see MatrixInterface#matrixString()
     */
    @Override
    public String toString() {
        return MatrixInterface.super.matrixString();
    }
    
    /**
     * Creates a cloned copy of the Integer Matrix.
     *
     * @return The cloned Integer Matrix.
     * @see #IntMatrix(IntMatrix)
     */
    @Override
    public IntMatrix cloned() {
        IntMatrix clone = new IntMatrix(this);
        copyMeta(clone);
        return clone;
    }
    
    /**
     * Creates an empty copy of the Integer Matrix.
     *
     * @return The empty copy of the Integer Matrix.
     * @see #IntMatrix(int)
     */
    @Override
    public IntMatrix emptyCopy() {
        return new IntMatrix(getDimensionality());
    }
    
    /**
     * Creates a new Vector instance for the Integer Matrix.
     *
     * @return The new Vector.
     * @see IntVector#IntVector(int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public IntVector newVector() {
        return new IntVector(getDimensionality());
    }
    
    /**
     * Creates a new Integer Matrix instance of the specified dimensionality.
     *
     * @param dim The dimensionality of the new Integer Matrix.
     * @return The new Integer Matrix.
     * @see #createInstance(int)
     */
    @Override
    public IntMatrix createNewInstance(int dim) {
        return createInstance(Math.max(dim, 0));
    }
    
    /**
     * Calculates the product of this Integer Matrix and another Integer Matrix.
     *
     * @param other The other Integer Matrix.
     * @return The Integer Matrix produced as a result of the multiplication.
     * @throws ArithmeticException When the two Matrices do not have the same dimensionality.
     * @see MatrixInterface#times(MatrixInterface)
     */
    @Override
    public IntMatrix times(IntMatrix other) throws ArithmeticException {
        return MatrixInterface.super.times(other);
    }
    
    /**
     * Resizes the Integer Matrix.
     *
     * @param newDim The new dimensionality of the Integer Matrix.
     * @see MatrixInterface#redim(int)
     */
    @Override
    public void redim(int newDim) {
        MatrixInterface.super.redim(newDim);
    }
    
    /**
     * Calculates the Integer Matrix's length from its dimensionality.
     *
     * @param dim The dimensionality of the Integer Matrix.
     * @return The Integer Matrix's length.
     * @see MatrixInterface#dimensionalityToLength(int)
     */
    @Override
    public int dimensionalityToLength(int dim) {
        return MatrixInterface.super.dimensionalityToLength(dim);
    }
    
    /**
     * Calculates the Integer Matrix's dimensionality from its length.
     *
     * @param length The length of the Integer Matrix.
     * @return The Integer Matrix's dimensionality.
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
        return "Integer Matrix";
    }
    
    
    //Functions
    
    /**
     * Creates a new Integer Matrix instance of the specified dimensionality.
     *
     * @param dim The dimensionality of the new Integer Matrix.
     * @return The new Integer Matrix.
     * @see #IntMatrix(int)
     */
    public static IntMatrix createInstance(int dim) {
        return new IntMatrix(Math.max(dim, 0));
    }
    
    /**
     * Creates an identity Integer Matrix of a certain dimensionality.
     *
     * @param dim The dimensionality of the identity Integer Matrix.
     * @return The identity Integer Matrix.
     * @see MatrixInterface#identity(int, Class)
     */
    public static IntMatrix identity(int dim) {
        return MatrixInterface.identity(dim, IntMatrix.class);
    }
    
    /**
     * Creates an origin Integer Matrix of a certain dimensionality.
     *
     * @param dim The dimensionality of the origin Integer Matrix.
     * @return The origin Integer Matrix.
     * @see MatrixInterface#origin(int, Class)
     */
    public static IntMatrix origin(int dim) {
        return MatrixInterface.origin(dim, IntMatrix.class);
    }
    
    /**
     * Creates a sign chart Integer Matrix of a certain dimensionality.
     *
     * @param dim The dimensionality of the sign chart Integer Matrix.
     * @return The sign chart Integer Matrix.
     * @see MatrixInterface#signChart(int, Class)
     */
    public static IntMatrix signChart(int dim) {
        return MatrixInterface.signChart(dim, IntMatrix.class);
    }
    
}
