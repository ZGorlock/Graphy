/*
 * File:    RawComponentMathHandler.java
 * Package: commons.math.component.handler.math
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.component.handler.math;

import java.util.function.IntFunction;

import commons.math.MathUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the Component Math Handler for performing math operations for Raw Components.
 */
public class RawComponentMathHandler implements ComponentMathHandlerInterface<Number> {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(RawComponentMathHandler.class);
    
    
    //Constants
    
    /**
     * The precision to use in comparisons.
     */
    public static final Number PRECISION = DEFAULT_PRECISION;
    
    /**
     * The number of significant figures of the precision.
     */
    public static final int SIGNIFICANT_FIGURES = DEFAULT_SIGNIFICANT_FIGURES;
    
    
    //Constructors
    
    /**
     * The default no-argument constructor for a Raw Component Math Handler.
     */
    public RawComponentMathHandler() {
    }
    
    
    //Methods
    
    /**
     * Returns zero.
     *
     * @return Zero.
     */
    @Override
    public Number zero() {
        return 0.0;
    }
    
    /**
     * Returns one.
     *
     * @return One.
     */
    @Override
    public Number one() {
        return 1.0;
    }
    
    /**
     * Returns negative one.
     *
     * @return Negative one.
     */
    @Override
    public Number negativeOne() {
        return -1.0;
    }
    
    /**
     * Returns the value of a number.
     *
     * @param n The number.
     * @return The value of the specified number.
     */
    @Override
    public Number valueOf(Number n) {
        return n;
    }
    
    /**
     * Returns an empty array.
     *
     * @param length The length of the array.
     * @return An empty array of the specified length.
     */
    @Override
    public Number[] array(int length) {
        return new Number[length];
    }
    
    /**
     * Returns an array generator.
     *
     * @return An array generator.
     */
    @Override
    public IntFunction<Number[]> arrayGenerator() {
        return Number[]::new;
    }
    
    /**
     * Defines the addition of one component with another.
     *
     * @param a The first component.
     * @param b The second component.
     * @return The result of the addition of the components.
     */
    @Override
    public Number add(Number a, Number b) {
        return a.doubleValue() + b.doubleValue();
    }
    
    /**
     * Defines the subtraction of one component from another.
     *
     * @param a The first component.
     * @param b The second component.
     * @return The result of the subtraction of the components.
     */
    @Override
    public Number subtract(Number a, Number b) {
        return a.doubleValue() - b.doubleValue();
    }
    
    /**
     * Defines the multiplication of one component with another.
     *
     * @param a The first component.
     * @param b The second component.
     * @return The result of the multiplication of the components.
     */
    @Override
    public Number multiply(Number a, Number b) {
        return a.doubleValue() * b.doubleValue();
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
    public Number divide(Number a, Number b) throws ArithmeticException {
        if (isZero(b)) {
            throw new ArithmeticException("Attempted to divide by zero");
        }
        return a.doubleValue() / b.doubleValue();
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
    public Number power(Number a, Number n) throws ArithmeticException {
        return Math.pow(a.doubleValue(), n.doubleValue());
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
    public Number root(Number a, Number n) throws ArithmeticException {
        if (compare(a, zero()) < 0) {
            throw new ArithmeticException("Result of root is imaginary");
        }
        return Math.pow(a.doubleValue(), reciprocal(n).doubleValue());
    }
    
    /**
     * Defines the square root of a component.
     *
     * @param a The component.
     * @return The square root of the component.
     * @throws ArithmeticException When the result is imaginary.
     */
    @Override
    public Number sqrt(Number a) throws ArithmeticException {
        if (compare(a, zero()) < 0) {
            throw new ArithmeticException("Result of square root is imaginary");
        }
        return Math.sqrt(a.doubleValue());
    }
    
    /**
     * Defines the reciprocal of a component.
     *
     * @param a The component.
     * @return The reciprocal of the component.
     * @throws ArithmeticException When the component is zero.
     */
    @Override
    public Number reciprocal(Number a) throws ArithmeticException {
        return divide(one(), a);
    }
    
    /**
     * Defines the absolute value operation of a component.
     *
     * @param a The component.
     * @return The absolute value of the component.
     */
    @Override
    public Number abs(Number a) {
        return Math.abs(a.doubleValue());
    }
    
    /**
     * Defines the negative value operation of a component.
     *
     * @param a The component.
     * @return The negative value of the component.
     */
    @Override
    public Number negate(Number a) {
        return multiply(a, negativeOne());
    }
    
    /**
     * Defines the round operation of a component.
     *
     * @param a The component.
     * @return The round value of the component.
     */
    @Override
    public Number round(Number a) {
        return (Number) Math.round(a.doubleValue());
    }
    
    /**
     * Defines the comparison of one component with another.
     *
     * @param a The first component.
     * @param b The second component.
     * @return The result of the comparison of the components.
     */
    @Override
    public int compare(Number a, Number b) {
        return Double.compare(a.doubleValue(), b.doubleValue());
    }
    
    /**
     * Defines the is equal operation of one component with another.
     *
     * @param a The first component.
     * @param b The second component.
     * @return The result of the is equal operation of the components.
     */
    @Override
    public boolean isEqual(Number a, Number b) {
        return (compare(abs(subtract(b, a)), PRECISION) < 0);
    }
    
    /**
     * Defines the is zero operation of a component.
     *
     * @param a The component.
     * @return Whether the component is zero or not.
     * @see #isEqual(Number, Number)
     */
    @Override
    public boolean isZero(Number a) {
        return clean(a).equals(zero());
    }
    
    /**
     * Cleans a component.
     *
     * @param a The component.
     * @return The cleaned component.
     */
    @Override
    public Number clean(Number a) {
        return MathUtility.roundWithPrecision(a.doubleValue(), SIGNIFICANT_FIGURES);
    }
    
    
    //Getters
    
    /**
     * Returns the precision of the Component Math Handler.
     *
     * @return The precision of the Component Math Handler.
     */
    public Number getPrecision() {
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
