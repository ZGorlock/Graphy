/*
 * File:    VectorInterface.java
 * Package: commons.math.component.vector
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.component.vector;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

import commons.math.component.ComponentInterface;
import commons.math.component.handler.error.ComponentErrorHandlerProvider;

/**
 * Defines an additional contract for Vector classes.
 *
 * @param <T> The component type of the Vector.
 * @param <I> The type of the Vector.
 */
@SuppressWarnings("unchecked")
public interface VectorInterface<T extends Number, I extends VectorInterface<?, ?>> extends ComponentInterface<Number, I, T> {
    
    //Methods
    
    /**
     * Returns a string that represents the Vector.
     *
     * @return A string that represents the Vector.
     */
    default String vectorString() {
        return Arrays.stream(getComponents())
                .map(e -> (e instanceof BigDecimal) ? ((BigDecimal) e).toPlainString() : e.toString())
                .collect(Collectors.joining(", ", "<", ">"));
    }
    
    /**
     * Calculates the dot product of this Vector with another Vector.
     *
     * @param other The other Vector.
     * @return The dot product.
     * @throws ArithmeticException When the two Vectors do not have the same dimensionality.
     */
    default T dot(I other) throws ArithmeticException {
        ComponentErrorHandlerProvider.assertDimensionalitySame(this, other);
        
        Number dot = getHandler().zero();
        for (int c = 0; c < getLength(); c++) {
            dot = getHandler().add(dot,
                    getHandler().multiply(getRawComponents()[c], other.getRawComponents()[c]));
        }
        return (T) dot;
    }
    
    /**
     * Normalizes the Vector.
     *
     * @return The normalized Vector.
     */
    default I normalize() {
        T hypotenuse = hypotenuse();
        return getHandler().isZero(hypotenuse) ? cloned() :
               scale(getHandler().reciprocal(hypotenuse()));
    }
    
    /**
     * Performs the square root of the sum of the squares of the components.
     *
     * @return The square root of the sum of the squares of the components.
     */
    default T hypotenuse() {
        return (T) getHandler().sqrt(squareSum());
    }
    
    /**
     * Creates a sub Vector from the Vector.
     *
     * @param from The index to start the sub Vector from, inclusive.
     * @param to   The index to end the sub Vector at, exclusive.
     * @return The sub Vector.
     * @throws IndexOutOfBoundsException When the range of indices is out of bounds of the Vector.
     * @throws ArithmeticException       When the Vector is not resizable.
     */
    default I subVector(int from, int to) throws IndexOutOfBoundsException {
        int dim = to - from;
        if ((dim > getLength()) || (from > to) || (from < 0) || (to > getLength())) {
            throw new IndexOutOfBoundsException(getErrorHandler().componentRangeOutOfBoundsErrorMessage(this, from, to));
        }
        
        if (!isResizeable() && (getDimensionality() != dim)) {
            throw new ArithmeticException(getErrorHandler().dimensionalityNotEqualErrorMessage(this, getDimensionality()));
        }
        
        I subVector = createNewInstance(dim);
        System.arraycopy(getRawComponents(), from, subVector.getRawComponents(), 0, (to - from));
        copyMeta(subVector);
        return subVector;
    }
    
    /**
     * Creates a sub Vector from the Vector.
     *
     * @param from The index to start the sub Vector from, inclusive.
     * @return The sub Vector.
     * @throws IndexOutOfBoundsException When the range of indices is out of bounds of the Vector.
     * @see #subVector(int, int)
     */
    default I subVector(int from) throws IndexOutOfBoundsException {
        return subVector(from, getLength());
    }
    
    
    //Getters
    
    /**
     * Returns the raw x component of the Vector.
     *
     * @return The raw x component of the Vector.
     */
    default T getRawX() {
        return (getDimensionality() > 0) ?
               (T) getRawComponents()[0] : (T) getHandler().zero();
    }
    
    /**
     * Returns the x component of the Vector.
     *
     * @return The x component of the Vector.
     * @see #getRawX()
     */
    default T getX() {
        return (T) getHandler().clean(getRawX());
    }
    
