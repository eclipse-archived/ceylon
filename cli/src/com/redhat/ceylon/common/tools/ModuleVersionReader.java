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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.redhat.ceylon.cmr.api.ModuleDependencyInfo;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.common.Backends;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.ModuleDescriptorReader;
import com.redhat.ceylon.common.ModuleDescriptorReader.NoSuchModuleException;

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
