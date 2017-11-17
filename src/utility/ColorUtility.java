/*
 * File:    ColorUtility.java
 * Package: utility
 * Author:  Zachary Gill
 */

package utility;

import java.awt.*;

/**
 * Handles color operations.
 */
public final class ColorUtility
{
    
    //Functions
    
    /**
     * Returns a random color.
     *
     * @return The random color.
     */
    public static Color getRandomColor()
    {
        int r = (int) (Math.random() * 255) + 1;
        int g = (int) (Math.random() * 255) + 1;
        int b = (int) (Math.random() * 255) + 1;
        return new Color(r, g, b);
    }
    
    /**
     * Returns a color by its hue.
     *
     * @param hue The hue of the color.
     * @return The color.
     */
    public static Color getColorByHue(float hue)
    {
        return Color.getHSBColor(hue, 1, 1);
    }
    
}
