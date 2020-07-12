/*
 * File:    PicturePane.java
 * Package: objects.complex.pane
 * Author:  Zachary Gill
 */

package objects.complex.pane;

import java.awt.Color;
import java.io.File;

import objects.base.AbstractObject;
import objects.base.polygon.Rectangle;
import utility.ImageUtility;

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
     */
    public PicturePane(AbstractObject parent, Color color, Rectangle bounds, File picture) {
        super(parent, color, bounds);
        
        setImage(ImageUtility.loadImage(picture));
    }
    
}
