/*
 * File:    PerspectiveDice.java
 * Package: main.scenes
 * Author:  Zachary Gill
 */

package main.scenes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import camera.Camera;
import main.Environment;
import math.vector.Vector;
import objects.base.Frame;
import objects.base.Scene;
import objects.base.polygon.Rectangle;
import objects.complex.VariablePlane;
import objects.polyhedron.regular.platonic.Dodecahedron;
import objects.polyhedron.regular.platonic.Hexahedron;
import objects.polyhedron.regular.platonic.Icosahedron;
import objects.polyhedron.regular.platonic.Octahedron;
import objects.polyhedron.regular.platonic.Tetrahedron;
import objects.sphere.Sphere;
import utility.SphericalCoordinateUtility;

public class PerspectiveDice extends Scene {
    
    //Main Method
    
    /**
     * The main method for the Perspective Dice scene.
     *
     * @param args The arguments to the main method.
     * @throws Exception When the Scene class cannot be constructed.
     */
    public static void main(String[] args) throws Exception {
        runScene(PerspectiveDice.class);
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Perspective Dice scene.
     *
     * @param environment The Environment to render the Perspective Dice in.
     */
    public PerspectiveDice(Environment environment) {
        super(environment);
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Perspective Dice.
     */
    @Override
    public void calculate() {
        Rectangle floorBounds = new Rectangle(new Vector(-10, -10, -2), new Vector(-10, 10, -2), new Vector(10, 10, -2), new Vector(10, -10, -2));
        VariablePlane floor = new VariablePlane(Color.BLACK, floorBounds, 0.5, .065, 1.5);
        Frame floorFrame = new Frame(floor);
        floorFrame.addColorAnimation(5000, 2500);
        registerComponent(floor);
        registerComponent(floorFrame);
        
        List<Vector> diceLocations = new ArrayList<>();
        for (double theta = 0; theta - (Math.PI * 2) < Environment.OMEGA; theta += Math.PI * 2 / 5) {
            diceLocations.add(SphericalCoordinateUtility.sphericalToCartesian(Math.PI / 2, theta, 5));
        }
        
        Tetrahedron d4 = new Tetrahedron(diceLocations.get(0), Color.BLACK, 1);
        Hexahedron d6 = new Hexahedron(diceLocations.get(1), Color.BLACK, 1);
        Octahedron d8 = new Octahedron(diceLocations.get(2), Color.BLACK, 1);
        Dodecahedron d12 = new Dodecahedron(diceLocations.get(3), Color.BLACK, 1);
        Icosahedron d20 = new Icosahedron(diceLocations.get(4), Color.BLACK, 1);
        Sphere dx = new Sphere(Environment.ORIGIN, Color.BLACK, .25, Math.PI / 32);
        
        d4.addRotationAnimation(2, 0, 0);
        d6.addRotationAnimation(-2, 0, 0);
        d8.addRotationAnimation(2, 0, 0);
        d12.addRotationAnimation(-2, 0, 0);
        d20.addRotationAnimation(2, 0, 0);
        
        registerComponent(d4);
        registerComponent(d6);
        registerComponent(d8);
        registerComponent(d12);
        registerComponent(d20);
        registerComponent(dx);
        
        d4.addOrbitAnimation(dx, 10000);
        d6.addOrbitAnimation(dx, 10000);
        d8.addOrbitAnimation(dx, 10000);
        d12.addOrbitAnimation(dx, 10000);
        d20.addOrbitAnimation(dx, 10000);
        
        Frame d4Frame = new Frame(d4);
        Frame d6Frame = new Frame(d6);
        Frame d8Frame = new Frame(d8);
        Frame d12Frame = new Frame(d12);
        Frame d20Frame = new Frame(d20);
        Frame dxFrame = new Frame(dx);
        
        d4Frame.addColorAnimation(5000, 0);
        d6Frame.addColorAnimation(5000, 0);
        d8Frame.addColorAnimation(5000, 0);
        d12Frame.addColorAnimation(5000, 0);
        d20Frame.addColorAnimation(5000, 0);
        dxFrame.addColorAnimation(5000, 0);
        
        registerComponent(d4Frame);
        registerComponent(d6Frame);
        registerComponent(d8Frame);
        registerComponent(d12Frame);
        registerComponent(d20Frame);
        registerComponent(dxFrame);
    }
    
    /**
     * Sets up components for the Perspective Dice scene.
     */
    @Override
    public void initComponents() {
    }
    
    /**
     * Sets up cameras for the Perspective Dice scene.
     */
    @Override
    public void setupCameras() {
        Camera camera = new Camera(this, true, true);
        camera.setLocation(Math.PI / 2, Math.PI / 2, 2);
        camera.setOffset(new Vector(0, 1, 0));
        camera.setPerspective(Camera.Perspective.FIRST_PERSON);
    }
    
    /**
     * Sets up controls for the Perspective Dice scene.
     */
    @Override
    public void setupControls() {
    }
    
}
