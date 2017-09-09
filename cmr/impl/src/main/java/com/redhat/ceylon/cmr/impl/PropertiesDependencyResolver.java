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

import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ModuleDependencyInfo;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.Overrides;
import com.redhat.ceylon.common.Backends;
import com.redhat.ceylon.common.ModuleUtil;

/**
 * Read module info from properties.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public final class PropertiesDependencyResolver extends ModulesDependencyResolver {
    public static final PropertiesDependencyResolver INSTANCE = new PropertiesDependencyResolver();

    private PropertiesDependencyResolver() {
        super(ArtifactContext.MODULE_PROPERTIES);
    }

    @Override
    public ModuleInfo resolveFromInputStream(InputStream stream, String moduleName, String moduleVersion, Overrides overrides) {
        try {
            final Properties properties = new Properties();
            properties.load(stream);
            Set<ModuleDependencyInfo> infos = new LinkedHashSet<>();
            for (Map.Entry<?,?> entry : properties.entrySet()) {
                String depUri = entry.getKey().toString();
                String version = entry.getValue().toString();
                boolean optional = false;
                boolean shared = false;
                if (depUri.startsWith("+")) {
                    depUri = depUri.substring(1);
                    shared = true;
                }
                if (depUri.endsWith("?")) {
                    depUri = depUri.substring(0, depUri.length() - 1);
                    optional = true;
                }
                String namespace = ModuleUtil.getNamespaceFromUri(depUri);
                String modName = ModuleUtil.getModuleNameFromUri(depUri);
                infos.add(new ModuleDependencyInfo(namespace, modName, version, optional, shared, Backends.JAVA));
            }
            ModuleInfo ret = new ModuleInfo(null, 
            		moduleName, moduleVersion,
                    // FIXME: store this
                    ModuleUtil.getMavenGroupIdIfMavenModule(moduleName),
                    ModuleUtil.getMavenArtifactIdIfMavenModule(moduleName),
                    ModuleUtil.getMavenClassifierIfMavenModule(moduleName),
                    null, infos);
            if(overrides != null)
                ret = overrides.applyOverrides(moduleName, moduleVersion, ret);
            return ret;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
