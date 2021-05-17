/*
 * File:    ImageSquare.java
 * Package: graphy.objects.complex
 * Author:  Zachary Gill
 */

package graphy.object.complex;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.UUID;

import commons.graphics.DrawUtility;
import commons.math.component.vector.Vector;
import graphy.object.base.AbstractObject;
import graphy.object.base.polygon.Square;

/**
 * Defines a Square.
 */
public class ImageSquare extends Square {
    
    //Fields
    
    /**
     * The image to print on the Image Square.
     */
    protected BufferedImage image;
    
    
    //Constructors
    
    /**
     * The constructor for a Image Square.
     *
     * @param parent The parent of the Image Square.
     * @param color  The color of the Image Square.
     * @param v1     The first point of the Image Square.
     * @param side   The side length of the Image Square.
     */
    public ImageSquare(AbstractObject parent, Color color, Vector v1, double side) {
        super(parent, color, v1, side);
    }
    
    /**
     * The constructor for a Image Square.
     *
     * @param parent The parent of the Image Square.
     * @param v1     The first point of the Image Square.
     * @param side   The side length of the Image Square.
     */
    public ImageSquare(AbstractObject parent, Vector v1, double side) {
        this(parent, Color.BLACK, v1, side);
    }
    
    /**
     * The constructor for a Image Square.
     *
     * @param color The color of the Image Square.
     * @param v1    The first point of the Image Square.
     * @param side  The side length of the Image Square.
     */
    public ImageSquare(Color color, Vector v1, double side) {
        this(null, color, v1, side);
    }
    
    /**
     * The constructor for a Image Square.
     *
     * @param v1   The first point of the Image Square.
     * @param side The side length of the Image Square.
     */
    public ImageSquare(Vector v1, double side) {
        this(null, Color.BLACK, v1, side);
    }
    
    
    //Methods
    
    /**
     * Renders the Image Square on the screen.
     *
     * @param perspective The perspective to render the Image Square for.
     * @param g2          The 2D Graphics entity.
     */
    @Override
    public void render(Graphics2D g2, UUID perspective) {
        super.render(g2, perspective);
        if (image != null) {
            DrawUtility.drawImage(g2, image, prepared.get(perspective).get(0),
                    Math.abs((int) (prepared.get(perspective).get(1).getRawX() - prepared.get(perspective).get(0).getRawX())), Math.abs((int) (prepared.get(perspective).get(3).getRawY() - prepared.get(perspective).get(0).getRawY())));
        }
    }
    
    
    //Getters
    
    /**
     * Returns the image to print on the Image Square.
     *
     * @return The image to print on the Image Square.
     */
    public BufferedImage getImage() {
        return image;
    }
    
    
    //Setters
    
    /**
     * Sets the image to print on the Image Square.
     *
     * @param image The image.
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }
    
}
