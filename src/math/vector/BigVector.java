/*
 * File:    BigVector.java
 * Package: math.vector
 * Author:  Zachary Gill
 */

package math.vector;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

/**
 * Defines the base properties of a BigVector.
 */
public class BigVector {
    
    //Constants
    
    /**
     * A BigDecimal representing 1.
     */
    private static final BigDecimal ONE = BigDecimal.ONE;
    
    /**
     * A BigDecimal representing -1.
     */
    private static final BigDecimal NEGATIVE_ONE = BigDecimal.valueOf(-1);
    
    
    //Fields
    
    /**
     * The array of components that define the Vector.
     */
    protected BigDecimal[] components;
    
    /**
     * The MathContext to use when doing BigDecimal math.
     */
    protected MathContext mathContext = new MathContext(32, RoundingMode.HALF_UP);
    
    
    //Constructors
    
    /**
     * The constructor for a Vector.
     *
     * @param components The components that define the Vector.
     */
    public BigVector(BigDecimal... components) {
        this.components = new BigDecimal[components.length];
        System.arraycopy(components, 0, this.components, 0, components.length);
    }
    
    /**
     * The constructor for a Vector from a list of components.
     *
     * @param components The components that define the Vector, as a list.
     */
    public BigVector(List<BigDecimal> components) {
        this.components = new BigDecimal[components.size()];
        System.arraycopy(components.toArray(new BigDecimal[]{}), 0, this.components, 0, components.size());
    }
    
    /**
     * The constructor for a Vector from another Vector.
     *
     * @param v The Vector.
     */
    public BigVector(Vector v) {
        this.components = new BigDecimal[v.components.length];
        for (int i = 0; i < v.components.length; i++) {
            this.components[i] = BigDecimal.valueOf(v.components[i]);
        }
    }
    
    /**
     * The constructor for a Vector from another Vector.
     *
     * @param v The Vector.
     */
    public BigVector(BigVector v) {
        this.components = new BigDecimal[v.components.length];
        System.arraycopy(v.components, 0, this.components, 0, v.components.length);
        this.mathContext = v.mathContext;
    }
    
    /**
     * The constructor for a Vector from another Vector with added components.
     *
     * @param v          The Vector.
     * @param components The components to add.
     */
    public BigVector(BigVector v, BigDecimal... components) {
        this.components = new BigDecimal[v.components.length + components.length];
        System.arraycopy(v.components, 0, this.components, 0, v.components.length);
        System.arraycopy(components, 0, this.components, v.components.length, components.length);
        this.mathContext = v.mathContext;
    }
    
    
    //Methods
    
    /**
     * Returns a string that represents the Vector.
     *
     * @return A string that represents the Vector.
     */
    @Override
    public String toString() {
        StringBuilder vector = new StringBuilder();
        
        for (BigDecimal component : components) {
            if (!vector.toString().isEmpty()) {
                vector.append(", ");
            }
            vector.append(component.toPlainString());
        }
        
        return '<' + vector.toString() + '>';
    }
    
    /**
     * Determines if another Vector's dimension is equal to this Vector.
     *
     * @param v The other Vector.
     * @return Whether the two Vectors' dimension is equal or not.
     */
    public boolean dimensionsEqual(BigVector v) {
        return (components.length == v.components.length);
    }
    
