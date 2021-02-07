/*
 * File:    Vector.java
 * Package: commons.math.vector
 * Author:  Zachary Gill
 */

package commons.math.vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import commons.math.BoundUtility;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the base properties of a Vector.
 */
public class Vector {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(Vector.class);
    
    
    //Static Fields
    
    /**
     * The Vector used for justification.
     */
    public static final Vector justificationVector = new Vector(1, 1, 1);
    
    
    //Fields
    
    /**
     * The array of components that define the Vector.
     */
    public double[] components;
    
    
    //Constructors
    
    /**
     * The constructor for a Vector.
     *
     * @param components The components that define the Vector.
     */
    public Vector(double... components) {
        this.components = new double[components.length];
        System.arraycopy(components, 0, this.components, 0, components.length);
    }
    
    /**
     * The constructor for a Vector from a list of components.
     *
     * @param components The components that define the Vector, as a list.
     */
    public Vector(List<Double> components) {
        this.components = new double[components.size()];
        System.arraycopy(ArrayUtils.toPrimitive(components.toArray(new Double[] {})),
                0, this.components, 0, components.size());
    }
    
    /**
     * The constructor for a Vector from another Vector.
     *
     * @param vector The Vector.
     */
    public Vector(Vector vector) {
        this.components = new double[vector.getDimensionality()];
        System.arraycopy(vector.components, 0, this.components, 0, vector.getDimensionality());
    }
    
    /**
     * The constructor for a Vector from another Vector with added components.
     *
     * @param vector     The Vector.
     * @param components The components to add.
     */
    public Vector(Vector vector, double... components) {
        this.components = new double[vector.getDimensionality() + components.length];
        System.arraycopy(vector.components, 0, this.components, 0, vector.getDimensionality());
        System.arraycopy(components, 0, this.components, vector.getDimensionality(), components.length);
    }
    
    
    //Methods
    
    /**
     * Returns a string that represents the Vector.
     *
     * @return A string that represents the Vector.
     */
    @Override
    public String toString() {
        return Arrays.stream(components).mapToObj(String::valueOf)
                .collect(Collectors.joining(", ", "<", ">"));
    }
    
    /**
     * Determines if another Vector is equal to this Vector.
     *
     * @param o The other Vector.
     * @return Whether the two Vectors are equal or not.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vector)) {
            return false;
        }
        Vector vector = (Vector) o;
        
        if (!dimensionalityEqual(vector)) {
            return false;
        }
        
        for (int c = 0; c < getDimensionality(); c++) {
            if (Math.abs(get(c) - vector.get(c)) > 0.000000000001) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Determines if another Vector's dimensionality is equal to this Vector's dimensionality.
     *
     * @param vector The other Vector.
     * @return Whether the two Vectors' dimensionality is equal or not.
     */
    public boolean dimensionalityEqual(Vector vector) {
        return (getDimensionality() == vector.getDimensionality());
    }
    
    /**
     * Creates a cloned copy of the Vector.
     *
     * @return The cloned Vector.
     */
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public Vector clone() {
        return new Vector(components);
    }
    
    /**
     * Creates a cloned copy of the Vector with its elements reversed.
     *
     * @return The reversed Vector.
     */
    public Vector reverse() {
        double[] reversedComponents = new double[getDimensionality()];
        for (int i = 0; i < Math.ceil(getDimensionality() / 2.0); i++) {
            reversedComponents[i] = components[getDimensionality() - 1 - i];
            reversedComponents[getDimensionality() - 1 - i] = components[i];
        }
        return new Vector(reversedComponents);
    }
    
    /**
     * Justifies a Vector.
     *
     * @return The justified Vector.
     */
    public Vector justify() {
        return this.times(justificationVector);
    }
    
