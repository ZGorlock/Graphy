/*
 * File:    IntComponent.java
 * Package: commons.math.component
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.component;

import java.util.Arrays;

import commons.math.component.handler.math.IntComponentMathHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the properties of an Integer Component.
 *
 * @param <I> The type of the Component.
 */
public abstract class IntComponent<I extends IntComponent<?>> extends BaseComponent<Integer, I> {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(IntComponent.class);
    
    
    //Constructors
    
    /**
     * The protected no-argument constructor for an Integer Component.
     */
    protected IntComponent() {
        super(Integer.class, new IntComponentMathHandler());
    }
    
    
    //Methods
    
    /**
     * Copies this Integer Component's metadata to another Integer Component.
     *
     * @param to The Integer Component to copy the metadata to.
     */
    @Override
    public void copyMeta(I to) {
    }
    
    
    //Getters
    
    /**
     * Returns the raw components that define the Integer Component.
     *
     * @return The raw components that define the Integer Component.
     */
    @Override
    public final Integer[] getRawComponents() {
        return components;
    }
    
    /**
     * Returns the primitive components that define the Integer Component.
     *
     * @return The primitive components that define the Integer Component.
     * @see #getComponents()
     */
    public final int[] getPrimitiveComponents() {
        return Arrays.stream(getComponents()).mapToInt(e -> e).toArray();
    }
    
    /**
     * Returns the name of the type of Component.
     *
     * @return The name of the type of Component.
     */
    @Override
    public String getName() {
        return "Integer Component";
    }
    
}
