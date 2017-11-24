/*
 * File:    Camera.java
 * Package: objects.system
 * Author:  Zachary Gill
 */

package objects.system;

import main.Environment;
import objects.RectangularPyramid;
import objects.base.Frame;
import objects.base.Object;
import objects.base.polygon.Rectangle;
import objects.base.simple.BigVertex;
import objects.base.simple.Edge;

import java.awt.*;

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
    public Camera()
    {
        super(Environment.origin, Color.BLACK);
    
        camera = new BigVertex(this, Color.RED, Environment.origin, 3);
        screenNormal = new Edge(this, Color.BLUE, Environment.origin, Environment.origin);
        screenXNormal = new Edge(this, Color.RED, Environment.origin, Environment.origin);
        screenYNormal = new Edge(this, Color.GREEN, Environment.origin, Environment.origin);
    
        screen = new Rectangle(Color.RED, Environment.origin, Environment.origin, Environment.origin, Environment.origin); //not rendered
        cameraEnclosure = new RectangularPyramid(this, new Color(192, 192, 192, 64), screen, Environment.origin);
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
