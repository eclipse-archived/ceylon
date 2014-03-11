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
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.DependencyResolver;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.ModuleVersionArtifact;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.common.ModuleUtil;

/**
 * Utility functions to retrieve module meta information from compiled JS modules
 *
 * @author <a href="mailto:tako@ceylon-lang.org">Tako Schotanus</a>
 */
public final class JSUtils implements DependencyResolver, ModuleInfoReader {
    public static JSUtils INSTANCE = new JSUtils();

    private JSUtils() {
    }

    public Set<ModuleInfo> resolve(ArtifactResult result) {
        File mod = result.artifact();
        if (mod != null && mod.getName().toLowerCase().endsWith(ArtifactContext.JS)) {
            return readModuleInformation(result.name(), mod);
        } else {
            return null;
        }
    }
    
    @Override
    public Set<ModuleInfo> resolveFromFile(File file) {
        throw new UnsupportedOperationException("Operation not supported for .js files");
    }

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
    public static Set<ModuleInfo> readModuleInformation(final String moduleName, final File jarFile) {
        return getDependencies(getEngine(jarFile));
    }

    public int[] getBinaryVersions(String moduleName, File moduleArchive) {
        int major = 0;
        int minor = 0;
        
        ModuleVersionDetails mvd = readModuleInfo(moduleName, moduleArchive);
        ModuleVersionArtifact mva = mvd.getArtifactTypes().first();
        if (mva.getMajorBinaryVersion() != null) {
            major = mva.getMajorBinaryVersion();
        }
        if (mva.getMinorBinaryVersion() != null) {
            minor = mva.getMinorBinaryVersion();
        }
        
        return new int[]{major, minor};
    }


    private static Object getEngine(File moduleArchive) {
        ScriptEngine engine = null;
        try {
            engine = new ScriptEngineManager().getEngineByName("JavaScript");
            engine.eval("var exports={}");
            engine.eval("var module={}");
            engine.eval("function require() { return { '$addmod$' : function() {} } }");
            engine.eval(new FileReader(moduleArchive));
            return engine;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse module JS file", ex);
        }
    }

    private static Set<ModuleInfo> getDependencies(Object engine) {
        try {
            return asModInfos(safeEval(engine, "exports.$$CCMM$$()['$mod-deps']"));
        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse module JS file", ex);
        }
    }
    
    public ModuleVersionDetails readModuleInfo(String moduleName, File moduleArchive) {
        ScriptEngine engine = (ScriptEngine)getEngine(moduleArchive);
        Object name = safeEval(engine, "exports.$$CCMM$$()['$mod-name']");
        if (!moduleName.equals(name)) {
            throw new RuntimeException("Incorrect module");
        }
        String version = asString(safeEval(engine, "exports.$$CCMM$$()['$mod-version']"));
        Set<ModuleInfo> deps = getDependencies(engine);
        
        String type = ArtifactContext.getSuffixFromFilename(moduleArchive.getName());

        Integer mayor = null, minor = null;
        String bin = asString(safeEval(engine, "exports.$$CCMM$$()['$mod-bin']"));
        if (bin != null) {
            int p = bin.indexOf('.');
            if (p >= 0) {
                mayor = Integer.parseInt(bin.substring(0, p));
                minor = Integer.parseInt(bin.substring(p + 1));
            } else {
                mayor = Integer.parseInt(bin);
            }
        }
        
        ModuleVersionDetails mvd = new ModuleVersionDetails(version);
        mvd.getArtifactTypes().add(new ModuleVersionArtifact(type, mayor, minor));
        mvd.getDependencies().addAll(deps);
        return mvd;
    }
    
    private static Object safeEval(Object engine, String code) {
        try {
            return ((ScriptEngine)engine).eval(code);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse module JS file", ex);
        }
    }
    
    private static String asString(Object obj) {
        if (obj == null) {
            return null;
        } else {
            return obj.toString();
        }
    }
    
    private static String[] asStringArray(Object obj) {
        if (obj == null) {
            return null;
        }
        if (!(obj instanceof Iterable)) {
            throw new RuntimeException("Expected something Iterable");
        }
        Iterable<Object> array = (Iterable)obj;
        ArrayList<String> result = new ArrayList<String>();
        for (Object o : array) {
            result.add(asString(o));
        }
        return result.toArray(new String[result.size()]);
    }
    
    private static Set<ModuleInfo> asModInfos(Object obj) {
        if (obj == null) {
            return Collections.emptySet();
        }
        if (!(obj instanceof Iterable)) {
            throw new RuntimeException("Expected something Iterable");
        }
        Iterable<Object> array = (Iterable)obj;
        Set<ModuleInfo> result = new HashSet<ModuleInfo>();
        for (Object o : array) {
            String module = asString(o);
            String name = ModuleUtil.moduleName(module);
            if (!"ceylon.language".equals(name)) {
                result.add(new ModuleInfo(name, ModuleUtil.moduleVersion(module), false, false));
            }
        }
        return result;
    }

    public boolean matchesModuleInfo(String moduleName, File moduleArchive, String query) {
        ModuleVersionDetails mvd = readModuleInfo(moduleName, moduleArchive);
        if (mvd.getDoc() != null && matches(mvd.getDoc(), query))
            return true;
        if (mvd.getLicense() != null && matches(mvd.getLicense(), query))
            return true;
        for (String author : mvd.getAuthors()) {
            if (matches(author, query))
                return true;
        }
        for (ModuleInfo dep : mvd.getDependencies()) {
            if (matches(dep.getModuleName(), query))
                return true;
        }
        return false;
    }

    private static boolean matches(String string, String query) {
        return string.toLowerCase().contains(query);
    }

}
