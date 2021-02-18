/*
 * File:    BigVector.java
 * Package: commons.math.vector
 * Author:  Zachary Gill
 */

package commons.math.vector;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import commons.list.ListUtility;
import commons.math.BigMathUtility;
import commons.math.BoundUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the base properties of a BigVector.
 */
public class BigVector {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(BigVector.class);
    
    
    //Constants
    
    /**
     * The precision to use in comparisons.
     */
    public static final BigDecimal PRECISION = new BigDecimal("0.000000000000000000000000000001");
    
    /**
     * The default value of the precision of a Big Vector math context.
     */
    public static final int DEFAULT_MATH_PRECISION = BigMathUtility.DEFAULT_MATH_PRECISION / 2;
    
    /**
     * The default rounding mode of a Big Vector math context.
     */
    public static final RoundingMode DEFAULT_ROUNDING_MODE = BigMathUtility.DEFAULT_ROUNDING_MODE;
    
    
    //Static Fields
    
    /**
     * The Big Vector used for justification.
     */
    public static BigVector JUSTIFICATION_VECTOR = new BigVector(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE);
    
    
    //Fields
    
    /**
     * The array of components that define the Big Vector.
     */
    public BigDecimal[] components;
    
    /**
     * The MathContext to use when doing Big Vector math.
     */
    public MathContext mathContext = new MathContext(DEFAULT_MATH_PRECISION, DEFAULT_ROUNDING_MODE);
    
    
    //Constructors
    
    /**
     * The constructor for a Big Vector.
     *
     * @param components The components that define the Big Vector.
     * @param <T>        A Number type for the components.
     */
    @SafeVarargs
    public <T extends Number> BigVector(T... components) {
        this.components = new BigDecimal[components.length];
        for (int i = 0; i < components.length; i++) {
            this.components[i] =
                    (components[i] instanceof BigDecimal ?
                     (BigDecimal) components[i] :
                     BigDecimal.valueOf(components[i].doubleValue()));
        }
    }
    
    /**
     * The constructor for a Big Vector.
     *
     * @param components The string components that define the Big Vector.
     * @throws NumberFormatException If the string components provided are not numbers.
     */
    public BigVector(String... components) throws NumberFormatException {
        this.components = new BigDecimal[components.length];
        for (int i = 0; i < components.length; i++) {
            this.components[i] = new BigDecimal(components[i]);
        }
    }
    
    /**
     * The constructor for a Big Vector from a list of components.
     *
     * @param components The components that define the Big Vector, as a list.
     * @param <T>        A Number type for the components.
     */
    public <T extends Number> BigVector(List<T> components) {
        this.components = new BigDecimal[components.size()];
        for (int i = 0; i < components.size(); i++) {
            this.components[i] =
                    (components.get(i) instanceof BigDecimal ?
                     (BigDecimal) components.get(i) :
                     BigDecimal.valueOf(components.get(i).doubleValue()));
        }
    }
    
    /**
     * The constructor for a Big Vector from another Big Vector.
     *
     * @param vector The Big Vector.
     */
    public BigVector(BigVector vector) {
        this.components = new BigDecimal[vector.getDimensionality()];
        System.arraycopy(vector.components, 0, this.components, 0, vector.getDimensionality());
        this.mathContext = vector.mathContext;
    }
    
    /**
     * The constructor for a Big Vector from a Vector.
     *
     * @param vector The Vector.
     */
    public BigVector(Vector vector) {
        this.components = new BigDecimal[vector.getDimensionality()];
        for (int i = 0; i < vector.getDimensionality(); i++) {
            this.components[i] = BigDecimal.valueOf(vector.components[i]);
        }
    }
    
    /**
     * The constructor for a Big Vector from another Big Vector with added components.
     *
     * @param vector     The Big Vector.
     * @param components The components to add.
     * @param <T>        A Number type for the components.
     */
    @SafeVarargs
    public <T extends Number> BigVector(BigVector vector, T... components) {
        this.components = new BigDecimal[vector.getDimensionality() + components.length];
        System.arraycopy(vector.components, 0, this.components, 0, vector.getDimensionality());
        for (int i = 0; i < components.length; i++) {
            this.components[vector.getDimensionality() + i] =
                    (components[i] instanceof BigDecimal ?
                     (BigDecimal) components[i] :
                     BigDecimal.valueOf(components[i].doubleValue()));
        }
        this.mathContext = vector.mathContext;
    }
    
