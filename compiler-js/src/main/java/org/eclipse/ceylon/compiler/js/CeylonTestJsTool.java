/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.js;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.ceylon.cmr.api.ModuleQuery;
import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.common.ModuleUtil;
import org.eclipse.ceylon.common.OSUtil;
import org.eclipse.ceylon.common.Versions;
import org.eclipse.ceylon.common.config.DefaultToolOptions;
import org.eclipse.ceylon.common.tool.Description;
import org.eclipse.ceylon.common.tool.OptionArgument;
import org.eclipse.ceylon.common.tool.RemainingSections;
import org.eclipse.ceylon.common.tool.Summary;
import org.eclipse.ceylon.common.tools.AbstractTestTool;
import org.eclipse.ceylon.common.tools.CeylonTool;
import org.eclipse.ceylon.common.tools.RepoUsingTool;

@Summary("Executes tests on Node.js")
@Description(
        "Executes tests in specified `<modules>`. " +
        "The `<modules>` arguments are the names of the modules to test with an optional version.")
@RemainingSections(
        RepoUsingTool.DOCSECTION_COMPILE_FLAGS +
        "\n\n" +
        "## Configuration file" +
        "\n\n" +
        "The test-js tool accepts the following option from the Ceylon configuration file: " +
        "`testtool.compile` " +
        "(the equivalent option on the command line always has precedence)." +
        "\n\n" +
        "## EXAMPLE" +
        "\n\n" +
        "The following would execute tests in the `com.example.foobar` module:" +
        "\n\n" +
        "    ceylon test-js com.example.foobar/1.0.0")
public class CeylonTestJsTool extends AbstractTestTool {

    private static final String COLOR_RESET = "org.eclipse.ceylon.common.tool.terminal.color.reset";
    private static final String COLOR_GREEN = "org.eclipse.ceylon.common.tool.terminal.color.green";
    private static final String COLOR_RED = "org.eclipse.ceylon.common.tool.terminal.color.red";

    private String nodeExe;
    private boolean debug = true;

    private CeylonRunJsTool ceylonRunJsTool;
    
    public CeylonTestJsTool() {
        super(CeylonRunJsMessages.RESOURCE_BUNDLE, 
                ModuleQuery.Type.JS,
                // JVM binary but don't care since JS
                null, null,
                Versions.JS_BINARY_MAJOR_VERSION, Versions.JS_BINARY_MINOR_VERSION);
    }

    @OptionArgument(argumentName = "node-exe")
    @Description("The path to the node.js executable. Will be searched in standard locations if not specified.")
    public void setNodeExe(String nodeExe) {
        this.nodeExe = nodeExe;
    }

    @OptionArgument(argumentName = "debug")
    @Description("Shows more detailed output in case of errors.")
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @Override
    public void initialize(CeylonTool mainTool) throws Exception {
        super.initialize(mainTool);
        
        final List<String> args = new ArrayList<String>();
        final List<String> moduleAndVersionList = new ArrayList<String>();

        processModuleNameOptVersionList(args, moduleAndVersionList);
        processTestList(args);
        processTagList(args);
        processArgumentList(args);
        compileFlags = processCompileFlags(compileFlags, DefaultToolOptions.getTestToolCompileFlags(org.eclipse.ceylon.common.Backend.JavaScript));
        processTapOption(args);
        processReportOption(args);
        processColors(args);
        
        resolveVersion(moduleAndVersionList);

        ceylonRunJsTool = new CeylonRunJsTool() {
            @Override
            protected void customizeDependencies(List<File> localRepos, RepositoryManager repoman, Set<String> loadedDependencies) throws IOException {
                for (String moduleAndVersion : moduleAndVersionList) {
                    String modName = ModuleUtil.moduleName(moduleAndVersion);
                    String modVersion = ModuleUtil.moduleVersion(moduleAndVersion);
                    File artifact = getArtifact(repoman, modName, modVersion, true);
                    localRepos.add(getRepoDir(modName, artifact));
                    loadDependencies(localRepos, repoman, artifact, loadedDependencies);
                }
            };
        };
        ceylonRunJsTool.setModuleVersion(TEST_MODULE_NAME + "/" + version);
        ceylonRunJsTool.setRun(TEST_RUN_FUNCTION);
        ceylonRunJsTool.setArgs(args);
        ceylonRunJsTool.setRepository(repos);
        ceylonRunJsTool.setSystemRepository(systemRepo);
        ceylonRunJsTool.setCacheRepository(cacheRepo);
        ceylonRunJsTool.setOverrides(overrides);
        ceylonRunJsTool.setNoDefRepos(noDefRepos);
        ceylonRunJsTool.setOffline(offline);
        ceylonRunJsTool.setVerbose(verbose);
        ceylonRunJsTool.setNodeExe(nodeExe);
        ceylonRunJsTool.setDebug(debug);
        ceylonRunJsTool.setCompile(compileFlags);
        ceylonRunJsTool.setCwd(cwd);
    }

    @Override
    public void run() throws Exception {
        ceylonRunJsTool.run();
    }

    private void processColors(final List<String> args) {
        String reset = OSUtil.Color.reset.escape();
        String green = OSUtil.Color.green.escape();
        String red = OSUtil.Color.red.escape();
        if (reset != null && green != null && red != null) {
            args.add("--" + COLOR_RESET);
            args.add(reset);
            args.add("--" + COLOR_GREEN);
            args.add(green);
            args.add("--" + COLOR_RED);
            args.add(red);
        }
    }

}