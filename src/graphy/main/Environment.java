/*
 * File:    Environment.java
 * Package: graphy.main
 * Author:  Zachary Gill
 */

package graphy.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.management.InstanceAlreadyExistsException;

import commons.math.vector.Vector;
import graphy.camera.Camera;
import graphy.object.base.AbstractObject;
import graphy.object.base.ObjectInterface;
import graphy.object.base.Scene;

/**
 * The main Environment.
 */
public class Environment extends EnvironmentBase {
    
    //Constants
    
    /**
     * The default value of the flag indicating whether or not render buffering should be utilized.
     */
    public static final boolean DEFAULT_ENABLE_RENDER_BUFFERING = true;
    
    /**
     * The default value of the maximum distance to render.
     */
    public static final double DEFAULT_MAX_RENDER_DISTANCE = 250.0;
    
    
    //Static Fields
    
    /**
     * A flag indicating whether or not render buffering should be utilized.
     */
    public static boolean enableRenderBuffering = DEFAULT_ENABLE_RENDER_BUFFERING;
    
    /**
     * The maximum distance to render.
     */
    public static double maxRenderDistance = DEFAULT_MAX_RENDER_DISTANCE;
    
    
    //Fields
    
    /**
     * The Scene to render.
     */
    public Scene scene = null;
    
    /**
     * The unique perspective id for the Environment.
     */
    public final UUID perspective = UUID.randomUUID();
    
    /**
     * The list of Objects to be rendered in the Environment.
     */
    public List<ObjectInterface> objects = new ArrayList<>();
    
    
    //Constructors
    
    /**
     * The default constructor for an Environment.
     *
     * @throws InstanceAlreadyExistsException When an Environment is already defined.
     */
    public Environment() throws InstanceAlreadyExistsException {
        if (!instanced.compareAndSet(false, true)) {
            throw new InstanceAlreadyExistsException("Environment is already defined");
        }
        
        background = Color.WHITE;
        layout = new GridBagLayout();
        
        initializeCommons();
    }
    
    
    //Methods
    
    /**
     * Renders the Environment.
     *
     * @param g2 The 2D Graphics entity.
     */
    @Override
    protected void render(Graphics2D g2) {
        Camera.doRender(perspective, g2);
    }
    
    /**
     * Sizes the window.
     */
    @Override
    protected void sizeWindow() {
        super.sizeWindow();
        Camera.setScreenSize(perspective, new Vector(width, height, screenDepth));
    }
    
    /**
     * Adds the KeyListener for the main Environment controls.
     */
    @Override
    protected void addMainKeyListener() {
        super.addMainKeyListener();
        
        frame.addKeyListener(new KeyListener() {
            
            @Override
            public void keyTyped(KeyEvent e) {
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                
                if (key == KeyEvent.VK_DIVIDE) {
                    for (ObjectInterface object : objects) {
                        object.setDisplayMode(AbstractObject.DisplayMode.EDGE);
                    }
                }
                if (key == KeyEvent.VK_MULTIPLY) {
                    for (ObjectInterface object : objects) {
                        object.setDisplayMode(AbstractObject.DisplayMode.VERTEX);
                    }
                }
                if (key == KeyEvent.VK_SUBTRACT) {
                    for (ObjectInterface object : objects) {
                        object.setDisplayMode(AbstractObject.DisplayMode.FACE);
                    }
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
            }
            
        });
    }
    
    /**
     * Adds an Object to the Environment at runtime.
     *
     * @param object The Object to add to the Environment.
     */
    public void addObject(ObjectInterface object) {
        objects.add(object);
    }
    
    /**
     * Removes an Object from the Environment at runtime.
     *
     * @param object The Object to remove from the Environment.
     */
    public void removeObject(ObjectInterface object) {
        objects.remove(object);
    }
    
    
    //Setters
    
    /**
     * Sets the Scene to render.
     *
     * @param scene The Scene to render.
     */
    public void setScene(Scene scene) {
        this.scene = scene;
        frame.setTitle(scene.getName());
    }
    
}
