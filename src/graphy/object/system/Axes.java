/*
 * File:    Axes.java
 * Package: graphy.objects.system
 * Author:  Zachary Gill
 */

package graphy.object.system;

import java.awt.Color;

import commons.math.component.vector.Vector;
import graphy.main.Environment;
import graphy.object.base.Object;
import graphy.object.base.simple.Edge;
import graphy.object.base.simple.Text;

/**
 * Defines the coordinate Axes.
 */
public class Axes extends Object {
    
    
    //Fields
    
    /**
     * The radius of the Axes.
     */
    public int axesRadius;
    
    /**
     * The step for the Axes in Scene space.
     */
    public double step;
    
    /**
     * The size of the step for the Axes in graph space.
     */
    public double stepValue;
    
    /**
     * The main x coordinate Axis.
     */
    public Object xAxis = new Object();
    
    /**
     * The main y coordinate Axis.
     */
    public Object yAxis = new Object();
    
    /**
     * The main z coordinate Axis.
     */
    public Object zAxis = new Object();
    
    /**
     * The x sub coordinate Axes.
     */
    public Object xSubAxes = new Object();
    
    /**
     * The y sub coordinate Axes.
     */
    public Object ySubAxes = new Object();
    
    /**
     * The z sub coordinate Axes.
     */
    public Object zSubAxes = new Object();
    
    /**
     * The steps of the x coordinate Axis.
     */
    public Object xSteps = new Object();
    
    /**
     * The steps of the y coordinate Axis.
     */
    public Object ySteps = new Object();
    
    /**
     * The steps of the z coordinate Axis.
     */
    public Object zSteps = new Object();
    
    /**
     * Whether or not to display the x Axis.
     */
    public boolean displayXAxis;
    
    /**
     * Whether or not to display the y Axis.
     */
    public boolean displayYAxis;
    
    /**
     * Whether or not to display the z Axis.
     */
    public boolean displayZAxis;
    
    /**
     * Whether or not to display the x sub Axis.
     */
    public boolean displayXSubAxes;
    
    /**
     * Whether or not to display the y sub Axis.
     */
    public boolean displayYSubAxes;
    
    /**
     * Whether or not to display the z sub Axis.
     */
    public boolean displayZSubAxes;
    
    /**
     * Whether or not to display the x steps.
     */
    public boolean displayXSteps;
    
    /**
     * Whether or not to display the y steps.
     */
    public boolean displayYSteps;
    
    /**
     * Whether or not to display the z steps.
     */
    public boolean displayZSteps;
    
    /**
     * Whether or not to preload components that are not displayed.
     */
    public boolean preload;
    
    
    //Constructors
    
    /**
     * The constructor for the coordinate Axes.
     *
     * @param axesRadius      The radius of the Axes.
     * @param displayXAxis    Whether or not to display the x Axis.
     * @param displayYAxis    Whether or not to display the y Axis.
     * @param displayZAxis    Whether or not to display the z Axis.
     * @param displayXSubAxes Whether or not to display the x sub Axes.
     * @param displayYSubAxes Whether or not to display the y sub Axes.
     * @param displayZSubAxes Whether or not to display the z sub Axes.
     * @param step            The frequency with which to produce the steps of the Axes in Scene space.
     * @param stepValue       The size of the steps of the Axes in graph space.
     * @param displayXStep    Whether or not to display the x step.
     * @param displayYStep    Whether or not to display the y step.
     * @param displayZStep    Whether or not to display the z step.
     * @param preload         Whether or not to preload components that are not displayed.
     */
    public Axes(int axesRadius, boolean displayXAxis, boolean displayYAxis, boolean displayZAxis, boolean displayXSubAxes, boolean displayYSubAxes, boolean displayZSubAxes, double step, double stepValue, boolean displayXStep, boolean displayYStep, boolean displayZStep, boolean preload) {
        super(Environment.ORIGIN, Color.BLACK);
        
        this.axesRadius = axesRadius;
        this.displayXAxis = displayXAxis;
        this.displayYAxis = displayYAxis;
        this.displayZAxis = displayZAxis;
        this.displayXSubAxes = displayXSubAxes;
        this.displayYSubAxes = displayYSubAxes;
        this.displayZSubAxes = displayZSubAxes;
        this.step = step;
        this.stepValue = stepValue;
        this.displayXSteps = displayXStep;
        this.displayYSteps = displayYStep;
        this.displayZSteps = displayZStep;
        this.preload = preload;
        
        calculate();
        setClippingEnabled(false);
    }
    
