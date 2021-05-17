/*
 * File:    BaseComponent.java
 * Package: commons.math.component
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import commons.list.ArrayUtility;
import commons.math.component.handler.error.ComponentErrorHandlerInterface;
import commons.math.component.handler.error.ComponentErrorHandlerProvider;
import commons.math.component.handler.math.ComponentMathHandlerInterface;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the base properties of a Component.
 *
 * @param <T> The component type of the Component.
 * @param <I> The type of the Component.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class BaseComponent<T extends Number, I extends BaseComponent<?, ?>> implements ComponentInterface<Number, I, T> {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(BaseComponent.class);
    
    
    //Static Fields
    
    /**
     * The Error Handler to output errors.
     */
    protected static ComponentErrorHandlerInterface errorHandler = ComponentErrorHandlerProvider.getErrorHandler();
    
    
    //Fields
    
    /**
     * The components that define the Component.
     */
    protected T[] components;
    
    /**
     * The dimensionality of the Component.
     */
    protected int dimensionality;
    
    /**
     * The type of the components that define the Component.
     */
    private final Class<T> type;
    
    /**
     * The type of the Component.
     */
    private final Class<I> componentClass;
    
    /**
     * The Math Handler to perform component math operations.
     */
    protected ComponentMathHandlerInterface<T> handler;
    
    
    //Constructors
    
    /**
     * The generic constructor for a Base Component.
     *
     * @param type    The type of the components that define the Component.
     * @param handler The Math Handler to perform component math operations.
     */
    protected BaseComponent(Class<T> type, ComponentMathHandlerInterface<T> handler) {
        this.type = type;
        this.componentClass = (Class<I>) getClass();
        this.handler = handler;
    }
    
    
    //Methods
    
    /**
     * Returns a string that represents the Component.
     *
     * @return A string that represents the Component.
     */
    @Override
    public String toString() {
        return Arrays.stream(getComponents()).map(String::valueOf)
                .collect(Collectors.joining(", ", "[", "]"));
    }
    
    /**
     * Determines if another Component is equal to this Component.
     *
     * @param o The other Component.
     * @return Whether the two Component are equal or not.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BaseComponent) ||
                !((BaseComponent) o).getType().equals(getType())) {
            return false;
        }
        BaseComponent other = (BaseComponent) o;
        
        if (!dimensionalityEqual(other) || !lengthEqual(other)) {
            return false;
        }
        
        return IntStream.range(0, getLength()).boxed().allMatch(e ->
                getHandler().isEqual(getRawComponents()[e], other.getRawComponents()[e]));
    }
    
    /**
     * Determines if another Component's dimensionality is equal to this Component's dimensionality.
     *
     * @param other The other Component.
     * @return Whether the two Component's dimensionality is equal or not.
     */
    @Override
    public final <J extends ComponentInterface<?, ?, ?>> boolean dimensionalityEqual(J other) {
        return (other != null) && (getDimensionality() == other.getDimensionality());
    }
    
    /**
     * Determines if another Component's length is equal to this Component's length.
     *
     * @param other The other Component.
     * @return Whether the two Component's lengths are equal or not.
     */
    @Override
    public final <J extends ComponentInterface<?, ?, ?>> boolean lengthEqual(J other) {
        return (other != null) && (getLength() == other.getLength());
    }
    
    /**
     * Determines if another Component's component type is equal to this Component's component type.
     *
     * @param other The other Component.
     * @return Whether the two Component's component types are equal or not.
     */
    @Override
    public final <J extends ComponentInterface<?, ?, ?>> boolean componentTypeEqual(J other) {
        return (other != null) && (getType() == other.getType());
    }
    
    /**
     * Creates a cloned copy of the Component.
     *
     * @return The cloned Component.
     */
    @Override
    public abstract I cloned();
    
    /**
     * Creates an empty copy of the Component.
     *
     * @return The empty copy of the Component.
     */
    @Override
    public abstract I emptyCopy();
    
    /**
     * Creates a new Component instance of the specified dimensionality.
     *
     * @param dim The dimensionality of the new Component.
     * @return The new Component.
     */
    @Override
    public abstract I createNewInstance(int dim);
    
    /**
     * Creates a cloned copy of this Component with its elements reversed.
     *
     * @return The reversed Component.
     */
    @Override
    public I reverse() {
        I result = cloned();
        ArrayUtils.reverse(result.getRawComponents());
        copyMeta(result);
        return result;
    }
    
    /**
     * Calculates the distance between this Component and another Component.
     *
     * @param other The other Component.
     * @return The distance between the two Components.
     * @throws ArithmeticException When the two Components do not have the same dimensionality.
     */
    @Override
    public T distance(I other) throws ArithmeticException {
        ComponentErrorHandlerProvider.assertDimensionalitySame(this, other);
        
        Number distance = getHandler().zero();
        for (int c = 0; c < getLength(); c++) {
            distance = getHandler().add(distance,
                    getHandler().power(getHandler().subtract(other.getRawComponents()[c], getRawComponents()[c]), getHandler().valueOf(2)));
        }
        return (T) getHandler().sqrt(distance);
    }
    
    /**
     * Calculates the midpoint between this Component and another Component.
     *
     * @param other The other Component.
     * @return The midpoint between the two Components.
     * @throws ArithmeticException When the two Components do not have the same dimensionality.
     * @see #average(BaseComponent[])
     */
    @Override
    public I midpoint(I other) throws ArithmeticException {
        return average(other);
    }
    
    /**
     * Calculates the average of this Component with a list of Components.
     *
     * @param others The list of other Components.
     * @return The average of the Components.
     * @throws ArithmeticException When the Components do not all have the same dimensionality.
     */
    @Override
    public I average(List<I> others) throws ArithmeticException {
        for (I other : others) {
            ComponentErrorHandlerProvider.assertDimensionalitySame(this, other);
        }
        
        I result = emptyCopy();
        for (int c = 0; c < getLength(); c++) {
            Number component = getRawComponents()[c];
            for (I other : others) {
                component = getHandler().add(component,
                        other.getRawComponents()[c]);
            }
            result.getRawComponents()[c] = getHandler().divide(component, getHandler().valueOf(others.size() + 1));
        }
        copyMeta(result);
        return result;
    }
    
    /**
     * Calculates the average of this Component with a set of Components.
     *
     * @param others The set of other Components.
     * @return The average of the Components.
     * @throws ArithmeticException When the Components do not all have the same dimensionality.
     * @see #average(List)
     */
    @Override
    public I average(I... others) throws ArithmeticException {
        return average(Arrays.asList(others));
    }
    
    /**
     * Sums the components of the Component.
     *
     * @return The sum of the components of the Component.
     */
    @Override
    public T sum() {
        Number sum = getHandler().zero();
        for (int c = 0; c < getLength(); c++) {
            sum = getHandler().add(sum,
                    getRawComponents()[c]);
        }
        return (T) sum;
    }
    
    /**
     * Calculates the square sum of the Component.
     *
     * @return The square sum of the Component.
     */
    @Override
    public T squareSum() {
        Number squareSum = getHandler().zero();
        for (int c = 0; c < getLength(); c++) {
            squareSum = getHandler().add(squareSum,
                    getHandler().power(getRawComponents()[c], getHandler().valueOf(2)));
        }
        return (T) squareSum;
    }
    
    /**
     * Calculates the addition of this Component and another Component.
     *
     * @param other The other Component.
     * @return The Component produced as a result of the addition.
     * @throws ArithmeticException When the two Components do not have the same dimensionality.
     */
    @Override
    public I plus(I other) throws ArithmeticException {
        ComponentErrorHandlerProvider.assertDimensionalitySame(this, other);
        
        I result = emptyCopy();
        for (int c = 0; c < getLength(); c++) {
            result.getRawComponents()[c] = getHandler().add(getRawComponents()[c],
                    other.getRawComponents()[c]);
        }
        copyMeta(result);
        return result;
    }
    
    /**
     * Calculates the difference of this Component and another Component.
     *
     * @param other The other Component.
     * @return The Component produced as a result of the subtraction.
     * @throws ArithmeticException When the two Components do not have the same dimensionality.
     */
    @Override
    public I minus(I other) throws ArithmeticException {
        ComponentErrorHandlerProvider.assertDimensionalitySame(this, other);
        
        I result = emptyCopy();
        for (int c = 0; c < getLength(); c++) {
            result.getRawComponents()[c] = getHandler().subtract(getRawComponents()[c], other.getRawComponents()[c]);
        }
        copyMeta(result);
        return result;
    }
    
    /**
     * Calculates the product of this Component and another Component.
     *
     * @param other The other Component.
     * @return The Component produced as a result of the multiplication.
     * @throws ArithmeticException When the two Components do not have the same dimensionality.
     */
    @Override
    public I times(I other) throws ArithmeticException {
        ComponentErrorHandlerProvider.assertDimensionalitySame(this, other);
        
        I result = emptyCopy();
        for (int c = 0; c < getLength(); c++) {
            result.getRawComponents()[c] = getHandler().multiply(getRawComponents()[c], other.getRawComponents()[c]);
        }
        copyMeta(result);
        return result;
    }
    
    /**
     * Calculates the result of this Component scaled by a value.
     *
     * @param scalar The scalar.
     * @return The Component produced as a result of the scaling.
     */
    @Override
    public I scale(Number scalar) {
        I result = emptyCopy();
        for (int c = 0; c < getLength(); c++) {
            result.getRawComponents()[c] = getHandler().multiply(getRawComponents()[c], getHandler().valueOf(scalar));
        }
        copyMeta(result);
        return result;
    }
    
    /**
     * Calculates the quotient of this Component scaled by a value.
     *
     * @param other The other Component.
     * @return The Component produced as a result of the division.
     * @throws ArithmeticException When the two Components do not have the same dimensionality, or if a component in the divisor Component is 0.
     */
    @Override
    public I dividedBy(I other) throws ArithmeticException {
        ComponentErrorHandlerProvider.assertDimensionalitySame(this, other);
        if (Arrays.stream(other.getRawComponents()).anyMatch(e -> getHandler().isZero(e))) {
            throw new ArithmeticException("Attempted to divide by zero");
        }
        
        I result = emptyCopy();
        for (int c = 0; c < getLength(); c++) {
            result.getRawComponents()[c] = getHandler().divide(getRawComponents()[c], other.getRawComponents()[c]);
        }
        copyMeta(result);
        return result;
    }
    
    /**
     * Rounds the components of the Component.
     *
     * @return The Component rounded to integers.
     */
    @Override
    public I round() {
        I result = emptyCopy();
        for (int c = 0; c < getLength(); c++) {
            result.getRawComponents()[c] = getHandler().round(getRawComponents()[c]);
        }
        copyMeta(result);
        return result;
    }
    
    /**
     * Copies this Component to another Component.
     *
     * @param to The Component to copy to.
     * @throws ArithmeticException When the two Components do not have the same dimensionality.
     */
    @Override
    public final void copy(I to) throws ArithmeticException {
        ComponentErrorHandlerProvider.assertDimensionalitySame(this, to);
        
        for (int i = 0; i < getLength(); i++) {
            to.getRawComponents()[i] = getRawComponents()[i];
        }
        copyMeta(to);
    }
    
    /**
     * Copies this Component's metadata to another Component.
     *
     * @param to The Component to copy the metadata to.
     */
    @Override
    public abstract void copyMeta(I to);
    
    /**
     * Resizes the Component.
     *
     * @param newDim The new dimensionality of the Component.
     */
    @Override
    public void redim(int newDim) {
        if (!isResizeable() || (newDim == getDimensionality())) {
            return;
        } else if (newDim <= 0) {
            setComponents((T[]) getHandler().array(0));
            return;
        }
        
        Number[] newComponents = getHandler().array(dimensionalityToLength(newDim));
        Arrays.fill(newComponents, getHandler().zero());
        System.arraycopy(getRawComponents(), 0, newComponents, 0, Math.min(getLength(), newComponents.length));
        setComponents((T[]) newComponents);
    }
    
    /**
     * Calculates the Component's length from its dimensionality.
     *
     * @param dim The dimensionality of the Component.
     * @return The Component's length.
     */
    @Override
    public int dimensionalityToLength(int dim) {
        return Math.max(dim, 0);
    }
    
    /**
     * Calculates the Component's length from its dimensionality.
     *
     * @return The Component's length.
     * @see #dimensionalityToLength(int)
     */
    @Override
    public final int dimensionalityToLength() {
        return dimensionalityToLength(getDimensionality());
    }
    
    /**
     * Calculates the Component's dimensionality from its length.
     *
     * @param length The length of the Component.
     * @return The Component's dimensionality.
     */
    @Override
    public int lengthToDimensionality(int length) {
        return Math.max(length, 0);
    }
    
    /**
     * Calculates the Component's dimensionality from its length.
     *
     * @return The Component's dimensionality.
     * @see #lengthToDimensionality(int)
     */
    @Override
    public final int lengthToDimensionality() {
        return lengthToDimensionality(getLength());
    }
    
    /**
     * Calculates and sets the Component's dimensionality using its length.
     */
    @Override
    public final void calculateDimensionality() {
        dimensionality = lengthToDimensionality();
    }
    
    
    //Getters
    
    /**
     * Returns the raw components that define the Component.
     *
     * @return The raw components that define the Component.
     */
    @Override
    public abstract Number[] getRawComponents();
    
    /**
     * Returns the components that define the Component.
     *
     * @return The components that define the Component.
     * @see #getRawComponents()
     */
    @Override
    public final T[] getComponents() {
        return (T[]) Arrays.stream(getRawComponents())
                .map(e -> getHandler().clean(e))
                .toArray(getHandler().arrayGenerator());
    }
    
    /**
     * Returns a raw component of the Component.
     *
     * @param index The index of the component.
     * @return The raw component of the Component at the specified index.
     * @throws IndexOutOfBoundsException When the Component does not contain a component at the specified index.
     */
    @Override
    public T getRaw(int index) throws IndexOutOfBoundsException {
        ComponentErrorHandlerProvider.assertIndexInBounds(this, index);
        return (T) getRawComponents()[index];
    }
    
    /**
     * Returns a component of the Component.
     *
     * @param index The index of the component.
     * @return The component of the Component at the specified index.
     * @throws IndexOutOfBoundsException When the Component does not contain a component at the specified index.
     * @see #getRaw(int)
     */
    @Override
    public T get(int index) throws IndexOutOfBoundsException {
        return (T) getHandler().clean(getRaw(index));
    }
    
    /**
     * Returns the dimensionality of the Component.
     *
     * @return The dimensionality of the Component.
     */
    @Override
    public final int getDimensionality() {
        return dimensionality;
    }
    
    /**
     * Returns the length of the Component.
     *
     * @return The length of the Component.
     */
    @Override
    public final int getLength() {
        return getRawComponents().length;
    }
    
    /**
     * Returns the type of the Component.
     *
     * @return The type of the Component.
     */
    @Override
    public final Class<? extends BaseComponent> getComponentClass() {
        return componentClass;
    }
    
    /**
     * Returns the type of the components that define the Component.
     *
     * @return The type of the components that define the Component.
     */
    @Override
    public final Class<? extends Number> getType() {
        return type;
    }
    
    /**
     * Returns the Component Math Handler for the Component.
     *
     * @return The Component Math Handler for the Component.
     */
    @Override
    public ComponentMathHandlerInterface getHandler() {
        return handler;
    }
    
    /**
     * Returns the Component Error Handler for the Component.
     *
     * @return The Component Error Handler for the Component.
     */
    @Override
    public ComponentErrorHandlerInterface getErrorHandler() {
        return errorHandler;
    }
    
    /**
     * Returns the name of the type of Component.
     *
     * @return The name of the type of Component.
     */
    @Override
    public String getName() {
        return "Base Component";
    }
    
    /**
     * Returns the precision to use in comparisons.
     *
     * @return The precision to use in comparisons.
     */
    @Override
    public Number getPrecision() {
        return getHandler().getPrecision();
    }
    
    /**
     * Returns whether this Component is resizeable or not.
     *
     * @return Whether this Component is resizeable or not.
     */
    @Override
    public boolean isResizeable() {
        return true;
    }
    
    
    //Setters
    
    /**
     * Sets the components that define the Component.
     *
     * @param newComponents The components that define the Component.
     * @throws IndexOutOfBoundsException If the provided components are not the same length as the existing components.
     */
    @Override
    public final void setComponents(T[] newComponents) throws IndexOutOfBoundsException {
        if ((components != null) && (!isResizeable() && (newComponents.length != getLength()))) {
            throw new IndexOutOfBoundsException(getErrorHandler().componentLengthNotEqualErrorMessage(newComponents, getLength()));
        }
        if (ArrayUtility.anyNull(newComponents)) {
            throw new NullPointerException();
        }
        
        components = newComponents;
        calculateDimensionality();
    }
    
    /**
     * Sets the value of a component of the Component.
     *
     * @param index The index of the component to set.
     * @param value The new value of the component.
     * @throws IndexOutOfBoundsException When the Component does not contain a component at the specified index.
     */
    @Override
    public void set(int index, T value) throws IndexOutOfBoundsException {
        ComponentErrorHandlerProvider.assertIndexInBounds(this, index);
        
        if (value != null) {
            getRawComponents()[index] = value;
        }
    }
    
    
    //Functions
    
    /**
     * Copies the values from one Component to another.
     *
     * @param source The Component to copy from.
     * @param dest   The Component to copy to.
     * @throws ArithmeticException When the Components do not have the same dimensionality, or the two Components have different component types.
     */
    public static void copy(BaseComponent source, BaseComponent dest) throws ArithmeticException {
        if (!source.componentTypeEqual(dest)) {
            throw new ArithmeticException(source.getErrorHandler().componentTypeNotSameErrorMessage(source, dest));
        }
        
        source.copy(dest);
    }
    
}
