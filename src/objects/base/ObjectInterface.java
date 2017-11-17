/*
 * File:    ObjectInterface.java
 * Package: objects
 * Author:  Zachary Gill
 */

package objects.base;

import math.matrix.Matrix3;
import math.vector.Vector;

import java.awt.*;
import java.util.List;

/**
 * The interface that defines the contract for creating an Object class.
 */
public interface ObjectInterface
{
    
    //Methods
    
    /**
     * Prepares the Object to be rendered.
     *
     * @return The list of BaseObjects that were prepared.
     */
    List<BaseObject> prepare();
    
    /**
     * Renders the Object on the screen.
     *
     * @param g2 The 2D Graphics entity.
     */
    void render(Graphics2D g2);
    
    /**
     * Moves the Object in a certain direction.
     *
     * @param offset The relative offsets to move the Object.
     */
    void move(Vector offset);
    
    /**
     * Adds a constant movement animation to an Object.
     *
     * @param xSpeed The speed of the x movement in units per second.
     * @param ySpeed The speed of the y movement in units per second.
     * @param zSpeed The speed of the z movement in units per second.
     */
    void addMovementAnimation(double xSpeed, double ySpeed, double zSpeed);
    
    /**
     * Adds a constant rotation animation to an Object.
     *
     * @param yawSpeed   The speed of the yaw rotation in radians per second.
     * @param pitchSpeed The speed of the pitch rotation in radians per second.
     * @param rollSpeed  The speed of the roll rotation in radians per second.
     */
    void addRotationAnimation(double yawSpeed, double pitchSpeed, double rollSpeed);
    
    /**
     * Adds a constant color animation to an Object.
     *
     * @param period The period of the color animation.
     * @param offset The offset of the color animation.
     */
    void addColorAnimation(double period, double offset);
    
    /**
     * Registers a component with the Object.
     *
     * @param component The component to register.
     */
    void registerComponent(ObjectInterface component);
    
    /**
     * Registers a Frame with the Object.
     *
     * @param frame The Frame to register.
     */
    void registerFrame(Frame frame);
    
    
    //Setters
    
    /**
     * Sets the center point of the Object.
     *
     * @param center The nwe center point of the Object.
     */
    void setCenter(Vector center);
    
    /**
     * Sets the color of the Object.
     *
     * @param color The new color of the Object.
     */
    void setColor(Color color);
    
    /**
     * Sets the angles that define the rotation of the Object from the parent of that Object.
     *
     * @param rotation The angles that define the rotation of the Object.
     */
    void setRotationWithoutUpdate(Vector rotation);
    
    /**
     * Sets the transformation Matrix that defines the rotation of the Object.
     *
     * @param rotationMatrix The transformation Matrix that defines the rotation of the Object.
     */
    void setRotationMatrix(Matrix3 rotationMatrix);
    
    /**
     * Sets the visibility of the Object.
     *
     * @param visible The new visibility of the Object.
     */
    void setVisible(boolean visible);
    
    /**
     * Sets the display mode of the Object.
     *
     * @param displayMode The new display mode of the Object.
     */
    void setDisplayMode(BaseObject.DisplayMode displayMode);
    
    /**
     * Sets whether to clip Vectors or not.
     *
     * @param clippingEnabled The new clipping mode of the Object.
     */
    void setClippingEnabled(boolean clippingEnabled);
    
}
