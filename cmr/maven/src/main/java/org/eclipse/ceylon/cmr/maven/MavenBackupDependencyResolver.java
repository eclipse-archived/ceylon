/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.maven;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.ceylon.cmr.api.AbstractDependencyResolver;
import org.eclipse.ceylon.cmr.api.DependencyContext;
import org.eclipse.ceylon.cmr.api.ModuleInfo;
import org.eclipse.ceylon.cmr.api.Overrides;
import org.eclipse.ceylon.cmr.impl.IOUtils;
import org.eclipse.ceylon.cmr.impl.NodeUtils;
import org.eclipse.ceylon.cmr.spi.Node;
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.cmr.RepositoryException;

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