    /**
     * The constructor for the coordinate Axes.
     *
     * @param axesRadius     The radius of the Axes.
     * @param displayXAxis   Whether or not to display the x Axis.
     * @param displayYAxis   Whether or not to display the y Axis.
     * @param displayZAxis   Whether or not to display the z Axis.
     * @param displaySubAxes Whether or not to display the sub Axes.
     * @param step           The frequency with which to produce the steps of the Axes in Scene space.
     * @param stepValue      The size of the steps of the Axes in graph space.
     * @param preload        Whether or not to preload components that are not displayed.
     */
    public Axes(int axesRadius, boolean displayXAxis, boolean displayYAxis, boolean displayZAxis, boolean displaySubAxes, double step, double stepValue, boolean preload) {
        this(axesRadius, displayXAxis, displayYAxis, displayZAxis, displaySubAxes, displaySubAxes, displaySubAxes, step, stepValue, true, true, true, preload);
    }
    
    /**
     * The constructor for the coordinate Axes.
     *
     * @param axesRadius     The radius of the Axes.
     * @param displayXAxis   Whether or not to display the x Axis.
     * @param displayYAxis   Whether or not to display the y Axis.
     * @param displayZAxis   Whether or not to display the z Axis.
     * @param displaySubAxes Whether or not to display the sub Axes.
     * @param step           The frequency with which to produce the steps of the Axes in Scene space.
     * @param stepValue      The size of the steps of the Axes in graph space.
     */
    public Axes(int axesRadius, boolean displayXAxis, boolean displayYAxis, boolean displayZAxis, boolean displaySubAxes, double step, double stepValue) {
        this(axesRadius, displayXAxis, displayYAxis, displayZAxis, displaySubAxes, step, stepValue, false);
    }
    
    /**
     * The constructor for the coordinate Axes.
     *
     * @param axesRadius     The radius of the Axes.
     * @param displayXAxis   Whether or not to display the x Axis.
     * @param displayYAxis   Whether or not to display the y Axis.
     * @param displayZAxis   Whether or not to display the z Axis.
     * @param displaySubAxes Whether or not to display the sub Axes.
     * @param displaySteps   Whether or not to display the steps for the Axes.
     * @param preload        Whether or not to preload components that are not displayed.
     */
    public Axes(int axesRadius, boolean displayXAxis, boolean displayYAxis, boolean displayZAxis, boolean displaySubAxes, boolean displaySteps, boolean preload) {
        this(axesRadius, displayXAxis, displayYAxis, displayZAxis, displaySubAxes, displaySubAxes, displaySubAxes, 1, 1, displaySteps, displaySteps, displaySteps, preload);
    }
    
    /**
     * The constructor for the coordinate Axes.
     *
     * @param axesRadius     The radius of the axes.
     * @param displayXAxis   Whether or not to display the x Axis.
     * @param displayYAxis   Whether or not to display the y Axis.
     * @param displayZAxis   Whether or not to display the z Axis.
     * @param displaySubAxes Whether or not to display the sub Axes.
     * @param displaySteps   Whether or not to display the steps for the Axes.
     */
    public Axes(int axesRadius, boolean displayXAxis, boolean displayYAxis, boolean displayZAxis, boolean displaySubAxes, boolean displaySteps) {
        this(axesRadius, displayXAxis, displayYAxis, displayZAxis, displaySubAxes, displaySteps, false);
    }
    
    /**
     * The constructor for the coordinate Axes.
     *
     * @param axesRadius     The radius of the axes.
     * @param displayXAxis   Whether or not to display the x Axis.
     * @param displayYAxis   Whether or not to display the y Axis.
     * @param displayZAxis   Whether or not to display the z Axis.
     * @param displaySubAxes Whether or not to display the sub Axes.
     */
    public Axes(int axesRadius, boolean displayXAxis, boolean displayYAxis, boolean displayZAxis, boolean displaySubAxes) {
        this(axesRadius, displayXAxis, displayYAxis, displayZAxis, displaySubAxes, false);
    }
    
    /**
     * The constructor for the coordinate Axes.
     *
     * @param axesRadius   The radius of the axes.
     * @param displayXAxis Whether or not to display the x Axis.
     * @param displayYAxis Whether or not to display the y Axis.
     * @param displayZAxis Whether or not to display the z Axis.
     */
    public Axes(int axesRadius, boolean displayXAxis, boolean displayYAxis, boolean displayZAxis) {
        this(axesRadius, displayXAxis, displayYAxis, displayZAxis, false);
    }
    
    /**
     * The constructor for the coordinate Axes.
     *
     * @param axesRadius The radius of the Axes.
     */
    public Axes(int axesRadius) {
        this(axesRadius, true, true, true);
    }
    
    
    //Methods
    
