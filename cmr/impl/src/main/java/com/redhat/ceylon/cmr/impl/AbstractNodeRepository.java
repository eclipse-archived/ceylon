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
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractNodeRepository extends AbstractRepository {

    protected static final String SHA1 = ".sha1";
    protected static final String LOCAL = ".local";
    protected static final String CACHED = ".cached";

    protected OpenNode root; // main local root
    protected List<OpenNode> roots = new CopyOnWriteArrayList<OpenNode>(); // lookup roots - order matters! 

    public AbstractNodeRepository(Logger log) {
        super(log);
    }

    protected OpenNode getRoot() {
        if (root == null)
            throw new IllegalArgumentException("Missing root!");

        return root;
    }

    protected void setRoot(OpenNode root) {
        if (root == null)
            throw new IllegalArgumentException("Null root");
        if (this.root != null)
            throw new IllegalArgumentException("Root already set!");

        this.root = root;
        this.roots.add(root);
    }

    protected void prependExternalRoot(OpenNode external) {
        roots.add(0, external);
    }

    protected void appendExternalRoot(OpenNode external) {
        roots.add(external);
    }

    protected void removeExternalRoot(OpenNode external) {
        roots.remove(external);
    }

    protected String getArtifactName(ArtifactContext context) {
        return getArtifactName(context.getName(), context.getVersion(), context.getSuffix());
    }

    protected String getArtifactName(String name, String version, String suffix) {
        if (DEFAULT_MODULE.equals(name))
            return name + suffix;
        else
            return name + "-" + version + suffix;
    }

    protected List<String> getPath(ArtifactContext context, boolean addLeaf) {
        final String name = context.getName();
        final List<String> tokens = new ArrayList<String>();
        tokens.addAll(Arrays.asList(name.split("\\.")));
        final String version = context.getVersion();
        if (!DEFAULT_MODULE.equals(name))
            tokens.add(version); // add version
        if (addLeaf)
            tokens.add(getArtifactName(context)); // add leaf name
        return tokens;
    }

    public void putArtifact(ArtifactContext context, InputStream content) throws IOException {
        final List<String> tokens = getPath(context, false);
        Node parent = getFromRootNode(tokens);
        if (parent == null) {
            OpenNode current = root;
            for (String path : tokens)
                current = current.addNode(path);
            parent = current;
        }

        final String label = getArtifactName(context);
        if (parent instanceof OpenNode) {
            final OpenNode on = (OpenNode) parent;
            if (on.addContent(label, content, context) == null)
                addContent(context, parent, label, content);
        } else {
            addContent(context, parent, label, content);
        }
    }

    protected void addContent(ArtifactContext context, Node parent, String label, InputStream content) throws IOException {
        throw new IOException("Cannot add child [" + label + "] content [" + content + "] on parent node: " + parent);
    }

    public void removeArtifact(ArtifactContext context) throws IOException {
        final List<String> tokens = getPath(context, false);
        Node parent = getFromRootNode(tokens);
        if (parent != null) {
            final String label = getArtifactName(context);
            removeNode(parent, label);
        } else {
            log.info("No such artifact: " + context);
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
        final Node node = getFromAllRoots(getPath(context, true));
        if (node == null) {
            if (context.isThrowErrorIfMissing())
                throw new IllegalArgumentException("No such artifact: " + context);
            return null;
        }

        if (context.isIgnoreSHA() == false) {
            Boolean result = null;
            Node shaResult = node.getChild(SHA1 + CACHED);
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

    protected Node getFromRootNode(final Iterable<String> tokens) {
        return NodeUtils.getNode(root, tokens);
    }

    protected Node getFromAllRoots(final Iterable<String> tokens) {
        for (Node ext : roots) {
            Node node = NodeUtils.getNode(ext, tokens);
            if (node != null)
                return node;
        }

        return null; // not found
    }
}
