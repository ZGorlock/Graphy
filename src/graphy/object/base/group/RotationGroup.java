/*
 * File:    RotationGroup.java
 * Package: graphy.object.base.group
 * Author:  Zachary Gill
 */

package graphy.object.base.group;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import graphy.math.vector.Vector;
import graphy.object.base.AbstractObject;
import graphy.object.base.BaseObject;
import graphy.object.base.Object;

/**
 * Defines a rotation for a group of Objects.
 */
public class RotationGroup extends Object {
    
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
    public RotationGroup(AbstractObject parent, Object locus, List<Object> objects) {
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
    public RotationGroup(Object locus, List<Object> objects) {
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
    public RotationGroup(AbstractObject locus) {
        super(new Vector(0, 0, 0), Color.BLACK);
        
        this.locus = locus;
        this.visible = false;
    }
    
    
    //Methods
    
    /**
     * Prepares the Rotation Group to be rendered.
     *
     * @param perspective The perspective to prepare the Rotation Group for.
     * @return The list of BaseObjects that were prepared.
     */
    @Override
    public List<BaseObject> prepare(UUID perspective) {
        return new ArrayList<>();
    }
    
    /**
     * Renders the Rotation Group on the screen.
     *
     * @param perspective The perspective to render the Rotation Group for.
     * @param g2          The 2D Graphics entity.
     */
    @Override
    public void render(Graphics2D g2, UUID perspective) {
    }
    
    /**
     * Adds a rotation transformation to a group over a period of time.
     *
     * @param rollRotation  The total roll rotation in radians.
     * @param pitchRotation The total pitch rotation in radians.
     * @param yawRotation   The total yaw rotation in radians.
     * @param period        The period over which to perform the transition in milliseconds.
     */
    public void rotateGroup(double rollRotation, double pitchRotation, double yawRotation, long period) {
        addRotationTransformation(rollRotation, pitchRotation, yawRotation, period);
        
        RotationGroup thisGroup = this;
        ScheduledExecutorService checkRotationComplete = Executors.newSingleThreadScheduledExecutor();
        checkRotationComplete.scheduleAtFixedRate(() -> {
            if (!inRotationTransformation()) {
                if (parent != null) {
                    parent.unregisterComponent(thisGroup);
                }
                checkRotationComplete.shutdownNow();
            }
        }, 0, 10, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Rotates the Object in a certain direction and saves the rotation in its vector state.
     *
     * @param offset The relative offsets to rotate the Object.
     */
    @Override
    public synchronized void rotateAndTransform(Vector offset) {
        rotateAndTransform(offset, getCenter());
    }
    
    
    //Getters
    
    /**
     * Returns the center point of the Object.
     *
     * @return The center point of the Object.
     */
    @Override
    public Vector getCenter() {
        return locus.getCenter();
    }
    
}
