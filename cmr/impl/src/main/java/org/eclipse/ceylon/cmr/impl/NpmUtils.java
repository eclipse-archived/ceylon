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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.ceylon.cmr.api.AbstractDependencyResolverAndModuleInfoReader;
import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.api.DependencyContext;
import org.eclipse.ceylon.cmr.api.ModuleDependencyInfo;
import org.eclipse.ceylon.cmr.api.ModuleInfo;
import org.eclipse.ceylon.cmr.api.ModuleVersionArtifact;
import org.eclipse.ceylon.cmr.api.ModuleVersionDetails;
import org.eclipse.ceylon.cmr.api.Overrides;
import org.eclipse.ceylon.cmr.resolver.javascript.JavaScriptResolver;
import org.eclipse.ceylon.cmr.spi.Node;
import org.eclipse.ceylon.common.Backends;
import org.eclipse.ceylon.model.cmr.ArtifactResult;

/**
 * Utility functions to retrieve module meta information from NPM modules
 *
 * @author <a href="mailto:tako@ceylon-lang.org">Tako Schotanus</a>
 */
public final class NpmUtils extends AbstractDependencyResolverAndModuleInfoReader {

    private JavaScriptResolver resolver;

    /**
     * Warning: used by reflection in Configuration
     */
    public NpmUtils() {
        resolver = new JavaScriptResolver();
    }

    @Override
    public ModuleInfo resolve(DependencyContext context, Overrides overrides) {
        if (context.ignoreInner()) {
            return null;
        }

        ArtifactResult result = context.result();
        File mod = result.artifact();
        File pkgFile = findNpmDescriptor(mod);
        if (pkgFile != null) {
            return readModuleInformation(result.name(), pkgFile, overrides);
        } else {
            return null;
        }
    }
    
    @Override
    public ModuleInfo resolveFromFile(File file, String name, String version, Overrides overrides) {
        throw new UnsupportedOperationException("Operation not supported for .js files");
    }

    @Override
    public ModuleInfo resolveFromInputStream(InputStream stream, String name, String version, Overrides overrides) {
        throw new UnsupportedOperationException("Operation not supported for .js files");
    }

    @Override
    public Node descriptor(Node artifact) {
        return NodeUtils.firstParent(artifact).getChild("package.json");
    }

    @Override
    public int[] getBinaryVersions(String moduleName, String version, File moduleArchive) {
        int major = 0;
        int minor = 0;
        ModuleVersionDetails mvd = readModuleInfo(moduleName, version, moduleArchive, false, null);
        ModuleVersionArtifact mva = mvd.getArtifactTypes().first();
        if (mva.getMajorBinaryVersion() != null) {
            major = mva.getMajorBinaryVersion();
        }
        if (mva.getMinorBinaryVersion() != null) {
            minor = mva.getMinorBinaryVersion();
        }
        
        return new int[]{major, minor};
    }