    /**
     * The constructor for a Big Vector from another Big Vector with added components.
     *
     * @param vector     The Big Vector.
     * @param components The string components to add.
     */
    public BigVector(BigVector vector, String... components) {
        this.components = new BigDecimal[vector.getDimensionality() + components.length];
        System.arraycopy(vector.components, 0, this.components, 0, vector.getDimensionality());
        for (int i = 0; i < components.length; i++) {
            this.components[vector.getDimensionality() + i] = new BigDecimal(components[i]);
        }
        this.mathContext = vector.mathContext;
    }
    
    /**
     * The constructor for a Big Vector from another Vector with added components.
     *
     * @param vector     The Vector.
     * @param components The components to add.
     * @param <T>        A Number type for the components.
     */
    @SafeVarargs
    public <T extends Number> BigVector(Vector vector, T... components) {
        this.components = new BigDecimal[vector.getDimensionality() + components.length];
        for (int i = 0; i < vector.getDimensionality(); i++) {
            this.components[i] = BigDecimal.valueOf(vector.components[i]);
        }
        for (int i = 0; i < components.length; i++) {
            this.components[vector.getDimensionality() + i] =
                    (components[i] instanceof BigDecimal ?
                     (BigDecimal) components[i] :
                     BigDecimal.valueOf(components[i].doubleValue()));
        }
    }
    
    /**
     * The constructor for a Big Vector from another Vector with added components.
     *
     * @param vector     The Vector.
     * @param components The string components to add.
     */
    public BigVector(Vector vector, String... components) {
        this.components = new BigDecimal[vector.getDimensionality() + components.length];
        for (int i = 0; i < vector.getDimensionality(); i++) {
            this.components[i] = BigDecimal.valueOf(vector.components[i]);
        }
        for (int i = 0; i < components.length; i++) {
            this.components[vector.getDimensionality() + i] = new BigDecimal(components[i]);
        }
    }
    
    /**
     * The default no-argument constructor for a Big Vector.
     */
    public BigVector() {
        this.components = new BigDecimal[0];
    }
    
    
    //Methods
    
    /**
     * Returns a string that represents the Big Vector.
     *
     * @return A string that represents the Big Vector.
     */
    @Override
    public String toString() {
        return Arrays.stream(components).map(BigDecimal::toPlainString)
                .collect(Collectors.joining(", ", "<", ">"));
    }
    
    /**
     * Determines if another Big Vector is equal to this Big Vector.
     *
     * @param o The other Big Vector.
     * @return Whether the two Big Vectors are equal or not.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BigVector)) {
            return false;
        }
        BigVector vector = (BigVector) o;
        
        if (!dimensionalityEqual(vector)) {
            return false;
        }
        
        for (int c = 0; c < getDimensionality(); c++) {
            if (get(c).subtract(vector.get(c)).abs().compareTo(PRECISION) > 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Determines if another Big Vector's dimensionality is equal to this Big Vector's dimensionality.
     *
     * @param vector The other Big Vector.
     * @return Whether the two Big Vectors' dimensionality is equal or not.
     */
    public boolean dimensionalityEqual(BigVector vector) {
        return (vector != null) && (getDimensionality() == vector.getDimensionality());
    }
    
    /**
     * Creates a cloned copy of the Big Vector.
     *
     * @return The cloned Big Vector.
     */
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public BigVector clone() {
        BigVector bigVector = new BigVector(components);
        bigVector.setMathContext(mathContext);
        return bigVector;
    }
    
    /**
     * Creates a cloned copy of the Big Vector with its elements reversed.
     *
     * @return The reversed Big Vector.
     */
    public BigVector reverse() {
        BigDecimal[] reversedComponents = new BigDecimal[getDimensionality()];
        for (int i = 0; i < Math.ceil(getDimensionality() / 2.0); i++) {
            reversedComponents[i] = components[getDimensionality() - 1 - i];
            reversedComponents[getDimensionality() - 1 - i] = components[i];
        }
        BigVector bigVector = new BigVector(reversedComponents);
        bigVector.setMathContext(mathContext);
        return bigVector;
    }
    
