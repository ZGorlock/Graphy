/*
 * File:    BaseObject.java
 * Package: objects.base
 * Author:  Zachary Gill
 */

package objects.base;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import camera.Camera;
import main.Environment;
import math.matrix.Matrix3;
import math.vector.Vector;
import utility.RotationUtility;

/**
 * Defines the base properties of an Object.
 */
public abstract class BaseObject extends AbstractObject {
    
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
        this(parent, Color.BLACK, center, vertices.toArray(new Vector[] {}));
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
        this(null, color, center, vertices.toArray(new Vector[] {}));
    }
    
    /**
     * The constructor for a BaseObject from a list of vertices.
     *
     * @param center   The center of the Object.
     * @param vertices The vertices that define the Object, as a list.
     * @throws ArithmeticException When the vertices are not all of the same spacial dimension.
     */
    public BaseObject(Vector center, List<Vector> vertices) throws ArithmeticException {
        this(null, Color.BLACK, center, vertices.toArray(new Vector[] {}));
    }
    
    /**
     * The constructor for a BaseObject from a list of vertices.
     *
     * @param vertices The vertices that define the Object, as a list.
     * @throws ArithmeticException When the vertices are not all of the same spacial dimension.
     */
    public BaseObject(List<Vector> vertices) throws ArithmeticException {
        this(null, Color.BLACK, Environment.ORIGIN, vertices.toArray(new Vector[] {}));
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
    public void move(Vector offset) {
        super.move(offset);
        
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = vertices[i].plus(offset);
        }
    }
    
    /**
     * Rotates the Object in a certain direction and saves the rotation in its vector state.
     *
     * @param offset The relative offsets to rotate the Object.
     */
    @Override
    public void rotateAndTransform(Vector offset) {
        rotateAndTransform(offset, getRootCenter());
    }
    
    /**
     * Rotates the Object in a certain direction and saves the rotation in its vector state.
     *
     * @param offset The relative offsets to rotate the Object.
     * @param center The center to rotate the Object about.
     */
    @Override
    public void rotateAndTransform(Vector offset, Vector center) {
        Matrix3 rotationTransformationMatrix = RotationUtility.getRotationMatrix(offset.getX(), offset.getY(), offset.getZ());
        
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = RotationUtility.performRotation(vertices[i], rotationTransformationMatrix, center.justify());
        }
        this.center = RotationUtility.performRotation(this.center, rotationTransformationMatrix, center.justify());
    }
    
    /**
     * Calculates the distance from the Camera to Object.
     *
     * @return The distance from the Camera to the Object.
     */
    @Override
    public double calculateRenderDistance() {
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
        renderDistance = max;
        return renderDistance;
    }
    
}
