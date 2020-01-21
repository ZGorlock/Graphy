/*
 * File:    Origin.java
 * Package: objects.system
 * Author:  Zachary Gill
 */

package objects.system;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

import main.Environment;
import objects.base.Object;
import objects.polyhedron.regular.platonic.Hexahedron;

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
        
        Timer updateOrigin = new Timer();
        updateOrigin.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                origin.reposition(Environment.ORIGIN);
            }
        }, 0, 1000 / Environment.fps);
    }
    
    
    //Methods
    
    /**
     * Calculates the Objects for the Origin Object.
     */
    @Override
    protected void calculate() {
    }
    
}
