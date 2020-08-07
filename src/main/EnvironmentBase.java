/*
 * File:    EnvironmentBase.java
 * Package: main
 * Author:  Zachary Gill
 */

package main;

import java.awt.AlphaComposite;
import java.awt.BufferCapabilities;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.ImageCapabilities;
import java.awt.LayoutManager2;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import camera.CaptureHandler;
import math.vector.Vector;
import utility.ScreenUtility;

/**
 * The base of the main Environment.
 */
public abstract class EnvironmentBase {
    
    //Constants
    
    /**
     * The maximum number of frames to render per second.
     */
    public static final int MAX_FPS = 60;
    
    /**
     * The display width of the screen.
     */
    private static final int DISPLAY_WIDTH = ScreenUtility.DISPLAY_WIDTH;
    
    /**
     * The display height of the screen.
     */
    private static final int DISPLAY_HEIGHT = ScreenUtility.DISPLAY_HEIGHT;
    
    /**
     * The maximum width of the Window.
     */
    public static final int MAX_SCREEN_WIDTH = DISPLAY_WIDTH - ((DISPLAY_WIDTH % 2 == 0) ? 0 : 1);
    
    /**
     * The maximum height of the Window.
     */
    public static final int MAX_SCREEN_HEIGHT = DISPLAY_HEIGHT - ((DISPLAY_HEIGHT % 2 == 0) ? 0 : 1);
    
    /**
     * The maximum depth of the Window.
     */
    public static final int MAX_SCREEN_DEPTH = 720;
    
    /**
     * The origin of the Environment.
     */
    public static final Vector ORIGIN = new Vector(0, 0, 0);
    
    /**
     * The acceptable rounding error for double precision.
     */
    public static final double OMEGA = 0.0000001;
    
    
    //Static Fields
    
    /**
     * A flag indicating whether or not an Environment has been instanced yet or not.
     */
    protected static AtomicBoolean instanced = new AtomicBoolean(false);
    
    /**
     * The number of frames to render per second.
     */
    public static int fps = MAX_FPS;
    
    /**
     * The width of the Window.
     */
    public static int screenWidth = MAX_SCREEN_WIDTH;
    
    /**
     * The height of the Window.
     */
    public static int screenHeight = MAX_SCREEN_HEIGHT;
    
    /**
     * The depth of the Window.
     */
    public static int screenDepth = MAX_SCREEN_DEPTH;
    
    /**
     * The width of the Render Panel.
     */
    public static int width = MAX_SCREEN_WIDTH;
    
    /**
     * The height of the Render Panel.
     */
    public static int height = MAX_SCREEN_HEIGHT;
    
    /**
     * The coordinates to center the Environment at.
     */
    public static Vector origin = ORIGIN.clone();
    
    /**
     * The Environment time in milliseconds.
     */
    private static long time = 0;
    
    /**
     * A list of Tasks to be run after each frame of the Environment.
     */
    private static final Map<UUID, Task> tasks = new ConcurrentHashMap<>();
    
    /**
     * A list of Tasks to be run during the shutdown of the Environment.
     */
    private static final Map<UUID, Task> shutdownTasks = new ConcurrentHashMap<>();
    
    /**
     * A flag indicating whether or not to record the entire session.
     */
    private static final boolean recordSession = false;
    
    
    //Fields
    
    /**
     * The Frame of the Window.
     */
    public JFrame frame;
    
    /**
     * The Canvas to render the Environment in.
     */
    public Canvas renderPanel;
    
    /**
     * The Layout Manager to use for the Window.
     */
    public LayoutManager2 layout;
    
    /**
     * A flag indicating whether or not to use double buffering.
     */
    private boolean doubleBuffering;
    
    /**
     * The buffer populated for captures and recordings.
     */
    public final Queue<BufferedImage> buffer = new LinkedBlockingQueue<>();
    
    /**
     * The background color of the Environment.
     */
    public Color background;
    
    /**
     * The capture handler for the Environment.
     */
    protected CaptureHandler captureHandler;
    
