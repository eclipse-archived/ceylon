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

import com.redhat.ceylon.cmr.api.AbstractRepository;
import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractNodeRepository extends AbstractRepository {

    protected Node root;

    protected Node getRoot() {
        if (root == null)
            throw new IllegalArgumentException("Missing root!");

        return root;
    }

    protected void setRoot(Node root) {
        if (root == null)
            throw new IllegalArgumentException("Null root");
        if (this.root != null)
            throw new IllegalArgumentException("Root already set!");

        this.root = root;
    }

    public void putArtifact(ArtifactContext context, InputStream content) throws IOException {
        OpenNode node = getParentNode(context);
        String label = getLabel(context);
        node.addContent(label, content);
    }

    protected Node getLeafNode(ArtifactContext context) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    protected OpenNode getParentNode(ArtifactContext context) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    protected String getLabel(ArtifactContext context) {
        return null;
    }
}
