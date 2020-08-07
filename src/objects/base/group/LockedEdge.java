/*
 * File:    LockedEdge.java
 * Package: objects.base.group
 * Author:  Zachary Gill
 */

package objects.base.group;

import java.awt.Color;

import main.Environment;
import objects.base.AbstractObject;
import objects.base.simple.Edge;

/**
 * Defines a Locked Edge.
 */
public class LockedEdge extends Edge {
    
    //Constructors
    
    /**
     * The constructor for a Locked Edge.
     *
     * @param parent The parent of the Edge.
     * @param color  The color of the Edge.
     * @param o1     The first object whose center the Edge is locked to.
     * @param o2     The second object whose center the Edge is locked to.
     */
    public LockedEdge(AbstractObject parent, Color color, AbstractObject o1, AbstractObject o2) {
        super(parent, color, o1.getCenter(), o2.getCenter());
        
        Environment.addTask(() -> {
            setV1(o1.getCenter());
            setV2(o2.getCenter());
        });
    }
    
}