    /**
     * Calculates the distance between this Vector and another Vector.
     *
     * @param vector The other Vector.
     * @return The distance between the two Vectors.
     * @throws ArithmeticException When the two Vectors do not have the same dimensionality.
     */
    public double distance(Vector vector) throws ArithmeticException {
        if (!dimensionalityEqual(vector)) {
            throw new ArithmeticException(dimensionalityNotEqualErrorMessage(this, vector));
        }
        
        double distance = 0;
        for (int c = 0; c < getDimensionality(); c++) {
            distance += Math.pow(vector.get(c) - get(c), 2);
        }
        return Math.sqrt(distance);
    }
    
    /**
     * Calculates the midpoint between this Vector and another Vector.
     *
     * @param vector The other Vector.
     * @return The midpoint between the two Vectors.
     * @throws ArithmeticException When the two Vectors do not have the same dimensionality.
     * @see #average(Vector...)
     */
    public Vector midpoint(Vector vector) throws ArithmeticException {
        return average(this, vector);
    }
    
    /**
     * Calculates the average of this Vector with a list of Vectors.
     *
     * @param vectors The list of Vectors.
     * @return The average of the Vectors.
     * @throws ArithmeticException When the Vectors do not all have the same dimensionality.
     */
    public Vector average(List<Vector> vectors) throws ArithmeticException {
        for (Vector vector : vectors) {
            if (!dimensionalityEqual(vector)) {
                throw new ArithmeticException(dimensionalityNotEqualErrorMessage(this, vector));
            }
        }
        
        double[] newComponents = new double[getDimensionality()];
        for (int c = 0; c < getDimensionality(); c++) {
            double component = get(c);
            for (Vector vector : vectors) {
                component += vector.get(c);
            }
            newComponents[c] = component / (vectors.size() + 1);
        }
        return new Vector(newComponents);
    }
    
    /**
     * Calculates the average of this Vector with a set of Vectors.
     *
     * @param vectors The set of Vectors.
     * @return The average of the Vectors.
     * @throws ArithmeticException When the Vectors do not all have the same dimensionality.
     * @see #average(List)
     */
    public Vector average(Vector... vectors) throws ArithmeticException {
        return average(Arrays.asList(vectors));
    }
    
    /**
     * Calculates the dot product of this Vector with another Vector.
     *
     * @param vector The other Vector.
     * @return The dot product.
     * @throws ArithmeticException When the two Vectors do not have the same dimensionality.
     */
    public double dot(Vector vector) throws ArithmeticException {
        if (!dimensionalityEqual(vector)) {
            throw new ArithmeticException(dimensionalityNotEqualErrorMessage(this, vector));
        }
        
        double dot = 0;
        for (int c = 0; c < getDimensionality(); c++) {
            dot += (get(c) * vector.get(c));
        }
        return dot;
    }
    
    /**
     * Normalizes the Vector.
     *
     * @return The normalized Vector.
     */
    public Vector normalize() {
        return scale(1.0 / hypotenuse());
    }
    
    /**
     * Performs the square root of the sum of the squares of the components.
     *
     * @return The square root of the sum of the squares of the components.
     */
    public double hypotenuse() {
        return Math.sqrt(squareSum());
    }
    
    /**
     * Sums the components of the Vector.
     *
     * @return The sum of the components of the Vector.
     */
    public double sum() {
        double sum = 0;
        for (int c = 0; c < getDimensionality(); c++) {
            sum += get(c);
        }
        return sum;
    }
    
    /**
     * Calculates the square sum of the Vector.
     *
     * @return The square sum of the Vector.
     */
    public double squareSum() {
        double squareSum = 0;
        for (int c = 0; c < getDimensionality(); c++) {
            squareSum += Math.pow(get(c), 2);
        }
        return squareSum;
    }
    
    /**
     * Calculates the addition of this Vector and another Vector.
     *
     * @param vector The other Vector.
     * @return The Vector produced as a result of the addition.
     * @throws ArithmeticException When the two Vectors do not have the same dimensionality.
     */
    public Vector plus(Vector vector) throws ArithmeticException {
        if (!dimensionalityEqual(vector)) {
            throw new ArithmeticException(dimensionalityNotEqualErrorMessage(this, vector));
        }
        
        double[] newComponents = new double[getDimensionality()];
        for (int c = 0; c < getDimensionality(); c++) {
            newComponents[c] = get(c) + vector.get(c);
        }
        return new Vector(newComponents);
    }
    
