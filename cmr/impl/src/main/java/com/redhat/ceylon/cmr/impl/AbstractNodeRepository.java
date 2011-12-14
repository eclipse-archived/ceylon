/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.redhat.ceylon.cmr.impl;

import com.redhat.ceylon.cmr.api.AbstractRepository;
import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractNodeRepository extends AbstractRepository {

    protected static final String SHA1 = ".sha1";
    protected static final String LOCAL = ".local";
    protected static final String CACHED = ".cached";

    protected OpenNode root;

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
    }

    protected String getArtifactName(ArtifactContext context) {
        return getArtifactName(context.getName(), context.getVersion(), context.getSuffix());
    }

    protected String getArtifactName(String name, String version, String suffix) {
        if (NO_VERSION.equals(version))
            return name + suffix;
        else
            return name + "-" + version + suffix;
    }

    protected List<String> getPath(ArtifactContext context, boolean addLeaf) {
        final String name = context.getName();
        final List<String> tokens = new ArrayList<String>();
        tokens.addAll(Arrays.asList(name.split("\\.")));
        final String version = context.getVersion();
        if (NO_VERSION.equals(version) == false)
            tokens.add(version); // add version
        if (addLeaf)
            tokens.add(getArtifactName(context)); // add leaf name
        return tokens;
    }

    public void putArtifact(ArtifactContext context, InputStream content) throws IOException {
        final List<String> tokens = getPath(context, false);
        Node parent = getNode(tokens);
        if (parent == null) {
            OpenNode current = root;
            for (String path : tokens)
                current = current.addNode(path);
            parent = current;
        }

        final String label = getArtifactName(context);
        if (parent instanceof OpenNode) {
            final OpenNode on = (OpenNode) parent;
            if (on.addContent(label, content) == null)
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
        Node parent = getNode(tokens);
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
        final Node node = getNode(getPath(context, true));
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
        final String shaFromArtifact = IOUtils.sha1(artifact.getInputStream());
        final String shaFromSha = IOUtils.readSha1(shaStream);
        return shaFromArtifact.equals(shaFromSha);
    }

    protected Node getNode(Iterable<String> tokens) {
        Node current = root;
        for (String token : tokens) {
            current = current.getChild(token);
            if (current == null) {
                return null;
            }
        }
        return current;
    }
}
