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

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;

import java.io.*;

/**
 * Root node -- main entry point into Ceylon repositories.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RootRepository extends AbstractNodeRepository {

    private final FileContentStore fileContentStore;

    private static File getRootDir() {
        String repo = SecurityActions.getProperty("ceylon.repo");
        if (repo == null)
            repo = SecurityActions.getProperty("user.home") + File.separator + ".ceylon" + File.separator + "repo" + File.separator;
        final File root = new File(repo);
        if (root.exists() == false && root.mkdirs() == false)
            throw new IllegalArgumentException("Cannot create Ceylon repo root directory: " + root);
        return root;
    }

    public RootRepository() {
        this(getRootDir());
    }

    public RootRepository(File rootDir) {
        fileContentStore = new FileContentStore(rootDir);
        setRoot(new RootNode(fileContentStore, fileContentStore));
    }

    public File getArtifact(ArtifactContext context) throws IOException {
        Node node = getLeafNode(context);
        if (node != null) {
            File file = fileContentStore.getFile(node);
            if (file.exists() == false) {
                file = putContent(context, node, node.getInputStream());
            }
            return file;
        } else {
            return null;
        }
    }

    protected File putContent(ArtifactContext context, Node node, InputStream stream) throws IOException {
        log.fine("Creating local copy of external node: " + node);
        fileContentStore.putContent(node, stream);
        File file = fileContentStore.getFile(node); // re-get
        if (context.isIgnoreSHA() == false && node instanceof OpenNode) {
            OpenNode on = (OpenNode) node;
            String sha1 = IOUtils.sha1(new FileInputStream(file));
            if (sha1 != null) {
                ByteArrayInputStream shaStream = new ByteArrayInputStream(sha1.getBytes("ASCII"));
                Node sha = node.getChild(SHA1);
                if (sha == null) {
                    // put it to ext node as well, if supported
                    on.addContent(SHA1, shaStream);
                    shaStream.reset(); // reset, for next read
                }
                // create empty marker node
                OpenNode sl = on.addNode(SHA1 + LOCAL);
                // put sha to local store as well
                fileContentStore.putContent(sl, shaStream);
            }
        }
        return file;
    }

    @Override
    protected void addContent(ArtifactContext context, Node parent, String label, InputStream content) throws IOException {
        Node child = parent.getChild(label);
        if (child == null && parent instanceof OpenNode) {
            OpenNode on = (OpenNode) parent;
            child = on.addNode(label);
        }
        if (child != null) {
            putContent(context, child, content);
        } else {
            throw new IOException("Cannot add child [" + label + "] content [" + content + "] on parent node: " + parent);
        }
    }

    @Override
    protected void removeNode(Node parent, String child) throws IOException {
        final Node node = parent.getChild(child);
        
        try {
            if (node != null) {
                Node sl = node.getChild(SHA1 + LOCAL);
                if (sl != null)
                    fileContentStore.removeFile(sl);
            }
        } catch (Exception ignored) {            
        }
        
        try {                        
            super.removeNode(parent, child);
        } finally {
            if (node != null) {
                fileContentStore.removeFile(node);
            }                
        }
    }

    @Override
    protected Boolean checkSHA(Node artifact) throws IOException {
        Boolean result = super.checkSHA(artifact);
        if (result == null) {
            Node sha = artifact.getChild(SHA1 + LOCAL);
            if (sha != null) {
                File shaFile = fileContentStore.getFile(sha);
                if (shaFile.exists())
                    return checkSHA(artifact, IOUtils.toInputStream(shaFile));
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "RootRepository: " + fileContentStore;
    }
}
