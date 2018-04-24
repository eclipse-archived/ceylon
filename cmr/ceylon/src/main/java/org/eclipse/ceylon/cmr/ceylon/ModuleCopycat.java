/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.ceylon;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;

import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.api.ModuleDependencyInfo;
import org.eclipse.ceylon.cmr.api.ModuleQuery;
import org.eclipse.ceylon.cmr.api.ModuleVersionDetails;
import org.eclipse.ceylon.cmr.api.ModuleVersionQuery;
import org.eclipse.ceylon.cmr.api.ModuleVersionResult;
import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.cmr.impl.CMRJULLogger;
import org.eclipse.ceylon.cmr.impl.DefaultRepository;
import org.eclipse.ceylon.common.ModuleSpec;
import org.eclipse.ceylon.common.ModuleUtil;
import org.eclipse.ceylon.common.log.Logger;
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.loader.JdkProvider;

/**
 * Class that can be used to copy modules from one repository to another.
 * Specific artifact types can be selected for copying while others 
 * will be skipped. It's also possible to recursively copy all dependencies.
 * 
 * NB: Feed back gives information about the total number of modules to copy
 * and the current number (to be) copied. This information is not entirely
 * correct because the total gets updated when new dependencies get found.
 * This is done because retrieving dependencies is actually one of the
 * slowest parts of the entire copy process. If we'd do it beforehand there
 * not be much sense in having a progress report at all.
 * @author Tako Schotanus
 */
public class ModuleCopycat {
    private RepositoryManager srcRepoman;
    private RepositoryManager dstRepoman;
    private CopycatFeedback feedback;
    private Logger log;
    private JdkProvider jdkProvider;
    private boolean includeLanguage;
    private Set<String> excludeModules;
    
    private Set<String> copiedModules;
    private int count;
    private int maxCount;

    /**
     * Class for feedback and control during copying.
     * The <code>beforeCopyModule</code> and <code>beforeCopyArtifact</code> should return
     * <code>true</code> to indicate they want the specified module or artifact to be copied
     * or skipped otherwise.
     * The <code>afterCopyModule</code> and <code>afterCopyArtifact</code> will get a boolean
     * passed if the copy was actually done or not (the copy algorithm might decide to skip the
     * module or artifact anyway, even if you returned <code>true</code> from the "before" method.
     */
    public static interface CopycatFeedback {
        boolean beforeCopyModule(ArtifactContext ac, int count, int max) throws Exception;
        void afterCopyModule(ArtifactContext ac, int count, int max, boolean copied) throws Exception;
        boolean beforeCopyArtifact(ArtifactContext ac, ArtifactResult ar, int count, int max) throws Exception;
        void afterCopyArtifact(ArtifactContext ac, ArtifactResult ar, int count, int max, boolean copied) throws Exception;
        void notFound(ArtifactContext ac) throws Exception;
    }
    
    /**
     * Set up the object with the given source and destination repositories
     * @param srcRepoman The source repository to copy from
     * @param dstRepoman The destination repository to copy to
     */
    public ModuleCopycat(RepositoryManager srcRepoman, RepositoryManager dstRepoman) {
        this(srcRepoman, dstRepoman, null);
    }
    
    /**
     * Set up the object with the given source and destination repositories and callback
     * interface to receive feedback on progress and errors
     * @param srcRepoman The source repository to copy from
     * @param dstRepoman The destination repository to copy to
     * @param feedback Instance of ModuleCopycat.CopycatFeedback for receiving feedback
     */
    public ModuleCopycat(RepositoryManager srcRepoman, RepositoryManager dstRepoman, CopycatFeedback feedback) {
        this(srcRepoman, dstRepoman, new CMRJULLogger(), feedback);
    }
    
