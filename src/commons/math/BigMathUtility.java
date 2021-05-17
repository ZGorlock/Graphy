/*
 * File:    BigMathUtility.java
 * Package: commons.math
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.math;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import ch.obermuhlner.math.big.BigDecimalMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A resource class that provides additional big math functionality.
 */
public final class BigMathUtility {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(BigMathUtility.class);
    
    
    //Enums
    
    /**
     * An enumeration of precision modes for decimal places for math operations.
     */
    public enum PrecisionMode {
        
        //Values
        
        DEFAULT_PRECISION(-1),
        NO_PRECISION(0),
        LOW_PRECISION(8),
        MID_PRECISION(16),
        HIGH_PRECISION(64),
        MAX_PRECISION(512),
        MATH_PRECISION(1024);
        
        
        //Fields
        
        /**
         * The number of decimal places for the PrecisionMode.
         */
        private final int precision;
        
        
        //Constructors
        
        /**
         * Constructs a PrecisionMode.
         *
         * @param precision The number of decimal places for the PrecisionMode.
         */
        PrecisionMode(int precision) {
            this.precision = precision;
        }
        
        
        //Getters
        
        /**
         * Returns the number of decimal places for the PrecisionMode.
         *
         * @return The number of decimal places for the PrecisionMode.
         */
        public int getPrecision() {
            return precision;
        }
        
    }
    
    
    //Constants
    
    /**
     * The default precision to use for math operations.
     */
    public static final int DEFAULT_MATH_PRECISION = PrecisionMode.HIGH_PRECISION.getPrecision();
    
    /**
     * The default rounding mode to use for math operations.
     */
    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;
    
    
    //Functions
    
    /**
     * Adds two numbers represented by strings.
     *
     * @param n1        The first number.
     * @param n2        The second number.
     * @param precision The number of significant figures to return.
     * @return The first number added to the second number, represented as a string.
     * @throws NumberFormatException When a number string does not represent a number.
     */
    public static String add(String n1, String n2, int precision) throws NumberFormatException {
        BigDecimal b1 = new BigDecimal(n1);
        BigDecimal b2 = new BigDecimal(n2);
        
        BigDecimal br = b1.add(b2);
        if (precision != PrecisionMode.DEFAULT_PRECISION.getPrecision()) {
            br = br.setScale(precision, DEFAULT_ROUNDING_MODE);
        }
        return br.toPlainString();
    }
    
    /**
     * Adds two numbers represented by strings.
     *
     * @param n1            The first number.
     * @param n2            The second number.
     * @param precisionMode The precision mode specifying the number of significant figures to return.
     * @return The first number added to the second number, represented as a string.
     * @throws NumberFormatException When a number string does not represent a number.
     * @see #add(String, String, int)
     */
    public static String add(String n1, String n2, PrecisionMode precisionMode) throws NumberFormatException {
        return add(n1, n2, precisionMode.getPrecision());
    }
    
    /**
     * Adds two numbers represented by strings.
     *
     * @param n1 The first number.
     * @param n2 The second number.
     * @return The first number added to the second number, represented as a string.
     * @throws NumberFormatException When a number string does not represent a number.
     * @see #add(String, String, PrecisionMode)
     */
    public static String add(String n1, String n2) throws NumberFormatException {
        return add(n1, n2, PrecisionMode.DEFAULT_PRECISION);
    }
    
    /**
     * Subtracts two numbers represented by strings.
     *
     * @param n1        The first number.
     * @param n2        The second number.
     * @param precision The number of significant figures to return.
     * @return The second number subtracted from the first number, represented as a string.
     * @throws NumberFormatException When a number string does not represent a number.
     */
    public static String subtract(String n1, String n2, int precision) throws NumberFormatException {
        BigDecimal b1 = new BigDecimal(n1);
        BigDecimal b2 = new BigDecimal(n2);
        
        BigDecimal br = b1.subtract(b2);
        if (precision != PrecisionMode.DEFAULT_PRECISION.getPrecision()) {
            br = br.setScale(precision, DEFAULT_ROUNDING_MODE);
        }
        return br.toPlainString();
    }
    
