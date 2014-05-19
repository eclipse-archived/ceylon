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
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import com.redhat.ceylon.cmr.api.AbstractDependencyResolver;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.DependencyContext;
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.impl.IOUtils;
import com.redhat.ceylon.cmr.impl.JULLogger;
import com.redhat.ceylon.cmr.impl.NodeUtils;
import com.redhat.ceylon.cmr.spi.Node;
import org.jboss.shrinkwrap.resolver.api.maven.MavenArtifactInfo;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenCoordinate;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class MavenDependencyResolver extends AbstractDependencyResolver {
    private static final Logger logger = new JULLogger();

    public Set<ModuleInfo> resolve(DependencyContext context) {
        if (context.ignoreInner() == false) {
            ArtifactResult result = context.result();
            String name = result.name();
            int p = name.indexOf(':');
            if (p < 0) {
                p = name.lastIndexOf('.');
            }
            String groupId = name.substring(0, p);
            String artifactId = name.substring(p + 1);
            String descriptorPath = String.format("META-INF/maven/%s/%s/pom.xml", groupId, artifactId);
            InputStream inputStream = IOUtils.findDescriptor(result, descriptorPath);
            if (inputStream != null) {
                try {
                    return resolveFromInputStream(inputStream);
                } finally {
                    IOUtils.safeClose(inputStream);
                }
            }
        }
        return null;
    }

    public Set<ModuleInfo> resolveFromFile(File file) {
        if (file.exists() == false) {
            return null;
        }

        AetherUtils utils = new AetherUtils(logger, false);
        MavenArtifactInfo[] dependencies = utils.getDependencies(file);
        return toModuleInfo(dependencies);
    }

    public Set<ModuleInfo> resolveFromInputStream(InputStream stream) {
        if (stream == null) {
            return null;
        }

        AetherUtils utils = new AetherUtils(logger, false);
        MavenArtifactInfo[] dependencies = utils.getDependencies(stream);
        return toModuleInfo(dependencies);
    }

    public Node descriptor(Node artifact) {
        return NodeUtils.firstParent(artifact).getChild("pom.xml");
    }

    protected static Set<ModuleInfo> toModuleInfo(MavenArtifactInfo[] dependencies) {
        Set<ModuleInfo> infos = new HashSet<>();
        for (MavenArtifactInfo dep : dependencies) {
            MavenCoordinate co = dep.getCoordinate();
            infos.add(new ModuleInfo(AetherUtils.toCanonicalForm(co.getGroupId(), co.getArtifactId()), co.getVersion(), AetherUtils.isOptional(dep), false));
        }
        return infos;
    }
}
