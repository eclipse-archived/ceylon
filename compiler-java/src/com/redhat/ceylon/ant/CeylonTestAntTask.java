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

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;


public class CeylonTestAntTask extends CeylonAntTask {

    static final String FAIL_MSG = "Test failed; see the error output for details.";
    
    private final ModuleSet moduleSet = new ModuleSet();
    private String systemRepository;
    private RepoSet reposet = new RepoSet();
    private String compileFlags;
    
    public CeylonTestAntTask() {
        super("test");
    }

    /**
     * Calling the run tool ATM needs a new JVM: https://github.com/ceylon/ceylon-compiler/issues/1366
     */
    protected boolean shouldSpawnJvm() {
        return true;
    }

    /**
     * Adds a module repository
     * @param repo the new module repository
     */
    public void addConfiguredRep(Repo repo){
        this.reposet.addConfiguredRepo(repo);
    }
    
    public void addConfiguredReposet(RepoSet reposet){
        this.reposet.addConfiguredRepoSet(reposet);
    }
    
    /**
     * Sets the system repository
     * @param rep the new system repository
     */
    public void setSysRep(String rep) {
        systemRepository = rep;
    }

    public void addConfiguredModuleSet(ModuleSet moduleset) {
        this.moduleSet.addConfiguredModuleSet(moduleset);
    }
    
    /**
     * Adds a module to compile
     * @param module the module name to compile
     */
    public void addConfiguredModule(Module module) {
        this.moduleSet.addConfiguredModule(module);
    }
    
    public void addConfiguredSourceModules(SourceModules sourceModules) {
        this.moduleSet.addConfiguredSourceModules(sourceModules);
    }

    /**
     * Sets compile flags
     */
    public void setCompile(String compileFlags) {
        this.compileFlags = compileFlags;
    }
    
    /**
     * Check that all required attributes have been set and nothing silly has
     * been entered.
     * 
     * @exception BuildException if an error occurs
     */
    protected void checkParameters() throws BuildException {
        if (this.moduleSet.getModules().isEmpty()) {
            throw new BuildException("You must specify a <module> or <moduleset>");
        }
    }

    /**
     * Perform the compilation.
     */
    protected void completeCommandline(Commandline cmd) {
        super.completeCommandline(cmd);
        
        if (systemRepository != null) {
            appendOptionArgument(cmd, "--sysrep", systemRepository);
        }
        if(compileFlags != null){
            appendOptionArgument(cmd, "--compile", compileFlags);
        }
        
        for(Repo rep : this.reposet.getRepos()){
            // skip empty entries
            if(rep.url == null || rep.url.isEmpty())
                continue;
            appendOptionArgument(cmd, "--rep", rep.url);
        }
        
        for (Module module : moduleSet.getModules()) {
            log("Adding module: "+module, Project.MSG_VERBOSE);
            cmd.createArgument().setValue(module.toSpec());
        }
    }

    @Override
    protected String getFailMessage() {
        return FAIL_MSG;
    }

    
}
