/*
 * File:    AbstractObject.java
 * Package: objects.base
 * Author:  Zachary Gill
 */

package objects.base;

import main.Environment;
import math.matrix.Matrix3;
import math.vector.Vector;
import utility.ColorUtility;
import utility.RotationUtility;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Defines an abstract implementation of an Object.
 */
public abstract class AbstractObject implements ObjectInterface {
    
    //Fields
    
    /**
     * The parent of the Object.
     */
    protected AbstractObject parent;
    
    /**
     * The center point of the Object.
     */
    protected Vector center;
    
    /**
     * The color of the Object.
     */
    protected Color color;
    
    /**
     * The frame of the Object.
     */
    protected Frame frame;
    
    /**
     * The angles that define the rotation of the Object.
     */
    protected Vector rotation = new Vector(0, 0, 0);
    
    /**
     * The transformation Matrix that defines the rotation of the Object.
     */
    protected Matrix3 rotationMatrix = null;
    
    /**
     * The visibility of the Object.
     */
    protected boolean visible = true;
    
    /**
     * The display mode of the Object.
     */
    protected DisplayMode displayMode = DisplayMode.FACE;
    
    /**
     * The clipping mode of the Object.
     */
    protected boolean clippingEnabled = true;
    
    //TODO make protected
    
    /**
     * The animations timers of the Object.
     */
    public final List<Timer> animationTimers = new ArrayList<>();
    
    /**
     * The animations of movement set for the Object.
     */
    public final List<double[]> movementAnimations = new ArrayList<>();
    
    /**
     * The animations of rotation set for the Object.
     */
    public final List<double[]> rotationAnimations = new ArrayList<>();
    
    /**
     * A flag indicating whether the Object is currently undergoing a movement transformation or not.
     */
    public final AtomicBoolean inMovementTransformation = new AtomicBoolean(false);
    
    /**
     * A flag indicating whether the Object is currently undergoing a rotation transformation or not.
     */
    public final AtomicBoolean inRotationTransformation = new AtomicBoolean(false);
    
    
    //Enums
    
    
    /**
     * The enumeration of display modes for Objects.
     */
    public enum DisplayMode {
        VERTEX,
        EDGE,
        FACE
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
        center = center.plus(offset);
    }
    
    /**
     * Rotates the Object in a certain direction.
     *
     * @param offset The relative offsets to rotate the Object.
     */
    @Override
    public void rotate(Vector offset) {
        setRotation(rotation.plus(offset));
    }
    
    /**
     * Rotates the Object in a certain direction and saves the rotation in its vector state.
     *
     * @param offset The relative offsets to rotate the Object.
     */
    @Override
    public abstract void rotateAndTransform(Vector offset);
    
    /**
     * Rotates the Object in a certain direction and saves the rotation in its vector state.
     *
     * @param offset The relative offsets to rotate the Object.
     * @param center The center to rotate the Object about.
     */
    @Override
    public abstract void rotateAndTransform(Vector offset, Vector center);
    
    /**
     * Adds a constant movement animation to an Object.
     *
     * @param xSpeed The speed of the x movement in units per second.
     * @param ySpeed The speed of the y movement in units per second.
     * @param zSpeed The speed of the z movement in units per second.
     */
    @Override
    public void addMovementAnimation(double xSpeed, double ySpeed, double zSpeed) {
        Timer animationTimer = new Timer();
        animationTimers.add(animationTimer);
        movementAnimations.add(new double[]{xSpeed, ySpeed, zSpeed});
        animationTimer.scheduleAtFixedRate(new TimerTask() {
            private Vector speedVector = new Vector(xSpeed, ySpeed, zSpeed);
            
            private long lastTime = 0;
            
            @Override
            public void run() {
                if (lastTime == 0) {
                    lastTime = System.currentTimeMillis();
                    return;
                }
                
                long currentTime = System.currentTimeMillis();
                long timeElapsed = currentTime - lastTime;
                lastTime = currentTime;
                
                double scale = (double) timeElapsed / 1000;
                move(speedVector.scale(scale));
            }
        }, 0, 1000 / Environment.FPS);
    }
    
