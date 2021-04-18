/*
 * File:    PerspectiveDice.java
 * Package: graphy.main.scene
 * Author:  Zachary Gill
 */

package graphy.main.scene;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import commons.math.CoordinateUtility;
import commons.math.vector.Vector;
import graphy.camera.Camera;
import graphy.main.Environment;
import graphy.object.base.Frame;
import graphy.object.base.Scene;
import graphy.object.base.polygon.Rectangle;
import graphy.object.complex.VariablePlane;
import graphy.object.polyhedron.regular.platonic.Dodecahedron;
import graphy.object.polyhedron.regular.platonic.Hexahedron;
import graphy.object.polyhedron.regular.platonic.Icosahedron;
import graphy.object.polyhedron.regular.platonic.Octahedron;
import graphy.object.polyhedron.regular.platonic.Tetrahedron;
import graphy.object.sphere.Sphere;

public class PerspectiveDice extends Scene {
    
    //Main Method
    
    /**
     * The main method for the Perspective Dice scene.
     *
     * @param args The arguments to the main method.
     * @throws Exception When the Scene cannot be created.
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
        
        Environment.enableRenderBuffering = false;
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
        for (double theta = 0; theta - (Math.PI * 8 / 5) < Environment.OMEGA; theta += Math.PI * 2 / 5) {
            diceLocations.add(CoordinateUtility.sphericalToCartesian(5, theta, Math.PI / 2).plus(Environment.ORIGIN));
        }
        
        Tetrahedron d4 = new Tetrahedron(diceLocations.get(0), Color.BLACK, 1);
        Hexahedron d6 = new Hexahedron(diceLocations.get(1), Color.BLACK, 1);
        Octahedron d8 = new Octahedron(diceLocations.get(2), Color.BLACK, 1);
        Dodecahedron d12 = new Dodecahedron(diceLocations.get(3), Color.BLACK, 1);
        Icosahedron d20 = new Icosahedron(diceLocations.get(4), Color.BLACK, 1);
        Sphere dx = new Sphere(Environment.ORIGIN, Color.BLACK, .25, Math.PI / 16);
        
        d4.addRotationAnimation(0, 0, 2);
        d6.addRotationAnimation(0, 0, -2);
        d8.addRotationAnimation(0, 0, 2);
        d12.addRotationAnimation(0, 0, -2);
        d20.addRotationAnimation(0, 0, 2);
        
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
        Camera camera = new Camera(this, environment.perspective, true, true);
        camera.setLocation(2, Math.PI / 2, Math.PI / 2);
        camera.setOffset(new Vector(0, 1, 0));
        camera.setMode(Camera.Perspective.FIRST_PERSON);
    }
    
    /**
     * Sets up controls for the Perspective Dice scene.
     */
    @Override
    public void setupControls() {
    }
    
}
