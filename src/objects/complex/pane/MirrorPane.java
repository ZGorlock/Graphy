/*
 * File:    MirrorPane.java
 * Package: objects.complex.pane
 * Author:  Zachary Gill
 */

package objects.complex.pane;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import camera.Camera;
import main.Environment;
import math.vector.Vector;
import objects.base.AbstractObject;
import objects.base.polygon.Rectangle;
import utility.SphericalCoordinateUtility;

/**
 * Defines a Mirror Pane.
 */
public class MirrorPane extends Pane {
    
    //Fields
    
    /**
     * The perspective for the Mirror Pane.
     */
    private UUID parentPerspective;
    
    /**
     * The perspective for the Mirror Pane.
     */
    private UUID perspective;
    
    /**
     * The Camera for the Mirror Pane.
     */
    private Camera camera;
    
    
    //Constructors
    
    /**
     * The constructor for a Mirror Pane.
     *
     * @param parent            The parent of the Mirror Pane.
     * @param color             The color of the Mirror Pane.
     * @param bounds            The bounds of the Mirror Pane.
     * @param invert            Whether or not to invert the colors of the Mirror Pane.
     * @param parentPerspective The perspective of the parent scene.
     */
    public MirrorPane(AbstractObject parent, Color color, Rectangle bounds, boolean invert, UUID parentPerspective) {
        super(parent, color, bounds, invert);
        
        this.parentPerspective = parentPerspective;
        this.perspective = UUID.randomUUID();
        this.camera = new Camera(null, parentPerspective, new Vector(bounds.getP2().distance(bounds.getP1()) / 1000.0, bounds.getP4().distance(bounds.getP1()) / 1000.0), new Vector(bounds.getP2().distance(bounds.getP1()), bounds.getP4().distance(bounds.getP1()), Environment.screenZ), false, false);
        camera.setOffset(getCenter());
        camera.setVisible(true);
        
        renderMirror();
    }
    
    /**
     * The constructor for a Mirror Pane.
     *
     * @param parent            The parent of the Mirror Pane.
     * @param color             The color of the Mirror Pane.
     * @param bounds            The bounds of the Mirror Pane.
     * @param parentPerspective The perspective of the parent scene.
     */
    public MirrorPane(AbstractObject parent, Color color, Rectangle bounds, UUID parentPerspective) {
        this(parent, color, bounds, false, parentPerspective);
    }
    
    
    //Methods
    
    /**
     * Renders the mirror.
     */
    private void renderMirror() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (normal == null) {
                    return;
                }
                
                Camera parentCamera = Camera.getActiveCameraView(parentPerspective);
                if (parentCamera == null) {
                    return;
                }
                
                Vector viewer = parentCamera.getCameraPosition();
                Vector virtual = viewer.minus(normal.scale(2 * viewer.dot(normal)));
                camera.setLocation(SphericalCoordinateUtility.cartesianToSpherical(virtual));
                
                //TODO render scene through camera and paint to pane
            }
        }, 0, 1000 / Environment.fps);
    }
    
    /**
     * Sets the center point of the Pane.
     *
     * @param center The new center point of the Pane.
     */
    @Override
    public void setCenter(Vector center) {
        super.setCenter(center);
        this.camera.setOffset(center);
    }
    
}
