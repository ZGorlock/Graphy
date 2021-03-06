/*
 * File:    Graph.java
 * Package: graphy.main.scene
 * Author:  Zachary Gill
 */

package graphy.main.scene;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import commons.math.EquationUtility;
import commons.math.component.vector.Vector;
import graphy.camera.Camera;
import graphy.main.Environment;
import graphy.math.vector.UniqueVectorSet;
import graphy.object.base.Object;
import graphy.object.base.Scene;
import graphy.object.base.polygon.Rectangle;
import graphy.object.system.Axes;

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
     * @throws Exception When the Scene cannot be created.
     */
    public static void main(String[] args) throws Exception {
        runScene(Graph.class);
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Graph Scene.
     *
     * @param environment The Environment to render the Graph in.
     */
    public Graph(Environment environment) {
        super(environment);
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Graph.
     */
    @Override
    public void calculate() {
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
            vars.put("x", v.getRawX());
            vars.put("y", v.getRawY());
            v.setZ(equation.evaluate(vars).doubleValue());
            if (v.getRawZ() != v.getRawZ()) {
                v.setZ(0.0);
            }
            if (v.getRawZ() > boundDiameter) {
                v.setZ((double) boundDiameter);
            }
            if (v.getRawZ() < -boundDiameter) {
                v.setZ((double) -boundDiameter);
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
        Camera camera = new Camera(this, environment.perspective, true, true);
        camera.setLocation(25, 5 * Math.PI / 4, Math.PI / 4);
    }
    
    /**
     * Sets up controls for the Graph scene.
     */
    @Override
    public void setupControls() {
    }
    
}
