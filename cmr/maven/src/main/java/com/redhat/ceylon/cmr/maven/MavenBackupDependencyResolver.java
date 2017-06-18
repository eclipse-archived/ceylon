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

import com.redhat.ceylon.cmr.api.AbstractDependencyResolver;
import com.redhat.ceylon.cmr.api.DependencyContext;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.Overrides;
import com.redhat.ceylon.cmr.impl.IOUtils;
import com.redhat.ceylon.cmr.impl.NodeUtils;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.RepositoryException;

/**
 * Poor man's Maven internal pom reader when aether is not available.
 * 
 * @author Stef Epardaud
 */
public class MavenBackupDependencyResolver extends AbstractDependencyResolver {
    
    public final static MavenBackupDependencyResolver INSTANCE = new MavenBackupDependencyResolver();
    
    private MavenBackupDependencyResolver(){}
    
    
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

		try {
            ModuleInfo ret = MavenUtils.getDependencies(file, name, version);
            if(overrides != null && ret != null)
                ret = overrides.applyOverrides(name != null ? name : ret.getName(), 
                        version != null ? version : ret.getVersion(), ret);
            return ret;
		} catch (IOException e) {
			throw new RepositoryException("Failed to resolve pom", e);
		}
    }

    public ModuleInfo resolveFromInputStream(InputStream stream, String name, String version, Overrides overrides) {
        if (stream == null) {
            return null;
        }

        try{
            ModuleInfo ret = MavenUtils.getDependencies(stream, name, version);
            if(overrides != null && ret != null)
                ret = overrides.applyOverrides(name != null ? name : ret.getName(), 
                        version != null ? version : ret.getVersion(), ret);
            return ret;
        } catch (IOException e) {
        	throw new RepositoryException("Failed to resolve pom", e);
        }
    }

    public Node descriptor(Node artifact) {
        return NodeUtils.firstParent(artifact).getChild("pom.xml");
    }
}
