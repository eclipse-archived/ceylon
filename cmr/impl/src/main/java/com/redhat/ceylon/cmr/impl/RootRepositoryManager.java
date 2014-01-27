/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * 
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.redhat.ceylon.cmr.api.ArtifactCallback;
import com.redhat.ceylon.cmr.api.ArtifactCallbackStream;
import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.api.RepositoryException;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;
import com.redhat.ceylon.common.config.Repositories;

/**
 * Root node -- main entry point into Ceylon repositories.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RootRepositoryManager extends AbstractNodeRepositoryManager {
    private final FileContentStore fileContentStore;

    private static File getRootDir() {
        com.redhat.ceylon.common.config.Repositories.Repository rootRepo = Repositories.get().getCacheRepository();
        return new File(rootRepo.getUrl());
    }

    public RootRepositoryManager(Logger log) {
        this(getRootDir(), log);
    }

    public RootRepositoryManager(File rootDir, Logger log) {
        super(log);
        if (!rootDir.exists() && !rootDir.mkdirs()) {
            throw new IllegalArgumentException("Cannot create Ceylon cache repository directory: " + rootDir);
        }
        fileContentStore = new FileContentStore(rootDir);
        final Repository aaca = new DefaultRepository(new RootNode(fileContentStore, fileContentStore));
        setCache(aaca);
    }

    public ArtifactResult getArtifactResult(ArtifactContext context) throws RepositoryException {
        final Node node = getLeafNode(context);
        if (node != null) {
            if (node.isRemote()) {
                final boolean forceOp = context.isForceOperation();
                try {
                    context.setForceOperation(true); // just force the ops
                    log.debug("Looking up artifact " + context + " from " + node + " to cache it");
                    InputStream inputStream = node.getInputStream();
                    // temp fix for https://github.com/ceylon/ceylon-module-resolver/issues/60
                    // in theory we should not have nodes with null streams, but at least provide a helpful exception
                    if (inputStream == null) {
                        throw new RepositoryException("Node " + node + " for repository " + this + " returned a null stream");
                    }
                    try {
                        context.setSuffixes(ArtifactContext.getSuffixFromNode(node)); // Make sure we'll have only one suffix
                        log.debug(" -> Found it, now caching it");
                        final File file = putContent(context, node, inputStream);
                        log.debug("    Caching done: " + file);
                        // we expect the remote nodes to support Ceylon module info
                        return new FileArtifactResult(this, context.getName(), context.getVersion(), file);
                    } finally {
                        IOUtils.safeClose(inputStream);
                    }
                } catch (IOException e) {
                    throw new RepositoryException(e);
                } finally {
                    context.setForceOperation(forceOp);
                }
            } else {
                return toArtifactResult(node);
            }
        } else {
            return null;
        }
    }

    protected File putContent(ArtifactContext context, Node node, InputStream stream) throws IOException {
        log.debug("Creating local copy of external node: " + node + " at repo: " + fileContentStore.getDisplayString());

        ArtifactCallback callback = context.getCallback();
        if (callback == null) {
            callback = ArtifactCallbackStream.getCallback();
        }
        final File file;
        try {
            if (callback != null) {
                callback.size(node.getSize());
                stream = new ArtifactCallbackStream(callback, stream);
            }
            fileContentStore.putContent(node, stream, context); // stream should be closed closer to API call
            file = fileContentStore.getFile(node); // re-get
            if (callback != null) {
                callback.done(file);
            }
        } catch (Throwable t) {
            if (callback != null) {
                callback.error(t);
            }
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw IOUtils.toIOException(t);
            }
        }

        if (context.isIgnoreSHA() == false && node instanceof OpenNode) {
            final OpenNode on = (OpenNode) node;
            final String sha1 = IOUtils.sha1(new FileInputStream(file));
            if (sha1 != null) {
                ByteArrayInputStream shaStream = new ByteArrayInputStream(sha1.getBytes("ASCII"));
                Node parent = NodeUtils.firstParent(node);
                if (parent == null) {
                    throw new IllegalArgumentException("Parent should not be null: " + node);
                }
                Node sha = parent.getChild(on.getLabel() + SHA1);
                if (sha == null) {
                    // put it to ext node as well, if supported
                    on.addContent(SHA1, shaStream, context);
                    shaStream.reset(); // reset, for next read
                } else if (sha.hasBinaries()) {
                    final String existingSha1 = IOUtils.readSha1(sha.getInputStream());
                    if (sha1.equals(existingSha1) == false) {
                        try {
                            fileContentStore.delete(file, node);
                        } catch (Exception e) {
                            log.warning("Error removing new content: " + file);
                        }
                        throw new IOException("Bad SHA1 - file: " + sha1 + " != " + existingSha1);
                    }
                }
                // create empty marker node
                OpenNode sl = ((OpenNode) parent).addNode(on.getLabel() + SHA1 + LOCAL);
                // put sha to local store as well
                fileContentStore.putContent(sl, shaStream, context);
            }
        }

        // only check for jars or forced checks
        if (ArtifactContext.JAR.equals(context.getSingleSuffix()) || context.forceDescriptorCheck()) {
            // transfer descriptor as well, if there is one
            final Node descriptor = Configuration.getResolvers().descriptor(node);
            if (descriptor != null && descriptor.hasBinaries()) {
                try (InputStream is = descriptor.getInputStream()) {
                    fileContentStore.putContent(descriptor, is, context);
                }
            }
        }

        // refresh markers from root to this newly put node
        final List<String> paths = NodeUtils.toLabelPath(node);
        OpenNode current = getCache();
        for (String path : paths) {
            if (current == null)
                break;

            current.refresh(false);
            final Node tmp = current.peekChild(path);
            current = (tmp instanceof OpenNode) ? OpenNode.class.cast(tmp) : null;
        }

        return file;
    }

    @Override
    protected void addContent(ArtifactContext context, Node parent, String label, InputStream content) throws IOException {
        Node child;
        if (parent instanceof OpenNode) {
            OpenNode on = (OpenNode) parent;
            child = on.peekChild(label);
            if (child == null) {
                child = on.addNode(label);
            }
        } else {
            child = parent.getChild(label);
        }
        if (child != null) {
            putContent(context, child, content);
        } else {
            throw new IOException("Cannot add child [" + label + "] content [" + content + "] on parent node: " + parent);
        }
    }

    @Override
    protected void removeNode(Node parent, String child) throws IOException {
        final Node node = parent.getChild(child);

        try {
            if (node != null) {
                final Node sl = parent.getChild(child + SHA1 + LOCAL);
                if (sl != null) {
                    fileContentStore.removeFile(sl);
                }
                final Node descriptor = Configuration.getResolvers().descriptor(node);
                if (descriptor != null) {
                    fileContentStore.removeFile(descriptor);
                }
            }
        } catch (Exception ignored) {
        }

        try {
            super.removeNode(parent, child);
        } finally {
            if (node != null) {
                fileContentStore.removeFile(node);
            }
        }
    }

    @Override
    protected Boolean checkSHA(Node artifact) throws IOException {
        Boolean result = super.checkSHA(artifact);
        if (result == null && artifact.isRemote() == false && NodeUtils.isAncestor(getCache(), artifact)) {
            Node sha = artifact.getChild(SHA1 + LOCAL);
            if (sha != null) {
                File shaFile = fileContentStore.getFile(sha);
                if (shaFile.exists())
                    return checkSHA(artifact, IOUtils.toInputStream(shaFile));
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "RootRepositoryManager: " + fileContentStore;
    }
}
