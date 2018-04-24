/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.ceylon.cmr.api.AbstractDependencyResolverAndModuleInfoReader;
import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.api.DependencyContext;
import org.eclipse.ceylon.cmr.api.ModuleDependencyInfo;
import org.eclipse.ceylon.cmr.api.ModuleInfo;
import org.eclipse.ceylon.cmr.api.ModuleVersionArtifact;
import org.eclipse.ceylon.cmr.api.ModuleVersionDetails;
import org.eclipse.ceylon.cmr.api.Overrides;
import org.eclipse.ceylon.cmr.spi.Node;
import org.eclipse.ceylon.model.cmr.ArtifactResult;

/**
 * Utility functions to retrieve module meta information from legacy JAR files
 * that have either a module.xml or a module.properties file 
 *
 * @author <a href="mailto:tako@ceylon-lang.org">Tako Schotanus</a>
 */
public final class JarUtils extends AbstractDependencyResolverAndModuleInfoReader {
    public static JarUtils INSTANCE = new JarUtils();

    private JarUtils() {
    }

    @Override
    public ModuleInfo resolve(DependencyContext context, Overrides overrides) {
        if (context.ignoreExternal()) {
            return null;
        }
        ArtifactResult result = context.result();
        File mod = result.artifact();
        if (mod != null && mod.getName().toLowerCase().endsWith(ArtifactContext.JAR)) {
            return getDependencies(result.artifact(), result.name(), result.version(), overrides);
        } else {
            return null;
        }
    }
    
    @Override
    public ModuleInfo resolveFromFile(File file, String name, String version, Overrides overrides) {
        return getDependenciesFromFile(file, name, version, overrides);
    }

    @Override
    public ModuleInfo resolveFromInputStream(InputStream stream, String name, String version, Overrides overrides) {
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
    public ModuleVersionDetails readModuleInfo(String moduleName, String version, File moduleArchive, boolean includeMembers, Overrides overrides) {
        ModuleInfo info = getDependencies(moduleArchive, moduleName, version, overrides);
        ModuleVersionDetails mvd = new ModuleVersionDetails(moduleName, version, info != null ? info.getGroupId() : null, info != null ? info.getArtifactId() : null);
        mvd.getArtifactTypes().add(new ModuleVersionArtifact(ArtifactContext.JAR, null, null));
        if (info != null) {
            mvd.getDependencies().addAll(info.getDependencies());
        }
        if (includeMembers) {
            mvd.setMembers(getMembers(moduleArchive));
        }
        return mvd;
    }

    private Set<String> getMembers(File moduleArchive) {
        try {
            return gatherCeylonNamesFromJar(moduleArchive);
        } catch (IOException e) {
            throw new RuntimeException("Failed to retrieve members for module " + moduleArchive.getPath(), e);
        }
    }

    private static ModuleInfo getDependencies(File moduleArchive, String name, String version, Overrides overrides) {
        // FIXME: also look inside the dependencies for descriptors
        File xml = new File(moduleArchive.getParentFile(), "module.xml");
        ModuleInfo result = getDependenciesFromFile(xml, name, version, overrides);
        if (result != null) {
            return result;
        }

        File props = new File(moduleArchive.getParentFile(), "module.properties");
        return getDependenciesFromFile(props, name, version, overrides);
    }
    
    private static ModuleInfo getDependenciesFromFile(File descriptorFile, String name, String version, Overrides overrides) {
        if (descriptorFile.isFile() == false) {
            return null;
        }

        if (descriptorFile.getName().equalsIgnoreCase("module.xml")) {
            return XmlDependencyResolver.INSTANCE.resolveFromFile(descriptorFile, name, version, overrides);
        } else if (descriptorFile.getName().equalsIgnoreCase("module.properties")) {
            return PropertiesDependencyResolver.INSTANCE.resolveFromFile(descriptorFile, name, version, overrides);
        } else {
            return null;
        }
    }
    
    @Override
    public boolean matchesModuleInfo(String moduleName, String version, File moduleArchive, String query, Overrides overrides) {
        ModuleInfo deps = getDependencies(moduleArchive, moduleName, version, overrides);
        if (deps != null) {
            for (ModuleDependencyInfo dep : deps.getDependencies()) {
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
    public int[] getBinaryVersions(String moduleName, String version, File moduleArchive) {
        return null;
    }

    // Return the set of fully qualified names in Ceylon format for all the classes
    // in the JAR pointed to by the given file
    private static Set<String> gatherCeylonNamesFromJar(File jar) throws IOException {
        HashSet<String> ceylonNames = new HashSet<>();
        Set<String> classNames = gatherClassnamesFromJar(jar);
        for (String clsName : classNames) {
            int p = clsName.lastIndexOf('.');
            if (p >= 0) {
                String pkg = clsName.substring(0, p);
                String member = clsName.substring(p + 1).replace('$', '.');
                clsName = pkg + "::" + member;
            }
            ceylonNames.add(clsName);
        }
        return ceylonNames;
    }

    // Return the set of fully qualified names in their original format for all the classes
    // in the JAR pointed to by the given file
    public static Set<String> gatherClassnamesFromJar(File jar) throws IOException {
        HashSet<String> names = new HashSet<>();
        try (JarFile zf = new JarFile(jar)) {
            Enumeration<? extends JarEntry> entries = zf.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!entry.isDirectory() 
                        && entry.getName().endsWith(".class")
                        // skip those because ClassLoader.findClass barfs on them
                        && !entry.getName().endsWith("module-info.class")) {
                    String name = entry.getName();
                    String className = name.substring(0, name.length() - 6).replace('/', '.');
                    names.add(className);
                }
            }
        }
        return names;
    }

}
