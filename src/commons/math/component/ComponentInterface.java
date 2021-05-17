/*
 * File:    ComponentInterface.java
 * Package: commons.math.component
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.component;

import java.util.List;

import commons.math.component.handler.error.ComponentErrorHandlerInterface;
import commons.math.component.handler.math.ComponentMathHandlerInterface;

/**
 * Defines the contract for Component classes.
 *
 * @param <T> The component type of the Component.
 * @param <I> The type of the Component.
 * @param <N> The subclass component type of the Component.
 */
public interface ComponentInterface<T extends Number, I extends ComponentInterface<?, ?, ?>, N extends Number> {
    
    //Methods
    
    /**
     * Returns a string that represents the Component.
     *
     * @return A string that represents the Component.
     */
    @Override
    String toString();
    
    /**
     * Determines if another Component is equal to this Component.
     *
     * @param o The other Component.
     * @return Whether the two Component are equal or not.
     */
    @Override
    boolean equals(Object o);
    
    /**
     * Determines if another Component's dimensionality is equal to this Component's dimensionality.
     *
     * @param other The other Component.
     * @param <J>   The type of the other Component.
     * @return Whether the two Component's dimensionality is equal or not.
     */
    <J extends ComponentInterface<?, ?, ?>> boolean dimensionalityEqual(J other);
    
    /**
     * Determines if another Component's length is equal to this Component's length.
     *
     * @param other The other Component.
     * @param <J>   The type of the other Component.
     * @return Whether the two Component's lengths are equal or not.
     */
    <J extends ComponentInterface<?, ?, ?>> boolean lengthEqual(J other);
    
    /**
     * Determines if another Component's component type is equal to this Component's component type.
     *
     * @param other The other Component.
     * @param <J>   The type of the other Component.
     * @return Whether the two Component's component types are equal or not.
     */
    <J extends ComponentInterface<?, ?, ?>> boolean componentTypeEqual(J other);
    
    /**
     * Creates a cloned copy of the Component.
     *
     * @return The cloned Component.
     */
    I cloned();
    
    /**
     * Creates an empty copy of the Component.
     *
     * @return The empty copy of the Component.
     */
    I emptyCopy();
    
    /**
     * Creates a new Vector instance of the specified dimensionality.
     *
     * @param dim The dimensionality of the new Vector.
     * @return The new Vector.
     */
    I createNewInstance(int dim);
    
    /**
     * Creates a cloned copy of this Component with its elements reversed.
     *
     * @return The reversed Component.
     */
    I reverse();
    
    /**
     * Calculates the distance between this Component and another Component.
     *
     * @param other The other Component.
     * @return The distance between the two Components.
     * @throws ArithmeticException When the two Components do not have the same dimensionality.
     */
    T distance(I other) throws ArithmeticException;
    
    /**
     * Calculates the midpoint between this Component and another Component.
     *
     * @param other The other Component.
     * @return The midpoint between the two Components.
     * @throws ArithmeticException When the two Components do not have the same dimensionality.
     */
    I midpoint(I other) throws ArithmeticException;
    
    /**
     * Calculates the average of this Component with a list of Components.
     *
     * @param others The list of other Components.
     * @return The average of the Components.
     * @throws ArithmeticException When the Components do not all have the same dimensionality.
     */
    I average(List<I> others) throws ArithmeticException;
    
    /**
     * Calculates the average of this Component with a set of Components.
     *
     * @param others The set of other Components.
     * @return The average of the Components.
     * @throws ArithmeticException When the Components do not all have the same dimensionality.
     */
    @SuppressWarnings("unchecked")
    I average(I... others) throws ArithmeticException;
    
    /**
     * Sums the components of the Component.
     *
     * @return The sum of the components of the Component.
     */
    T sum();
    
    /**
     * Calculates the square sum of the Component.
     *
     * @return The square sum of the Component.
     */
    T squareSum();
    
    /**
     * Calculates the addition of this Component and another Component.
     *
     * @param other The other Component.
     * @return The Component produced as a result of the addition.
     * @throws ArithmeticException When the two Components do not have the same dimensionality.
     */
    I plus(I other) throws ArithmeticException;
    
    /**
     * Calculates the difference of this Component and another Component.
     *
     * @param other The other Component.
     * @return The Component produced as a result of the subtraction.
     * @throws ArithmeticException When the two Components do not have the same dimensionality.
     */
    I minus(I other) throws ArithmeticException;
    
    /**
     * Calculates the product of this Component and another Component.
     *
     * @param other The other Component.
     * @return The Component produced as a result of the multiplication.
     * @throws ArithmeticException When the two Components do not have the same dimensionality.
     */
    I times(I other) throws ArithmeticException;
    
