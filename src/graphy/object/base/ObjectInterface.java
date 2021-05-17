/*
 * File:    ObjectInterface.java
 * Package: graphy.object
 * Author:  Zachary Gill
 */

package graphy.object.base;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import java.util.UUID;

import commons.math.component.matrix.Matrix3;
import commons.math.component.vector.Vector;

/**
 * The interface that defines the contract for creating an Object class.
 */
public interface ObjectInterface {
    
    //Methods
    
    /**
     * Performs pre-preparing steps on the Object.
     *
     * @param perspective The perspective to pre-prepare the Object for.
     * @return Whether or not the Object should continue preparing.
     */
    boolean prePrepare(UUID perspective);
    
    /**
     * Prepares the Object to be rendered.
     *
     * @param perspective The perspective to prepare the Object for.
     * @return The list of BaseObjects that were prepared.
     */
    List<BaseObject> prepare(UUID perspective);
    
    /**
     * Performs post-preparing steps on the Object.
     *
     * @param perspective The perspective to post-prepare the Object for.
     * @return Whether or not the Object should continue to rendering.
     */
    boolean postPrepare(UUID perspective);
    
    /**
     * Performs the preparation for the Object to be rendered.
     *
     * @param perspective The perspective to prepare the Object for.
     * @return The list of BaseObjects that were prepared.
     */
    List<BaseObject> doPrepare(UUID perspective);
    
    /**
     * Performs pre-rendering steps on the Object.
     *
     * @param perspective The perspective to pre-render the Object for.
     * @return Whether or not the Object should continue rendering.
     */
    boolean preRender(UUID perspective);
    
    /**
     * Renders the Object on the screen.
     *
     * @param perspective The perspective to render the Object for.
     * @param g2          The 2D Graphics entity.
     */
    void render(Graphics2D g2, UUID perspective);
    
    /**
     * Runes the animation tasks of the Object.
     */
    void runAnimationTasks();
    
    /**
     * Performs post-rendering steps on the Object.
     *
     * @param perspective The perspective to post-render the Object for.
     * @param g2          The 2D Graphics entity.
     */
    void postRender(Graphics2D g2, UUID perspective);
    
    /**
     * Draws the frame for the Object.
     *
     * @param perspective The perspective to render the frame for.
     * @param g2          The 2D Graphics entity.
     */
    void renderFrame(Graphics2D g2, UUID perspective);
    
    /**
     * Performs the rendering for the Object on the screen.
     *
     * @param perspective The perspective to render the Object for.
     * @param g2          The 2D Graphics entity.
     */
    void doRender(Graphics2D g2, UUID perspective);
    
    /**
     * Moves the Object in a certain direction.
     *
     * @param offset The relative offsets to move the Object.
     */
    void move(Vector offset);
    
    /**
     * Rotates the Object in a certain direction.
     *
     * @param offset The relative offsets to rotate the Object.
     */
    void rotate(Vector offset);
    
    /**
     * Rotates the Object in a certain direction and saves the rotation in its vector state.
     *
     * @param offset The relative offsets to rotate the Object.
     */
    void rotateAndTransform(Vector offset);
    
    /**
     * Rotates the Object in a certain direction and saves the rotation in its vector state.
     *
     * @param offset The relative offsets to rotate the Object.
     * @param center The center to rotate the Object about.
     */
    void rotateAndTransform(Vector offset, Vector center);
    
    /**
     * Calculates the distance from the Camera to the Object.
     *
     * @param perspective The perspective to use to calculate the distance from the Camera to the Object.
     * @return The distance from the Camera to the Object.
     */
    double calculateRenderDistance(UUID perspective);
    
    /**
     * Adds a constant movement animation to an Object.
     *
     * @param xSpeed The speed of the x movement in units per second.
     * @param ySpeed The speed of the y movement in units per second.
     * @param zSpeed The speed of the z movement in units per second.
     * @return The id of the animation task.
     */
    UUID addMovementAnimation(double xSpeed, double ySpeed, double zSpeed);
    
