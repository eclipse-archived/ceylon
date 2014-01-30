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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.redhat.ceylon.cmr.spi.ContentHandle;
import com.redhat.ceylon.cmr.spi.ContentTransformer;
import com.redhat.ceylon.cmr.spi.MergeStrategy;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;
import com.redhat.ceylon.cmr.spi.StructureBuilder;

/**
 * Abstract node impl.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@SuppressWarnings({"NullableProblems"})
public abstract class AbstractOpenNode implements OpenNode, Serializable {

    private static final long serialVersionUID = 1L;
    private static final String NODE_MARKER = "#marker#";

    protected static final ContentHandle HANDLE_MARKER = new ContentHandle() {
        public boolean hasBinaries() {
            return false;
        }

        public InputStream getBinariesAsStream() throws IOException {
            return null;
        }

        public File getContentAsFile() throws IOException {
            return null;
        }

        public long getLastModified() throws IOException {
            return -1L;
        }

        public long getSize() throws IOException {
            return -1L;
        }

        public void clean() {
        }
    };

    private String label;
    private Object value;
    private final ConcurrentMap<String, OpenNode> parents = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, OpenNode> children = new ConcurrentHashMap<>();

    private transient final Map<Class<?>, Object> services = new WeakHashMap<>();

    public AbstractOpenNode() {
        // serialization only
    }

    public AbstractOpenNode(String label, Object value) {
        this.label = label;
        this.value = value;
    }

    protected <T> T findService(Class<T> serviceType) {
        T service = getService(serviceType);
        if (service != null)
            return service;

        for (Node parent : getParents()) {
            if (parent instanceof AbstractOpenNode) {
                AbstractOpenNode dn = (AbstractOpenNode) parent;
                T ps = dn.findService(serviceType);
                if (ps != null) {
                    addService(serviceType, ps);
                    return ps;
                }
            }
        }

        throw new IllegalArgumentException("No such service [" + serviceType + "] found in node chain!");
    }

    public synchronized <T> void addService(Class<T> serviceType, T service) {
        if (serviceType == null)
            throw new IllegalArgumentException("Null service type");

        if (service != null)
            services.put(serviceType, service);
        else
            services.remove(serviceType);
    }

    public synchronized <T> T getService(Class<T> serviceType) {
        return serviceType.cast(services.get(serviceType));
    }

    protected OpenNode putChildIfAbsent(String label, OpenNode child) {
        return children.putIfAbsent(label, child);
    }

    protected OpenNode putParentIfAbsent(String label, OpenNode parent) {
        return parents.putIfAbsent(label, parent);
    }

    @Override
    public void link(OpenNode child) {
        if (child == null)
            throw new IllegalArgumentException("Null node!");

        OpenNode previous = children.putIfAbsent(child.getLabel(), child);
        if (previous == null) {
            if (child instanceof AbstractOpenNode) {
                AbstractOpenNode dn = (AbstractOpenNode) child;
                dn.parents.put(getLabel(), this);
            }
        } else {
            MergeStrategy ms = findService(MergeStrategy.class);
            ms.conflict(previous, child);
        }
    }

    @Override
    public OpenNode addNode(String label) {
        return addNode(label, null);
    }

    @Override
    public OpenNode createNode(String label) {
        return getNode(label, true);
    }

    @Override
    public Node removeNode(String label) {
        // get node, so we actually have the right instance to fully remove
        final Node node = getChild(label);
        if (node != null) {
            children.remove(label);
            children.remove(label + NODE_MARKER);
        }
        return node;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public <T> T getValue(Class<T> valueType) {
        if (valueType == null)
            throw new IllegalArgumentException("Null value type");

        return valueType.cast(value);
    }

    @Override
    public OpenNode peekChild(String label) {
        return children.get(label);
    }

    @Override
    public Node getChild(String label) {
        OpenNode child = children.get(label);
        if (child == null) {
            final String markerLabel = label + NODE_MARKER;
            final OpenNode marker = children.get(markerLabel);
            if (marker == null) {
                child = getNode(label, false);
                children.put(markerLabel, new MarkerNode(label, child));
            } else {
                return marker.getValue(Node.class);
            }
        }
        return child;
    }

    protected OpenNode getNode(String label, boolean create) {
        final StructureBuilder builder = findService(StructureBuilder.class);
        OpenNode child = create ? builder.create(this, label) : builder.find(this, label);
        if (child != null) {
            child = put(children, label, child);
        }
        return child;
    }

    protected OpenNode put(ConcurrentMap<String, OpenNode> map, String label, OpenNode child) {
        final OpenNode previous = map.putIfAbsent(label, child);
        if (previous == null) {
            if (child instanceof AbstractOpenNode) {
                final AbstractOpenNode dn = (AbstractOpenNode) child;
                dn.parents.put(getLabel(), this);
            }
        } else {
            child = previous; // replace
        }
        return child;
    }

    @Override
    public Iterable<? extends Node> getChildren() {
        if (!children.containsKey(NODE_MARKER)) {
            children.put(NODE_MARKER, new MarkerNode()); // add marker

            ConcurrentMap<String, OpenNode> tmp = new ConcurrentHashMap<>();
            for (OpenNode on : findService(StructureBuilder.class).find(this))
                put(tmp, on.getLabel(), on);
            children.putAll(tmp);

            return tmp.values();
        } else {
            List<Node> nodes = new ArrayList<>();
            for (Node on : children.values()) {
                if (on instanceof MarkerNode == false)
                    nodes.add(on);
            }
            return nodes;
        }
    }

    @Override
    public void refresh(boolean recurse) {
        Iterator<Map.Entry<String, OpenNode>> iter = children.entrySet().iterator();
        while (iter.hasNext()) {
            final Map.Entry<String, OpenNode> entry = iter.next();
            if (recurse)
                entry.getValue().refresh(recurse); // recurse
            // remove the markers
            final String key = entry.getKey();
            if (key.endsWith(NODE_MARKER))
                iter.remove();
        }
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <T> T getContent(Class<T> contentType) throws IOException {
        if (contentType == null)
            throw new IllegalArgumentException("Null content type!");

        if (InputStream.class.equals(contentType)) {
            return (T) getInputStream();
        } else {
            final ContentTransformer ct = findService(ContentTransformer.class);
            if (ct != null)
                return ct.transform(contentType, new LazyInputStream());
            else
                return IOUtils.fromStream(contentType, getInputStream());
        }
    }

    @Override
    public Node getParent(String label) {
        return parents.get(label);
    }

    @Override
    public Iterable<? extends Node> getParents() {
        return parents.values();
    }

    @Override
    public String toString() {
        return "[" + getLabel() + "]";
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
    
    @Override
    public String getStoreDisplayString() {
        return "";
    };

    protected class LazyInputStream extends InputStream {
        private InputStream delegate;

        private InputStream getDelegate() throws IOException {
            if (delegate == null) {
                InputStream is = AbstractOpenNode.this.getInputStream();
                if (is == null)
                    throw new IllegalArgumentException("Null input stream!");
                delegate = is;
            }
            return delegate;
        }

        public int read() throws IOException {
            return getDelegate().read();
        }

        @Override
        public int read(byte[] b) throws IOException {
            return getDelegate().read(b);
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return getDelegate().read(b, off, len);
        }

        @Override
        public long skip(long n) throws IOException {
            return getDelegate().skip(n);
        }

        @Override
        public int available() throws IOException {
            return getDelegate().available();
        }

        @Override
        public void mark(int readlimit) {
            try {
                getDelegate().mark(readlimit);
            } catch (IOException ignored) {
            }
        }

        @Override
        public void reset() throws IOException {
            getDelegate().reset();
        }

        @Override
        public boolean markSupported() {
            try {
                return getDelegate().markSupported();
            } catch (IOException ignored) {
                return false;
            }
        }

        public void close() throws IOException {
            if (delegate != null)
                delegate.close();
        }
    }
}
