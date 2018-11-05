/*
 * File:    Vector.java
 * Package: math.vector
 * Author:  Zachary Gill
 */

package math.vector;

import java.util.List;
import java.util.Objects;

/**
 * Defines the base properties of a Vector.
 */
public class Vector {
    
    //Fields
    
    /**
     * The array of components that define the Vector.
     */
    protected double[] components;
    
    
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
        System.arraycopy(components.toArray(new Double[] {}), 0, this.components, 0, components.size());
    }
    
    /**
     * The constructor for a Vector from another Vector.
     *
     * @param v The Vector.
     */
    public Vector(Vector v) {
        this.components = new double[v.components.length];
        System.arraycopy(v.components, 0, this.components, 0, v.components.length);
    }
    
    /**
     * The constructor for a Vector from another Vector.
     *
     * @param v The Vector.
     */
    public Vector(Vector v, double... components) {
        this.components = new double[v.components.length + components.length];
        System.arraycopy(v.components, 0, this.components, 0, v.components.length);
        System.arraycopy(components, 0, this.components, v.components.length, components.length);
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
        
        for (Double component : components) {
            if (!vector.toString().isEmpty()) {
                vector.append(", ");
            }
            vector.append(component);
        }
        
        return '<' + vector.toString() + '>';
    }
    
    /**
     * Determines if another Vector's dimension is equal to this Vector.
     *
     * @param v The other Vector.
     * @return Whether the two Vectors' dimension is equal or not.
     */
    public boolean dimensionsEqual(Vector v) {
        return (components.length == v.components.length);
    }
    
    /**
     * Determines if another Vector is equal to this Vector.
     *
     * @param v The other Vector.
     * @return Whether the two Vectors are equal or not.
     */
    public boolean equals(Vector v) {
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
    public Vector clone() {
        return new Vector(components);
    }
    
    /**
     * Justifies a Vector.
     *
     * @return The Vector.
     */
    public Vector justify() {
        return this.times(new Vector(-1, -1, 1));
    }
    
    /**
     * Calculates the distance between this Vector and another Vector.
     *
     * @param v The other Vector.
     * @return The distance between the two Vectors.
     *
     * @throws ArithmeticException When the two Vectors are not of the same dimension.
     */
    public double distance(Vector v) throws ArithmeticException {
        if (!dimensionsEqual(v)) {
            throw new ArithmeticException("The vectors: " + toString() + " and " + v.toString() + " are of different dimensions.");
        }
        
        double distance = 0;
        for (int c = 0; c < components.length; c++) {
            distance += Math.pow(v.components[c] - components[c], 2);
        }
        return Math.sqrt(distance);
    }
    
    /**
     * Calculates the midpoint between this Vector and another Vector.
     *
     * @param v The other Vector.
     * @return The midpoint between the two Vectors.
     *
     * @throws ArithmeticException When the two Vectors are not of the same dimension.
     */
    public Vector midpoint(Vector v) throws ArithmeticException {
        return average(v);
    }
    
    /**
     * Calculates the average of this Vector with a set of Vectors.
     *
     * @param vs The set of Vectors.
     * @return The average of the Vectors.
     *
     * @throws ArithmeticException When the Vectors are not all of the same dimension.
     */
    public Vector average(Vector... vs) throws ArithmeticException {
        for (Vector v : vs) {
            if (!dimensionsEqual(v)) {
                throw new ArithmeticException("The vectors: " + toString() + " and " + v.toString() + " are of different dimensions.");
            }
        }
        
        double[] newComponents = new double[getDimension()];
        for (int c = 0; c < components.length; c++) {
            double component = components[c];
            for (Vector v : vs) {
                component += v.components[c];
            }
            newComponents[c] = component / (vs.length + 1);
        }
        return new Vector(newComponents);
    }
    
    /**
     * Calculates the average of this Vector with a list of Vectors.
     *
     * @param vs The list of Vectors.
     * @return The average of the Vectors.
     *
     * @throws ArithmeticException When the Vectors are not all of the same dimension.
     */
    public Vector average(List<Vector> vs) throws ArithmeticException {
        return average(vs.toArray(new Vector[] {}));
    }
    
    /**
     * Calculates the dot product of this Vector with another Vector.
     *
     * @param v The other Vector.
     * @return The dot product.
     *
     * @throws ArithmeticException When the two Vectors are not of the same dimension.
     */
    public double dot(Vector v) throws ArithmeticException {
        if (!dimensionsEqual(v)) {
            throw new ArithmeticException("The vectors: " + toString() + " and " + v.toString() + " are of different dimensions.");
        }
        
        double dot = 0;
        for (int c = 0; c < components.length; c++) {
            dot += (components[c] * v.components[c]);
        }
        return dot;
    }
    
    /**
     * Normalizes the Vector.
     *
     * @return The normalized Vector.
     */
    public Vector normalize() {
        return scale(1 / hypotenuse());
    }
    
    /**
     * Performs the square root of the sum of the squares of the components.
     *
     * @return The square root of the sum of the squares of the components.
     */
    public double hypotenuse() {
        return Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2) + Math.pow(getZ(), 2));
    }
    
    /**
     * Sums the components of the Vector.
     *
     * @return The sum of the components of the Vector.
     */
    public double sum() {
        double sum = 0;
        for (int c = 0; c < components.length; c++) {
            sum += get(c);
        }
        return sum;
    }
    
    /**
     * Calculates the addition of this Vector and another Vector.
     *
     * @param v The other Vector.
     * @return The Vector produced as a result of the addition.
     *
     * @throws ArithmeticException When the two Vectors are not of the same dimension.
     */
    public Vector plus(Vector v) throws ArithmeticException {
        if (!dimensionsEqual(v)) {
            throw new ArithmeticException("The vectors: " + toString() + " and " + v.toString() + " are of different dimensions.");
        }
        
        double[] newComponents = new double[getDimension()];
        for (int c = 0; c < components.length; c++) {
            newComponents[c] = components[c] + v.components[c];
        }
        return new Vector(newComponents);
    }
    
    /**
     * Calculates the difference of this Vector and another Vector.
     *
     * @param v The other Vector.
     * @return The Vector produced as a result of the subtraction.
     *
     * @throws ArithmeticException When the two Vectors are not of the same dimension.
     */
    public Vector minus(Vector v) throws ArithmeticException {
        if (!dimensionsEqual(v)) {
            throw new ArithmeticException("The vectors: " + toString() + " and " + v.toString() + " are of different dimensions.");
        }
        
        double[] newComponents = new double[getDimension()];
        for (int c = 0; c < components.length; c++) {
            newComponents[c] = components[c] - v.components[c];
        }
        return new Vector(newComponents);
    }
    
    /**
     * Calculates the product of this Vector and another Vector.
     *
     * @param v The other Vector.
     * @return The Vector produced as a result of the multiplication.
     *
     * @throws ArithmeticException When the two Vectors are not of the same dimension.
     */
    public Vector times(Vector v) throws ArithmeticException {
        if (!dimensionsEqual(v)) {
            throw new ArithmeticException("The vectors: " + toString() + " and " + v.toString() + " are of different dimensions.");
        }
        
        double[] newComponents = new double[getDimension()];
        for (int c = 0; c < components.length; c++) {
            newComponents[c] = components[c] * v.components[c];
        }
        return new Vector(newComponents);
    }
    
    /**
     * Calculates the result of this Vector scaled by a constant.
     *
     * @param d The constant.
     * @return The Vector produced as a result of the scaling.
     *
     * @throws ArithmeticException When the two Vectors are not of the same dimension.
     */
    public Vector scale(double d) throws ArithmeticException {
        double[] newComponents = new double[getDimension()];
        for (int c = 0; c < components.length; c++) {
            newComponents[c] = components[c] * d;
        }
        return new Vector(newComponents);
    }
    
    /**
     * Calculates the quotient of this Vector and another Vector.
     *
     * @param v The other Vector.
     * @return The Vector produced as a result of the division.
     *
     * @throws ArithmeticException When the two Vectors are not of the same dimension.
     */
    public Vector dividedBy(Vector v) throws ArithmeticException {
        if (!dimensionsEqual(v)) {
            throw new ArithmeticException("The vectors: " + toString() + " and " + v.toString() + " are of different dimensions.");
        }
        
        double[] newComponents = new double[getDimension()];
        for (int c = 0; c < components.length; c++) {
            newComponents[c] = components[c] / v.components[c];
        }
        return new Vector(newComponents);
    }
    
    /**
     * Rounds the components of the Vector.
     *
     * @return The Vector rounded to integers.
     */
    public Vector round() {
        double[] newComponents = new double[getDimension()];
        for (int c = 0; c < components.length; c++) {
            newComponents[c] = Math.round(components[c]);
        }
        return new Vector(newComponents);
    }
    
    /**
     * Resizes the Vector by dropping the higher-dimensional components.
     *
     * @param newDim The new dimension of the Vector.
     */
    public void redim(int newDim) {
        double[] newComponents = new double[newDim];
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
    public double[] getComponents() {
        return components;
    }
    
    /**
     * Returns the x component of the Vector.
     *
     * @return The x component of the Vector.
     */
    public double getX() {
        return (getDimension() >= 1) ? get(0) : 0;
    }
    
    /**
     * Returns the y component of the Vector.
     *
     * @return The y component of the Vector.
     */
    public double getY() {
        return (getDimension() >= 2) ? get(1) : 0;
    }
    
    /**
     * Returns the z component of the Vector.
     *
     * @return The z component of the Vector.
     */
    public double getZ() {
        return (getDimension() >= 3) ? get(2) : 0;
    }
    
    /**
     * Returns the w component of the Vector.
     *
     * @return The w component of the Vector.
     */
    public double getW() {
        return (getDimension() >= 4) ? get(3) : 0;
    }
    
    /**
     * Returns a component of the Vector.
     *
     * @param i The index of the component.
     * @return The component of the Vector at the index.
     *
     * @throws IndexOutOfBoundsException When the Vector does not contain a component of the specified index.
     */
    public double get(int i) throws IndexOutOfBoundsException {
        if (i >= getDimension() || i < 0) {
            throw new IndexOutOfBoundsException("The vector: " + toString() + " does not have a component of index: " + i);
        }
        
        return components[i];
    }
    
    
    //Setters
    
    /**
     * Sets the x component of the Vector.
     *
     * @param x The new x component of the Vector.
     */
    public void setX(double x) {
        if (getDimension() >= 1) {
            set(0, x);
        }
    }
    
    /**
     * Sets the y component of the Vector.
     *
     * @param y The new y component of the Vector.
     */
    public void setY(double y) {
        if (getDimension() >= 2) {
            set(1, y);
        }
    }
    
    /**
     * Sets the z component of the Vector.
     *
     * @param z The new z component of the Vector.
     */
    public void setZ(double z) {
        if (getDimension() >= 3) {
            set(2, z);
        }
    }
    
    /**
     * Sets the w component of the Vector.
     *
     * @param w The new w component of the Vector.
     */
    public void setW(double w) {
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
    public void set(int i, double value) throws IndexOutOfBoundsException {
        if (i >= getDimension() || i < 0) {
            throw new IndexOutOfBoundsException("The vector: " + toString() + " does not have a component of index: " + i);
        }
        
        components[i] = value;
    }
    
    
    //Functions
    
    /**
     * Calculates the average of a set of Vectors.
     *
     * @param vs The set of Vectors.
     * @return The average of the Vectors.
     *
     * @throws ArithmeticException When the Vectors are not all of the same dimension.
     */
    public static Vector averageVector(Vector... vs) throws ArithmeticException {
        int dim = 0;
        for (Vector v : vs) {
            if (dim == 0) {
                dim = v.getDimension();
            } else if (v.getDimension() != dim) {
                throw new ArithmeticException("The vectors: " + vs[0].toString() + " and " + v.toString() + " are of different dimensions.");
            }
        }
        if (dim == 0) {
            return new Vector(0, 0, 0);
        }
        
        double[] newComponents = new double[vs[0].getDimension()];
        for (int c = 0; c < vs[0].components.length; c++) {
            double component = 0;
            for (Vector v : vs) {
                component += v.components[c];
            }
            newComponents[c] = component / vs.length;
        }
        return new Vector(newComponents);
    }
    
}
