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

@ToolEquivalent("copy")
@AntDoc("To copy the module `com.example.foo` with all its dependencies\n"+
        "to a module repository in the `build` directory:\n"+
        "\n"+
        "<!-- lang: xml -->\n"+
        "    <target name=\"copy\" depends=\"ceylon-ant-taskdefs\">\n"+
        "      <ceylon-copy out=\"build\" recursive=\"true\">\n"+
        "        <module name=\"com.example.foo\" version=\"1.5\"/>\n"+
        "      </ceylon-copy>\n"+
        "    </target>\n")
public class CeylonCopyAntTask extends OutputRepoUsingCeylonAntTask {

    static final String FAIL_MSG = "Copy failed; see the error output for details.";
    
    private final ModuleSet moduleSet = new ModuleSet();
    private boolean withDependencies;
    private Boolean js;
    private Boolean jvm;
    private Boolean docs;
    private Boolean src;
    private Boolean scripts;
    private Boolean all;
    
    public CeylonCopyAntTask() {
        super("copy");
    }

    @AntDoc("A set of modules to copy.")
    public void addConfiguredModuleSet(ModuleSet moduleset) {
        this.moduleSet.addConfiguredModuleSet(moduleset);
    }
    
    /**
     * Adds a module to compile
     * @param module the module name to compile
     */
    @AntDoc("A module to copy.")
    public void addConfiguredModule(Module module) {
        this.moduleSet.addConfiguredModule(module);
    }
    
    @AntDoc("A set of modules to copy.")
    public void addConfiguredSourceModules(SourceModules sourceModules) {
        this.moduleSet.addConfiguredSourceModules(sourceModules);
    }

    /**
     * Determines if dependencies should be recursively copied or not
     */
    @OptionEquivalent("--with-dependencies")
    public void setWithDependencies(boolean withDependencies) {
        this.withDependencies = withDependencies;
    }

    /**
     * Set to true to copy JS artifacts (defaults: true)
     */
    @OptionEquivalent
    public void setJs(Boolean js) {
        this.js = js;
    }

    /**
     * Set to true to copy JVM artifacts (defaults: true)
     */
    @OptionEquivalent
    public void setJvm(Boolean jvm) {
        this.jvm = jvm;
    }

    /**
     * Set to true to copy source artifacts (defaults: false)
     */
    @OptionEquivalent
    public void setSrc(Boolean src) {
        this.src = src;
    }

    /**
     * Set to true to copy script artifacts (defaults: false)
     */
    @OptionEquivalent
    public void setScripts(Boolean scripts) {
        this.scripts = scripts;
    }

    /**
     * Set to true to copy documentation artifacts (defaults: false)
     */
    @OptionEquivalent
    public void setDocs(Boolean docs) {
        this.docs = docs;
    }

    /**
     * Set to true to copy every artifact (js, jvm, docs, src)
     */
    @OptionEquivalent
    public void setAll(Boolean all) {
        this.all = all;
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
        for (Module module : moduleSet.getModules()) {
        	if (module.getVersion() == null || module.getVersion().isEmpty()) {
                throw new BuildException("You must specify a version for the module " + module.getName());
        	}
        }
    }

    /**
     * Perform the compilation.
     */
    protected void completeCommandline(Commandline cmd) {
        super.completeCommandline(cmd);
        
        if (withDependencies) {
            appendOption(cmd, "--with-dependencies");
        }

        if (BooleanUtil.isTrue(js)) {
            appendOption(cmd, "--js");
        }

        if (BooleanUtil.isTrue(jvm)) {
            appendOption(cmd, "--jvm");
        }

        if (BooleanUtil.isTrue(src)) {
            appendOption(cmd, "--src");
        }

        if (BooleanUtil.isTrue(scripts)) {
            appendOption(cmd, "--scripts");
        }

        if (BooleanUtil.isTrue(docs)) {
            appendOption(cmd, "--docs");
        }

        if (BooleanUtil.isTrue(all)) {
            appendOption(cmd, "--all");
        }

        for (Module module : moduleSet.getModules()) {
            log("Adding module: "+module, Project.MSG_VERBOSE);
            cmd.createArgument().setValue(module.toVersionedSpec());
        }
    }

    @Override
    protected String getFailMessage() {
        return FAIL_MSG;
    }

    
}