    /**
     * Calculates the result of this Component scaled by a value.
     *
     * @param scalar The scalar.
     * @return The Component produced as a result of the scaling.
     */
    I scale(Number scalar);
    
    /**
     * Calculates the quotient of this Component scaled by a value.
     *
     * @param other The other Component.
     * @return The Component produced as a result of the division.
     * @throws ArithmeticException When the two Components do not have the same dimensionality, or if a component in the divisor Component is 0.
     */
    I dividedBy(I other) throws ArithmeticException;
    
    /**
     * Rounds the components of the Component.
     *
     * @return The Component rounded to integers.
     */
    I round();
    
    /**
     * Copies this Component to another Big Component.
     *
     * @param to The Component to copy to.
     */
    void copy(I to);
    
    /**
     * Copies this Component's metadata to another Component.
     *
     * @param to The Component to copy the metadata to.
     */
    void copyMeta(I to);
    
    /**
     * Resizes the Component.
     *
     * @param newDim The new dimensionality of the Component.
     */
    void redim(int newDim);
    
    /**
     * Calculates the Component's length from its dimensionality.
     *
     * @param dim The dimensionality of the Component.
     * @return The Component's length.
     */
    int dimensionalityToLength(int dim);
    
    /**
     * Calculates the Component's length from its dimensionality.
     *
     * @return The Component's length.
     */
    int dimensionalityToLength();
    
    /**
     * Calculates the Component's dimensionality from its length.
     *
     * @param length The length of the Component.
     * @return The Component's dimensionality.
     */
    int lengthToDimensionality(int length);
    
    /**
     * Calculates the Component's dimensionality from its length.
     *
     * @return The Component's dimensionality.
     */
    int lengthToDimensionality();
    
    /**
     * Calculates and sets the Component's dimensionality using its length.
     */
    void calculateDimensionality();
    
    
    //Getters
    
    /**
     * Returns the raw components that define the Component.
     *
     * @return The raw components that define the Component.
     */
    T[] getRawComponents();
    
    /**
     * Returns the components that define the Component.
     *
     * @return The components that define the Component.
     */
    T[] getComponents();
    
    /**
     * Returns a raw component of the Component.
     *
     * @param index The index of the component.
     * @return The raw component of the Component at the specified index.
     * @throws IndexOutOfBoundsException When the Component does not contain a component at the specified index.
     */
    T getRaw(int index) throws IndexOutOfBoundsException;
    
    /**
     * Returns a component of the Component.
     *
     * @param index The index of the component.
     * @return The component of the Component at the specified index.
     * @throws IndexOutOfBoundsException When the Component does not contain a component at the specified index.
     */
    T get(int index) throws IndexOutOfBoundsException;
    
    /**
     * Returns the dimensionality of the Component.
     *
     * @return The dimensionality of the Component.
     */
    int getDimensionality();
    
    /**
     * Returns the length of the Component.
     *
     * @return The length of the Component.
     */
    int getLength();
    
    /**
     * Returns the type of the Component.
     *
     * @return The type of the Component.
     */
    @SuppressWarnings("rawtypes")
    default Class<? extends ComponentInterface> getComponentClass() {
        return ComponentInterface.class;
    }
    
    /**
     * Returns the type of the components that define the Component.
     *
     * @return The type of the components that define the Component.
     */
    default Class<? extends Number> getType() {
        return Number.class;
    }
    
    /**
     * Returns the Component Math Handler for the Component.
     *
     * @return The Component Math Handler for the Component.
     */
    @SuppressWarnings("rawtypes")
    ComponentMathHandlerInterface getHandler();
    
    /**
     * Returns the Component Error Handler for the Component.
     *
     * @return The Component Error Handler for the Component.
     */
    ComponentErrorHandlerInterface getErrorHandler();
    
    /**
     * Returns the name of the type of Component.
     *
     * @return The name of the type of Component.
     */
    default String getName() {
        return "Component Interface";
    }
    
    /**
     * Returns the plural name of the type of the Component.
     *
     * @return The plural name of the type of the Component.
     */
    default String getNamePlural() {
        return getName() + 's';
    }
    
    /**
     * Returns the precision to use in comparisons.
     *
     * @return The precision to use in comparisons.
     */
    T getPrecision();
    
    /**
     * Returns whether this Component is resizeable or not.
     *
     * @return Whether this Component is resizeable or not.
     */
    boolean isResizeable();
    
    
    //Setters
    
    /**
     * Sets the components that define the Component.
     *
     * @param newComponents The components that define the Component.
     */
    void setComponents(N[] newComponents);
    
    /**
     * Sets the value of a component of the Component.
     *
     * @param index The index of the component to set.
     * @param value The new value of the component.
     * @throws IndexOutOfBoundsException When the Component does not contain a component at the specified index.
     */
    void set(int index, N value) throws IndexOutOfBoundsException;
    
}