    /**
     * Adds a movement transformation to an Object over a period of time.
     *
     * @param xMovement The total x movement in radians.
     * @param yMovement The total y movement in radians.
     * @param zMovement The total z movement in radians.
     * @param period    The period over which to perform the transition in milliseconds.
     * @return The id of the animation task.
     */
    UUID addMovementTransformation(double xMovement, double yMovement, double zMovement, long period);
    
    /**
     * Adds a constant rotation animation to an Object.
     *
     * @param yawSpeed   The speed of the yaw rotation in radians per second.
     * @param pitchSpeed The speed of the pitch rotation in radians per second.
     * @param rollSpeed  The speed of the roll rotation in radians per second.
     * @return The id of the animation task.
     */
    UUID addRotationAnimation(double yawSpeed, double pitchSpeed, double rollSpeed);
    
    /**
     * Adds a rotation transformation to an Object over a period of time.
     *
     * @param yawRotation   The total yaw rotation in radians.
     * @param pitchRotation The total pitch rotation in radians.
     * @param rollRotation  The total roll rotation in radians.
     * @param period        The period over which to perform the transition in milliseconds.
     * @return The id of the animation task.
     */
    UUID addRotationTransformation(double yawRotation, double pitchRotation, double rollRotation, long period);
    
    /**
     * Adds a constant color animation to an Object.
     *
     * @param period The period of the color animation in milliseconds.
     * @param offset The offset of the color animation in milliseconds.
     * @return The id of the animation task.
     */
    UUID addColorAnimation(long period, long offset);
    
    /**
     * Adds a constant orbit animation to an Object.
     *
     * @param object      The Object to orbit around.
     * @param orbitPeriod The period of the orbit in milliseconds.
     * @param clockwise   Whether the orbit around the Object should be clockwise or counterclockwise.
     * @return The id of the animation task.
     */
    UUID addOrbitAnimation(Object object, double orbitPeriod, boolean clockwise);
    
    /**
     * Adds an orbit transformation to an Object over a period of time.
     *
     * @param object    The Object to orbit around.
     * @param orbits    The number of orbits to perform during the transformation.
     * @param period    The period of the orbit in milliseconds.
     * @param clockwise Whether the orbit around the Object should be clockwise or counterclockwise.
     * @return The id of the animation task.
     */
    UUID addOrbitTransformation(Object object, double orbits, double period, boolean clockwise);
    
    /**
     * Registers a component with the Object.
     *
     * @param component The component to register.
     */
    void registerComponent(ObjectInterface component);
    
    /**
     * Unregisters a component with the Object.
     *
     * @param component The component to unregister.
     */
    void unregisterComponent(ObjectInterface component);
    
    /**
     * Registers a Frame with the Object.
     *
     * @param frame The Frame to register.
     */
    void registerFrame(Frame frame);
    
    /**
     * Returns whether the Object is undergoing a movement transformation or not.
     *
     * @return Whether the Object is undergoing a movement transformation or not.
     */
    boolean inMovementTransformation();
    
    /**
     * Returns whether the Object is undergoing a rotation transformation or not.
     *
     * @return Whether the Object is undergoing a rotation transformation or not.
     */
    boolean inRotationTransformation();
    
    /**
     * Returns whether the Object is undergoing an orbit transformation or not.
     *
     * @return Whether the Object is undergoing an orbit transformation or not.
     */
    boolean inOrbitTransformation();
    
    
    //Setters
    
    /**
     * Sets the center point of the Object.
     *
     * @param center The new center point of the Object.
     */
    void setCenter(Vector center);
    
    /**
     * Sets the color of the Object.
     *
     * @param color The new color of the Object.
     */
    void setColor(Color color);
    
    /**
     * Sets the angles that define the rotation of the Object.
     *
     * @param rotation The angles that define the rotation of the Object.
     */
    void setRotation(Vector rotation);
    
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
