/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.tools.copy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.api.ModuleQuery;
import org.eclipse.ceylon.cmr.ceylon.ModuleCopycat;
import org.eclipse.ceylon.common.BooleanUtil;
import org.eclipse.ceylon.common.Messages;
import org.eclipse.ceylon.common.ModuleSpec;
import org.eclipse.ceylon.common.ModuleUtil;
import org.eclipse.ceylon.common.OSUtil;
import org.eclipse.ceylon.common.tool.Argument;
import org.eclipse.ceylon.common.tool.Description;
import org.eclipse.ceylon.common.tool.Option;
import org.eclipse.ceylon.common.tool.OptionArgument;
import org.eclipse.ceylon.common.tool.RemainingSections;
import org.eclipse.ceylon.common.tool.Summary;
import org.eclipse.ceylon.common.tool.ToolUsageError;
import org.eclipse.ceylon.common.tools.CeylonTool;
import org.eclipse.ceylon.common.tools.OutputRepoUsingTool;
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.tools.war.CeylonWarMessages;

@Summary("Copies modules from one Ceylon module repository to another")
@Description("Copies a module or a set of modules from one repository " +
        "to another. If set for recursive copying it will also copy " +
        "all the module's dependencies and their dependencies until the " +
        "entire module tree has been copied. NB: This tool will only copy " +
        "between Ceylon repositories.")
@RemainingSections(OutputRepoUsingTool.DOCSECTION_REPOSITORIES)
public class CeylonCopyTool extends OutputRepoUsingTool {
    
    private List<ModuleSpec> modules;
    private List<String> excludeModules = new ArrayList<>();
    private boolean withDependencies;
    private boolean includeLanguage;
    
    private Boolean jvm;
    private Boolean js;
    private Boolean dart;
    private Boolean docs;
    private Boolean src;
    private Boolean scripts;
    private Boolean all;
    
    public CeylonCopyTool() {
        super(CeylonCopyMessages.RESOURCE_BUNDLE);
    }

    @Argument(argumentName="module", multiplicity="+")
    public void setModules(List<String> modules) {
        setModuleSpecs(ModuleSpec.parseEachList(modules));
    }
    
    public void setModuleSpecs(List<ModuleSpec> modules) {
        this.modules = modules;
    }
    
