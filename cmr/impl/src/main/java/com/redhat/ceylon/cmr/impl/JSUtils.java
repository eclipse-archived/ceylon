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
import java.util.List;
import java.util.Set;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import sun.org.mozilla.javascript.NativeArray;

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
public final class JSUtils implements DependencyResolver {
    static JSUtils INSTANCE = new JSUtils();

    private JSUtils() {
    }

    public Set<ModuleInfo> resolve(ArtifactResult result) {
        return readModuleInformation(result.name(), result.artifact());
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
        return null;
    }

    public static int[] getBinaryVersions(String moduleName, File moduleArchive) {
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


    public static void readModuleInfo(String moduleName, File moduleArchive, ModuleInfoCallback callback) {
        ModuleVersionDetails mvd = readModuleInfo(moduleName, moduleArchive);
        callback.storeInfo(mvd);
    }
    
    public static ModuleVersionDetails readModuleInfo(String moduleName, File moduleArchive) {
        ScriptEngine engine = null;
        try {
            engine = new ScriptEngineManager().getEngineByName("JavaScript");
            engine.eval("var exports={}");
            engine.eval("var module={}");
            engine.eval("function require() { return { '$addmod$' : function() {} } }");
            engine.eval(new FileReader(moduleArchive));
            Object name = engine.eval("exports.$$METAMODEL$$()['$mod-name']");
            if (!moduleName.equals(name)) {
                throw new RuntimeException("Incorrect module");
            }
            String version = asString(engine.eval("exports.$$METAMODEL$$()['$mod-version']"));
            List<ModuleInfo> deps = asModInfos(engine.eval("exports.$$METAMODEL$$()['$mod-deps']"));
            String type = ArtifactContext.getSuffixFromFilename(moduleArchive.getName());
            
            ModuleVersionDetails mvd = new ModuleVersionDetails(version);
            mvd.getArtifactTypes().add(new ModuleVersionArtifact(type, null, null));
            mvd.getDependencies().addAll(deps);
            return mvd;
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
        if (!(obj instanceof NativeArray)) {
            throw new RuntimeException("Expected native JavaScript array");
        }
        NativeArray array = (NativeArray)obj;
        String[] result = new String[(int)array.getLength()];
        for (int i = 0; i < array.getLength(); i++) {
            result[i] = asString(array.get(i));
        }
        return result;
    }
    
    private static List<ModuleInfo> asModInfos(Object obj) {
        if (obj == null) {
            return Collections.emptyList();
        }
        if (!(obj instanceof NativeArray)) {
            throw new RuntimeException("Expected native JavaScript array");
        }
        NativeArray array = (NativeArray)obj;
        List<ModuleInfo> result = new ArrayList<ModuleInfo>((int)array.getLength());
        for (int i = 0; i < array.getLength(); i++) {
            String module = asString(array.get(i));
            result.add(new ModuleInfo(ModuleUtil.moduleName(module), ModuleUtil.moduleVersion(module), false, false));
        }
        return result;
    }

    public static boolean matchesModuleInfo(String moduleName, File moduleArchive, String query) {
        return false;
    }
}
