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

import com.redhat.ceylon.cmr.spi.ContentHandle;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Collections;

/**
 * Remote content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RemoteContentStore extends URLContentStore {

    public RemoteContentStore(String root) {
        super(root);
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

    protected UrlNode createNode(String label) {
        return new RemoteNode(label);
    }

    protected ContentHandle createContentHandle(Node parent, String child, String path, Node node) {
        return new RemoteContentHandle(node);
    }

    public Iterable<? extends OpenNode> find(Node parent) {
        return Collections.emptyList(); // cannot find all children
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

    @Override
    public String toString() {
        return "RemoteContentStore: " + root;
    }

    private class RemoteContentHandle implements ContentHandle {
        private final Node node;

        private RemoteContentHandle(Node node) {
            this.node = node;
        }

        public InputStream getContentAsStream() throws IOException {
            final URL url = getURL(NodeUtils.getFullPath(node, SEPARATOR));
            log.info("Fetching resource: " + url);
            return url.openStream();
        }

        public File getContentAsFile() throws IOException {
            return null;  // unsupported
        }

        public void clean() {
        }
    }

    private static class RemoteNode extends UrlNode {
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
