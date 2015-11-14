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

import com.redhat.ceylon.cmr.api.AbstractDependencyResolver;
import com.redhat.ceylon.cmr.api.DependencyContext;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.Overrides;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.model.cmr.ArtifactResult;

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

    @Override
    public ModuleInfo resolve(DependencyContext context, Overrides overrides) {
        final ArtifactResult result = context.result();

        if (context.ignoreInner() == false) {
            String descriptorPath = getQualifiedMetaInfDescriptorName(result.name(), result.version());
            final InputStream descriptor = IOUtils.findDescriptor(result, descriptorPath);
            if (descriptor != null) {
                try {
                    return resolveFromInputStream(descriptor, result.name(), result.version(), overrides);
                } finally {
                    IOUtils.safeClose(descriptor);
                }
            }
        }

        if (context.ignoreExternal() == false) {
            final File artifact = result.artifact();
            File mp = new File(artifact.getParent(), descriptorName);
            if(!mp.exists()){
                // if we don't have module.xml, look for module.name-version-module.xml
                // FIXME: go through the repository so we can find it in other repos?
                String qualifiedDescriptorName = getQualifiedToplevelDescriptorName(result.name(), result.version());
                mp = new File(artifact.getParent(), qualifiedDescriptorName);
            }
            return resolveFromFile(mp, result.name(), result.version(), overrides);
        }

        return null;
    }

    public String getQualifiedMetaInfDescriptorName(String module, String version) {
        return String.format("META-INF/jbossmodules/%s/%s/" + descriptorName, module.replace('.', '/'), version);
    }

    public String getQualifiedToplevelDescriptorName(String module, String version){
        return String.format("%s-%s-" + descriptorName, module, version);
    }
    
    @Override
    public ModuleInfo resolveFromFile(File mp, String name, String version, Overrides overrides) {
        if (mp.exists() == false) {
            return null;
        }

        try {
            try (InputStream is = new FileInputStream(mp)) {
                return resolveFromInputStream(is, name, version, overrides);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Node descriptor(Node artifact) {
        return NodeUtils.firstParent(artifact).getChild(descriptorName);
    }
}
