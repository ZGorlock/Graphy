/*
 * File:    BaseObject.java
 * Package: objects.base
 * Author:  Zachary Gill
 */

package objects.base;

import camera.Camera;
import main.Environment;
import math.vector.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines the base properties of an Object.
 */
public abstract class BaseObject extends AbstractObject
{
    
    //Fields
    
    /**
     * The array of Vertices that define the Object.
     */
    protected Vector[] vertices;
    
    /**
     * A list of the Vectors of the Object that have been prepared for rendering.
     */
    protected final List<Vector> prepared = new ArrayList<>();
    
    
    //Constructors
    
    /**
     * The constructor for a BaseObject.
     *
     * @param parent   The parent of the Object.
     * @param color    The color of the Object.
     * @param center   The center of the Object.
     * @param vertices The vertices that define the Object.
     * @throws ArithmeticException When the vertices are not all of the same spacial dimension.
     */
    public BaseObject(AbstractObject parent, Color color, Vector center, Vector... vertices) throws ArithmeticException {
        if (vertices.length > 0) {
            int dim = vertices[0].getDimension();
            for (Vector vertex : vertices) {
                if (vertex.getDimension() != dim) {
                    throw new ArithmeticException("Not all of the vertices for the Base Object are in the same spacial dimension.");
                }
            }
        }
        
        this.vertices = new Vector[vertices.length];
        System.arraycopy(vertices, 0, this.vertices, 0, vertices.length);
        
        this.color = color;
        this.center = center;
        setParent(parent);
    }
    
    /**
     * The constructor for a BaseObject from a list of vertices.
     *
     * @param parent   The parent of the Object.
     * @param center   The center of the Object.
     * @param vertices The vertices that define the Object, as a list.
     * @throws ArithmeticException When the vertices are not all of the same spacial dimension.
     */
    public BaseObject(AbstractObject parent, Vector center, List<Vector> vertices) throws ArithmeticException {
        this(parent, Color.BLACK, center, vertices.toArray(new Vector[]{}));
    }
    
    /**
     * The constructor for a BaseObject from a list of vertices.
     *
     * @param color    The color of the Object.
     * @param center   The center of the Object.
     * @param vertices The vertices that define the Object, as a list.
     * @throws ArithmeticException When the vertices are not all of the same spacial dimension.
     */
    public BaseObject(Color color, Vector center, List<Vector> vertices) throws ArithmeticException {
        this(null, color, center, vertices.toArray(new Vector[]{}));
    }
    
    /**
     * The constructor for a BaseObject from a list of vertices.
     *
     * @param center   The center of the Object.
     * @param vertices The vertices that define the Object, as a list.
     * @throws ArithmeticException When the vertices are not all of the same spacial dimension.
     */
    public BaseObject(Vector center, List<Vector> vertices) throws ArithmeticException {
        this(null, Color.BLACK, center, vertices.toArray(new Vector[]{}));
    }
    
    /**
     * The constructor for a BaseObject from a list of vertices.
     *
     * @param vertices The vertices that define the Object, as a list.
     * @throws ArithmeticException When the vertices are not all of the same spacial dimension.
     */
    public BaseObject(List<Vector> vertices) throws ArithmeticException {
        this(null, Color.BLACK, Environment.origin, vertices.toArray(new Vector[]{}));
    }
    
    
    //Methods
    
    /**
     * Prepares the Object to be rendered.
     *
     * @return The list of BaseObjects that were prepared.
     */
    @Override
    public abstract List<BaseObject> prepare();
    
    /**
     * Renders the Object on the screen.
     *
     * @param g2 The 2D Graphics entity.
     */
    @Override
    public abstract void render(Graphics2D g2);
    
    /**
     * Moves the Object in a certain direction.
     *
     * @param offset The relative offsets to move the Object.
     */
    @Override
    public void move(Vector offset)
    {
        super.move(offset);
        
        for (int i = 0; i < vertices.length; i++) {
           vertices[i] = vertices[i].plus(offset);
        }
    }
    
    /**
     * Calculates the distance of the prepared Vectors of the Object from the Camera.
     *
     * @return The distance of the prepared Vectors of the Object from the Camera.
     */
    public double calculatePreparedDistance()
    {
        if (prepared.isEmpty()) {
            return 0;
        }
    
        Camera cam = Camera.getActiveCameraView();
        if (cam == null) {
            return 0;
        }
        
        Vector pos = cam.getCameraPosition();
        double max = 0;
        for (Vector prepare : prepared) {
            double dist = prepare.distance(pos);
            if (dist > max) {
                max = dist;
            }
        }
        return max;
    }
    
    /**
     * Draws the frame for the Object.
     *
     * @param g2 The 2D Graphics entity.
     */
    public void addFrame(Graphics2D g2)
    {
        if (frame == null) {
            return;
        }
        
        frame.render(g2, prepared);
    }
    
    
    //Getters
    
    /**
     * Returns the list of Vertices that define the Object.
     *
     * @return The list of Vertices that define the Object.
     */
    public Vector[] getVertices()
    {
        return vertices;
    }
    
}
