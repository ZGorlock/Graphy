/*
 * File:    MathUtility.java
 * Package: commons.math
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A resource class that provides additional math functionality.
 */
public class MathUtility {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(MathUtility.class);
    
    
    //Functions
    
    /**
     * Returns a random number between two values.
     *
     * @param min The minimum possible value.
     * @param max The maximum possible value.
     * @return A random number between the minimum and maximum values.
     */
    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }
    
    /**
     * Returns a random number between 0 and a value.
     *
     * @param max The maximum possible value.
     * @return A random number between 0 and the maximum values.
     * @see #random(int, int)
     */
    public static int random(int max) {
        return random(0, max);
    }
    
    /**
     * Returns the result of a dice roll.
     *
     * @param sides The number of sides on the dice.
     * @param rolls The number of rolls to perform.
     * @return The result of the dice roll.
     * @see #random(int, int)
     */
    public static int dice(int sides, int rolls) {
        if ((sides <= 0) || (rolls <= 0)) {
            return 0;
        }
        
        int roll = 0;
        for (int i = 0; i < rolls; i++) {
            roll += random(1, sides);
        }
        return roll;
    }
    
    /**
     * Returns the result of a dice roll.
     *
     * @param sides The number of sides on the dice.
     * @return The result of the dice roll.
     * @see #dice(int, int)
     */
    public static int dice(int sides) {
        return dice(sides, 1);
    }
    
    /**
     * Maps a value from one range to another.
     *
     * @param value       The value.
     * @param inputStart  The start of the input range.
     * @param inputEnd    The end of the input range.
     * @param outputStart The start of the output range.
     * @param outputEnd   The end of the output range.
     * @return The value after it has been mapped from the input range to the output range.
     */
    public static double mapValue(double value, double inputStart, double inputEnd, double outputStart, double outputEnd) {
        if (value < inputStart) {
            return outputStart;
        }
        if (value > inputEnd) {
            return outputEnd;
        }
        return (value - inputStart) / (inputEnd - inputStart) * (outputEnd - outputStart) + outputStart;
    }
    
}
