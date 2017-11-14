/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.webdav;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.eclipse.ceylon.cmr.impl.NodeUtils;
import org.eclipse.ceylon.cmr.impl.URLContentStore;
import org.eclipse.ceylon.cmr.repository.webdav.WebDAVInputStream;
import org.eclipse.ceylon.cmr.repository.webdav.WebDAVRepository;
import org.eclipse.ceylon.cmr.repository.webdav.WebDAVResource;
import org.eclipse.ceylon.cmr.spi.ContentHandle;
import org.eclipse.ceylon.cmr.spi.ContentOptions;
import org.eclipse.ceylon.cmr.spi.Node;
import org.eclipse.ceylon.cmr.spi.OpenNode;
import org.eclipse.ceylon.cmr.spi.SizedInputStream;
import org.eclipse.ceylon.common.log.Logger;
import org.eclipse.ceylon.model.cmr.RepositoryException;

/**
 * WebDAV content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 * @author Stef Epardaud
 */
public class WebDAVContentStore extends URLContentStore {

    private boolean forcedAuthenticationForPutOnHerd = false;
    private WebDAVRepository repository;

    /**
     * For tests only!!!
     */
    public WebDAVContentStore(String root, Logger log, boolean offline, int timeout, Proxy proxy, String apiVersion) {
        super(root, log, offline, timeout, proxy, apiVersion);
        this.repository = new WebDAVRepository(timeout, null, null);
    }

    /**
     * WARNING: used by reflection from CeylonUtils
     */
    public WebDAVContentStore(String root, Logger log, boolean offline, int timeout, Proxy proxy, 
            String username, String password) {
        super(root, log, offline, timeout, proxy);
        setUsername(username);
        setPassword(password);
        this.repository = new WebDAVRepository(timeout, username, password);
    }

    @Override
    public OpenNode create(Node parent, String child) {
        if (!connectionAllowed()) {
            return null;
        }
        try {
            if (!isHerd())
                mkdirs(parent);
            return createNode(child);
        } catch (IOException e) {
            throw convertIOException(e);
        }
    }

    @Override
    public ContentHandle peekContent(Node node) {
        if (!connectionAllowed()) {
            return null;
        }
        try {
            final String url = getUrlAsString(node);
            return (repository.exists(url) ? new WebDAVContentHandle(url) : null);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public ContentHandle getContent(Node node) throws IOException {
        return new WebDAVContentHandle(getUrlAsString(node));
    }

    @Override
    public ContentHandle putContent(Node node, InputStream stream, ContentOptions options) throws IOException {
        if (!connectionAllowed()) {
            return null;
        }
        try {
            /*
             * Most disgusting trick ever. Stef failed to set up Sardine to do preemptive auth on all hosts
             * and ports (may only work on port 80, reading the code), so when not using Herd we generate a ton
             * of requests that will trigger auth, but not for Herd. So we start with a PUT and that replies with
             * an UNAUTHORIZED response, which Sardine can't handle because the InputStream is not "restartable".
             * By making an extra HEAD request (restartable because no entity body) we force the auth to happen.
             * Yuk.
             */
            if (isHerd() && !forcedAuthenticationForPutOnHerd) {
                repository.exists(getUrlAsString(node));
                forcedAuthenticationForPutOnHerd = true;
            }
            final Node parent = NodeUtils.firstParent(node);
            if (!isHerd())
                mkdirs(parent);

            final String pUrl = getUrlAsString(parent);
            String token = null;
            if (!isHerd())
                token = repository.lock(pUrl); // local parent
            final String url = getUrlAsString(node);
            try {
                repository.put(url, stream);
                return new WebDAVContentHandle(url);
            } catch (SocketTimeoutException x) {
                SocketTimeoutException ret = new SocketTimeoutException("Timed out writing to "+url);
                ret.initCause(x);
                throw ret;
            } finally {
                if (!isHerd())
                    repository.unlock(pUrl, token);
            }
        } catch (IOException x) {
            throw convertIOException(x);
        }
    }

    private RepositoryException convertIOException(IOException x) {
        String msg = repository.getBetterExceptionMessage(x, this.root);
        if(msg != null){
            return new RepositoryException(msg);
        }
        return new RepositoryException(x);
    }

    private void mkdirs(Node parent) throws IOException {
        if (parent == null)
            return;

        mkdirs(NodeUtils.firstParent(parent));

        final String url = getUrlAsString(parent);
        if (repository.exists(url) == false) {
            repository.createDirectory(url);
        }
    }

    @Override
    protected ContentHandle createContentHandle(Node parent, String child, String path, Node node) {
        return new WebDAVContentHandle(root + path);
    }

    @Override
    public Iterable<? extends OpenNode> find(Node parent) {
        if (!connectionAllowed()) {
            return Collections.emptyList();
        }
        final String url = getUrlAsString(parent);
        try {
            final List<OpenNode> nodes = new ArrayList<>();
            final List<WebDAVResource> resources = repository.list(url);
            for (WebDAVResource dr : resources) {
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
        if (!connectionAllowed()) {
            return false;
        }
        try {
            return repository.exists(getUrlAsString(path));
        } catch (IOException e) {
            log.debug("Failed to check url: " + path);
            return false;
        }
    }

    @Override
    protected boolean urlExists(URL url) {
        if (!connectionAllowed()) {
            return false;
        }
        try {
            return repository.exists(url.toExternalForm());
        } catch (IOException e) {
            log.debug("Failed to check url: " + url);
            return false;
        }
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

        @Override
        public boolean hasBinaries() {
            if (!connectionAllowed()) {
                return false;
            }
            try {
                final List<WebDAVResource> list = repository.list(url);
                return list.size() == 1 && list.get(0).isDirectory() == false;
            } catch (IOException e) {
                log.warning("Cannot list resources: " + url + "; error - " + e);
                return false;
            }
        }

        @Override
        public InputStream getBinariesAsStream() throws IOException {
            SizedInputStream ret = getBinariesAsSizedStream();
            return ret != null ? ret.getInputStream() : null;
        }
        
        @Override
        public SizedInputStream getBinariesAsSizedStream() throws IOException {
            if (!connectionAllowed()) {
                return null;
            }
            WebDAVInputStream inputStream = repository.get(url);
            Long length = inputStream.getLength();
            return new SizedInputStream(inputStream.getInputStream(), length != null ? length.longValue() : -1);
        }

        @Override
        public File getContentAsFile() throws IOException {
            return null;
        }

        @Override
        public long getSize() throws IOException {
            if (connectionAllowed()) {
                if (isHerd()) {
                    return size(new URL(url));
                }

                final List<WebDAVResource> list = repository.list(url);
                if (list.isEmpty() == false && list.get(0).isDirectory() == false) {
                    Long length = list.get(0).getContentLength();
                    if (length != null) {
                        return length;
                    }
                }
            }
            return -1L;
        }

        @Override
        public long getLastModified() throws IOException {
            if (connectionAllowed()) {
                if (isHerd()) {
                    return lastModified(new URL(url));
                }

                final List<WebDAVResource> list = repository.list(url);
                if (list.isEmpty() == false && list.get(0).isDirectory() == false) {
                    Date modified = list.get(0).getModified();
                    if (modified != null) {
                        return modified.getTime();
                    }
                }
            }
            return -1L;
        }

        @Override
        public void clean() {
        }
    }
}
