/*
 * File:    MirrorPane.java
 * Package: graphy.objects.complex.pane
 * Author:  Zachary Gill
 */

package graphy.object.complex.pane;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import commons.graphics.DrawUtility;
import commons.graphics.ImageTransformationUtility;
import commons.math.CoordinateUtility;
import commons.math.component.vector.Vector;
import graphy.camera.Camera;
import graphy.main.Environment;
import graphy.object.base.AbstractObject;
import graphy.object.base.polygon.Rectangle;

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
     * @param parentPerspective The perspective of the parent scene.
     */
    public MirrorPane(AbstractObject parent, Color color, Rectangle bounds, UUID parentPerspective) {
        super(parent, color, bounds);
        
        this.parentPerspective = parentPerspective;
        this.perspective = UUID.randomUUID();
        this.camera = new Camera(Camera.getScene(this.parentPerspective), perspective, false, false);
        
        displayMode = DisplayMode.FACE;
    }
    
    //Methods
    
    /**
     * Renders the Mirror Pane on the screen.
     *
     * @param perspective The perspective to render the Mirror Pane for.
     * @param g2          The 2D Graphics entity.
     */
    @Override
    public void render(Graphics2D g2, UUID perspective) {
        if (getDisplayMode() != null) {
            super.render(g2, perspective);
        }
    }
    
    /**
     * Calculates the unit normal vector of the Mirror Pane.
     *
     * @return The unit normal vector of the Mirror Pane.
     */
    @Override
    public Vector calculateNormal() {
        super.calculateNormal();
        if (normal.dot(camera.getCameraPosition()) < 0) {
            normal = normal.scale(-1);
        }
        
        camera.setNormalLimit(normal);
        return normal;
    }
    
    /**
     * Draws the drawing to the Mirror Pane.
     *
     * @param g2          The 2D Graphics entity.
     * @param perspective The perspective from which to draw the drawing to the Mirror Pane.
     */
    @Override
    protected void draw(Graphics2D g2, UUID perspective) {
        setDisplayMode(null);
        renderMirror(g2, perspective);
        setDisplayMode(DisplayMode.FACE);
    }
    
    /**
     * Renders the mirror to the Mirror Pane.
     *
     * @param g2          The 2D Graphics entity.
     * @param perspective The perspective from which to draw the drawing to the Mirror Pane.
     */
    private void renderMirror(Graphics2D g2, UUID perspective) {
        if (normal == null) {
            return;
        }
        
        this.parentPerspective = perspective;
        Camera parentCamera = Camera.getActiveCameraView(this.parentPerspective);
        if (parentCamera == null) {
            return;
        }
        camera.setOffset(getCenter());
        
        Vector viewer = parentCamera.getCameraPosition();
        Vector virtual = viewer.minus(normal.scale(2 * viewer.dot(normal)));
        camera.setLocation(CoordinateUtility.cartesianToSpherical(virtual));
        
        if (!preparedForPerspective(this.perspective)) {
            prePrepare(this.perspective);
        }
        camera.fitToObject(this);
        prepare(this.perspective);
        List<Vector> bounds = new ArrayList<>(getPrepared(this.perspective).subList(0, 4));
        Camera.projectVectorsToCamera(this.perspective, bounds);
        Camera.collapseVectorsToViewport(this.perspective, bounds);
        Camera.scaleVectorsToScreen(this.perspective, bounds);
        
        Vector screenSize = Camera.getScreenSize(this.perspective);
        BufferedImage reflection = new BufferedImage((int) Math.max(screenSize.getRawX(), 1), (int) Math.max(screenSize.getRawY(), 1), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2Reflection = reflection.createGraphics();
        DrawUtility.setClip(g2Reflection, bounds);
        camera.render(g2Reflection);
        DrawUtility.dispose(g2Reflection);
        
        if (!preparedForPerspective(this.parentPerspective)) {
            prePrepare(this.parentPerspective);
        }
        prepare(this.parentPerspective);
        List<Vector> parentBounds = new ArrayList<>(getPrepared(this.parentPerspective).subList(0, 4));
        Camera.projectVectorsToCamera(this.parentPerspective, parentBounds);
        Camera.collapseVectorsToViewport(this.parentPerspective, parentBounds);
        Camera.scaleVectorsToScreen(this.parentPerspective, parentBounds);
        
        Shape saveClip = DrawUtility.getClip(g2);
        DrawUtility.setClip(g2, parentBounds);
        ImageTransformationUtility.transformImage(reflection, bounds, g2, Environment.screenWidth, Environment.screenHeight, parentBounds);
        DrawUtility.setClip(g2, saveClip);
    }
    
    
    //Setters
    
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
