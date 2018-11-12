/*
 * File:    Graph.java
 * Package: main.scenes
 * Author:  Zachary Gill
 */

package main.scenes;

import camera.Camera;
import main.Environment;
import math.vector.UniqueVectorSet;
import math.vector.Vector;
import objects.base.Object;
import objects.base.Scene;
import objects.base.polygon.Rectangle;
import objects.system.Axes;
import utility.EquationUtility;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Defines a Graph scene.
 */
public class Graph extends Scene {
    
    //Constants
    
    /**
     * The density of the graph.
     */
    public static double density = .25;
    
    /**
     * The diameter of the bounding cube to render results within.
     */
    public static int boundDiameter = 10;
    
    
    //Fields
    
    /**
     * The equation to graph
     */
    private static EquationUtility.MathOperation graph = EquationUtility.parseMath("x^3 - y^3 - x^2 + y^2 - x + y");
    
    
    //Main Methods
    
    /**
     * The main method for the Graph scene.
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
        UniqueVectorSet uniqueVectorSet = new UniqueVectorSet();
        Map<Vector, Double> vsm = new HashMap<>();
        for (double x = -boundDiameter / 2.0; x <= boundDiameter / 2.0; x += density) {
            for (double y = -boundDiameter / 2.0; y <= boundDiameter / 2.0; y += density) {
                
                List<Vector> vt = new ArrayList<>();
                vt.add(new Vector(x, y, 0));
                vt.add(new Vector(x + density, y, 0));
                vt.add(new Vector(x + density, y + density, 0));
                vt.add(new Vector(x, y + density, 0));
                
                uniqueVectorSet.alignVectorsToSet(vt);
                
                Rectangle t = new Rectangle(plain, Color.WHITE, vt.get(0), vt.get(1), vt.get(2), vt.get(3));
                t.addFrame(Color.BLACK);
                
                vs.addAll(vt);
            }
        }
        
        for (Vector v : vs) {
            Map<String, Number> vars = new HashMap<>();
            vars.put("x", v.getX());
            vars.put("y", v.getY());
            v.setZ(graph.evaluate(vars).doubleValue());
            if (v.getZ() != v.getZ()) {
                v.setZ(0);
            }
            if (v.getZ() > boundDiameter) {
                v.setZ(boundDiameter);
            }
            if (v.getZ() < -boundDiameter) {
                v.setZ(-boundDiameter);
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
        camera.setLocation(Math.PI / 4, 5 * Math.PI / 4, 25);
    }
    
    /**
     * Sets up controls for the scene.
     */
    public static void setupControls() {
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Graph Scene.
     *
     * @param center The center of the scene.
     */
    public Graph(Vector center) {
        super(center);
    }
    
}
