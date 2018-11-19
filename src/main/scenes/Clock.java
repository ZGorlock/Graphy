/*
 * File:    Clock.java
 * Package: main.scenes
 * Author:  Zachary Gill
 */

package main.scenes;

import camera.Camera;
import main.Environment;
import math.vector.Vector;
import objects.base.Object;
import objects.base.Scene;
import objects.base.group.LockedEdge;
import objects.base.simple.Text;
import objects.polyhedron.regular.platonic.Hexahedron;
import utility.SphericalCoordinateUtility;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Defines an Clock scene.
 */
public class Clock extends Scene {
    
    //Main Methods
    
    /**
     * The main method for the Clock scene.
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
        
        Hexahedron c = new Hexahedron(Environment.origin, Color.BLACK, 1);
        Text time = new Text(c, Color.WHITE, c.getCenter().plus(new Vector(-.55, -.15, 1)), "00:00:00");
        c.addFrame(Color.WHITE);
        
        List<Hexahedron> numbers = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Hexahedron number = new Hexahedron(SphericalCoordinateUtility.sphericalToCartesian(Math.PI / 2, (Math.PI / 2) - (Math.PI / 6 * i), 10), Color.WHITE, 1);
            number.addFrame(Color.BLACK);
            Text text = new Text(number, number.getCenter().plus(new Vector(0 - ((i > 9) ? .15 : 0), -.15, 1)), String.valueOf(i));
            numbers.add(number);
        }
        
        objects.add(c);
        objects.addAll(numbers);
        
        long lastSecond = System.currentTimeMillis() / 1000;
        
        //noinspection StatementWithEmptyBody
        while ((System.currentTimeMillis() / 1000) == lastSecond) {
        }
        
        Date now = new Date();
        double hour = (((now.getHours() % 12) * 3600) + (now.getMinutes() * 60) + (now.getSeconds())) / 43200.0;
        double minute = ((now.getMinutes() * 60) + (now.getSeconds())) / 3600.0;
        double second = now.getSeconds() / 60.0;
        
        Hexahedron hourMark = new Hexahedron(SphericalCoordinateUtility.sphericalToCartesian(Math.PI / 2, (Math.PI / 2) - (hour * Math.PI * 2), 4), Color.BLACK, .5);
        Hexahedron minuteMark = new Hexahedron(SphericalCoordinateUtility.sphericalToCartesian(Math.PI / 2, (Math.PI / 2) - (minute * Math.PI * 2), 6), Color.BLACK, .25);
        Hexahedron secondMark = new Hexahedron(SphericalCoordinateUtility.sphericalToCartesian(Math.PI / 2, (Math.PI / 2) - (second * Math.PI * 2), 8), Color.BLACK, .125);
        hourMark.addFrame(Color.WHITE);
        minuteMark.addFrame(Color.WHITE);
        secondMark.addFrame(Color.WHITE);
        
        new LockedEdge(hourMark, Color.BLACK, c, hourMark);
        new LockedEdge(minuteMark, Color.BLACK, c, minuteMark);
        new LockedEdge(secondMark, Color.BLACK, c, secondMark);
        
        objects.add(hourMark);
        objects.add(minuteMark);
        objects.add(secondMark);
        
        hourMark.addOrbitAnimation(c, 43200000);
        minuteMark.addOrbitAnimation(c, 3600000);
        secondMark.addOrbitAnimation(c, 60000);
        
        Timer updateTime = new Timer();
        updateTime.scheduleAtFixedRate(new TimerTask() {
            
            //Methods
            
            /**
             * Updates the time string.
             */
            @Override
            public void run() {
                Date now = new Date();
                time.setText((((now.getHours() % 12) < 10) ? "0" : "") + (now.getHours() % 12) + ':' + ((now.getMinutes() < 10) ? "0" : "") + now.getMinutes() + ':' + ((now.getSeconds() < 10) ? "0" : "") + now.getSeconds());
            }
        }, 0, 1000 / Environment.FPS);
        
        return objects;
    }
    
    /**
     * Sets up cameras for the scene.
     */
    public static void setupCameras() {
        Camera camera = new Camera(true, true);
        camera.setLocation(0, Math.PI / 2, 25);
    }
    
    /**
     * Sets up controls for the scene.
     */
    public static void setupControls() {
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Clock Scene.
     *
     * @param center The center of the scene.
     */
    public Clock(Vector center) {
        super(center);
    }
    
}
