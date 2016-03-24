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
import org.apache.tools.ant.DynamicAttribute;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.UnsupportedAttributeException;
import org.apache.tools.ant.types.Commandline;

@ToolEquivalent("test-js")
public class CeylonTestJsAntTask extends RepoUsingCeylonAntTask implements DynamicAttribute {

    static final String FAIL_MSG = "Test failed; see the error output for details.";

    public static class Test {
        private String test;

        public String getTest() {
            return test;
        }

        @AntDoc("The name of a test to run.")
        @OptionEquivalent("--test")
        public void setTest(String test) {
            this.test = test;
        }
    }

    private final ModuleSet moduleSet = new ModuleSet();
    private String compileFlags;
    private String version;
    private String nodeExe;
    private Boolean tap = false;
    private Boolean debug = true;
    private Boolean report = false;
    private List<Test> tests = new ArrayList<Test>(0);
    
    public CeylonTestJsAntTask() {
        super("test-js");
    }

    @AntDoc("Modules to test")
    public void addConfiguredModuleSet(ModuleSet moduleset) {
        this.moduleSet.addConfiguredModuleSet(moduleset);
    }
    
    /**
     * Adds a module to compile
     * @param module the module name to compile
     */
    @AntDoc("A module to test")
    public void addConfiguredModule(Module module) {
        this.moduleSet.addConfiguredModule(module);
    }
    
    @AntDoc("Modules to test")
    public void addConfiguredSourceModules(SourceModules sourceModules) {
        this.moduleSet.addConfiguredSourceModules(sourceModules);
    }

    /**
     * Sets compile flags
     */
    @OptionEquivalent
    public void setCompile(String compileFlags) {
        this.compileFlags = compileFlags;
    }
    
    /**
     * Sets the version of the ceylon.test module to use.
     */
    @OptionEquivalent
    public void setVersion(String version) {
        this.version = version;
    }
    
    /**
     * For the attribute name 'node-exe', sets the path to the node.js executable.
     * <p>
     * (This is a bit convoluted because 'setNode-exe' is not a legal Java method name.)
     */
    @AntAttribute("node-exe")
    @OptionEquivalent("--node-exe")
    public void setDynamicAttribute(String name, String value) {
        if (name.equals("node-exe")) {
            this.nodeExe = value;
        } else {
            throw new UnsupportedAttributeException("ceylon-test-js does not support the \"" + name + "\" attribute.", name);
        }
    }
    
    /**
     * Enables more detailed output on errors.
     */
    @OptionEquivalent
    public void setDebug(Boolean debug) {
        this.debug = debug;
    }
    
    /**
     * Enables the Test Anything Protocol v13.
     * @param tap
     */
    @OptionEquivalent
    public void setTap(Boolean tap) {
        this.tap = tap;
    }
    
    /**
     * Generates the test results report into HTML format, output directory is `reports/test-js` (experimental).
     * @param report
     */
    @OptionEquivalent
    public void setReport(Boolean report) {
        this.report = report;
    }

    /**
     * Adds a test to run.
     * @param test
     */
    @AntDoc("The name of a test to run. If no `<test>`s are given then all "
            + "tests in the given `<module>`/`<moduleset>`/`<sourcemodule>`")
    @OptionEquivalent
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
        if(version != null){
            appendOptionArgument(cmd, "--version", version);
        }
        if(nodeExe != null){
            appendOptionArgument(cmd, "--node-exe", nodeExe);
        }
        if(debug != null) { // defaults to true, so set it like an option argument, not like a flag option
            appendOptionArgument(cmd, "--debug", debug.toString());
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
