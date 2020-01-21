/*
 * File:    Camera.java
 * Package: camera
 * Author:  Zachary Gill
 */

package camera;

import main.Environment;
import math.Delta;
import math.vector.Vector;
import math.vector.Vector3;
import objects.base.Scene;
import utility.SphericalCoordinateUtility;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Defines the functionality of a Camera.
 */
public class Camera {
    
    //Constants
    
    /**
     * The maximum distance from the phi maximum and minimum.
     */
    public static final double phiBoundary = .0001;
    
    /**
     * The maximum distance for rho in first person perspective.
     */
    public static final double rhoBoundary = .0001;
    
    
    //Enums
    
    /**
     * An enumeration of camera perspectives
     */
    public enum Perspective {
        THIRD_PERSON(1),
        FIRST_PERSON(-1);
        
        private int scale;
        
        Perspective(int scale) {
            this.scale = scale;
        }
        
        public int getScale() {
            return scale;
        }
    }
    
    
    //Static Fields
    
    /**
     * The Scene the camera is viewing.
     */
    private static Scene scene;
    
    /**
     * The x dimension of the viewport.
     */
    private static double viewportX = Environment.MAX_SCREEN_X / 1000.0;
    
    /**
     * The y dimension of the viewport.
     */
    private static double viewportY = Environment.MAX_SCREEN_Y / 1000.0;
    
    /**
     * The next Camera id to be used.
     */
    private static int nextCameraId = 0;
    
    /**
     * The current active Camera for viewing.
     */
    private static int activeCameraView = -1;
    
    /**
     * The current active Camera for controls.
     */
    private static int activeCameraControl = -1;
    
    /**
     * The map of Cameras that are registered in the Environment.
     */
    private static final Map<Integer, Camera> cameraMap = new HashMap<>();
    
    /**
     * The active Camera view in the Environment.
     */
    private static Camera activeView = null;
    
    /**
     * The active Camera control in the Environment.
     */
    private static Camera activeControl = null;
    
    /**
     * Whether the static KeyListener has been set up or not.
     */
    private static AtomicBoolean hasSetupStaticKeyListener = new AtomicBoolean(false);
    
    
    //Fields
    
    /**
     * The id of the Camera.
     */
    private int cameraId;
    
    /**
     * The Camera Object rendered in the Environment.
     */
    private objects.system.Camera cameraObject = new objects.system.Camera();
    
    /**
     * The current phi location of the Camera in spherical coordinates.
     */
    private double phi = Math.PI;
    
    /**
     * The current theta location of the Camera in spherical coordinates.
     */
    private double theta = Math.PI / 2;
    
    /**
     * The current rho location of the Camera in spherical coordinates.
     */
    private double rho = 10;
    
    /**
     * The rho location of the Camera saved when switching between first and third person perspectives.
     */
    private double switchRho = 10;
    
    /**
     * The position of the Camera.
     */
    private Vector c;
    
    /**
     * The offset of the Camera.
     */
    private Vector offset;
    
    /**
     * The current phi movement speed of the Camera.
     */
    private double phiSpeed = .02;
    
    /**
     * The current theta movement speed of the Camera.
     */
    private double thetaSpeed = .02;
    
    /**
     * The current rho movement speed of the Camera.
     */
    private double rhoSpeed = 1;
    
    /**
     * The current origin movement speed of the Camera.
     */
    private double movementSpeed = .05;
    
    /**
     * A flag indicating whether or not the Camera should operate in pan mode.
     */
    private boolean panMode = false;
    
    /**
     * The perspective mode of the Camera.
     */
    private Perspective perspective = Perspective.THIRD_PERSON;
    
    /**
     * The angle of view of the Camera.
     */
    private double angleOfView = 90;
    
    /**
     * The heading Vector of the Camera.
     */
    private Vector heading;
    
    /**
     * The normal unit Vector of the Screen.
     */
    private Vector n;
    
    /**
     * The position of the center of the Screen.
     */
    private Vector m;
    
    /**
     * The Vector of coefficients from the scalar equation of the Screen for projections.
     */
    private Vector e;
    
    /**
     * The Vector of coefficients from the scalar equation of the Screen plane.
     */
    private Vector p;
    
    /**
     * The first point that defines the Screen viewport.
     */
    private Vector s1;
    
    /**
     * The second point that defines the Screen viewport.
     */
    private Vector s2;
    
