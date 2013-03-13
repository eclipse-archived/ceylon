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

import java.util.Set;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;

public class CeylonRunJsAntTask extends CeylonAntTask {

    private String module;
    private String func;
    private RepoSet reposet = new RepoSet();
    private String systemRepository;

    public CeylonRunJsAntTask() {
        super("run-js");
    }
    
    /**
     * Adds a module repository
     * @param repo the new module repository
     */
    public void addConfiguredRep(Repo repo) {
        reposet.addConfiguredRepo(repo);
    }
    protected Set<Repo> getRepositories() {
        return reposet.getRepos();
    }

    protected String getSystemRepository() {
        return systemRepository;
    }

    /**
     * Sets the system repository
     * @param rep the new system repository
     */
    public void setSysRep(String rep) {
        systemRepository = rep;
    }

    public void setModule(String value) {
        module = value;
    }
    public void setRun(String value) {
        func = value;
    }

    @Override
    protected void checkParameters() {
        super.checkParameters();
        if (module == null) {
            throw new BuildException("ceylonjs requires module attribute to be set");
        }   
    }
    
    @Override
    protected void completeCommandline(Commandline cmd) {
        if(func != null){
            cmd.createArgument().setValue("--run=" + func);
        }
        if (systemRepository != null) {
            cmd.createArgument().setValue("--sysrep=" + systemRepository);
        }
        
        for (Repo rep : reposet.getRepos()) {
            // skip empty entries
            if (rep.url == null || rep.url.isEmpty())
                continue;
            cmd.createArgument().setValue("--rep="+rep.url);
        }
        
        cmd.createArgument().setValue(module);
    }

    @Override
    protected String getFailMessage() {
        return CeylonRunAntTask.FAIL_MSG;
    }

    
}
