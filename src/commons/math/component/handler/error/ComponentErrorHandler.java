/*
 * File:    ComponentErrorHandler.java
 * Package: commons.math.component.handler.error
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.component.handler.error;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

import commons.math.component.ComponentInterface;

public class ComponentErrorHandler implements ComponentErrorHandlerInterface {
    
    //Methods
    
    /**
     * Returns the error message to display when two Components do not have the same dimensionality.
     *
     * @param component1 The first Component.
     * @param component2 The second Component.
     * @return The error message.
     */
    @Override
    public String dimensionalityNotSameErrorMessage(ComponentInterface<?, ?, ?> component1, ComponentInterface<?, ?, ?> component2) {
        return "The " + component1.getName() + ": " + component1 + " and the " + component2.getName() + ": " + component2 + " do not have the same dimensionality";
    }
    
    /**
     * Returns the error message to display when a Component does not have the expected dimensionality.
     *
     * @param component      The Component.
     * @param dimensionality The dimensionality.
     * @return The error message.
     */
    @Override
    public String dimensionalityNotEqualErrorMessage(ComponentInterface<?, ?, ?> component, int dimensionality) {
        return "The " + component.getName() + ": " + component + " does not have the expected dimensionality of: " + dimensionality;
    }
    
    /**
     * Returns the error message to display when two Components do not have the same component type.
     *
     * @param component1 The first Component.
     * @param component2 The second Component.
     * @return The error message.
     */
    @Override
    public String componentTypeNotSameErrorMessage(ComponentInterface<?, ?, ?> component1, ComponentInterface<?, ?, ?> component2) {
        return "The " + component1.getName() + ": " + component1 + " and the " + component2.getName() + ": " + component2 + " do not have the same component type";
    }
    
    /**
     * Returns the error message to display when a Component does not have the expected component type.
     *
     * @param component The Component.
     * @param type      The component type.
     * @param <T>       The type of the component type.
     * @return The error message.
     */
    @Override
    public <T extends Number> String componentTypeNotEqualErrorMessage(ComponentInterface<?, ?, ?> component, Class<T> type) {
        return "The " + component.getName() + ": " + component + " does not have the expected component type of: " + type.getSimpleName();
    }
    
    /**
     * Returns the error message to display when a Component does not have a minimum dimensionality.
     *
     * @param component The Component.
     * @param minimum   The minimum dimensionality.
     * @return The error message.
     */
    @Override
    public String dimensionalityMinimumNotMetErrorMessage(ComponentInterface<?, ?, ?> component, int minimum) {
        return "The " + component.getName() + ": " + component + " does not have the minimum dimensionality of: " + minimum;
    }
    
    /**
     * Returns the error message to display when the number components that define a Component is not the expected number.
     *
     * @param components The components that define the Component.
     * @param length     The expected length.
     * @param <T>        The type of the component type.
     * @return The error message.
     */
    @Override
    public <T extends Number> String componentLengthNotEqualErrorMessage(T[] components, int length) {
        return "The components: " + Arrays.stream(components).map(e -> (e instanceof BigDecimal) ? ((BigDecimal) e).toPlainString() : e.toString()).collect(Collectors.joining(",", "[", "]")) + " has a length of: " + components.length + " but was expecting a length of: " + length;
    }
    
    /**
     * Returns the error message to display when the number components that define a Component is not is not square.
     *
     * @param components The components that define the Component.
     * @param <T>        The type of the component type.
     * @return The error message.
     */
    @Override
    public <T extends Number> String componentLengthNotSquareErrorMessage(T[] components) {
        return "The components: " + Arrays.stream(components).map(e -> (e instanceof BigDecimal) ? ((BigDecimal) e).toPlainString() : e.toString()).collect(Collectors.joining(",", "[", "]")) + " has a length of: " + components.length + " but was expecting a perfect square";
    }
    
    /**
     * Returns the error message to display when the component index of a Component is out of bounds.
     *
     * @param component The Component.
     * @param index     The index of the component.
     * @return The error message.
     */
    @Override
    public String componentIndexOutOfBoundsErrorMessage(ComponentInterface<?, ?, ?> component, int index) {
        return "The " + component.getName() + ": " + component + " does not have a component at index: " + index;
    }
    
    /**
     * Returns the error message to display when the component coordinate of a Component is out of bounds.
     *
     * @param component The Component.
     * @param x         The x coordinate of the component.
     * @param y         The y coordinate of the component.
     * @return The error message.
     */
    @Override
    public String componentCoordinateOutOfBoundsErrorMessage(ComponentInterface<?, ?, ?> component, int x, int y) {
        return "The " + component.getName() + ": " + component + " does not have a component at coordinate: (" + x + ',' + y + ')';
    }
    
    /**
     * Returns the error message to display when a sub range of a Component is out of bounds.
     *
     * @param component The Component.
     * @param from      The starting index of the range.
     * @param to        The ending index of the range.
     * @return The error message.
     */
    @Override
    public String componentRangeOutOfBoundsErrorMessage(ComponentInterface<?, ?, ?> component, int from, int to) {
        return "The range: [" + from + ',' + to + ") is out of bounds of the " + component.getName() + ": " + component;
    }
    
    /**
     * Returns the error message to display when a sub coordinate range of a Component is out of bounds.
     *
     * @param component The Component.
     * @param fromX     The starting x coordinate of the coordinate range.
     * @param fromY     The starting y coordinate of the coordinate range.
     * @param toX       The ending x coordinate of the coordinate range.
     * @param toY       The ending y coordinate of the coordinate range.
     * @return The error message.
     */
    @Override
    public String componentCoordinateRangeOutOfBoundsErrorMessage(ComponentInterface<?, ?, ?> component, int fromX, int fromY, int toX, int toY) {
        return "The coordinate range: (" + fromX + ',' + fromY + ") to (" + toX + ',' + toY + ") is out of bounds of the " + component.getName() + ": " + component;
    }
    
}