    /**
     * Justifies a Big Vector.
     *
     * @return The justified Big Vector.
     * @throws ArithmeticException When the two Big Vectors do not have the same dimensionality.
     */
    public BigVector justify() throws ArithmeticException {
        return this.times(JUSTIFICATION_VECTOR);
    }
    
    /**
     * Calculates the distance between this Big Vector and another Big Vector.
     *
     * @param vector The other Big Vector.
     * @return The distance between the two Big Vectors.
     * @throws ArithmeticException When the two Big Vectors do not have the same dimensionality.
     */
    public BigDecimal distance(BigVector vector) throws ArithmeticException {
        if (!dimensionalityEqual(vector)) {
            throw new ArithmeticException(dimensionalityNotEqualErrorMessage(this, vector));
        }
        
        BigDecimal distance = BigDecimal.ZERO;
        for (int c = 0; c < getDimensionality(); c++) {
            distance = distance.add(vector.get(c).subtract(get(c)).pow(2, mathContext));
        }
        return new BigDecimal(BigMathUtility.sqrt(distance.toPlainString(), mathContext.getPrecision())).stripTrailingZeros();
    }
    
    /**
     * Calculates the midpoint between this Big Vector and another Big Vector.
     *
     * @param vector The other Big Vector.
     * @return The midpoint between the two Big Vectors.
     * @throws ArithmeticException When the two Big Vectors do not have the same dimensionality.
     */
    public BigVector midpoint(BigVector vector) throws ArithmeticException {
        return average(this, vector);
    }
    
    /**
     * Calculates the average of this Big Vector with a list of Big Vectors.
     *
     * @param vectors The list of Big Vectors.
     * @return The average of the Big Vectors.
     * @throws ArithmeticException When the Big Vectors do not all have the same dimensionality.
     */
    public BigVector average(List<BigVector> vectors) throws ArithmeticException {
        for (BigVector vector : vectors) {
            if (!dimensionalityEqual(vector)) {
                throw new ArithmeticException(dimensionalityNotEqualErrorMessage(this, vector));
            }
        }
        
        BigDecimal[] newComponents = new BigDecimal[getDimensionality()];
        BigDecimal size = BigDecimal.valueOf(vectors.size() + 1);
        for (int c = 0; c < getDimensionality(); c++) {
            BigDecimal component = get(c);
            for (BigVector vector : vectors) {
                component = component.add(vector.get(c));
            }
            newComponents[c] = component.divide(size, mathContext);
        }
        BigVector bigVector = new BigVector(newComponents);
        bigVector.setMathContext(mathContext);
        return bigVector;
    }
    
    /**
     * Calculates the average of this Big Vector with a set of Big Vectors.
     *
     * @param vectors The set of Big Vectors.
     * @return The average of the Big Vectors.
     * @throws ArithmeticException When the Big Vectors do not all have the same dimensionality.
     * @see #average(List)
     */
    public BigVector average(BigVector... vectors) throws ArithmeticException {
        return average(Arrays.asList(vectors));
    }
    
    /**
     * Calculates the dot product of this Big Vector with another Big Vector.
     *
     * @param vector The other Big Vector.
     * @return The dot product.
     * @throws ArithmeticException When the two Big Vectors do not have the same dimensionality.
     */
    public BigDecimal dot(BigVector vector) throws ArithmeticException {
        if (!dimensionalityEqual(vector)) {
            throw new ArithmeticException(dimensionalityNotEqualErrorMessage(this, vector));
        }
        
        BigDecimal dot = BigDecimal.ZERO;
        for (int c = 0; c < getDimensionality(); c++) {
            dot = dot.add(get(c).multiply(vector.get(c)));
        }
        return dot.stripTrailingZeros();
    }
    
    /**
     * Normalizes the Big Vector.
     *
     * @return The normalized Big Vector.
     */
    public BigVector normalize() {
        BigDecimal hypotenuse = hypotenuse();
        return (hypotenuse.compareTo(PRECISION) < 0) ? clone() :
               scale(hypotenuse().pow(-1, mathContext));
    }
    
