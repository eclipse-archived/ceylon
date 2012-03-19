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
import com.redhat.ceylon.cmr.spi.ContentHandle;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;

import java.net.URL;

/**
 * URL based content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class URLContentStore extends AbstractRemoteContentStore {

    protected final String root;
    protected String username;
    protected String password;

    protected URLContentStore(String root, Logger log) {
        super(log);
        if (root == null)
            throw new IllegalArgumentException("Null root url");
        this.root = root;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public OpenNode find(Node parent, String child) {
        final String path = getFullPath(parent, child);
        if (urlExists(path)) {
            final RemoteNode node = createNode(child);
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

    @Override
    public String getDisplayString() {
        return root;
    }
}
