/*
 * File:    MirrorPane.java
 * Package: objects.complex.pane
 * Author:  Zachary Gill
 */

package objects.complex.pane;

import java.awt.Color;

import objects.base.AbstractObject;
import objects.base.polygon.Rectangle;

/**
 * Defines a Mirror Pane.
 */
public class MirrorPane extends Pane {
    
    public MirrorPane(AbstractObject parent, Color color, Rectangle bounds, boolean invert) {
        super(parent, color, bounds, invert);
    }
}