    /**
     * Performs the square root of the sum of the squares of the components.
     *
     * @return The square root of the sum of the squares of the components.
     */
    public BigDecimal hypotenuse() {
        return new BigDecimal(BigMathUtility.sqrt(squareSum().toPlainString(), mathContext.getPrecision())).stripTrailingZeros();
    }
    
    /**
     * Sums the components of the Big Vector.
     *
     * @return The sum of the components of the Big Vector.
     */
    public BigDecimal sum() {
        BigDecimal sum = BigDecimal.ZERO;
        for (int c = 0; c < getDimensionality(); c++) {
            sum = sum.add(get(c));
        }
        return sum;
    }
    
    /**
     * Calculates the square sum of the Big Vector.
     *
     * @return The square sum of the Big Vector.
     */
    public BigDecimal squareSum() {
        BigDecimal squareSum = BigDecimal.ZERO;
        for (int c = 0; c < getDimensionality(); c++) {
            squareSum = squareSum.add(get(c).pow(2, mathContext));
        }
        return squareSum;
    }
    
    /**
     * Moves the decimal place of the components of the Big Vector left a certain distance.
     *
     * @param move The number of decimal places to move the components of the Big Vector.
     * @return The Big Vector after the move has been performed.
     */
    public BigVector movePointLeft(int move) {
        BigDecimal[] newComponents = new BigDecimal[getDimensionality()];
        for (int c = 0; c < getDimensionality(); c++) {
            newComponents[c] = get(c).movePointLeft(move);
        }
        BigVector bigVector = new BigVector(newComponents);
        bigVector.setMathContext(mathContext);
        return bigVector;
    }
    
    /**
     * Moves the decimal place of the components of the Big Vector right a certain distance.
     *
     * @param move The number of decimal places to move the components of the Big Vector.
     * @return The Big Vector after the move has been performed.
     */
    public BigVector movePointRight(int move) {
        BigDecimal[] newComponents = new BigDecimal[getDimensionality()];
        for (int c = 0; c < getDimensionality(); c++) {
            newComponents[c] = get(c).movePointRight(move);
        }
        BigVector bigVector = new BigVector(newComponents);
        bigVector.setMathContext(mathContext);
        return bigVector;
    }
    
    /**
     * Strips the trailing zeros of the components of the Big Vector.
     *
     * @return The Big Vector after the stripping has been performed.
     */
    public BigVector stripTrailingZeros() {
        BigDecimal[] newComponents = new BigDecimal[getDimensionality()];
        for (int c = 0; c < getDimensionality(); c++) {
            newComponents[c] = get(c).stripTrailingZeros();
        }
        BigVector bigVector = new BigVector(newComponents);
        bigVector.setMathContext(mathContext);
        return bigVector;
    }
    
    /**
     * Calculates the addition of this Big Vector and another Big Vector.
     *
     * @param vector The other Big Vector.
     * @return The Big Vector produced as a result of the addition.
     * @throws ArithmeticException When the two Big Vectors do not have the same dimensionality.
     */
    public BigVector plus(BigVector vector) throws ArithmeticException {
        if (!dimensionalityEqual(vector)) {
            throw new ArithmeticException(dimensionalityNotEqualErrorMessage(this, vector));
        }
        
        BigDecimal[] newComponents = new BigDecimal[getDimensionality()];
        for (int c = 0; c < getDimensionality(); c++) {
            newComponents[c] = get(c).add(vector.get(c));
        }
        BigVector bigVector = new BigVector(newComponents);
        bigVector.setMathContext(mathContext);
        return bigVector;
    }
    
    /**
     * Calculates the difference of this Big Vector and another Big Vector.
     *
     * @param vector The other Big Vector.
     * @return The Big Vector produced as a result of the subtraction.
     * @throws ArithmeticException When the two Big Vectors do not have the same dimensionality.
     */
    public BigVector minus(BigVector vector) throws ArithmeticException {
        if (!dimensionalityEqual(vector)) {
            throw new ArithmeticException(dimensionalityNotEqualErrorMessage(this, vector));
        }
        
        BigDecimal[] newComponents = new BigDecimal[getDimensionality()];
        for (int c = 0; c < getDimensionality(); c++) {
            newComponents[c] = get(c).subtract(vector.get(c));
        }
        BigVector bigVector = new BigVector(newComponents);
        bigVector.setMathContext(mathContext);
        return bigVector;
    }
    
