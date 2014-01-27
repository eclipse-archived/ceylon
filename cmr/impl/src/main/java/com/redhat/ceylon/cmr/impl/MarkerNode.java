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

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import com.redhat.ceylon.cmr.spi.ContentOptions;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;

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
        throw new UnsupportedOperationException("Marker node doesn't support merge: " + toString());
    }

    @Override
    public OpenNode addNode(String label, Object value) {
        throw new UnsupportedOperationException("Marker node doesn't add node: " + toString());
    }

    @Override
    public OpenNode createNode(String label) {
        throw new UnsupportedOperationException("Marker node cannot create node: " + toString());
    }

    @Override
    public OpenNode addContent(String label, InputStream content, ContentOptions options) throws IOException {
        throw new UnsupportedOperationException("Marker node doesn't add content: " + toString());
    }

    @Override
    public <T extends Serializable> OpenNode addContent(String label, T content, ContentOptions options) throws IOException {
        throw new UnsupportedOperationException("Marker node doesn't add content: " + toString());
    }

    @Override
    public Node removeNode(String label) {
        throw new UnsupportedOperationException("Marker node doesn't remove node: " + toString());
    }

    @Override
    public boolean hasBinaries() {
        return false;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public long getLastModified() {
        return -1L;
    }

    @Override
    public long getSize() {
        return -1;
    }

    @Override
    public boolean isRemote() {
        return false;
    }

    @Override
    public String getDisplayString() {
        return "Marker node";
    }
}
