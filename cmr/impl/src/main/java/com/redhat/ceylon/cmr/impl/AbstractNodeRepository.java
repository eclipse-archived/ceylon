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

import com.redhat.ceylon.cmr.api.AbstractRepository;
import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.spi.ContentOptions;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractNodeRepository extends AbstractRepository {

    protected static final String SHA1 = ".sha1";
    protected static final String LOCAL = ".local";
    protected static final String CACHED = ".cached";

    protected ArtifactContextAdapter root; // main local root
    protected List<ArtifactContextAdapter> roots = new CopyOnWriteArrayList<ArtifactContextAdapter>(); // lookup roots - order matters!

    public AbstractNodeRepository(Logger log) {
        super(log);
    }

    protected OpenNode getRoot() {
        if (root == null)
            throw new IllegalArgumentException("Missing root!");

        return root.getRoot();
    }

    protected void setRoot(ArtifactContextAdapter root) {
        if (root == null)
            throw new IllegalArgumentException("Null root");
        if (this.root != null)
            throw new IllegalArgumentException("Root already set!");

        this.root = root;
        this.roots.add(root);
    }

    protected void prependExternalRoot(ArtifactContextAdapter external) {
        roots.add(0, external);
    }

    protected void appendExternalRoot(ArtifactContextAdapter external) {
        roots.add(external);
    }

    protected void removeExternalRoot(ArtifactContextAdapter external) {
        roots.remove(external);
    }

    public void putArtifact(ArtifactContext context, InputStream content) throws IOException {
        final Node parent = getOrCreateParent(context);
        final String label = root.getArtifactName(context);
        if (parent instanceof OpenNode) {
            final OpenNode on = (OpenNode) parent;
            if (on.addContent(label, content, context) == null)
                addContent(context, parent, label, content);
        } else {
            addContent(context, parent, label, content);
        }
    }

    @Override
    protected void putFolder(ArtifactContext context, File folder) throws IOException {
        Node parent = getOrCreateParent(context);
        final String label = root.getArtifactName(context);
        if (parent instanceof OpenNode) {
            final OpenNode on = (OpenNode) parent;
            final OpenNode curent = on.createNode(label);
            try {
                for (File f : folder.listFiles()) // ignore folder, it should match new root
                    putFiles(curent, f, context);
            } catch (IOException e) {
                removeArtifact(context);
                throw e;
            }
        } else {
            throw new IOException("Cannot put folder [" + folder + "] to non-open node: " + context);
        }
    }

    protected void putFiles(OpenNode current, File file, ContentOptions options) throws IOException {
        if (current == null)
            throw new IOException("Null current, could probably not create new node for file: " + file.getParent());

        if (file.isDirectory()) {
            current = current.createNode(file.getName());
            for (File f : file.listFiles())
                putFiles(current, f, options);
        } else {
            current.addContent(file.getName(), new FileInputStream(file), options);
        }
    }

    protected void addContent(ArtifactContext context, Node parent, String label, InputStream content) throws IOException {
        throw new IOException("Cannot add child [" + label + "] content [" + content + "] on parent node: " + parent);
    }

    public void removeArtifact(ArtifactContext context) throws IOException {
        Node parent = getFromRootNode(context, false);
        if (parent != null) {
            final String label = root.getArtifactName(context);
            removeNode(parent, label);
        } else {
            log.debug("No such artifact: " + context);
        }
    }

    protected void removeNode(Node parent, String child) throws IOException {
        if (parent instanceof OpenNode) {
            final OpenNode on = (OpenNode) parent;
            on.removeNode(child);
        } else {
            throw new IOException("Parent node is not open: " + parent);
        }
    }

    protected Node getLeafNode(ArtifactContext context) {
        final Node node = getFromAllRoots(context, true);
        if (node == null) {
            if (context.isThrowErrorIfMissing())
                throw new IllegalArgumentException("No such artifact: " + context);
            return null;
        }

        // by default we don't check sha1 on remote nodes, it should be done after download
        if (context.isIgnoreSHA() == false && node.isRemote() == false && node.hasBinaries()) {
            Boolean result = null;
            Node shaResult = (node instanceof OpenNode) ? (OpenNode.class.cast(node).peekChild(SHA1 + CACHED)) : node.getChild(SHA1 + CACHED);
            if (shaResult == null) {
                try {
                    result = checkSHA(node);
                    if (node instanceof OpenNode) {
                        final OpenNode on = (OpenNode) node;
                        on.addNode(SHA1 + CACHED, result);
                    }
                } catch (IOException e) {
                    log.warning("Error checking SHA1: " + e);
                }

            } else {
                result = shaResult.getValue(Boolean.class);
            }
            // check sha
            if (result != null && result == false)
                throw new IllegalArgumentException("Invalid SHA1 for artifact: " + context);
        }

        // save the context info
        context.toNode(node);

        return node;
    }

    protected Boolean checkSHA(Node artifact) throws IOException {
        final Node sha = artifact.getChild(SHA1);
        return (sha != null) ? checkSHA(artifact, sha.getInputStream()) : null;
    }

    protected boolean checkSHA(Node artifact, InputStream shaStream) throws IOException {
        final String shaFromSha = IOUtils.readSha1(shaStream);
        final String shaFromArtifact = IOUtils.sha1(artifact.getInputStream());
        return shaFromArtifact.equals(shaFromSha);
    }

    protected Node getOrCreateParent(ArtifactContext context) {
        Node parent = getFromRootNode(context, false);
        if (parent == null) {
            parent = root.createParent(context);
        }
        return parent;
    }

    protected Node getFromRootNode(ArtifactContext context, boolean addLeaf) {
        return fromAdapters(Collections.singleton(root), context, addLeaf);
    }

    protected Node getFromAllRoots(ArtifactContext context, boolean addLeaf) {
        LookupCaching.enable();
        try {
            return fromAdapters(roots, context, addLeaf);
        } finally {
            LookupCaching.disable();
        }
    }

    protected static Node fromAdapters(Iterable<ArtifactContextAdapter> adapters, ArtifactContext context, boolean addLeaf) {
        for (ArtifactContextAdapter ext : adapters) {
            Node node = ext.findParent(context);
            if (node != null) {
                if (addLeaf)
                    node = node.getChild(ext.getArtifactName(context));

                if (node != null)
                    return node;
            }
        }

        return null; // not found
    }
}
