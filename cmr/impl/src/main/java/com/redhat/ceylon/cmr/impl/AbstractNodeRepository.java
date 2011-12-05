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

    protected static final String CAR = ".car";
    protected static final String SHA = ".sha";
    protected static final String LOCAL = ".local";

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

    protected String getArtifactName(ArtifactContext context, String extension) {
        return context.getName() + "-" + context.getVersion() + extension;
    }

    protected List<String> getPath(ArtifactContext context, boolean addLeaf) {
        String name = context.getName();
        List<String> tokens = new ArrayList<String>();
        tokens.addAll(Arrays.asList(name.split("\\.")));
        tokens.add(context.getVersion()); // add version
        if (addLeaf)
            tokens.add(getLabel(context)); // add leaf name
        return tokens;
    }

    public void putArtifact(ArtifactContext context, InputStream content) throws IOException {
        List<String> tokens = getPath(context, false);
        Node parent = getNode(tokens);
        if (parent == null) {
            OpenNode current = root;
            for (String path : tokens)
                current = current.addNode(path);
            parent = current;
        }

        String label = getLabel(context);
        if (parent instanceof OpenNode) {
            OpenNode on = (OpenNode) parent;
            on.addContent(label, content);
        } else {
            throw new IOException("Parent node is not open: " + parent);
        }
    }

    protected Node getLeafNode(ArtifactContext context) {
        Node node = getNode(getPath(context, true));
        if (node == null) {
            if (context.isThrowErrorIfMissing())
                throw new IllegalArgumentException("No such artifact: " + context);
            return null;
        }

        if (context.isIgnoreSHA() == false) {
            try {
                checkSHA(node);
            } catch (IOException e) {
                log.warning("Cannot check SHA: " + e);
            }
        }

        return node;
    }

    protected boolean checkSHA(Node artifact) throws IOException {
        Node sha = artifact.getChild(SHA);
        if (sha != null) {
            checkSHA(artifact, sha.getContent());
            return true;
        }
        return false;
    }

    protected void checkSHA(Node artifact, InputStream shaStream) throws IOException {
        // TODO
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

    protected String getLabel(ArtifactContext context) {
        return getArtifactName(context, CAR);
    }
}
