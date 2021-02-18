/*
 * File:    Matrix.java
 * Package: commons.math.matrix
 * Author:  Zachary Gill
 */

package commons.math.matrix;

import java.util.stream.Collectors;

import commons.list.ListUtility;
import commons.math.BoundUtility;
import commons.math.vector.Vector;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines a Matrix.
 */
public class Matrix {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(Matrix.class);
    
    
    //Fields
    
    /**
     * The elements of the matrix.
     */
    public double[] values;
    
    
    //Constructors
    
    /**
     * The constructor for a Matrix.
     *
     * @param values The elements of the matrix.
     */
    public Matrix(double[] values) {
        this.values = values;
    }
    
    
    //Methods
    
    /**
     * Returns a string that represents the Matrix.
     *
     * @return A string that represents the Matrix.
     */
    @Override
    public String toString() {
        return ListUtility.split(ListUtility.toList(ArrayUtils.toObject(getValues())), getDimensionality()).stream()
                .map(e -> new Vector(e).toString())
                .collect(Collectors.joining(", ", "(", ")"));
    }
    
    /**
     * Determines if another Matrix is equal to this Matrix.
     *
     * @param o The other Matrix.
     * @return Whether the two Matrices are equal or not.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Matrix)) {
            return false;
        }
        Matrix matrix = (Matrix) o;
        
        if (!dimensionalityEqual(matrix)) {
            return false;
        }
        
        for (int v = 0; v < getValues().length; v++) {
            if (Math.abs(get(v) - matrix.get(v)) > 0.000000000001) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Determines if another Matrix's dimensionality is equal to this Matrix's dimensionality.
     *
     * @param matrix The other Matrix.
     * @return Whether the two Matrices' dimensionality is equal or not.
     */
    public boolean dimensionalityEqual(Matrix matrix) {
        return (getDimensionality() == matrix.getDimensionality());
    }
    
    
    //Getters
    
    /**
     * Returns the dimensionality of the Matrix.
     *
     * @return The dimensionality of the Matrix.
     */
    public int getDimensionality() {
        return (int) Math.sqrt(values.length);
    }
    
    /**
     * Returns the values of the Matrix.
     *
     * @return The values of the Matrix.
     */
    public double[] getValues() {
        return values;
    }
    
    /**
     * Returns a value of the Matrix.
     *
     * @param index The index of the value.
     * @return The value of the Matrix at the specified index.
     * @throws IndexOutOfBoundsException When the Matrix does not contain a component at the specified index.
     */
    public double get(int index) {
        if (!BoundUtility.inBounds(index, 0, values.length, true, false)) {
            throw new IndexOutOfBoundsException(valueIndexOutOfRangeError(this, index));
        }
        
        return values[index];
    }
    
    
    //Setters
    
    /**
     * Sets the value of a value of the Matrix.
     *
     * @param index The index of the value to set.
     * @param value The new value of the value.
     * @throws IndexOutOfBoundsException When the Matrix does not contain a value at the specified index.
     */
    public void set(int index, double value) throws IndexOutOfBoundsException {
        if (!BoundUtility.inBounds(index, 0, values.length, true, false)) {
            throw new IndexOutOfBoundsException(valueIndexOutOfRangeError(this, index));
        }
        
        values[index] = value;
    }
    
    
    //Functions
    
    /**
     * Returns the error message to display when two Matrices do not have the same dimensionality.
     *
     * @param matrix1 The first Matrix.
     * @param matrix2 The second Matrix.
     * @return The error message.
     */
    protected static String dimensionalityNotEqualErrorMessage(Matrix matrix1, Matrix matrix2) {
        return "The matrices: " + matrix1 + " and " + matrix2 + " do not have the same dimensionality.";
    }
    
    /**
     * Returns the error message to display when the component index of a MAtrix is out of range.
     *
     * @param matrix The Matrix.
     * @param index  The index of the component.
     * @return The error message.
     */
    protected static String valueIndexOutOfRangeError(Matrix matrix, int index) {
        return "The matrix: " + matrix + " does not have a value at index: " + index;
    }
    
}
