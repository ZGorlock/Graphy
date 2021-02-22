/*
 * File:    CmdLine.java
 * Package: commons.access
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.access;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import commons.string.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A resource class that provides access to the system command line.
 */
public final class CmdLine {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(CmdLine.class);
    
    
    //Functions
    
    /**
     * Executes a command on the system command line.
     *
     * @param cmd  The command to execute.
     * @param wait Whether to wait for the execution to finish or not.
     * @return The output.
     * @see #buildProcess(String, boolean)
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
            
        } catch (IOException | InterruptedException e) {
            logger.warn("Error executing command: " + cmd);
        } catch (Exception e) {
            logger.error("Error executing command: " + cmd, e);
        }
        return "";
    }
    
    /**
     * Executes a command on the system command line.
     *
     * @param cmd The command to execute.
     * @return The output.
     * @see #executeCmd(String, boolean)
     */
    public static String executeCmd(String cmd) {
        return executeCmd(cmd, true);
    }
    
    /**
     * Executes a command on the system command line as a thread.
     *
     * @param cmd The command to execute.
     * @return The process running the command execution.
     * @see #buildProcess(String, boolean)
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
     * @throws Exception When there is an unknown operating system.
     */
    @SuppressWarnings("DuplicateBranchesInSwitch")
    public static ProcessBuilder buildProcess(String cmd, boolean useScriptCommand) throws Exception {
        ProcessBuilder builder;
        if (useScriptCommand) {
            switch (OperatingSystem.getOperatingSystem()) {
                case WINDOWS:
                    builder = new ProcessBuilder("cmd.exe", "/c", cmd);
                    break;
                case UNIX:
                    builder = new ProcessBuilder("bash", "-c", cmd);
                    break;
                case MACOS:
                    builder = new ProcessBuilder(StringUtility.tokenizeArgs(cmd));
                    break;
                case POSIX:
                    builder = new ProcessBuilder(StringUtility.tokenizeArgs(cmd));
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
