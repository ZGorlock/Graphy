/*
 * File:    DoubleComponentMathHandler.java
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
 * Defines the Component Math Handler for performing math operations for Components.
 */
public class DoubleComponentMathHandler implements ComponentMathHandlerInterface<Double> {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(DoubleComponentMathHandler.class);
    
    
    //Constants
    
    /**
     * The precision to use in comparisons.
     */
    public static final Double PRECISION = DEFAULT_PRECISION.doubleValue();
    
    /**
     * The number of significant figures of the precision.
     */
    public static final int SIGNIFICANT_FIGURES = DEFAULT_SIGNIFICANT_FIGURES;
    
    
    //Constructors
    
    /**
     * The default no-argument constructor for a Double Component Math Handler.
     */
    public DoubleComponentMathHandler() {
    }
    
    
    //Methods
    
    /**
     * Returns zero.
     *
     * @return Zero.
     */
    @Override
    public Double zero() {
        return 0.0;
    }
    
    /**
     * Returns one.
     *
     * @return One.
     */
    @Override
    public Double one() {
        return 1.0;
    }
    
    /**
     * Returns negative one.
     *
     * @return Negative one.
     */
    @Override
    public Double negativeOne() {
        return -1.0;
    }
    
    /**
     * Returns the value of a number.
     *
     * @param n The number.
     * @return The value of the specified number.
     */
    @Override
    public Double valueOf(Number n) {
        return n.doubleValue();
    }
    
    /**
     * Returns an empty array.
     *
     * @param length The length of the array.
     * @return An empty array of the specified length.
     */
    @Override
    public Double[] array(int length) {
        return new Double[length];
    }
    
    /**
     * Returns an array generator.
     *
     * @return An array generator.
     */
    @Override
    public IntFunction<Double[]> arrayGenerator() {
        return Double[]::new;
    }
    
    /**
     * Defines the addition of one component with another.
     *
     * @param a The first component.
     * @param b The second component.
     * @return The result of the addition of the components.
     */
    @Override
    public Double add(Double a, Double b) {
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
    public Double subtract(Double a, Double b) {
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
    public Double multiply(Double a, Double b) {
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
    public Double divide(Double a, Double b) throws ArithmeticException {
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
    public Double power(Double a, Double n) throws ArithmeticException {
        return Math.pow(a, n);
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
    public Double root(Double a, Double n) throws ArithmeticException {
        if (compare(a, zero()) < 0) {
            throw new ArithmeticException("Result of root is imaginary");
        }
        return Math.pow(a, reciprocal(n));
    }
    
    /**
     * Defines the square root of a component.
     *
     * @param a The component.
     * @return The square root of the component.
     * @throws ArithmeticException When the result is imaginary.
     */
    @Override
    public Double sqrt(Double a) throws ArithmeticException {
        if (compare(a, zero()) < 0) {
            throw new ArithmeticException("Result of square root is imaginary");
        }
        return Math.sqrt(a);
    }
    
    /**
     * Defines the reciprocal of a component.
     *
     * @param a The component.
     * @return The reciprocal of the component.
     * @throws ArithmeticException When the component is zero.
     */
    @Override
    public Double reciprocal(Double a) throws ArithmeticException {
        return divide(one(), a);
    }
    
    /**
     * Defines the absolute value operation of a component.
     *
     * @param a The component.
     * @return The absolute value of the component.
     */
    @Override
    public Double abs(Double a) {
        return Math.abs(a);
    }
    
    /**
     * Defines the negative value operation of a component.
     *
     * @param a The component.
     * @return The negative value of the component.
     */
    @Override
    public Double negate(Double a) {
        return multiply(a, negativeOne());
    }
    
    /**
     * Defines the round operation of a component.
     *
     * @param a The component.
     * @return The round value of the component.
     */
    @Override
    public Double round(Double a) {
        return (double) Math.round(a);
    }
    
    /**
     * Defines the comparison of one component with another.
     *
     * @param a The first component.
     * @param b The second component.
     * @return The result of the comparison of the components.
     */
    @Override
    public int compare(Double a, Double b) {
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
    public boolean isEqual(Double a, Double b) {
        return (abs(subtract(b, a)) <= PRECISION);
    }
    
    /**
     * Defines the is zero operation of a component.
     *
     * @param a The component.
     * @return Whether the component is zero or not.
     * @see #isEqual(Double, Double)
     */
    @Override
    public boolean isZero(Double a) {
        return clean(a) == 0.0;
    }
    
    /**
     * Cleans a component.
     *
     * @param a The component.
     * @return The cleaned component.
     */
    @Override
    public Double clean(Double a) {
        return MathUtility.roundWithPrecision(a, SIGNIFICANT_FIGURES);
    }
    
    
    //Getters
    
    /**
     * Returns the precision of the Component Math Handler.
     *
     * @return The precision of the Component Math Handler.
     */
    public Double getPrecision() {
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
