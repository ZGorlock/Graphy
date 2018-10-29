/*
 * File:    Graph.java
 * Package: main.scenes
 * Author:  Zachary Gill
 */

package main.scenes;

import camera.Camera;
import main.Environment;
import math.vector.Vector;
import objects.base.Object;
import objects.base.Scene;
import objects.base.polygon.Rectangle;
import objects.system.Axes;
import utility.MathUtility;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Defines a Graph scene.
 */
public class Graph extends Scene {
    
    //Constants
    
    /**
     * The density of the plain.
     */
    public static double density = 1;
    
    
    //Fields
    
    /**
     * The equation to graph
     */
    private static MathUtility.MathOperation graph = MathUtility.parseMath("5 - x + y^2");
    
    
    //Main Methods
    
    /**
     * The main method for the Sphere Field scene.
     *
     * @param args The arguments to the main method.
     */
    public static void main(String[] args) {
        String[] environmentArgs = new String[] {};
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
        objects.add(new Axes(5));
        
        Object plain = new Object(Color.BLACK);
        
        Set<Vector> vs = new HashSet<>();
        Map<Vector, Double> vsm = new HashMap<>();
        for (double x = -10; x <= 10; x += density) {
            for (double y = -10; y <= 10; y += density) {
                
                List<Vector> vt = new ArrayList<>();
                vt.add(new Vector(x, 0, y));
                vt.add(new Vector(x + density, 0, y));
                vt.add(new Vector(x + density, 0, y + density));
                vt.add(new Vector(x, 0, y + density));
                
                for (int i = 0; i < vt.size(); i++) {
                    for (Vector vsi : vs) {
                        if (vt.get(i).getX() == vsi.getX() &&
                                vt.get(i).getY() == vsi.getY() &&
                                vt.get(i).getZ() == vsi.getZ()) {
                            vt.set(i, vsi);
                        }
                    }
                }
                
                Rectangle t = new Rectangle(plain, Color.WHITE, vt.get(0), vt.get(1), vt.get(2), vt.get(3));
                t.addFrame(Color.BLACK);
                
                vs.addAll(vt);
            }
        }
        
        for (Vector v : vs) {
            Map<String, Number> vars = new HashMap<>();
            vars.put("x", v.getX());
            vars.put("y", v.getZ());
            v.setY(- graph.evaluate(vars).doubleValue());
            if (v.getY() > 20) {
                v.setY(20);
            }
            if (v.getY() < -20) {
                v.setY(-20);
            }
        }
        
        objects.add(plain);
        
        return objects;
    }
    
    /**
     * Sets up cameras for the scene.
     */
    public static void setupCameras() {
        Camera camera = new Camera(true, true);
        camera.setLocation(Math.PI / 2, 0, 50);
        Camera.setActiveCamera(0);
    }
    
    /**
     * Sets up controls for the scene.
     */
    public static void setupControls() {
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Variable Plain Scene.
     *
     * @param center The center of the scene.
     */
    public Graph(Vector center) {
        super(center);
    }
    
}
