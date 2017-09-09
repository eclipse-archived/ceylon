/*
 * Copyright 2014 Red Hat inc. and third party contributors as noted
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

package com.redhat.ceylon.cmr.maven;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import com.redhat.ceylon.cmr.api.AbstractDependencyResolver;
import com.redhat.ceylon.cmr.api.DependencyContext;
import com.redhat.ceylon.cmr.api.ModuleDependencyInfo;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.Overrides;
import com.redhat.ceylon.cmr.impl.CMRJULLogger;
import com.redhat.ceylon.cmr.impl.IOUtils;
import com.redhat.ceylon.cmr.impl.MavenRepository;
import com.redhat.ceylon.cmr.impl.NodeUtils;
import com.redhat.ceylon.cmr.resolver.aether.DependencyDescriptor;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.common.Backends;
import com.redhat.ceylon.common.log.Logger;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.RepositoryException;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class MavenDependencyResolver extends AbstractDependencyResolver {
    private static final Logger logger = new CMRJULLogger();
    
    // ensures that instantiating this resolver without the cmr-maven module will fail
    private AetherUtils utils = new AetherUtils(logger, null, null, false, (int)com.redhat.ceylon.common.Constants.DEFAULT_TIMEOUT, null);

    @Override
    public ModuleInfo resolve(DependencyContext context, Overrides overrides) {
        if (context.ignoreInner() == false) {
            ArtifactResult result = context.result();
            File mod = result.artifact();
            if (mod != null && IOUtils.isZipFile(mod)) {
                String name = result.name();
                int p = name.indexOf(':');
                if (p < 0) {
                    p = name.lastIndexOf('.');
                }
                if (p < 0) {
                    // not a Maven artifact
                    return null;
                }
                String groupId = name.substring(0, p);
                String artifactId = name.substring(p + 1);
                String descriptorPath = String.format("META-INF/maven/%s/%s/pom.xml", groupId, artifactId);
                InputStream inputStream = IOUtils.findDescriptor(result, descriptorPath);
                if (inputStream != null) {
                    try {
                        return resolveFromInputStream(inputStream, name, result.version(), overrides);
                    } finally {
                        IOUtils.safeClose(inputStream);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public ModuleInfo resolveFromFile(File file, String name, String version, Overrides overrides) {
        if (file.exists() == false) {
            return null;
        }

        DependencyDescriptor descriptor;
		try {
			descriptor = utils.getDependencies(file, name, version);
		} catch (IOException e) {
			throw new RepositoryException("Failed to resolve pom", e);
		}
        return toModuleInfo(descriptor, name, version, overrides);
    }

    public ModuleInfo resolveFromInputStream(InputStream stream, String name, String version, Overrides overrides) {
        if (stream == null) {
            return null;
        }

        DependencyDescriptor descriptor;
        try{
        	descriptor = utils.getDependencies(stream, name, version);
        } catch (IOException e) {
        	throw new RepositoryException("Failed to resolve pom", e);
        }
        return toModuleInfo(descriptor, name, version, overrides);
    }

    public Node descriptor(Node artifact) {
        return NodeUtils.firstParent(artifact).getChild("pom.xml");
    }

    private static ModuleInfo toModuleInfo(DependencyDescriptor descriptor, String name, String version, Overrides overrides) {
        Set<ModuleDependencyInfo> infos = new HashSet<>();
        for (DependencyDescriptor dep : descriptor.getDependencies()) {
            String depName = MavenUtils.moduleName(dep.getGroupId(), dep.getArtifactId(), null);
            infos.add(new ModuleDependencyInfo(MavenRepository.NAMESPACE, depName, dep.getVersion(), dep.isOptional(), false, Backends.JAVA, AetherUtils.toModuleScope(dep)));
        }
        String descrName = MavenUtils.moduleName(descriptor.getGroupId(), descriptor.getArtifactId(), null);
        // if it's not the descriptor we wanted, let's not return it
        if(name != null && !name.equals(descrName))
            return null;
        if(version != null && !version.equals(descriptor.getVersion()))
            return null;
        ModuleInfo ret = new ModuleInfo(MavenRepository.NAMESPACE, descrName, descriptor.getVersion(), 
                descriptor.getGroupId(), descriptor.getArtifactId(), null, null, infos);
        if(overrides != null)
            ret = overrides.applyOverrides(descrName, descriptor.getVersion(), ret);
        return ret;
    }
}
