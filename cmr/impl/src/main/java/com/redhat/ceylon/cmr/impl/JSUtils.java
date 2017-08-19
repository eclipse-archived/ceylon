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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.cmr.api.AbstractDependencyResolverAndModuleInfoReader;
import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.DependencyContext;
import com.redhat.ceylon.cmr.api.ModuleDependencyInfo;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.ModuleVersionArtifact;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.Overrides;
import com.redhat.ceylon.cmr.resolver.javascript.JavaScriptResolver;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.common.Backends;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.model.cmr.ArtifactResult;

/**
 * Utility functions to retrieve module meta information from compiled JS modules
 *
 * @author <a href="mailto:tako@ceylon-lang.org">Tako Schotanus</a>
 */
public final class JSUtils extends AbstractDependencyResolverAndModuleInfoReader {

    private JavaScriptResolver resolver;

    /**
     * Warning: used by reflection in Configuration
     */
    public JSUtils() {
        resolver = new JavaScriptResolver();
    }

    @Override
    public ModuleInfo resolve(DependencyContext context, Overrides overrides) {
        if (context.ignoreInner()) {
            return null;
        }

        ArtifactResult result = context.result();
        File mod = result.artifact();
        if (mod != null && (mod.getName().toLowerCase().endsWith(ArtifactContext.JS_MODEL)
                || mod.getName().toLowerCase().endsWith(ArtifactContext.JS))) {
            return readModuleInformation(result.name(), mod, overrides);
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
        return null; // artifact is a descriptor
    }

    /**
     * Read module info from JS file
     *
     * @param moduleName the module name
     * @param jarFile    the module JS file
     * @return module info list
     */
    private ModuleInfo readModuleInformation(final String moduleName, final File jarFile, Overrides overrides) {
        Map<String, Object> model = loadJsonModel(jarFile);
        String version = asString(metaModelProperty(model, "$mod-version"));
        return getModuleInfo(model, moduleName, version, overrides);
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

    private ModuleInfo getModuleInfo(Map<String,Object> model, String module, String version, Overrides overrides) {
        try {
            return getModuleInfo(metaModelProperty(model, "$mod-deps"), module, version, overrides);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse module JS file", ex);
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public ModuleVersionDetails readModuleInfo(String moduleName, String moduleVersion, File moduleArchive, boolean includeMembers, Overrides overrides) {
        Map<String, Object> model = loadJsonModel(moduleArchive);

        String name = asString(metaModelProperty(model, "$mod-name"));
        if (!moduleName.equals(name)) {
            throw new RuntimeException("Incorrect module");
        }
        String version = asString(metaModelProperty(model, "$mod-version"));
        Set<ModuleDependencyInfo> dependencies = getModuleInfo(model, moduleName, version, overrides).getDependencies();
        
        String type = ArtifactContext.getSuffixFromFilename(moduleArchive.getName());

        Integer major = null, minor = null;
        String bin = asString(metaModelProperty(model, "$mod-bin"));
        if (bin != null) {
            int p = bin.indexOf('.');
            if (p >= 0) {
                major = Integer.parseInt(bin.substring(0, p));
                minor = Integer.parseInt(bin.substring(p + 1));
            } else {
                major = Integer.parseInt(bin);
            }
        }
        ModuleVersionDetails mvd = new ModuleVersionDetails(moduleName, version, null, null);
        mvd.getArtifactTypes().add(new ModuleVersionArtifact(type, major, minor));
        mvd.getDependencies().addAll(dependencies);

        Object anns = metaModelProperty(model, "$mod-anns");
        List<Map<String,Object>> annotations = null;
        if (anns instanceof Map) {
            // Pre-10.0 style annotations
            annotations = new ArrayList<Map<String,Object>>(1);
            annotations.add((Map<String,Object>)anns);
        } else if (anns instanceof List) {
            // 10.0+ style annotations
            annotations = (List<Map<String,Object>>)anns;
        }
        if(annotations != null) {
            for (Map<String, Object> annot : annotations) {
                if (annot.containsKey("doc")) {
                    mvd.setDoc(asString(annot.get("doc")));
                }
                if (annot.containsKey("label")) {
                    mvd.setLabel(asString(annot.get("label")));
                }
                if (annot.containsKey("license")) {
                    mvd.setLicense(asString(annot.get("license")));
                }
                if (annot.containsKey("by")) {
                    Iterable<String> by = (Iterable<String>) annot.get("by");
                    if (by != null) {
                        for (String author : by) {
                            mvd.getAuthors().add(author);
                        }
                    }
                }
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

    private ModuleInfo getModuleInfo(Object obj, String moduleName, String version, Overrides overrides) {
        if (obj == null) {
            return new ModuleInfo(null, moduleName, version, null, null, null, null, Collections.<ModuleDependencyInfo>emptySet());
        }
        if (!(obj instanceof Iterable)) {
            throw new RuntimeException("Expected something Iterable");
        }
        @SuppressWarnings("unchecked")
        Iterable<Object> array = (Iterable<Object>)obj;
        Set<ModuleDependencyInfo> deps = new HashSet<ModuleDependencyInfo>();
        for (Object o : array) {
            String module;
            boolean optional = false;
            boolean exported = false;
            if (o instanceof String) {
                module = asString(o);
            } else {
                @SuppressWarnings("unchecked")
                Map<String,Object> m = (Map<String,Object>)o;
                module = m.get("path").toString();
                optional = m.containsKey("opt");
                exported = m.containsKey("exp");
            }
            String depUri = ModuleUtil.moduleName(module);
            String namespace = ModuleUtil.getNamespaceFromUri(depUri);
            String modName = ModuleUtil.getModuleNameFromUri(depUri);
            deps.add(new ModuleDependencyInfo(namespace, modName, ModuleUtil.moduleVersion(module), optional, exported, Backends.JS));
        }
        ModuleInfo result = new ModuleInfo(null, moduleName, version, null, null, null, null, deps);
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

    private Map<String,Object> loadJsonModel(File jsFile) {
        try {
            // If what we have is a plain .js file (not a -model.js file)
            // we first check if a model file exists and if so we use that
            // one instead of the given file
            String name = jsFile.getName().toLowerCase();
            if (!name.endsWith(ArtifactContext.JS_MODEL) && name.endsWith(ArtifactContext.JS)) {
                name = jsFile.getName();
                name = name.substring(0, name.length() - 3) + ArtifactContext.JS_MODEL;
                File modelFile = new File(jsFile.getParentFile(), name);
                if (modelFile.isFile()) {
                    jsFile = modelFile;
                }
            }
            Map<String, Object> model = resolver.readJsonModel(jsFile);
            if (model == null) {
                throw new RuntimeException("Unable to read meta model from file " + jsFile);
            }
            return model;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