    /**
     * Calculates the product of this Big Vector and another Big Vector.
     *
     * @param vector The other Big Vector.
     * @return The Big Vector produced as a result of the multiplication.
     * @throws ArithmeticException When the two Big Vectors do not have the same dimensionality.
     */
    public BigVector times(BigVector vector) throws ArithmeticException {
        if (!dimensionalityEqual(vector)) {
            throw new ArithmeticException(dimensionalityNotEqualErrorMessage(this, vector));
        }
        
        BigDecimal[] newComponents = new BigDecimal[getDimensionality()];
        for (int c = 0; c < getDimensionality(); c++) {
            newComponents[c] = get(c).multiply(vector.get(c));
        }
        BigVector bigVector = new BigVector(newComponents);
        bigVector.setMathContext(mathContext);
        return bigVector;
    }
    
    /**
     * Calculates the result of this Big Vector scaled by a value.
     *
     * @param scalar The scalar.
     * @return The Big Vector produced as a result of the scaling.
     */
    public BigVector scale(BigDecimal scalar) throws ArithmeticException {
        BigDecimal[] newComponents = new BigDecimal[getDimensionality()];
        for (int c = 0; c < getDimensionality(); c++) {
            newComponents[c] = get(c).multiply(scalar);
        }
        BigVector bigVector = new BigVector(newComponents);
        bigVector.setMathContext(mathContext);
        return bigVector;
    }
    
    /**
     * Calculates the result of this Big Vector scaled by a value.
     *
     * @param scalar The scalar.
     * @return The Big Vector produced as a result of the scaling.
     * @see #scale(BigDecimal)
     */
    public BigVector scale(double scalar) throws ArithmeticException {
        return scale(BigDecimal.valueOf(scalar));
    }
    
    /**
     * Calculates the quotient of this Big Vector and another Big Vector.
     *
     * @param vector The other Big Vector.
     * @return The Big Vector produced as a result of the division.
     * @throws ArithmeticException When the two Big Vectors do not have the same dimensionality, or if a component in the divisor Big Vector is 0.
     */
    public BigVector dividedBy(BigVector vector) throws ArithmeticException {
        if (!dimensionalityEqual(vector)) {
            throw new ArithmeticException(dimensionalityNotEqualErrorMessage(this, vector));
        }
        if (Arrays.stream(vector.components).anyMatch(e -> e.abs().compareTo(PRECISION) < 0)) {
            throw new ArithmeticException("Attempted to divide by 0");
        }
        
        BigDecimal[] newComponents = new BigDecimal[getDimensionality()];
        for (int c = 0; c < getDimensionality(); c++) {
            newComponents[c] = get(c).divide(vector.get(c), mathContext);
        }
        BigVector bigVector = new BigVector(newComponents);
        bigVector.setMathContext(mathContext);
        return bigVector;
    }
    
    /**
     * Rounds the components of the Big Vector.
     *
     * @return The Big Vector rounded to integers.
     */
    public BigVector round() {
        BigDecimal[] newComponents = new BigDecimal[getDimensionality()];
        for (int c = 0; c < getDimensionality(); c++) {
            newComponents[c] = get(c).setScale(0, mathContext.getRoundingMode());
        }
        BigVector bigVector = new BigVector(newComponents);
        bigVector.setMathContext(mathContext);
        return bigVector;
    }
    
    /**
     * Resizes the Big Vector.
     *
     * @param newDim The new dimension of the Big Vector.
     */
    public void redim(int newDim) {
        if (newDim <= 0) {
            components = new BigDecimal[0];
            return;
        }
        
        BigDecimal[] newComponents = new BigDecimal[newDim];
        Arrays.fill(newComponents, BigDecimal.ZERO);
        System.arraycopy(components, 0, newComponents, 0, Math.min(newDim, getDimensionality()));
        components = newComponents;
    }
    
