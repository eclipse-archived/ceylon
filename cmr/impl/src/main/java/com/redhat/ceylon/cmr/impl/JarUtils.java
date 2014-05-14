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
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.DependencyResolver;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.ModuleVersionArtifact;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.common.ModuleUtil;

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
    public Set<ModuleInfo> resolveFromInputStream(InputStream stream) {
        throw new UnsupportedOperationException("Cannot resolve from stream");
    }

    @Override
    public Node descriptor(Node artifact) {
        Node parent = NodeUtils.firstParent(artifact);
        Node descriptor = parent .getChild(ArtifactContext.MODULE_XML);
        if (descriptor == null) {
            descriptor = parent.getChild(ArtifactContext.MODULE_PROPERTIES);
        }
        return descriptor;
    }

    @Override
    public ModuleVersionDetails readModuleInfo(String moduleName, File moduleArchive, boolean includeMembers) {
        ModuleVersionDetails mvd = new ModuleVersionDetails(getVersionFromFilename(moduleName, moduleArchive.getName()));
        mvd.getArtifactTypes().add(new ModuleVersionArtifact(ArtifactContext.JAR, null, null));
        Set<ModuleInfo> deps = getDependencies(moduleArchive);
        if (deps != null) {
            mvd.getDependencies().addAll(deps);
        }
        if (includeMembers) {
            mvd.setMembers(getMembers(moduleArchive));
        }
        return mvd;
    }

    private Set<String> getMembers(File moduleArchive) {
        try {
            return gatherClassnamesFromJar(moduleArchive);
        } catch (IOException e) {
            throw new RuntimeException("Failed to retrieve members for module " + moduleArchive.getPath(), e);
        }
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
        if (!ModuleUtil.isDefaultModule(moduleName)) {
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

    // Return the set of fully qualified names for all the classes
    // in the JAR pointed to by the given file
    public static Set<String> gatherClassnamesFromJar(File jar) throws IOException {
        HashSet<String> names = new HashSet<>();
        JarFile zf = new JarFile(jar);
        try {
            Enumeration<? extends JarEntry> entries = zf.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                    String name = entry.getName();
                    String className = name.substring(0, name.length() - 6).replace('/', '.');
                    names.add(className);
                }
            }
        } finally {
            zf.close();
        }
        return names;
    }

}
