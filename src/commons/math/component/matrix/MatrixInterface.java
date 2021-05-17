/*
 * File:    MatrixInterface.java
 * Package: commons.math.component.matrix
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.component.matrix;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import commons.list.ListUtility;
import commons.math.BoundUtility;
import commons.math.component.ComponentInterface;
import commons.math.component.handler.error.ComponentErrorHandlerProvider;
import commons.math.component.vector.VectorInterface;
import commons.string.StringUtility;

/**
 * Defines an additional contract for Matrix classes.
 *
 * @param <T> The component type of the Matrix.
 * @param <I> The type of the Matrix.
 */
@SuppressWarnings("unchecked")
public interface MatrixInterface<T extends Number, I extends MatrixInterface<?, ?>> extends ComponentInterface<Number, I, T> {
    
    //Methods
    
    /**
     * Returns a string that represents the Matrix.
     *
     * @return A string that represents the Matrix.
     */
    default String matrixString() {
        return ListUtility.split(ListUtility.toList(getComponents()), getDimensionality()).stream()
                .map(e -> e.stream().map(c -> (c instanceof BigDecimal) ? ((BigDecimal) c).toPlainString() : c.toString())
                        .collect(Collectors.joining(", ", "<", ">")))
                .collect(Collectors.joining(", ", "[", "]"));
    }
    
    /**
     * Creates a new Vector instance for the Matrix.
     *
     * @return The new Vector.
     */
    <J extends VectorInterface<T, ?>> J newVector();
    
    /**
     * Converts a coordinate to an index in the Matrix.
     *
     * @param x The x coordinate in the Matrix.
     * @param y The y coordinate in the Matrix.
     * @return The corresponding index in the Matrix.
     */
    default int toIndex(int x, int y) {
        return (y * getWidth()) + x;
    }
    
    /**
     * Calculates the product of this Matrix and another Matrix.
     *
     * @param other The other Matrix.
     * @return The Matrix produced as a result of the multiplication.
     * @throws ArithmeticException When the two Matrices do not have the same dimensionality.
     */
    @Override
    default I times(I other) throws ArithmeticException {
        ComponentErrorHandlerProvider.assertDimensionalitySame(this, other);
        
        I result = emptyCopy();
        for (int row = 0; row < getHeight(); row++) {
            for (int col = 0; col < getWidth(); col++) {
                for (int d = 0; d < getDimensionality(); d++) {
                    result.getRawComponents()[toIndex(col, row)] = getHandler().add(result.getRawComponents()[toIndex(col, row)],
                            getHandler().multiply(getRawComponents()[toIndex(d, row)], other.getRawComponents()[toIndex(col, d)]));
                }
            }
        }
        copyMeta(result);
        return result;
    }
    
    /**
     * Calculates the product of this Matrix and a Vector.
     *
     * @param other The Vector.
     * @return The Vector produced as a result of the multiplication.
     * @throws ArithmeticException When the Matrix and the Vector do not have the same dimensionality.
     */
    default <J extends VectorInterface<T, ?>> J times(J other) throws ArithmeticException {
        ComponentErrorHandlerProvider.assertDimensionalitySame(this, other);
        
        J result = newVector();
        for (int row = 0; row < getHeight(); row++) {
            for (int col = 0; col < getWidth(); col++) {
                result.getRawComponents()[row] = getHandler().add(result.getRawComponents()[row],
                        getHandler().multiply(getRawComponents()[toIndex(col, row)], other.getRawComponents()[col]));
            }
        }
        return result;
    }
    
    /**
     * Calculates the result of this Matrix scaled by the components of another Matrix.
     *
     * @param other The scalar Matrix.
     * @return The Matrix produced as a result of the scaling.
     * @throws ArithmeticException When the two Matrices do not have the same dimensionality.
     */
    default I scale(I other) throws ArithmeticException {
        ComponentErrorHandlerProvider.assertDimensionalitySame(this, other);
        
        I result = emptyCopy();
        for (int c = 0; c < getLength(); c++) {
            result.getRawComponents()[c] = getHandler().multiply(getRawComponents()[c], other.getRawComponents()[c]);
        }
        copyMeta(result);
        return result;
    }
    
