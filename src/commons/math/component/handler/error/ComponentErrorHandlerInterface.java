/*
 * File:    ErrorHandlerInterface.java
 * Package: commons.math.component.handler.error
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.component.handler.error;

import commons.math.component.ComponentInterface;

/**
 * Defines the contract for Component Error Handler classes.
 */
public interface ComponentErrorHandlerInterface {
    
    //Methods
    
    /**
     * Returns the error message to display when two Components do not have the same dimensionality.
     *
     * @param component1 The first Component.
     * @param component2 The second Component.
     * @return The error message.
     */
    String dimensionalityNotSameErrorMessage(ComponentInterface<?, ?, ?> component1, ComponentInterface<?, ?, ?> component2);
    
    /**
     * Returns the error message to display when a Component does not have the expected dimensionality.
     *
     * @param component      The Component.
     * @param dimensionality The dimensionality.
     * @return The error message.
     */
    String dimensionalityNotEqualErrorMessage(ComponentInterface<?, ?, ?> component, int dimensionality);
    
    /**
     * Returns the error message to display when two Components do not have the same component type.
     *
     * @param component1 The first Component.
     * @param component2 The second Component.
     * @return The error message.
     */
    String componentTypeNotSameErrorMessage(ComponentInterface<?, ?, ?> component1, ComponentInterface<?, ?, ?> component2);
    
    /**
     * Returns the error message to display when a Component does not have the expected component type.
     *
     * @param component The Component.
     * @param type      The component type.
     * @return The error message.
     */
    <T extends Number> String componentTypeNotEqualErrorMessage(ComponentInterface<?, ?, ?> component, Class<T> type);
    
    /**
     * Returns the error message to display when a Component does not have a minimum dimensionality.
     *
     * @param component The Component.
     * @param minimum   The minimum dimensionality.
     * @return The error message.
     */
    String dimensionalityMinimumNotMetErrorMessage(ComponentInterface<?, ?, ?> component, int minimum);
    
    /**
     * Returns the error message to display when the number components that define a Component is not the expected number.
     *
     * @param components The components that define the Component.
     * @param length     The expected length.
     * @param <T>        The type of the component type.
     * @return The error message.
     */
    <T extends Number> String componentLengthNotEqualErrorMessage(T[] components, int length);
    
    /**
     * Returns the error message to display when the number components that define a Component is not is not square.
     *
     * @param components The components that define the Component.
     * @param <T>        The type of the component type.
     * @return The error message.
     */
    <T extends Number> String componentLengthNotSquareErrorMessage(T[] components);
    
    /**
     * Returns the error message to display when the component index of a Component is out of bounds.
     *
     * @param component The Component.
     * @param index     The index of the component.
     * @return The error message.
     */
    String componentIndexOutOfBoundsErrorMessage(ComponentInterface<?, ?, ?> component, int index);
    
    /**
     * Returns the error message to display when the component coordinate of a Component is out of bounds.
     *
     * @param component The Component.
     * @param x         The x coordinate of the component.
     * @param y         The y coordinate of the component.
     * @return The error message.
     */
    String componentCoordinateOutOfBoundsErrorMessage(ComponentInterface<?, ?, ?> component, int x, int y);
    
    /**
     * Returns the error message to display when a component range of a Component is out of bounds.
     *
     * @param component The Component.
     * @param from      The starting index of the range.
     * @param to        The ending index of the range.
     * @return The error message.
     */
    String componentRangeOutOfBoundsErrorMessage(ComponentInterface<?, ?, ?> component, int from, int to);
    
    /**
     * Returns the error message to display when a component coordinate range of a Component is out of bounds.
     *
     * @param component The Component.
     * @param fromX     The starting x coordinate of the coordinate range.
     * @param fromY     The starting y coordinate of the coordinate range.
     * @param toX       The ending x coordinate of the coordinate range.
     * @param toY       The ending y coordinate of the coordinate range.
     * @return The error message.
     */
    String componentCoordinateRangeOutOfBoundsErrorMessage(ComponentInterface<?, ?, ?> component, int fromX, int fromY, int toX, int toY);
    
}
