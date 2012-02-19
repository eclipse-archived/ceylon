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

package com.redhat.ceylon.cmr.webdav;

import com.googlecode.sardine.DavResource;
import com.googlecode.sardine.Sardine;
import com.googlecode.sardine.SardineFactory;
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.impl.NodeUtils;
import com.redhat.ceylon.cmr.impl.URLContentStore;
import com.redhat.ceylon.cmr.spi.ContentHandle;
import com.redhat.ceylon.cmr.spi.ContentOptions;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * WebDAV content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class WebDAVContentStore extends URLContentStore {

    private volatile Sardine sardine;
    private String username;
    private String password;

    public WebDAVContentStore(String root, Logger log) {
        super(root, log);
    }

    protected Sardine getSardine() {
        if (sardine == null) {
            synchronized (this) {
                if (sardine == null) {
                    sardine = (username == null || password == null) ? SardineFactory.begin() : SardineFactory.begin(username, password);
                }
            }
        }
        return sardine;
    }

    public OpenNode create(Node parent, String child) {
        try {
            mkdirs(parent);
            return createNode(child);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public ContentHandle peekContent(Node node) {
        try {
            final String url = getUrlAsString(node);
            return (getSardine().exists(url) ? new WebDAVContentHandle(url) : null);
        } catch (IOException e) {
            return null;
        }
    }

    public ContentHandle getContent(Node node) throws IOException {
        return new WebDAVContentHandle(getUrlAsString(node));
    }

    public ContentHandle putContent(Node node, InputStream stream, ContentOptions options) throws IOException {
        final Sardine s = getSardine();

        final Node parent = NodeUtils.firstParent(node);
        mkdirs(s, parent);

        final String pUrl = getUrlAsString(parent);
        final String token = s.lock(pUrl); // local parent
        try {
            final String url = getUrlAsString(node);
            s.put(url, stream);
            return new WebDAVContentHandle(url);
        } finally {
            s.unlock(pUrl, token);
        }
    }

    protected void mkdirs(Sardine s, Node parent) throws IOException {
        if (parent == null)
            return;

        mkdirs(s, NodeUtils.firstParent(parent));

        final String url = getUrlAsString(parent);
        if (s.exists(url) == false) {
            s.createDirectory(url);
        }
    }

    protected ContentHandle createContentHandle(Node parent, String child, String path, Node node) {
        return new WebDAVContentHandle(path);
    }

    public Iterable<? extends OpenNode> find(Node parent) {
        final String url = getUrlAsString(parent);
        try {
            final List<OpenNode> nodes = new ArrayList<OpenNode>();
            final List<DavResource> resources = getSardine().list(url);
            for (DavResource dr : resources) {
                final String label = dr.getName();
                final RemoteNode node = new RemoteNode(label);
                if (dr.isDirectory())
                    node.setContentMarker();
                else
                    node.setHandle(new WebDAVContentHandle(url + label));
                nodes.add(node);
            }
            return nodes;
        } catch (IOException e) {
            log.debug("Failed to list url: " + url);
            return Collections.emptyList();
        }
    }

    @Override
    protected boolean urlExists(String path) {
        try {
            return getSardine().exists(getUrlAsString(path));
        } catch (IOException e) {
            log.debug("Failed to check url: " + path);
            return false;
        }
    }

    protected boolean urlExists(URL url) {
        try {
            return getSardine().exists(url.toExternalForm());
        } catch (IOException e) {
            log.debug("Failed to check url: " + url);
            return false;
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "WebDAV content store: " + root;
    }

    private class WebDAVContentHandle implements ContentHandle {

        private final String url;

        private WebDAVContentHandle(String url) {
            this.url = url;
        }

        public boolean hasBinaries() {
            return true;
        }

        public InputStream getBinariesAsStream() throws IOException {
            return getSardine().get(url);
        }

        public File getContentAsFile() throws IOException {
            return null;
        }

        public void clean() {
        }
    }
}
