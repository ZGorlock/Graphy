/*
 * File:    ImageSquare.java
 * Package: objects.complex
 * Author:  Zachary Gill
 */

package objects.complex;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import math.vector.Vector;
import objects.base.AbstractObject;
import objects.base.polygon.Square;

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
     * @param g2 The 2D Graphics entity.
     */
    @Override
    public void render(Graphics2D g2) {
        super.render(g2);
        if (image != null) {
            g2.drawImage(image, (int) prepared.get(0).getX(), (int) prepared.get(0).getY(),
                    Math.abs((int) (prepared.get(1).getX() - prepared.get(0).getX())), Math.abs((int) (prepared.get(3).getY() - prepared.get(0).getY())), null);
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
