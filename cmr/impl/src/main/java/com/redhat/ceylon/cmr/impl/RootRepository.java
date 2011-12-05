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

import java.io.File;
import java.io.IOException;

/**
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
        setRoot(new RootNode(fileContentStore));
    }

    public File getArtifact(ArtifactContext context) throws IOException {
        Node node = getLeafNode(context);
        return fileContentStore.getFile(node);
    }
}
