/*
 * File:    Clock.java
 * Package: main.scenes
 * Author:  Zachary Gill
 */

package main.scenes;

import camera.Camera;
import main.Environment;
import math.vector.Vector;
import objects.base.Scene;
import objects.base.group.LockedEdge;
import objects.base.simple.Text;
import objects.polyhedron.regular.platonic.Hexahedron;
import utility.SphericalCoordinateUtility;

import java.awt.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Defines an Clock scene.
 */
public class Clock extends Scene {
    
    //Main Method
    
    /**
     * The main method for the Clock scene.
     *
     * @param args The arguments to the main method.
     */
    public static void main(String[] args) {
        Environment environment = new Environment();
        environment.setup();
        environment.setupMainKeyListener();
        
        Clock clock = new Clock(environment);
        clock.initComponents();
        clock.setupCameras();
        clock.setupControls();
        
        environment.addObject(clock);
        environment.run();
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Clock Scene.
     *
     * @param environment The Environment to render the Clock in.
     */
    public Clock(Environment environment) {
        super(environment);
        
        calculate();
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Clock.
     */
    @Override
    public void calculate() {
        Hexahedron c = new Hexahedron(Environment.ORIGIN, Color.BLACK, 1);
        Text time = new Text(c, Color.WHITE, c.getCenter().plus(new Vector(-.55, -.15, 1)), "00:00:00");
        c.addFrame(Color.WHITE);
        registerComponent(c);
        
        for (int i = 1; i <= 12; i++) {
            Hexahedron number = new Hexahedron(SphericalCoordinateUtility.sphericalToCartesian(Math.PI / 2, (Math.PI / 2) - (Math.PI / 6 * i), 10), Color.WHITE, 1);
            number.addFrame(Color.BLACK);
            Text text = new Text(number, number.getCenter().plus(new Vector(0 - ((i > 9) ? .15 : 0), -.15, 1)), String.valueOf(i));
            registerComponent(number);
        }
        
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
        
        registerComponent(hourMark);
        registerComponent(minuteMark);
        registerComponent(secondMark);
        
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
        }, 0, 1000 / Environment.fps);
    }
    
    /**
     * Sets up components for the Clock scene.
     */
    @Override
    public void initComponents() {
    }
    
    /**
     * Sets up cameras for the Clock scene.
     */
    @Override
    public void setupCameras() {
        Camera camera = new Camera(this, true, true);
        camera.setLocation(0, Math.PI / 2, 25);
    }
    
    /**
     * Sets up controls for the Clock scene.
     */
    @Override
    public void setupControls() {
    }
    
}
