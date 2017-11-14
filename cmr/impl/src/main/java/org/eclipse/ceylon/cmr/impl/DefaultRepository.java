/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.api.CmrRepository;
import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.cmr.spi.Node;
import org.eclipse.ceylon.cmr.spi.OpenNode;
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.cmr.RepositoryException;

/**
 * Default.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class DefaultRepository extends AbstractRepository {

    public static final String NAMESPACE = "ceylon";
    
    public DefaultRepository(OpenNode root) {
        super(root);
    }

    protected ArtifactResult getArtifactResultInternal(RepositoryManager manager, Node node) {
        return new DefaultArtifactResult(this, manager, node);
    }

    protected static class DefaultArtifactResult extends AbstractCeylonArtifactResult {

        private Node node;

        protected DefaultArtifactResult(CmrRepository repository, RepositoryManager manager, Node node) {
            super(repository, manager, ArtifactContext.fromNode(node).getName(), 
                    ArtifactContext.fromNode(node).getVersion());
            this.node = node;
        }

        protected File artifactInternal() throws RepositoryException {
            try {
                return node.getContent(File.class);
            } catch (IOException e) {
                throw new RepositoryException(e);
            }
        }
        
        @Override
        public String repositoryDisplayString() {
            String repositoryDisplayString = NodeUtils.getRepositoryDisplayString(node);
            File artifact = artifact();
            File originFile = new File(artifact.getParentFile(), artifact.getName() + RootRepositoryManager.ORIGIN);
            if (originFile.exists()) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(originFile));
                    try {
                        String line = reader.readLine();
                        if (line != null && !line.trim().isEmpty()) {
                            repositoryDisplayString = line;
                        }
                    } finally {
                        reader.close();
                    }
                } catch(IOException e) {
                } 
            }
            return repositoryDisplayString;
        }
    }

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }
}