    /**
     * Determines if another Vector is equal to this Vector.
     *
     * @param v The other Vector.
     * @return Whether the two Vectors are equal or not.
     */
    public boolean equals(BigVector v) {
        if (!dimensionsEqual(v)) {
            return false;
        }
        
        for (int c = 0; c < components.length; c++) {
            if (!Objects.equals(components[c], v.components[c])) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Creates a cloned copy of the Vector.
     *
     * @return The cloned Vector.
     */
    @Override
    public BigVector clone() {
        BigVector v = new BigVector(components);
        v.setMathContext(mathContext);
        return v;
    }
    
    /**
     * Creates a cloned copy of the Vector with its elements reversed.
     *
     * @return The reversed Vector.
     */
    public BigVector reverse() {
        BigDecimal[] reversedComponents = new BigDecimal[components.length];
        for (int i = 0; i < Math.ceil(components.length / 2); i++) {
            reversedComponents[i] = components[components.length - 1 - i];
            reversedComponents[components.length - 1 - i] = components[i];
        }
        BigVector v = new BigVector(reversedComponents);
        v.setMathContext(mathContext);
        return v;
    }
    
    /**
     * Justifies a Vector.
     *
     * @return The Vector.
     */
    public BigVector justify() {
        return this.times(new BigVector(NEGATIVE_ONE, NEGATIVE_ONE, ONE));
    }
    
    /**
     * Calculates the distance between this Vector and another Vector.
     *
     * @param v The other Vector.
     * @return The distance between the two Vectors.
     * @throws ArithmeticException When the two Vectors are not of the same dimension.
     */
    public double distance(BigVector v) throws ArithmeticException {
        if (!dimensionsEqual(v)) {
            throw new ArithmeticException("The vectors: " + toString() + " and " + v.toString() + " are of different dimensions.");
        }
        
        double distance = 0;
        for (int c = 0; c < components.length; c++) {
            distance += Math.pow((v.components[c].subtract(components[c])).doubleValue(), 2);
        }
        return Math.sqrt(distance);
    }
    
    /**
     * Calculates the midpoint between this Vector and another Vector.
     *
     * @param v The other Vector.
     * @return The midpoint between the two Vectors.
     * @throws ArithmeticException When the two Vectors are not of the same dimension.
     */
    public BigVector midpoint(BigVector v) throws ArithmeticException {
        return average(v);
    }
    
    /**
     * Calculates the average of this Vector with a set of Vectors.
     *
     * @param vs The set of Vectors.
     * @return The average of the Vectors.
     * @throws ArithmeticException When the Vectors are not all of the same dimension.
     */
    public BigVector average(BigVector... vs) throws ArithmeticException {
        for (BigVector v : vs) {
            if (!dimensionsEqual(v)) {
                throw new ArithmeticException("The vectors: " + toString() + " and " + v.toString() + " are of different dimensions.");
            }
        }
        
        BigDecimal[] newComponents = new BigDecimal[getDimension()];
        for (int c = 0; c < components.length; c++) {
            BigDecimal component = components[c];
            for (BigVector v : vs) {
                component = component.add(v.components[c]);
            }
            newComponents[c] = component.divide(BigDecimal.valueOf(vs.length + 1), 32, RoundingMode.HALF_UP);
        }
        BigVector v = new BigVector(newComponents);
        v.setMathContext(mathContext);
        return v;
    }
    
    /**
     * Calculates the average of this Vector with a list of Vectors.
     *
     * @param vs The list of Vectors.
     * @return The average of the Vectors.
     * @throws ArithmeticException When the Vectors are not all of the same dimension.
     */
    public BigVector average(List<BigVector> vs) throws ArithmeticException {
        return average(vs.toArray(new BigVector[]{}));
    }
    
    /**
     * Calculates the dot product of this Vector with another Vector.
     *
     * @param v The other Vector.
     * @return The dot product.
     * @throws ArithmeticException When the two Vectors are not of the same dimension.
     */
    public double dot(BigVector v) throws ArithmeticException {
        if (!dimensionsEqual(v)) {
            throw new ArithmeticException("The vectors: " + toString() + " and " + v.toString() + " are of different dimensions.");
        }
        
        double dot = 0;
        for (int c = 0; c < components.length; c++) {
            dot += (components[c].multiply(v.components[c])).doubleValue();
        }
        return dot;
    }
    
    /**
     * Normalizes the Vector.
     *
     * @return The normalized Vector.
     */
    public BigVector normalize() {
        return scale(1.0 / hypotenuse());
    }
    
    /**
     * Performs the square root of the sum of the squares of the components.
     *
     * @return The square root of the sum of the squares of the components.
     */
    public double hypotenuse() {
        return Math.sqrt((getX().multiply(getX())).add(getY().multiply(getY())).add(getZ().multiply(getZ())).doubleValue());
    }
    
    /**
     * Sums the components of the Vector.
     *
     * @return The sum of the components of the Vector.
     */
    public BigDecimal sum() {
        BigDecimal sum = BigDecimal.ZERO;
        for (int c = 0; c < components.length; c++) {
            sum = sum.add(get(c));
        }
        return sum;
    }
    
    /**
     * Moves the decimal place of the components of the Vector left a certain distance.
     *
     * @param move The number of decimal places to move the components of the Vector.
     * @return The Vector after the move has been performed.
     */
    public BigVector movePointLeft(int move) {
        BigDecimal[] newComponents = new BigDecimal[getDimension()];
        for (int c = 0; c < components.length; c++) {
            newComponents[c] = components[c].movePointLeft(move);
        }
        BigVector bv = new BigVector(newComponents);
        bv.setMathContext(mathContext);
        return bv;
    }
    
    /**
     * Moves the decimal place of the components of the Vector right a certain distance.
     *
     * @param move The number of decimal places to move the components of the Vector.
     * @return The Vector after the move has been performed.
     */
    public BigVector movePointRight(int move) {
        BigDecimal[] newComponents = new BigDecimal[getDimension()];
        for (int c = 0; c < components.length; c++) {
            newComponents[c] = components[c].movePointRight(move);
        }
        BigVector bv = new BigVector(newComponents);
        bv.setMathContext(mathContext);
        return bv;
    }
    
    /**
     * Strips the trailing zeros of the components of the Vector.
     *
     * @return The Vector after the stripping has been performed.
     */
    public BigVector stripTrailingZeros() {
        BigDecimal[] newComponents = new BigDecimal[getDimension()];
        for (int c = 0; c < components.length; c++) {
            newComponents[c] = components[c].stripTrailingZeros();
        }
        BigVector bv = new BigVector(newComponents);
        bv.setMathContext(mathContext);
        return bv;
    }
    
    /**
     * Calculates the addition of this Vector and another Vector.
     *
     * @param v The other Vector.
     * @return The Vector produced as a result of the addition.
     * @throws ArithmeticException When the two Vectors are not of the same dimension.
     */
    public BigVector plus(BigVector v) throws ArithmeticException {
        if (!dimensionsEqual(v)) {
            throw new ArithmeticException("The vectors: " + toString() + " and " + v.toString() + " are of different dimensions.");
        }
        
        BigDecimal[] newComponents = new BigDecimal[getDimension()];
        for (int c = 0; c < components.length; c++) {
            newComponents[c] = components[c].add(v.components[c]);
        }
        BigVector bv = new BigVector(newComponents);
        bv.setMathContext(mathContext);
        return bv;
    }
    
    /**
     * Calculates the difference of this Vector and another Vector.
     *
     * @param v The other Vector.
     * @return The Vector produced as a result of the subtraction.
     * @throws ArithmeticException When the two Vectors are not of the same dimension.
     */
    public BigVector minus(BigVector v) throws ArithmeticException {
        if (!dimensionsEqual(v)) {
            throw new ArithmeticException("The vectors: " + toString() + " and " + v.toString() + " are of different dimensions.");
        }
        
        BigDecimal[] newComponents = new BigDecimal[getDimension()];
        for (int c = 0; c < components.length; c++) {
            newComponents[c] = components[c].subtract(v.components[c]);
        }
        BigVector bv = new BigVector(newComponents);
        bv.setMathContext(mathContext);
        return bv;
    }
    
    /**
     * Calculates the product of this Vector and another Vector.
     *
     * @param v The other Vector.
     * @return The Vector produced as a result of the multiplication.
     * @throws ArithmeticException When the two Vectors are not of the same dimension.
     */
    public BigVector times(BigVector v) throws ArithmeticException {
        if (!dimensionsEqual(v)) {
            throw new ArithmeticException("The vectors: " + toString() + " and " + v.toString() + " are of different dimensions.");
        }
        
        BigDecimal[] newComponents = new BigDecimal[getDimension()];
        for (int c = 0; c < components.length; c++) {
            newComponents[c] = components[c].multiply(v.components[c]);
        }
        BigVector bv = new BigVector(newComponents);
        bv.setMathContext(mathContext);
        return bv;
    }
    
    /**
     * Calculates the result of this Vector scaled by a constant.
     *
     * @param d The constant.
     * @return The Vector produced as a result of the scaling.
     * @throws ArithmeticException When the two Vectors are not of the same dimension.
     */
    public BigVector scale(BigDecimal d) throws ArithmeticException {
        BigDecimal[] newComponents = new BigDecimal[getDimension()];
        for (int c = 0; c < components.length; c++) {
            newComponents[c] = components[c].multiply(d);
        }
        BigVector bv = new BigVector(newComponents);
        bv.setMathContext(mathContext);
        return bv;
    }
    
    /**
     * Calculates the result of this Vector scaled by a constant.
     *
     * @param d The constant.
     * @return The Vector produced as a result of the scaling.
     * @throws ArithmeticException When the two Vectors are not of the same dimension.
     */
    public BigVector scale(double d) throws ArithmeticException {
        return scale(BigDecimal.valueOf(d));
    }
    
    /**
     * Calculates the quotient of this Vector and another Vector.
     *
     * @param v The other Vector.
     * @return The Vector produced as a result of the division.
     * @throws ArithmeticException When the two Vectors are not of the same dimension.
     */
    public BigVector dividedBy(BigVector v) throws ArithmeticException {
        if (!dimensionsEqual(v)) {
            throw new ArithmeticException("The vectors: " + toString() + " and " + v.toString() + " are of different dimensions.");
        }
        
        BigDecimal[] newComponents = new BigDecimal[getDimension()];
        for (int c = 0; c < components.length; c++) {
            newComponents[c] = components[c].divide(v.components[c], 32, RoundingMode.HALF_UP);
        }
        BigVector bv = new BigVector(newComponents);
        bv.setMathContext(mathContext);
        return bv;
    }
    
    /**
     * Rounds the components of the Vector.
     *
     * @return The Vector rounded to integers.
     */
    public BigVector round() {
        BigDecimal[] newComponents = new BigDecimal[getDimension()];
        for (int c = 0; c < components.length; c++) {
            newComponents[c] = BigDecimal.valueOf(Math.round(components[c].doubleValue()));
        }
        BigVector bv = new BigVector(newComponents);
        bv.setMathContext(mathContext);
        return bv;
    }
    
    /**
     * Resizes the Vector by dropping the higher-dimensional components.
     *
     * @param newDim The new dimension of the Vector.
     */
    public void redim(int newDim) {
        BigDecimal[] newComponents = new BigDecimal[newDim];
        System.arraycopy(components, 0, newComponents, 0, newDim);
        components = newComponents;
    }
    
    
    //Getters
    
    /**
     * Returns the dimension of the Vector.
     *
     * @return The dimension of the Vector.
     */
    public int getDimension() {
        return components.length;
    }
    
    /**
     * Returns the components of the Vector.
     *
     * @return The components of the Vector.
     */
    public BigDecimal[] getComponents() {
        return components;
    }
    
    /**
     * Returns the x component of the Vector.
     *
     * @return The x component of the Vector.
     */
    public BigDecimal getX() {
        return (getDimension() >= 1) ? get(0) : BigDecimal.ZERO;
    }
    
    /**
     * Returns the y component of the Vector.
     *
     * @return The y component of the Vector.
     */
    public BigDecimal getY() {
        return (getDimension() >= 2) ? get(1) : BigDecimal.ZERO;
    }
    
    /**
     * Returns the z component of the Vector.
     *
     * @return The z component of the Vector.
     */
    public BigDecimal getZ() {
        return (getDimension() >= 3) ? get(2) : BigDecimal.ZERO;
    }
    
    /**
     * Returns the w component of the Vector.
     *
     * @return The w component of the Vector.
     */
    public BigDecimal getW() {
        return (getDimension() >= 4) ? get(3) : BigDecimal.ZERO;
    }
    
    /**
     * Returns a component of the Vector.
     *
     * @param i The index of the component.
     * @return The component of the Vector at the index.
     * @throws IndexOutOfBoundsException When the Vector does not contain a component of the specified index.
     */
    public BigDecimal get(int i) throws IndexOutOfBoundsException {
        if (i >= getDimension() || i < 0) {
            throw new IndexOutOfBoundsException("The vector: " + toString() + " does not have a component of index: " + i);
        }
        
        return components[i];
    }
    
    /**
     * Returns the MathContext used when doing BigDecimal math.
     *
     * @return The MathContext used when doing BigDecimal math.
     */
    public MathContext getMathContext() {
        return mathContext;
    }
    
    
    //Setters
    
    /**
     * Sets the x component of the Vector.
     *
     * @param x The new x component of the Vector.
     */
    public void setX(BigDecimal x) {
        if (getDimension() >= 1) {
            set(0, x);
        }
    }
    
    /**
     * Sets the y component of the Vector.
     *
     * @param y The new y component of the Vector.
     */
    public void setY(BigDecimal y) {
        if (getDimension() >= 2) {
            set(1, y);
        }
    }
    
    /**
     * Sets the z component of the Vector.
     *
     * @param z The new z component of the Vector.
     */
    public void setZ(BigDecimal z) {
        if (getDimension() >= 3) {
            set(2, z);
        }
    }
    
    /**
     * Sets the w component of the Vector.
     *
     * @param w The new w component of the Vector.
     */
    public void setW(BigDecimal w) {
        if (getDimension() >= 4) {
            set(3, w);
        }
    }
    
    /**
     * Sets the value of a component of the Vector.
     *
     * @param i     The index of the component to set.
     * @param value The new value of the component.
     * @throws IndexOutOfBoundsException When the Vector does not contain a component of the specified index.
     */
    public void set(int i, BigDecimal value) throws IndexOutOfBoundsException {
        if (i >= getDimension() || i < 0) {
            throw new IndexOutOfBoundsException("The vector: " + toString() + " does not have a component of index: " + i);
        }
        
        components[i] = value;
    }
    
    /**
     * Sets the MathContext used when doing BigDecimal math.
     *
     * @param mathContext The MathContext used when doing BigDecimal math.
     */
    public void setMathContext(MathContext mathContext) {
        this.mathContext = mathContext;
    }
    
    
    //Functions
    
    /**
     * Calculates the average of a set of Vectors.
     *
     * @param vs The set of Vectors.
     * @return The average of the Vectors.
     * @throws ArithmeticException When the Vectors are not all of the same dimension.
     */
    public static BigVector averageVector(BigVector... vs) throws ArithmeticException {
        int dim = 0;
        for (BigVector v : vs) {
            if (dim == 0) {
                dim = v.getDimension();
            } else if (v.getDimension() != dim) {
                throw new ArithmeticException("The vectors: " + vs[0].toString() + " and " + v.toString() + " are of different dimensions.");
            }
        }
        if (dim == 0) {
            return new BigVector(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        }
        
        BigDecimal[] newComponents = new BigDecimal[vs[0].getDimension()];
        for (int c = 0; c < vs[0].components.length; c++) {
            BigDecimal component = BigDecimal.ZERO;
            for (BigVector v : vs) {
                component = component.add(v.components[c]);
            }
            newComponents[c] = component.divide(BigDecimal.valueOf(vs.length), 32, RoundingMode.HALF_UP);
        }
        BigVector bv = new BigVector(newComponents);
        bv.setMathContext(vs[0].mathContext);
        return bv;
    }
    
}
