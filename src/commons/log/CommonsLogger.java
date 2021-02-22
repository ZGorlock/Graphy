/*
 * File:    CommonsLogger.java
 * Package: commons.log
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.log;

/**
 * The contract for a Commons Logger implementation used to determine what should be logged.
 */
public interface CommonsLogger {
    
    //Methods
    
    /**
     * Returns whether or not Filesystem operations should be logged.
     *
     * @return Whether or not Filesystem operations should be logged.
     */
    boolean logFilesystem();
    
    /**
     * Returns whether or not Clipboard operations should be logged.
     *
     * @return Whether or not Clipboard operations should be logged.
     */
    boolean logClipboard();
    
    /**
     * Returns whether or not Internet operations should be logged.
     *
     * @return Whether or not Internet operations should be logged.
     */
    boolean logInternet();
    
}
