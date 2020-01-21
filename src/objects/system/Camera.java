/*
 * File:    Camera.java
 * Package: objects.system
 * Author:  Zachary Gill
 */

package objects.system;

import java.awt.Color;

import main.Environment;
import objects.base.Object;
import objects.base.polygon.Rectangle;
import objects.base.simple.BigVertex;
import objects.base.simple.Edge;
import objects.polyhedron.irregular.RectangularPyramid;

/**
 * Defines a Camera Object.
 */
public class Camera extends Object {
    
    //Fields
    
    /**
     * The point of the Camera.
     */
    public BigVertex camera;
    
    /**
     * The Rectangle of the Screen.
     */
    public Rectangle screen;
    
    /**
     * The Edges for the three normal Vectors defining the Screen's local coordinate system.
     */
    public Edge screenNormal, screenXNormal, screenYNormal;
    
    /**
     * The Triangles that make up the Camera enclosure.
     */
    public RectangularPyramid cameraEnclosure;
    
    
    //Constructors
    
    /**
     * The constructor for a Camera.
     */
    public Camera() {
        super(Environment.ORIGIN, Color.BLACK);
        
        camera = new BigVertex(this, Color.RED, Environment.ORIGIN, 3);
        screenNormal = new Edge(this, Color.BLUE, Environment.ORIGIN, Environment.ORIGIN);
        screenXNormal = new Edge(this, Color.RED, Environment.ORIGIN, Environment.ORIGIN);
        screenYNormal = new Edge(this, Color.GREEN, Environment.ORIGIN, Environment.ORIGIN);
        
        screen = new Rectangle(Color.RED, Environment.ORIGIN, Environment.ORIGIN, Environment.ORIGIN, Environment.ORIGIN); //not rendered
        cameraEnclosure = new RectangularPyramid(this, new Color(192, 192, 192, 64), screen, Environment.ORIGIN);
        cameraEnclosure.addFrame(Color.BLACK);
        
        setDisplayMode(DisplayMode.FACE);
    }
    
    
    //Methods
    
    /**
     * Calculates the Objects for the Camera Object.
     */
    @Override
    protected void calculate() {
    }
    
}
