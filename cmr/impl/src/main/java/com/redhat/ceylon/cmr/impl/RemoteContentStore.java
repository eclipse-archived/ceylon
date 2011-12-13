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

import com.redhat.ceylon.cmr.spi.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Remote content store.
 * 
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RemoteContentStore implements ContentStore, StructureBuilder {
    
    private static final Logger log = Logger.getLogger(RemoteContentStore.class.getName());
    private static final String SEPARATOR = "/";
    private static final String CAR = ".car";
    private static final String JAR = ".jar";

    private final String root;
    private final Set<String> suffixes = new HashSet<String>();

    public RemoteContentStore(String root) {
        if (root == null)
            throw new IllegalArgumentException("Null root url");
        this.root = root;
        addSuffix(CAR);
        addSuffix(JAR);
    }

    public void addSuffix(String suffix) {
        suffixes.add(suffix);        
    }
    
    public ContentHandle popContent(Node node) {
        return urlExists(node) ? new RemoteContentHandle(node) : null;
    }

    public ContentHandle getContent(Node node) throws IOException {
        return new RemoteContentHandle(node);
    }

    public ContentHandle putContent(Node node, InputStream stream) throws IOException {
        return null; // cannot write
    }

    public OpenNode createRoot(String label) {
        final RemoteNode node = new RemoteNode(label);
        node.addService(ContentStore.class, this);
        node.addService(StructureBuilder.class, this);
        node.setHandle(DefaultNode.HANDLE_MARKER);
        return node;
    }

    public OpenNode find(Node parent, String child) {
        final String path = NodeUtils.getFullPath(parent, SEPARATOR) + "/" + child;
        if (urlExists(path)) {
            final RemoteNode node = new RemoteNode(child);
            ContentHandle handle;
            if (hasContent(child))
                handle = new RemoteContentHandle(node);
            else
                handle = DefaultNode.HANDLE_MARKER;
            node.setHandle(handle);
            return node;
        } else {
            return null;
        }
    }

    public Iterable<? extends OpenNode> find(Node parent) {
        return Collections.emptyList(); // cannot find all children
    }

    protected URL getURL(Node node) {
        return getURL(NodeUtils.getFullPath(node, SEPARATOR));
    }

    protected URL getURL(String path) {
        try {
            return new URL(root + path);
        } catch (Exception e) {
            log.warning("Cannot create URL: " + e);
            return null;
        }
    }
    
    protected boolean urlExists(String path) {
        return urlExists(getURL(path));
    }
    
    protected boolean urlExists(Node node) {
        return urlExists(getURL(node));
    }

    protected boolean urlExists(URL url) {
        if (url == null)
            return false;

        InputStream is = null;
        try {
            is = url.openStream();
            return (is != null); // should this do?
        } catch (IOException ignored) {
            return false;
        } finally {
            IOUtils.safeClose(is);
        }
    }
    
    protected boolean hasContent(String child) {
        for (String suffix : suffixes)
            if (child.endsWith(suffix))
                return true;
        return false;
    }

    private class RemoteContentHandle implements ContentHandle {
        private final Node node;

        private RemoteContentHandle(Node node) {
            this.node = node;
        }

        public InputStream getContent() throws IOException {
            final URL url = getURL(NodeUtils.getFullPath(node, SEPARATOR));
            log.info("Fetching resource: " + url);
            return url.openStream();
        }

        public void clean() {
        }
    }

    private static class RemoteNode extends DefaultNode {
        private RemoteNode(String label) {
            super(label);
        }

        @Override
        public OpenNode addContent(String label, InputStream content) throws IOException {
            return null; // cannot add content
        }

        @Override
        public <T extends Serializable> OpenNode addContent(String label, T content) throws IOException {
            return null; // cannot add content
        }
    }
}
