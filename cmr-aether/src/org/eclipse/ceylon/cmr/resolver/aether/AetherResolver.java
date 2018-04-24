/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.resolver.aether;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface AetherResolver {
    
    public DependencyDescriptor getDependencies(String groupId, String artifactId, String version, boolean fetchSingleArtifact) 
            throws AetherException;
    
    public DependencyDescriptor getDependencies(String groupId, String artifactId, String version, 
            String classifier, String extension, boolean fetchSingleArtifact) 
                    throws AetherException;
    
    public List<String> resolveVersionRange(String groupId, String artifactId, String versionRange) throws AetherException;

    public DependencyDescriptor getDependencies(File pomXml, String name, String version) throws IOException;

    public DependencyDescriptor getDependencies(InputStream pomXml, String name, String version) throws IOException;
    
    public File getLocalRepositoryBaseDir();
}
