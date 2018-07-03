/*
 * File:    RotationGroup.java
 * Package: objects.base.group
 * Author:  Zachary Gill
 */

package objects.base.group;

import math.vector.Vector;
import objects.base.AbstractObject;
import objects.base.BaseObject;
import objects.base.Object;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Defines a rotation for a group of Objects.
 */
public class RotationGroup extends Object
{
    
    //Fields
    
    /**
     * The center point of the RotationGroup.
     */
    protected AbstractObject locus;
    
    
    //Constructors
    
    /**
     * The constructor for an RotationGroup.
     *
     * @param parent  The parent of the RotationGroup.
     * @param locus   The locus of the RotationGroup.
     * @param objects The list of objects of the RotationGroup.
     */
    public RotationGroup(AbstractObject parent, Object locus, List<Object> objects)
    {
        super(parent, new Vector(0, 0, 0), Color.BLACK);
    
        this.locus = locus;
        this.components.addAll(objects);
        this.visible = false;
    }
    
    /**
     * The constructor for an RotationGroup.
     *
     * @param locus   The locus of the RotationGroup.
     * @param objects The list of objects of the RotationGroup.
     */
    public RotationGroup(Object locus, List<Object> objects)
    {
        super(new Vector(0, 0, 0), Color.BLACK);
    
        this.locus = locus;
        this.components.addAll(objects);
        this.visible = false;
    }
    
    /**
     * The constructor for an RotationGroup.
     *
     * @param locus The center of the RotationGroup.
     */
    public RotationGroup(AbstractObject locus)
    {
        super(new Vector(0, 0, 0), Color.BLACK);
        
        this.locus = locus;
        this.visible = false;
    }
    
    
    //Methods
    
    /**
     * Prepares the Object to be rendered.
     *
     * @return The list of BaseObjects that were prepared.
     */
    @Override
    public List<BaseObject> prepare()
    {
        return new ArrayList<>();
    }
    
    /**
     * Renders the Object on the screen.
     *
     * @param g2 The 2D Graphics entity.
     */
    @Override
    public void render(Graphics2D g2)
    {
    }
    
    /**
     * Adds a rotation transformation to a group over a period of time.
     *
     * @param yawRotation   The total yaw rotation in radians.
     * @param pitchRotation The total pitch rotation in radians.
     * @param rollRotation  The total roll rotation in radians.
     * @param period        The period over which to perform the transition in milliseconds.
     */
    public void rotateGroup(double yawRotation, double pitchRotation, double rollRotation, long period)
    {
        addRotationTransformation(yawRotation, pitchRotation, rollRotation, period);
        
        RotationGroup thisGroup = this;
        Timer completionTimer = new Timer();
        TimerTask checkRotationComplete = new TimerTask()
        {
            @Override
            public void run()
            {
                if (!inRotationTransformation()) {
                    if (parent != null) {
                        parent.unregisterComponent(thisGroup);
                    }
                    completionTimer.purge();
                    completionTimer.cancel();
                }
            }
        };
        completionTimer.scheduleAtFixedRate(checkRotationComplete, 0, 10);
    }
    
    /**
     * Rotates the Object in a certain direction and saves the rotation in its vector state.
     *
     * @param offset The relative offsets to rotate the Object.
     */
    @Override
    public void rotateAndTransform(Vector offset)
    {
        rotateAndTransform(offset, getCenter());
    }
    
    
    //Getters
    
    /**
     * Returns the center point of the Object.
     *
     * @return The center point of the Object.
     */
    @Override
    public Vector getCenter()
    {
        return locus.getCenter();
    }
    
}
