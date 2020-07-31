/*
 * File:    CmdLineUtility.java
 * Package: utility
 * Author:  Zachary Gill
 */

package utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Provides access to the system command line.
 */
public final class CmdLineUtility {
    
    //Functions
    
    /**
     * Executes a command on the system command line.
     *
     * @param cmd  The command to execute.
     * @param wait Whether to wait for the execution to finish or not.
     * @return The output.
     */
    public static String executeCmd(String cmd, boolean wait) {
        try {
            ProcessBuilder builder = buildProcess(cmd, true);
            
            Process process = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
            
            StringBuilder response = new StringBuilder();
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }
                response.append(line).append(System.lineSeparator());
            }
            
            if (wait) {
                process.waitFor();
            }
            r.close();
            process.destroy();
            
            return response.toString();
            
        } catch (Exception e) {
            System.err.println("Error executing command: " + cmd);
        }
        return "";
    }
    
    /**
     * Executes a command on the system command line.
     *
     * @param cmd The command to execute.
     * @return The output.
     */
    public static String executeCmd(String cmd) {
        return executeCmd(cmd, true);
    }
    
    /**
     * Executes a command on the system command line as a thread.
     *
     * @param cmd The command to execute.
     * @return The process running the command execution.
     */
    public static Process executeCmdAsThread(String cmd) {
        try {
            ProcessBuilder builder = buildProcess(cmd, true);
            
            return builder.start();
            
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Builds a process from a command.
     *
     * @param cmd              The command to build a process for.
     * @param useScriptCommand Whether or not to include the script command at the beginning ("cmd.exe  /c" or "bash -c").
     * @return The process that was built or null if it was not built.
     *
     * @throws Exception When there is an unknown operating system.
     */
    private static ProcessBuilder buildProcess(String cmd, boolean useScriptCommand) throws Exception {
        ProcessBuilder builder;
        if (useScriptCommand) {
            switch (OperatingSystemUtility.getOS()) {
                case WINDOWS:
                    builder = new ProcessBuilder("cmd.exe", "/c", cmd);
                    break;
                case UNIX:
                    builder = new ProcessBuilder("bash", "-c", cmd);
                    break;
                case MACOS:
                case POSIX:
                    builder = new ProcessBuilder(cmd);
                    break;
                case OTHER:
                default:
                    throw new Exception("Operating system: " + System.getProperty("os.name").toUpperCase() + " is not supported!");
            }
        } else {
            builder = new ProcessBuilder(cmd);
        }
        builder.redirectErrorStream(true);
        
        return builder;
    }
    
}
