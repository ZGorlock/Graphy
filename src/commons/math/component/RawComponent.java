/*
 * File:    RawComponent.java
 * Package: commons.math.component
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.component;

import java.util.Arrays;

import commons.math.component.handler.math.RawComponentMathHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the properties of a Raw Component.
 *
 * @param <I> The type of the Component.
 */
public abstract class RawComponent<I extends RawComponent<?>> extends BaseComponent<Number, I> {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(RawComponent.class);
    
    
    //Constructors
    
    /**
     * The protected no-argument constructor for a Raw Component.
     */
    protected RawComponent() {
        super(Number.class, new RawComponentMathHandler());
    }
    
    
    //Methods
    
    /**
     * Copies this Raw Component's metadata to another Raw Component.
     *
     * @param to The Raw Component to copy the metadata to.
     */
    @Override
    public void copyMeta(I to) {
    }
    
    
    //Getters
    
    /**
     * Returns the raw components that define the Raw Component.
     *
     * @return The raw components that define the Raw Component.
     */
    @Override
    public final Number[] getRawComponents() {
        return components;
    }
    
    /**
     * Returns the primitive components that define the Raw Component.
     *
     * @return The primitive components that define the Raw Component.
     * @see #getComponents()
     */
    public final double[] getPrimitiveComponents() {
        return Arrays.stream(getComponents()).mapToDouble(Number::doubleValue).toArray();
    }
    
    /**
     * Returns the name of the type of Component.
     *
     * @return The name of the type of Component.
     */
    @Override
    public String getName() {
        return "Raw Component";
    }
    
}