    /**
     * Calculates the determinant of the Matrix.
     *
     * @return The determinant of the Matrix.
     */
    default T determinant() {
        switch (getDimensionality()) {
            case 0:
                return (T) getHandler().zero();
            case 1:
                return (T) getRawComponents()[0];
            case 2:
                return (T) getHandler().subtract(
                        getHandler().multiply(getRawComponents()[0], getRawComponents()[3]),
                        getHandler().multiply(getRawComponents()[1], getRawComponents()[2]));
        }
        
        Number determinant = getHandler().zero();
        MatrixInterface<?, ?> subMatrix = isResizeable() ? createNewInstance(getDimensionality() - 1) : new RawMatrix(getDimensionality() - 1);
        Number[] subComponents = getHandler().array(dimensionalityToLength(getDimensionality() - 1));
        
        boolean cofactorSign = false;
        for (int h = 0; h < getHeight(); h++) {
            int subComponentIndex = 0;
            for (int row = 0; row < getHeight(); row++) {
                if (row == h) {
                    continue;
                }
                for (int col = 1; col < getWidth(); col++) {
                    subMatrix.getRawComponents()[subComponentIndex++] = getRawComponents()[toIndex(col, row)];
                }
            }
            
            determinant = getHandler().add(determinant,
                    getHandler().multiply(getHandler().valueOf(subMatrix.determinant()), getHandler().multiply(getRawComponents()[toIndex(0, h)], cofactorScalar(h))));
            cofactorSign ^= true;
        }
        
        return (T) determinant;
    }
    
    /**
     * Calculates the minor for a particular component in this Matrix.
     *
     * @param x The x coordinate of the component.
     * @param y The y coordinate of the component.
     * @return The minor for the specified component.
     * @throws IndexOutOfBoundsException When the Matrix does not contain a component at the specified coordinate.
     */
    default T minor(int x, int y) throws IndexOutOfBoundsException {
        ComponentErrorHandlerProvider.assertCoordinateInBounds(this, x, y);
        
        MatrixInterface<?, ?> subMatrix = isResizeable() ? createNewInstance(getDimensionality() - 1) : new RawMatrix(getDimensionality() - 1);
        int index = 0;
        for (int col = 0; col < getWidth(); col++) {
            for (int row = 0; row < getHeight(); row++) {
                if ((col == x) || (row == y)) {
                    continue;
                }
                subMatrix.getRawComponents()[index++] = getRawComponents()[toIndex(col, row)];
            }
        }
        return (T) getHandler().valueOf(subMatrix.determinant());
    }
    
    /**
     * Calculates the minor for a particular component in this Matrix.
     *
     * @param index The index of the component.
     * @return The minor for the specified component.
     * @throws IndexOutOfBoundsException When the Matrix does not contain a component at the specified index.
     * @see #minor(int, int)
     */
    default T minor(int index) throws IndexOutOfBoundsException {
        ComponentErrorHandlerProvider.assertIndexInBounds(this, index);
        
        return minor((index % getWidth()), (index / getWidth()));
    }
    
    /**
     * Calculates the Matrix of minors for this Matrix.
     *
     * @return The Matrix of minors.
     */
    default I minors() {
        I result = emptyCopy();
        for (int c = 0; c < getLength(); c++) {
            result.getRawComponents()[c] = minor(c);
        }
        copyMeta(result);
        return result;
    }
    
