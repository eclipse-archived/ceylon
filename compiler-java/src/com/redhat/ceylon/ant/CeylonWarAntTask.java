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

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;

public class CeylonWarAntTask extends RepoUsingCeylonAntTask {

    static final String FAIL_MSG = "War failed; see the compiler error output for details.";

    public static class Exclude {
        private String module;

        public String getModule() {
            return module;
        }

        public void setModule(String module) {
            this.module = module;
        }
    }

    private String module;
    private String name;
    private String out;
    private String resourceRoot;
    private List<Exclude> excludeModules = new ArrayList<Exclude>(0);

    public CeylonWarAntTask() {
        super("war");
    }

    /**
     * Calling the run tool ATM needs a new JVM: https://github.com/ceylon/ceylon-compiler/issues/1366
     */
    protected boolean shouldSpawnJvm() {
        return true;
    }

    /**
     * Sets name of the WAR file (default: moduleName-version.war)
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the output directory for the WAR file (default: .)
     */
    public void setOut(String out) {
        this.out = out;
    }

    /**
     * Sets the special resource folder name whose files will end up in
     * the root of the resulting WAR file (no default).
     */
    public void setResourceRoot(String resourceRoot) {
        this.resourceRoot = resourceRoot;
    }

    /**
     * Adds a module to exclude from the WAR file. The module attribute
     * of the nested exclude element can be a module name or a
     * file containing module names.
     */
    public void addExclude(Exclude module) {
        this.excludeModules.add(module);
    }

    /**
     * Set the name of a module for which to create the war
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
        super.completeCommandline(cmd);

        if (name != null) {
            appendOptionArgument(cmd, "--name", name);
        }

        if (out != null){
            appendOptionArgument(cmd, "--out", out);
        }

        if (resourceRoot != null){
            appendOptionArgument(cmd, "--resource-root", resourceRoot);
        }

        for (Exclude exclude : excludeModules) {
            appendOptionArgument(cmd, "--exclude-module", exclude.getModule());
        }

        cmd.createArgument().setValue(module);
    }

    @Override
    protected String getFailMessage() {
        return "War failed; see the compiler error output for details.";
    }

}
