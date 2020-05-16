/*
 * File:    PicturePane.java
 * Package: objects.complex.pane
 * Author:  Zachary Gill
 */

package objects.complex.pane;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import objects.base.AbstractObject;
import objects.base.polygon.Rectangle;

/**
 * Defines a Picture Pane.
 */
public class PicturePane extends Pane {
    
    //Constructors
    
    /**
     * The constructor for a Picture Pane.
     *
     * @param parent  The parent of the Picture Pane.
     * @param color   The color of the Picture Pane.
     * @param bounds  The bounds of the Picture Pane.
     * @param picture The picture of the Picture Pane.
     * @param invert  Whether or not to invert the colors of the Picture Pane.
     */
    public PicturePane(AbstractObject parent, Color color, Rectangle bounds, File picture, boolean invert) {
        super(parent, color, bounds, invert);
        
        BufferedImage image = null;
        if ((picture != null) && picture.exists()) {
            try {
                image = ImageIO.read(picture);
            } catch (IOException ignored) {
            }
        }
        setImage(image);
    }
    
    /**
     * The constructor for a Picture Pane.
     *
     * @param parent  The parent of the Picture Pane.
     * @param color   The color of the Picture Pane.
     * @param bounds  The bounds of the Picture Pane.
     * @param picture The picture of the Picture Pane.
     */
    public PicturePane(AbstractObject parent, Color color, Rectangle bounds, File picture) {
        this(parent, color, bounds, picture, false);
    }
    
}