    /**
     * Subtracts two numbers represented by strings.
     *
     * @param n1            The first number.
     * @param n2            The second number.
     * @param precisionMode The precision mode specifying the number of significant figures to return.
     * @return The second number subtracted from the first number, represented as a string.
     * @throws NumberFormatException When a number string does not represent a number.
     * @see #subtract(String, String, int)
     */
    public static String subtract(String n1, String n2, PrecisionMode precisionMode) throws NumberFormatException {
        return subtract(n1, n2, precisionMode.getPrecision());
    }
    
    /**
     * Subtracts two numbers represented by strings.
     *
     * @param n1 The first number.
     * @param n2 The second number.
     * @return The second number subtracted from the first number, represented as a string.
     * @throws NumberFormatException When a number string does not represent a number.
     * @see #subtract(String, String, PrecisionMode)
     */
    public static String subtract(String n1, String n2) throws NumberFormatException {
        return subtract(n1, n2, PrecisionMode.DEFAULT_PRECISION);
    }
    
    /**
     * Multiplies two numbers represented by strings.
     *
     * @param n1        The first number.
     * @param n2        The second number.
     * @param precision The number of significant figures to return.
     * @return The first number multiplied with the second number, represented as a string.
     * @throws NumberFormatException When a number string does not represent a number.
     */
    public static String multiply(String n1, String n2, int precision) throws NumberFormatException {
        BigDecimal b1 = new BigDecimal(n1);
        BigDecimal b2 = new BigDecimal(n2);
        
        BigDecimal br = b1.multiply(b2);
        if (precision != PrecisionMode.DEFAULT_PRECISION.getPrecision()) {
            br = br.setScale(precision, DEFAULT_ROUNDING_MODE);
        }
        return br.toPlainString();
    }
    
    /**
     * Multiplies two numbers represented by strings.
     *
     * @param n1            The first number.
     * @param n2            The second number.
     * @param precisionMode The precision mode specifying the number of significant figures to return.
     * @return The first number multiplied with the second number, represented as a string.
     * @throws NumberFormatException When a number string does not represent a number.
     * @see #multiply(String, String, int)
     */
    public static String multiply(String n1, String n2, PrecisionMode precisionMode) throws NumberFormatException {
        return multiply(n1, n2, precisionMode.getPrecision());
    }
    
    /**
     * Multiplies two numbers represented by strings.
     *
     * @param n1 The first number.
     * @param n2 The second number.
     * @return The first number multiplied with the second number, represented as a string.
     * @throws NumberFormatException When a number string does not represent a number.
     * @see #multiply(String, String, PrecisionMode)
     */
    public static String multiply(String n1, String n2) throws NumberFormatException {
        return multiply(n1, n2, PrecisionMode.DEFAULT_PRECISION);
    }
    
