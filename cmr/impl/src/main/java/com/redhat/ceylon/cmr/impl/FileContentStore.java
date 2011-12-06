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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * File content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class FileContentStore implements ContentStore {

    private File root;

    public FileContentStore(File root) {
        if (root == null)
            throw new IllegalArgumentException("Null root!");
        this.root = root;
    }

    File getFile(Node node) {
        String path = getFullPath(node);
        return new File(root, path);
    }

    protected String getFullPath(Node node) {
        StringBuilder path = new StringBuilder();
        buildFullPath(node, path, false);
        return path.toString();
    }

    protected static void buildFullPath(Node node, StringBuilder path, boolean appendSeparator) {
        Iterable<? extends Node> parents = node.getParents();
        //noinspection LoopStatementThatDoesntLoop
        for (Node parent : parents) {
            buildFullPath(parent, path, true);
            break; // just use the first one
        }
        path.append(node.getLabel());
        if (appendSeparator && node.hasContent() == false)
            path.append(File.separator);
    }

    public ContentHandle popContent(Node node) {
        File file = getFile(node);
        return (file.exists()) ? new FileContentHandle(file) : null;
    }

    public ContentHandle getContent(Node node) throws IOException {
        File file = getFile(node);
        if (file.exists() == false)
            throw new IOException("Content doesn't exist: " + file);

        return new FileContentHandle(file);
    }

    public ContentHandle putContent(Node node, InputStream stream) throws IOException {
        if (stream == null)
            throw new IllegalArgumentException("Null stream!");

        File file = getFile(node);
        if (file.exists())
            throw new IOException("Content already exists: " + file);

        IOUtils.writeToFile(file, stream);
        return new FileContentHandle(file);
    }

    private static class FileContentHandle implements ContentHandle {

        private File file;

        private FileContentHandle(File file) {
            this.file = file;
        }

        public InputStream getContent() throws IOException {
            return new FileInputStream(file);
        }

        @Override
        public void clean() {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
    }
}
