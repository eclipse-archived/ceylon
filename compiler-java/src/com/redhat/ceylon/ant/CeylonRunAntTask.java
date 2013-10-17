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
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;


public class CeylonRunAntTask extends CeylonAntTask {

    static final String FAIL_MSG = "Run failed; see the compiler error output for details.";
    
    private Path src;   
    private String run;
    private String module;
    private String systemRepository;
    private RepoSet reposet = new RepoSet();
    
    public CeylonRunAntTask() {
        super("run");
    }

    /**
     * Calling the run tool ATM needs a new JVM: https://github.com/ceylon/ceylon-compiler/issues/1366
     */
    protected boolean shouldSpawnJvm() {
        return true;
    }

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

    /**
     * Set the fully qualified name of a toplevel method or class with no parameters.
     */
    public void setRun(String run) {
        this.run = run;
    }

    /**
     * Set the name of a runnable module with an optional version
     */
    public void setModule(String module) {
        this.module = module;
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
    }

    /**
     * Perform the compilation.
     */
    protected void completeCommandline(Commandline cmd) {
        if(run != null){
            cmd.createArgument().setValue("--run=" + run);
        }
        if(src != null){
            cmd.createArgument().setValue("--src="+src.toString());
        }
        if (systemRepository != null) {
            cmd.createArgument().setValue("--sysrep=" + systemRepository);
        }
        
        for(Repo rep : this.reposet.getRepos()){
            // skip empty entries
            if(rep.url == null || rep.url.isEmpty())
                continue;
            cmd.createArgument().setValue("--rep="+rep.url);
        }
        
        cmd.createArgument().setValue(module);

        
    }

    @Override
    protected String getFailMessage() {
        return "Run failed; see the compiler error output for details.";
    }

    
}
