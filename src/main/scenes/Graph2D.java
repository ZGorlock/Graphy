/*
 * File:    Graph2D.java
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
import objects.base.simple.Edge;
import objects.system.Axes;
import utility.EquationUtility;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Defines a Graph2D scene.
 */
public class Graph2D extends Scene {
    
    //Static Fields
    
    /**
     * The density of the graph.
     */
    public static double density = 0.01;
    
    /**
     * The equation to graph
     */
    private static EquationUtility.MathOperation equation = EquationUtility.parseMath("x + 3");
    
    
    //Main Method
    
    /**
     * The main method for the Graph2D scene.
     *
     * @param args The arguments to the main method.
     */
    public static void main(String[] args) {
        Environment environment = new Environment();
        environment.setup();
        environment.setupMainKeyListener();
        
        Graph2D graph2D = new Graph2D(environment);
        graph2D.initComponents();
        graph2D.setupCameras();
        graph2D.setupControls();
        
        environment.addObject(graph2D);
        environment.run();
    }
    
    //Constructors
    
    /**
     * Constructor for the Graph2D Scene.
     *
     * @param environment The Environment to render the Graph2D in.
     */
    public Graph2D(Environment environment) {
        super(environment);
        
        calculate();
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Graph2D.
     */
    @Override
    public void calculate() {
        registerComponent(new Axes(5));
        
        Object plane = new Object(Color.BLACK);
        
        Set<Vector> vs = new HashSet<>();
        UniqueVectorSet uniqueVectorSet = new UniqueVectorSet();
        Map<Vector, Double> vsm = new HashMap<>();
        for (double x = -10; x <= 10; x += density) {
            
            List<Vector> vt = new ArrayList<>();
            vt.add(new Vector(x, 0, 0));
            vt.add(new Vector(x + density, 0, 0));
            
            uniqueVectorSet.alignVectorsToSet(vt);
            
            Edge t = new Edge(plane, Color.BLACK, vt.get(0), vt.get(1));
            
            vs.addAll(vt);
        }
        
        for (Vector v : vs) {
            Map<String, Number> vars = new HashMap<>();
            vars.put("x", Math.atan(v.getX()));
            v.setY(equation.evaluate(vars).doubleValue());
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
        
        registerComponent(plane);
    }
    
    /**
     * Sets up components for the Graph2D scene.
     */
    @Override
    public void initComponents() {
    }
    
    /**
     * Sets up cameras for the Graph2D scene.
     */
    @Override
    public void setupCameras() {
        Camera camera = new Camera(this, true, true);
        camera.setLocation(0, Math.PI / 2, 20);
    }
    
    /**
     * Sets up controls for the Graph2D scene.
     */
    @Override
    public void setupControls() {
    }
    
}
