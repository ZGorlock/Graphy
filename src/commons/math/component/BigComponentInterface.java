/*
 * File:    BigComponentInterface.java
 * Package: commons.math.component
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math.component;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Defines an additional contract for Big Component classes.
 *
 * @param <I> The type of the Big Component.
 */
public interface BigComponentInterface<I extends BigComponent<?>> extends ComponentInterface<Number, I, BigDecimal> {
    
    //Methods
    
    /**
     * Moves the decimal place of the components of the Big Component left a certain distance.
     *
     * @param move The number of decimal places to move the components of the Big Component.
     * @return The Big Component after the move has been performed.
     */
    default I movePointLeft(int move) {
        I result = emptyCopy();
        for (int c = 0; c < getLength(); c++) {
            result.getRawComponents()[c] = ((BigDecimal) getRawComponents()[c]).movePointLeft(move);
        }
        copyMeta(result);
        return result;
    }
    
    /**
     * Moves the decimal place of the components of the Big Component right a certain distance.
     *
     * @param move The number of decimal places to move the components of the Big Component.
     * @return The Big Component after the move has been performed.
     */
    default I movePointRight(int move) {
        I result = emptyCopy();
        for (int c = 0; c < getLength(); c++) {
            result.getRawComponents()[c] = ((BigDecimal) getRawComponents()[c]).movePointRight(move);
        }
        copyMeta(result);
        return result;
    }
    
    /**
     * Strips the trailing zeros of the components of the Big Component.
     *
     * @return The Big Component after the stripping has been performed.
     */
    default I stripTrailingZeros() {
        I result = emptyCopy();
        for (int c = 0; c < getLength(); c++) {
            result.getRawComponents()[c] = ((BigDecimal) getRawComponents()[c]).stripTrailingZeros();
        }
        copyMeta(result);
        return result;
    }
    
    
    //Getters
    
    /**
     * Returns the Math Context used when doing Big Component math.
     *
     * @return The Math Context used when doing Big Component math.
     */
    MathContext getMathContext();
    
    
    //Setters
    
    /**
     * Sets the Math Context used when doing Big Component math.
     *
     * @param mathContext The Math Context used when doing BigDecimal math.
     */
    void setMathContext(MathContext mathContext);
    
}
