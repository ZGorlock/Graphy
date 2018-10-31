/*
 * File:    VariablePlain.java
 * Package: main.scenes
 * Author:  Zachary Gill
 */

package main.scenes;

import camera.Camera;
import main.Environment;
import math.vector.Vector;
import objects.base.Object;
import objects.base.Scene;
import objects.base.polygon.Triangle;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Defines a Variable Plain scene.
 */
public class VariablePlain extends Scene {
    
    //Constants
    
    /**
     * The density of the plain.
     */
    public static double density = 0.5;
    
    
    //Main Methods
    
    /**
     * The main method for the Variable Plain scene.
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
        Object plain = new Object(Color.BLACK);
        
        Set<Vector> vs = new HashSet<>();
        Map<Vector, Double> vsm = new HashMap<>();
        for (double x = -10; x <= 10; x += density) {
            for (double y = -10; y <= 10; y += density) {
                for (double z = -density; z <= density; z += density * 2) {
                    List<Vector> vt = new ArrayList<>();
                    vt.add(new Vector(x, y, 0));
                    vt.add(new Vector(x + z, y + z, 0));
                    vt.add(new Vector(new Vector(x + z, y, 0)));
                    
                    for (int i = 0; i < vt.size(); i++) {
                        for (Vector vsi : vs) {
                            if (vt.get(i).getX() == vsi.getX() &&
                                    vt.get(i).getY() == vsi.getY() &&
                                    vt.get(i).getZ() == vsi.getZ()) {
                                vt.set(i, vsi);
                            }
                        }
                    }
                    
                    Triangle t = new Triangle(plain, Color.WHITE, vt.get(0), vt.get(1), vt.get(2));
//                    t.addColorAnimation(10000, 0);
                    t.addFrame(Color.BLACK);
                    
                    vs.addAll(vt);
                }
            }
        }
        
        objects.add(plain);
        
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Vector v : vs) {
                    if (vsm.containsKey(v)) {
                        double m = vsm.get(v);
                        v.setZ(v.getZ() + m);
                        
                        if (Math.abs(v.getZ()) >= density / 2) {
                            m = -m;
                            vsm.replace(v, m);
                        }
                    } else {
                        vsm.put(v, (Math.random() - .5) / 100);
                    }
                }
            }
        }, 0, 50);
        
        return objects;
    }
    
    /**
     * Sets up cameras for the scene.
     */
    public static void setupCameras() {
        Camera camera = new Camera(true, true);
        camera.setLocation(4 * Math.PI / 6, Math.PI / 4, 5);
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
    public VariablePlain(Vector center) {
        super(center);
    }
    
}
