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

import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.api.CmrRepository;
import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.cmr.spi.Node;
import org.eclipse.ceylon.cmr.spi.StructureBuilder;
import org.eclipse.ceylon.common.log.Logger;
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.cmr.RepositoryException;

/**
 * Caching / tmp repository.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class CachingRepositoryManager extends AbstractNodeRepositoryManager {

    private RepositoryManager caching;
    private final File cachingDir;

    public CachingRepositoryManager(StructureBuilder root, File cachingDir, Logger log) {
        super(log, null);
        if (root == null)
            throw new IllegalArgumentException("Null structure builder!");

        setAddCacheAsRoot(true);
        setCache(new DefaultRepository(root.createRoot()));
        this.caching = new SimpleRepositoryManager(new FileContentStore(cachingDir), log);
        this.cachingDir = cachingDir;
    }

    public CachingRepositoryManager(CmrRepository root, File cachingDir, Logger log) {
        super(log, null);
        if (root == null)
            throw new IllegalArgumentException("Null root!");

        setAddCacheAsRoot(true);
        setCache(root);
        this.caching = new SimpleRepositoryManager(new FileContentStore(cachingDir), log);
        this.cachingDir = cachingDir;
    }

    protected ArtifactResult getArtifactResult(ArtifactContext context, Node node) throws RepositoryException {
        try {
            ArtifactResult result = caching.getArtifactResult(context);
            if (result != null) {
                boolean valid = false;
                File file = result.artifact();
                if (file.exists()) {
                    long lm = node.getLastModified();
                    valid = (lm == -1 || lm < file.lastModified());
                }
                if (valid) {
                    return result;
                }
            }

            final boolean previous = context.isForceOperation();
            context.setForceOperation(true);
            try {
                context.setSuffixes(ArtifactContext.getSuffixFromNode(node)); // Make sure we'll have only one suffix
                caching.putArtifact(context, node.getInputStream());
            } finally {
                context.setForceOperation(previous);
            }
            return caching.getArtifactResult(context);
        } catch (IOException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public void putArtifact(ArtifactContext context, InputStream content) throws RepositoryException {
        caching.putArtifact(context, content); // first copy to local
        final File file = caching.getArtifact(context); // should be here
        try {
            super.putArtifact(context, new FileInputStream(file)); // upload
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public String toString() {
        return "CachingRepositoryManager: " + getCache();
    }

    public File getCacheFolder() {
        return cachingDir;
    }
}
