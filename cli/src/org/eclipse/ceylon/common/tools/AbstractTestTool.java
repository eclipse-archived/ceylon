/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.tools;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;

import org.eclipse.ceylon.cmr.api.ModuleDependencyInfo;
import org.eclipse.ceylon.cmr.api.ModuleQuery;
import org.eclipse.ceylon.cmr.api.ModuleVersionDetails;
import org.eclipse.ceylon.cmr.api.ModuleQuery.Type;
import org.eclipse.ceylon.common.Messages;
import org.eclipse.ceylon.common.ModuleUtil;
import org.eclipse.ceylon.common.Versions;
import org.eclipse.ceylon.common.tool.Argument;
import org.eclipse.ceylon.common.tool.Description;
import org.eclipse.ceylon.common.tool.Option;
import org.eclipse.ceylon.common.tool.OptionArgument;
import org.eclipse.ceylon.common.tool.Rest;
import org.eclipse.ceylon.common.tool.ToolUsageError;

public abstract class AbstractTestTool extends RepoUsingTool {
    
    protected static final String TEST_MODULE_NAME = "ceylon.test";
    protected static final String TEST_RUN_FUNCTION = "ceylon.test::runTestTool";
    
    protected List<String> moduleNameOptVersionList;
    protected List<String> testList;
    protected List<String> tagList;
    protected List<String> argumentList;
    protected String version;
    protected String compileFlags;
    protected String tap;
    protected String reportsDir;
    protected boolean report;
    protected boolean xmlJUnitReport;
    private final Type type;
    private final Integer jvmBinaryMajor;
    private final Integer jvmBinaryMinor;
    private final Integer jsBinaryMajor;
    private final Integer jsBinaryMinor;

    public AbstractTestTool(ResourceBundle bundle, ModuleQuery.Type type, 
            Integer jvmBinaryMajor, Integer jvmBinaryMinor,
            Integer jsBinaryMajor, Integer jsBinaryMinor) {
        super(bundle);
        this.type = type;
        this.jvmBinaryMajor = jvmBinaryMajor;
        this.jvmBinaryMinor = jvmBinaryMinor;
        this.jsBinaryMajor = jsBinaryMajor;
        this.jsBinaryMinor = jsBinaryMinor;
    }
    
    @Override
    public void initialize(CeylonTool mainTool) throws Exception {
        // noop
    }
    
    @Argument(argumentName = "modules", multiplicity = "+")
    public void setModules(List<String> moduleNameOptVersionList) {
        this.moduleNameOptVersionList = moduleNameOptVersionList;
    }

    @OptionArgument(longName = "test", argumentName = "test")
    @Description("Specifies which tests will be run.")
    public void setTests(List<String> testList) {
        this.testList = testList;
    }
    
    @OptionArgument(longName = "tag", argumentName = "tag")
    @Description("Specifies which tests will be run according to their tags. It can be used as "
            + "include filter, so only tests with specified tag will be executed. But it can "
            + "be used also as exclude filter, if tag name is prefixed with !, "
            + "so only tests without specified tag will be executed..")
    public void setTags(List<String> tagList) {
        this.tagList = tagList;
    }

    @Option
    @OptionArgument(argumentName = "flags")
    @Description("Determines if and how compilation should be handled. Allowed flags include: `never`, `once`, `force`, `check`.")
    public void setCompile(String compile) {
        this.compileFlags = compile;
    }

    @OptionArgument(argumentName = "version")
    @Description("Specifies which version of the test module to use.")
    public void setVersion(String version) {
        this.version = version;
    }
    
    @Option
    @OptionArgument(argumentName = "file")
    @Description("Enables the Test Anything Protocol v13 "
            + "and writes the results to the specified file. "
            + "If the file name is empty or `-`, print to standard output.")
    public void setTap(String tap) {
        this.tap = tap;
    }

    @OptionArgument(longName = "out", argumentName = "file")
    @Description("Sets the folder to use for reports. Defalts to `reports/{test|test-js}`.")
    public void setOut(String out) {
        this.reportsDir = out;
    }

    @Option(longName = "report")
    @Description("Generates the test results report into HTML format, output directory is set with `--out` (experimental).")
    public void setReport(boolean report) {
        this.report = report;
    }

    @Option(longName = "xml-junit-report")
    @Description("Generates the test results report into JUnit XML format, output directory is set with `--out` (experimental).")
    public void setXmlJUnitReport(boolean xmlJUnitReport) {
        this.xmlJUnitReport = xmlJUnitReport;
    }

    @Rest
    public void setArgs(List<String> argumentList) {
        this.argumentList = argumentList;
    }
    
