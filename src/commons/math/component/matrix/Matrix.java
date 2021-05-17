/*
 * File:    Matrix.java
 * Package: commons.math.component.matrix
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.component.matrix;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import commons.math.MathUtility;
import commons.math.component.Component;
import commons.math.component.vector.Vector;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the base properties of a Matrix.
 */
public class Matrix extends Component<Matrix> implements MatrixInterface<Double, Matrix> {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(Matrix.class);
    
    
    //Constructors
    
    /**
     * The constructor for a Matrix from components.
     *
     * @param components The components that define the Matrix.
     * @throws ArithmeticException When the number of components that define the Matrix is not a perfect square.
     */
    public Matrix(double... components) throws ArithmeticException {
        super();
        setComponents(ArrayUtils.toObject(components));
        
        if (!MathUtility.isSquare(components.length)) {
            throw new ArithmeticException(getErrorHandler().componentLengthNotSquareErrorMessage(ArrayUtils.toObject(components)));
        }
    }
    
    /**
     * The constructor for a Matrix from a list of components.
     *
     * @param components The components that define the Matrix, as a list.
     * @param <T>        The Number type of the components.
     * @throws ArithmeticException When the length of the list of components that define the Matrix is not a perfect square.
     * @see #Matrix(double...)
     */
    public <T extends Number> Matrix(List<T> components) throws ArithmeticException {
        this(components.stream()
                .mapToDouble(Number::doubleValue).toArray());
    }
    
    /**
     * The constructor for a Matrix from another Matrix.
     *
     * @param matrix The Matrix.
     * @see #Matrix(double...)
     */
    public Matrix(Matrix matrix) {
        this(Arrays.stream(matrix.getRawComponents())
                .mapToDouble(e -> e).toArray());
    }
    
    /**
     * The constructor for a Matrix of a certain dimensionality.
     *
     * @param dim The dimensionality of the Matrix.
     * @see #Matrix(double...)
     */
    public Matrix(int dim) {
        this(Collections.nCopies(dim * dim, 0.0).stream()
                .mapToDouble(e -> e).toArray());
    }
    
    /**
     * The constructor for an empty Matrix.
     *
     * @see #Matrix(int)
     */
    public Matrix() {
        this(0);
    }
    
    
    //Methods
    
    /**
     * Returns a string that represents the Matrix.
     *
     * @return A string that represents the Matrix.
     * @see MatrixInterface#matrixString()
     */
    @Override
    public String toString() {
        return MatrixInterface.super.matrixString();
    }
    
    /**
     * Creates a cloned copy of the Matrix.
     *
     * @return The cloned Matrix.
     * @see #Matrix(Matrix)
     */
    @Override
    public Matrix cloned() {
        Matrix clone = new Matrix(this);
        copyMeta(clone);
        return clone;
    }
    
    /**
     * Creates an empty copy of the Matrix.
     *
     * @return The empty copy of the Matrix.
     * @see #Matrix(int)
     */
    @Override
    public Matrix emptyCopy() {
        return new Matrix(getDimensionality());
    }
    
    /**
     * Creates a new Vector instance for the Matrix.
     *
     * @return The new Vector.
     * @see Vector#Vector(int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Vector newVector() {
        return new Vector(getDimensionality());
    }
    
    /**
     * Creates a new Matrix instance of the specified dimensionality.
     *
     * @param dim The dimensionality of the new Matrix.
     * @return The new Matrix.
     * @see #createInstance(int)
     */
    @Override
    public Matrix createNewInstance(int dim) {
        return createInstance(Math.max(dim, 0));
    }
    
    /**
     * Calculates the product of this Matrix and another Matrix.
     *
     * @param other The other Matrix.
     * @return The Matrix produced as a result of the multiplication.
     * @throws ArithmeticException When the two Matrices do not have the same dimensionality.
     * @see MatrixInterface#times(MatrixInterface)
     */
    @Override
    public Matrix times(Matrix other) throws ArithmeticException {
        return MatrixInterface.super.times(other);
    }
    
    /**
     * Resizes the Matrix.
     *
     * @param newDim The new dimensionality of the Matrix.
     * @see MatrixInterface#redim(int)
     */
    @Override
    public void redim(int newDim) {
        MatrixInterface.super.redim(newDim);
    }
    
    /**
     * Calculates the Matrix's length from its dimensionality.
     *
     * @param dim The dimensionality of the Matrix.
     * @return The Matrix's length.
     * @see MatrixInterface#dimensionalityToLength(int)
     */
    @Override
    public int dimensionalityToLength(int dim) {
        return MatrixInterface.super.dimensionalityToLength(dim);
    }
    
    /**
     * Calculates the Matrix's dimensionality from its length.
     *
     * @param length The length of the Matrix.
     * @return The Matrix's dimensionality.
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
        return "Matrix";
    }
    
    
    //Functions
    
    /**
     * Creates a new Matrix instance of the specified dimensionality.
     *
     * @param dim The dimensionality of the new Matrix.
     * @return The new Matrix.
     * @see #Matrix(int)
     */
    public static Matrix createInstance(int dim) {
        return new Matrix(Math.max(dim, 0));
    }
    
    /**
     * Creates an identity Matrix of a certain dimensionality.
     *
     * @param dim The dimensionality of the identity Matrix.
     * @return The identity Matrix.
     * @see MatrixInterface#identity(int, Class)
     */
    public static Matrix identity(int dim) {
        return MatrixInterface.identity(dim, Matrix.class);
    }
    
    /**
     * Creates an origin Matrix of a certain dimensionality.
     *
     * @param dim The dimensionality of the origin Matrix.
     * @return The origin Matrix.
     * @see MatrixInterface#origin(int, Class)
     */
    public static Matrix origin(int dim) {
        return MatrixInterface.origin(dim, Matrix.class);
    }
    
    /**
     * Creates a sign chart Matrix of a certain dimensionality.
     *
     * @param dim The dimensionality of the sign chart Matrix.
     * @return The sign chart Matrix.
     * @see MatrixInterface#signChart(int, Class)
     */
    public static Matrix signChart(int dim) {
        return MatrixInterface.signChart(dim, Matrix.class);
    }
    
}
