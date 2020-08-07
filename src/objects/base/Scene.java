/*
 * File:    Scene.java
 * Package: objects.base
 * Author:  Zachary Gill
 */

package objects.base;

import java.awt.Color;
import java.lang.reflect.Constructor;
import javax.management.InstanceAlreadyExistsException;

import main.Environment;

/**
 * Defines a Scene to render.
 */
public abstract class Scene extends Object {
    
    //Fields
    
    /**
     * The Environment to render the Scene.
     */
    public Environment environment;
    
    
    //Constructors
    
    /**
     * Constructs a Scene.
     *
     * @param environment The Environment to render the Scene in.
     */
    public Scene(Environment environment) {
        super(Environment.ORIGIN, Color.BLACK);
        this.environment = environment;
        environment.setScene(this);
    }
    
    
    //Methods
    
    /**
     * Sets up components for the Scene.
     */
    public void initComponents() {
    }
    
    /**
     * Sets up cameras for the Scene.
     */
    public void setupCameras() {
    }
    
    /**
     * Sets up controls for the Scene.
     */
    public void setupControls() {
    }
    
    /**
     * Determines the name of the Scene.
     *
     * @return The name of the Scene.
     */
    public String getName() {
        return getClass().getSimpleName().replaceAll("(?<![0-9])([A-Z0-9])", " $1");
    }
    
    
    //Static Methods
    
    /**
     * Runs a Scene.
     *
     * @param sceneClass The class of Scene to run.
     * @throws InstanceAlreadyExistsException When an Environment is already defined.
     * @throws Exception                      When the Scene class cannot be constructed.
     */
    protected static void runScene(Class<? extends Scene> sceneClass) throws InstanceAlreadyExistsException, Exception {
        Environment environment = new Environment();
        environment.setDoubleBuffering(true);
        environment.setup();
        environment.setupMainKeyListener();
        
        Constructor<? extends Scene> constructor = sceneClass.getDeclaredConstructor(Environment.class);
        Scene scene = constructor.newInstance(environment);
        environment.frame.setTitle(sceneClass.getSimpleName());
        scene.initComponents();
        scene.setupCameras();
        scene.setupControls();
        scene.calculate();
        
        environment.addObject(scene);
        environment.run();
    }
    
}
