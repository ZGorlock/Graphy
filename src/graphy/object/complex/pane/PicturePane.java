/*
 * File:    PicturePane.java
 * Package: graphy.objects.complex.pane
 * Author:  Zachary Gill
 */

package graphy.object.complex.pane;

import java.awt.Color;
import java.io.File;

import graphy.object.base.AbstractObject;
import graphy.object.base.polygon.Rectangle;
import graphy.utility.ImageUtility;

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
