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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.ceylon.cmr.api.AbstractDependencyResolver;
import org.eclipse.ceylon.cmr.api.DependencyContext;
import org.eclipse.ceylon.cmr.api.ModuleInfo;
import org.eclipse.ceylon.cmr.api.Overrides;
import org.eclipse.ceylon.cmr.spi.Node;
import org.eclipse.ceylon.model.cmr.ArtifactResult;

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
        File mod = result.artifact();
        if (mod != null && IOUtils.isZipFile(mod)) {
            if (context.ignoreInner() == false) {
                String descriptorPath = getQualifiedMetaInfDescriptorName(result.name(), result.version());
                final InputStream descriptor = IOUtils.findDescriptor(result, descriptorPath);
                if (descriptor != null) {
                    try {
                        return augment(result, resolveFromInputStream(descriptor, result.name(), result.version(), overrides));
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
                return augment(result, resolveFromFile(mp, result.name(), result.version(), overrides));
            }
        }
        return null;
    }

    private ModuleInfo augment(ArtifactResult result, ModuleInfo ret) {
        if(ret == null)
            return null;
        if(ret.getGroupId() != null)
            return ret;
        // see if we have a Maven descriptor in there
        if(result.artifact() != null){
            try(ZipFile zf = new ZipFile(result.artifact())){
                Enumeration<? extends ZipEntry> entries = zf.entries();
                while(entries.hasMoreElements()){
                    ZipEntry entry = entries.nextElement();
                    String path = entry.getName();
                    if(path.startsWith("META-INF/maven/")
                            && path.endsWith("/pom.xml")){
                        String part = path.substring(15, path.length()-8);
                        int sep = part.indexOf('/');
                        if(sep != -1){
                            String groupId = part.substring(0, sep);
                            String artifactId = part.substring(sep+1);
                            return new ModuleInfo(ret.getNamespace(), 
                                    ret.getName(), ret.getVersion(), 
                                    groupId, artifactId, null,
                                    ret.getFilter(), 
                                    ret.getDependencies());
                        }
                    }
                }
            } catch (IOException e) {
                // not a zip file
            }
        }
        return ret;
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
