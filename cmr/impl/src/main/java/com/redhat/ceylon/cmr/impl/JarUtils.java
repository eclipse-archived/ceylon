/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted
 * by the author tags.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redhat.ceylon.cmr.impl;

import java.io.File;
import java.util.Set;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.DependencyResolver;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.ModuleVersionArtifact;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.spi.Node;

/**
 * Utility functions to retrieve module meta information from legacy JAR files
 * that have either a module.xml or a module.properties file 
 *
 * @author <a href="mailto:tako@ceylon-lang.org">Tako Schotanus</a>
 */
public final class JarUtils implements DependencyResolver, ModuleInfoReader {
    public static JarUtils INSTANCE = new JarUtils();

    private JarUtils() {
    }

    @Override
    public Set<ModuleInfo> resolve(ArtifactResult result) {
        File mod = result.artifact();
        if (mod != null && mod.getName().toLowerCase().endsWith(ArtifactContext.JAR)) {
            return getDependencies(result.artifact());
        } else {
            return null;
        }
    }
    
    @Override
    public Set<ModuleInfo> resolveFromFile(File file) {
        return getDependenciesFromFile(file);
    }

    @Override
    public Node descriptor(Node artifact) {
        throw new UnsupportedOperationException("Operation not implemented");
    }

    @Override
    public ModuleVersionDetails readModuleInfo(String moduleName, File moduleArchive) {
        ModuleVersionDetails mvd = new ModuleVersionDetails(getVersionFromFilename(moduleName, moduleArchive.getName()));
        mvd.getArtifactTypes().add(new ModuleVersionArtifact(ArtifactContext.JAR, null, null));
        Set<ModuleInfo> deps = getDependencies(moduleArchive);
        if (deps != null) {
            mvd.getDependencies().addAll(deps);
        }
        return mvd;
    }

    private static Set<ModuleInfo> getDependencies(File moduleArchive) {
        Set<ModuleInfo> deps = null;
        File xml = new File(moduleArchive.getParentFile(), "module.xml");
        if (xml.isFile()) {
            deps = getDependenciesFromFile(xml);
        } else {
            File props = new File(moduleArchive.getParentFile(), "module.properties");
            if (props.isFile()) {
                deps = getDependenciesFromFile(props);
            }
        }
        return deps;
    }
    
    private static Set<ModuleInfo> getDependenciesFromFile(File descriptorFile) {
        Set<ModuleInfo> deps = null;
        if (descriptorFile.isFile() && descriptorFile.getName().equalsIgnoreCase("module.xml")) {
            XmlDependencyResolver res = XmlDependencyResolver.INSTANCE;
            deps = res.resolveFromFile(descriptorFile);
        } else {
            if (descriptorFile.isFile() && descriptorFile.getName().equalsIgnoreCase("module.properties")) {
                PropertiesDependencyResolver res = PropertiesDependencyResolver.INSTANCE;
                deps = res.resolveFromFile(descriptorFile);
            }
        }
        return deps;
    }
    
    private static String getVersionFromFilename(String moduleName, String name) {
        if (!"default".equals(moduleName)) {
            String type = ArtifactContext.getSuffixFromFilename(name);
            return name.substring(moduleName.length() + 1, name.length() - type.length());
        } else {
            return "";
        }
    }

    @Override
    public boolean matchesModuleInfo(String moduleName, File moduleArchive, String query) {
        Set<ModuleInfo> deps = getDependencies(moduleArchive);
        if (deps != null) {
            for (ModuleInfo dep : deps) {
                if (matches(dep.getModuleName(), query))
                    return true;
            }
        }
        return false;
    }

    private static boolean matches(String string, String query) {
        return string.toLowerCase().contains(query);
    }

    @Override
    public int[] getBinaryVersions(String moduleName, File moduleArchive) {
        return null;
    }

}
