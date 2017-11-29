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

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.ceylon.cmr.api.ModuleDependencyInfo;
import org.eclipse.ceylon.cmr.api.ModuleVersionDetails;
import org.eclipse.ceylon.common.Backends;
import org.eclipse.ceylon.common.Constants;
import org.eclipse.ceylon.common.FileUtil;
import org.eclipse.ceylon.common.ModuleUtil;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;

/**
 * This class takes a list of source files and determines which modules
 * are referenced as dependencies by them that were not mentioned in the
 * original list.
 * 
 * @author Tako Schotanus (tako@ceylon-lang.org)
 */
public class SourceDependencyResolver {
    private final ModuleVersionReader moduleVersionReader;
    private final Iterable<File> sourceDirs;
    private final Backends forBackends;
    
    private Set<ModuleVersionDetails> originalModules;
    private Set<ModuleVersionDetails> allModules;
    private Set<ModuleVersionDetails> additionalModules;
    
    public SourceDependencyResolver(ModuleVersionReader moduleVersionReader, Iterable<File> sourceDirs, Backends forBackends) {
        this.moduleVersionReader = moduleVersionReader;
        this.sourceDirs = sourceDirs;
        this.forBackends = forBackends;
    }
    
    /**
     * Returns the list of modules as found in the list of sources
     * passed to <code>traverseDependencies()</code>
     * @return A list of <code>ModuleVersionDetails</code>
     */
    public Set<ModuleVersionDetails> getOriginalModules() {
        return originalModules;
    }

    /**
     * Returns the list of modules as found in the list of sources
     * passed to <code>traverseDependencies()</code> and the ones
     * that were found while traversing their dependencies
     * @return A list of <code>ModuleVersionDetails</code>
     */
    public Set<ModuleVersionDetails> getAllModules() {
        return allModules;
    }

    /**
     * Returns the list of dependent modules that were found while
     * traversing the list of sources passed to
     * <code>traverseDependencies()</code> that were not mentioned
     * in the original list of sources
     * @return A list of <code>ModuleVersionDetails</code>
     */
    public Set<ModuleVersionDetails> getAdditionalModules() {
        return additionalModules;
    }

    /**
     * Traverses the given sources finding all dependencies that have
     * their sources available in the source directories
     * @param sourceFiles Iterable of <File> pointing to sources
     * @return A boolean indicating if any additional modules were found
     */
    public boolean traverseDependencies(Iterable<File> sourceFiles) {
        originalModules = collectModulesFromSources(sourceFiles);
        allModules = new HashSet<ModuleVersionDetails>(originalModules);
        for (ModuleVersionDetails module : originalModules) {
            collectModulesFromDependencies(allModules, module);
        }
        additionalModules = new HashSet<ModuleVersionDetails>(allModules);
        additionalModules.removeAll(originalModules);
        return !additionalModules.isEmpty();
    }

    // Recursively traverses and collects all dependencies from the
    // given module and adds their dependencies to the given set, but
    // only those that have locally available sources
    private void collectModulesFromDependencies(Set<ModuleVersionDetails> modules, String moduleName) {
        ModuleVersionDetails mvd = moduleVersionReader.fromSource(moduleName);
        if (mvd != null) {
            modules.add(mvd);
            collectModulesFromDependencies(modules, mvd);
        }
    }
    
    // Recursively traverses and collects all dependencies from the
    // given module and adds their dependencies to the given set, but
    // only those that have locally available sources
    private void collectModulesFromDependencies(Set<ModuleVersionDetails> modules, ModuleVersionDetails mvd) {
        for (ModuleDependencyInfo dep : mvd.getDependencies()) {
            if (dep.getNamespace() == null
                    && !modules.contains(dep)
                    && hasSources(dep.getName())
                    && ModelUtil.isForBackend(dep.getNativeBackends(), forBackends)) {
                collectModulesFromDependencies(modules, dep.getName());
            }
        }
    }
    
    // Determines if the given module has locally available sources
    private boolean hasSources(String moduleName) {
        for (File srcDir : sourceDirs) {
            File moduleDir = ModuleUtil.moduleToPath(srcDir, moduleName);
            if (moduleDir.isDirectory() && moduleDir.canRead()) {
                File descriptor = new File(moduleDir, Constants.MODULE_DESCRIPTOR);
                if (descriptor.isFile() && descriptor.canRead()) {
                    return true;
                }
            }
        }
        return false;
    }

    // Given the list of _all_ source files we return the list of module details
    private Set<ModuleVersionDetails> collectModulesFromSources(Iterable<File> sourceFiles) {
        Set<ModuleVersionDetails> modules = new HashSet<ModuleVersionDetails>();
        Set<File> descriptors = collectModuleDescriptorsFromSources(sourceFiles);
        for (File d : descriptors) {
            String moduleName = moduleNameFromDescriptorFile(d);
            ModuleVersionDetails mvd = moduleVersionReader.fromSource(moduleName);
            if (mvd != null) {
                modules.add(mvd);
            }
        }
        return modules;
    }
    
    // Given a descriptor File we return its module name
    private String moduleNameFromDescriptorFile(File descriptor) {
        String rel = FileUtil.relativeFile(sourceDirs, descriptor.getParentFile().getPath());
        return ModuleUtil.pathToModule(new File(rel));
    }

    // Given a list of source files returns only the module descriptors
    private Set<File> collectModuleDescriptorsFromSources(Iterable<File> sourceFiles) {
        Set<File> files = new HashSet<File>();
        for (File f : sourceFiles) {
            if (Constants.MODULE_DESCRIPTOR.equals(f.getName())) {
                files.add(f);
            }
        }
        return files;
    }
}