    /**
     * Adds a movement transformation to an Object over a period of time.
     *
     * @param xMovement The total x movement in radians.
     * @param yMovement The total y movement in radians.
     * @param zMovement The total z movement in radians.
     * @param period    The period over which to perform the transition in milliseconds.
     */
    @Override
    public void addMovementTransformation(double xMovement, double yMovement, double zMovement, long period) {
        inMovementTransformation.set(true);
        Timer transitionTimer = new Timer();
        transitionTimer.scheduleAtFixedRate(new TimerTask() {
            private Vector movementVector = new Vector(xMovement, yMovement, zMovement);
            
            private Vector totalMovement = new Vector(0, 0, 0);
            
            private long timeCount = 0;
            
            private long lastTime = 0;
            
            @Override
            public void run() {
                if (lastTime == 0) {
                    lastTime = System.currentTimeMillis();
                    return;
                }
                
                long currentTime = System.currentTimeMillis();
                long timeElapsed = currentTime - lastTime;
                lastTime = currentTime;
                timeCount += timeElapsed;
                
                if (timeCount >= period) {
                    move(movementVector.minus(totalMovement));
                    transitionTimer.purge();
                    transitionTimer.cancel();
                    inMovementTransformation.set(false);
                } else {
                    double scale = (double) timeElapsed / period;
                    Vector movementFrame = movementVector.scale(scale);
                    move(movementFrame);
                    totalMovement = totalMovement.plus(movementFrame);
                }
                
                double scale = (double) timeElapsed / 1000;
            }
        }, 0, 1000 / Environment.FPS);
    }
    
    /**
     * Adds a constant rotation animation to an Object.
     *
     * @param rollSpeed  The speed of the roll rotation in radians per second.
     * @param pitchSpeed The speed of the pitch rotation in radians per second.
     * @param yawSpeed   The speed of the yaw rotation in radians per second.
     */
    @Override
    public void addRotationAnimation(double rollSpeed, double pitchSpeed, double yawSpeed) {
        Timer animationTimer = new Timer();
        animationTimers.add(animationTimer);
        rotationAnimations.add(new double[]{rollSpeed, pitchSpeed, yawSpeed});
        animationTimer.scheduleAtFixedRate(new TimerTask() {
            private Vector speedVector = new Vector(rollSpeed, pitchSpeed, yawSpeed);
            
            private long lastTime = 0;
            
            @Override
            public void run() {
                if (lastTime == 0) {
                    lastTime = System.currentTimeMillis();
                    return;
                }
                
                long currentTime = System.currentTimeMillis();
                long timeElapsed = currentTime - lastTime;
                lastTime = currentTime;
                
                double scale = (double) timeElapsed / 1000;
                rotate(speedVector.scale(scale));
            }
        }, 0, 1000 / Environment.FPS);
    }
    
    /**
     * Adds a rotation transformation to an Object over a period of time.
     *
     * @param rollRotation  The total roll rotation in radians.
     * @param pitchRotation The total pitch rotation in radians.
     * @param yawRotation   The total yaw rotation in radians.
     * @param period        The period over which to perform the transition in milliseconds.
     */
    @Override
    public void addRotationTransformation(double rollRotation, double pitchRotation, double yawRotation, long period) {
        inRotationTransformation.set(true);
        Timer transitionTimer = new Timer();
        transitionTimer.scheduleAtFixedRate(new TimerTask() {
            private Vector rotationVector = new Vector(rollRotation, pitchRotation, yawRotation);
            
            private Vector totalRotation = new Vector(0, 0, 0);
            
            private long timeCount = 0;
            
            private long lastTime = 0;
            
            @Override
            public void run() {
                if (lastTime == 0) {
                    lastTime = System.currentTimeMillis();
                    return;
                }
                
                long currentTime = System.currentTimeMillis();
                long timeElapsed = currentTime - lastTime;
                lastTime = currentTime;
                timeCount += timeElapsed;
                
                if (timeCount >= period) {
                    rotateAndTransform(rotationVector.minus(totalRotation));
                    transitionTimer.purge();
                    transitionTimer.cancel();
                    inRotationTransformation.set(false);
                } else {
                    double scale = (double) timeElapsed / period;
                    Vector rotationFrame = rotationVector.scale(scale);
                    rotateAndTransform(rotationFrame);
                    totalRotation = totalRotation.plus(rotationFrame);
                }
                
                double scale = (double) timeElapsed / 1000;
            }
        }, 0, 1000 / Environment.FPS);
    }
    