    /**
     * Returns the cofactor scalar for a particular component in the Matrix
     *
     * @param x The x coordinate of the component.
     * @param y The y coordinate of the component.
     * @return The cofactor scalar for the specified component.
     * @throws IndexOutOfBoundsException When the Matrix does not contain a component at the specified coordinate.
     */
    default T cofactorScalar(int x, int y) throws IndexOutOfBoundsException {
        ComponentErrorHandlerProvider.assertCoordinateInBounds(this, x, y);
        
        return (T) (((x % 2) == (y % 2)) ?
                    getHandler().one() : getHandler().negativeOne());
    }
    
    /**
     * Returns the cofactor scalar for a particular component in the Matrix
     *
     * @param index The index of the component.
     * @return The cofactor scalar for the specified component.
     * @throws IndexOutOfBoundsException When the Matrix does not contain a component at the specified index.
     * @see #cofactorScalar(int, int)
     */
    default T cofactorScalar(int index) throws IndexOutOfBoundsException {
        ComponentErrorHandlerProvider.assertIndexInBounds(this, index);
        
        return cofactorScalar((index % getWidth()), (index / getWidth()));
    }
    
    /**
     * Returns the cofactor Matrix for this Matrix.
     *
     * @return The cofactor Matrix.
     */
    default I cofactor() {
        I result = emptyCopy();
        for (int c = 0; c < result.getLength(); c++) {
            result.getRawComponents()[c] = getHandler().multiply(getRawComponents()[c], cofactorScalar(c));
        }
        copyMeta(result);
        return result;
    }
    
    /**
     * Calculates the transpose Matrix of this Matrix.
     *
     * @return The transpose Matrix.
     */
    default I transpose() {
        I result = emptyCopy();
        for (int col = 0; col < getWidth(); col++) {
            for (int row = 0; row < getHeight(); row++) {
                result.getRawComponents()[toIndex(col, row)] = getRawComponents()[toIndex(row, col)];
            }
        }
        copyMeta(result);
        return result;
    }
    
    /**
     * Calculates the adjugate Matrix of this Matrix.
     *
     * @return The adjugate Matrix.
     */
    default I adjoint() {
        return (I) minors().cofactor().transpose();
    }
    
    /**
     * Calculates the inverse Matrix of this Matrix.
     *
     * @return The inverse Matrix.
     * @throws ArithmeticException If the Matrix cannot be inverted.
     */
    default I inverse() throws ArithmeticException {
        T determinant = determinant();
        if (getHandler().isZero(determinant)) {
            throw new ArithmeticException("The " + getName() + ": " + toString() + " cannot be inverted");
        }
        return (I) adjoint().scale(getHandler().reciprocal(determinant()));
    }
    
    /**
     * Solves the Matrix as a system of equations.
     *
     * @param solutionVector The solution Vector.
     * @return The solution to the system of equations.
     * @throws ArithmeticException When the solution Vector and the Matrix do not have the same dimensionality, or when the Matrix cannot be inverted.
     */
    @SuppressWarnings({"rawtypes", "RedundantCast"})
    default <J extends VectorInterface> J solveSystem(J solutionVector) throws ArithmeticException {
        return (J) inverse().times(solutionVector);
    }
    
    /**
     * Transforms a Vector using the Matrix.
     *
     * @param vector The Vector to transform.
     * @return The transformed Vector.
     * @throws ArithmeticException When the Vector and the Matrix do not have the same dimensionality.
     */
    @SuppressWarnings({"rawtypes", "RedundantCast"})
    default <J extends VectorInterface> J transform(J vector) throws ArithmeticException {
        return (J) transpose().times(vector);
    }
    
    /**
     * Resizes the Matrix.
     *
     * @param newDim The new dimensionality of the Matrix.
     */
    @Override
    default void redim(int newDim) {
        if (!isResizeable() || (newDim == getDimensionality())) {
            return;
        } else if (newDim <= 0) {
            setComponents((T[]) getHandler().array(0));
            return;
        }
        
        if (newDim < getDimensionality()) {
            setComponents((T[]) subMatrix(0, 0, newDim).getRawComponents());
            
        } else {
            Number[] newComponents = getHandler().array(dimensionalityToLength(newDim));
            Arrays.fill(newComponents, getHandler().zero());
            for (int col = 0; col < getWidth(); col++) {
                for (int row = 0; row < getHeight(); row++) {
                    newComponents[(row * newDim) + col] = getRawComponents()[toIndex(col, row)];
                }
            }
            setComponents((T[]) newComponents);
        }
    }
    