    /**
     * Creates a sub Big Vector from the Big Vector.
     *
     * @param from The index to start the sub Big Vector from.
     * @param to   The index to end the sub Big Vector at.
     * @return The sub Big Vector.
     * @throws IndexOutOfBoundsException When the from or to indices are out of bounds of the Big Vector.
     */
    public BigVector subVector(int from, int to) throws IndexOutOfBoundsException {
        int dim = to - from;
        if ((dim > getDimensionality()) || (from > to) || (from < 0) || (to > getDimensionality())) {
            throw new IndexOutOfBoundsException(componentRangeOutOfBoundsErrorMessage(this, from, to));
        }
        
        List<BigDecimal> componentList = Arrays.asList(components);
        return new BigVector(ListUtility.subList(componentList, from, to));
    }
    
    /**
     * Creates a sub Vector from the Big Vector.
     *
     * @param from The index to start the sub Big Vector from.
     * @return The sub Big Vector.
     * @throws IndexOutOfBoundsException When the from or to indices are out of bounds of the Big Vector.
     * @see #subVector(int, int)
     */
    public BigVector subVector(int from) throws IndexOutOfBoundsException {
        return subVector(from, getDimensionality());
    }
    
    
    //Getters
    
    /**
     * Returns the dimension of the Big Vector.
     *
     * @return The dimension of the Big Vector.
     */
    public int getDimensionality() {
        return components.length;
    }
    
    /**
     * Returns the components of the Big Vector.
     *
     * @return The components of the Big Vector.
     */
    public BigDecimal[] getComponents() {
        return components;
    }
    
    /**
     * Returns the x component of the Big Vector.
     *
     * @return The x component of the Big Vector.
     */
    public BigDecimal getX() {
        return (getDimensionality() > 0) ? get(0) : BigDecimal.ZERO;
    }
    
    /**
     * Returns the y component of the Big Vector.
     *
     * @return The y component of the Big Vector.
     */
    public BigDecimal getY() {
        return (getDimensionality() >= Vector2.DIMENSIONALITY) ? get(1) : BigDecimal.ZERO;
    }
    
    /**
     * Returns the z component of the Big Vector.
     *
     * @return The z component of the Big Vector.
     */
    public BigDecimal getZ() {
        return (getDimensionality() >= Vector3.DIMENSIONALITY) ? get(2) : BigDecimal.ZERO;
    }
    
    /**
     * Returns the w component of the Big Vector.
     *
     * @return The w component of the Big Vector.
     */
    public BigDecimal getW() {
        return (getDimensionality() >= Vector4.DIMENSIONALITY) ? get(3) : BigDecimal.ZERO;
    }
    
    /**
     * Returns a component of the Big Vector.
     *
     * @param index The index of the component.
     * @return The component of the Big Vector at the index.
     * @throws IndexOutOfBoundsException When the Big Vector does not contain a component at the specified index.
     */
    public BigDecimal get(int index) throws IndexOutOfBoundsException {
        if (!BoundUtility.inBounds(index, 0, components.length, true, false)) {
            throw new IndexOutOfBoundsException(componentIndexOutOfBoundsErrorMessage(this, index));
        }
        
        return components[index];
    }
    
    /**
     * Returns the MathContext used when doing BigDecimal math.
     *
     * @return The MathContext used when doing BigDecimal math.
     */
    public MathContext getMathContext() {
        return mathContext;
    }
    
    /**
     * Returns the Big Vector used for justification.
     *
     * @return The Big Vector used for justification.
     */
    public static BigVector getJustificationVector() {
        return JUSTIFICATION_VECTOR.clone();
    }
    
    
    //Setters
    
    /**
     * Sets the x component of the Big Vector.
     *
     * @param x The new x component of the Big Vector.
     */
    public void setX(BigDecimal x) {
        if (getDimensionality() > 0) {
            set(0, x);
        }
    }
    
    /**
     * Sets the y component of the Big Vector.
     *
     * @param y The new y component of the Big Vector.
     */
    public void setY(BigDecimal y) {
        if (getDimensionality() >= Vector2.DIMENSIONALITY) {
            set(1, y);
        }
    }
    
    /**
     * Sets the z component of the Big Vector.
     *
     * @param z The new z component of the Big Vector.
     */
    public void setZ(BigDecimal z) {
        if (getDimensionality() >= Vector3.DIMENSIONALITY) {
            set(2, z);
        }
    }
    
