/*
 * File:    IntComponentMathHandler.java
 * Package: commons.math.component.handler.math
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.component.handler.math;

import java.util.function.IntFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the Component Math Handler for performing math operations for Int Components.
 */
public class IntComponentMathHandler implements ComponentMathHandlerInterface<Integer> {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(IntComponentMathHandler.class);
    
    
    //Constants
    
    /**
     * The precision to use in comparisons.
     */
    public static final Integer PRECISION = 1;
    
    /**
     * The number of significant figures of the precision.
     */
    public static final int SIGNIFICANT_FIGURES = 0;
    
    
    //Constructors
    
    /**
     * The default no-argument constructor for a Integer Component Math Handler.
     */
    public IntComponentMathHandler() {
    }
    
    
    //Methods
    
    /**
     * Returns zero.
     *
     * @return Zero.
     */
    @Override
    public Integer zero() {
        return 0;
    }
    
    /**
     * Returns one.
     *
     * @return One.
     */
    @Override
    public Integer one() {
        return 1;
    }
    
    /**
     * Returns negative one.
     *
     * @return Negative one.
     */
    @Override
    public Integer negativeOne() {
        return -1;
    }
    
    /**
     * Returns the value of a number.
     *
     * @param n The number.
     * @return The value of the specified number.
     */
    @Override
    public Integer valueOf(Number n) {
        return n.intValue();
    }
    
    /**
     * Returns an empty array.
     *
     * @param length The length of the array.
     * @return An empty array of the specified length.
     */
    @Override
    public Integer[] array(int length) {
        return new Integer[length];
    }
    
    /**
     * Returns an array generator.
     *
     * @return An array generator.
     */
    @Override
    public IntFunction<Integer[]> arrayGenerator() {
        return Integer[]::new;
    }
    
    /**
     * Defines the addition of one component with another.
     *
     * @param a The first component.
     * @param b The second component.
     * @return The result of the addition of the components.
     */
    @Override
    public Integer add(Integer a, Integer b) {
        return a + b;
    }
    
    /**
     * Defines the subtraction of one component from another.
     *
     * @param a The first component.
     * @param b The second component.
     * @return The result of the subtraction of the components.
     */
    @Override
    public Integer subtract(Integer a, Integer b) {
        return a - b;
    }
    
    /**
     * Defines the multiplication of one component with another.
     *
     * @param a The first component.
     * @param b The second component.
     * @return The result of the multiplication of the components.
     */
    @Override
    public Integer multiply(Integer a, Integer b) {
        return a * b;
    }
    
    /**
     * Defines the division of one component by another.
     *
     * @param a The first component.
     * @param b The second component.
     * @return The result of the division of the components.
     * @throws ArithmeticException When the divisor is zero.
     */
    @Override
    public Integer divide(Integer a, Integer b) throws ArithmeticException {
        if (isZero(b)) {
            throw new ArithmeticException("Attempted to divide by zero");
        }
        return a / b;
    }
    
    /**
     * Defines the operation of the nth power of a component.
     *
     * @param a The component.
     * @param n The power.
     * @return The result of the power operation of the component.
     * @throws ArithmeticException When the result is imaginary.
     */
    @Override
    public Integer power(Integer a, Integer n) throws ArithmeticException {
        return (int) Math.pow(a, n);
    }
    
    /**
     * Defines the operation of the nth root of a component.
     *
     * @param a The component.
     * @param n The root.
     * @return The result of the root operation of the component.
     * @throws ArithmeticException When the result is imaginary, or when the degree of the root is divided by zero.
     */
    @Override
    public Integer root(Integer a, Integer n) throws ArithmeticException {
        if (compare(a, zero()) < 0) {
            throw new ArithmeticException("Result of root is imaginary");
        }
        return (int) Math.pow(a, reciprocal(n));
    }
    
    /**
     * Defines the square root of a component.
     *
     * @param a The component.
     * @return The square root of the component.
     * @throws ArithmeticException When the result is imaginary.
     */
    @Override
    public Integer sqrt(Integer a) throws ArithmeticException {
        if (compare(a, zero()) < 0) {
            throw new ArithmeticException("Result of square root is imaginary");
        }
        return (int) Math.sqrt(a);
    }
    
    /**
     * Defines the reciprocal of a component.
     *
     * @param a The component.
     * @return The reciprocal of the component.
     * @throws ArithmeticException When the component is zero.
     */
    @Override
    public Integer reciprocal(Integer a) throws ArithmeticException {
        return divide(one(), a);
    }
    
    /**
     * Defines the absolute value operation of a component.
     *
     * @param a The component.
     * @return The absolute value of the component.
     */
    @Override
    public Integer abs(Integer a) {
        return Math.abs(a);
    }
    
    /**
     * Defines the negative value operation of a component.
     *
     * @param a The component.
     * @return The negative value of the component.
     */
    @Override
    public Integer negate(Integer a) {
        return multiply(a, negativeOne());
    }
    
    /**
     * Defines the round operation of a component.
     *
     * @param a The component.
     * @return The round value of the component.
     */
    @Override
    public Integer round(Integer a) {
        return a;
    }
    
    /**
     * Defines the comparison of one component with another.
     *
     * @param a The first component.
     * @param b The second component.
     * @return The result of the comparison of the components.
     */
    @Override
    public int compare(Integer a, Integer b) {
        return a.compareTo(b);
    }
    
    /**
     * Defines the is equal operation of one component with another.
     *
     * @param a The first component.
     * @param b The second component.
     * @return The result of the is equal operation of the components.
     */
    @Override
    public boolean isEqual(Integer a, Integer b) {
        return a.equals(b);
    }
    
    /**
     * Defines the is zero operation of a component.
     *
     * @param a The component.
     * @return Whether the component is zero or not.
     * @see #isEqual(Integer, Integer)
     */
    @Override
    public boolean isZero(Integer a) {
        return clean(a) == 0;
    }
    
    /**
     * Cleans a component.
     *
     * @param a The component.
     * @return The cleaned component.
     */
    @Override
    public Integer clean(Integer a) {
        return a;
    }
    
    
    //Getters
    
    /**
     * Returns the precision of the Component Math Handler.
     *
     * @return The precision of the Component Math Handler.
     */
    public Integer getPrecision() {
        return PRECISION;
    }
    
    /**
     * Returns the significant figures of the Component Math Handler.
     *
     * @return The significant figures of the Component Math Handler.
     */
    public int getSignificantFigures() {
        return SIGNIFICANT_FIGURES;
    }
    
}