    /**
     * Set up the object with the given source and destination repositories and callback
     * interface to receive feedback on progress and errors
     * @param srcRepoman The source repository to copy from
     * @param dstRepoman The destination repository to copy to
     * @param log The logger to use
     * @param feedback Instance of ModuleCopycat.CopycatFeedback for receiving feedback
     */
    public ModuleCopycat(RepositoryManager srcRepoman, RepositoryManager dstRepoman, Logger log, CopycatFeedback feedback) {
        assert(srcRepoman != null);
        assert(dstRepoman != null);
        assert(log != null);
        this.srcRepoman = srcRepoman;
        this.dstRepoman = dstRepoman;
        this.feedback = feedback;
        this.log = log;
        this.copiedModules = new HashSet<String>();
        this.excludeModules = new HashSet<String>();
        // FIXME: probably needs to be an option
        this.jdkProvider = new JdkProvider();
    }
    
    /**
     * Determines if the language module and it's dependencies should be copied as well.
     * By default the language module and it's dependencies will be skipped.
     * For this to work <code>isIgnoreDependencies()</code> must not be set to true
     * on the toplevel artifact context to copy.
     * @param includeLanguage
     * @return this object for chaining
     */
    public ModuleCopycat includeLanguage(boolean includeLanguage) {
        this.includeLanguage = includeLanguage;
        return this;
    }
    
    /**
     * Defines a collection of module names to be excluded from copying.
     * A module name can end in <code>*</code> to signify that any module
     * that has a name starting with such a string will be excluded.
     * @param excludeModules A collection of module names
     * @return this object for chaining
     */
    public ModuleCopycat excludeModules(Collection<String> excludeModules) {
        this.excludeModules = new HashSet<String>(excludeModules);
        return this;
    }

    /**
     * This method basically calls <code>copyModule</code> on each of the artifact
     * contexts in the list it gets passed.
     * @param contexts
     * @throws Exception
     */
    public void copyModules(List<ArtifactContext> contexts) throws Exception {
        init();
        count = 0;
        maxCount = contexts.size();
        for (ArtifactContext context : contexts) {
            copyModuleInternal(context);
        }
    }
    
    /**
     * Method to copy the given module from the source repository to the destination repository.
     * The context's "suffixes" will be used to determine which artifact types will be copied.
     * If the `isFetchSingleArtifact` is not set all the module's dependencies will also be copied.
     * Repeated call to this method with the same module/version will only result in a single copy!  
     * @param context The ArtifactContext containing the information on the module to copy
     * @throws Exception Can throw RepositoryException or any exception that was thrown by code
     * in the "feedback" callback interface
     */
    public void copyModule(ArtifactContext context) throws Exception {
        init();
        count = 0;
        maxCount = 1;
        copyModuleInternal(context);
    }
    
    private void init() {
        if (!includeLanguage) {
            excludeModules.add("ceylon.language");
        }
    }
    
    private void copyModuleInternal(ArtifactContext context) throws Exception {
        assert(context != null);
        if (!shouldExclude(context.getName())) {
            String module = ModuleUtil.makeModuleName(context.getName(), context.getVersion());
            // Skip all duplicates and artifacts from repositories that don't support copying
            if (!copiedModules.add(module) || !canBeCopied(context)) {
                // Faking a copy here for feedback because it was already done and we never copy twice
                if (feedback != null) {
                    feedback.beforeCopyModule(context, count++, maxCount);
                }
                if (feedback != null) {
                    feedback.afterCopyModule(context, count, maxCount, false);
                }
                return;
            }
            Collection<ModuleVersionDetails> versions = getModuleVersions(srcRepoman, context.getName(), context.getVersion(), 
                    ModuleQuery.Type.ALL, null, null, null, null);
            if (!versions.isEmpty()) {
                ArtifactContext depContext = context.copy();
                ModuleVersionDetails ver = versions.iterator().next();
                boolean copyModule = true;
                if (feedback != null) {
                    copyModule = feedback.beforeCopyModule(context, count++, maxCount);
                }
                boolean copiedModule = false;
                if (copyModule) {
                    List<ArtifactResult> results = srcRepoman.getArtifactResults(context);
                    int artCnt = 0;
                    for (ArtifactResult r : results) {
                        boolean copyArtifact = true;
                        if (feedback != null) {
                            copyArtifact = feedback.beforeCopyArtifact(context, r, artCnt++, results.size());
                        }
                        boolean copied = copyArtifact && copyArtifact(context, r);
                        if (feedback != null) {
                            feedback.afterCopyArtifact(context, r, artCnt, results.size(), copied);
                        }
                        copiedModule |= copied;
                    }
                }
                if (feedback != null) {
                    feedback.afterCopyModule(context, count, maxCount, copiedModule);
                }
                if (copyModule && !context.isIgnoreDependencies()) {
                    maxCount += countNonExcludedDeps(ver.getDependencies());
                    for (ModuleDependencyInfo dep : ver.getDependencies()) {
                        if (skipDependency(dep)) {
                            continue;
                        }
                        ModuleSpec depModule = new ModuleSpec(dep.getNamespace(), dep.getName(), dep.getVersion());
                        ArtifactContext copyContext = depContext.copy();
                        copyContext.setNamespace(dep.getNamespace());
                        copyContext.setName(depModule.getName());
                        copyContext.setVersion(depModule.getVersion());
                        copyModuleInternal(copyContext);
                    }
                }
            } else {
                if (feedback != null) {
                    feedback.notFound(context);
                }
            }
        }
    }