    /**
     * Creates a sub-Matrix from a subset of the components of this Matrix.
     *
     * @param x1 The upper left x coordinate of the sub-Matrix, inclusive.
     * @param y1 The upper left y coordinate of the sub-Matrix, inclusive.
     * @param x2 The lower right x coordinate of the sub-Matrix, inclusive.
     * @param y2 The lower right y coordinate of the sub-Matrix, inclusive.
     * @return The sub-Matrix.
     * @throws IndexOutOfBoundsException When the component coordinate range is out of bounds of the Matrix.
     * @throws ArithmeticException       When the component coordinate rage is not a perfect square, or the Matrix is not resizable.
     */
    default I subMatrix(int x1, int y1, int x2, int y2) throws IndexOutOfBoundsException, ArithmeticException {
        if (((x2 - x1) < 0) || ((y2 - y1) < 0) ||
                !BoundUtility.inBounds(toIndex(x1, y1), 0, getLength(), true, false) ||
                !BoundUtility.inBounds(toIndex(x2, y2), 0, getLength(), true, false)) {
            throw new IndexOutOfBoundsException(getErrorHandler().componentCoordinateRangeOutOfBoundsErrorMessage(this, x1, y1, x2, y2));
        }
        
        Number[] newComponents = getHandler().array((x2 - x1 + 1) * (y2 - y1 + 1));
        int newIndex = 0;
        for (int row = y1; row <= y2; row++) {
            for (int col = x1; col <= x2; col++) {
                newComponents[newIndex++] = getRawComponents()[toIndex(col, row)];
            }
        }
        
        if ((x2 - x1) != (y2 - y1)) {
            throw new ArithmeticException(getErrorHandler().componentLengthNotSquareErrorMessage(newComponents));
        }
        int dim = (x2 - x1) + 1;
        if (!isResizeable() && (getDimensionality() != dim)) {
            throw new ArithmeticException(getErrorHandler().dimensionalityNotEqualErrorMessage(this, getDimensionality()));
        }
        
        I subMatrix = createNewInstance(x2 - x1 + 1);
        System.arraycopy(newComponents, 0, subMatrix.getRawComponents(), 0, subMatrix.getLength());
        return subMatrix;
    }
    
    /**
     * Creates a sub-Matrix from a subset of the components of this Matrix.
     *
     * @param x1     The upper left x coordinate of the sub-Matrix, inclusive.
     * @param y1     The upper left y coordinate of the sub-Matrix, inclusive.
     * @param subDim The dimensionality of the sub-Matrix.
     * @return The sub-Matrix.
     * @throws IndexOutOfBoundsException When the component coordinate range is out of bounds of the Matrix.
     * @throws ArithmeticException       When the component coordinate rage is not a perfect square.
     * @see #subMatrix(int, int, int, int)
     */
    default I subMatrix(int x1, int y1, int subDim) throws IndexOutOfBoundsException, ArithmeticException {
        return subMatrix(x1, y1, x1 + dimensionalityToWidth(subDim) - 1, y1 + dimensionalityToHeight(subDim) - 1);
    }
    
    /**
     * Calculates the Matrix's length from its dimensionality.
     *
     * @param dim The dimensionality of the Matrix.
     * @return The Matrix's length.
     */
    @Override
    default int dimensionalityToLength(int dim) {
        return (dim <= 0) ? 0 :
               (dim * dim);
    }
    
