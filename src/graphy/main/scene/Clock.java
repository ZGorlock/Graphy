/*
 * File:    Clock.java
 * Package: graphy.main.scene
 * Author:  Zachary Gill
 */

package graphy.main.scene;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;

import commons.math.CoordinateUtility;
import commons.math.component.vector.Vector;
import graphy.camera.Camera;
import graphy.main.Environment;
import graphy.object.base.Scene;
import graphy.object.base.group.LockedEdge;
import graphy.object.base.simple.Text;
import graphy.object.polyhedron.regular.platonic.Hexahedron;

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
    public static void main(String[] args) throws Exception {
        runScene(Clock.class);
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Clock Scene.
     *
     * @param environment The Environment to render the Clock in.
     */
    public Clock(Environment environment) {
        super(environment);
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
            Hexahedron number = new Hexahedron(CoordinateUtility.sphericalToCartesian(10, (Math.PI / 2) - (Math.PI / 6 * i), Math.PI / 2), Color.WHITE, 1);
            number.addFrame(Color.BLACK);
            Text text = new Text(number, number.getCenter().plus(new Vector(0 - ((i > 9) ? .15 : 0), -.15, 1)), String.valueOf(i));
            registerComponent(number);
        }
        
        long lastSecond = System.currentTimeMillis() / 1000;
        
        //noinspection StatementWithEmptyBody
        while ((System.currentTimeMillis() / 1000) == lastSecond) {
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int nowHour = cal.get(Calendar.HOUR_OF_DAY);
        int nowMinute = cal.get(Calendar.MINUTE);
        int nowSecond = cal.get(Calendar.SECOND);
        
        double hour = (((nowHour % 12) * 3600) + (nowMinute * 60) + (nowSecond)) / 43200.0;
        double minute = ((nowMinute * 60) + (nowSecond)) / 3600.0;
        double second = nowSecond / 60.0;
        
        Hexahedron hourMark = new Hexahedron(CoordinateUtility.sphericalToCartesian(4, (Math.PI / 2) - (hour * Math.PI * 2), Math.PI / 2), Color.BLACK, .5);
        Hexahedron minuteMark = new Hexahedron(CoordinateUtility.sphericalToCartesian(6, (Math.PI / 2) - (minute * Math.PI * 2), Math.PI / 2), Color.BLACK, .25);
        Hexahedron secondMark = new Hexahedron(CoordinateUtility.sphericalToCartesian(8, (Math.PI / 2) - (second * Math.PI * 2), Math.PI / 2), Color.BLACK, .125);
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
        
        Environment.addTask(() -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            int currentMinute = calendar.get(Calendar.MINUTE);
            int currentSecond = calendar.get(Calendar.SECOND);
            time.setText(
                    (((currentHour % 12) < 10) ? "0" : "") + (currentHour % 12) + ':' +
                            ((currentMinute < 10) ? "0" : "") + currentMinute + ':' +
                            ((currentSecond < 10) ? "0" : "") + currentSecond);
        });
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
        Camera camera = new Camera(this, environment.perspective, true, true);
        camera.setLocation(25, Math.PI / 2, 0);
    }
    
    /**
     * Sets up controls for the Clock scene.
     */
    @Override
    public void setupControls() {
    }
    
}
