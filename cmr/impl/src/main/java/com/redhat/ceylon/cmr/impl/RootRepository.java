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

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;

import java.io.*;
import java.util.List;

/**
 * Root node -- main entry point into Ceylon repositories.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RootRepository extends AbstractNodeRepository {

    private final FileContentStore fileContentStore;

    private static File getRootDir() {
        String repo = SecurityActions.getProperty("ceylon.repo");
        if (repo == null)
            repo = SecurityActions.getProperty("user.home") + File.separator + ".ceylon" + File.separator + "repo" + File.separator;
        final File root = new File(repo);
        if (root.exists() == false && root.mkdirs() == false)
            throw new IllegalArgumentException("Cannot create Ceylon repo root directory: " + root);
        return root;
    }

    public RootRepository(Logger log) {
        this(getRootDir(), log);
    }

    public RootRepository(File rootDir, Logger log) {
        super(log);
        fileContentStore = new FileContentStore(rootDir);
        final ArtifactContextAdapter aaca = new DefaultArtifactContextAdapter(new RootNode(fileContentStore, fileContentStore));
        setRoot(aaca);
    }

    public ArtifactResult getArtifactResult(ArtifactContext context) throws IOException {
        final Node node = getLeafNode(context);
        if (node != null) {
            if (node.isRemote()) {
                final boolean forceOp = context.isForceOperation();
                try {
                    context.setForceOperation(true); // just force the ops
                    final File file = putContent(context, node, node.getInputStream());
                    // we expect the remote nodes to support Ceylon module info                    
                    return new FileArtifactResult(this, context.getName(), file);
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
        log.debug("Creating local copy of external node: " + node);
        fileContentStore.putContent(node, stream, context);
        File file = fileContentStore.getFile(node); // re-get

        if (context.isIgnoreSHA() == false && node instanceof OpenNode) {
            final OpenNode on = (OpenNode) node;
            final String sha1 = IOUtils.sha1(new FileInputStream(file));
            if (sha1 != null) {
                ByteArrayInputStream shaStream = new ByteArrayInputStream(sha1.getBytes("ASCII"));
                Node sha = node.getChild(SHA1);
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
                OpenNode sl = on.addNode(SHA1 + LOCAL);
                // put sha to local store as well
                fileContentStore.putContent(sl, shaStream, context);
            }
        }

        // refresh markers from root to this newly put node
        final List<String> paths = NodeUtils.toLabelPath(node);
        OpenNode current = getRoot();
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
                Node sl = node.getChild(SHA1 + LOCAL);
                if (sl != null) {
                    fileContentStore.removeFile(sl);
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
        if (result == null && artifact.isRemote() == false && NodeUtils.isAncestor(getRoot(), artifact)) {
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
        return "RootRepository: " + fileContentStore;
    }
}
