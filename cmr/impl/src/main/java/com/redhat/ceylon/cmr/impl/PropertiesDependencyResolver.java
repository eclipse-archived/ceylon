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
import com.redhat.ceylon.cmr.api.ModuleInfo;

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

    public Set<ModuleInfo> resolveFromInputStream(InputStream stream) {
        try {
            final Properties properties = new Properties();
            properties.load(stream);
            Set<ModuleInfo> infos = new LinkedHashSet<>();
            for (Map.Entry<?,?> entry : properties.entrySet()) {
                String name = entry.getKey().toString();
                String version = entry.getValue().toString();
                boolean optional = false;
                boolean shared = false;
                if (name.startsWith("+")) {
                    name = name.substring(1);
                    shared = true;
                }
                if (name.endsWith("?")) {
                    name = name.substring(0, name.length() - 1);
                    optional = true;
                }
                infos.add(new ModuleInfo(name, version, optional, shared));
            }
            return infos;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
