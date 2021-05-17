/*
 * File:    MathUtility.java
 * Package: commons.math
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
     * Determines if a number is a perfect square or not.
     *
     * @param value The number.
     * @return Whether the number is a perfect square or not.
     */
    public static boolean isSquare(long value) {
        if (value < 0) {
            return false;
        }
        
        double sqrt = Math.sqrt(value);
        return sqrt - Math.floor(sqrt) == 0;
    }
    
    /**
     * Determines if a number is prime or not.
     *
     * @param value The number.
     * @return Whether the number is prime or not.
     */
    public static boolean isPrime(long value) {
        if (value < 2) {
            return false;
        }
        
        for (long i = 2; i <= value / 2; i++) {
            if (value % i == 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Rounds a decimal number with a certain precision.
     *
     * @param value     The number.
     * @param precision The maximum number of decimal places of the result.
     * @return The rounded number.
     */
    public static double roundWithPrecision(double value, int precision) {
        double inversePrecision = Math.pow(10.0, precision);
        return (double) Math.round(value * inversePrecision) / inversePrecision;
    }
    
    /**
     * Rounds a Big Decimal number with a certain precision.
     *
     * @param value        The number.
     * @param precision    The maximum number of decimal places of the result.
     * @param roundingMode The rounding mode to use when rounding the result.
     * @return The rounded number.
     */
    public static BigDecimal roundWithPrecision(BigDecimal value, int precision, RoundingMode roundingMode) {
        return new BigDecimal(value.setScale(precision, roundingMode).stripTrailingZeros().toPlainString());
    }
    
    /**
     * Rounds a Big Decimal number with a certain precision.
     *
     * @param value     The number.
     * @param precision The maximum number of decimal places of the result.
     * @return The rounded number.
     * @see #roundWithPrecision(BigDecimal, int, RoundingMode)
     */
    public static BigDecimal roundWithPrecision(BigDecimal value, int precision) {
        return roundWithPrecision(value, precision, RoundingMode.HALF_UP);
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
