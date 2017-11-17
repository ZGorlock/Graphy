package objects.base;

import camera.Camera;
import main.Environment;
import math.vector.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines a Triangle.
 */
public class Triangle extends BaseObject
{
    
    //Constructors
    
    /**
     * The constructor for a Triangle.
     *
     * @param parent The parent of the Triangle.
     * @param color  The color of the Triangle.
     * @param v1     The first point of the Triangle.
     * @param v2     The second point of the Triangle.
     * @param v3     The third point of the Triangle.
     */
    public Triangle(AbstractObject parent, Color color, Vector v1, Vector v2, Vector v3)
    {
        super(color, v1, v2, v3);
        center = v1.average(v2, v3);
        type = Triangle.class;
        setParent(parent);
    }
    
    /**
     * The constructor for a Triangle from three Vertices.
     *
     * @param parent The parent of the Triangle.
     * @param v1     The first point of the Triangle.
     * @param v2     The second point of the Triangle.
     * @param v3     The third point of the Triangle.
     */
    public Triangle(AbstractObject parent, Vector v1, Vector v2, Vector v3)
    {
        this(parent, Color.BLACK, v1, v2, v3);
    }
    
    /**
     * The constructor for a Triangle from three Vertices.
     *
     * @param color The color of the Triangle.
     * @param v1    The first point of the Triangle.
     * @param v2    The second point of the Triangle.
     * @param v3    The third point of the Triangle.
     */
    public Triangle(Color color, Vector v1, Vector v2, Vector v3)
    {
        this(null, color, v1, v2, v3);
    }
    
    /**
     * The constructor for a Triangle.
     *
     * @param v1 The first point of the Triangle.
     * @param v2 The second point of the Triangle.
     * @param v3 The third point of the Triangle.
     */
    public Triangle(Vector v1, Vector v2, Vector v3)
    {
        this(null, Color.BLACK, v1, v2, v3);
    }
    
    
    //Methods
    
    /**
     * Prepares the Triangle to be rendered.
     *
     * @return The list of BaseObjects that were prepared.
     */
    @Override
    public List<BaseObject> prepare()
    {
        List<BaseObject> preparedBases = new ArrayList<>();
        if (!visible) {
            return preparedBases;
        }
        
        prepared.clear();
        prepared.add(vertices[0].clone());
        prepared.add(vertices[1].clone());
        prepared.add(vertices[2].clone());
    
        performRotationTransformation(prepared);
        
        preparedBases.add(this);
        return preparedBases;
    }
    
    /**
     * Renders the Triangle on the screen.
     *
     * @param g2 The 2D Graphics entity.
     */
    public void render(Graphics2D g2)
    {
        if (!visible || prepared.size() != 3) {
            return;
        }
    
        Camera.projectVectorToCamera(prepared);
        Camera.collapseVectorToViewport(prepared);
    
        if (!clippingEnabled || Camera.hasVectorInView(prepared, vertices)) {
            Camera.scaleVectorToScreen(prepared);
        
            g2.setColor(getColor());
            switch (displayMode) {
                case VERTEX:
                    g2.drawRect((int) prepared.get(0).getX(), (int) prepared.get(0).getY(), 1, 1);
                    g2.drawRect((int) prepared.get(1).getX(), (int) prepared.get(1).getY(), 1, 1);
                    g2.drawRect((int) prepared.get(2).getX(), (int) prepared.get(2).getY(), 1, 1);
                    break;
                case EDGE:
                    g2.drawLine((int) prepared.get(0).getX(), (int) prepared.get(0).getY(), (int) prepared.get(1).getX(), (int) prepared.get(1).getY());
                    g2.drawLine((int) prepared.get(1).getX(), (int) prepared.get(1).getY(), (int) prepared.get(2).getX(), (int) prepared.get(2).getY());
                    g2.drawLine((int) prepared.get(2).getX(), (int) prepared.get(2).getY(), (int) prepared.get(0).getX(), (int) prepared.get(0).getY());
                    break;
                case FACE:
                    Polygon face = new Polygon(
                            new int[] {(int) prepared.get(0).getX(), (int) prepared.get(1).getX(), (int) prepared.get(2).getX()},
                            new int[] {(int) prepared.get(0).getY(), (int) prepared.get(1).getY(), (int) prepared.get(2).getY()},
                            3
                    );
                    g2.fillPolygon(face);
                    break;
            }
    
            addFrame(g2);
        }
    }
    
    
    //Getters
    
    /**
     * Returns the first point of the Triangle.
     *
     * @return The first point of the Triangle.
     */
    public Vector getP1()
    {
        return vertices[0];
    }
    
    /**
     * Returns the second point of the Triangle.
     *
     * @return The second point of the Triangle.
     */
    public Vector getP2()
    {
        return vertices[1];
    }
    
    /**
     * Returns the third point of the Triangle.
     *
     * @return The third point of the Triangle.
     */
    public Vector getP3()
    {
        return vertices[2];
    }
    
    
    //Setters
    
    /**
     * Sets the point of the Triangle.
     *
     * @param p1 The first point of the Triangle.
     * @param p2 The second point of the Triangle.
     * @param p3 The third point of the Triangle.
     */
    public void setPoints(Vector p1, Vector p2, Vector p3)
    {
        setP1(p1);
        setP2(p2);
        setP3(p3);
    }
    
    /**
     * Sets the first point of the Triangle.
     *
     * @param p1 The new first point of the Triangle.
     */
    public void setP1(Vector p1)
    {
        vertices[0] = p1;
    }
    
    /**
     * Sets the second point of the Triangle.
     *
     * @param p2 The new second point of the Triangle.
     */
    public void setP2(Vector p2)
    {
        vertices[1] = p2;
    }
    
    /**
     * Sets the third point of the Triangle.
     *
     * @param p3 The new third point of the Triangle.
     */
    public void setP3(Vector p3)
    {
        vertices[2] = p3;
    }
    
}