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
import com.redhat.ceylon.cmr.spi.MergeStrategy;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Default node impl.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class DefaultNode implements OpenNode {

    private String label;
    private ConcurrentMap<String, Node> parents = new ConcurrentHashMap<String, Node>();
    private ConcurrentMap<String, Node> children = new ConcurrentHashMap<String, Node>();

    private volatile ContentHandle handle;
    private volatile MergeStrategy strategy;
    private volatile ContentStore contentStore;

    public DefaultNode() {
    }

    public DefaultNode(String label) {
        this.label = label;
    }

    public DefaultNode(ContentStore contentStore) {
        this("<root>");
        setContentStore(contentStore);
    }

    protected ContentStore getContentStore() {
        if (contentStore != null)
            return contentStore;

        for (Node parent : getParents()) {
            if (parent instanceof DefaultNode) {
                DefaultNode dn = (DefaultNode) parent;
                ContentStore cs = dn.getContentStore();
                if (cs != null) {
                    setContentStore(cs);
                    return cs;
                }
            }
        }

        throw new IllegalArgumentException("No content store defined in node chain!");
    }

    protected void setContentStore(ContentStore contentStore) {
        this.contentStore = contentStore;
    }

    protected MergeStrategy getStrategy() {
        if (strategy != null)
            return strategy;

        for (Node parent : getParents()) {
            if (parent instanceof DefaultNode) {
                DefaultNode dn = (DefaultNode) parent;
                MergeStrategy ms = dn.getStrategy();
                if (ms != null) {
                    setMergeStrategy(ms);
                    return ms;
                }
            }
        }

        throw new IllegalArgumentException("No merge strategy defined in node chain!");
    }

    @Override
    public void setMergeStrategy(MergeStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void merge(Node other) {
        // TODO
    }

    @Override
    public Node addNode(String label) {
        try {
            //noinspection NullableProblems
            return addNode(label, null, true);
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
    public Node addContent(String label, InputStream content) throws IOException {
        return addNode(label, content, false);
    }

    protected Node addNode(String label, InputStream content, boolean allowNoContent) throws IOException {
        if (content == null && allowNoContent == false)
            throw new IllegalArgumentException("Null content not allowed: " + label);

        DefaultNode node = new DefaultNode(label);
        Node previous = children.putIfAbsent(label, node);
        if (previous == null) {
            previous = node;
            node.parents.put(this.label, this);
            if (content != null)
                node.handle = getContentStore().putContent(node, content);
        } else if (content != null) {
            throw new IllegalArgumentException("Content node already exists: " + label);
        }
        return previous;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Node getChild(String label) {
        return children.get(label);
    }

    @Override
    public Iterable<Node> getChildren() {
        return children.values();
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public InputStream getContent() throws IOException {
        if (handle != null) {
            return handle.getContent();
        } else if (isLeaf()) {
            ContentHandle ch = getContentStore().getContent(this);
            InputStream stream = ch.getContent();
            handle = ch;
            return stream;
        } else {
            return null;
        }
    }

    @Override
    public Node getParent(String label) {
        return parents.get(label);
    }

    @Override
    public Iterable<Node> getParents() {
        return parents.values();
    }

    @Override
    public int hashCode() {
        return label.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node == false)
            return false;

        Node dn = (Node) obj;

        if (label.equals(dn.getLabel()) == false)
            return false;

        // check if we have the same parents
        for (Node p : getParents()) {
            for (Node dp : dn.getParents()) {
                if (p.equals(dp))
                    return true; // one is enough to make it true
            }
        }

        return false;
    }
}
