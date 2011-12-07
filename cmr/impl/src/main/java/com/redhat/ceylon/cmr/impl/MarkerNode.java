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

import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Marker node.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class MarkerNode extends AbstractOpenNode {

    private static final long serialVersionUID = 1L;

    public MarkerNode() {
        // serialization only
    }

    public MarkerNode(String label, Object value) {
        super(label, value);
    }

    @Override
    public void merge(OpenNode other) {
    }

    @Override
    public OpenNode addNode(String label, Object value) {
        return null; // cannot add to marker node
    }

    @Override
    public OpenNode addContent(String label, InputStream content) throws IOException {
        return null; // cannot add to marker node
    }

    @Override
    public <T extends Serializable> OpenNode addContent(String label, T content) throws IOException {
        return null; // cannot add to marker node
    }

    @Override
    public Node removeNode(String label) {
        return null; // cannot remove from marker node
    }

    @Override
    public boolean hasContent() {
        return false;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }
}
