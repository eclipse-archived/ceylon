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

import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.api.ModuleDependencyInfo;
import org.eclipse.ceylon.cmr.api.ModuleInfo;
import org.eclipse.ceylon.cmr.api.Overrides;
import org.eclipse.ceylon.common.Backends;
import org.eclipse.ceylon.common.ModuleUtil;

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
