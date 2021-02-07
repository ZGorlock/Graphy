/*
 * File:    DirtyPolyhedraExplosion.java
 * Package: graphy.main.scene
 * Author:  Zachary Gill
 */

package graphy.main.scene;

import graphy.main.Environment;

/**
 * Defines a Dirty Polyhedra Explosion scene.
 */
public class DirtyPolyhedraExplosion extends PolyhedraExplosion {
    
    //Main Method
    
    /**
     * The main method for the Dirty Polyhedra Explosion scene.
     *
     * @param args The arguments to the main method.
     * @throws Exception When the Scene cannot be created.
     */
    public static void main(String[] args) throws Exception {
        runScene(DirtyPolyhedraExplosion.class);
    }
    
    
    //Constructors
    
    /**
     * Constructor for a Dirty Polyhedra Explosion scene.
     *
     * @param environment The Environment to render the Dirty Polyhedra Explosion in.
     */
    public DirtyPolyhedraExplosion(Environment environment) {
        super(environment);
    }
    
    
    //Methods
    
    /**
     * Sets up components for the Dirty Polyhedra Explosion scene.
     */
    @Override
    public void initComponents() {
        environment.setBackground(null);
    }
    
}
