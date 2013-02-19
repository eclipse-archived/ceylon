/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.ant;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.types.Commandline;

/**
 * Baseclass for ant tasks which execute a ceylon tool in a subprocess.
 */
public abstract class CeylonAntTask extends Task {

    private final String toolName;
    
    private File executable;
    
    private ExitHandler exitHandler = new ExitHandler();

    /**
     * @param toolName The name of the ceylon tool which this task executes.
     */
    protected CeylonAntTask(String toolName) {
        this.toolName = toolName;
    }
    
    public String getExecutable() {
        return executable.getPath();
    }
    
    /** Sets the location of the ceylon executable script */
    public void setExecutable(String executable) {
        this.executable = new File(Util.getScriptName(executable));
    }
    
    public boolean getFailOnError() {
        return exitHandler.isFailOnError();
    }

    /** Sets whether an error in executing this task should fail the build */
    public void setFailOnError(boolean failOnError) {
        this.exitHandler.setFailOnError(failOnError);
    }
    
    public String getErrorProperty() {
        return exitHandler.getErrorProperty();
    }

    /** Sets name of a property to set to true in the event of an error */
    public void setErrorProperty(String errorProperty) {
        this.exitHandler.setErrorProperty(errorProperty);
    }
    
    /**
     * Sets the Ceylon program exit code into the given property
     * @param resultProperty the property to set to the Ceylon program exit code
     */
    public void setResultProperty(String resultProperty){
        this.exitHandler.setResultProperty(resultProperty);
    }
    
    /** Executes the task */
    public void execute() {
        Java7Checker.check();
        
        checkParameters();
        Commandline cmd = buildCommandline();
        if (cmd != null) {
            executeCommandline(cmd);
        }
    }

    /**
     * Executes a Commandline
     * @param cmd The commandline
     */
    protected void executeCommandline(Commandline cmd) {
        try {
            
            Execute exe = new Execute(new LogStreamHandler(this, Project.MSG_INFO, Project.MSG_WARN));
            exe.setAntRun(getProject());
            exe.setWorkingDirectory(getProject().getBaseDir());
            log("Command line " + Arrays.toString(cmd.getCommandline()), Project.MSG_VERBOSE);
            exe.setCommandline(cmd.getCommandline());
            exe.execute();
            if (exe.getExitValue() != 0) {
                String message = formatFailureMessage(cmd);
                exitHandler.handleExit(this, exe.getExitValue(), message);
            }else{
                exitHandler.handleExit(this, exe.getExitValue(), null);
            }
        } catch (IOException e) {
            throw new BuildException("Error running Ceylon compiler", e, getLocation());
        }
    }

    protected String formatFailureMessage(Commandline cmd) {
        StringBuilder sb = new StringBuilder("While executing command").append(System.lineSeparator());
        for (String s : cmd.getCommandline()) {
            sb.append("   ").append(s).append(System.lineSeparator());
        }
        sb.append(getFailMessage());
        String message = sb.toString();
        return message;
    }

    /**
     * Returns the message to pass to {@link ExitHandler#handleExit(Task, int, String)}
     */
    protected abstract String getFailMessage();

    /**
     * Builds the command line to be executed
     * @return the command line to be executed, 
     * or null if there is no command to be executed
     */
    protected Commandline buildCommandline() {
        Commandline cmd = new Commandline();
        cmd.setExecutable(findExecutable());
        cmd.createArgument().setValue(toolName);
        completeCommandline(cmd);
        return cmd;
    }

    /**
     * Completes the building of a partially configured Commandline
     * @param cmd
     */
    protected abstract void completeCommandline(Commandline cmd);

    /**
     * Checks the parameters
     */
    protected void checkParameters() {
        findExecutable();
    }
    
    /**
     * Tries to find a ceylonc compiler either user-specified or detected
     */
    private String findExecutable() {
        return Util.findCeylonScript(this.executable, getProject());
    }
    
    /**
     * Adds a {@code --verbose} (or {@code --verbose=flags}) to the given 
     * command line
     * @param cmd The command line
     * @param verbose The verbose flags: {@code "true"}, {@code "yes"} or 
     * {@code "on"} just adds a plain {@code --verbose} option, 
     * {@code "false"}, {@code "no"} or 
     * {@code "off"} doesn't add any option, 
     * otherwise the value is 
     * treated as a list of flags and is passed verbatim via {@code --verbose=...}.
     */
    protected static void appendVerboseOption(Commandline cmd, String verbose) {
        if (verbose != null
                && !"false".equals(verbose)
                && !"no".equals(verbose)
                && !"off".equals(verbose)) {
            if ("true".equals(verbose)
                    || "yes".equals(verbose)
                    || "on".equals(verbose)) {
                // backward compat with verbose previously being a Boolean
                appendOption(cmd, "--verbose");
            } else {
                appendOptionArgument(cmd, "--verbose", verbose);
            }
        }
    }
    
    /**
     * Adds a {@code --user=user} to the given command line iff the given user 
     * is not null.
     * @param cmd The command line
     * @param user The user
     */
    protected static void appendUserOption(Commandline cmd, String user) {
        appendOptionArgument(cmd, "--user", user);
    }
    
    /**
     * Adds a {@code --pass=pass} to the given command line iff the given 
     * password is not null.
     * @param cmd The command line
     * @param pass The password
     */
    protected static void appendPassOption(Commandline cmd, String pass) {
        appendOptionArgument(cmd, "--pass", pass);
    }
    
    /**
     * Adds a long option argument to the given command line iff the given 
     * value is not null.
     * @param cmd The command line
     * @param longOption The long option name (including the {@code --}, but 
     * excluding the {@code =}).
     * @param value The option value
     */
    protected static void appendOptionArgument(Commandline cmd, String longOption, String value) {
        if (value != null){
            cmd.createArgument().setValue(longOption);
            cmd.createArgument().setValue(value);
        }
    }
    
    /**
     * Adds a long option (with no argument) to the given command line.
     * @param cmd The command line
     * @param longOption The long option name (including the {@code --} 
     */
    protected static void appendOption(Commandline cmd, String longOption) {
        cmd.createArgument().setValue(longOption);
    }
    
}