    /**
     * Calculates the difference of this Vector and another Vector.
     *
     * @param vector The other Vector.
     * @return The Vector produced as a result of the subtraction.
     * @throws ArithmeticException When the two Vectors do not have the same dimensionality.
     */
    public Vector minus(Vector vector) throws ArithmeticException {
        if (!dimensionalityEqual(vector)) {
            throw new ArithmeticException(dimensionalityNotEqualErrorMessage(this, vector));
        }
        
        double[] newComponents = new double[getDimensionality()];
        for (int c = 0; c < getDimensionality(); c++) {
            newComponents[c] = get(c) - vector.get(c);
        }
        return new Vector(newComponents);
    }
    
    /**
     * Calculates the product of this Vector and another Vector.
     *
     * @param vector The other Vector.
     * @return The Vector produced as a result of the multiplication.
     * @throws ArithmeticException When the two Vectors do not have the same dimensionality.
     */
    public Vector times(Vector vector) throws ArithmeticException {
        if (!dimensionalityEqual(vector)) {
            throw new ArithmeticException(dimensionalityNotEqualErrorMessage(this, vector));
        }
        
        double[] newComponents = new double[getDimensionality()];
        for (int c = 0; c < getDimensionality(); c++) {
            newComponents[c] = get(c) * vector.get(c);
        }
        return new Vector(newComponents);
    }
    
    /**
     * Calculates the result of this Vector scaled by a value.
     *
     * @param scalar The scalar.
     * @return The Vector produced as a result of the scaling.
     */
    public Vector scale(double scalar) {
        double[] newComponents = new double[getDimensionality()];
        for (int c = 0; c < getDimensionality(); c++) {
            newComponents[c] = get(c) * scalar;
        }
        return new Vector(newComponents);
    }
    
    /**
     * Calculates the quotient of this Vector and another Vector.
     *
     * @param vector The other Vector.
     * @return The Vector produced as a result of the division.
     * @throws ArithmeticException When the two Vectors do not have the same dimensionality.
     */
    public Vector dividedBy(Vector vector) throws ArithmeticException {
        if (!dimensionalityEqual(vector)) {
            throw new ArithmeticException(dimensionalityNotEqualErrorMessage(this, vector));
        }
        
        double[] newComponents = new double[getDimensionality()];
        for (int c = 0; c < getDimensionality(); c++) {
            newComponents[c] = get(c) / vector.get(c);
        }
        return new Vector(newComponents);
    }
    
    /**
     * Rounds the components of the Vector.
     *
     * @return The Vector rounded to integers.
     */
    public Vector round() {
        double[] newComponents = new double[getDimensionality()];
        for (int c = 0; c < getDimensionality(); c++) {
            newComponents[c] = Math.round(get(c));
        }
        return new Vector(newComponents);
    }
    
    /**
     * Resizes the Vector.
     *
     * @param newDim The new dimensionality of the Vector.
     */
    public void redim(int newDim) {
        double[] newComponents = new double[newDim];
        System.arraycopy(components, 0, newComponents, 0, newDim);
        components = newComponents;
    }
    
    
    //Getters
    
    /**
     * Returns the dimensionality of the Vector.
     *
     * @return The dimensionality of the Vector.
     */
    public int getDimensionality() {
        return components.length;
    }
    
    /**
     * Returns the components of the Vector.
     *
     * @return The components of the Vector.
     */
    public double[] getComponents() {
        return components;
    }
    
    /**
     * Returns the x component of the Vector.
     *
     * @return The x component of the Vector.
     */
    public double getX() {
        return (getDimensionality() > 0) ? get(0) : 0;
    }
    
    /**
     * Returns the y component of the Vector.
     *
     * @return The y component of the Vector.
     */
    public double getY() {
        return (getDimensionality() >= Vector2.DIMENSIONALITY) ? get(1) : 0;
    }
    