    private ModuleInfo readModuleInformation(final String moduleName, final File jarFile, Overrides overrides) {
        Map<String, Object> model = loadJsonModel(jarFile);
        String version = asString(metaModelProperty(model, "version"));
        return getModuleInfo(model, moduleName, version, overrides);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public ModuleVersionDetails readModuleInfo(String moduleName, String moduleVersion, File moduleArchive, boolean includeMembers, Overrides overrides) {
        Map<String, Object> model = loadJsonModel(moduleArchive);

        String name = asString(metaModelProperty(model, "name"));
        if (!moduleName.equals(name)) {
            throw new RuntimeException("Incorrect module");
        }
        String version = asString(metaModelProperty(model, "version"));
        Set<ModuleDependencyInfo> dependencies = getModuleInfo(model, moduleName, version, overrides).getDependencies();
        
        String type = ArtifactContext.getSuffixFromFilename(moduleArchive.getName());

        ModuleVersionDetails mvd = new ModuleVersionDetails(NpmRepository.NAMESPACE, moduleName, version, null, null);
        mvd.getArtifactTypes().add(new ModuleVersionArtifact(type, null, null));
        mvd.getDependencies().addAll(dependencies);

        mvd.setDoc(asString(model.get("description")));
        mvd.setLicense(asString(model.get("license")));
        String author = asString(model.get("author.name"));
        if (author != null) {
            mvd.getAuthors().add(author);
        }
        Iterable<Map<String, Object>> contributors = (Iterable<Map<String, Object>>) model.get("contributors");
        if (contributors != null) {
            for (Map<String, Object> contrib : contributors) {
                mvd.getAuthors().add(asString(contrib.get("name")));
            }
        }
        
        if (includeMembers) {
            mvd.setMembers(getMembers(moduleName, moduleArchive));
        }
        
        return mvd;
    }

    private Set<String> getMembers(String moduleName, File moduleArchive) {
        // TODO Implement this!
        throw new RuntimeException("Not implemented yet");
    }

    private Object metaModelProperty(Map<String,Object> model, String propName) {
        String[] names = propName.split("\\.");
        for (int i=0; i<names.length-1; i++) {
            model = (Map<String, Object>) model.get(names[i]);
            if (model == null) {
                return null;
            }
        }
        return model.get(propName);
    }
    
    private String asString(Object obj) {
        if (obj == null) {
            return null;
        } else if(obj instanceof Iterable){
            Iterator<String> iter = ((Iterable<String>) obj).iterator();
            return iter.hasNext() ? iter.next() : null;
        } else {
            return obj.toString();
        }
    }

    private ModuleInfo getModuleInfo(Map<String,Object> model, String module, String version, Overrides overrides) {
        try {
            return getModuleInfo(metaModelProperty(model, "dependencies"), module, version, overrides);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse NPM package.json file", ex);
        }
    }
    
    private static final Set<ModuleDependencyInfo> NO_DEPS = Collections.<ModuleDependencyInfo>emptySet();
    
    private ModuleInfo getModuleInfo(Object obj, String moduleName, String version, Overrides overrides) {
        if (obj == null) {
            return new ModuleInfo(NpmRepository.NAMESPACE, moduleName, version, null, null, null, null, NO_DEPS);
        }
        if (!(obj instanceof Map)) {
            throw new RuntimeException("Expected an Object");
        }
        @SuppressWarnings("unchecked")
        Map<String,Object> map = (Map<String,Object>)obj;
        Set<ModuleDependencyInfo> deps = new HashSet<ModuleDependencyInfo>();
        for (String depName : map.keySet()) {
            String depVersion = asString(map.get(depName));
            deps.add(new ModuleDependencyInfo(NpmRepository.NAMESPACE, depName, depVersion, false, false, Backends.JS));
        }
        ModuleInfo result = new ModuleInfo(NpmRepository.NAMESPACE, moduleName, version, null, null, null, null, deps);
        if(overrides != null)
            result = overrides.applyOverrides(moduleName, version, result);
        return result;
    }

    @Override
    public boolean matchesModuleInfo(String moduleName, String moduleVersion, File moduleArchive, String query, Overrides overrides) {
        ModuleVersionDetails mvd = readModuleInfo(moduleName, moduleVersion, moduleArchive, false, overrides);
        if (mvd.getDoc() != null && matches(mvd.getDoc(), query))
            return true;
        if (mvd.getLicense() != null && matches(mvd.getLicense(), query))
            return true;
        if (mvd.getLabel() != null && matches(mvd.getLabel(), query))
            return true;
        for (String author : mvd.getAuthors()) {
            if (matches(author, query))
                return true;
        }
        for (ModuleDependencyInfo dep : mvd.getDependencies()) {
            if (matches(dep.getModuleName(), query))
                return true;
        }
        return false;
    }

    private boolean matches(String string, String query) {
        return string.toLowerCase().contains(query);
    }

    private File findNpmDescriptor(File jsFile) {
        File parent = jsFile.getParentFile();
        File pkgFile = new File(parent, "package.json");
        while (parent != null && !pkgFile.exists()) {
            parent = parent.getParentFile();
            pkgFile = new File(parent, "package.json");
        }
        if (parent != null) {
            return pkgFile;
        } else {
            return null;
        }
    }
    
    private Map<String,Object> loadJsonModel(File jsFile) {
        File pkgFile = findNpmDescriptor(jsFile);
        if (pkgFile != null) {
            try {
                return resolver.readNpmDescriptor(pkgFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

}