    /**
     * Sets the w component of the Big Vector.
     *
     * @param w The new w component of the Big Vector.
     */
    public void setW(BigDecimal w) {
        if (getDimensionality() >= Vector4.DIMENSIONALITY) {
            set(3, w);
        }
    }
    
    /**
     * Sets the value of a component of the Big Vector.
     *
     * @param index The index of the component to set.
     * @param value The new value of the component.
     * @throws IndexOutOfBoundsException When the Big Vector does not contain a component at the specified index.
     */
    public void set(int index, BigDecimal value) throws IndexOutOfBoundsException {
        if (!BoundUtility.inBounds(index, 0, components.length, true, false)) {
            throw new IndexOutOfBoundsException(componentIndexOutOfBoundsErrorMessage(this, index));
        }
        
        components[index] = value;
    }
    
    /**
     * Sets the MathContext used when doing BigDecimal math.
     *
     * @param mathContext The MathContext used when doing BigDecimal math.
     */
    public void setMathContext(MathContext mathContext) {
        this.mathContext = mathContext;
    }
    
    /**
     * Sets the Big Vector used for justification.
     *
     * @param justificationVector The Big Vector to be used for justification.
     */
    public static void setJustificationVector(BigVector justificationVector) {
        JUSTIFICATION_VECTOR = justificationVector.clone();
    }
    
    
    //Functions
    
    /**
     * Copies the values from one Big Vector to another.
     *
     * @param source The Big Vector to copy the values from.
     * @param dest   The Big Vector to copy the values to.
     * @throws ArithmeticException When the Big Vectors do not have the same dimensionality.
     */
    public static void copyVector(BigVector source, BigVector dest) throws ArithmeticException {
        if (!dest.dimensionalityEqual(source)) {
            throw new ArithmeticException(dimensionalityNotEqualErrorMessage(source, dest));
        }
        
        for (int i = 0; i < source.getDimensionality(); i++) {
            dest.set(i, source.get(i));
        }
    }
    
    /**
     * Creates a unit Big Vector of a certain dimensionality.
     *
     * @param dim The dimensionality of the unit Big Vector
     * @return The unit Big Vector.
     */
    public static BigVector unit(int dim) {
        BigDecimal[] components = new BigDecimal[Math.max(dim, 0)];
        Arrays.fill(components, BigDecimal.ONE);
        return new BigVector(components);
    }
    
    /**
     * Creates an origin Big Vector of a certain dimensionality.
     *
     * @param dim The dimensionality of the unit Big Vector
     * @return The origin Big Vector.
     */
    public static BigVector origin(int dim) {
        BigDecimal[] components = new BigDecimal[Math.max(dim, 0)];
        Arrays.fill(components, BigDecimal.ZERO);
        return new BigVector(components);
    }
    
    /**
     * Calculates the average of a list of Big Vectors.
     *
     * @param vectors The list of Big Vectors.
     * @return The average of the Big Vectors.
     * @throws ArithmeticException When the Big Vectors do not all have the same dimensionality.
     */
    public static BigVector averageVector(List<BigVector> vectors) throws ArithmeticException {
        int dim = -1;
        for (BigVector vector : vectors) {
            if (dim == -1) {
                dim = vector.getDimensionality();
            } else if (!vector.dimensionalityEqual(vectors.get(0))) {
                throw new ArithmeticException(dimensionalityNotEqualErrorMessage(vectors.get(0), vector));
            }
        }
        if (dim == -1) {
            return new BigVector();
        }
        
        BigDecimal[] newComponents = new BigDecimal[vectors.get(0).getDimensionality()];
        for (int c = 0; c < vectors.get(0).getDimensionality(); c++) {
            BigDecimal component = BigDecimal.ZERO;
            for (BigVector vector : vectors) {
                component = component.add(vector.get(c));
            }
            newComponents[c] = component.divide(BigDecimal.valueOf(vectors.size()), vectors.get(0).mathContext);
        }
        BigVector bigVector = new BigVector(newComponents);
        bigVector.setMathContext(vectors.get(0).mathContext);
        return bigVector;
    }
    