    /**
     * Calculates the Matrix's width from its dimensionality.
     *
     * @param dim The dimensionality of the Matrix.
     * @return The Matrix's width.
     */
    default int dimensionalityToWidth(int dim) {
        return Math.max(dim, 0);
    }
    
    /**
     * Calculates the Matrix's width from its dimensionality.
     *
     * @return The Matrix's width.
     * @see #dimensionalityToWidth(int)
     */
    default int dimensionalityToWidth() {
        return dimensionalityToWidth(getDimensionality());
    }
    
    /**
     * Calculates the Matrix's height from its dimensionality.
     *
     * @param dim The dimensionality of the Matrix.
     * @return The Matrix's height.
     */
    default int dimensionalityToHeight(int dim) {
        return Math.max(dim, 0);
    }
    
    /**
     * Calculates the Matrix's height from its dimensionality.
     *
     * @return The Matrix's height.
     * @see #dimensionalityToHeight(int)
     */
    default int dimensionalityToHeight() {
        return dimensionalityToHeight(getDimensionality());
    }
    
    /**
     * Calculates the Matrix's dimensionality from its length.
     *
     * @param length The length of the Matrix.
     * @return The Matrix's dimensionality.
     */
    @Override
    default int lengthToDimensionality(int length) {
        return (length <= 0) ? 0 :
               (int) Math.sqrt(length);
    }
    
    /**
     * Returns a formatted printable Matrix string.
     *
     * @return A formatted printable Matrix string.
     */
    default String prettyPrint() {
        List<List<String>> elements = ListUtility.split(Arrays.stream(getComponents())
                .map(e -> (e instanceof BigDecimal) ? ((BigDecimal) e).toPlainString() : e.toString())
                .collect(Collectors.toList()), getWidth());
        int[] elementWidths = new int[getWidth()];
        for (List<String> elementRow : elements) {
            for (int c = 0; c < elementRow.size(); c++) {
                elementWidths[c] = Math.max(elementWidths[c], elementRow.get(c).length() - (elementRow.get(c).startsWith("-") ? 1 : 0));
            }
        }
        
        switch (elements.size()) {
            case 0:
                return "[]";
            case 1:
                return elements.get(0).stream().collect(Collectors.joining("  ", "[  ", "  ]"));
            
            default:
                StringBuilder print = new StringBuilder();
                for (int row = 0; row < getHeight(); row++) {
                    print.append((row == 0) ? '┌' : (System.lineSeparator() + (row == (getHeight() - 1) ? '└' : '│'))).append(" ");
                    for (int col = 0; col < getWidth(); col++) {
                        String element = elements.get(row).get(col);
                        print.append((element.startsWith("-") ? "" : ' ')).append(element).append(StringUtility.spaces(elementWidths[col] + 2 - element.length() + (element.startsWith("-") ? 1 : 0)));
                    }
                    print.append((row == 0) ? '┐' : (row == (getHeight() - 1) ? '┘' : '│'));
                }
                return print.toString();
        }
    }
    
    
    //Getters
    
    /**
     * Returns a raw component of the Matrix.
     *
     * @param x The x coordinate of the component.
     * @param y The y coordinate of the component.
     * @return The raw component of the Matrix at the specified coordinate.
     * @throws IndexOutOfBoundsException When the Matrix does not contain a component at the specified coordinate.
     * @see #getRaw(int)
     */
    default T getRaw(int x, int y) throws IndexOutOfBoundsException {
        try {
            return (T) getRaw(toIndex(x, y));
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException(getErrorHandler().componentCoordinateOutOfBoundsErrorMessage(this, x, y));
        }
    }
    
    /**
     * Returns a component of the Matrix.
     *
     * @param x The x coordinate of the component.
     * @param y The y coordinate of the component.
     * @return The component of the Matrix at the specified coordinate.
     * @throws IndexOutOfBoundsException When the Matrix does not contain a component at the specified coordinate.
     * @see #get(int)
     */
    default T get(int x, int y) throws IndexOutOfBoundsException {
        try {
            return (T) get(toIndex(x, y));
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException(getErrorHandler().componentCoordinateOutOfBoundsErrorMessage(this, x, y));
        }
    }
    