    /**
     * Returns the z component of the Vector.
     *
     * @return The z component of the Vector.
     */
    public double getZ() {
        return (getDimensionality() >= Vector3.DIMENSIONALITY) ? get(2) : 0;
    }
    
    /**
     * Returns the w component of the Vector.
     *
     * @return The w component of the Vector.
     */
    public double getW() {
        return (getDimensionality() >= Vector4.DIMENSIONALITY) ? get(3) : 0;
    }
    
    /**
     * Returns a component of the Vector.
     *
     * @param index The index of the component.
     * @return The component of the Vector at the specified index.
     * @throws IndexOutOfBoundsException When the Vector does not contain a component at the specified index.
     */
    public double get(int index) throws IndexOutOfBoundsException {
        if (!BoundUtility.inBounds(index, 0, components.length, true, false)) {
            throw new IndexOutOfBoundsException(componentIndexOutOfRangeError(this, index));
        }
        
        return components[index];
    }
    
    /**
     * Returns the Vector used for justification.
     *
     * @return The Vector used for justification.
     */
    public static Vector getJustificationVector() {
        return justificationVector;
    }
    
    
    //Setters
    
    /**
     * Sets the x component of the Vector.
     *
     * @param x The new x component of the Vector.
     */
    public void setX(double x) {
        if (getDimensionality() > 0) {
            set(0, x);
        }
    }
    
    /**
     * Sets the y component of the Vector.
     *
     * @param y The new y component of the Vector.
     */
    public void setY(double y) {
        if (getDimensionality() >= Vector2.DIMENSIONALITY) {
            set(1, y);
        }
    }
    
    /**
     * Sets the z component of the Vector.
     *
     * @param z The new z component of the Vector.
     */
    public void setZ(double z) {
        if (getDimensionality() >= Vector3.DIMENSIONALITY) {
            set(2, z);
        }
    }
    
    /**
     * Sets the w component of the Vector.
     *
     * @param w The new w component of the Vector.
     */
    public void setW(double w) {
        if (getDimensionality() >= Vector4.DIMENSIONALITY) {
            set(3, w);
        }
    }
    
    /**
     * Sets the value of a component of the Vector.
     *
     * @param index The index of the component to set.
     * @param value The new value of the component.
     * @throws IndexOutOfBoundsException When the Vector does not contain a component at the specified index.
     */
    public void set(int index, double value) throws IndexOutOfBoundsException {
        if (!BoundUtility.inBounds(index, 0, components.length, true, false)) {
            throw new IndexOutOfBoundsException(componentIndexOutOfRangeError(this, index));
        }
        
        components[index] = value;
    }
    
    /**
     * Sets the Vector used for justification.
     *
     * @param justificationVector The Vector to be used for justification.
     */
    public static void setJustificationVector(Vector justificationVector) {
        copyVector(justificationVector, Vector.justificationVector);
    }
    
    
    //Functions
    
    /**
     * Copies the values from one Vector to another.
     *
     * @param source The Vector to copy the values from.
     * @param dest   The Vector to copy the values to.
     * @throws ArithmeticException When the Vectors do not have the same dimensionality.
     */
    public static void copyVector(Vector source, Vector dest) throws ArithmeticException {
        int dim = source.getDimensionality();
        if (dest.getDimensionality() != dim) {
            throw new ArithmeticException(dimensionalityNotEqualErrorMessage(source, dest));
        }
        
        for (int i = 0; i < dim; i++) {
            dest.set(i, source.get(i));
        }
    }
    