    /**
     * The third point that defines the Screen viewport.
     */
    private Vector s3;
    
    /**
     * The fourth point that defines the Screen viewport.
     */
    private Vector s4;
    
    /**
     * The timer for running Camera calculations.
     */
    private Timer timer;
    
    /**
     * Whether an update is required or not.
     */
    private boolean updateRequired = true;
    
    /**
     * Whether to verify the viewport dimensions or not.
     */
    private boolean verifyViewport = false;
    
    /**
     * A flag indicating whether the camera is performing an update or not.
     */
    public final AtomicBoolean inUpdate = new AtomicBoolean(false);
    
    
    //Constructors
    
    /**
     * The constructor for a Camera.
     */
    public Camera(Scene scene, boolean cameraControls, boolean cameraMovement) {
        this.cameraId = nextCameraId;
        nextCameraId++;
        cameraMap.put(cameraId, this);
        
        Camera.scene = scene;
        viewportX = Environment.screenX / 1000.0;
        viewportY = Environment.screenY / 1000.0;
        
        offset = new Vector(0, 0, 0);
        
        calculateCamera();
        
        if ((activeCameraView == -1) || (activeCameraControl == -1)) {
            setActiveCamera(cameraId);
        }
        scene.registerComponent(cameraObject);
        
        if (cameraMovement) {
            setupKeyListener();
            setupMouseListener();
        }
        if (cameraControls) {
            setupStaticKeyListener();
        }
        
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                calculateCamera();
            }
        }, 500 / Environment.fps, 1000 / Environment.fps);
    }
    
    
    //Methods
    
    /**
     * Calculates the Camera.
     */
    public void calculateCamera() {
        if (!updateRequired) {
            return;
        }
        
        synchronized (inUpdate) {
            if (!inUpdate.compareAndSet(false, true)) {
                return;
            }
            
            
            //cartesian camera location
            Vector cartesian = SphericalCoordinateUtility.sphericalToCartesian(phi, theta, rho);
            
            
            //center of screen, m
            Vector cameraOrigin = Environment.origin.plus(offset).justify();
            m = cartesian.plus(cameraOrigin);
            
            
            //normal unit vector of screen, n
            n = cartesian.normalize();
            
            
            //heading vector
            heading = cartesian.times(new Vector(1, 1, 0)).normalize();
            
            
            //position camera behind screen a distance, h
            double h = viewportX * Math.tan(Math.toRadians(90 - (angleOfView / 2))) / 2;
            if (perspective == Perspective.FIRST_PERSON) {
                c = m;
                m = c.minus(n.scale(h));
            } else {
                c = m.plus(n.scale(h));
            }
            cameraObject.camera.setPoint(c.justify());
            cameraObject.setCenter(c.justify());
            
            
            //vector equation of plane of screen
            //(r - m) dot n = 0
            //n.x(x - m.x) + n.y(y - m.y) + n.z(z - m.z) = 0
            
            
            //satisfy equation to determine second point
            double p2x = cameraOrigin.getX();
            double p2y = cameraOrigin.getY();
            Vector p2 = new Vector(p2x, p2y,
                    ((n.getX() * (p2x - m.getX()) + (n.getY() * (p2y - m.getY())) - (n.getZ() * m.getZ()))) / -n.getZ());
            
            
            //calculate local coordinate system
            Vector ly = (phi > Math.PI / 2) ? p2.minus(m) : m.minus(p2);
            Vector lx = new Vector3(ly).cross(n).scale(-1);
            lx = lx.normalize();
            ly = ly.normalize();
            
            
            //calculate screen viewport
            s1 = lx.scale(-viewportX / 2).plus(ly.scale(-viewportY / 2)).plus(m);
            s2 = lx.scale(viewportX / 2).plus(ly.scale(-viewportY / 2)).plus(m);
            s3 = lx.scale(viewportX / 2).plus(ly.scale(viewportY / 2)).plus(m);
            s4 = lx.scale(-viewportX / 2).plus(ly.scale(viewportY / 2)).plus(m);
            
            if (perspective == Perspective.FIRST_PERSON) {
                Vector tmp = s1.clone();
                s1 = s3;
                s3 = tmp;
                tmp = s2;
                s2 = s4;
                s4 = tmp;
            }
            
            cameraObject.screen.setP1(s1.justify());
            cameraObject.screen.setP2(s2.justify());
            cameraObject.screen.setP3(s3.justify());
            cameraObject.screen.setP4(s4.justify());
            
            
            //find scalar equation of screen
            //e.x*x + e.y*y + e.z*z = 1            
            double d = n.dot(m);
            e = new Vector(n.getX(), n.getY(), n.getZ()).scale(1.0 / d);
            
            
            //draw local coordinate system normals
            cameraObject.screenNormal.setPoints(c.justify(), c.plus(n.scale(viewportX * 2 / 3)).justify());
            cameraObject.screenXNormal.setPoints(c.justify(), c.plus(lx.scale(viewportX * 2 / 3)).justify());
            cameraObject.screenYNormal.setPoints(c.justify(), c.minus(ly.scale(viewportX * 2 / 3)).justify());
            
            
            //draw camera enclosure
            cameraObject.cameraEnclosure.setComponents(cameraObject.screen, c.justify());
            cameraObject.cameraEnclosure.setColor(new Color(192, 192, 192, 64));
            cameraObject.cameraEnclosure.setFaceColor(5, Color.RED);
            
            
            //verify screen
            if (verifyViewport) {
                if (Math.abs(viewportX - s1.distance(s2)) > Environment.OMEGA) {
                    System.err.println("Camera: " + cameraId + " - Screen viewportX does not match the actual viewport width");
                }
                if (Math.abs(viewportY - s1.distance(s3)) > Environment.OMEGA) {
                    System.err.println("Camera: " + cameraId + " - Screen viewportY does not match the actual viewport height");
                }
            }
            
            
            //update has been performed
            updateRequired = false;
            inUpdate.set(false);
        }
    }
    
    /**
     * Projects a Vector to the viewport of the Camera.
     *
     * @param v The Vector to project.
     * @return The projected Vector.
     */
    public Vector projectVector(Vector v) {
        //equation of plane of screen
        //e.x*v.x + e.y*v.y + e.z*v.z = 1
        
        
        //plug vector v into equation
        Vector keq = c.minus(v);
        double keqk = e.dot(keq);
        double keqc = 1 - e.dot(v);
        double k = keqc / keqk;
        
        
        //solve projection
        return keq.scale(k).plus(v);
    }
    
    /**
     * Collapses a Vector to the viewport of the Camera and determines if it is visible.
     *
     * @param v The Vector, will be updated with its relative coordinates on the viewport.
     * @return Whether the Vector is visible on the Screen or not.
     */
    public Vector collapseVector(Vector v) {
        //perform pre-calculations
        Vector s1v = v.minus(s1);
        Vector s4v = v.minus(s4);
        Vector s1s2 = s2.minus(s1);
        Vector s1s4 = s4.minus(s1);
        double w = s1.distance(s2);
        double h = s1.distance(s4);
        double s1vh = s1v.hypotenuse();
        
        
        //find screen angles
        double x = (s1s2.times(s1v)).sum() / (w * s1vh);
        double y = (s1s4.times(s1v)).sum() / (h * s1vh);
        
        
        //determine true screen coordinates
        double d = (v.minus(s1)).hypotenuse();
        double m = x * d;
        double n = y * d;
        return new Vector(m, n, 0);
    }
    
    /**
     * Sets this Camera as the active camera.
     */
    public void setAsActiveCamera() {
        setActiveCamera(cameraId);
    }
    
    /**
     * Sets this Camera as the active camera for viewing.
     */
    public void setAsActiveViewCamera() {
        setActiveCameraView(cameraId);
    }
    
    /**
     * Sets this Camera as the active camera for controls.
     */
    public void setAsActiveControlCamera() {
        setActiveCameraControl(cameraId);
    }
    
    /**
     * Removes the Camera.
     */
    public void removeCamera() {
        timer.purge();
        timer.cancel();
        cameraObject.setVisible(false);
        scene.unregisterComponent(cameraObject);
        cameraMap.remove(cameraId);
    }
    
    /**
     * Adds a KeyListener for the Camera controls.
     */
    private void setupKeyListener() {
        final Set<Integer> pressed = new HashSet<>();
        
        scene.environment.frame.addKeyListener(new KeyListener() {
            
            @Override
            public void keyTyped(KeyEvent e) {
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                if (cameraId != activeCameraControl) {
                    return;
                }
                
                synchronized (pressed) {
                    pressed.add(e.getKeyCode());
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                if (cameraId != activeCameraControl) {
                    return;
                }
                
                synchronized (pressed) {
                    pressed.remove(e.getKeyCode());
                }
            }
            
        });
        
        Timer keyListenerThread = new Timer();
        keyListenerThread.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (cameraId != activeCameraControl) {
                    return;
                }
                
                synchronized (pressed) {
                    double oldPhi = phi;
                    double oldTheta = theta;
                    double oldRho = rho;
                    Vector oldOrigin = Environment.origin.clone();
                    
                    Vector headingMovement = heading.scale(movementSpeed);
                    Vector perpendicularMovement = new Vector3(0, 0, 1).cross(heading).scale(movementSpeed);
                    
                    for (Integer key : pressed) {
                        if (!panMode) {
                            if (key == KeyEvent.VK_W) {
                                phi -= phiSpeed * perspective.getScale();
                            }
                            if (key == KeyEvent.VK_S) {
                                phi += phiSpeed * perspective.getScale();
                            }
                            if (key == KeyEvent.VK_A) {
                                theta -= thetaSpeed * perspective.getScale();
                            }
                            if (key == KeyEvent.VK_D) {
                                theta += thetaSpeed * perspective.getScale();
                            }
                            if (key == KeyEvent.VK_Q) {
                                rho -= rhoSpeed;
                            }
                            if (key == KeyEvent.VK_Z) {
                                rho += rhoSpeed;
                            }
                        }
                        
                        if (key == KeyEvent.VK_LEFT) {
                            Environment.origin = Environment.origin.plus(perpendicularMovement);
                        }
                        if (key == KeyEvent.VK_RIGHT) {
                            Environment.origin = Environment.origin.plus(perpendicularMovement.scale(-1));
                        }
                        if (key == KeyEvent.VK_UP) {
                            Environment.origin = Environment.origin.plus(headingMovement);
                        }
                        if (key == KeyEvent.VK_DOWN) {
                            Environment.origin = Environment.origin.plus(headingMovement.scale(-1));
                        }
                    }
                    
                    if (phi != oldPhi || theta != oldTheta || rho != oldRho || !Environment.origin.equals(oldOrigin)) {
                        bindLocation();
                        updateRequired = true;
                    }
                }
            }
        }, 0, 20);
    }
    
    /**
     * Adds a MouseListener for the Camera controls.
     */
    private void setupMouseListener() {
        final Delta delta = new Delta();
        final AtomicInteger button = new AtomicInteger(0);
        
        scene.environment.frame.getContentPane().getComponent(0).addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                button.set(e.getButton());
                delta.x = e.getX();
                delta.y = e.getY();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                
            }
        });
        
        scene.environment.frame.getContentPane().getComponent(0).addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (cameraId != activeCameraControl) {
                    return;
                }
                
                if (!panMode) {
                    double deltaX = e.getX() - delta.x;
                    double deltaY = e.getY() - delta.y;
                    delta.x = e.getX();
                    delta.y = e.getY();
                    
                    double oldTheta = theta;
                    double oldPhi = phi;
                    
                    theta -= (thetaSpeed / 4 * deltaX) / (perspective == Perspective.FIRST_PERSON ? 4 : 1);
                    phi -= (phiSpeed / 4 * deltaY) / (perspective == Perspective.FIRST_PERSON ? 4 : 1);
                    bindLocation();
                    
                    if (phi != oldPhi || theta != oldTheta) {
                        updateRequired = true;
                    }
                    
                } else if (button.get() == MouseEvent.BUTTON3) {
                    double deltaX = e.getX() - delta.x;
                    double deltaY = e.getY() - delta.y;
                    delta.x = e.getX();
                    delta.y = e.getY();
                    
                    Environment.origin = Environment.origin.plus(new Vector(-deltaX, deltaY, 0).scale(rho / 1000));
                    updateRequired = true;
                }
            }
            
            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
        
        scene.environment.frame.getContentPane().getComponent(0).addMouseWheelListener(e -> {
            if (cameraId != activeCameraControl) {
                return;
            }
            
            double oldRho = rho;
            
            rho += rhoSpeed * e.getWheelRotation();
            bindLocation();
            
            if (rho != oldRho) {
                updateRequired = true;
            }
        });
    }
    
    /**
     * Adds a fluid movement transition for the Camera over a period of time.
     *
     * @param phiMovement   The phi angle to move over the period.
     * @param thetaMovement The phi angle to move over the period.
     * @param rhoMovement   The phi angle to move over the period.
     * @param period        The period over which to perform the movement in milliseconds.
     */
    public void addFluidTransition(double phiMovement, double thetaMovement, double rhoMovement, long period) {
        Timer transitionTimer = new Timer();
        transitionTimer.scheduleAtFixedRate(new TimerTask() {
            private Vector movementVector = new Vector(phiMovement, thetaMovement, rhoMovement);
            
            private Vector totalMovement = new Vector(0, 0, 0);
            
            private long timeCount = 0;
            
            private long lastTime;
            
            @Override
            public void run() {
                if (lastTime == 0) {
                    lastTime = System.currentTimeMillis();
                    return;
                }
                
                long currentTime = System.currentTimeMillis();
                long timeElapsed = currentTime - lastTime;
                lastTime = currentTime;
                timeCount += timeElapsed;
                
                if (timeCount >= period) {
                    moveCamera(movementVector.minus(totalMovement));
                    transitionTimer.purge();
                    transitionTimer.cancel();
                } else {
                    double scale = (double) timeElapsed / period;
                    Vector movementFrame = movementVector.scale(scale);
                    moveCamera(movementFrame);
                    totalMovement = totalMovement.plus(movementFrame);
                }
                
            }
        }, 0, (long) (1000.0 / Environment.fps / 2));
    }
    
    /**
     * Binds the location of the camera to its limits.
     */
    public void bindLocation() {
        if (phi < phiBoundary) {
            phi = phiBoundary;
        } else if (phi > Math.PI - phiBoundary) {
            phi = Math.PI - phiBoundary;
        }
        
        if (theta > (Math.PI * 2)) {
            theta -= (Math.PI * 2);
        } else if (theta < 0) {
            theta = (Math.PI * 2) + theta;
        }
        
        if (perspective == Perspective.THIRD_PERSON) {
            if (rho < rhoSpeed) {
                rho = rhoSpeed;
            }
        } else if (perspective == Perspective.FIRST_PERSON) {
            rho = rhoBoundary;
        }
    }
    
    
    //Getters
    
    /**
     * Returns the Camera position.
     *
     * @return The Camera position.
     */
    public Vector getCameraPosition() {
        return c.clone();
    }
    
    /**
     * Returns the perspective mode of the Camera.
     *
     * @return The perspective mode of the Camera.
     */
    public Perspective getPerspective() {
        return perspective;
    }
    
    /**
     * Returns the angle of view of the Camera.
     *
     * @return The angle of view of the Camera.
     */
    public double getAngleOfView() {
        return angleOfView;
    }
    
    /**
     * Returns the heading Vector of the Camera.
     *
     * @return The heading Vector of the Camera.
     */
    public Vector getHeading() {
        return heading.clone();
    }
    
    
    //Setters
    
    /**
     * Sets the location of the Camera.
     *
     * @param phi   The new phi angle of the Camera.
     * @param theta The new theta angle of the Camera.
     * @param rho   The new rho distance of the Camera.
     */
    public void setLocation(double phi, double theta, double rho) {
        this.phi = phi;
        this.theta = theta;
        this.rho = rho;
        
        bindLocation();
        updateRequired = true;
    }
    
    /**
     * Sets the phi location of the Camera.
     *
     * @param phi The new phi angle of the Camera.
     */
    public void setPhi(double phi) {
        this.phi = phi;
        
        bindLocation();
        updateRequired = true;
    }
    
    /**
     * Sets the theta location of the Camera.
     *
     * @param theta The new theta angle of the Camera.
     */
    public void setTheta(double theta) {
        this.theta = theta;
        
        bindLocation();
        updateRequired = true;
    }
    
    /**
     * Sets the rho location of the Camera.
     *
     * @param rho The new rho distance of the Camera.
     */
    public void setRho(double rho) {
        this.rho = rho;
        
        bindLocation();
        updateRequired = true;
    }
    
    /**
     * Moves the Camera in a certain direction.
     *
     * @param offset The relative offsets to move the Camera.
     */
    public void moveCamera(Vector offset) {
        setLocation(phi + offset.getX(), theta + offset.getY(), rho + offset.getZ());
    }
    
    /**
     * Sets the offset of the Camera.
     *
     * @param offset The offsets of the Camera.
     */
    public void setOffset(Vector offset) {
        this.offset = offset.clone();
        
        updateRequired = true;
    }
    
    /**
     * Translates the Camera in the x direction.
     *
     * @param xOffset The relative x offset to translate the Camera.
     */
    public void translateCameraX(double xOffset) {
        translateCamera(new Vector(xOffset, 0, 0));
    }
    
    /**
     * Translates the Camera in the y direction.
     *
     * @param yOffset The relative y offset to translate the Camera.
     */
    public void translateCameraY(double yOffset) {
        translateCamera(new Vector(0, yOffset, 0));
    }
    
    /**
     * Translates the Camera in the z direction.
     *
     * @param zOffset The relative z offset to translate the Camera.
     */
    public void translateCameraZ(double zOffset) {
        translateCamera(new Vector(0, 0, zOffset));
    }
    
    /**
     * Translates the Camera in a certain direction.
     *
     * @param offset The relative offsets to translate the Camera.
     */
    public void translateCamera(Vector offset) {
        setOffset(this.offset.plus(offset));
    }
    
    /**
     * Sets the Camera to pan mode or not.
     *
     * @param panMode The flag indicating whether or not the Camera should be in pan mode.
     */
    public void setPanMode(boolean panMode) {
        this.panMode = panMode;
    }
    
    /**
     * Sets the perspective mode of the Camera.
     *
     * @param perspective The perspective mode of the Camera.
     */
    public void setPerspective(Perspective perspective) {
        this.perspective = perspective;
        
        if (perspective == Perspective.THIRD_PERSON) {
            rho = switchRho;
        } else if (perspective == Perspective.FIRST_PERSON) {
            switchRho = rho;
            rho = rhoBoundary;
        }
        updateRequired = true;
    }
    
    /**
     * Sets the angle of view of the Camera.
     *
     * @param angleOfView The angle of view of the Camera.
     */
    public void setAngleOfView(double angleOfView) {
        this.angleOfView = angleOfView;
        
        updateRequired = true;
    }
    
    
    //Functions
    
    /**
     * Projects the Vectors to the active Camera view.
     *
     * @param vs The list of Vectors to project.
     */
    public static void projectVectorsToCamera(List<Vector> vs) {
        if (activeView == null) {
            return;
        }
        
        for (int i = 0; i < vs.size(); i++) {
            vs.set(i, activeView.projectVector(vs.get(i)));
        }
    }
    
    /**
     * Collapses the Vectors to the viewport.
     *
     * @param vs The list of Vector to be prepared for rendering.
     */
    public static void collapseVectorsToViewport(List<Vector> vs) {
        if (activeView == null) {
            return;
        }
        
        for (int i = 0; i < vs.size(); i++) {
            vs.set(i, activeView.collapseVector(vs.get(i)));
        }
    }
    
    /**
     * Determines if any Vectors are behind the Screen.
     *
     * @param vs The list of Vectors.
     * @return Whether any of the Vectors are behind the the Screen or not.
     */
    public static boolean hasVectorBehindScreen(Vector[] vs) {
        if (activeView == null) {
            return false;
        }
        Vector cm = activeView.m.justify();
        Vector cc = activeView.c.justify();
        
        //ensure Vectors are not behind Camera
        boolean behind = false;
        for (Vector v : vs) {
            double d1 = cm.distance(v);
            double d2 = cc.distance(v);
            
            if (d2 <= d1) {
                behind = true;
                break;
            }
        }
        return behind;
    }
    
    /**
     * Determines if any Vectors are visible on the Screen.
     *
     * @param vs The list of Vectors.
     * @return Whether any of the Vectors are visible on the Screen or not.
     */
    public static boolean hasVectorInView(List<Vector> vs) {
        //ensure Vectors are in field of view
        boolean inView = false;
        for (Vector v : vs) {
            if ((v.getX() >= 0) && (v.getX() < viewportX) &&
                (v.getY() >= 0) && (v.getY() < viewportY)) {
                inView = true;
                break;
            }
        }
        return inView;
    }
    
    /**
     * Scales the Vectors to the screen to be drawn.
     *
     * @param vs The list of Vectors to scale.
     */
    public static void scaleVectorsToScreen(List<Vector> vs) {
        Vector viewportDim = getActiveViewportDim();
        Vector scale = new Vector(
                Environment.screenX / viewportDim.getX(),
                Environment.screenY / viewportDim.getY(),
                Environment.screenZ
        );
        
        for (int i = 0; i < vs.size(); i++) {
            vs.set(i, (vs.get(i).times(scale)).round());
        }
    }
    
    /**
     * Returns the active Camera for viewing.
     *
     * @return The active Camera for viewing.
     */
    public static Camera getActiveCameraView() {
        return cameraMap.get(activeCameraView);
    }
    
    /**
     * Returns the active Camera for control.
     *
     * @return The active Camera for control.
     */
    public static Camera getActiveCameraControl() {
        return cameraMap.get(activeCameraControl);
    }
    
    /**
     * Sets the active Camera.
     *
     * @param cameraId The id of the new active Camera.
     */
    public static void setActiveCamera(int cameraId) {
        setActiveCameraView(cameraId);
        setActiveCameraControl(cameraId);
    }
    
    /**
     * Sets the active Camera for viewing.
     *
     * @param cameraId The id of the new active Camera for viewing.
     */
    public static void setActiveCameraView(int cameraId) {
        if (cameraMap.containsKey(cameraId) && (cameraId != activeCameraView)) {
            if (activeCameraView >= 0) {
                cameraMap.get(activeCameraView).cameraObject.show();
                cameraMap.get(activeCameraView).updateRequired = true;
            }
            activeCameraView = cameraId;
            cameraMap.get(activeCameraView).cameraObject.hide();
            cameraMap.get(activeCameraView).updateRequired = true;
            
            activeView = cameraMap.get(activeCameraView);
        }
    }
    
    /**
     * Sets the active Camera for control.
     *
     * @param cameraId The id of the new active Camera for control.
     */
    public static void setActiveCameraControl(int cameraId) {
        if (cameraMap.containsKey(cameraId) && (cameraId != activeCameraControl)) {
            activeCameraControl = cameraId;
            
            activeControl = cameraMap.get(activeCameraControl);
        }
    }
    
    /**
     * Returns the viewport dimensions of the active Camera.
     *
     * @return The viewport dimensions of the active Camera.
     */
    public static Vector getActiveViewportDim() {
        return new Vector(viewportX, viewportY);
    }
    
    /**
     * Adds the static KeyListener for the Camera system controls.
     */
    private static void setupStaticKeyListener() {
        if (!hasSetupStaticKeyListener.compareAndSet(false, true)) {
            return;
        }
        
        scene.environment.frame.addKeyListener(new KeyListener() {
            
            @Override
            public void keyTyped(KeyEvent e) {
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                
                if (key == KeyEvent.VK_1 || key == KeyEvent.VK_NUMPAD1) {
                    setActiveCamera(0);
                }
                if (key == KeyEvent.VK_2 || key == KeyEvent.VK_NUMPAD2) {
                    setActiveCamera(1);
                }
                if (key == KeyEvent.VK_3 || key == KeyEvent.VK_NUMPAD3) {
                    setActiveCamera(2);
                }
                if (key == KeyEvent.VK_4 || key == KeyEvent.VK_NUMPAD4) {
                    setActiveCameraView(0);
                }
                if (key == KeyEvent.VK_5 || key == KeyEvent.VK_NUMPAD5) {
                    setActiveCameraView(1);
                }
                if (key == KeyEvent.VK_6 || key == KeyEvent.VK_NUMPAD6) {
                    setActiveCameraView(2);
                }
                if (key == KeyEvent.VK_7 || key == KeyEvent.VK_NUMPAD7) {
                    setActiveCameraControl(0);
                }
                if (key == KeyEvent.VK_8 || key == KeyEvent.VK_NUMPAD8) {
                    setActiveCameraControl(1);
                }
                if (key == KeyEvent.VK_9 || key == KeyEvent.VK_NUMPAD9) {
                    setActiveCameraControl(2);
                }
                
                if (key == KeyEvent.VK_E) {
                    if (activeControl == null) {
                        return;
                    }
                    
                    if (activeControl.perspective == Perspective.THIRD_PERSON) {
                        activeControl.setPerspective(Perspective.FIRST_PERSON);
                    } else if (activeControl.perspective == Perspective.FIRST_PERSON) {
                        activeControl.setPerspective(Perspective.THIRD_PERSON);
                    }
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
            }
            
        });
    }
    
}
