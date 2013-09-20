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

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.api.RepositoryException;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.StructureBuilder;

/**
 * Caching / tmp repository.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class CachingRepositoryManager extends AbstractNodeRepositoryManager {

    private RepositoryManager caching;
    private final File cachingDir;

    public CachingRepositoryManager(StructureBuilder root, File cachingDir, Logger log) {
        super(log);
        if (root == null)
            throw new IllegalArgumentException("Null structure builder!");

        setAddCacheAsRoot(true);
        setCache(new DefaultRepository(root.createRoot()));
        this.caching = new SimpleRepositoryManager(new FileContentStore(cachingDir), log);
        this.cachingDir = cachingDir;
    }

    public CachingRepositoryManager(Repository root, File cachingDir, Logger log) {
        super(log);
        if (root == null)
            throw new IllegalArgumentException("Null root!");

        setAddCacheAsRoot(true);
        setCache(root);
        this.caching = new SimpleRepositoryManager(new FileContentStore(cachingDir), log);
        this.cachingDir = cachingDir;
    }

    public ArtifactResult getArtifactResult(ArtifactContext context) throws RepositoryException {
        try {
            Node node = getLeafNode(context);
            // node and file must exist to check cache
            if (node != null) {
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
            }
            return null;
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
