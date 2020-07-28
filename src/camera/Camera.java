/*
 * File:    Camera.java
 * Package: camera
 * Author:  Zachary Gill
 */

package camera;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import main.Environment;
import math.Delta;
import math.vector.Vector;
import math.vector.Vector3;
import objects.base.BaseObject;
import objects.base.ObjectInterface;
import objects.base.Scene;
import objects.base.polygon.Rectangle;
import utility.SphericalCoordinateUtility;

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
     * The next Camera id to be used.
     */
    private static int nextCameraId = 0;
    
    /**
     * The map of Cameras that are registered per perspective.
     */
    private static final Map<UUID, Map<Integer, Camera>> cameraMap = new HashMap<>();
    
    /**
     * The map of current active Camera ids for viewing per perspective.
     */
    private static Map<UUID, Integer> activeCameraView = new HashMap<>();
    
    /**
     * The map of current active Camera ids for controls per perspective.
     */
    private static Map<UUID, Integer> activeCameraControl = new HashMap<>();
    
    /**
     * The map of active Cameras for viewing per perspective.
     */
    private static Map<UUID, Camera> activeView = new HashMap<>();
    
    /**
     * The map of active Cameras for controls per perspective.
     */
    private static Map<UUID, Camera> activeControl = new HashMap<>();
    
    /**
     * The map of the Scenes the Camera is viewing per perspective.
     */
    private static Map<UUID, Scene> scenes = new HashMap<>();
    
    /**
     * The map of the dimensions of the viewport per perspective.
     */
    private static Map<UUID, Vector> viewports = new HashMap<>();
    
    /**
     * The map of the dimensions of the screen per perspective.
     */
    private static Map<UUID, Vector> screenSizes = new HashMap<>();
    
    /**
     * Whether the static KeyListener has been set up or not per perspective.
     */
    private static Map<UUID, AtomicBoolean> hasSetupStaticKeyListener = new HashMap<>();
    
    
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
     * The perspective of the Camera.
     */
    private UUID perspective;
    
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
    private Perspective mode = Perspective.THIRD_PERSON;
    
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
     * The set of Vectors that define the Screen viewport.
     */
    private Rectangle s;
    
    /**
     * The normal Vector of a plane in which the direction of the Vector indicates the side of the plane in which to render Objects, or null.
     */
    private Vector normalLimit;
    
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
     *
     * @param scene          The scene the Camera is viewing.
     * @param perspective    The perspective for the Camera.
     * @param viewport       The dimensions for the viewport for the Camera.
     * @param screenSize     The dimensions for the screen size for the Camera.
     * @param cameraControls A flag indicating whether or not to allow controls for the Camera.
     * @param cameraMovement A flag indicating whether or not to allow movement for the Camera.
     */
    public Camera(Scene scene, UUID perspective, Vector viewport, Vector screenSize, boolean cameraControls, boolean cameraMovement) {
        this.cameraId = nextCameraId;
        nextCameraId++;
        cameraMap.putIfAbsent(perspective, new LinkedHashMap<>());
        cameraMap.get(perspective).put(cameraId, this);
        this.perspective = perspective;
        
        scenes.putIfAbsent(perspective, scene);
        viewports.putIfAbsent(perspective, viewport);
        screenSizes.putIfAbsent(perspective, screenSize);
        
        this.offset = new Vector(0, 0, 0);
        
        calculateCamera(perspective);
        
        activeCameraView.putIfAbsent(perspective, -1);
        activeCameraControl.putIfAbsent(perspective, -1);
        activeView.putIfAbsent(perspective, null);
        activeControl.putIfAbsent(perspective, null);
        if ((activeCameraView.get(perspective) == -1) || (activeCameraControl.get(perspective) == -1)) {
            setActiveCamera(perspective, cameraId);
        }
        if (scenes.get(perspective) != null) {
            scenes.get(perspective).registerComponent(cameraObject);
        }
        
        if (cameraMovement) {
            setupKeyListener();
            setupMouseListener();
        }
        
        hasSetupStaticKeyListener.putIfAbsent(perspective, new AtomicBoolean(false));
        if (cameraControls) {
            setupStaticKeyListener(perspective);
        }
        
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                calculateCamera(perspective);
            }
        }, 500 / Environment.fps, 1000 / Environment.fps);
    }
    
    /**
     * The constructor for a Camera.
     *
     * @param scene          The scene the Camera is viewing.
     * @param perspective    The perspective for the Camera.
     * @param cameraControls A flag indicating whether or not to allow controls for the Camera.
     * @param cameraMovement A flag indicating whether or not to allow movement for the Camera.
     */
    public Camera(Scene scene, UUID perspective, boolean cameraControls, boolean cameraMovement) {
        this(scene, perspective, new Vector(Environment.width / 1000.0, Environment.height / 1000.0), new Vector(Environment.screenX, Environment.screenY, Environment.screenZ), cameraControls, cameraMovement);
    }
    
    
    //Methods
    
    /**
     * Calculates the Camera.
     *
     * @param perspective The perspective for the Camera.
     */
    public void calculateCamera(UUID perspective) {
        if (!updateRequired) {
            return;
        }
        
        synchronized (inUpdate) {
            if (!inUpdate.compareAndSet(false, true)) {
                return;
            }
            
            
            //cartesian camera location
            Vector cartesian = SphericalCoordinateUtility.sphericalToCartesian(phi, theta, rho);
            Vector viewport = getViewport(perspective);
            
            
            //center of screen, m
            Vector cameraOrigin = Environment.origin.plus(offset).justify();
            m = cartesian.plus(cameraOrigin);
            
            
            //normal unit vector of screen, n
            n = cartesian.normalize();
            
            
            //heading vector
            heading = cartesian.times(new Vector(1, 1, 0)).normalize();
            
            
            //position camera behind screen a distance, h
            double h = viewport.getX() * Math.tan(Math.toRadians(90 - (angleOfView / 2))) / 2;
            if (mode == Perspective.FIRST_PERSON) {
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
            s = new Rectangle(
                    lx.scale(-viewport.getX() / 2).plus(ly.scale(-viewport.getY() / 2)).plus(m),
                    lx.scale(viewport.getX() / 2).plus(ly.scale(-viewport.getY() / 2)).plus(m),
                    lx.scale(viewport.getX() / 2).plus(ly.scale(viewport.getY() / 2)).plus(m),
                    lx.scale(-viewport.getX() / 2).plus(ly.scale(viewport.getY() / 2)).plus(m));
            
            if (mode == Perspective.FIRST_PERSON) {
                Vector tmp = s.getP1().clone();
                s.setP1(s.getP3());
                s.setP3(tmp);
                tmp = s.getP2();
                s.setP2(s.getP4());
                s.setP4(tmp);
            }
            
            cameraObject.screen.setP1(s.getP1().justify());
            cameraObject.screen.setP2(s.getP2().justify());
            cameraObject.screen.setP3(s.getP3().justify());
            cameraObject.screen.setP4(s.getP4().justify());
            
            
            //find scalar equation of screen
            //e.x*x + e.y*y + e.z*z = 1            
            double d = n.dot(m);
            e = new Vector(n.getX(), n.getY(), n.getZ()).scale(1.0 / d);
            
            
            //draw local coordinate system normals
            cameraObject.screenNormal.setPoints(c.justify(), c.plus(n.scale(viewport.getX() * 2 / 3)).justify());
            cameraObject.screenXNormal.setPoints(c.justify(), c.plus(lx.scale(viewport.getX() * 2 / 3)).justify());
            cameraObject.screenYNormal.setPoints(c.justify(), c.minus(ly.scale(viewport.getX() * 2 / 3)).justify());
            
            
            //draw camera enclosure
            cameraObject.cameraEnclosure.setComponents(cameraObject.screen, c.justify());
            cameraObject.cameraEnclosure.setColor(new Color(192, 192, 192, 64));
            cameraObject.cameraEnclosure.setFaceColor(5, Color.RED);
            
            
            //verify screen
            if (verifyViewport) {
                if (Math.abs(viewport.getX() - s.getP1().distance(s.getP2())) > Environment.OMEGA) {
                    System.err.println("Camera: " + cameraId + " - Screen viewportX does not match the actual viewport width");
                }
                if (Math.abs(viewport.getY() - s.getP1().distance(s.getP3())) > Environment.OMEGA) {
                    System.err.println("Camera: " + cameraId + " - Screen viewportY does not match the actual viewport height");
                }
            }
            
            
            //update has been performed
            updateRequired = false;
            inUpdate.set(false);
        }
    }
    
    /**
     * Renders the Scene through the Camera.
     *
     * @param g2 The 2D Graphics entity.
     */
    public void render(Graphics2D g2) {
        synchronized (inUpdate) {
            Scene scene = scenes.get(perspective);
            if (scene == null) {
                return;
            }
            
            List<BaseObject> preparedBases = new ArrayList<>();
            try {
                for (ObjectInterface object : scene.getComponents()) {
                    preparedBases.addAll(object.doPrepare(perspective));
                }
            } catch (ConcurrentModificationException ignored) {
                return;
            }
            
            if (normalLimit != null) {
                preparedBases.removeIf(e -> normalLimit.dot(e.getCenter()) < 0);
            }
            
            preparedBases.sort((o1, o2) -> Double.compare(o2.getRenderDistance(), o1.getRenderDistance()));
            
            scene.environment.colorBackground(g2);
            
            for (BaseObject preparedBase : preparedBases) {
                preparedBase.doRender(g2, perspective);
            }
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
        Vector s1v = v.minus(s.getP1());
        Vector s4v = v.minus(s.getP4());
        Vector s1s2 = s.getP2().minus(s.getP1());
        Vector s1s4 = s.getP4().minus(s.getP1());
        double w = s.getP1().distance(s.getP2());
        double h = s.getP1().distance(s.getP4());
        double s1vh = s1v.hypotenuse();
        
        
        //find screen angles
        double x = (s1s2.times(s1v)).sum() / (w * s1vh);
        double y = (s1s4.times(s1v)).sum() / (h * s1vh);
        
        
        //determine true screen coordinates
        double d = (v.minus(s.getP1())).hypotenuse();
        double m = x * d;
        double n = y * d;
        return new Vector(m, n, 0);
    }
    
    /**
     * Sets this Camera as the active camera.
     */
    public void setAsActiveCamera() {
        setActiveCamera(perspective, cameraId);
    }
    
    /**
     * Sets this Camera as the active camera for viewing.
     */
    public void setAsActiveViewCamera() {
        setActiveCameraView(perspective, cameraId);
    }
    
    /**
     * Sets this Camera as the active camera for controls.
     */
    public void setAsActiveControlCamera() {
        setActiveCameraControl(perspective, cameraId);
    }
    
    /**
     * Removes the Camera.
     */
    public void removeCamera() {
        timer.purge();
        timer.cancel();
        cameraObject.setVisible(false);
        if (scenes.get(perspective) != null) {
            scenes.get(perspective).unregisterComponent(cameraObject);
        }
        cameraMap.remove(cameraId);
    }
    
    /**
     * Sets the visibility of the Camera.
     *
     * @param visible The new visibility of the Camera.
     */
    public void setVisible(boolean visible) {
        cameraObject.setVisible(visible);
    }
    
    /**
     * Adds a KeyListener for the Camera controls.
     */
    private void setupKeyListener() {
        final Set<Integer> pressed = new HashSet<>();
        
        if (scenes.get(perspective) != null) {
            scenes.get(perspective).environment.frame.addKeyListener(new KeyListener() {
                
                @Override
                public void keyTyped(KeyEvent e) {
                }
                
                @Override
                public void keyPressed(KeyEvent e) {
                    if (cameraId != activeCameraControl.get(perspective)) {
                        return;
                    }
                    
                    synchronized (pressed) {
                        pressed.add(e.getKeyCode());
                    }
                }
                
                @Override
                public void keyReleased(KeyEvent e) {
                    if (cameraId != activeCameraControl.get(perspective)) {
                        return;
                    }
                    
                    synchronized (pressed) {
                        pressed.remove(e.getKeyCode());
                    }
                }
                
            });
        }
        
        Timer keyListenerThread = new Timer();
        keyListenerThread.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (cameraId != activeCameraControl.get(perspective)) {
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
                                phi -= phiSpeed * mode.getScale();
                            }
                            if (key == KeyEvent.VK_S) {
                                phi += phiSpeed * mode.getScale();
                            }
                            if (key == KeyEvent.VK_A) {
                                theta -= thetaSpeed * mode.getScale();
                            }
                            if (key == KeyEvent.VK_D) {
                                theta += thetaSpeed * mode.getScale();
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
        
        if (scenes.get(perspective) == null) {
            return;
        }
        
        scenes.get(perspective).environment.renderPanel.addMouseListener(new MouseListener() {
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
        
        scenes.get(perspective).environment.renderPanel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (cameraId != activeCameraControl.get(perspective)) {
                    return;
                }
                
                if (!panMode) {
                    double deltaX = e.getX() - delta.x;
                    double deltaY = e.getY() - delta.y;
                    delta.x = e.getX();
                    delta.y = e.getY();
                    
                    double oldTheta = theta;
                    double oldPhi = phi;
                    
                    theta -= (thetaSpeed / 4 * deltaX) / (mode == Perspective.FIRST_PERSON ? 4 : 1);
                    phi -= (phiSpeed / 4 * deltaY) / (mode == Perspective.FIRST_PERSON ? 4 : 1);
                    bindLocation();
                    
                    if (phi != oldPhi || theta != oldTheta) {
                        updateRequired = true;
                    }
                    
                } else if (button.get() == MouseEvent.BUTTON3) {
                    double deltaX = e.getX() - delta.x;
                    double deltaY = e.getY() - delta.y;
                    delta.x = e.getX();
                    delta.y = e.getY();
                    
                    Environment.origin = Environment.origin.plus(new Vector(deltaX, deltaY, 0).justify().scale(rho / 1000));
                    updateRequired = true;
                }
            }
            
            @Override
            public void mouseMoved(MouseEvent e) {
            }
            
        });
        
        scenes.get(perspective).environment.renderPanel.addMouseWheelListener(e -> {
            if (cameraId != activeCameraControl.get(perspective)) {
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
        
        if (mode == Perspective.THIRD_PERSON) {
            if (rho < rhoSpeed) {
                rho = rhoSpeed;
            }
        } else if (mode == Perspective.FIRST_PERSON) {
            rho = rhoBoundary;
        }
    }
    
    /**
     * Fits the Camera to a particular Object.
     *
     * @param o The Object.
     */
    public void fitToObject(BaseObject o) {
        o.prepare(perspective);
        List<Vector> bounds = o.getPrepared(perspective);
        projectVectorsToCamera(perspective, bounds);
        collapseVectorsToViewport(perspective, bounds);
        
        if (!hasAllVectorsInView(perspective, bounds)) {
            do {
                setAngleOfView(getAngleOfView() / 0.99);
                setScreenSize(perspective, getScreenSize(perspective).scale(1 / 0.99));
                calculateCamera(perspective);
                o.prepare(perspective);
                bounds = o.getPrepared(perspective);
                projectVectorsToCamera(perspective, bounds);
                collapseVectorsToViewport(perspective, bounds);
            } while (!hasAllVectorsInView(perspective, bounds));
            
        } else {
            do {
                setAngleOfView(getAngleOfView() * 0.99);
                setScreenSize(perspective, getScreenSize(perspective).scale(0.99));
                calculateCamera(perspective);
                o.prepare(perspective);
                bounds = o.getPrepared(perspective);
                projectVectorsToCamera(perspective, bounds);
                collapseVectorsToViewport(perspective, bounds);
            } while (hasAllVectorsInView(perspective, bounds));
            
            setAngleOfView(getAngleOfView() / 0.99);
            setScreenSize(perspective, getScreenSize(perspective).scale(1 / 0.99));
            calculateCamera(perspective);
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
     * Returns the Camera location.
     *
     * @return The Camera location.
     */
    public Vector getLocation() {
        return new Vector(phi, theta, rho);
    }
    
    /**
     * Returns the perspective mode of the Camera.
     *
     * @return The perspective mode of the Camera.
     */
    public Perspective getMode() {
        return mode;
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
     * Sets the location of the Camera.
     *
     * @param location The location of the Camera in spherical coordinates.
     */
    public void setLocation(Vector location) {
        setLocation(location.getX(), location.getY(), location.getZ());
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
        if (panMode) {
            setLocation(Math.PI, Math.PI / 2, rho);
        }
    }
    
    /**
     * Sets the perspective mode of the Camera.
     *
     * @param mode The perspective mode of the Camera.
     */
    public void setMode(Perspective mode) {
        this.mode = mode;
        
        if (mode == Perspective.THIRD_PERSON) {
            rho = switchRho;
        } else if (mode == Perspective.FIRST_PERSON) {
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
    
    /**
     * Sets the normal limit of the Camera.
     *
     * @param normalLimit The normal limit of the Camera.
     */
    public void setNormalLimit(Vector normalLimit) {
        this.normalLimit = normalLimit;
        
        updateRequired = true;
    }
    
    
    //Functions
    
    /**
     * Renders the Scene with the active Camera for that Scene.
     *
     * @param perspective The perspective to render the Scene for.
     * @param g2          The 2D Graphics entity.
     */
    public static void doRender(UUID perspective, Graphics2D g2) {
        if (activeView.get(perspective) == null) {
            return;
        }
        
        activeView.get(perspective).render(g2);
    }
    
    /**
     * Projects the Vectors to the active Camera view.
     *
     * @param perspective The perspective to project the Vectors for.
     * @param vs          The list of Vectors to project.
     */
    public static void projectVectorsToCamera(UUID perspective, List<Vector> vs) {
        if (activeView.get(perspective) == null) {
            return;
        }
        
        for (int i = 0; i < vs.size(); i++) {
            vs.set(i, activeView.get(perspective).projectVector(vs.get(i)));
        }
    }
    
    /**
     * Collapses the Vectors to the viewport.
     *
     * @param perspective The perspective to collapse the Vectors for.
     * @param vs          The list of Vector to be prepared for rendering.
     */
    public static void collapseVectorsToViewport(UUID perspective, List<Vector> vs) {
        if (activeView.get(perspective) == null) {
            return;
        }
        
        for (int i = 0; i < vs.size(); i++) {
            vs.set(i, activeView.get(perspective).collapseVector(vs.get(i)));
        }
    }
    
    /**
     * Determines if any Vectors are behind the Screen.
     *
     * @param perspective The perspective to determine if any Vectors are behind the screen for.
     * @param vs          The list of Vectors.
     * @return Whether any of the Vectors are behind the the Screen or not.
     */
    public static boolean hasVectorBehindScreen(UUID perspective, Vector[] vs) {
        if (activeView.get(perspective) == null) {
            return false;
        }
        Vector cm = activeView.get(perspective).m.justify();
        Vector cc = activeView.get(perspective).c.justify();
        
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
     * @param perspective The perspective to determine if any Vectors are visible on the screen for.
     * @param vs          The list of Vectors.
     * @return Whether any of the Vectors are visible on the Screen or not.
     */
    public static boolean hasVectorInView(UUID perspective, List<Vector> vs) {
        Vector viewportDim = getViewport(perspective);
        //ensure Vectors are in field of view
        boolean inView = false;
        for (Vector v : vs) {
            if ((v.getX() >= 0) && (v.getX() < viewportDim.getX()) &&
                    (v.getY() >= 0) && (v.getY() < viewportDim.getY())) {
                inView = true;
                break;
            }
        }
        return inView;
    }
    
    /**
     * Determines if all Vectors are visible on the Screen.
     *
     * @param perspective The perspective to determine if any Vectors are visible on the screen for.
     * @param vs          The list of Vectors.
     * @return Whether all of the Vectors are visible on the Screen or not.
     */
    public static boolean hasAllVectorsInView(UUID perspective, List<Vector> vs) {
        Vector viewportDim = getViewport(perspective);
        //ensure Vectors are in field of view
        boolean inView = true;
        for (Vector v : vs) {
            if (!((v.getX() >= 0) && (v.getX() < viewportDim.getX()) &&
                    (v.getY() >= 0) && (v.getY() < viewportDim.getY()))) {
                inView = false;
                break;
            }
        }
        return inView;
    }
    
    /**
     * Scales the Vectors to the screen to be drawn.
     *
     * @param perspective The perspective to scale the Vectors to the screen for.
     * @param vs          The list of Vectors to scale.
     */
    public static void scaleVectorsToScreen(UUID perspective, List<Vector> vs) {
        Vector screenDim = getScreenSize(perspective);
        Vector viewportDim = getViewport(perspective);
        Vector scale = new Vector(
                screenDim.getX() / viewportDim.getX(),
                screenDim.getY() / viewportDim.getY(),
                screenDim.getZ()
        );
        
        for (int i = 0; i < vs.size(); i++) {
            vs.set(i, (vs.get(i).times(scale)).round());
        }
    }
    
    /**
     * Returns the active Camera for viewing.
     *
     * @param perspective The perspective to get the active Camera view for.
     * @return The active Camera for viewing.
     */
    public static Camera getActiveCameraView(UUID perspective) {
        if (cameraMap.containsKey(perspective) && activeCameraView.containsKey(perspective)) {
            return cameraMap.get(perspective).get(activeCameraView.get(perspective));
        }
        return null;
    }
    
    /**
     * Returns the active Camera for control.
     *
     * @param perspective The perspective to get the active Camera control for.
     * @return The active Camera for control.
     */
    public static Camera getActiveCameraControl(UUID perspective) {
        if (cameraMap.containsKey(perspective) && activeCameraControl.containsKey(perspective)) {
            return cameraMap.get(perspective).get(activeCameraControl.get(perspective));
        }
        return null;
    }
    
    /**
     * Sets the active Camera.
     *
     * @param perspective The perspective to set the active Camera for.
     * @param cameraId    The id of the new active Camera.
     */
    public static void setActiveCamera(UUID perspective, int cameraId) {
        setActiveCameraView(perspective, cameraId);
        setActiveCameraControl(perspective, cameraId);
    }
    
    /**
     * Sets the active Camera for viewing.
     *
     * @param perspective The perspective to set the active Camera view for.
     * @param cameraId    The id of the new active Camera for viewing.
     */
    public static void setActiveCameraView(UUID perspective, int cameraId) {
        if (cameraMap.containsKey(perspective) && cameraMap.get(perspective).containsKey(cameraId) && (cameraId != activeCameraView.get(perspective))) {
            if (activeCameraView.get(perspective) >= 0) {
                cameraMap.get(perspective).get(activeCameraView.get(perspective)).cameraObject.show();
                cameraMap.get(perspective).get(activeCameraView.get(perspective)).updateRequired = true;
            }
            activeCameraView.put(perspective, cameraId);
            cameraMap.get(perspective).get(activeCameraView.get(perspective)).cameraObject.hide();
            cameraMap.get(perspective).get(activeCameraView.get(perspective)).updateRequired = true;
            
            activeView.put(perspective, cameraMap.get(perspective).get(activeCameraView.get(perspective)));
        }
    }
    
    /**
     * Sets the active Camera for control.
     *
     * @param perspective The perspective to set the active Camera control for.
     * @param cameraId    The id of the new active Camera for control.
     */
    public static void setActiveCameraControl(UUID perspective, int cameraId) {
        if (cameraMap.containsKey(perspective) && cameraMap.get(perspective).containsKey(cameraId) && (cameraId != activeCameraControl.get(perspective))) {
            activeCameraControl.put(perspective, cameraId);
            
            activeControl.put(perspective, cameraMap.get(perspective).get(activeCameraControl));
        }
    }
    
    /**
     * Returns the Scene for a perspective.
     *
     * @param perspective The perspective to get the Scene for.
     * @return The Scene.
     */
    public static Scene getScene(UUID perspective) {
        if (scenes.containsKey(perspective)) {
            return scenes.get(perspective);
        }
        return null;
    }
    
    /**
     * Returns the viewport for a perspective.
     *
     * @param perspective The perspective to get the viewport for.
     * @return The viewport.
     */
    public static Vector getViewport(UUID perspective) {
        if (viewports.containsKey(perspective)) {
            return viewports.get(perspective);
        }
        return new Vector(0, 0);
    }
    
    /**
     * Returns the screen size for a perspective.
     *
     * @param perspective The perspective to get the screen size for.
     * @return The screen size.
     */
    public static Vector getScreenSize(UUID perspective) {
        if (screenSizes.containsKey(perspective)) {
            return screenSizes.get(perspective);
        }
        return new Vector(0, 0, 0);
    }
    
    /**
     * Sets the Scene for a perspective.
     *
     * @param perspective The perspective to set the Scene for.
     * @param scene       The Scene.
     */
    public static void setScene(UUID perspective, Scene scene) {
        if (scenes.containsKey(perspective)) {
            scenes.put(perspective, scene);
        }
    }
    
    /**
     * Sets the viewport dimensions for a perspective.
     *
     * @param perspective The perspective to set the viewport dimensions for.
     * @param viewport    The viewport dimensions.
     */
    public static void setViewport(UUID perspective, Vector viewport) {
        if (viewports.containsKey(perspective)) {
            viewports.put(perspective, viewport);
            Camera perspectiveView = getActiveCameraView(perspective);
            if (perspectiveView != null) {
                perspectiveView.updateRequired = true;
            }
        }
    }
    
    /**
     * Sets the screen dimensions for a perspective.
     *
     * @param perspective The perspective to set the screen dimensions for.
     * @param screenSize  The screen dimensions.
     */
    public static void setScreenSize(UUID perspective, Vector screenSize) {
        if (screenSizes.containsKey(perspective)) {
            screenSizes.put(perspective, screenSize);
            Camera perspectiveView = getActiveCameraView(perspective);
            if (perspectiveView != null) {
                perspectiveView.updateRequired = true;
            }
        }
    }
    
    /**
     * Adds the static KeyListener for the Camera system controls.
     *
     * @param perspective The perspective to set the static KeyListener for.
     */
    private static void setupStaticKeyListener(UUID perspective) {
        if (!hasSetupStaticKeyListener.get(perspective).compareAndSet(false, true)) {
            return;
        }
        
        if (scenes.get(perspective) == null) {
            return;
        }
        
        scenes.get(perspective).environment.frame.addKeyListener(new KeyListener() {
            
            @Override
            public void keyTyped(KeyEvent e) {
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                
                if ((key >= KeyEvent.VK_1 && key <= KeyEvent.VK_9) || (key >= KeyEvent.VK_NUMPAD1 && key <= KeyEvent.VK_NUMPAD9)) {
                    List<Camera> cs = Arrays.asList(cameraMap.get(perspective).values().toArray(new Camera[] {}));
                    
                    if (cs.size() > 0) {
                        if (key == KeyEvent.VK_1 || key == KeyEvent.VK_NUMPAD1) {
                            setActiveCamera(perspective, cs.get(0).cameraId);
                        }
                        if (key == KeyEvent.VK_4 || key == KeyEvent.VK_NUMPAD4) {
                            setActiveCameraView(perspective, cs.get(0).cameraId);
                        }
                        if (key == KeyEvent.VK_7 || key == KeyEvent.VK_NUMPAD7) {
                            setActiveCameraControl(perspective, cs.get(0).cameraId);
                        }
                    }
                    if (cs.size() > 1) {
                        if (key == KeyEvent.VK_2 || key == KeyEvent.VK_NUMPAD2) {
                            setActiveCamera(perspective, cs.get(1).cameraId);
                        }
                        if (key == KeyEvent.VK_5 || key == KeyEvent.VK_NUMPAD5) {
                            setActiveCameraView(perspective, cs.get(1).cameraId);
                        }
                        if (key == KeyEvent.VK_8 || key == KeyEvent.VK_NUMPAD8) {
                            setActiveCameraControl(perspective, cs.get(1).cameraId);
                        }
                    }
                    if (cs.size() > 2) {
                        if (key == KeyEvent.VK_3 || key == KeyEvent.VK_NUMPAD3) {
                            setActiveCamera(perspective, cs.get(2).cameraId);
                        }
                        if (key == KeyEvent.VK_6 || key == KeyEvent.VK_NUMPAD6) {
                            setActiveCameraView(perspective, cs.get(2).cameraId);
                        }
                        if (key == KeyEvent.VK_9 || key == KeyEvent.VK_NUMPAD9) {
                            setActiveCameraControl(perspective, cs.get(2).cameraId);
                        }
                    }
                }
                
                if (key == KeyEvent.VK_E) {
                    if (activeControl.get(perspective) == null) {
                        return;
                    }
                    
                    if (activeControl.get(perspective).getMode() == Perspective.THIRD_PERSON) {
                        activeControl.get(perspective).setMode(Perspective.FIRST_PERSON);
                    } else if (activeControl.get(perspective).getMode() == Perspective.FIRST_PERSON) {
                        activeControl.get(perspective).setMode(Perspective.THIRD_PERSON);
                    }
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
            }
            
        });
    }
    
}
