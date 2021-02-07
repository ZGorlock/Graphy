/*
 * File:    Camera.java
 * Package: graphy.objects.system
 * Author:  Zachary Gill
 */

package graphy.object.system;

import java.awt.Color;

import graphy.main.Environment;
import graphy.object.base.Object;
import graphy.object.base.polygon.Rectangle;
import graphy.object.base.simple.BigVertex;
import graphy.object.base.simple.Edge;
import graphy.object.polyhedron.irregular.RectangularPyramid;

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
