/* Originally based on the javac task from apache-ant-1.7.1.
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
import java.io.FileFilter;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;

public class CeylonDocAntTask extends LazyCeylonAntTask {
    private static final FileFilter ARTIFACT_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return true;
        }
    };

    private ModuleSet moduleset = new ModuleSet();
    private boolean includeNonShared;
    private boolean includeSourceCode;
    private String user;
    private String pass;
    
    public CeylonDocAntTask() {
        super("doc");
    }
    
    /**
     * Sets the user name for the output module repository (HTTP only)
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Sets the password for the output module repository (HTTP only)
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * Include even non-shared declarations
     */
    public void setIncludeNonShared(boolean includeNonShared){
        this.includeNonShared = includeNonShared;
    }
    
    /**
     * Include source code in the documentation
     */
    public void setIncludeSourceCode(boolean includeSourceCode){
        this.includeSourceCode = includeSourceCode;
    }
    
    /**
     * Adds a module to compile
     * @param module the module name to compile
     */
    public void addConfiguredModule(Module module){
        this.moduleset.addConfiguredModule(module);
    }
    
    public void addConfiguredModuleset(ModuleSet moduleset){
        this.moduleset.addConfiguredModuleSet(moduleset);
    }
    
    public void addConfiguredSourceModules(SourceModules modules){
        this.moduleset.addConfiguredSourceModules(modules);
    }

    /**
     * Perform the compilation.
     */
    @Override
    protected Commandline buildCommandline() {
        LazyHelper lazyTask = new LazyHelper(this) {
            @Override
            protected File getArtifactDir(String version, Module module) {
                File outModuleDir = new File(getOut(), module.toDir().getPath()+"/"+version +"/module-doc");
                return outModuleDir;
            }
            
            @Override
            protected FileFilter getArtifactFilter() {
                return ARTIFACT_FILTER;
            }
        };
        if (lazyTask.filterModules(moduleset.getModules())) {
            log("Everything's up to date");
            return null;
        }
        
        return super.buildCommandline();
    }
    
    protected void completeCommandline(Commandline cmd) {
        super.completeCommandline(cmd);
        
        if(includeSourceCode)
            cmd.createArgument().setValue("--source-code");
        if(includeNonShared)
            cmd.createArgument().setValue("--non-shared");
        // modules to document
        for (Module module : moduleset.getModules()) {
            log("Adding module: "+module, Project.MSG_VERBOSE);
            cmd.createArgument().setValue(module.toSpec());
        }

    }

    @Override
    protected String getFailMessage() {
        return "Documentation failed; see the ceylond error output for details.";
    }

}
