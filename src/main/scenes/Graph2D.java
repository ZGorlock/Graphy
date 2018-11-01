/*
 * File:    Graph2D.java
 * Package: main.scenes
 * Author:  Zachary Gill
 */

package main.scenes;

import camera.Camera;
import main.Environment;
import math.vector.Vector;
import objects.base.Object;
import objects.base.Scene;
import objects.base.simple.Edge;
import objects.system.Axes;
import utility.EquationUtility;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Defines a Graph2D scene.
 */
public class Graph2D extends Scene {
    
    //Constants
    
    /**
     * The density of the graph.
     */
    public static double density = 0.01;
    
    
    //Fields
    
    /**
     * The equation to graph
     */
    private static EquationUtility.MathOperation graph = EquationUtility.parseMath("x");
    
    
    //Main Methods
    
    /**
     * The main method for the Graph2D scene.
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
        objects.add(new Axes(5));
        
        Object plain = new Object(Color.BLACK);
        
        Set<Vector> vs = new HashSet<>();
        Map<Vector, Double> vsm = new HashMap<>();
        for (double x = -10; x <= 10; x += density) {
            
            List<Vector> vt = new ArrayList<>();
            vt.add(new Vector(x, 0, 0));
            vt.add(new Vector(x + density, 0, 0));
            
            for (int i = 0; i < vt.size(); i++) {
                for (Vector vsi : vs) {
                    if (vt.get(i).getX() == vsi.getX() &&
                            vt.get(i).getY() == vsi.getY() &&
                            vt.get(i).getZ() == vsi.getZ()) {
                        vt.set(i, vsi);
                    }
                }
            }
            
            Edge t = new Edge(plain, Color.BLACK, vt.get(0), vt.get(1));
            
            vs.addAll(vt);
        }
        
        for (Vector v : vs) {
            Map<String, Number> vars = new HashMap<>();
            vars.put("x", Math.atan(v.getX()));
            v.setY(-graph.evaluate(vars).doubleValue());
            if (v.getY() != v.getY()) {
                v.setY(0);
            }
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
        camera.setLocation(Math.PI, 0, 50);
        Camera.setActiveCamera(0);
    }
    
    /**
     * Sets up controls for the scene.
     */
    public static void setupControls() {
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Graph2D Scene.
     *
     * @param center The center of the scene.
     */
    public Graph2D(Vector center) {
        super(center);
    }
    
}