    /**
     * Calculates the Objects for the Camera Object.
     */
    @Override
    protected void calculate() {
        Color xAxisColor = Color.RED;
        Color yAxisColor = Color.GREEN;
        Color zAxisColor = Color.BLUE;
        Color subAxesColor = new Color(128, 128, 128, 32);
        Color stepColor = new Color(64, 64, 64, 64);
        Color textColor = Color.BLACK;
        String stepFormat = ((int) stepValue == stepValue) ? "%.0f" : "%.2f";
        
        if (displayXAxis || preload) {
            Edge xAxisLine = new Edge(this, xAxisColor,
                    new Vector(-axesRadius, 0, 0),
                    new Vector(axesRadius, 0, 0));
            xAxis.getComponents().add(xAxisLine);
            xAxis.getComponents().add(new Text(textColor, xAxisLine.getV1().plus(new Vector(-step, 0, 0)), "-X"));
            xAxis.getComponents().add(new Text(textColor, xAxisLine.getV2().plus(new Vector(step, 0, 0)), "X"));
            xAxis.setVisible(displayXAxis);
            components.add(xAxis);
            
            if (displayXSubAxes || preload) {
                for (double x = 0; x <= axesRadius; x += step) {
                    xSubAxes.getComponents().add(new graphy.object.base.simple.Edge(this, subAxesColor,
                            new Vector(x, -axesRadius, 0),
                            new Vector(x, axesRadius, 0)));
                    if (x != -x) {
                        xSubAxes.getComponents().add(new graphy.object.base.simple.Edge(this, subAxesColor,
                                new Vector(-x, -axesRadius, 0),
                                new Vector(-x, axesRadius, 0)));
                    }
                }
                xSubAxes.setVisible(displayXSubAxes);
                xAxis.getComponents().add(xSubAxes);
            }
            
            if (displayXSteps || preload) {
                for (double x = step; x <= axesRadius; x += step) {
                    xSteps.getComponents().add(new graphy.object.base.simple.Edge(this, stepColor,
                            new Vector(x, -step / 4, 0),
                            new Vector(x, step / 4, 0)));
                    xSteps.getComponents().add(new Text(textColor, new Vector(x, 0, 0), String.format(stepFormat, x / step * stepValue)));
                    if (x != -x) {
                        xSteps.getComponents().add(new graphy.object.base.simple.Edge(this, stepColor,
                                new Vector(-x, -step / 4, 0),
                                new Vector(-x, step / 4, 0)));
                        xSteps.getComponents().add(new Text(textColor, new Vector(-x, 0, 0), String.format(stepFormat, -x / step * stepValue)));
                    }
                }
                xSteps.setVisible(displayXSteps);
                xAxis.getComponents().add(xSteps);
            }
        }
        
        if (displayYAxis || preload) {
            Edge yAxisLine = new Edge(this, yAxisColor,
                    new Vector(0, -axesRadius, 0),
                    new Vector(0, axesRadius, 0));
            yAxis.getComponents().add(yAxisLine);
            yAxis.getComponents().add(new Text(textColor, yAxisLine.getV1().plus(new Vector(0, -step, 0)), "-Y"));
            yAxis.getComponents().add(new Text(textColor, yAxisLine.getV2().plus(new Vector(0, step, 0)), "Y"));
            yAxis.setVisible(displayYAxis);
            components.add(yAxis);
            
            if (displayYSubAxes || preload) {
                for (double y = 0; y <= axesRadius; y += step) {
                    ySubAxes.getComponents().add(new graphy.object.base.simple.Edge(this, subAxesColor,
                            new Vector(-axesRadius, y, 0),
                            new Vector(axesRadius, y, 0)));
                    if (y != -y) {
                        ySubAxes.getComponents().add(new graphy.object.base.simple.Edge(this, subAxesColor,
                                new Vector(-axesRadius, -y, 0),
                                new Vector(axesRadius, -y, 0)));
                    }
                }
                ySubAxes.setVisible(displayYSubAxes);
                yAxis.getComponents().add(ySubAxes);
            }
            
            if (displayYSteps || preload) {
                for (double y = step; y <= axesRadius; y += step) {
                    ySteps.getComponents().add(new graphy.object.base.simple.Edge(this, stepColor,
                            new Vector(-step / 4, y, 0),
                            new Vector(step / 4, y, 0)));
                    ySteps.getComponents().add(new Text(textColor, new Vector(0, y, 0), String.format(stepFormat, y / step * stepValue)));
                    if (y != -y) {
                        ySteps.getComponents().add(new graphy.object.base.simple.Edge(this, stepColor,
                                new Vector(-step / 4, -y, 0),
                                new Vector(step / 4, -y, 0)));
                        ySteps.getComponents().add(new Text(textColor, new Vector(0, -y, 0), String.format(stepFormat, -y / step * stepValue)));
                    }
                }
                ySteps.setVisible(displayYSteps);
                yAxis.getComponents().add(ySteps);
            }
        }
        
        if (displayZAxis || preload) {
            Edge zAxisLine = new Edge(this, zAxisColor,
                    new Vector(0, 0, -axesRadius),
                    new Vector(0, 0, axesRadius));
            zAxis.getComponents().add(zAxisLine);
            zAxis.getComponents().add(new Text(textColor, zAxisLine.getV1().plus(new Vector(0, 0, -step)), "-Z"));
            zAxis.getComponents().add(new Text(textColor, zAxisLine.getV2().plus(new Vector(0, 0, step)), "Z"));
            zAxis.setVisible(displayZAxis);
            components.add(zAxis);
            
            if (displayZSubAxes || preload) {
                for (double z = 0; z <= axesRadius; z += step) {
                    zSubAxes.getComponents().add(new graphy.object.base.simple.Edge(this, subAxesColor,
                            new Vector(0, -axesRadius, z),
                            new Vector(0, axesRadius, z)));
                    zSubAxes.getComponents().add(new graphy.object.base.simple.Edge(this, subAxesColor,
                            new Vector(-axesRadius, 0, z),
                            new Vector(axesRadius, 0, z)));
                    if (z != -z) {
                        zSubAxes.getComponents().add(new graphy.object.base.simple.Edge(this, subAxesColor,
                                new Vector(0, -axesRadius, -z),
                                new Vector(0, axesRadius, -z)));
                        zSubAxes.getComponents().add(new graphy.object.base.simple.Edge(this, subAxesColor,
                                new Vector(-axesRadius, 0, -z),
                                new Vector(axesRadius, 0, -z)));
                    }
                }
                for (double x = 0; x <= axesRadius; x += step) {
                    zSubAxes.getComponents().add(new graphy.object.base.simple.Edge(this, subAxesColor,
                            new Vector(x, 0, -axesRadius),
                            new Vector(x, 0, axesRadius)));
                    if (x != -x) {
                        zSubAxes.getComponents().add(new graphy.object.base.simple.Edge(this, subAxesColor,
                                new Vector(-x, 0, -axesRadius),
                                new Vector(-x, 0, axesRadius)));
                    }
                }
                for (double y = 0; y <= axesRadius; y += step) {
                    zSubAxes.getComponents().add(new graphy.object.base.simple.Edge(this, subAxesColor,
                            new Vector(0, y, -axesRadius),
                            new Vector(0, y, axesRadius)));
                    if (y != -y) {
                        zSubAxes.getComponents().add(new graphy.object.base.simple.Edge(this, subAxesColor,
                                new Vector(0, -y, -axesRadius),
                                new Vector(0, -y, axesRadius)));
                    }
                }
                zSubAxes.setVisible(displayZSubAxes);
                zAxis.getComponents().add(zSubAxes);
            }
            
            if (displayZSteps || preload) {
                for (double z = step; z <= axesRadius; z += step) {
                    zSteps.getComponents().add(new graphy.object.base.simple.Edge(this, stepColor,
                            new Vector(-step / 4, 0, z),
                            new Vector(step / 4, 0, z)));
                    zSteps.getComponents().add(new graphy.object.base.simple.Edge(this, stepColor,
                            new Vector(0, -step / 4, z),
                            new Vector(0, step / 4, z)));
                    zSteps.getComponents().add(new Text(textColor, new Vector(0, 0, z), String.format(stepFormat, z / step * stepValue)));
                    if (z != -z) {
                        zSteps.getComponents().add(new graphy.object.base.simple.Edge(this, stepColor,
                                new Vector(-step / 4, 0, -z),
                                new Vector(step / 4, 0, -z)));
                        zSteps.getComponents().add(new graphy.object.base.simple.Edge(this, stepColor,
                                new Vector(0, -step / 4, -z),
                                new Vector(0, step / 4, -z)));
                        zSteps.getComponents().add(new Text(textColor, new Vector(0, 0, -z), String.format(stepFormat, -z / step * stepValue)));
                    }
                }
                zSteps.setVisible(displayZSteps);
                zAxis.getComponents().add(zSteps);
            }
        }
        
        setVisible(true);
    }
    
}
