/* Based on the javac task from apache-ant-1.7.1.
 * The license in that file is as follows:
 *
 *   Licensed to the Apache Software Foundation (ASF) under one or
 *   more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information regarding
 *   copyright ownership.  The ASF licenses this file to You under
 *   the Apache License, Version 2.0 (the "License"); you may not
 *   use this file except in compliance with the License.  You may
 *   obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an "AS
 *   IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *   express or implied.  See the License for the specific language
 *   governing permissions and limitations under the License.
 *
 */

/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 */

package com.redhat.ceylon.ant;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;


public class Ceylon extends Task {

    static final String FAIL_MSG = "Run failed; see the compiler error output for details.";

    private Path src;   
    private String run;
    private String module;
    private Rep systemRepository;
    private List<Rep> repositories = new LinkedList<Rep>();
    private File executable;
    private ExitHandler exitHandler = new ExitHandler();

	/**
     * Set the source directories to find the source Java and Ceylon files.
     * @param src the source directories as a path
     */
    public void setSrc(Path src) {
        if (this.src == null) {
            this.src = src;
        } else {
            this.src.append(src);
        }
    }

    /**
     * Adds a module repository
     * @param rep the new module repository
     */
    public void addRep(Rep rep){
        repositories.add(rep);
    }
    
    /**
     * Set the fully qualified name of a toplevel method or class with no parameters.
     */
    public void setRun(String run) {
        this.run = run;
    }

    /**
     * Set the name of a runnable module with an optional version
     */
    public void setModule(ModuleAndVersion module) {
        this.module = module.toSpec();
    }

    public String getErrorProperty() {
        return exitHandler.getErrorProperty();
    }

    public void setErrorProperty(String errorProperty) {
        this.exitHandler.setErrorProperty(errorProperty);
    }

    public boolean getFailOnError() {
        return exitHandler.isFailOnError();
    }

    public void setFailOnError(boolean failOnError) {
        this.exitHandler.setFailOnError(failOnError);
    }

    /**
     * Executes the task.
     * @exception BuildException if an error occurs
     */
    @Override
    public void execute() throws BuildException {
        Java7Checker.check();
        
        checkParameters();
        
        run();
    }

    /**
     * Set compiler executable depending on the OS.
     */
    public void setExecutable(String executable) {
        this.executable = new File(Util.getScriptName(executable));
	}

    /**
     * Check that all required attributes have been set and nothing silly has
     * been entered.
     * 
     * @exception BuildException if an error occurs
     */
    protected void checkParameters() throws BuildException {
        if(module == null || module.isEmpty()){
            throw new BuildException("Missing module parameter is required");
        }
        // this will check that we have one
        getCeylon();
    }

    /**
     * Perform the compilation.
     */
    private void run() {
        Commandline cmd = new Commandline();
        cmd.setExecutable(getCeylon());
        cmd.createArgument().setValue("run");
        if(run != null){
            cmd.createArgument().setValue("--run=" + run);
        }
        if(src != null){
            cmd.createArgument().setValue("--src="+src.toString());
        }
        if (systemRepository != null) {
            cmd.createArgument().setValue("--sysrep=" + Util.quoteParameter(systemRepository.url));
        }
        if(repositories != null){
            for(Rep rep : repositories){
                // skip empty entries
                if(rep.url == null || rep.url.isEmpty())
                    continue;
                cmd.createArgument().setValue("--rep="+Util.quoteParameter(rep.url));
            }
        }
        cmd.createArgument().setValue(module);

        try {
            Execute exe = new Execute(new LogStreamHandler(this, Project.MSG_INFO, Project.MSG_WARN));
            exe.setAntRun(getProject());
            exe.setWorkingDirectory(getProject().getBaseDir());
            log("Command line " + Arrays.toString(cmd.getCommandline()), Project.MSG_VERBOSE);
            exe.setCommandline(cmd.getCommandline());
            exe.execute();
            if (exe.getExitValue() != 0) {
                exitHandler.handleExit(this, exe.getExitValue(), FAIL_MSG);
            }
        } catch (IOException e) {
            throw new BuildException("Error running Ceylon compiler", e, getLocation());
        }
    }

    /**
     * Tries to find a ceylon runner either user-specified or detected
     */
    private String getCeylon() {
        return Util.findCeylonScript(this.executable, getProject());
    }
}
