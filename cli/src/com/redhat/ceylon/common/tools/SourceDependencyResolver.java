/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.common.tools;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.redhat.ceylon.cmr.api.ModuleDependencyInfo;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.ModuleDescriptorReader;
import com.redhat.ceylon.common.ModuleDescriptorReader.NoSuchModuleException;
import com.redhat.ceylon.common.ModuleUtil;

/**
 * This class takes a list of source files and determines which modules
 * are referenced as dependencies by them that were not mentioned in the
 * original list.
 * 
 * @author Tako Schotanus (tako@ceylon-lang.org)
 */
public class SourceDependencyResolver {
    private final Iterable<File> sourceDirs;
    
    private File cwd;

    private Set<ModuleVersionDetails> originalModules;
    private Set<ModuleVersionDetails> allModules;
    private Set<ModuleVersionDetails> additionalModules;
    
    public SourceDependencyResolver(Iterable<File> sourceDirs) {
        this.sourceDirs = sourceDirs;
    }
    
    /**
     * Sets the current working directory
     * @param cwd A <File> pointing to a directory
     * @return This object for chaining
     */
    public SourceDependencyResolver cwd(File cwd) {
        this.cwd = cwd;
        return this;
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
        ModuleVersionDetails mvd = getModuleVersionDetailsFromSource(cwd, sourceDirs, moduleName);
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
                    && hasSources(dep.getName())) {
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
            ModuleVersionDetails mvd = getModuleVersionDetailsFromSource(cwd, sourceDirs, moduleName);
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
        ModuleVersionDetails mvd = new ModuleVersionDetails(null, module != null ? module : "", version != null ? version : "");
        mvd.setLicense(mdr.getModuleLicense());
        List<String> by = mdr.getModuleAuthors();
        if (by != null) {
            mvd.getAuthors().addAll(by);
        }
        SortedSet<ModuleDependencyInfo> dependencies = new TreeSet<>();
        for(Object[] dep : mdr.getModuleImports()){
            dependencies.add(new ModuleDependencyInfo((String)dep[0], (String)dep[1], (String)dep[2], (Boolean)dep[3], (Boolean)dep[4]));
        }
        mvd.setDependencies(dependencies);
        mvd.setRemote(false);
        mvd.setOrigin("Local source folder");
        return mvd;
    }
}
