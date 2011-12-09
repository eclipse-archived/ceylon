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

    File getFile(Node node) {
        File file = cache.get(node);
        if (file == null) { 
            String path = NodeUtils.getFullPath(node);
            file = new File(root, path);
            cache.put(node, file);
        }
        return file;
    }

    void removeFile(Node node) {
        File file = cache.remove(node);
        if (file != null)
            delete(file, node);
    }
    
    void clear() {
        cache.clear();
    }

    public ContentHandle popContent(Node node) {
        final File file = getFile(node);
        return (file.exists() && file.isFile()) ? new FileContentHandle(node, file) : null;
    }

    public ContentHandle getContent(Node node) throws IOException {
        final File file = getFile(node);
        if (file.exists() == false)
            throw new IOException("Content doesn't exist: " + file);
        else if (file.isDirectory())
            throw new IOException("Content is directory: " + file);

        return new FileContentHandle(node, file);
    }

    public ContentHandle putContent(Node node, InputStream stream) throws IOException {
        if (stream == null)
            throw new IllegalArgumentException("Null stream!");

        final File file = getFile(node);
        if (file.exists())
            throw new IOException("Content already exists: " + file);

        final File parent = file.getParentFile();
        if (parent.exists() == false && parent.mkdirs() == false)
            throw new IOException("Cannot create dirs: " + file);

        IOUtils.writeToFile(file, stream);
        return new FileContentHandle(node, file);
    }

    public OpenNode find(Node parent, String child) {
        final File pf = getFile(parent);

        File file;
        if (pf.isFile()) {
            final String path = pf.getPath();
            file = new File(path + child); // just concat paths
        } else {
            file = new File(pf, child);
        }

        if (file.exists()) {
            final DefaultNode node = new DefaultNode(child);
            if (file.isFile())
                node.setHandle(new FileContentHandle(node, file));
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
                if (file.isFile())
                    node.setHandle(new FileContentHandle(node, file));
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
        if ((files == null || files.length == 0) && file.delete()) {
            cache.remove(node); // remove from cache, since probably not used anymore
            delete(file.getParentFile(), NodeUtils.firstParent(node));
        }
    }
    
    private class FileContentHandle implements ContentHandle {

        private Node owner;
        private File file;

        private FileContentHandle(Node owner, File file) {
            this.owner = owner;
            this.file = file;
        }

        public InputStream getContent() throws IOException {
            return new FileInputStream(file);
        }

        @Override
        public void clean() {
            delete(file, owner);
        }
    }
}
