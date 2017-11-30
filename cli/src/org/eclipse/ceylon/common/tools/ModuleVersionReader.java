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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.ceylon.cmr.api.ModuleDependencyInfo;
import org.eclipse.ceylon.cmr.api.ModuleVersionDetails;
import org.eclipse.ceylon.common.Backends;
import org.eclipse.ceylon.common.FileUtil;
import org.eclipse.ceylon.common.ModuleDescriptorReader;
import org.eclipse.ceylon.common.ModuleDescriptorReader.NoSuchModuleException;

public class ModuleVersionReader {
    private final Iterable<File> sourceDirs;
    
    private File cwd;
    
    private Map<String, ModuleVersionDetails> cache;
    
    public ModuleVersionReader(Iterable<File> sourceDirs) {
        this.sourceDirs = sourceDirs;
        cache = new HashMap<String, ModuleVersionDetails>();
    }
    
    /**
     * Sets the current working directory
     * @param cwd A <File> pointing to a directory
     * @return This object for chaining
     */
    public ModuleVersionReader cwd(File cwd) {
        this.cwd = cwd;
        return this;
    }

    public ModuleVersionDetails fromSource(String moduleName) {
        ModuleVersionDetails mvd = cache.get(moduleName);
        if (mvd == null) {
            mvd = getModuleVersionDetailsFromSource(cwd, sourceDirs, moduleName);
            if (mvd != null) {
                cache.put(moduleName, mvd);
            }
        }
        return mvd;
    }
    
    /**
     * Given a current working directory, a list of source directories and a
     * module name tries to encounter and return the module descriptor
     * @param cwd The current working directory to use
     * @param srcDirs An iterable of (possibly relative) source directories
     * @param moduleName The name of the module who's descriptor to read
     * @return A <code>ModuleVersionsDetails</code> or <code>null</code> if the module wasn't found
     */
    public static ModuleVersionDetails getModuleVersionDetailsFromSource(File cwd, Iterable<File> srcDirs, String moduleName) {
        try {
            for (File srcDir : srcDirs) {
                try {
                    return getModuleVersionDetailsFromSource(moduleName, FileUtil.applyCwd(cwd, srcDir));
                } catch (ModuleDescriptorReader.NoSuchModuleException x){
                    // skip this source folder and look in the next one
                }
            }
        } catch (Exception ex) {
            // Just continue as if nothing happened
        }
        return null;
    }

    /**
     * Reads a module descriptor and returns its information
     * @param moduleName The name of the module
     * @param srcDir The source directory where to find the descriptor
     * @return A <code>ModuleVersionsDetails</code> with the encountered information
     * @throws NoSuchModuleException if the module could not be found
     */
    public static ModuleVersionDetails getModuleVersionDetailsFromSource(String moduleName, File srcDir) throws NoSuchModuleException {
        ModuleDescriptorReader mdr = new ModuleDescriptorReader(moduleName, srcDir);
        String module = mdr.getModuleName();
        String version = mdr.getModuleVersion();
        // PS In case the module descriptor was found but could not be parsed
        // we'll create an invalid details object
        ModuleVersionDetails mvd = new ModuleVersionDetails(module != null ? module : "", version != null ? version : "",
                mdr.getModuleGroupId(), mdr.getModuleArtifactId());
        mvd.setLabel(mdr.getModuleLabel());
        mvd.setLicense(mdr.getModuleLicense());
        List<String> by = mdr.getModuleAuthors();
        if (by != null) {
            mvd.getAuthors().addAll(by);
        }
        SortedSet<ModuleDependencyInfo> dependencies = new TreeSet<>();
        for(Object[] dep : mdr.getModuleImports()){
            dependencies.add(new ModuleDependencyInfo((String)dep[0], (String)dep[1], (String)dep[2], (Boolean)dep[3], (Boolean)dep[4], (Backends)dep[5]));
        }
        mvd.setDependencies(dependencies);
        mvd.setRemote(false);
        mvd.setOrigin("Local source folder");
        return mvd;
    }

}
