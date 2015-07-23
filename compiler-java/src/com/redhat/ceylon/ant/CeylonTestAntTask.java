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
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;

public class CeylonTestAntTask extends RepoUsingCeylonAntTask {

    static final String FAIL_MSG = "Test failed; see the error output for details.";

    public static class Test {
        private String test;

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }
    }

    private final ModuleSet moduleSet = new ModuleSet();
    private String compileFlags;
    private String version;
    private Boolean tap = false;
    private Boolean report = false;
    private List<Test> tests = new ArrayList<Test>(0);

    public CeylonTestAntTask() {
        super("test");
    }

    /**
     * Calling the test tool ATM needs a new JVM: https://github.com/ceylon/ceylon-compiler/issues/1366
     */
    protected boolean shouldSpawnJvm() {
        return true;
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
     * Sets the ceylon.test module version.
     */
    public void setVersion(String version) {
        this.version = version;
    }
    
    /**
     * Enables the Test Anything Protocol v13.
     * @param tap
     */
    public void setTap(Boolean tap) {
        this.tap = tap;
    }
    
    /**
     * Generates the test results report into HTML format, output directory is `reports/test` (experimental).
     * @param report
     */
    public void setReport(Boolean report) {
        this.report = report;
    }

    /**
     * Adds a test to run.
     * @param test
     */
    public void addTest(Test test) {
        this.tests.add(test);
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
        
        if(compileFlags != null){
            appendOptionArgument(cmd, "--compile", compileFlags);
        }
        if(version != null) {
            appendOptionArgument(cmd, "--version", version);
        }
        if(tap) {
            appendOption(cmd, "--tap");
        }
        if(report) {
            appendOption(cmd, "--report");
        }
        for (Test test : tests) {
            appendOptionArgument(cmd, "--test", test.getTest());
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