    /**
     * Returns the raw y component of the Vector.
     *
     * @return The raw y component of the Vector.
     */
    default T getRawY() {
        return (getDimensionality() >= Vector2.DIMENSIONALITY) ?
               (T) getRawComponents()[1] : (T) getHandler().zero();
    }
    
    /**
     * Returns the y component of the Vector.
     *
     * @return The y component of the Vector.
     * @see #getRawY()
     */
    default T getY() {
        return (T) getHandler().clean(getRawY());
    }
    
    /**
     * Returns the raw z component of the Vector.
     *
     * @return The raw z component of the Vector.
     */
    default T getRawZ() {
        return (getDimensionality() >= Vector3.DIMENSIONALITY) ?
               (T) getRawComponents()[2] : (T) getHandler().zero();
    }
    
    /**
     * Returns the z component of the Vector.
     *
     * @return The z component of the Vector.
     * @see #getRawZ()
     */
    default T getZ() {
        return (T) getHandler().clean(getRawZ());
    }
    
    /**
     * Returns the raw w component of the Vector.
     *
     * @return The raw w component of the Vector.
     */
    default T getRawW() {
        return (getDimensionality() >= Vector4.DIMENSIONALITY) ?
               (T) getRawComponents()[3] : (T) getHandler().zero();
    }
    
    /**
     * Returns the w component of the Vector.
     *
     * @return The w component of the Vector.
     * @see #getRawW()
     */
    default T getW() {
        return (T) getHandler().clean(getRawW());
    }
    
    
    //Setters
    
    /**
     * Sets the x component of the Vector.
     *
     * @param x The new x component of the Vector.
     */
    default void setX(T x) {
        if ((getDimensionality() > 0) && (x != null)) {
            getRawComponents()[0] = x;
        }
    }
    
    /**
     * Sets the y component of the Vector.
     *
     * @param y The new y component of the Vector.
     */
    default void setY(T y) {
        if ((getDimensionality() >= Vector2.DIMENSIONALITY) && (y != null)) {
            getRawComponents()[1] = y;
        }
    }
    
    /**
     * Sets the z component of the Vector.
     *
     * @param z The new z component of the Vector.
     */
    default void setZ(T z) {
        if ((getDimensionality() >= Vector3.DIMENSIONALITY) && (z != null)) {
            getRawComponents()[2] = z;
        }
    }
    
    /**
     * Sets the w component of the Vector.
     *
     * @param w The new w component of the Vector.
     */
    default void setW(T w) {
        if ((getDimensionality() >= Vector4.DIMENSIONALITY) && (w != null)) {
            getRawComponents()[3] = w;
        }
    }
    
    
    //Static Methods
    
    /**
     * Creates a new Vector instance of the specified dimensionality.
     *
     * @param dim   The dimensionality of the new Vector.
     * @param clazz The class of Vector to create the instance of.
     * @param <I>   The type of the Vector class.
     * @return The new Vector.
     */
    static <I extends VectorInterface<?, ?>> I createInstance(int dim, Class<? extends I> clazz) {
        try {
            return clazz.getConstructor(int.class).newInstance(Math.max(dim, 0));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            return null;
        }
    }
    
    /**
     * Creates an identity Vector of a certain dimensionality.
     *
     * @param dim   The dimensionality of the identity Vector.
     * @param clazz The class of Vector to create the instance of.
     * @param <I>   The type of the Vector class.
     * @return The identity Vector.
     */
    static <I extends VectorInterface<?, ?>> I identity(int dim, Class<? extends I> clazz) {
        I result = createInstance(Math.max(dim, 0), clazz);
        if (result == null) {
            return null;
        }
        
        Arrays.fill(result.getRawComponents(), result.getHandler().one());
        return result;
    }
    
    /**
     * Creates an origin Vector of a certain dimensionality.
     *
     * @param dim   The dimensionality of the origin Vector.
     * @param clazz The class of Vector to create the instance of.
     * @param <I>   The type of the Vector class.
     * @return The origin Vector.
     */
    static <I extends VectorInterface<?, ?>> I origin(int dim, Class<? extends I> clazz) {
        I result = createInstance(Math.max(dim, 0), clazz);
        if (result == null) {
            return null;
        }
        
        Arrays.fill(result.getRawComponents(), result.getHandler().zero());
        return result;
    }
    
}
