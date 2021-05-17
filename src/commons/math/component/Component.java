/*
 * File:    Component.java
 * Package: commons.math.component
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.component;

import java.util.Arrays;

import commons.math.component.handler.math.DoubleComponentMathHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the properties of a Component.
 *
 * @param <I> The type of the Component.
 */
public abstract class Component<I extends Component<?>> extends BaseComponent<Double, I> {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(Component.class);
    
    
    //Constructors
    
    /**
     * The protected no-argument constructor for a Component.
     */
    protected Component() {
        super(Double.class, new DoubleComponentMathHandler());
    }
    
    
    //Methods
    
    /**
     * Copies this Component's metadata to another Component.
     *
     * @param to The Component to copy the metadata to.
     */
    @Override
    public void copyMeta(I to) {
    }
    
    
    //Getters
    
    /**
     * Returns the raw components that define the Component.
     *
     * @return The raw components that define the Component.
     */
    @Override
    public final Double[] getRawComponents() {
        return components;
    }
    
    /**
     * Returns the primitive components that define the Component.
     *
     * @return The primitive components that define the Component.
     * @see #getComponents()
     */
    public final double[] getPrimitiveComponents() {
        return Arrays.stream(getComponents()).mapToDouble(e -> e).toArray();
    }
    
    /**
     * Returns the name of the type of Component.
     *
     * @return The name of the type of Component.
     */
    @Override
    public String getName() {
        return "Component";
    }
    
}
