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

import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.spi.*;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * URL based content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class URLContentStore implements ContentStore, StructureBuilder {

    private static final String CAR = ".car";
    private static final String JAR = ".jar";
    private static final String SHA1 = ".sha1";
    private static final String ZIP = ".zip";

    protected static final String SEPARATOR = "/";

    protected final String root;
    protected final Set<String> suffixes = new HashSet<String>();
    protected Logger log;

    protected URLContentStore(String root, Logger log) {
        if (root == null)
            throw new IllegalArgumentException("Null root url");
        this.root = root;
        this.log = log;
        addSuffix(CAR);
        addSuffix(JAR);
        addSuffix(ZIP);
        addSuffix(SHA1);
    }

    public void addSuffix(String suffix) {
        suffixes.add(suffix);
    }

    public OpenNode createRoot() {
        final UrlNode node = new UrlRootNode();
        node.addService(ContentStore.class, this);
        node.addService(StructureBuilder.class, this);
        node.setHandle(DefaultNode.HANDLE_MARKER);
        return node;
    }

    private String getFullPath(Node parent, String child) {
        final StringBuilder sb = new StringBuilder(NodeUtils.getFullPath(parent, SEPARATOR));
        if (parent.hasBinaries() == false)
            sb.append(SEPARATOR);
        sb.append(child);
        return sb.toString();
    }

    public OpenNode find(Node parent, String child) {
        final String path = getFullPath(parent, child);
        if (urlExists(path)) {
            final UrlNode node = createNode(child);
            ContentHandle handle;
            if (hasContent(child))
                handle = createContentHandle(parent, child, path, node);
            else
                handle = DefaultNode.HANDLE_MARKER;
            node.setHandle(handle);
            return node;
        } else {
            return null;
        }
    }

    protected UrlNode createNode(String label) {
        return new UrlNode(label);
    }

    protected abstract ContentHandle createContentHandle(Node parent, String child, String path, Node node);

    protected String getUrlAsString(Node node) {
        return getUrlAsString(NodeUtils.getFullPath(node, SEPARATOR));
    }

    protected String getUrlAsString(String path) {
        return root + path;
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

    protected abstract boolean urlExists(URL url);

    protected boolean hasContent(String child) {
        for (String suffix : suffixes)
            if (child.endsWith(suffix))
                return true;
        return false;
    }

    protected static class UrlNode extends DefaultNode {
        public UrlNode(String label) {
            super(label);
        }

        public boolean isRemote() {
            return true;
        }

        public void setContentMarker() {
            setHandle(HANDLE_MARKER);
        }

        public void setHandle(ContentHandle handle) {
            super.setHandle(handle);
        }
    }

    protected static class UrlRootNode extends UrlNode {
        public UrlRootNode() {
            super("");
        }

        @Override
        public boolean equals(Object obj) {
            return (obj == this);
        }
    }
}