    protected String resolveModuleAndVersion(String moduleNameOptVersion) throws IOException {
        String moduleName = ModuleUtil.moduleName(moduleNameOptVersion);
        String moduleVersion = ModuleUtil.moduleVersion(moduleNameOptVersion);

        moduleVersion = checkModuleVersionsOrShowSuggestions(
                moduleName,
                moduleVersion,
                type,
                jvmBinaryMajor, jvmBinaryMinor, jsBinaryMajor, jsBinaryMinor,
                compileFlags);

        return moduleVersion != null ? moduleName + "/" + moduleVersion : null;
    }
    
    protected void processModuleNameOptVersionList(List<String> args, List<String> moduleAndVersionList) throws IOException {
        if (moduleNameOptVersionList != null) {
            for (String moduleNameOptVersion : moduleNameOptVersionList) {
                String moduleAndVersion = resolveModuleAndVersion(moduleNameOptVersion);
                if (moduleAndVersion == null) {
                    return;
                }
                args.add("--module");
                args.add(moduleAndVersion);
                moduleAndVersionList.add(moduleAndVersion);
            }
        }
    }
    
    protected void processTestList(List<String> args) {
        if (testList != null) {
            for (String test : testList) {
                args.add("--test");
                args.add(test);
            }
        }
    }
    
    protected void processTagList(List<String> args) {
        if (tagList != null) {
            for (String tag : tagList) {
                args.add("--tag");
                args.add(tag);
            }
        }
    }
    
    protected void processArgumentList(List<String> args) {
        if (argumentList != null) {
            args.addAll(argumentList);
        }
    }

    protected void processReportOption(List<String> args) {
        if (report) {
            args.add("--report");
        }
        if (xmlJUnitReport) {
            args.add("--xml-junit-report");
        }
        if (reportsDir != null) {
            args.add("--reports-dir");
            args.add(reportsDir);
        }
    }

    protected void processTapOption(List<String> args) {
        if (tap != null) {
            if (tap.isEmpty()) {
                args.add("--tap");
            } else {
                args.add("--tap=" + tap);
            }
        }
    }
    
    protected void resolveVersion(List<String> moduleAndVersionList) throws IOException {
        if (version == null) {
            for (String moduleAndVersion : moduleAndVersionList) {
                String foundVersion = findTestVersionInDependecies(moduleAndVersion);
                if (version != null && foundVersion != null && !version.equals(foundVersion)) {
                    throw new ToolUsageError(Messages.msg(bundle, "test.version.ambiguous"));
                }
                if (foundVersion != null) {
                    version = foundVersion;
                }
            }
            if (version == null) {
                version = Versions.CEYLON_VERSION_NUMBER;
                msg("test.version.default", version).newline();
            }
        }
        if( verbose != null && (verbose.equals("") || verbose.equals("test")) ) {
            msg("test.version.info", version).newline();
        }

        // before ceylon.test 1.2.1 were used org.eclipse.ceylon.test* modules as entry point for tests execution
        if( version.equals("1.2.0") || version.equals("1.1.0") || version.equals("1.0.0") ) {
            throw new ToolUsageError(Messages.msg(bundle, "test.version.incompatible"));
        }
    }

    private String findTestVersionInDependecies(String moduleAndVersion) {
        String moduleName = ModuleUtil.moduleName(moduleAndVersion);
        String moduleVersion = ModuleUtil.moduleVersion(moduleAndVersion);
        ModuleDependencyInfo root = new ModuleDependencyInfo(null, moduleName, moduleVersion, false, false);
        Queue<ModuleDependencyInfo> queue = new LinkedList<ModuleDependencyInfo>();
        queue.add(root);
        
        while(!queue.isEmpty()) {
            String foundVersion = findTestVersionInDependecies(queue.poll(), queue);
            if( foundVersion != null ) {
                return foundVersion;
            }
        }
        
        return null;
    }

    private String findTestVersionInDependecies(ModuleDependencyInfo module, Queue<ModuleDependencyInfo> queue) {
        Collection<ModuleVersionDetails> moduleDetailsCollection = getModuleVersions(
                module.getNamespace(), module.getName(), module.getVersion(), false,
                type, jvmBinaryMajor, jvmBinaryMinor, jsBinaryMajor, jsBinaryMinor);
        Iterator<ModuleVersionDetails> moduleDetailsIterator = moduleDetailsCollection.iterator();
        if( moduleDetailsIterator.hasNext() ) {
            ModuleVersionDetails moduleDetails = moduleDetailsIterator.next();
            for (ModuleDependencyInfo dependency : moduleDetails.getDependencies()) {
                if( dependency.getName().equals("ceylon.test") ) {
                    return dependency.getVersion();
                }
                if( dependency.getName().equals("ceylon.language") ) {
                    continue;
                }
                queue.add(dependency);
            }
        }
        return null;
    }
    
}
