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

import com.redhat.ceylon.cmr.spi.ContentHandle;
import com.redhat.ceylon.cmr.spi.ContentStore;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Default node impl.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@SuppressWarnings({"NullableProblems"})
public class DefaultNode extends AbstractOpenNode {

    private static final long serialVersionUID = 1L;

    private transient ContentHandle handle;

    public DefaultNode() {
        // serialization only
    }

    public DefaultNode(String label) {
        super(label, null);
    }

    public DefaultNode(String label, Object value) {
        super(label, value);
    }

    void setHandle(ContentHandle handle) {
        this.handle = handle;
    }

    @Override
    public void merge(OpenNode other) {
        if (other == null)
            throw new IllegalArgumentException("Null node!");
        
        Node root = NodeUtils.getRoot(this);
        Node or = NodeUtils.getRoot(other);
        // TODO
    }

    @Override
    public OpenNode addNode(String label, Object value) {
        try {
            //noinspection NullableProblems
            return addNode(label, null, null, true);
        } catch (IOException e) {
            throw new RuntimeException("Should not be here!", e);
        }
    }

    @Override
    public Node removeNode(String label) {
        Node node = children.remove(label);
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
    public OpenNode addContent(String label, InputStream content) throws IOException {
        return addNode(label, null, content, false);
    }

    @Override
    public <T extends Serializable> OpenNode addContent(String label, T content) throws IOException {
        InputStream stream = IOUtils.toObjectStream(content);
        return addContent(label, stream);
    }

    protected OpenNode addNode(final String label, final Object value, InputStream content, boolean allowNoContent) throws IOException {
        if (label == null)
            throw new IllegalArgumentException("Null label");
        if (content == null && allowNoContent == false)
            throw new IllegalArgumentException("Null content not allowed: " + label);

        DefaultNode node = new DefaultNode(label, value);
        OpenNode previous = children.putIfAbsent(label, node);
        if (previous == null) {
            previous = node;
            node.parents.put(getLabel(), this);
            if (content != null) {
                ContentStore contentStore = findService(ContentStore.class);
                node.handle = contentStore.putContent(node, content);
            }
        } else if (content != null) {
            throw new IllegalArgumentException("Content node already exists: " + label);
        }
        return previous;
    }

    @Override
    public boolean hasContent() {
        if (handle == HANDLE_MARKER)
            return false;

        ContentHandle ch = findService(ContentStore.class).popContent(this);
        if (ch == null) {
            handle = HANDLE_MARKER;
        }

        return (ch != null);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (handle != null) {
            return handle.getContent();
        } else {
            ContentHandle ch = findService(ContentStore.class).getContent(this);
            if (ch == null) {
                ch = HANDLE_MARKER;
            }
            handle = ch;
            return ch.getContent();
        }
    }
}
