/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.tools.moduleloading;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.api.ModuleQuery;
import org.eclipse.ceylon.cmr.ceylon.loader.ModuleNotFoundException;
import org.eclipse.ceylon.cmr.ceylon.loader.ModuleGraph.Module;
import org.eclipse.ceylon.common.Messages;
import org.eclipse.ceylon.common.ModuleSpec;
import org.eclipse.ceylon.common.ModuleUtil;
import org.eclipse.ceylon.common.Versions;
import org.eclipse.ceylon.common.config.DefaultToolOptions;
import org.eclipse.ceylon.common.tool.Description;
import org.eclipse.ceylon.common.tool.Option;
import org.eclipse.ceylon.common.tool.OptionArgument;
import org.eclipse.ceylon.common.tool.ToolUsageError;
import org.eclipse.ceylon.common.tools.CeylonTool;
import org.eclipse.ceylon.common.tools.RepoUsingTool;
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.cmr.JDKUtils;
import org.eclipse.ceylon.model.cmr.ModuleScope;
import org.eclipse.ceylon.model.cmr.JDKUtils.JDK;
import org.eclipse.ceylon.model.loader.JdkProvider;

public abstract class ModuleLoadingTool extends RepoUsingTool {

    protected boolean upgradeDist = DefaultToolOptions.getLinkWithCurrentDistribution();
    // start out with the JDK one and change in initialise()
    protected JdkProvider jdkProvider = new JdkProvider();
    protected String jdkProviderModule;
    protected ToolModuleLoader loader;

    public ModuleLoadingTool() {
        super(ModuleLoadingMessages.RESOURCE_BUNDLE);
    }
    
    @Option
    @Description("Downgrade which were compiled with a more recent "
            + "version of the distribution to the version of that module "
            + "present in this distribution (" + Versions.CEYLON_VERSION_NUMBER + "). "
            + "This might fail with a linker error at runtime. For example "
            + "if the module depended on an API present in the more "
            + "recent version, but absent from " + Versions.CEYLON_VERSION_NUMBER +". "
                    + "Allowed arguments are upgrade, downgrade or abort. Default: upgrade")
    public void setLinkWithCurrentDistribution(boolean downgradeDist) {
        this.upgradeDist = !downgradeDist;
    }
    
    @Description("Alternate JDK provider module (defaults to the current running JDK).")
    @OptionArgument(argumentName="module")
    public void setJdkProvider(String jdkProviderModule) {
        this.jdkProviderModule = jdkProviderModule;
    }

    @Override
    protected boolean shouldUpgradeDist(){
        return upgradeDist;
    }
    
    protected String moduleVersion(String moduleNameOptVersion) throws IOException {
        return checkModuleVersionsOrShowSuggestions(
                ModuleUtil.moduleName(moduleNameOptVersion),
                ModuleUtil.moduleVersion(moduleNameOptVersion),
                ModuleQuery.Type.JVM,
                Versions.JVM_BINARY_MAJOR_VERSION,
                Versions.JVM_BINARY_MINOR_VERSION,
                // JS binary but don't care since JVM
                null, null);

    }
    
    protected boolean loadModule(String namespace, String moduleName, String moduleVersion) throws IOException {
        if (moduleVersion != null) {
            return internalLoadModule(namespace, moduleName, moduleVersion);
        }
        return false;
    }

    protected boolean shouldExclude(String moduleName, String version) {
        // FIXME: update for Android/JDK9
        if(JDKUtils.jdk.providesVersion(JDK.JDK9.version)){
            moduleName = JDKUtils.getJava9ModuleName(moduleName, version);
        }
        return jdkProvider.isJDKModule(moduleName);
    }

    protected boolean isProvided(String moduleName, String version) {
        return false;
    }

    private boolean internalLoadModule(String namespace, String name, String version) throws IOException {
        try {
            return loader.loadModuleForTool(name, version, ModuleScope.RUNTIME);
        } catch (ModuleNotFoundException e) {
            // this should not happen, since we collect errors, but just in case…
            String err = getModuleNotFoundErrorMessage(getRepositoryManager(), name, version);
            errorAppend(err);
            errorNewline();
            return false;
        }
    }
    
    /**
     * For subclasses.
     */
    protected boolean skipDependency(ArtifactResult dep) {
        return false;
    }

    protected void errorOnConflictingModule(String module, String version) throws IOException{
        boolean duplicate = false;
        for(Map.Entry<String, SortedSet<String>> entry : loader.getDuplicateModules().entrySet()){
            duplicate = true;
            printDuplicateModuleErrorMessage(entry.getKey(), entry.getValue());
        }
        if(duplicate)
            throw new ToolUsageError(Messages.msg(bundle, "module.conflict.error", module, version));
    }

    private void printDuplicateModuleErrorMessage(String name, SortedSet<String> versions) throws IOException {
        StringBuilder err = new StringBuilder();
        boolean first = true;
        for(String version : versions){
            if(first)
                first = false;
            else
                err.append(", ");
            err.append(version);
        }
        errorMsg("module.duplicate.error", name, err, versions.last());
    }
    
    protected String[] getLoaderSuffixes() {
        return new String[] { ArtifactContext.CAR, ArtifactContext.JAR };
    }

    @Override
    public void initialize(CeylonTool mainTool) throws Exception {
        super.initialize(mainTool);
        loader = new ToolModuleLoader(this, getRepositoryManager(), getLoaderSuffixes());
        if(jdkProviderModule != null){
            ModuleSpec moduleSpec = ModuleSpec.parse(jdkProviderModule);
            if(!internalLoadModule(null, moduleSpec.getName(), moduleSpec.getVersion())){
                throw new ToolUsageError(Messages.msg(bundle, "jdk.provider.not.found", jdkProviderModule));
            }
            ArtifactResult result = loader.getModuleArtifact(moduleSpec.getName());
            jdkProvider = new JdkProvider(moduleSpec.getName(), moduleSpec.getVersion(), null, result.artifact());
        }
        // else keep the JDK one
    }

    public void handleMissingModuleError(String name, String version) {
        try{
            String err = getModuleNotFoundErrorMessage(getRepositoryManager(), name, version);
            errorAppend(err);
            errorNewline();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean includeOptionalDependencies() {
        return false;
    }

    public void cycleDetected(List<Module> path) {
        // log it as a warning?
    }
}
