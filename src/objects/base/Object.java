/*
 * File:    Object.java
 * Package: objects.base
 * Author:  Zachary Gill
 */

package objects.base;

import math.vector.Vector;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Defines the base properties of an Object.
 */
public class Object extends AbstractObject
{
    
    //Fields
    
    /**
     * The list of Objects that compose the Object.
     */
    protected List<ObjectInterface> components = new ArrayList<>();
    
    
    //Constructors
    
    /**
     * The constructor for an Object.
     *
     * @param center The center of the Object.
     * @param color  The color of the Object.
     */
    public Object(Vector center, Color color)
    {
        this.center = center;
        this.color = color;
    }
    
    /**
     * The constructor for an Object.
     *
     * @param color The color of the Object.
     */
    public Object(Color color)
    {
        this.color = color;
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Object.
     */
    protected void calculate()
    {
    }
    
    /**
     * Prepares the Object to be rendered.
     *
     * @return The list of BaseObjects that were prepared.
     */
    @Override
    public List<BaseObject> prepare()
    {
        List<BaseObject> preparedBases = new ArrayList<>();
        
        for (ObjectInterface component : components) {
            preparedBases.addAll(component.prepare());
        }
        
        return preparedBases;
    }
    
    /**
     * Renders the Object on the screen.
     *
     * @param g2 The 2D Graphics entity.
     */
    @Override
    public void render(Graphics2D g2)
    {
        if (!visible) {
            return;
        }
        
        for (ObjectInterface component : components) {
            component.render(g2);
        }
    }
    
    /**
     * Moves the Object in a certain direction.
     *
     * @param offset The relative offsets to move the Object.
     */
    @Override
    public void move(Vector offset)
    {
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
    public void reposition(Vector center)
    {
        if (this.center.equals(center)) {
            return;
        }
        
        this.center = center;
        calculate();
    }
    
    /**
     * Hides the Object from being rendered.
     */
    public void hide()
    {
        setVisible(false);
        for (ObjectInterface component : components) {
            if (component instanceof BaseObject) {
                component.setVisible(false);
            } else if (component instanceof Object) {
                ((Object) component).hide();
                component.setVisible(false);
            } else {
                //TODO
            }
        }
    }
    
    /**
     * Shows the Object to be rendered.
     */
    public void show()
    {
        setVisible(true);
        for (ObjectInterface component : components) {
            if (component instanceof BaseObject) {
                component.setVisible(true);
            } else if (component instanceof Object) {
                ((Object) component).show();
                component.setVisible(true);
            } else {
                //TODO
            }
        }
    }
    
    /**
     * Updates the rotation matrix for the Object.
     */
    @Override
    public void updateRotationMatrix()
    {
        super.updateRotationMatrix();
        for (ObjectInterface component : components) {
            component.setRotationWithoutUpdate(rotation);
            component.setRotationMatrix(rotationMatrix);
        }
    }
    
    /**
     * Adds a frame to the Object.
     *
     * @param color The color of the Frame to add.
     */
    public Frame addFrame(Color color)
    {
        frame = new Frame(this, color);
        return frame;
    }
    
    /**
     * Registers a component with the Object.
     *
     * @param component The component to register.
     */
    @Override
    public void registerComponent(ObjectInterface component)
    {
        if (!components.contains(component)) {
            this.components.add(component);
        }
    }
    
    /**
     * Registers a Frame with the Object.
     *
     * @param frame The Frame to register.
     */
    @Override
    public void registerFrame(Frame frame)
    {
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
    public void addProcess(Runnable process, long delay)
    {
        Timer processTimer = new Timer();
        processTimer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                process.run();
            }
        }, delay, delay);
    }
    
    
    //Getters
    
    /**
     * Returns the list of Objects that define the Object.
     *
     * @return The list of Objects that define the Object.
     */
    public List<ObjectInterface> getComponents()
    {
        return components;
    }
    
    /**
     * Returns the list of Base Objects that define the Object.
     *
     * @return The list of Base Objects that define the Object.
     */
    public List<BaseObject> getBaseComponents()
    {
        List<BaseObject> baseComponents = new ArrayList<>();
        for (ObjectInterface component : components) {
            if (component instanceof BaseObject) {
                baseComponents.add((BaseObject) component);
            } else if (component instanceof  Object){
                baseComponents.addAll(((Object) component).getBaseComponents());
            } else {
                //TODO
            }
        }
        return baseComponents;
    }
    
    /**
     * Returns the list of Vectors that define the Object.
     *
     * @return The list of Vectors that define the Object.
     */
    public List<Vector> getVectors()
    {
        List<Vector> vectors = new ArrayList<>();
        for (ObjectInterface component : components) {
            if (component instanceof BaseObject) {
                vectors.addAll(Arrays.asList(((BaseObject) component).vertices));
            } else if (component instanceof  Object){
                vectors.addAll(((Object) component).getVectors());
            } else {
                //TODO
            }
        }
        return vectors;
    }
    
    
    //Setters
    
    /**
     * Sets the center point of the Object.
     *
     * @param center The nwe center point of the Object.
     */
    @Override
    public void setCenter(Vector center)
    {
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
    public void setColor(Color color)
    {
        super.setColor(color);
        
        for (ObjectInterface component : components) {
            component.setColor(color);
        }
    }
    
    /**
     * Sets the visibility of the Object.
     *
     * @param visible The new visibility of the Object.
     */
    @Override
    public void setVisible(boolean visible)
    {
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
    public void setDisplayMode(BaseObject.DisplayMode displayMode)
    {
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
    public void setClippingEnabled(boolean clippingEnabled)
    {
        super.setClippingEnabled(clippingEnabled);
        
        for (ObjectInterface component : components) {
            component.setClippingEnabled(clippingEnabled);
        }
    }
    
}
