/*
 * File:    JustificationUtil.java
 * Package: graphy.math.vector
 * Author:  Zachary Gill
 */

package graphy.math.vector;

import java.math.BigDecimal;

import commons.math.component.vector.BigVector;
import commons.math.component.vector.Vector;

/**
 * Handles the justification of Vectors.
 */
public final class JustificationUtil {
    
    
    //Static Fields
    
    /**
     * The Vector used for justification.
     */
    private static Vector JUSTIFICATION_VECTOR = new Vector(-1, -1, 1);
    
    /**
     * The Big Vector used for justification.
     */
    private static BigVector BIG_JUSTIFICATION_VECTOR = new BigVector(BigDecimal.valueOf(-1), BigDecimal.valueOf(-1), BigDecimal.valueOf(1));
    
    
    //Getters
    
    /**
     * Returns the Vector used for justification.
     *
     * @return The Vector used for justification.
     */
    public static Vector getJustificationVector() {
        return JUSTIFICATION_VECTOR.cloned();
    }
    
    /**
     * Returns the Big Vector used for justification.
     *
     * @return The Big Vector used for justification.
     */
    public static BigVector getBigJustificationVector() {
        return BIG_JUSTIFICATION_VECTOR.cloned();
    }
    
    
    //Setters
    
    /**
     * Sets the Vector used for justification.
     *
     * @param justificationVector The Vector to be used for justification.
     */
    public static void setJustificationVector(Vector justificationVector) {
        JUSTIFICATION_VECTOR = justificationVector.cloned();
    }
    
    /**
     * Sets the Big Vector used for justification.
     *
     * @param justificationVector The  BigVector to be used for justification.
     */
    public static void setBigJustificationVector(BigVector justificationVector) {
        BIG_JUSTIFICATION_VECTOR = justificationVector.cloned();
    }
    
    
    //Functions
    
    /**
     * Justifies a 3D Vector for rendering.
     *
     * @param v The Vector.
     * @return The justified Vector.
     */
    public static Vector justify(Vector v) {
        return v.times(JUSTIFICATION_VECTOR);
    }
    
    /**
     * Justifies a 3D Big Vector for rendering.
     *
     * @param v The Big Vector.
     * @return The justified Big Vector.
     */
    public static BigVector justifyBig(BigVector v) {
        return v.times(BIG_JUSTIFICATION_VECTOR);
    }
    
}
