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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import com.redhat.ceylon.cmr.spi.ContentHandle;
import com.redhat.ceylon.cmr.spi.ContentOptions;
import com.redhat.ceylon.cmr.spi.ContentStore;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;

/**
 * Default node impl.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@SuppressWarnings({"NullableProblems"})
public class DefaultNode extends AbstractOpenNode {

    private static final long serialVersionUID = 1L;

    private transient ContentHandle handle;
    private boolean remote = false;

    public DefaultNode() {
        // serialization only
    }

    public DefaultNode(String label) {
        super(label, null);
    }

    public DefaultNode(String label, Object value) {
        super(label, value);
    }

    public void setContentMarker() {
        setHandle(HANDLE_MARKER);
    }

    // make sure the handle is properly set
    public synchronized void setHandle(ContentHandle handle) {
        this.handle = handle;
    }

    public boolean isRemote() {
        return remote;
    }

    void setRemote(boolean remote) {
        this.remote = remote;
    }

    @Override
    public void merge(OpenNode other) {
        if (other == null)
            throw new IllegalArgumentException("Null node!");

        // Node root = NodeUtils.getRoot(this);
        // Node or = NodeUtils.getRoot(other);
        // TODO
    }

    @Override
    public OpenNode addNode(String label, Object value) {
        try {
            //noinspection NullableProblems
            return addNode(label, value, null, ContentOptions.DEFAULT, true);
        } catch (IOException e) {
            throw new RuntimeException("Should not be here!", e);
        }
    }

    @Override
    public Node removeNode(String label) {
        final Node node = super.removeNode(label);
        if (node instanceof DefaultNode) {
            DefaultNode dn = (DefaultNode) node;
            ContentHandle ch = dn.handle;
            dn.handle = null;
            if (ch != null) {
                ch.clean();
            }
        }
        return node;
    }

    @Override
    public OpenNode addContent(String label, InputStream content, ContentOptions options) throws IOException {
        return addNode(label, null, content, options, false);
    }

    @Override
    public <T extends Serializable> OpenNode addContent(String label, T content, ContentOptions options) throws IOException {
        try (InputStream stream = IOUtils.toObjectStream(content)) {
            return addContent(label, stream, options);
        }
    }

    protected OpenNode addNode(final String label, final Object value, InputStream content, ContentOptions options, boolean allowNoContent) throws IOException {
        if (label == null)
            throw new IllegalArgumentException("Null label");
        if (content == null && allowNoContent == false)
            throw new IllegalArgumentException("Null content not allowed: " + label);

        final DefaultNode node = new DefaultNode(label, value);
        OpenNode previous = putChildIfAbsent(label, node);
        if (previous == null) {
            previous = node;
            node.putParentIfAbsent(getLabel(), this);
            if (content != null) {
                final ContentStore contentStore = findService(ContentStore.class);
                node.handle = contentStore.putContent(node, content, options);
            }
        } else if (content != null) {
            if (options.forceOperation()) {
                final ContentStore contentStore = findService(ContentStore.class);
                ContentHandle ch = contentStore.putContent(previous, content, options);
                if (previous instanceof DefaultNode) {
                    DefaultNode dn = (DefaultNode) previous;
                    dn.handle = ch;
                }
            } else {
                throw new IllegalArgumentException("Content node already exists: " + label);
            }
        }
        return previous;
    }

    @Override
    public boolean hasBinaries() {
        synchronized (this) {
            if (handle != null)
                return handle.hasBinaries();
        }

        final ContentStore cs = findService(ContentStore.class);
        final ContentHandle ch = cs.peekContent(this);

        synchronized (this) {
            handle = (ch == null) ? HANDLE_MARKER : ch;
        }

        return (ch != null && ch.hasBinaries());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getContent(Class<T> contentType) throws IOException {
        if (File.class.equals(contentType)) {
            synchronized (this) {
                if (handle != null)
                    return (T) handle.getContentAsFile();
            }

            final ContentStore cs = findService(ContentStore.class);
            ContentHandle ch = cs.getContent(this);
            if (ch == null) {
                ch = HANDLE_MARKER;
            }

            synchronized (this) {
                handle = ch;
            }

            return (T) ch.getContentAsFile();
        } else {
            return super.getContent(contentType);
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        synchronized (this) {
            if (handle != null)
                return handle.getBinariesAsStream();
        }

        final ContentStore cs = findService(ContentStore.class);
        ContentHandle ch = cs.getContent(this);
        if (ch == null) {
            ch = HANDLE_MARKER;
        }

        synchronized (this) {
            handle = ch;
        }

        return ch.getBinariesAsStream();
    }

    @Override
    public long getLastModified() throws IOException {
        synchronized (this) {
            if (handle != null)
                return handle.getLastModified();
        }

        final ContentStore cs = findService(ContentStore.class);
        ContentHandle ch = cs.getContent(this);
        if (ch == null) {
            ch = HANDLE_MARKER;
        }

        synchronized (this) {
            handle = ch;
        }

        return ch.getLastModified();
    }

    @Override
    public long getSize() throws IOException {
        synchronized (this) {
            if (handle != null)
                return handle.getSize();
        }

        final ContentStore cs = findService(ContentStore.class);
        ContentHandle ch = cs.getContent(this);
        if (ch == null) {
            ch = HANDLE_MARKER;
        }

        synchronized (this) {
            handle = ch;
        }

        return ch.getSize();
    }

    @Override public String getStoreDisplayString() {
        final ContentStore cs = findService(ContentStore.class);
        return cs.getDisplayString();
    }

    @Override
    public String getDisplayString() {
        return getLabel();
    }
}