    private boolean skipDependency(ModuleDependencyInfo dep) {
        return shouldExclude(dep.getName()) | dep.getNamespace() != null;
    }
    
    // Can the artifact be copied?
    private boolean canBeCopied(ArtifactContext context) {
        // Only allow modules from the "ceylon" namespace to be copied
        return context.getNamespace() == null
                || DefaultRepository.NAMESPACE.equals(context.getNamespace());
    }

    private int countNonExcludedDeps(NavigableSet<ModuleDependencyInfo> dependencies) {
        int cnt = 0;
        for (ModuleDependencyInfo dep : dependencies) {
            if (!shouldExclude(dep.getName())
                    && !skipDependency(dep)) {
                cnt++;
            }
        }
        return cnt;
    }

    private boolean shouldExclude(String moduleName) {
        if (jdkProvider.isJDKModule(moduleName) ||
                excludeModules.contains(moduleName)) {
            return true;
        }
        for (String ex : excludeModules) {
            if (ex.endsWith("*") &&
                    moduleName.startsWith(ex.substring(0, ex.length() - 1))) {
                return true;
            }
        }
        return false;
    }

    private Collection<ModuleVersionDetails> getModuleVersions(RepositoryManager repoMgr, String name, String version, 
            ModuleQuery.Type type, 
            Integer jvmBinaryMajor, Integer jvmBinaryMinor,
            Integer jsBinaryMajor, Integer jsBinaryMinor) {
        ModuleVersionQuery query = new ModuleVersionQuery(null, name, version, type);
        if (jvmBinaryMajor != null) {
            query.setJvmBinaryMajor(jvmBinaryMajor);
        }
        if (jvmBinaryMinor != null) {
            query.setJvmBinaryMinor(jvmBinaryMinor);
        }
        if (jsBinaryMajor != null) {
            query.setJsBinaryMajor(jsBinaryMajor);
        }
        if (jsBinaryMinor != null) {
            query.setJsBinaryMinor(jsBinaryMinor);
        }
        ModuleVersionResult result = repoMgr.completeVersions(query);
        NavigableMap<String, ModuleVersionDetails> versionMap = result.getVersions();
        return versionMap.values();
    }
    
    private boolean copyArtifact(ArtifactContext orgac, ArtifactResult ar) throws Exception {
        ArtifactContext ac = orgac.copy();
        // Make sure we set the correct suffix for the put
        String suffix = ArtifactContext.getSuffixFromFilename(ar.artifact().getName());
        ac.setSuffixes(suffix);
        // Store the artifact
        dstRepoman.putArtifact(ac, ar.artifact());
        // SHA1 it if required
        signArtifact(ac, ar.artifact());
        return true;
    }

    private void signArtifact(ArtifactContext context, File jarFile){
        ShaSigner.signArtifact(dstRepoman, context, jarFile, log);
    }
}
