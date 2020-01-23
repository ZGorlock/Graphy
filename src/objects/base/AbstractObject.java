/*
 * File:    AbstractObject.java
 * Package: objects.base
 * Author:  Zachary Gill
 */

package objects.base;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import camera.Camera;
import main.Environment;
import math.matrix.Matrix3;
import math.vector.Vector;
import math.vector.Vector3;
import utility.ColorUtility;
import utility.RotationUtility;
import utility.SphericalCoordinateUtility;

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
     * The array of Vertices that define the Object.
     */
    protected Vector[] vertices = new Vector[0];
    
    /**
     * A list of the Vectors of the Object that have been prepared for rendering.
     */
    protected final List<Vector> prepared = new ArrayList<>();
    
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
     * A flag indicating whether or not the Object is rendered.
     */
    protected AtomicBoolean rendered = new AtomicBoolean(true);
    
    /**
     * The distance from the Camera to the Object.
     */
    protected double renderDistance = 0.0;
    
    /**
     * The display mode of the Object.
     */
    protected DisplayMode displayMode = DisplayMode.FACE;
    
    /**
     * The clipping mode of the Object.
     */
    protected boolean clippingEnabled = false;
    
    /**
     * The render delay in frames for the Object.
     */
    protected AtomicInteger renderDelay = new AtomicInteger(0);
    
    /**
     * The animations timers of the Object.
     */
    public final List<Timer> animationTimers = new ArrayList<>();
    
    /**
     * The set of movement animations for the Object.
     */
    public final List<double[]> movementAnimations = new ArrayList<>();
    
    /**
     * The set of rotation animations for the Object.
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
    
    /**
     * A flag indicating whether the Object is currently undergoing an orbit transformation or not.
     */
    public final AtomicBoolean inOrbitTransformation = new AtomicBoolean(false);
    
    
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
     * Performs pre-preparing steps on the Object.
     *
     * @return Whether or not the Object should continue preparing.
     */
    @Override
    public final boolean prePrepare() {
        if (!Environment.ENABLE_RENDER_BUFFERING || renderDelay.decrementAndGet() <= 0) {
            if (!visible) {
                renderDelay.set(Environment.ENABLE_RENDER_BUFFERING ? ((int) (Math.random() * (Environment.fps / 8))) : 1);
                return false;
            }
            return true;
        }
        return false;
    }
    
    /**
     * Prepares the Object to be rendered.
     *
     * @return The list of BaseObjects that were prepared.
     */
    @Override
    public abstract List<BaseObject> prepare();
    
    /**
     * Performs post-preparing steps on the Object.
     *
     * @return Whether or not the Object should continue to rendering.
     */
    @Override
    public final boolean postPrepare() {
        if (calculateRenderDistance() > Environment.MAX_RENDER_DISTANCE) {
            renderDelay.set(Environment.ENABLE_RENDER_BUFFERING ? ((int) (Math.random() * (Environment.fps / 8))) : 1);
            return !Environment.ENABLE_RENDER_BUFFERING;
        }
        return true;
    }
    
    /**
     * Performs the preparation for the Object to be rendered.
     *
     * @return The list of BaseObjects that were prepared.
     */
    @Override
    public final List<BaseObject> doPrepare() {
        if (!prePrepare()) {
            rendered.set(false);
            return new ArrayList<>();
        }
        
        List<BaseObject> preparedBases = prepare();
        
        if (!postPrepare()) {
            rendered.set(false);
            return new ArrayList<>();
        }
        return preparedBases;
    }
    
    /**
     * Performs pre-rendering steps on the Object.
     *
     * @return Whether or not the Object should continue rendering.
     */
    @Override
    public final boolean preRender() {
        if (!Environment.ENABLE_RENDER_BUFFERING || renderDelay.get() <= 0) {
            if (!visible || (prepared.size() != vertices.length) || Camera.hasVectorBehindScreen(vertices)) {
                renderDelay.set(Environment.ENABLE_RENDER_BUFFERING ? ((int) (Math.random() * (Environment.fps / 8))) : 1);
                return false;
            }
            
            Camera.projectVectorsToCamera(prepared);
            Camera.collapseVectorsToViewport(prepared);
            if (!Camera.hasVectorInView(prepared)) {
                renderDelay.set(Environment.ENABLE_RENDER_BUFFERING ? ((int) (Math.random() * (Environment.fps / 8))) : 1);
                return false;
            } else {
                renderDelay.set(1);
                Camera.scaleVectorsToScreen(prepared);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Renders the Object on the screen.
     *
     * @param g2 The 2D Graphics entity.
     */
    @Override
    public abstract void render(Graphics2D g2);
    
    /**
     * Performs post-rendering steps on the Object.
     *
     * @param g2 The 2D Graphics entity.
     */
    @Override
    public final void postRender(Graphics2D g2) {
        renderFrame(g2);
    }
    
    /**
     * Draws the frame for the Object.
     *
     * @param g2 The 2D Graphics entity.
     */
    @Override
    public void renderFrame(Graphics2D g2) {
        if (frame == null) {
            return;
        }
        
        frame.render(g2, prepared);
    }
    
    /**
     * Performs the rendering for the Object on the screen.
     *
     * @param g2 The 2D Graphics entity.
     */
    @Override
    public final void doRender(Graphics2D g2) {
        if (!preRender()) {
            rendered.set(false);
            return;
        }
        
        render(g2);
        
        postRender(g2);
        rendered.set(true);
    }
    
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
     * Calculates the distance from the Camera to the Object.
     *
     * @return The distance from the Camera to the Object.
     */
    @Override
    public abstract double calculateRenderDistance();
    
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
        movementAnimations.add(new double[] {xSpeed, ySpeed, zSpeed});
        animationTimer.scheduleAtFixedRate(new TimerTask() {
            
            //Fields
            
            /**
             * The movement speed vector.
             */
            private Vector speedVector = new Vector(xSpeed, ySpeed, zSpeed);
            
            /**
             * The last time that the animation ran.
             */
            private long lastTime = 0;
            
            
            //Methods
            
            /**
             * Performs the movement animation.
             */
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
        }, 0, 1000 / Environment.fps);
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
        Timer transformationTimer = new Timer();
        transformationTimer.scheduleAtFixedRate(new TimerTask() {
            
            //Fields
            
            /**
             * The movement vector.
             */
            private Vector movementVector = new Vector(xMovement, yMovement, zMovement);
            
            /**
             * The total movement so far.
             */
            private Vector totalMovement = new Vector(0, 0, 0);
            
            /**
             * The total amount of time elapsed.
             */
            private long timeCount = 0;
            
            /**
             * The last time the transformation ran.
             */
            private long lastTime = 0;
            
            
            //Methods
            
            /**
             * Performs the movement transformation.
             */
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
                    transformationTimer.purge();
                    transformationTimer.cancel();
                    inMovementTransformation.set(false);
                } else {
                    double scale = (double) timeElapsed / period;
                    Vector movementFrame = movementVector.scale(scale);
                    move(movementFrame);
                    totalMovement = totalMovement.plus(movementFrame);
                }
            }
        }, 0, 1000 / Environment.fps);
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
        animationTimer.scheduleAtFixedRate(new TimerTask() {
            
            //Fields
            
            /**
             * The rotation speed vector.
             */
            private Vector speedVector = new Vector(rollSpeed, pitchSpeed, yawSpeed);
            
            /**
             * The last time the animation ran.
             */
            private long lastTime = 0;
            
            
            //Methods
            
            /**
             * Performs the rotation animation.
             */
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
        }, 0, 1000 / Environment.fps);
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
        Timer transformationTimer = new Timer();
        transformationTimer.scheduleAtFixedRate(new TimerTask() {
            
            //Fields
            
            /**
             * The rotation vector.
             */
            private Vector rotationVector = new Vector(rollRotation, pitchRotation, yawRotation);
            
            /**
             * The total rotation so far.
             */
            private Vector totalRotation = new Vector(0, 0, 0);
            
            /**
             * The total time elapsed.
             */
            private long timeCount = 0;
            
            /**
             * The last time the transformation ran.
             */
            private long lastTime = 0;
            
            
            //Methods
            
            /**
             * Performs the rotation transformation.
             */
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
                    transformationTimer.purge();
                    transformationTimer.cancel();
                    inRotationTransformation.set(false);
                } else {
                    double scale = (double) timeElapsed / period;
                    Vector rotationFrame = rotationVector.scale(scale);
                    rotateAndTransform(rotationFrame);
                    totalRotation = totalRotation.plus(rotationFrame);
                }
            }
        }, 0, 1000 / Environment.fps);
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
            
            //Fields
            
            /**
             * The first time the animation ran.
             */
            private long firstTime = 0;
            
            
            //Methods
            
            /**
             * Performs the color animation.
             */
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
        }, 0, 1000 / Environment.fps);
    }
    
    /**
     * Adds a constant orbit animation to and Object.
     *
     * @param point       The point to orbit around.
     * @param orbitPeriod The period of the orbit in milliseconds.
     */
    public void addOrbitAnimation(Vector point, double orbitPeriod) {
        addOrbitAnimation(new Object(point, Color.BLACK), orbitPeriod);
    }
    
    /**
     * Adds a constant orbit animation to an Object.
     *
     * @param object      The Object to orbit around.
     * @param orbitPeriod The period of the orbit in milliseconds.
     */
    public void addOrbitAnimation(Object object, double orbitPeriod) {
        addOrbitAnimation(object, orbitPeriod, true);
    }
    
    /**
     * Adds a constant orbit animation to an Object.
     *
     * @param object      The Object to orbit around.
     * @param orbitPeriod The period of the orbit in milliseconds.
     * @param clockwise   Whether the orbit around the Object should be clockwise or counterclockwise.
     */
    public void addOrbitAnimation(Object object, double orbitPeriod, boolean clockwise) {
        Timer animationTimer = new Timer();
        animationTimers.add(animationTimer);
        AbstractObject o = this;
        animationTimer.scheduleAtFixedRate(new TimerTask() {
            
            //Fields
            
            /**
             * The last object center for tracking relative movement.
             */
            private Vector lastObjectCenter = object.center.clone();
            
            /**
             * The normal vector of the plane of motion around the object.
             */
            private Vector normal = Environment.ORIGIN;
            
            /**
             * The direction of motion.
             */
            private int wise = -1;
            
            /**
             * The original distance from the object.
             */
            private double originalRho = 0;
            
            /**
             * The length of the path of motion around the object.
             */
            private double circumference = 0;
            
            /**
             * The last time the animation ran.
             */
            private long lastTime = 0;
            
            
            //Methods
            
            /**
             * Performs the orbit animation.
             */
            @Override
            public void run() {
                Vector currentObjectCenter = object.center.clone();
                Vector objectMovement = currentObjectCenter.minus(lastObjectCenter);
                lastObjectCenter = currentObjectCenter;
                
                if (lastTime == 0) {
                    Vector sphericalLocation = SphericalCoordinateUtility.cartesianToSpherical(center.minus(lastObjectCenter));
                    Vector direction = center.minus(lastObjectCenter).normalize();
                    Vector perpendicular = SphericalCoordinateUtility.sphericalToCartesian(Math.PI / 2, sphericalLocation.getY() + (Math.PI / 2), sphericalLocation.getZ()).minus(lastObjectCenter).normalize();
                    
                    normal = new Vector3(direction).cross(perpendicular).normalize();
                    wise = (clockwise ? 1 : -1) * (((direction.getX() == 0) && (direction.getY() == 0) && (direction.getZ() > 0)) ? 1 : -1);
                    originalRho = sphericalLocation.getZ();
                    circumference = (Math.PI * 2 * sphericalLocation.getZ());
                    
                    lastTime = System.currentTimeMillis();
                    return;
                }
                
                long currentTime = System.currentTimeMillis();
                long timeElapsed = currentTime - lastTime;
                lastTime = currentTime;
                
                Vector gravity = lastObjectCenter.minus(center).normalize();
                Vector movement = new Vector3(gravity).cross(normal).normalize().scale(wise);
                
                double scale = ((double) timeElapsed / orbitPeriod) * circumference;
                
                Vector translation = movement.scale(scale).plus(objectMovement);
                Vector newLocation = center.plus(translation);
                Vector sphericalLocation = SphericalCoordinateUtility.cartesianToSpherical(newLocation.minus(lastObjectCenter));
                Vector adjustedLocation = SphericalCoordinateUtility.sphericalToCartesian(sphericalLocation.getX(), sphericalLocation.getY(), originalRho).plus(lastObjectCenter);
                Vector adjustment = adjustedLocation.minus(newLocation);
                
                move(translation.plus(adjustment));
            }
        }, 0, 1000 / Environment.fps);
    }
    
    /**
     * Adds an orbit transformation to an Object over a period of time.
     *
     * @param point  The point to orbit around.
     * @param orbits The number of orbits to perform during the transformation.
     * @param period The period over which to perform the transition in milliseconds.
     */
    public void addOrbitTransformation(Vector point, double orbits, double period) {
        addOrbitTransformation(new Object(point, Color.BLACK), orbits, period);
    }
    
    /**
     * Adds an orbit transformation to an Object over a period of time.
     *
     * @param object The Object to orbit around.
     * @param orbits The number of orbits to perform during the transformation.
     * @param period The period of the orbit in milliseconds.
     */
    public void addOrbitTransformation(Object object, double orbits, double period) {
        addOrbitTransformation(object, orbits, period, true);
    }
    
    /**
     * Adds an orbit transformation to an Object over a period of time.
     *
     * @param object    The Object to orbit around.
     * @param orbits    The number of orbits to perform during the transformation.
     * @param period    The period of the orbit in milliseconds.
     * @param clockwise Whether the orbit around the Object should be clockwise or counterclockwise.
     */
    public void addOrbitTransformation(Object object, double orbits, double period, boolean clockwise) {
        inOrbitTransformation.set(true);
        double orbitPeriod = period / orbits;
        Timer transformationTimer = new Timer();
        transformationTimer.scheduleAtFixedRate(new TimerTask() {
            
            //Fields
            
            /**
             * The last object center for tracking relative movement.
             */
            private Vector lastObjectCenter = object.center.clone();
            
            /**
             * The normal vector of the plane of motion around the object.
             */
            private Vector normal = Environment.ORIGIN;
            
            /**
             * The direction of motion.
             */
            private int wise = -1;
            
            /**
             * The original distance from the object.
             */
            private double originalRho = 0;
            
            /**
             * The length of the path of motion around the object.
             */
            private double circumference = 0;
            
            /**
             * The total time elapsed.
             */
            private long timeCount = 0;
            
            /**
             * The last time the animation ran.
             */
            private long lastTime = 0;
            
            
            //Methods
            
            /**
             * Performs the orbit transformation.
             */
            @Override
            public void run() {
                Vector currentObjectCenter = object.center.clone();
                Vector objectMovement = currentObjectCenter.minus(lastObjectCenter);
                lastObjectCenter = currentObjectCenter;
                
                if (lastTime == 0) {
                    Vector sphericalLocation = SphericalCoordinateUtility.cartesianToSpherical(center.minus(lastObjectCenter));
                    Vector direction = center.minus(lastObjectCenter).normalize();
                    Vector perpendicular = SphericalCoordinateUtility.sphericalToCartesian(Math.PI / 2, sphericalLocation.getY() + (Math.PI / 2), sphericalLocation.getZ()).minus(lastObjectCenter).normalize();
                    
                    normal = new Vector3(direction).cross(perpendicular).normalize();
                    wise = (clockwise ? 1 : -1) * (((direction.getX() == 0) && (direction.getY() == 0) && (direction.getZ() > 0)) ? 1 : -1);
                    originalRho = sphericalLocation.getZ();
                    circumference = (Math.PI * 2 * sphericalLocation.getZ());
                    
                    lastTime = System.currentTimeMillis();
                    return;
                }
                
                long currentTime = System.currentTimeMillis();
                long timeElapsed = currentTime - lastTime;
                lastTime = currentTime;
                timeCount += timeElapsed;
                
                if (timeCount > period) {
                    timeElapsed -= (timeCount - period);
                }
                
                Vector gravity = lastObjectCenter.minus(center).normalize();
                Vector movement = new Vector3(gravity).cross(normal).normalize().scale(wise);
                
                double scale = ((double) timeElapsed / orbitPeriod) * circumference;
                
                Vector translation = movement.scale(scale).plus(objectMovement);
                Vector newLocation = center.plus(translation);
                Vector sphericalLocation = SphericalCoordinateUtility.cartesianToSpherical(newLocation.minus(lastObjectCenter));
                Vector adjustedLocation = SphericalCoordinateUtility.sphericalToCartesian(sphericalLocation.getX(), sphericalLocation.getY(), originalRho).plus(lastObjectCenter);
                Vector adjustment = adjustedLocation.minus(newLocation);
                
                move(translation.plus(adjustment));
                
                if (timeCount >= period) {
                    transformationTimer.purge();
                    transformationTimer.cancel();
                    inOrbitTransformation.set(false);
                }
            }
        }, 0, 1000 / Environment.fps);
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
     * Returns the list of Vertices that define the Object.
     *
     * @return The list of Vertices that define the Object.
     */
    public Vector[] getVertices() {
        return vertices;
    }
    
    /**
     * Returns the list of the Vectors of the Object that have been prepared for rendering.
     *
     * @return The list of the Vectors of the Object that have been prepared for rendering.
     */
    public List<Vector> getPrepared() {
        return prepared;
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
     * Returns whether the Object is rendered or not.
     *
     * @return Whether the Object is rendered or not.
     */
    public boolean isRendered() {
        return rendered.get();
    }
    
    /**
     * Returns the distance from the Camera to the Object.
     *
     * @return The distance from the Camera to the Object.
     */
    public double getRenderDistance() {
        return renderDistance;
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
    
    /**
     * Returns whether the Object is undergoing an orbit transformation or not.
     *
     * @return Whether the Object is undergoing an orbit transformation or not.
     */
    public boolean inOrbitTransformation() {
        return inOrbitTransformation.get();
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
