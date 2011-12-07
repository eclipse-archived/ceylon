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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Root node -- main entry point into Ceylon repositories.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RootRepository extends AbstractNodeRepository {

    private final FileContentStore fileContentStore;

    private static File getRootDir() {
        String repo = System.getProperty("ceylon.repo");
        if (repo == null)
            repo = System.getProperty("user.home") + File.separator + ".ceylon" + File.separator + "repo" + File.separator;
        return new File(repo);
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
                log.fine("Creating local copy of external node: " + node);
                fileContentStore.putContent(node, node.getInputStream());
                file = fileContentStore.getFile(node); // re-get
                if (context.isIgnoreSHA() == false && node instanceof OpenNode) {
                    OpenNode on = (OpenNode) node;
                    String sha1 = IOUtils.sha1(new FileInputStream(file));
                    ByteArrayInputStream shaStream = new ByteArrayInputStream(sha1.getBytes("ASCII"));
                    Node sha = node.getChild(SHA);
                    if (sha == null) {
                        // put it to ext node as well, if supported
                        on.addContent(SHA, shaStream);
                    }
                    // create empty marker node
                    OpenNode sl = on.addNode(SHA + LOCAL);
                    // put sha to local store as well
                    fileContentStore.putContent(sl, shaStream);
                }
            }
            return file;
        } else {
            return null;
        }
    }

    @Override
    protected Boolean checkSHA(Node artifact) throws IOException {
        Boolean result = super.checkSHA(artifact);
        if (result == null) {
            Node sha = artifact.getChild(SHA + LOCAL);
            if (sha != null) {
                File shaFile = fileContentStore.getFile(sha);
                if (shaFile.exists())
                    return checkSHA(artifact, IOUtils.toInputStream(shaFile));
            }
        }
        return result;
    }
}
