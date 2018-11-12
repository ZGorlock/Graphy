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
import objects.base.Object;
import objects.base.Scene;
import objects.base.polygon.Rectangle;
import objects.complex.VariablePlane;
import objects.polyhedron.regular.platonic.Dodecahedron;
import objects.polyhedron.regular.platonic.Hexahedron;
import objects.polyhedron.regular.platonic.Icosahedron;
import objects.polyhedron.regular.platonic.Octahedron;
import objects.polyhedron.regular.platonic.Tetrahedron;
import utility.SphericalCoordinateUtility;

public class PerspectiveDice extends Scene {
    
    //Main Methods
    
    /**
     * The main method for the Rubik's Cube scene.
     *
     * @param args The arguments to the main method.
     */
    public static void main(String[] args) {
        String[] environmentArgs = new String[]{};
        Environment.main(environmentArgs);
        Environment.setupMainKeyListener();
        
        List<Object> objects = createObjects();
        for (Object object : objects) {
            Environment.addObject(object);
        }
        
        setupCameras();
        
        setupControls();
    }
    
    /**
     * Creates objects for the scene.
     *
     * @return A list of Objects that were created for the scene.
     */
    public static List<Object> createObjects() {
        List<Object> objects = new ArrayList<>();
        
        Rectangle floorBounds = new Rectangle(new Vector(-15, -15, -2), new Vector(-15, 15, -2), new Vector(15, 15, -2), new Vector(15, -15, -2));
        VariablePlane floor = new VariablePlane(Color.WHITE, floorBounds, 0.5, .065, 1.5);
        floor.addFrame(Color.BLACK);
        objects.add(floor);
        
        List<Vector> diceLocations = new ArrayList<>();
        for (double theta = 0; theta - (Math.PI * 2) < Environment.omega; theta += Math.PI * 2 / 5) {
            diceLocations.add(SphericalCoordinateUtility.sphericalToCartesian(Math.PI /2, theta, 5));
        }
        
        Tetrahedron d4 = new Tetrahedron(diceLocations.get(0), Color.BLACK, 1);
        Hexahedron d6 = new Hexahedron(diceLocations.get(1), Color.BLACK, 1);
        Octahedron d8 = new Octahedron(diceLocations.get(2), Color.BLACK, 1);
        Dodecahedron d12 = new Dodecahedron(diceLocations.get(3), Color.BLACK, 1);
        Icosahedron d20 = new Icosahedron(diceLocations.get(4), Color.BLACK, 1);
    
        Frame d4Frame = new Frame(d4);
        Frame d6Frame = new Frame(d6);
        Frame d8Frame = new Frame(d8);
        Frame d12Frame = new Frame(d12);
        Frame d20Frame = new Frame(d20);
        
        d4Frame.addColorAnimation(5000, 0);
        d6Frame.addColorAnimation(5000, 0);
        d8Frame.addColorAnimation(5000, 0);
        d12Frame.addColorAnimation(5000, 0);
        d20Frame.addColorAnimation(5000, 0);
        
        objects.add(d4);
        objects.add(d6);
        objects.add(d8);
        objects.add(d12);
        objects.add(d20);
        objects.add(d4Frame);
        objects.add(d6Frame);
        objects.add(d8Frame);
        objects.add(d12Frame);
        objects.add(d20Frame);
        
        return objects;
    }
    
    /**
     * Sets up cameras for the scene.
     */
    public static void setupCameras() {
        Camera camera = new Camera(true, true);
        camera.setLocation(Math.PI / 2, Math.PI / 2, 2);
        camera.setPerspective(Camera.Perspective.FIRST_PERSON);
    }
    
    /**
     * Sets up controls for the scene.
     */
    public static void setupControls() {
    }
    
    
    //Constructors
    
    /**
     * Constructor for a Perspective Dice scene.
     *
     * @param center The center of the scene.
     */
    public PerspectiveDice(Vector center) {
        super(center);
    }
    
}
