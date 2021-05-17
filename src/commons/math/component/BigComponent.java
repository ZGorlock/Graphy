/*
 * File:    BigComponent.java
 * Package: commons.math.component
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.component;

import java.math.BigDecimal;
import java.math.MathContext;

import commons.math.component.handler.math.BigComponentMathHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the properties of a Big Component.
 *
 * @param <I> The type of the Component.
 */
public abstract class BigComponent<I extends BigComponent<?>> extends BaseComponent<BigDecimal, I> implements BigComponentInterface<I> {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(BigComponent.class);
    
    
    //Constructors
    
    /**
     * The protected no-argument constructor for a Big Component.
     */
    protected BigComponent() {
        super(BigDecimal.class, new BigComponentMathHandler());
    }
    
    
    //Methods
    
    /**
     * Copies this Big Component's metadata to another Big Component.
     *
     * @param to The Big Component to copy the metadata to.
     */
    @Override
    public void copyMeta(I to) {
        to.setMathContext(new MathContext(getMathContext().getPrecision(), getMathContext().getRoundingMode()));
    }
    
    
    //Getters
    
    /**
     * Returns the raw components that define the Big Component.
     *
     * @return The raw components that define the Big Component.
     */
    @Override
    public final BigDecimal[] getRawComponents() {
        return components;
    }
    
    /**
     * Returns the primitive components that define the Big Component.
     *
     * @return The primitive components that define the Big Component.
     * @see #getComponents()
     */
    public final BigDecimal[] getPrimitiveComponents() {
        return getComponents();
    }
    
    /**
     * Returns the name of the type of Component.
     *
     * @return The name of the type of Component.
     */
    @Override
    public String getName() {
        return "Big Component";
    }
    
    /**
     * Returns the Math Context used when doing Big Component math.
     *
     * @return The Math Context used when doing Big Component math.
     */
    @Override
    public final MathContext getMathContext() {
        return ((BigComponentMathHandler) getHandler()).getMathContext();
    }
    
    
    //Setters
    
    /**
     * Sets the Math Context used when doing Big Component math.
     *
     * @param mathContext The Math Context used when doing Big Component math.
     */
    @Override
    public final void setMathContext(MathContext mathContext) {
        ((BigComponentMathHandler) this.getHandler()).setMathContext(mathContext);
    }
    
}
