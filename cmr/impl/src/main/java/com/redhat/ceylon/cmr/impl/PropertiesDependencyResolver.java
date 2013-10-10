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
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.DependencyResolver;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.spi.Node;

/**
 * Read module info from properties.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
final class PropertiesDependencyResolver implements DependencyResolver {
    static PropertiesDependencyResolver INSTANCE = new PropertiesDependencyResolver();

    public Set<ModuleInfo> resolve(ArtifactResult result) {
        final File artifact = result.artifact();
        final File mp = new File(artifact.getParent(), ArtifactContext.MODULE_PROPERTIES);
        return resolveFromFile(mp);
    }

    public Node descriptor(Node artifact) {
        return NodeUtils.firstParent(artifact).getChild(ArtifactContext.MODULE_PROPERTIES);
    }

    @Override
    public Set<ModuleInfo> resolveFromFile(File mp) {
        if (mp.exists() == false)
            return null;

        try {
            final Properties properties = new Properties();
            final FileReader reader = new FileReader(mp);
            try {
                properties.load(reader);
            } finally {
                reader.close();
            }
            Set<ModuleInfo> infos = new LinkedHashSet<ModuleInfo>();
            for (Map.Entry entry : properties.entrySet()) {
                infos.add(new ModuleInfo(entry.getKey().toString(), entry.getValue().toString(), false, false));
            }
            return infos;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