    /**
     * Adds a constant color animation to an Object.
     *
     * @param period The period of the color animation in milliseconds.
     * @param offset The offset of the color animation in milliseconds.
     */
    @Override
    public void addColorAnimation(long period, long offset) {
        Timer animationTimer = new Timer();
        animationTimers.add(animationTimer);
        animationTimer.scheduleAtFixedRate(new TimerTask() {
            private long firstTime = 0;
            
            @Override
            public void run() {
                if (firstTime == 0) {
                    firstTime = System.currentTimeMillis() - offset;
                }
                
                long timeElapsed = System.currentTimeMillis() - firstTime;
                timeElapsed %= period;
                
                float hue = (float) timeElapsed / period;
                setColor(ColorUtility.getColorByHue(hue));
            }
        }, 0, 1000 / Environment.FPS);
    }
    
    /**
     * Updates the rotation matrix for the Object.
     */
    public void updateRotationMatrix() {
        rotationMatrix = RotationUtility.getRotationMatrix(getRotationRoll(), getRotationPitch(), getRotationYaw());
    }
    
    /**
     * Performs the rotation transformation on a list of Vectors.
     *
     * @param vs The list of Vectors to transform.
     */
    public void performRotationTransformation(List<Vector> vs) {
        if (rotationMatrix == null) {
            return;
        }
        
        for (int i = 0; i < vs.size(); i++) {
            vs.set(i, RotationUtility.performRotation(vs.get(i), rotationMatrix, getRootCenter()));
        }
    }
    
    /**
     * Registers a component with the Object.
     *
     * @param component The component to register.
     */
    @Override
    public void registerComponent(ObjectInterface component) {
    }
    
    /**
     * Unregisters a component with the Object.
     *
     * @param component The component to unregister.
     */
    @Override
    public void unregisterComponent(ObjectInterface component) {
    }
    
    /**
     * Adds a frame to the Object.
     *
     * @param color The color of the Frame to add.
     */
    public Frame addFrame(Color color) {
        frame = new Frame(this, color);
        return frame;
    }
    
    /**
     * Registers a Frame with the Object.
     *
     * @param frame The Frame to register.
     */
    @Override
    public void registerFrame(Frame frame) {
        this.frame = frame;
    }
    
    
    //Getters
    
    /**
     * Returns the parent of the Object.
     *
     * @return The parent of the Object.
     */
    public AbstractObject getParent() {
        return parent;
    }
    
    /**
     * Returns the center point of the Object.
     *
     * @return The center point of the Object.
     */
    public Vector getCenter() {
        return center;
    }
    
    /**
     * Returns the center of the root of the Object.
     *
     * @return The center of the root of the Object.
     */
    public Vector getRootCenter() {
        if (parent == null) {
            return getCenter();
        } else {
            return parent.getRootCenter();
        }
    }
    
    /**
     * Returns the color of the Object.
     *
     * @return The color of the Object.
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Returns the angles that define the rotation of the Object.
     *
     * @return The angles that define the rotation of the Object.
     */
    public Vector getRotation() {
        return rotation;
    }
    
    /**
     * Returns the angle that defines the roll rotation of the Object.
     *
     * @return The angle that defines the roll rotation of the Object.
     */
    public double getRotationRoll() {
        return rotation.get(0);
    }
    
    /**
     * Returns the angle that defines the pitch rotation of the Object.
     *
     * @return The angle that defines the pitch rotation of the Object.
     */
    public double getRotationPitch() {
        return rotation.get(1);
    }
    
    /**
     * Returns the angle that defines the yaw rotation of the Object.
     *
     * @return The angle that defines the yaw rotation of the Object.
     */
    public double getRotationYaw() {
        return rotation.get(2);
    }
    
    /**
     * Returns whether the Object is visible or not.
     *
     * @return Whether the Object is visible or not.
     */
    public boolean isVisible() {
        return visible;
    }
    
    /**
     * Returns the display mode of the Object.
     *
     * @return The display mode of the Object.
     */
    public BaseObject.DisplayMode getDisplayMode() {
        return displayMode;
    }
    
    /**
     * Returns whether the Object is undergoing a movement transformation or not.
     *
     * @return Whether the Object is undergoing a movement transformation or not.
     */
    public boolean inMovementTransformation() {
        return inMovementTransformation.get();
    }
    
    /**
     * Returns whether the Object is undergoing a rotation transformation or not.
     *
     * @return Whether the Object is undergoing a rotation transformation or not.
     */
    public boolean inRotationTransformation() {
        return inRotationTransformation.get();
    }
    
    
    //Setters
    
