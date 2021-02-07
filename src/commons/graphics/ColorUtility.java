/*
 * File:    ColorUtility.java
 * Package: commons.graphics
 * Author:  Zachary Gill
 */

package commons.graphics;

import java.awt.Color;

import commons.console.Console;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A resource class that provides additional color functionality.
 */
public final class ColorUtility {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ColorUtility.class);
    
    
    //Functions
    
    /**
     * Returns a random color.
     *
     * @return The random color.
     */
    public static Color getRandomColor() {
        int r = (int) (Math.random() * 255) + 1;
        int g = (int) (Math.random() * 255) + 1;
        int b = (int) (Math.random() * 255) + 1;
        return new Color(r, g, b);
    }
    
    /**
     * Returns a random color with a particular alpha value.
     *
     * @param alpha The alpha value for the color.
     * @return The random color.
     * @see #getRandomColor()
     */
    public static Color getRandomColor(int alpha) {
        Color color = getRandomColor();
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
    
    /**
     * Returns the inverse of a color.
     *
     * @param color The color to invert.
     * @return The inverted color.
     */
    public static Color invertColor(Color color) {
        return new Color(color.getRGB() ^ 0x00ffffff);
    }
    
    /**
     * Returns a color by its hue.
     *
     * @param hue The hue of the color.
     * @return The color.
     */
    public static Color getColorByHue(float hue) {
        return Color.getHSBColor(hue, 1, 1);
    }
    
    /**
     * Returns a color by its wavelength.
     *
     * @param wavelength The wavelength of the color in nanometers.
     * @return The color.
     */
    public static Color getColorByWavelength(double wavelength) {
        double gamma = 0.8;
        double maxIntensity = 255;
        
        double factor;
        double r, g, b;
        
        if ((wavelength >= 380) && (wavelength < 440)) {
            r = -(wavelength - 440) / (440 - 380);
            g = 0.0;
            b = 1.0;
        } else if ((wavelength >= 440) && (wavelength < 490)) {
            r = 0.0;
            g = (wavelength - 440) / (490 - 440);
            b = 1.0;
        } else if ((wavelength >= 490) && (wavelength < 510)) {
            r = 0.0;
            g = 1.0;
            b = -(wavelength - 510) / (510 - 490);
        } else if ((wavelength >= 510) && (wavelength < 580)) {
            r = (wavelength - 510) / (580 - 510);
            g = 1.0;
            b = 0.0;
        } else if ((wavelength >= 580) && (wavelength < 645)) {
            r = 1.0;
            g = -(wavelength - 645) / (645 - 580);
            b = 0.0;
        } else if ((wavelength >= 645) && (wavelength < 781)) {
            r = 1.0;
            g = 0.0;
            b = 0.0;
        } else {
            r = 0.0;
            g = 0.0;
            b = 0.0;
        }
        
        if ((wavelength >= 380) && (wavelength < 420)) {
            factor = 0.3 + ((0.7 * (wavelength - 380)) / (420 - 380));
        } else if ((wavelength >= 420) && (wavelength < 701)) {
            factor = 1.0;
        } else if ((wavelength >= 701) && (wavelength < 781)) {
            factor = 0.3 + 0.7 * (780 - wavelength) / (780 - 700);
        } else {
            factor = 0.0;
        }
        
        r = (r == 0.0) ? 0 : (int) Math.round(maxIntensity * Math.pow(r * factor, gamma));
        g = (g == 0.0) ? 0 : (int) Math.round(maxIntensity * Math.pow(g * factor, gamma));
        b = (b == 0.0) ? 0 : (int) Math.round(maxIntensity * Math.pow(b * factor, gamma));
        return new Color((int) r, (int) g, (int) b);
    }
    
    /**
     * Returns a random color excluding console codes.
     *
     * @return The random color.
     */
    public static Color getRandomColorExcludeConsoleEffects() {
        boolean conflict = true;
        
        int r = 0;
        while (conflict) {
            r = (int) (Math.random() * 255) + 1;
            conflict = false;
            for (Console.ConsoleEffect effect : Console.ConsoleEffect.values()) {
                if (r == effect.getCode()) {
                    conflict = true;
                    break;
                }
            }
        }
        conflict = true;
        
        int g = 0;
        while (conflict) {
            g = (int) (Math.random() * 255) + 1;
            conflict = false;
            for (Console.ConsoleEffect effect : Console.ConsoleEffect.values()) {
                if (g == effect.getCode()) {
                    conflict = true;
                    break;
                }
            }
        }
        conflict = true;
        
        int b = 0;
        while (conflict) {
            b = (int) (Math.random() * 255) + 1;
            conflict = false;
            for (Console.ConsoleEffect effect : Console.ConsoleEffect.values()) {
                if (b == effect.getCode()) {
                    conflict = true;
                    break;
                }
            }
        }
        
        return new Color(r, g, b);
    }
    
}
