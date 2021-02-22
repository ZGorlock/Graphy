/*
 * File:    Clipboard.java
 * Package: commons.access
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.access;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

import commons.log.CommonsLogging;
import commons.media.ImageUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A resource class that provides access to the clipboard.
 */
public final class Clipboard {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(Clipboard.class);
    
    
    //Functions
    
    /**
     * Returns the contents of the clipboard as a string.
     *
     * @return The contents of the clipboard.
     */
    public static String getClipboard() {
        String clipboard;
        try {
            clipboard = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (HeadlessException | UnsupportedFlavorException | IOException ignored) {
            if (logClipboard()) {
                logger.trace("Clipboard: Unable to retrieve contents from the clipboard");
            }
            return "";
        }
        if (logClipboard()) {
            logger.trace("Clipboard: Retrieved contents of the clipboard");
        }
        return clipboard;
    }
    
    /**
     * Publishes a string to the clipboard.
     *
     * @param content The new content of the clipboard.
     */
    public static void putClipboard(String content) {
        StringSelection selection = new StringSelection(content);
        java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        if (logClipboard()) {
            logger.trace("Clipboard: Published contents to the clipboard");
        }
    }
    
    /**
     * Returns the contents of the clipboard as an image.
     *
     * @return The contents of the clipboard, or null if there is no image on the clipboard.
     */
    public static BufferedImage getClipboardImage() {
        return ImageUtility.copyImageFromClipboard();
    }
    
    /**
     * Publishes an image to the clipboard.
     *
     * @param content The new content of the clipboard.
     */
    public static void putClipboardImage(BufferedImage content) {
        ImageUtility.copyImageToClipboard(content);
    }
    
    /**
     * Determines if clipboard logging is enabled or not.
     *
     * @return Whether clipboard logging is enabled or not.
     */
    public static boolean logClipboard() {
        return CommonsLogging.logClipboard();
    }
    
}
