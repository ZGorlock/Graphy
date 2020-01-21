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
import java.util.List;
import java.util.*;

/**
 * Defines a Graph scene.
 */
public class Graph extends Scene {
    
    //Static Fields
    
    /**
     * The density of the graph.
     */
    public static double density = .25;
    
    /**
     * The diameter of the bounding cube to render results within.
     */
    public static int boundDiameter = 10;
    
    /**
     * The equation to graph
     */
    private static EquationUtility.MathOperation equation = EquationUtility.parseMath("x^3 - y^3 - x^2 + y^2 - x + y");
    
    
    //Main Method
    
    /**
     * The main method for the Graph scene.
     *
     * @param args The arguments to the main method.
     */
    public static void main(String[] args) {
        Environment environment = new Environment();
        environment.setup();
        environment.setupMainKeyListener();
        
        Graph graph = new Graph(environment);
        graph.initComponents();
        graph.setupCameras();
        graph.setupControls();
        
        environment.addObject(graph);
        environment.run();
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Graph Scene.
     *
     * @param environment The Environment to render the Graph in.
     */
    public Graph(Environment environment) {
        super(environment);
        
        calculate();
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Graph.
     */
    @Override
    public void calculate() {
        ;
        registerComponent(new Axes(5));
        
        Object plane = new Object(Color.BLACK);
        
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
                
                Rectangle t = new Rectangle(plane, Color.WHITE, vt.get(0), vt.get(1), vt.get(2), vt.get(3));
                t.addFrame(Color.BLACK);
                
                vs.addAll(vt);
            }
        }
        
        for (Vector v : vs) {
            Map<String, Number> vars = new HashMap<>();
            vars.put("x", v.getX());
            vars.put("y", v.getY());
            v.setZ(equation.evaluate(vars).doubleValue());
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
        
        registerComponent(plane);
    }
    
    /**
     * Sets up components for the Graph scene.
     */
    @Override
    public void initComponents() {
    }
    
    /**
     * Sets up cameras for the Graph scene.
     */
    @Override
    public void setupCameras() {
        Camera camera = new Camera(this, true, true);
        camera.setLocation(Math.PI / 4, 5 * Math.PI / 4, 25);
    }
    
    /**
     * Sets up controls for the Graph scene.
     */
    @Override
    public void setupControls() {
    }
    
}