    /**
     * Sets the parent of the Object.
     *
     * @param parent The parent of the Object.
     */
    public void setParent(AbstractObject parent) {
        if (parent == null) {
            return;
        }
        
        this.parent = parent;
        this.displayMode = parent.displayMode;
        parent.registerComponent(this);
    }
    
    /**
     * Sets the center point of the Object.
     *
     * @param center The nwe center point of the Object.
     */
    @Override
    public void setCenter(Vector center) {
        this.center = center;
    }
    
    /**
     * Sets the color of the Piece.
     *
     * @param color The new color of the Object.
     */
    @Override
    public void setColor(Color color) {
        this.color = color;
    }
    
    /**
     * Sets the angles that define the rotation of the Object without updating the rotation transformation matrix.
     *
     * @param rotation The angles that define the rotation of the Object.
     */
    @Override
    public void setRotationWithoutUpdate(Vector rotation) {
        setRotationRollWithoutUpdate(rotation.getX());
        setRotationPitchWithoutUpdate(rotation.getY());
        setRotationYawWithoutUpdate(rotation.getZ());
    }
    
    /**
     * Sets the angles that define the rotation of the Object.
     *
     * @param rotation The angles that define the rotation of the Object.
     */
    @Override
    public void setRotation(Vector rotation) {
        setRotationWithoutUpdate(rotation);
        updateRotationMatrix();
    }
    
    /**
     * Sets the transformation Matrix that defines the rotation of the Object.
     *
     * @param rotationMatrix The transformation Matrix that defines the rotation of the Object.
     */
    @Override
    public void setRotationMatrix(Matrix3 rotationMatrix) {
        this.rotationMatrix = rotationMatrix;
    }
    
    /**
     * Sets the angle that defines the roll rotation of the Object without updating the rotation transformation matrix.
     *
     * @param roll The angle that defines the roll rotation of the Object.
     */
    public void setRotationRollWithoutUpdate(double roll) {
        roll %= (Math.PI * 2);
        rotation.set(0, roll);
    }
    
    /**
     * Sets the angle that defines the pitch rotation of the Object without updating the rotation transformation matrix.
     *
     * @param pitch The angle that defines the pitch rotation of the Object.
     */
    public void setRotationPitchWithoutUpdate(double pitch) {
        pitch %= (Math.PI * 2);
        rotation.set(1, pitch);
    }
    
    /**
     * Sets the angle that defines the yaw rotation of the Object without updating the rotation transformation matrix.
     *
     * @param yaw The angle that defines the yaw rotation of the Object.
     */
    public void setRotationYawWithoutUpdate(double yaw) {
        yaw %= (Math.PI * 2);
        rotation.set(2, yaw);
    }
    
    /**
     * Sets the angle that defines the roll rotation of the Object.
     *
     * @param roll The angle that defines the roll rotation of the Object.
     */
    public void setRotationRoll(double roll) {
        setRotationRollWithoutUpdate(roll);
        updateRotationMatrix();
    }
    
    /**
     * Sets the angle that defines the pitch rotation of the Object.
     *
     * @param pitch The angle that defines the pitch rotation of the Object.
     */
    public void setRotationPitch(double pitch) {
        setRotationPitchWithoutUpdate(pitch);
        updateRotationMatrix();
    }
    
    /**
     * Sets the angle that defines the yaw rotation of the Object.
     *
     * @param yaw The angle that defines the yaw rotation of the Object.
     */
    public void setRotationYaw(double yaw) {
        setRotationYawWithoutUpdate(yaw);
        updateRotationMatrix();
    }
    
    /**
     * Sets the visibility of the Object.
     *
     * @param visible The new visibility of the Object.
     */
    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    /**
     * Sets the display mode of the Object.
     *
     * @param displayMode The new display mode of the Object.
     */
    @Override
    public void setDisplayMode(BaseObject.DisplayMode displayMode) {
        this.displayMode = displayMode;
    }
    
    /**
     * Sets whether to clip Vectors or not.
     *
     * @param clippingEnabled The new clipping mode of the Object.
     */
    @Override
    public void setClippingEnabled(boolean clippingEnabled) {
        this.clippingEnabled = clippingEnabled;
    }
    
    /**
     * Sets the color of the Frame of the Object.
     *
     * @param color The new color of the Frame of the Object.
     */
    public void setFrameColor(Color color) {
        if (frame != null) {
            frame.setColor(color);
        }
    }
    
}
