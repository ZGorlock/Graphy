/*
 * File:    Object.java
 * Package: objects.base
 * Author:  Zachary Gill
 */

package objects.base;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import main.Environment;
import math.matrix.Matrix3;
import math.vector.Vector;
import utility.RotationUtility;

/**
 * Defines the base properties of an Object.
 */
public class Object extends AbstractObject {
    
    //Fields
    
    /**
     * The list of Objects that compose the Object.
     */
    protected final List<ObjectInterface> components = new ArrayList<>();
    
    
    //Constructors
    
    /**
     * The constructor for an Object.
     *
     * @param parent The parent of the Object.
     * @param center The center of the Object.
     * @param color  The color of the Object.
     */
    public Object(AbstractObject parent, Vector center, Color color) {
        this.center = center;
        this.color = color;
        setParent(parent);
    }
    
    /**
     * The constructor for an Object.
     *
     * @param center The center of the Object.
     * @param color  The color of the Object.
     */
    public Object(Vector center, Color color) {
        this(null, center, color);
    }
    
    /**
     * The constructor for an Object.
     *
     * @param color The color of the Object.
     */
    public Object(Color color) {
        this(Environment.ORIGIN, color);
    }
    
    /**
     * The constructor for an Object.
     */
    public Object() {
        this(Color.BLACK);
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Object.
     */
    protected void calculate() {
    }
    
    /**
     * Prepares the Object to be rendered.
     *
     * @param perspective The perspective to prepare the Object for.
     * @return The list of BaseObjects that were prepared.
     */
    @Override
    public List<BaseObject> prepare(UUID perspective) {
        List<BaseObject> preparedBases = new ArrayList<>();
        
        for (ObjectInterface component : components) {
            preparedBases.addAll(component.doPrepare(perspective));
        }
        
        return preparedBases;
    }
    
    /**
     * Renders the Object on the screen.
     *
     * @param perspective The perspective to render the Object for.
     * @param g2          The 2D Graphics entity.
     */
    @Override
    public void render(Graphics2D g2, UUID perspective) {
        for (ObjectInterface component : components) {
            component.doRender(g2, perspective);
        }
    }
    
    /**
     * Moves the Object in a certain direction.
     *
     * @param offset The relative offsets to move the Object.
     */
    @Override
    public synchronized void move(Vector offset) {
        super.move(offset);
        
        for (ObjectInterface component : components) {
            component.move(offset);
        }
    }
    
    /**
     * Repositions the object at a new center point.
     *
     * @param center The new center of the Object.
     */
    public synchronized void reposition(Vector center) {
        if (this.center.equals(center)) {
            return;
        }
        
        this.center = center;
        calculate();
    }
    
    /**
     * Rotates the Object in a certain direction and saves the rotation in its vector state.
     *
     * @param offset The relative offsets to rotate the Object.
     */
    @Override
    public synchronized void rotateAndTransform(Vector offset) {
        rotateAndTransform(offset, getRootCenter());
    }
    
    /**
     * Rotates the Object in a certain direction and saves the rotation in its vector state.
     *
     * @param offset The relative offsets to rotate the Object.
     * @param center The center to rotate the Object about.
     */
    @Override
    public synchronized void rotateAndTransform(Vector offset, Vector center) {
        for (ObjectInterface component : components) {
            component.rotateAndTransform(offset, center);
        }
        
        Matrix3 rotationTransformationMatrix = RotationUtility.getRotationMatrix(offset.getX(), offset.getY(), offset.getZ());
        this.center = RotationUtility.performRotation(this.center, rotationTransformationMatrix, center.justify());
    }
    
    /**
     * Calculates the distance from the Camera to the Object.
     *
     * @return The distance from the Camera to the Object.
     */
    @Override
    public double calculateRenderDistance(UUID perspective) {
        renderDistance = 0;
        return renderDistance;
    }
    
    /**
     * Hides the Object from being rendered.
     */
    public void hide() {
        setVisible(false);
        for (ObjectInterface component : components) {
            if (component instanceof Object) {
                ((Object) component).hide();
            } else {
                component.setVisible(false);
            }
        }
    }
    
    /**
     * Shows the Object to be rendered.
     */
    public void show() {
        setVisible(true);
        for (ObjectInterface component : components) {
            if (component instanceof Object) {
                ((Object) component).show();
            } else {
                component.setVisible(true);
            }
        }
    }
    
    /**
     * Updates the rotation matrix for the Object.
     */
    @Override
    public synchronized void updateRotationMatrix() {
        super.updateRotationMatrix();
        
        for (ObjectInterface component : components) {
            component.setRotationWithoutUpdate(rotation);
            component.setRotationMatrix(rotationMatrix);
        }
    }
    
    /**
     * Registers a component with the Object.
     *
     * @param component The component to register.
     */
    @Override
    public void registerComponent(ObjectInterface component) {
        if (!components.contains(component)) {
            this.components.add(component);
        }
    }
    
    /**
     * Unregisters a component with the Object.
     *
     * @param component The component to unregister.
     */
    @Override
    public void unregisterComponent(ObjectInterface component) {
        if (components.contains(component)) {
            this.components.remove(component);
        }
    }
    
    /**
     * Registers a Frame with the Object.
     *
     * @param frame The Frame to register.
     */
    @Override
    public void registerFrame(Frame frame) {
        super.registerFrame(frame);
        
        for (ObjectInterface component : components) {
            component.registerFrame(frame);
        }
    }
    
    /**
     * Adds a process to the Object that runs periodically.
     *
     * @param process The process to execute.
     * @param delay   The delay between executions.
     */
    public void addProcess(Runnable process, long delay) {
        Timer processTimer = new Timer();
        processTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                process.run();
            }
        }, delay, delay);
    }
    
    /**
     * Returns whether the Object is undergoing a movement transformation or not.
     *
     * @return Whether the Object is undergoing a movement transformation or not.
     */
    @Override
    public boolean inMovementTransformation() {
        if (super.inMovementTransformation.get()) {
            return true;
        }
        
        try {
            for (ObjectInterface component : components) {
                if (component.inMovementTransformation()) {
                    return true;
                }
            }
        } catch (ConcurrentModificationException e) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Returns whether the Object is undergoing a rotation transformation or not.
     *
     * @return Whether the Object is undergoing a rotation transformation or not.
     */
    @Override
    public boolean inRotationTransformation() {
        if (super.inRotationTransformation.get()) {
            return true;
        }
        
        try {
            for (ObjectInterface component : components) {
                if (component.inRotationTransformation()) {
                    return true;
                }
            }
        } catch (ConcurrentModificationException e) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Returns whether the Object is undergoing an orbit transformation or not.
     *
     * @return Whether the Object is undergoing an orbit transformation or not.
     */
    @Override
    public boolean inOrbitTransformation() {
        if (super.inOrbitTransformation.get()) {
            return true;
        }
        
        try {
            for (ObjectInterface component : components) {
                if (component.inOrbitTransformation()) {
                    return true;
                }
            }
        } catch (ConcurrentModificationException e) {
            return true;
        }
        
        return false;
    }
    
    
    //Getters
    
    /**
     * Returns the list of Objects that define the Object.
     *
     * @return The list of Objects that define the Object.
     */
    public List<ObjectInterface> getComponents() {
        return components;
    }
    
    /**
     * Returns the list of Base Objects that define the Object.
     *
     * @return The list of Base Objects that define the Object.
     */
    public List<BaseObject> getBaseComponents() {
        List<BaseObject> baseComponents = new ArrayList<>();
        for (ObjectInterface component : components) {
            if (component instanceof Object) {
                baseComponents.addAll(((Object) component).getBaseComponents());
            } else if (component instanceof BaseObject) {
                baseComponents.add((BaseObject) component);
            }
        }
        return baseComponents;
    }
    
    /**
     * Returns the list of Vectors that define the Object.
     *
     * @return The list of Vectors that define the Object.
     */
    public List<Vector> getVectors() {
        List<Vector> vectors = new ArrayList<>();
        for (ObjectInterface component : components) {
            if (component instanceof Object) {
                vectors.addAll(((Object) component).getVectors());
            } else if (component instanceof BaseObject) {
                vectors.addAll(Arrays.asList(((BaseObject) component).vertices));
            }
        }
        return vectors;
    }
    
    
    //Setters
    
    /**
     * Sets the center point of the Object.
     *
     * @param center The new center point of the Object.
     */
    @Override
    public void setCenter(Vector center) {
        super.setCenter(center);
        
        for (ObjectInterface component : components) {
            component.setCenter(center);
        }
    }
    
    /**
     * Sets the color of the Object.
     *
     * @param color The new color of the Object.
     */
    @Override
    public void setColor(Color color) {
        super.setColor(color);
        
        for (ObjectInterface component : components) {
            component.setColor(color);
        }
    }
    
    /**
     * Sets the angles that define the rotation of the Object.
     *
     * @param rotation The angles that define the rotation of the Object.
     */
    public synchronized void setRotation(Vector rotation) {
        super.setRotation(rotation);
        
        for (ObjectInterface component : components) {
            component.setRotation(rotation);
        }
    }
    
    /**
     * Sets the visibility of the Object.
     *
     * @param visible The new visibility of the Object.
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        
        for (ObjectInterface component : components) {
            component.setVisible(visible);
        }
    }
    
    /**
     * Sets the display mode of the Object.
     *
     * @param displayMode The new display mode of the Object.
     */
    @Override
    public void setDisplayMode(BaseObject.DisplayMode displayMode) {
        super.setDisplayMode(displayMode);
        
        for (ObjectInterface component : components) {
            component.setDisplayMode(displayMode);
        }
    }
    
    /**
     * Sets whether to clip Vectors or not.
     *
     * @param clippingEnabled The new clipping mode of the Object.
     */
    @Override
    public void setClippingEnabled(boolean clippingEnabled) {
        super.setClippingEnabled(clippingEnabled);
        
        for (ObjectInterface component : components) {
            component.setClippingEnabled(clippingEnabled);
        }
    }
    
}
