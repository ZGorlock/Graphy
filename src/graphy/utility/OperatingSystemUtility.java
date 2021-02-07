/*
 * File:    OperatingSystemUtility.java
 * Package: graphy.utility
 * Author:  Zachary Gill
 */

package graphy.utility;

/**
 * Provides access to the operating system.
 */
public final class OperatingSystemUtility {
    
    //Enums
    
    /**
     * An enumeration of operating systems.
     */
    public enum OS {
        
        //Values
        
        WINDOWS,
        UNIX,
        MACOS,
        POSIX,
        OTHER
        
    }
    
    
    //Functions
    
    /**
     * Determines the current operating system.
     *
     * @return The current operating system.
     */
    public static OS getOS() {
        String osName = getOSName().toUpperCase();
        
        if (osName.contains("WINDOWS")) {
            return OS.WINDOWS;
        } else if (osName.contains("LINUX") ||
                osName.contains("MPE/IX") ||
                osName.contains("FREEBSD") ||
                osName.contains("IRIX") ||
                osName.contains("UNIX")) {
            return OS.UNIX;
        } else if (osName.contains("MAC")) {
            return OS.MACOS;
        } else if (osName.contains("SUN") ||
                osName.contains("SOL") ||
                osName.contains("HP-UX") ||
                osName.contains("AIX")) {
            return OS.POSIX;
        } else {
            return OS.OTHER;
        }
    }
    
    /**
     * Returns the name of the current operating system.
     *
     * @return The name of the current operating system.
     */
    public static String getOSName() {
        return System.getProperty("os.name");
    }
    
    /**
     * Determines if the current operating system is Windows.
     *
     * @return Whether the current operating system is Windows or not.
     */
    public static boolean isWindows() {
        return getOS().equals(OS.WINDOWS);
    }
    
    /**
     * Determines if the current operating system is Unix.
     *
     * @return Whether the current operating system is Unix or not.
     */
    public static boolean isUnix() {
        return getOS().equals(OS.UNIX);
    }
    
    /**
     * Determines if the current operating system is macOS.
     *
     * @return Whether the current operating system is macOS or not.
     */
    public static boolean isMacOS() {
        return getOS().equals(OS.MACOS);
    }
    
    /**
     * Determines if the current operating system is POSIX.
     *
     * @return Whether the current operating system is POSIX or not.
     */
    public static boolean isPosix() {
        return getOS().equals(OS.POSIX);
    }
    
    /**
     * Determines if the current operating system is Other.
     *
     * @return Whether the current operating system is Other or not.
     */
    public static boolean isOther() {
        return getOS().equals(OS.OTHER);
    }
    
    /**
     * Determines if the current operating system is a particular operating system.
     *
     * @param os The operating system to test for.
     * @return Whether the current operating system is the particular operating system or not.
     */
    public static boolean is(OS os) {
        return getOS().equals(os);
    }
    
}
