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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import com.redhat.ceylon.cmr.api.AbstractDependencyResolver;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.DependencyContext;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.spi.Node;

/**
 * Read module info from module.xml.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class ModulesDependencyResolver extends AbstractDependencyResolver {
    private final String descriptorName;

    protected ModulesDependencyResolver(String descriptorName) {
        this.descriptorName = descriptorName;
    }

    public Set<ModuleInfo> resolve(DependencyContext context) {
        final ArtifactResult result = context.result();

        if (context.ignoreInner() == false) {
            String descriptorPath = String.format("META-INF/jbossmodules/%s/%s/" + descriptorName, result.name().replace('.', '/'), result.version());
            final InputStream descriptor = IOUtils.findDescriptor(result, descriptorPath);
            if (descriptor != null) {
                return resolveFromInputStream(descriptor);
            }
        }

        if (context.ignoreExternal() == false) {
            final File artifact = result.artifact();
            final File mp = new File(artifact.getParent(), descriptorName);
            return resolveFromFile(mp);
        }

        return null;
    }

    public Set<ModuleInfo> resolveFromFile(File mp) {
        if (mp.exists() == false) {
            return null;
        }

        try {
            try (InputStream is = new FileInputStream(mp)) {
                return resolveFromInputStream(is);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Node descriptor(Node artifact) {
        return NodeUtils.firstParent(artifact).getChild(descriptorName);
    }
}
