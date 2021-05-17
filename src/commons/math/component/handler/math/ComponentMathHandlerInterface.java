/*
 * File:    ComponentMathHandlerInterface.java
 * Package: commons.math.component.handler.math
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.component.handler.math;

import java.util.function.IntFunction;

/**
 * Defines the contract for Component Math Handler classes.
 *
 * @param <T> The type of the Component Math Handler.
 */
public interface ComponentMathHandlerInterface<T extends Number> {
    
    //Constants
    
    /**
     * The default precision to use in comparisons.
     */
    Number DEFAULT_PRECISION = 0.000000000001;
    
    /**
     * The default number of significant figures of the precision.
     */
    int DEFAULT_SIGNIFICANT_FIGURES = 12;
    
    
    //Methods
    
    /**
     * Returns zero.
     *
     * @return Zero.
     */
    T zero();
    
    /**
     * Returns one.
     *
     * @return One.
     */
    T one();
    
    /**
     * Returns negative one.
     *
     * @return Negative one.
     */
    T negativeOne();
    
    /**
     * Returns the value of a number.
     *
     * @param n The number.
     * @return The value of the specified number.
     */
    T valueOf(Number n);
    
    /**
     * Returns an empty array.
     *
     * @param length The length of the array.
     * @return An empty array of the specified length.
     */
    T[] array(int length);
    
    /**
     * Returns an array generator.
     *
     * @return An array generator.
     */
    IntFunction<T[]> arrayGenerator();
    
    /**
     * Defines the addition of one component with another.
     *
     * @param a The first component.
     * @param b The second component.
     * @return The result of the addition of the components.
     */
    T add(T a, T b);
    
    /**
     * Defines the subtraction of one component from another.
     *
     * @param a The first component.
     * @param b The second component.
     * @return The result of the subtraction of the components.
     */
    T subtract(T a, T b);
    
    /**
     * Defines the multiplication of one component with another.
     *
     * @param a The first component.
     * @param b The second component.
     * @return The result of the multiplication of the components.
     */
    T multiply(T a, T b);
    
    /**
     * Defines the division of one component by another.
     *
     * @param a The first component.
     * @param b The second component.
     * @return The result of the division of the components.
     * @throws ArithmeticException When the divisor is zero.
     */
    T divide(T a, T b) throws ArithmeticException;
    
    /**
     * Defines the operation of the nth power of a component.
     *
     * @param a The component.
     * @param n The power.
     * @return The result of the power operation of the component.
     * @throws ArithmeticException When the result is imaginary.
     */
    T power(T a, T n) throws ArithmeticException;
    
    /**
     * Defines the operation of the nth root of a component.
     *
     * @param a The component.
     * @param n The root.
     * @return The result of the root operation of the component.
     * @throws ArithmeticException When the result is imaginary, or when the degree of the root is divided by zero.
     */
    T root(T a, T n) throws ArithmeticException;
    
    /**
     * Defines the square root of a component.
     *
     * @param a The component.
     * @return The square root of the component.
     * @throws ArithmeticException When the result is imaginary.
     */
    T sqrt(T a) throws ArithmeticException;
    
    /**
     * Defines the reciprocal of a component.
     *
     * @param a The component.
     * @return The reciprocal of the component.
     * @throws ArithmeticException When the component is zero.
     */
    T reciprocal(T a) throws ArithmeticException;
    
    /**
     * Defines the absolute value operation of a component.
     *
     * @param a The component.
     * @return The absolute value of the component.
     */
    T abs(T a);
    
    /**
     * Defines the negative value operation of a component.
     *
     * @param a The component.
     * @return The negative value of the component.
     */
    T negate(T a);
    
    /**
     * Defines the round operation of a component.
     *
     * @param a The component.
     * @return The round value of the component.
     */
    T round(T a);
    
    /**
     * Defines the comparison of one component with another.
     *
     * @param a The first component.
     * @param b The second component.
     * @return The result of the comparison of the components.
     */
    int compare(T a, T b);
    
    /**
     * Defines the is equal operation of one component with another.
     *
     * @param a The first component.
     * @param b The second component.
     * @return The result of the is equal operation of the components.
     */
    boolean isEqual(T a, T b);
    
    /**
     * Defines the is zero operation of a component.
     *
     * @param a The component.
     * @return Whether the component is zero or not.
     */
    boolean isZero(T a);
    
    /**
     * Cleans a component.
     *
     * @param a The component.
     * @return The cleaned component.
     */
    T clean(T a);
    
    
    //Getters
    
    /**
     * Returns the precision of the Component Math Handler.
     *
     * @return The precision of the Component Math Handler.
     */
    T getPrecision();
    
    /**
     * Returns the significant figures of the Component Math Handler.
     *
     * @return The significant figures of the Component Math Handler.
     */
    int getSignificantFigures();
    
}
