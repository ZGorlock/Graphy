/*
 * File:    Origin.java
 * Package: graphy.objects.system
 * Author:  Zachary Gill
 */

package graphy.object.system;

import java.awt.Color;

import graphy.main.Environment;
import graphy.object.base.Object;
import graphy.object.polyhedron.regular.platonic.Hexahedron;

/**
 * Defines the origin.
 */
public class Origin extends Object {
    
    //Constructors
    
    /**
     * The constructor for a Camera.
     */
    public Origin() {
        super(Environment.ORIGIN, Color.BLACK);
        
        Hexahedron origin = new Hexahedron(Environment.ORIGIN, Color.RED, 0.25);
        components.add(origin);
        
        Environment.addTask(() -> origin.reposition(Environment.ORIGIN));
    }
    
    
    //Methods
    
    /**
     * Calculates the Objects for the Origin Object.
     */
    @Override
    protected void calculate() {
    }
    
}