    /**
     * Returns the height of the Matrix.
     *
     * @return The height of the Matrix.
     */
    default int getHeight() {
        return getDimensionality();
    }
    
    /**
     * Returns the width of the Matrix.
     *
     * @return The width of the Matrix.
     */
    default int getWidth() {
        return getDimensionality();
    }
    
    /**
     * Returns the plural name of the type of the Component.
     *
     * @return The plural name of the type of the Component.
     */
    @Override
    default String getNamePlural() {
        return StringUtility.rShear(getName(), 1) + "ces";
    }
    
    
    //Setters
    
    /**
     * Sets the value of a component of the Matrix.
     *
     * @param x     The x coordinate of the component to set.
     * @param y     The y coordinate of the component to set.
     * @param value The new value of the component.
     * @throws IndexOutOfBoundsException When the Matrix does not contain a component at the specified coordinate.
     * @see #set(int, Number)
     */
    default void set(int x, int y, T value) throws IndexOutOfBoundsException {
        try {
            set(toIndex(x, y), value);
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException(getErrorHandler().componentCoordinateOutOfBoundsErrorMessage(this, x, y));
        }
    }
    
    
    //Static Methods
    
    /**
     * Creates a new Matrix instance of the specified dimensionality.
     *
     * @param dim   The dimensionality of the new Matrix.
     * @param clazz The class of Matrix to create the instance of.
     * @param <I>   The type of the Matrix class.
     * @return The new Matrix.
     */
    static <I extends MatrixInterface<?, ?>> I createInstance(int dim, Class<? extends I> clazz) {
        try {
            return clazz.getConstructor(int.class).newInstance(dim);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            return null;
        }
    }
    
    /**
     * Creates an identity Matrix of a certain dimensionality.
     *
     * @param dim   The dimensionality of the identity Matrix.
     * @param clazz The class of Matrix to create the instance of.
     * @param <I>   The type of the Matrix class.
     * @return The identity Matrix.
     */
    static <I extends MatrixInterface<?, ?>> I identity(int dim, Class<? extends I> clazz) {
        I result = createInstance(Math.max(dim, 0), clazz);
        if (result == null) {
            return null;
        }
        
        Arrays.fill(result.getRawComponents(), result.getHandler().zero());
        for (int d = 0; d < dim; d++) {
            result.getRawComponents()[result.toIndex(d, d)] = result.getHandler().one();
        }
        return result;
    }
    
    /**
     * Creates an origin Matrix of a certain dimensionality.
     *
     * @param dim   The dimensionality of the origin Matrix.
     * @param clazz The class of Matrix to create the instance of.
     * @param <I>   The type of the Matrix class.
     * @return The origin Matrix.
     */
    static <I extends MatrixInterface<?, ?>> I origin(int dim, Class<? extends I> clazz) {
        I result = createInstance(Math.max(dim, 0), clazz);
        if (result == null) {
            return null;
        }
        
        Arrays.fill(result.getRawComponents(), result.getHandler().zero());
        return result;
    }
    
    /**
     * Creates a sign chart Matrix of a certain dimensionality.
     *
     * @param dim   The dimensionality of the sign chart Matrix.
     * @param clazz The class of Matrix to create the instance of.
     * @param <I>   The type of the Matrix class.
     * @return The sign chart Matrix.
     */
    static <I extends MatrixInterface<?, ?>> I signChart(int dim, Class<? extends I> clazz) {
        I result = createInstance(Math.max(dim, 0), clazz);
        if (result == null) {
            return null;
        }
        
        for (int c = 0; c < result.getLength(); c++) {
            result.getRawComponents()[c] = result.cofactorScalar(c);
        }
        return result;
    }
    
}
