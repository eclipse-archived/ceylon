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

import com.redhat.ceylon.cmr.spi.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * File content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class FileContentStore implements ContentStore, StructureBuilder {

    private final File root;
    private final ConcurrentMap<Node, File> cache = new ConcurrentHashMap<Node, File>();

    public FileContentStore(File root) {
        if (root == null)
            throw new IllegalArgumentException("Null root!");
        this.root = root;
    }

    @Override
    public String toString() {
        return "FileContentStore: " + root;
    }

    @Override
    public String getDisplayString() {
        return root.getPath();
    }

    File getFile(Node node) {
        if (node == null)
            throw new IllegalArgumentException("Null node");

        return getFileInternal(node);
    }

    private File getFileInternal(Node node) {
        if (node == null)
            return root;

        File file = cache.get(node);
        if (file == null) {
            File parent = getFileInternal(NodeUtils.firstParent(node));
            file = new File(parent, node.getLabel()); // bevare of concatinated names; e.g sha1.local
            cache.put(node, file);
        }
        return file;
    }

    void removeFile(Node node) {
        File file = cache.remove(node);
        if (file == null)
            file = getFile(node);

        delete(file, node);
    }

    void clear() {
        cache.clear();
    }

    protected ContentHandle createContentHandle(Node owner, File file) {
        return file.isDirectory() ? new FolderContentHandle(owner, file) : new FileContentHandle(owner, file);
    }

    public ContentHandle peekContent(Node node) {
        final File file = getFile(node);
        return file.exists() ? createContentHandle(node, file) : null;
    }

    public ContentHandle getContent(Node node) throws IOException {
        final File file = getFile(node);
        if (file.exists() == false)
            throw new IOException("Content doesn't exist: " + file);

        return createContentHandle(node, file);
    }

    public ContentHandle putContent(Node node, InputStream stream, ContentOptions options) throws IOException {
        if (stream == null)
            throw new IllegalArgumentException("Null stream!");
        if (options == null)
            throw new IllegalArgumentException("Null options!");

        final File parent = getFile(NodeUtils.firstParent(node));
        if (parent.exists() == false && parent.mkdirs() == false)
            throw new IOException("Cannot create dirs: " + parent);

        File file;
        if (parent.isDirectory()) {
            file = new File(parent, node.getLabel());
        } else {
            final String path = parent.getPath();
            file = new File(path + node.getLabel()); // just concat paths
        }

        IOUtils.writeToFile(file, stream);
        return new FileContentHandle(node, file);
    }

    public OpenNode createRoot() {
        return new RootNode(this, this);
    }

    public OpenNode create(Node parent, String child) {
        final File pf = getFile(parent);
        final File file = new File(pf, child);
        final DefaultNode node = new DefaultNode(child);
        node.setHandle(new FolderContentHandle(node, file));
        return node;
    }

    public OpenNode find(Node parent, String child) {
        final File pf = getFile(parent);

        File file;
        if (pf.isDirectory()) {
            file = new File(pf, child);
        } else {
            final String path = pf.getPath();
            file = new File(path + child); // just concat paths
        }

        if (file.exists()) {
            final DefaultNode node = new DefaultNode(child);
            node.setHandle(createContentHandle(node, file));
            return node;
        } else {
            return null;
        }
    }

    public Iterable<? extends OpenNode> find(Node parent) {
        final File pf = getFile(parent);
        if (pf.exists()) {
            List<OpenNode> nodes = new ArrayList<OpenNode>();
            for (File file : pf.listFiles()) {
                DefaultNode node = new DefaultNode(file.getName());
                node.setHandle(createContentHandle(node, file));
                nodes.add(node);
            }
            return nodes;
        } else {
            return Collections.emptyList();
        }
    }

    protected void delete(File file, Node node) {
        if (file == null)
            throw new IllegalArgumentException("Null file");
        if (node == null)
            throw new IllegalArgumentException("Null node");

        if (root.equals(file))
            return;

        File[] files = file.listFiles();
        if ((files == null || files.length == 0) && (file.exists() == false || file.delete())) {
            cache.remove(node); // remove from cache, since probably not used anymore
            delete(file.getParentFile(), NodeUtils.firstParent(node));
        }
    }

    private class FileContentHandle implements ContentHandle {

        protected Node owner;
        protected File file;

        private FileContentHandle(Node owner, File file) {
            this.owner = owner;
            this.file = file;
        }

        public boolean hasBinaries() {
            return true;
        }

        public InputStream getBinariesAsStream() throws IOException {
            return new FileInputStream(file);
        }

        public File getContentAsFile() throws IOException {
            return file;
        }

        public long getLastModified() throws IOException {
            return file.lastModified();
        }

        public void clean() {
            delete(file, owner);
        }
    }

    private class FolderContentHandle extends FileContentHandle {

        private FolderContentHandle(Node owner, File file) {
            super(owner, file);
        }

        @Override
        public boolean hasBinaries() {
            return false;
        }

        @Override
        public InputStream getBinariesAsStream() throws IOException {
            return null;
        }

        @Override
        public void clean() {
            try {
                IOUtils.deleteRecursively(file);
            } finally {
                super.clean();
            }
        }
    }

    @Override
    public boolean isOffline() {
        return true;
    }

    @Override
    public boolean isHerd() {
        return false;
    }
}