    /**
     * Calculates the average of a set of Big Vectors.
     *
     * @param vectors The set of Big Vectors.
     * @return The average of the Big Vectors.
     * @throws ArithmeticException When the Big Vectors do not all have the same dimensionality.
     * @see #averageVector(List)
     */
    public static BigVector averageVector(BigVector... vectors) throws ArithmeticException {
        return averageVector(Arrays.asList(vectors));
    }
    
    /**
     * Calculates the minimum and maximum values from a list of Big Vectors.
     *
     * @param vectors The list of Big Vectors.
     * @return A list of Big Vectors with length equal to the dimensionality of the Big Vectors in the list; with the first Big Vector in the list containing the minimum and maximum of the x coordinates, second Big Vector containing such for y, etc.
     * @throws ArithmeticException When the Big Vectors do not all have the same dimensionality.
     */
    public static List<BigVector> calculateMinMax(List<BigVector> vectors) throws ArithmeticException {
        int dim = -1;
        for (BigVector vector : vectors) {
            if (dim == -1) {
                dim = vector.getDimensionality();
            } else if (!vector.dimensionalityEqual(vectors.get(0))) {
                throw new ArithmeticException(dimensionalityNotEqualErrorMessage(vectors.get(0), vector));
            }
        }
        if (dim == -1) {
            return new ArrayList<>();
        }
        
        List<BigVector> minMax = new ArrayList<>();
        for (int d = 0; d < dim; d++) {
            BigDecimal min = BigDecimal.valueOf(Double.MAX_VALUE);
            BigDecimal max = BigDecimal.valueOf(Double.MIN_VALUE);
            for (BigVector vector : vectors) {
                min = vector.get(d).min(min);
                max = vector.get(d).max(max);
            }
            BigVector bigVector = new BigVector(min, max);
            bigVector.setMathContext(vectors.get(0).mathContext);
            minMax.add(bigVector);
        }
        return minMax;
    }
    
    /**
     * Calculates the minimum and maximum values from a list of Big Vectors.
     *
     * @param vectors The set of Big Vectors.
     * @return A list of Big Vectors with length equal to the dimensionality of the Big Vectors in the list; with the first Big Vector in the list containing the minimum and maximum of the x coordinates, second Big Vector containing such for y, etc.
     * @throws ArithmeticException When the Big Vectors do not all have the same dimensionality.
     * @see #calculateMinMax(List)
     */
    public static List<BigVector> calculateMinMax(BigVector... vectors) throws ArithmeticException {
        return calculateMinMax(Arrays.asList(vectors));
    }
    
    /**
     * Returns the error message to display when two Big Vectors do not have the same dimensionality.
     *
     * @param vector1 The first Big Vector.
     * @param vector2 The seconds Big Vector.
     * @return The error message.
     */
    protected static String dimensionalityNotEqualErrorMessage(BigVector vector1, BigVector vector2) {
        return "The big vectors: " + vector1 + " and " + vector2 + " do not have the same dimensionality";
    }
    
    /**
     * Returns the error message to display when a Big Vectors do not have the minimum dimensionality.
     *
     * @param vector  The Big Vector.
     * @param minimum The minimum dimensionality.
     * @return The error message.
     */
    protected static String dimensionalityMinimumNotMetErrorMessage(BigVector vector, int minimum) {
        return "The big vector: " + vector + " does not have the minimum dimensionality of: " + minimum;
    }
    
    /**
     * Returns the error message to display when the component index of a Big Vector is out of bounds.
     *
     * @param vector The Big Vector.
     * @param index  The index of the component.
     * @return The error message.
     */
    protected static String componentIndexOutOfBoundsErrorMessage(BigVector vector, int index) {
        return "The big vector: " + vector + " does not have a component at index: " + index;
    }
    
    /**
     * Returns the error message to display when a sub range of a Big Vector is out of bounds.
     *
     * @param vector The Big Vector.
     * @param from   The starting index of the range.
     * @param to     The ending index of the range.
     * @return The error message.
     */
    protected static String componentRangeOutOfBoundsErrorMessage(BigVector vector, int from, int to) {
        return "The range: [" + from + "," + to + ") is out of bounds of the big vector: " + vector;
    }
    
}
