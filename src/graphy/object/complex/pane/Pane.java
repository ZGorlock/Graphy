/*
 * File:    Pane.java
 * Package: graphy.objects.complex.pane
 * Author:  Zachary Gill
 */

package graphy.object.complex.pane;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import commons.graphics.ImageTransformationUtility;
import commons.math.vector.Vector;
import commons.math.vector.Vector3;
import graphy.main.Environment;
import graphy.object.base.AbstractObject;
import graphy.object.base.BaseObject;
import graphy.object.base.polygon.Rectangle;

/**
 * Defines a Pane.
 */
public abstract class Pane extends BaseObject {
    
    //Fields
    
    /**
     * The bounds of the Pane.
     */
    public Rectangle bounds;
    
    /**
     * The unit normal vector of the Pane.
     */
    public Vector normal;
    
    /**
     * The image of the Pane.
     */
    public BufferedImage image;
    
    
    //Constructors
    
    /**
     * The constructor for a Pane.
     *
     * @param parent The parent of the Pane.
     * @param color  The color of the Pane.
     * @param bounds The bounds of the Pane.
     */
    public Pane(AbstractObject parent, Color color, Rectangle bounds) {
        super(parent, color, bounds.getCenter(), bounds.getP1(), bounds.getP2(), bounds.getP3(), bounds.getP4());
        
        this.bounds = bounds;
        
        visible = true;
    }
    
    
    //Methods
    
    /**
     * Prepares the Pane to be rendered.
     *
     * @param perspective The perspective to prepare the Pane for.
     * @return The list of BaseObjects that were prepared.
     */
    @Override
    public java.util.List<BaseObject> prepare(UUID perspective) {
        List<BaseObject> preparedBases = new ArrayList<>();
        List<Vector> perspectivePrepared = prepared.get(perspective);
        
        perspectivePrepared.clear();
        for (Vector vertex : vertices) {
            perspectivePrepared.add(vertex.clone().justify());
        }
        
        perspectivePrepared.add(Vector.averageVector(perspectivePrepared));
        perspectivePrepared.add(perspectivePrepared.get(0).midpoint(perspectivePrepared.get(1)));
        perspectivePrepared.add(perspectivePrepared.get(1).midpoint(perspectivePrepared.get(2)));
        perspectivePrepared.add(perspectivePrepared.get(2).midpoint(perspectivePrepared.get(3)));
        perspectivePrepared.add(perspectivePrepared.get(3).midpoint(perspectivePrepared.get(0)));
        perspectivePrepared.add(perspectivePrepared.get(4).midpoint(perspectivePrepared.get(0)));
        perspectivePrepared.add(perspectivePrepared.get(4).midpoint(perspectivePrepared.get(1)));
        perspectivePrepared.add(perspectivePrepared.get(4).midpoint(perspectivePrepared.get(2)));
        perspectivePrepared.add(perspectivePrepared.get(4).midpoint(perspectivePrepared.get(3)));
        perspectivePrepared.add(perspectivePrepared.get(4).midpoint(perspectivePrepared.get(5)));
        perspectivePrepared.add(perspectivePrepared.get(4).midpoint(perspectivePrepared.get(6)));
        perspectivePrepared.add(perspectivePrepared.get(4).midpoint(perspectivePrepared.get(7)));
        perspectivePrepared.add(perspectivePrepared.get(4).midpoint(perspectivePrepared.get(8)));
        
        performRotationTransformation(perspectivePrepared);
        
        preparedBases.add(this);
        return preparedBases;
    }
    
    /**
     * Renders the Pane on the screen.
     *
     * @param perspective The perspective to render the Pane for.
     * @param g2          The 2D Graphics entity.
     */
    @Override
    public void render(Graphics2D g2, UUID perspective) {
        calculateNormal();
        g2.setColor(getColor());
        switch (displayMode) {
            case VERTEX:
                for (Vector v : prepared.get(perspective)) {
                    g2.drawRect((int) v.getX(), (int) v.getY(), 1, 1);
                }
                break;
            
            case FACE:
                draw(g2, perspective);
            
            case EDGE:
                for (int i = 1; i < 4; i++) {
                    g2.drawLine((int) prepared.get(perspective).get(i - 1).getX(), (int) prepared.get(perspective).get(i - 1).getY(), (int) prepared.get(perspective).get(i).getX(), (int) prepared.get(perspective).get(i).getY());
                }
                g2.drawLine((int) prepared.get(perspective).get(3).getX(), (int) prepared.get(perspective).get(3).getY(), (int) prepared.get(perspective).get(0).getX(), (int) prepared.get(perspective).get(0).getY());
                
                break;
        }
    }
    
    /**
     * Calculates the unit normal vector of the Pane.
     *
     * @return The unit normal vector of the Pane.
     */
    public Vector calculateNormal() {
        normal = new Vector3(bounds.getP2().minus(bounds.getP1())).cross(new Vector3(bounds.getP4().minus(bounds.getP1()))).normalize();
        return normal;
    }
    
    /**
     * Draws the drawing to the Pane.
     *
     * @param g2          The 2D Graphics entity.
     * @param perspective The perspective from which to draw the drawing to the Pane.
     */
    protected void draw(Graphics2D g2, UUID perspective) {
        if (image != null) {
            ImageTransformationUtility.transformImage(image, ImageTransformationUtility.getBoundsForImage(image), g2, Environment.screenWidth, Environment.screenHeight, prepared.get(perspective).subList(0, 4));
        }
    }
    
    
    //Setters
    
    /**
     * Sets the image of the Pane.
     *
     * @param image The image of the Pane.
     */
    public synchronized void setImage(BufferedImage image) {
        this.image = image;
    }
    
}