    /**
     * Divides two numbers represented by strings.
     *
     * @param n1        The first number.
     * @param n2        The second number.
     * @param precision The number of significant figures to return.
     * @return The first number divided by the second number, represented as a string.
     * @throws NumberFormatException When a number string does not represent a number.
     * @throws ArithmeticException   When the dividend is zero.
     */
    public static String divide(String n1, String n2, int precision) throws NumberFormatException, ArithmeticException {
        BigDecimal b1 = new BigDecimal(n1);
        BigDecimal b2 = new BigDecimal(n2);
        
        if (b2.compareTo(BigDecimal.valueOf(0)) == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        
        if (precision == PrecisionMode.DEFAULT_PRECISION.getPrecision()) {
            String result = b1.divide(b2, DEFAULT_MATH_PRECISION, DEFAULT_ROUNDING_MODE).toPlainString();
            return NumberStringUtility.cleanNumberString(result);
        } else {
            return b1.divide(b2, precision, DEFAULT_ROUNDING_MODE).toPlainString();
        }
    }
    
    /**
     * Divides two numbers represented by strings.
     *
     * @param n1            The first number.
     * @param n2            The second number.
     * @param precisionMode The precision mode specifying the number of significant figures to return.
     * @return The first number divided by the second number, represented as a string.
     * @throws NumberFormatException When a number string does not represent a number.
     * @throws ArithmeticException   When the dividend is zero.
     * @see #divide(String, String, int)
     */
    public static String divide(String n1, String n2, PrecisionMode precisionMode) throws NumberFormatException, ArithmeticException {
        return divide(n1, n2, precisionMode.getPrecision());
    }
    
    /**
     * Divides two numbers represented by strings.
     *
     * @param n1 The first number.
     * @param n2 The second number.
     * @return The first number divided by the second number, represented as a string.
     * @throws NumberFormatException When a number string does not represent a number.
     * @throws ArithmeticException   When the dividend is zero.
     * @see #divide(String, String, PrecisionMode)
     */
    public static String divide(String n1, String n2) throws NumberFormatException, ArithmeticException {
        return divide(n1, n2, PrecisionMode.DEFAULT_PRECISION);
    }
    
    /**
     * Performs the modulus operation on two numbers represented by strings.
     *
     * @param n1        The first number.
     * @param n2        The second number.
     * @param precision The number of significant figures to return.
     * @return The first number modulus the second number, represented as a string.
     * @throws NumberFormatException When a number string does not represent a number.
     * @throws ArithmeticException   When the dividend is zero.
     */
    public static String mod(String n1, String n2, int precision) throws NumberFormatException, ArithmeticException {
        BigDecimal b1 = new BigDecimal(n1);
        BigDecimal b2 = new BigDecimal(n2);
        
        if (b2.compareTo(BigDecimal.valueOf(0)) == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        
        BigDecimal x = new BigDecimal(100);
        BigDecimal remainder = x.remainder(new BigDecimal(20));
        
        if (precision == PrecisionMode.DEFAULT_PRECISION.getPrecision()) {
            String result = b1.remainder(b2).setScale(DEFAULT_MATH_PRECISION, DEFAULT_ROUNDING_MODE).toPlainString();
            return NumberStringUtility.cleanNumberString(result);
        } else {
            return b1.remainder(b2).setScale(precision, DEFAULT_ROUNDING_MODE).toPlainString();
        }
    }
    
    /**
     * Performs the modulus operation on two numbers represented by strings.
     *
     * @param n1            The first number.
     * @param n2            The second number.
     * @param precisionMode The precision mode specifying the number of significant figures to return.
     * @return The first number modulus the second number, represented as a string.
     * @throws NumberFormatException When a number string does not represent a number.
     * @throws ArithmeticException   When the dividend is zero.
     * @see #mod(String, String, int)
     */
    public static String mod(String n1, String n2, PrecisionMode precisionMode) throws NumberFormatException, ArithmeticException {
        return mod(n1, n2, precisionMode.getPrecision());
    }
    
    /**
     * Performs the modulus operation on two numbers represented by strings.
     *
     * @param n1 The first number.
     * @param n2 The second number.
     * @return The first number modulus the second number, represented as a string.
     * @throws NumberFormatException When a number string does not represent a number.
     * @throws ArithmeticException   When the dividend is zero.
     * @see #divide(String, String, PrecisionMode)
     */
    public static String mod(String n1, String n2) throws NumberFormatException, ArithmeticException {
        return mod(n1, n2, PrecisionMode.DEFAULT_PRECISION);
    }
    
    /**
     * Performs the power operation on two numbers represented by strings.
     *
     * @param n1        The first number.
     * @param n2        The second number.
     * @param precision The number of significant figures to return.
     * @return The first number raised to the power of the second number, represented as a string.
     * @throws NumberFormatException When a number string does not represent a number.
     */
    public static String power(String n1, String n2, int precision) throws NumberFormatException {
        BigDecimal b1 = new BigDecimal(n1);
        BigDecimal b2 = new BigDecimal(n2);
        
        MathContext context = new MathContext(PrecisionMode.MAX_PRECISION.getPrecision(), DEFAULT_ROUNDING_MODE);
        BigDecimal result = BigDecimalMath.pow(b1, b2, context);
        if (precision == PrecisionMode.DEFAULT_PRECISION.getPrecision()) {
            return result.setScale(DEFAULT_MATH_PRECISION, DEFAULT_ROUNDING_MODE).stripTrailingZeros().toPlainString();
        } else {
            return result.setScale(precision, DEFAULT_ROUNDING_MODE).toPlainString();
        }
    }
    
    /**
     * Performs the power operation on two numbers represented by strings.
     *
     * @param n1            The first number.
     * @param n2            The second number.
     * @param precisionMode The precision mode specifying the number of significant figures to return.
     * @return The first number raised to the power of the second number, represented as a string.
     * @throws NumberFormatException When a number string does not represent a number.
     * @see #power(String, String, int)
     */
    public static String power(String n1, String n2, PrecisionMode precisionMode) throws NumberFormatException {
        return power(n1, n2, precisionMode.getPrecision());
    }
    
    /**
     * Performs the power operation on two numbers represented by strings.
     *
     * @param n1 The first number.
     * @param n2 The second number.
     * @return The first number raised to the power of the second number, represented as a string.
     * @throws NumberFormatException When a number string does not represent a number.
     * @see #power(String, String, PrecisionMode)
     */
    public static String power(String n1, String n2) throws NumberFormatException {
        return power(n1, n2, PrecisionMode.DEFAULT_PRECISION);
    }
    
    /**
     * Performs the root operation on two numbers represented by strings.
     *
     * @param n1        The first number.
     * @param n2        The second number.
     * @param precision The number of significant figures to return.
     * @return The second number root of the first number, represented as a string.
     * @throws NumberFormatException When a number string does not represent a number.
     * @throws ArithmeticException   When the first number is less than zero.
     */
    public static String root(String n1, String n2, int precision) throws NumberFormatException, ArithmeticException {
        BigDecimal b1 = new BigDecimal(n1);
        BigDecimal b2 = new BigDecimal(n2);
        
        MathContext context = new MathContext(PrecisionMode.MAX_PRECISION.getPrecision(), DEFAULT_ROUNDING_MODE);
        BigDecimal result = BigDecimalMath.root(b1, b2, context);
        if (precision == PrecisionMode.DEFAULT_PRECISION.getPrecision()) {
            return result.setScale(DEFAULT_MATH_PRECISION, DEFAULT_ROUNDING_MODE).stripTrailingZeros().toPlainString();
        } else {
            return result.setScale(precision, DEFAULT_ROUNDING_MODE).toPlainString();
        }
    }
    
    /**
     * Performs the root operation on two numbers represented by strings.
     *
     * @param n1            The first number.
     * @param n2            The second number.
     * @param precisionMode The precision mode specifying the number of significant figures to return.
     * @return The second number root of the first number, represented as a string.
     * @throws NumberFormatException When a number string does not represent a number.
     * @throws ArithmeticException   When the first number is less than zero.
     * @see #root(String, String, int)
     */
    public static String root(String n1, String n2, PrecisionMode precisionMode) throws NumberFormatException, ArithmeticException {
        return root(n1, n2, precisionMode.getPrecision());
    }
    
    /**
     * Performs the root operation on two numbers represented by strings.
     *
     * @param n1 The first number.
     * @param n2 The second number.
     * @return The second number root of the first number, represented as a string.
     * @throws NumberFormatException When a number string does not represent a number.
     * @throws ArithmeticException   When the first number is less than zero.
     * @see #root(String, String, PrecisionMode)
     */
    public static String root(String n1, String n2) throws NumberFormatException, ArithmeticException {
        return root(n1, n2, PrecisionMode.DEFAULT_PRECISION);
    }
    
    /**
     * Performs the square root operation on a number represented by strings.
     *
     * @param n         The number.
     * @param precision The number of significant figures to return.
     * @return The square root of the number, represented as a string.
     * @throws NumberFormatException When a number string does not represent a number.
     * @throws ArithmeticException   When the number is less than zero.
     */
    public static String sqrt(String n, int precision) throws NumberFormatException, ArithmeticException {
        BigDecimal b = new BigDecimal(n);
        
        MathContext context = new MathContext(PrecisionMode.MAX_PRECISION.getPrecision(), DEFAULT_ROUNDING_MODE);
        BigDecimal result = BigDecimalMath.sqrt(b, context);
        if (precision == PrecisionMode.DEFAULT_PRECISION.getPrecision()) {
            return result.setScale(DEFAULT_MATH_PRECISION, DEFAULT_ROUNDING_MODE).stripTrailingZeros().toPlainString();
        } else {
            return result.setScale(precision, DEFAULT_ROUNDING_MODE).toPlainString();
        }
    }
    
    /**
     * Performs the square root operation on a number represented by strings.
     *
     * @param n             The number.
     * @param precisionMode The precision mode specifying the number of significant figures to return.
     * @return The square root of the number, represented as a string.
     * @throws NumberFormatException When a number string does not represent a number.
     * @throws ArithmeticException   When the number is less than zero.
     * @see #sqrt(String, int)
     */
    public static String sqrt(String n, PrecisionMode precisionMode) throws NumberFormatException, ArithmeticException {
        return sqrt(n, precisionMode.getPrecision());
    }
    
    /**
     * Performs the square root operation on a number represented by strings.
     *
     * @param n The number.
     * @return The square root of the number, represented as a string.
     * @throws NumberFormatException When a number string does not represent a number.
     * @throws ArithmeticException   When the number is less than zero.
     * @see #sqrt(String, PrecisionMode)
     */
    public static String sqrt(String n) throws NumberFormatException, ArithmeticException {
        return sqrt(n, PrecisionMode.DEFAULT_PRECISION);
    }
    
    /**
     * Computes the log of a number represented by a string to a specified base.
     *
     * @param n         The number.
     * @param base      The log base.
     * @param precision The number of significant figures to return.
     * @return The log of the number to the specified base, represented as a string.
     * @throws NumberFormatException When the number string does not represent a number.
     * @throws ArithmeticException   When the number is less than or equal to zero or the base of the log is invalid.
     */
    public static String log(String n, int base, int precision) throws NumberFormatException, ArithmeticException {
        if (base < 2) {
            throw new ArithmeticException("Cannot take a log with base: " + base);
        }
        
        BigDecimal input = new BigDecimal(n);
        if (input.signum() <= 0) {
            throw new ArithmeticException("Cannot handle imaginary numbers");
        }
        
        if (precision == PrecisionMode.DEFAULT_PRECISION.getPrecision()) {
            precision = DEFAULT_MATH_PRECISION;
        }
        
        MathContext context = new MathContext(PrecisionMode.MAX_PRECISION.getPrecision(), DEFAULT_ROUNDING_MODE);
        String numerator = BigDecimalMath.log10(input, context).toPlainString();
        String denominator = BigDecimalMath.log10(new BigDecimal(base), context).toPlainString();
        return divide(numerator, denominator, precision);
    }
    
    /**
     * Computes the log of a number represented by a string to a specified base.
     *
     * @param n             The number.
     * @param base          The log base.
     * @param precisionMode The precision mode specifying the number of significant figures to return.
     * @return The log of the number to the specified base, represented as a string.
     * @throws NumberFormatException When the number string does not represent a number.
     * @throws ArithmeticException   When the number is less than or equal to zero or the base of the log is invalid.
     * @see #log(String, int, int)
     */
    public static String log(String n, int base, PrecisionMode precisionMode) throws NumberFormatException, ArithmeticException {
        return log(n, base, precisionMode.getPrecision());
    }
    
    /**
     * Computes the log of a number represented by a string to a specified base.
     *
     * @param n    The number.
     * @param base The log base.
     * @return The log of the number to the specified base, represented as a string.
     * @throws NumberFormatException When the number string does not represent a number.
     * @throws ArithmeticException   When the number is less than or equal to zero or the base of the log is invalid.
     * @see #log(String, int, PrecisionMode)
     */
    public static String log(String n, int base) throws NumberFormatException, ArithmeticException {
        return log(n, base, PrecisionMode.DEFAULT_PRECISION);
    }
    
    /**
     * Computes the natural log of a number represented by a string.
     *
     * @param n         The number.
     * @param precision The number of significant figures to return.
     * @return The natural log of the number, represented as a string.
     * @throws NumberFormatException When the number string does not represent a number.
     * @throws ArithmeticException   When the number is less than or equal to zero.
     */
    public static String ln(String n, int precision) throws NumberFormatException, ArithmeticException {
        BigDecimal input = new BigDecimal(n);
        
        if (input.signum() <= 0) {
            throw new ArithmeticException("Cannot handle imaginary numbers");
        }
        
        if (precision == PrecisionMode.DEFAULT_PRECISION.getPrecision()) {
            precision = DEFAULT_MATH_PRECISION;
        }
        
        MathContext context = new MathContext(PrecisionMode.MAX_PRECISION.getPrecision(), DEFAULT_ROUNDING_MODE);
        String numerator = BigDecimalMath.log(input, context).toPlainString();
        String denominator = BigDecimalMath.log10(BigDecimalMath.e(context), context).toPlainString();
        return divide(numerator, denominator, precision);
    }
    
    /**
     * Computes the natural log of a number represented by a string.
     *
     * @param n             The number.
     * @param precisionMode The precision mode specifying the number of significant figures to return.
     * @return The natural log of the number, represented as a string.
     * @throws NumberFormatException When the number string does not represent a number.
     * @throws ArithmeticException   When the number is less than or equal to zero.
     * @see #ln(String, int)
     */
    public static String ln(String n, PrecisionMode precisionMode) throws NumberFormatException, ArithmeticException {
        return ln(n, precisionMode.getPrecision());
    }
    
    /**
     * Computes the natural log of a number represented by a string.
     *
     * @param n The number.
     * @return The natural log of the number, represented as a string.
     * @throws NumberFormatException When the number string does not represent a number.
     * @throws ArithmeticException   When the number is less than or equal to zero.
     * @see #ln(String, PrecisionMode)
     */
    public static String ln(String n) throws NumberFormatException, ArithmeticException {
        return ln(n, PrecisionMode.DEFAULT_PRECISION);
    }
    
    /**
     * Computes pi.
     *
     * @param precision The number of significant figures to return.
     * @return Pi, represented as a string.
     */
    public static String pi(int precision) {
        if (precision == PrecisionMode.DEFAULT_PRECISION.getPrecision()) {
            precision = DEFAULT_MATH_PRECISION;
        } else if (precision == PrecisionMode.NO_PRECISION.getPrecision()) {
            precision = 1;
        }
        
        MathContext context = new MathContext(precision, DEFAULT_ROUNDING_MODE);
        return BigDecimalMath.pi(context).toPlainString();
    }
    
    /**
     * Computes pi.
     *
     * @param precisionMode The precision mode specifying the number of significant figures to return.
     * @return Pi, represented as a string.
     * @see #pi(int)
     */
    public static String pi(PrecisionMode precisionMode) {
        return pi(precisionMode.getPrecision());
    }
    
    /**
     * Computes pi.
     *
     * @return Pi, represented as a string.
     * @see #pi(PrecisionMode)
     */
    public static String pi() {
        return pi(PrecisionMode.DEFAULT_PRECISION);
    }
    
    /**
     * Determines if a number represented by a string is greater than another number represented by a string.
     *
     * @param n1 The first number.
     * @param n2 The second number.
     * @return Whether the first number is greater than the second number or not.
     * @throws NumberFormatException When a number string does not represent a number.
     */
    public static boolean greaterThan(String n1, String n2) throws NumberFormatException {
        BigDecimal b1 = new BigDecimal(n1);
        BigDecimal b2 = new BigDecimal(n2);
        
        return (b1.compareTo(b2) > 0);
    }
    
    /**
     * Determines if a number represented by a string is less than another number represented by a string.
     *
     * @param n1 The first number.
     * @param n2 The second number.
     * @return Whether the first number is less than the second number or not.
     * @throws NumberFormatException When a number string does not represent a number.
     */
    public static boolean lessThan(String n1, String n2) throws NumberFormatException {
        BigDecimal b1 = new BigDecimal(n1);
        BigDecimal b2 = new BigDecimal(n2);
        
        return (b1.compareTo(b2) < 0);
    }
    
    /**
     * Determines if a number represented by a string is equal to another number represented by a string.
     *
     * @param n1 The first number.
     * @param n2 The second number.
     * @return Whether the first number is equal to the second number or not.
     * @throws NumberFormatException When a number string does not represent a number.
     */
    public static boolean equalTo(String n1, String n2) throws NumberFormatException {
        BigDecimal b1 = new BigDecimal(n1);
        BigDecimal b2 = new BigDecimal(n2);
        
        return (b1.compareTo(b2) == 0);
    }
    
}