    @OptionArgument(argumentName="moduleOrFile", shortName='x')
    @Description("Excludes modules from being copied. Can be a module name or " +
            "a file containing module names. Can be specified multiple times. " +
            "A module name can end in `*` to signify that any module that has " + 
            "a name starting with such a string will be excluded.")
    public void setExcludeModule(List<String> exclusions) {
        for (String each : exclusions) {
            File xFile = new File(each);
            if (xFile.exists() && xFile.isFile()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(xFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        this.excludeModules.add(line);
                    }
                } catch (IOException e) {
                    throw new ToolUsageError(CeylonWarMessages.msg("exclude.file.failure", each), 
                            e);
                }
            } else {
                this.excludeModules.add(each);
            }
        }
    }

    @Option(longName="with-dependencies", shortName='r')
    @Description("Recursively copy all dependencies")
    public void setWithDependencies(boolean withDependencies) {
        this.withDependencies = withDependencies;
    }
    
    @Option
    @Description("Determines if the language module should be copied as well, implies `--with-dependencies` (default is `false`)")
    public void setIncludeLanguage(boolean includeLanguage) {
        this.includeLanguage = includeLanguage;
    }

    @Option
    @Description("Include artifacts compiled for the JVM (`.car` and `.jar`) (default: `true`)")
    public void setJvm(boolean jvm) {
        this.jvm = jvm;
    }

    @Option
    @Description("Include artifacts compiled for JavaScript (`.js` and `-model.js`) (default: `true`)")
    public void setJs(boolean js) {
        this.js = js;
    }

    @Option
    @Description("Include artifacts compiled for Dart (`.dart` and `-dartmodel.json`) (default: `true`)")
    public void setDart(boolean dart) {
        this.dart = dart;
    }

    @Option
    @Description("Include documentation (default: `false`)")
    public void setDocs(boolean docs) {
        this.docs = docs;
    }

    @Option
    @Description("Include sources (default: `false`)")
    public void setSrc(boolean src) {
        this.src = src;
    }

    @Option
    @Description("Include scripts (default: `false`)")
    public void setScripts(boolean scripts) {
        this.scripts = scripts;
    }

    @Option
    @Description("Include everything (equivalent to `--jvm`, `--js`, `--docs`, `--src`, `--scripts`) (default: `false`)")
    public void setAll(boolean all) {
        this.all = all;
    }

    @Option(shortName='d')
    @OptionArgument(argumentName = "flags")
    @Description("Produce verbose output. " +
            "If no `flags` are given then be verbose about everything, " +
            "otherwise just be verbose about the flags which are present. " +
            "Allowed flags include: `all`, `loader`, `files`.")
    public void setVerbose(String verbose) {
        this.verbose = verbose;
    }
    
    protected Set<String> getVerboseCategories(String... morecats) {
        return super.getVerboseCategories("files");
    }
    
    @Override
    protected boolean needsSystemRepo() {
        return false;
    }

    @Override
    protected boolean doNotReadFromOutputRepo(){
        return true;
    }

    @Override
    public void initialize(CeylonTool mainTool) {
        withDependencies = withDependencies || includeLanguage;
    }
    
    @Override
    public void run() throws Exception {
        Set<String> artifacts = new LinkedHashSet<String>();
        boolean defaults = js == null 
                && jvm == null
                && dart == null
                && src == null
                && scripts == null
                && docs == null
                && all == null;
        if (BooleanUtil.isTrue(all)) {
            artifacts.addAll(Arrays.asList(ArtifactContext.allSuffixes()));
        }
        if (BooleanUtil.isTrue(js) || defaults) {
            artifacts.add(ArtifactContext.JS);
            artifacts.add(ArtifactContext.JS_MODEL);
            artifacts.add(ArtifactContext.RESOURCES);
        } else if (BooleanUtil.isFalse(js)) {
            artifacts.remove(ArtifactContext.JS);
            artifacts.remove(ArtifactContext.JS_MODEL);
            artifacts.remove(ArtifactContext.RESOURCES);
        }
        if (BooleanUtil.isTrue(jvm) || defaults) {
            // put the CAR first since its presence will shortcut the other three
            artifacts.add(ArtifactContext.CAR);
            artifacts.add(ArtifactContext.JAR);
            artifacts.add(ArtifactContext.MODULE_PROPERTIES);
            artifacts.add(ArtifactContext.MODULE_XML);
        } else if (BooleanUtil.isFalse(jvm)) {
            artifacts.remove(ArtifactContext.CAR);
            artifacts.remove(ArtifactContext.JAR);
            artifacts.remove(ArtifactContext.MODULE_PROPERTIES);
            artifacts.remove(ArtifactContext.MODULE_XML);
        }
        if (BooleanUtil.isTrue(dart) || defaults) {
            artifacts.add(ArtifactContext.DART);
            artifacts.add(ArtifactContext.DART_MODEL);
            artifacts.add(ArtifactContext.RESOURCES);
        } else if (BooleanUtil.isFalse(dart)) {
            artifacts.remove(ArtifactContext.DART);
            artifacts.remove(ArtifactContext.DART_MODEL);
            artifacts.remove(ArtifactContext.RESOURCES);
        }
        if (BooleanUtil.isTrue(src)) {
            artifacts.add(ArtifactContext.SRC);
        } else if (BooleanUtil.isFalse(src)) {
            artifacts.remove(ArtifactContext.SRC);
        }
        if (BooleanUtil.isTrue(scripts)) {
            artifacts.add(ArtifactContext.SCRIPTS_ZIPPED);
        } else if (BooleanUtil.isFalse(scripts)) {
            artifacts.remove(ArtifactContext.SCRIPTS_ZIPPED);
        }
        if (BooleanUtil.isTrue(docs)) {
            artifacts.add(ArtifactContext.DOCS);
        } else if (BooleanUtil.isFalse(docs)) {
            artifacts.remove(ArtifactContext.DOCS);
        }

        // Create the list of ArtifactContexts to copy
        List<ArtifactContext> acs = new ArrayList<ArtifactContext>();
        String[] artifactsArray = new String[artifacts.size()];
        artifacts.toArray(artifactsArray);
        for (ModuleSpec module : modules) {
            if (module != ModuleSpec.DEFAULT_MODULE && !module.isVersioned()) {
                String version = checkModuleVersionsOrShowSuggestions(module.getName(), null, 
                        ModuleQuery.Type.ALL, null, null, null, null);
                module = new ModuleSpec(module.getNamespace(), module.getName(), version);
            }
            ArtifactContext ac = new ArtifactContext(null, module.getName(), module.getVersion(), artifactsArray);
            ac.setIgnoreDependencies(!withDependencies);
            ac.setForceOperation(true);
            acs.add(ac);
        }
        
        // Now do the actual copying
        final boolean logArtifacts = verbose != null && (verbose.contains("all") || verbose.contains("files"));
        ModuleCopycat copier = new ModuleCopycat(getRepositoryManager(), getOutputRepositoryManager(), getLogger(), new ModuleCopycat.CopycatFeedback() {
            boolean haveSeenArtifacts = false;
            @Override
            public boolean beforeCopyModule(ArtifactContext ac, int count, int max) throws IOException {
                String module = ModuleUtil.makeModuleName(ac.getName(), ac.getVersion());
                msg("copying.module", module, count+1, max).flush();
                haveSeenArtifacts = false;
                return true;
            }
            @Override
            public void afterCopyModule(ArtifactContext ac, int count, int max, boolean copied) throws IOException {
                if (!logArtifacts || !haveSeenArtifacts) {
                    String msg;
                    if (copied) {
                        msg = OSUtil.color(Messages.msg(bundle, "copying.ok"), OSUtil.Color.green);
                    } else {
                        msg = OSUtil.color(Messages.msg(bundle, "copying.skipped"), OSUtil.Color.yellow);
                    }
                    if (haveSeenArtifacts) {
                        append(")");
                    }
                    append(" ").append(msg).newline().flush();
                }
            }
            @Override
            public boolean beforeCopyArtifact(ArtifactContext ac, ArtifactResult ar, int count, int max) throws IOException {
                haveSeenArtifacts = true;
                if (logArtifacts) {
                    if (count == 0) {
                        append(" -- ");
                        append(ar.repositoryDisplayString());
                        newline().flush();
                    }
                    append("    ").msg("copying.artifact", ar.artifact().getName(), count+1, max).flush();
                } else {
                    if (count > 0) {
                        append(", ");
                    } else {
                        append(" (");
                    }
                    String name = ArtifactContext.getSuffixFromFilename(ar.artifact().getName());
                    if (name.startsWith(".") || name.startsWith("-")) {
                        name = name.substring(1);
                    } else if ("module-doc".equals(name)) {
                        name = "doc";
                    }
                    append(name);
                }
                return true;
            }
            @Override
            public void afterCopyArtifact(ArtifactContext ac, ArtifactResult ar, int count, int max, boolean copied) throws IOException {
                haveSeenArtifacts = true;
                if (logArtifacts) {
                    append(" ").msg((copied) ? "copying.ok" : "copying.skipped").newline().flush();
                }
            }
            @Override
            public void notFound(ArtifactContext ac) throws IOException {
                String err = getModuleNotFoundErrorMessage(getRepositoryManager(), ac.getName(), ac.getVersion());
                errorAppend(err);
                errorNewline();
            }
        });
        copier.includeLanguage(includeLanguage).excludeModules(excludeModules).copyModules(acs);
    }

}