    /**
     * Calculates the average of a list of Vectors.
     *
     * @param vectors The list of Vectors.
     * @return The average of the Vectors.
     * @throws ArithmeticException When the Vectors do not all have the same dimensionality.
     */
    public static Vector averageVector(List<Vector> vectors) throws ArithmeticException {
        int dim = 0;
        for (Vector vector : vectors) {
            if (dim == 0) {
                dim = vector.getDimensionality();
            } else if (vector.getDimensionality() != dim) {
                throw new ArithmeticException(dimensionalityNotEqualErrorMessage(vectors.get(0), vector));
            }
        }
        if (dim == 0) {
            return new Vector(0, 0, 0);
        }
        
        double[] newComponents = new double[vectors.get(0).getDimensionality()];
        for (int c = 0; c < vectors.get(0).getDimensionality(); c++) {
            double component = 0;
            for (Vector vector : vectors) {
                component += vector.get(c);
            }
            newComponents[c] = component / vectors.size();
        }
        return new Vector(newComponents);
    }
    
    /**
     * Calculates the average of a set of Vectors.
     *
     * @param vectors The set of Vectors.
     * @return The average of the Vectors.
     * @throws ArithmeticException When the Vectors do not all have the same dimensionality.
     * @see #averageVector(List)
     */
    public static Vector averageVector(Vector... vectors) throws ArithmeticException {
        return averageVector(Arrays.asList(vectors));
    }
    
    /**
     * Calculates the minimum and maximum values from a list of Vectors.
     *
     * @param vectors The list of Vectors.
     * @return A list of Vectors with length equal to the dimensionality of the Vectors in the list; with the first Vector in the list containing the minimum and maximum of the x coordinates, second Vector containing such for y, etc.
     * @throws ArithmeticException When the Vectors do not all have the same dimensionality.
     */
    public static List<Vector> calculateMinMax(List<Vector> vectors) throws ArithmeticException {
        int dim = 0;
        for (Vector vector : vectors) {
            if (dim == 0) {
                dim = vector.getDimensionality();
            } else if (vector.getDimensionality() != dim) {
                throw new ArithmeticException(dimensionalityNotEqualErrorMessage(vectors.get(0), vector));
            }
        }
        if (dim == 0) {
            return new ArrayList<>();
        }
        
        List<Vector> minMax = new ArrayList<>();
        for (int d = 0; d < dim; d++) {
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;
            for (Vector vector : vectors) {
                min = Math.min(vector.get(d), min);
                max = Math.max(vector.get(d), max);
            }
            minMax.add(new Vector(min, max));
        }
        return minMax;
    }
    
    /**
     * Calculates the minimum and maximum values from a list of Vectors.
     *
     * @param vectors The set of Vectors.
     * @return A list of Vectors with length equal to the dimensionality of the Vectors in the list; with the first Vector in the list containing the minimum and maximum of the x coordinates, second Vector containing such for y, etc.
     * @throws ArithmeticException When the Vectors do not all have the same dimensionality.
     * @see #calculateMinMax(List)
     */
    public static List<Vector> calculateMinMax(Vector... vectors) throws ArithmeticException {
        return calculateMinMax(Arrays.asList(vectors));
    }
    
    /**
     * Returns the error message to display when two Vectors do not have the same dimensionality.
     *
     * @param vector1 The first Vector.
     * @param vector2 The second Vector.
     * @return The error message.
     */
    protected static String dimensionalityNotEqualErrorMessage(Vector vector1, Vector vector2) {
        return "The vectors: " + vector1 + " and " + vector2 + " do not have the same dimensionality.";
    }
    
    /**
     * Returns the error message to display when a Vectors do not have the minimum dimensionality.
     *
     * @param vector  The Vector.
     * @param minimum The minimum dimensionality.
     * @return The error message.
     */
    protected static String dimensionalityMinimumNotMetErrorMessage(Vector vector, int minimum) {
        return "The vector: " + vector + " do not have the minimum dimensionality of: " + minimum + ".";
    }
    
    /**
     * Returns the error message to display when the component index of a Vector is out of range.
     *
     * @param vector The Vector.
     * @param index  The index of the component.
     * @return The error message.
     */
    protected static String componentIndexOutOfRangeError(Vector vector, int index) {
        return "The vector: " + vector + " does not have a component at index: " + index;
    }
    
}
