/*
 * File:    Camera.java
 * Package: objects
 * Author:  Zachary Gill
 */

package objects;

import main.Environment;
import math.vector.Vector;
import objects.base.*;
import objects.base.Frame;
import objects.base.Object;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines a Camera Object.
 */
public class Camera extends Object
{
    
    //Fields
    
    /**
     * The point of the Camera.
     */
    public BigVertex camera;
    
    /**
     * The Rect of the Screen.
     */
    public Rect screen;
    
    /**
     * The Edges for the three normal Vectors defining the Screen's local coordinate system.
     */
    public Edge screenNormal, screenXNormal, screenYNormal;
    
    /**
     * The Triangles that make up the Camera enclosure.
     */
    public Pyramid cameraEnclosure;
    
    
    //Constructors
    
    /**
     * The constructor for a Camera.
     */
    public Camera()
    {
        super(Environment.origin, Color.BLACK);
    
        camera = new BigVertex(this, Color.RED, Environment.origin, 3);
        screenNormal = new Edge(this, Color.BLUE, Environment.origin, Environment.origin);
        screenXNormal = new Edge(this, Color.RED, Environment.origin, Environment.origin);
        screenYNormal = new Edge(this, Color.GREEN, Environment.origin, Environment.origin);
    
        screen = new Rect(Color.RED, Environment.origin, Environment.origin, Environment.origin, Environment.origin); //not rendered
        cameraEnclosure = new Pyramid(this, new Color(192, 192, 192, 64), screen, Environment.origin);
        frame = new Frame(cameraEnclosure);
        
        setDisplayMode(DisplayMode.FACE);
    }
    
    
    //Methods
    
    /**
     * Calculates the Objects for the Camera Object.
     */
    @Override
    protected void calculate()
    {
    }
    
}