    /**
     * A flag indicating whether or not the main KeyListener has been set up.
     */
    protected AtomicBoolean hasSetupMainKeyListener = new AtomicBoolean(false);
    
    /**
     * A flag indicating whether or not the Environment is currently rendering.
     */
    protected final AtomicBoolean rendering = new AtomicBoolean(false);
    
    
    //Methods
    
    /**
     * Sets up the Environment.
     */
    public final void setup() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(layout);
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
        
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown, "ShutdownHook"));
        
        // panel to display render results
        renderPanel = new Canvas() {
            
            /**
             * Updates the Canvas.
             *
             * @param g The Graphics.
             */
            @Override
            public void update(Graphics g) {
                paint(g);
            }
            
            /**
             * Paints the Canvas.
             *
             * @param g The Graphics.
             */
            @Override
            public void paint(Graphics g) {
                if (doubleBuffering) {
                    paintWithDoubleBuffer(g);
                } else {
                    paintWithoutDoubleBuffer(g);
                }
            }
            
            /**
             * Paints the Canvas with double buffering.
             *
             * @param g The Graphics.
             */
            public void paintWithDoubleBuffer(Graphics g) {
                BufferStrategy bufferStrategy = getBufferStrategy();
                if (bufferStrategy == null) {
                    try {
                        BufferCapabilities capabilities = new BufferCapabilities(new ImageCapabilities(true), new ImageCapabilities(true), BufferCapabilities.FlipContents.COPIED);
                        renderPanel.createBufferStrategy(2, capabilities);
                    } catch (Exception ignored) {
                        renderPanel.createBufferStrategy(2);
                    }
                    return;
                }
                
                do {
                    do {
                        Graphics2D g2 = null;
                        Graphics2D g2b = null;
                        try {
                            g2 = (Graphics2D) bufferStrategy.getDrawGraphics();
                            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                            if (captureHandler.needsBuffer()) {
                                BufferedImage bufferImage = new BufferedImage(renderPanel.getWidth(), renderPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
                                g2b = bufferImage.createGraphics();
                                print(g2b);
                                g2b.dispose();
                                g2b = null;
                                buffer.add(bufferImage);
                                g2.drawImage(bufferImage, 0, 0, null);
                            } else {
                                buffer.clear();
                                print(g2);
                            }
                        } catch (Exception ignored) {
                        } finally {
                            if (g2 != null) {
                                g2.dispose();
                            }
                            if (g2b != null) {
                                g2b.dispose();
                            }
                        }
                    } while (bufferStrategy.contentsRestored());
                    bufferStrategy.show();
                } while (bufferStrategy.contentsLost());
            }
            
            /**
             * Paints the Canvas without double buffering.
             *
             * @param g The Graphics.
             */
            public void paintWithoutDoubleBuffer(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                Graphics2D g2b = null;
                try {
                    if (captureHandler.needsBuffer()) {
                        BufferedImage bufferImage = new BufferedImage(renderPanel.getWidth(), renderPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
                        g2b = bufferImage.createGraphics();
                        print(g2b);
                        g2b.dispose();
                        g2b = null;
                        buffer.add(bufferImage);
                        g2.drawImage(bufferImage, 0, 0, null);
                    } else {
                        buffer.clear();
                        print(g2);
                    }
                } catch (Exception ignored) {
                } finally {
                    if (g2b != null) {
                        g2b.dispose();
                    }
                }
            }
            
            /**
             * Prints the Environment to the Canvas.
             *
             * @param g The Graphics.
             */
            @Override
            public void print(Graphics g) {
                render((Graphics2D) g);
            }
            
        };
        renderPanel.setBackground(Color.BLACK);
        renderPanel.setFocusable(false);
        frame.getContentPane().add(renderPanel);
        
        setSize(screenWidth, screenHeight, width, height);
        
        frame.pack();
        frame.setVisible(true);
        
        captureHandler = new CaptureHandler(this);
    }
    
    /**
     * Runs the Environment.
     */
    public final void run() {
        if (fps == 0) {
            renderPanel.repaint();
            
        } else {
            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
                if (rendering.compareAndSet(false, true)) {
                    try {
                        if (captureHandler.needsBuffer()) {
                            SwingUtilities.invokeAndWait(() -> renderPanel.repaint());
                        } else {
                            renderPanel.repaint();
                        }
                        runTasks();
                    } catch (Exception ignored) {
                    }
                    rendering.set(false);
                }
            }, 0, 1000 / fps, TimeUnit.MILLISECONDS);
            
            if (recordSession) {
                captureHandler.recordingListener();
            }
        }
    }
    
    /**
     * Shuts down the Environment.
     */
    public final void shutdown() {
        captureHandler.shutdown();
        shutdownTasks.values().stream().filter(e -> e.active.get()).forEach(e -> e.action.run());
    }
    
    /**
     * Runs the Tasks in the Environment.
     */
    private void runTasks() {
        time += (1000 / fps);
        tasks.values().stream().filter(e -> e.active.get()).forEach(e -> e.action.run());
    }
    
    /**
     * Renders the Environment.
     *
     * @param g2 The 2D Graphics entity.
     */
    protected abstract void render(Graphics2D g2);
    
    /**
     * Sizes the Window.
     */
    protected void sizeWindow() {
        renderPanel.setSize(new Dimension(width, height));
        renderPanel.setPreferredSize(new Dimension(renderPanel.getSize()));
        frame.setSize(new Dimension(screenWidth + ScreenUtility.BORDER_WIDTH, screenHeight + ScreenUtility.BORDER_HEIGHT));
        frame.setPreferredSize(frame.getSize());
        
        if ((screenWidth == MAX_SCREEN_WIDTH) && (screenHeight == MAX_SCREEN_HEIGHT)) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            frame.setExtendedState(JFrame.NORMAL);
        }
    }
    
    /**
     * Colors the background of the Environment.
     *
     * @param g2 The 2D Graphics entity.
     */
    public void colorBackground(Graphics2D g2) {
        if (background != null) {
            Color cacheColor = g2.getColor();
            g2.setColor(background);
            g2.fillRect(0, 0, renderPanel.getWidth(), renderPanel.getHeight());
            g2.setColor(cacheColor);
        }
    }
    
    /**
     * Sets up the KeyListener for the main Environment controls.
     */
    public final void setupMainKeyListener() {
        if (!hasSetupMainKeyListener.compareAndSet(false, true)) {
            return;
        }
        addMainKeyListener();
    }
    
    /**
     * Adds the KeyListener for the main Environment controls.
     */
    protected void addMainKeyListener() {
        frame.addKeyListener(new KeyListener() {
            
            @Override
            public void keyTyped(KeyEvent e) {
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                
                if (key == KeyEvent.VK_C) {
                    captureHandler.captureListener(e.isControlDown());
                }
                if (key == KeyEvent.VK_V) {
                    if (!recordSession && (fps > 0)) {
                        captureHandler.recordingListener();
                    }
                }
                if (key == KeyEvent.VK_B) {
                    captureHandler.openCaptureDir();
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
            }
            
        });
    }
    
    
    //Setters
    
    /**
     * Sets the number of frames to render per second.
     *
     * @param fps The number of frames to render per second.
     */
    public void setFps(int fps) {
        EnvironmentBase.fps = Math.min(fps, MAX_FPS);
    }
    
    /**
     * Sets the dimensions of the Window and the Render Panel.
     *
     * @param screenWidth  The width of the Window.
     * @param screenHeight The height of the Window.
     * @param width        The width of the Render Panel.
     * @param height       The height of the Render Panel.
     */
    public void setSize(int screenWidth, int screenHeight, int width, int height) {
        if (screenWidth > 0 && screenHeight > 0) {
            screenWidth -= (((screenWidth % 2) == 0) ? 0 : 1);
            screenHeight -= (((screenHeight % 2) == 0) ? 0 : 1);
            EnvironmentBase.screenWidth = Math.min(screenWidth, EnvironmentBase.MAX_SCREEN_WIDTH);
            EnvironmentBase.screenHeight = Math.min(screenHeight, EnvironmentBase.MAX_SCREEN_HEIGHT);
        }
        if (width > 0 && height > 0) {
            width -= (((width % 2) == 0) ? 0 : 1);
            height -= (((height % 2) == 0) ? 0 : 1);
            Environment.width = Math.min(width, Environment.screenWidth);
            Environment.height = Math.min(height, Environment.screenHeight);
        }
        sizeWindow();
    }
    
    /**
     * Sets the dimensions of the Window and the Render Panel.
     *
     * @param width  The width of the Window and the Render Panel.
     * @param height The height of the Window and the Render Panel.
     */
    public void setSize(int width, int height) {
        setSize(width, height, width, height);
    }
    
    /**
     * Sets the dimensions of the Window.
     *
     * @param screenWidth  The width of the Window.
     * @param screenHeight The height of the Window.
     */
    public void setWindowSize(int screenWidth, int screenHeight) {
        setSize(-1, -1, screenWidth, screenHeight);
    }
    
    /**
     * Sets the dimensions of the Render Panel.
     *
     * @param width  The width of the Render Panel.
     * @param height The height of the Render Panel.
     */
    public void setRenderSize(int width, int height) {
        setSize(width, height, -1, -1);
    }
    
    /**
     * Sets the coordinates to center the Environment at.
     *
     * @param origin The coordinates to center the Environment at.
     */
    public void setOrigin(Vector origin) {
        Environment.origin = origin;
    }
    
    /**
     * Sets the background color of the Environment.
     *
     * @param background The background color of the Environment.
     */
    public void setBackground(Color background) {
        this.background = background;
        frame.getContentPane().setBackground(background);
    }
    
    /**
     * Sets the flag indicating whether or not to use double buffering.
     *
     * @param doubleBuffering Whether to use double buffering or not.
     */
    public void setDoubleBuffering(boolean doubleBuffering) {
        this.doubleBuffering = doubleBuffering;
    }
    
    
    //Static Methods
    
    /**
     * Returns the Environment time in milliseconds.
     *
     * @return The Environment time in milliseconds.
     */
    public static long currentTimeMillis() {
        return time;
    }
    
    /**
     * Adds a Task to the Environment.
     *
     * @param task The Task.
     * @return The id of the Task that was added.
     */
    public static UUID addTask(Task task) {
        tasks.put(task.id, task);
        return task.id;
    }
    
    /**
     * Adds a Task to the Environment.
     *
     * @param action The action of the Task.
     * @return The id of the Task that was added.
     */
    public static UUID addTask(Runnable action) {
        Task task = new Task();
        task.action = action;
        return addTask(task);
    }
    
    /**
     * Removes a Task from the Environment.
     *
     * @param id The id of the Task to remove.
     */
    public static void removeTask(UUID id) {
        tasks.remove(id);
    }
    
    /**
     * Pauses a Task in the Environment.
     *
     * @param id The id of the Task to pause.
     */
    public static void pauseTask(UUID id) {
        Task task = tasks.get(id);
        if (task != null) {
            task.active.set(false);
        }
    }
    
    /**
     * Resumes a Task in the Environment.
     *
     * @param id The id the Task to resume.
     */
    public static void resumeTask(UUID id) {
        Task task = tasks.get(id);
        if (task != null) {
            task.active.set(true);
        }
    }
    
    /**
     * Adds a shutdown Task to the Environment.
     *
     * @param action The action of the Task.
     * @return The id of the Task that was added.
     */
    public static UUID registerShutdownTask(Runnable action) {
        Task task = new Task();
        task.action = action;
        shutdownTasks.put(task.id, task);
        return task.id;
    }
    
    
    //Inner Classes
    
    /**
     * Defines a task to be run after each frame of the Environment.
     */
    public static class Task {
        
        //Fields
        
        /**
         * The id of the Task.
         */
        public final UUID id;
        
        /**
         * The action of the Task.
         */
        public Runnable action;
        
        /**
         * A flag indicating whether the Task is active or not.
         */
        public AtomicBoolean active = new AtomicBoolean(true);
        
        
        //Constructors
        
        /**
         * The constructor for a Task.
         */
        public Task() {
            id = UUID.randomUUID();
        }
        
    }
    
}
