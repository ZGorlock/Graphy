/*
 * File:    ScreenUtility.java
 * Package: utility
 * Author:  Zachary Gill
 */

package utility;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Handles the screen size.
 */
public final class ScreenUtility {
    
    //Static Fields
    
    /**
     * The width of the screen.
     */
    public static int MONITOR_WIDTH;
    
    /**
     * The height of the screen.
     */
    public static int MONITOR_HEIGHT;
    
    /**
     * The width of the taskbar.
     */
    public static int TASKBAR_WIDTH;
    
    /**
     * The height of the taskbar.
     */
    public static int TASKBAR_HEIGHT;
    
    /**
     * The width of the screen excluding the taskbar.
     */
    public static int SCREEN_WIDTH;
    
    /**
     * The height of the screen excluding the taskbar.
     */
    public static int SCREEN_HEIGHT;
    
    /**
     * The width of the window border.
     */
    public static int BORDER_WIDTH;
    
    /**
     * The height of the window border.
     */
    public static int BORDER_HEIGHT;
    
    /**
     * The width of the screen excluding the taskbar and window border.
     */
    public static int DISPLAY_WIDTH;
    
    /**
     * The height of the screen excluding the taskbars and window border.
     */
    public static int DISPLAY_HEIGHT;
    
    static {
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = graphicsEnvironment.getScreenDevices()[0];
        GraphicsConfiguration graphicsConfiguration = graphicsDevice.getConfigurations()[0];
        Rectangle bounds = graphicsConfiguration.getBounds();
        MONITOR_WIDTH = bounds.width;
        MONITOR_HEIGHT = bounds.height;
        
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(graphicsConfiguration);
        TASKBAR_WIDTH = Math.abs(screenInsets.right - screenInsets.left);
        TASKBAR_HEIGHT = Math.abs(screenInsets.bottom - screenInsets.top);
        
        SCREEN_WIDTH = MONITOR_WIDTH - TASKBAR_WIDTH;
        SCREEN_HEIGHT = MONITOR_HEIGHT - TASKBAR_HEIGHT;
        
        JFrame tmpFrame = new JFrame();
        tmpFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        JPanel tmpPanel = new JPanel();
        tmpPanel.setSize(new Dimension(500, 500));
        tmpPanel.setPreferredSize(new Dimension(500, 500));
        tmpFrame.getContentPane().add(tmpPanel);
        tmpFrame.pack();
        BORDER_WIDTH = tmpFrame.getWidth() - 500;
        BORDER_HEIGHT = tmpFrame.getHeight() - 500;
        tmpFrame.dispose();
        
        DISPLAY_WIDTH = SCREEN_WIDTH - BORDER_WIDTH;
        DISPLAY_HEIGHT = SCREEN_HEIGHT - BORDER_HEIGHT;
    }
    
}
