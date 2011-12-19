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

package com.redhat.ceylon.test.smoke.support;

import com.redhat.ceylon.cmr.impl.DefaultNode;
import com.redhat.ceylon.cmr.impl.IOUtils;
import com.redhat.ceylon.cmr.impl.RootNode;
import com.redhat.ceylon.cmr.spi.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class InMemoryContentStore implements ContentStore, StructureBuilder {
    
    private static final byte[] MARKER = new byte[0];
    private Map<Node, byte[]> store = new HashMap<Node, byte[]>();
    
    @Override
    public ContentHandle popContent(Node node) {
        byte[] result = store.get(node);
        if (result == null || result == MARKER)
            return null;
        
        return new InMemoryContentHandle(result);
    }

    @Override
    public ContentHandle getContent(Node node) throws IOException {
        return popContent(node);
    }

    @Override
    public ContentHandle putContent(Node node, InputStream stream) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copyStream(stream, baos);
        final byte[] bytes = baos.toByteArray();
        store.put(node, bytes);
        return new InMemoryContentHandle(bytes);
    }

    @Override
    public OpenNode createRoot() {
        return new RootNode(this, this);
    }

    @Override
    public OpenNode find(Node parent, String child) {
        // automagically build the tree
        DefaultNode node = new DefaultNode(child);
        store.put(node, MARKER);
        return node;
    }

    @Override
    public Iterable<? extends OpenNode> find(Node parent) {
        return Collections.emptyList();
    }
    
    private static class InMemoryContentHandle implements ContentHandle {
        
        private byte[] bytes;

        private InMemoryContentHandle(byte[] bytes) {
            this.bytes = bytes;
        }

        @Override
        public InputStream getContent() throws IOException {
            return new ByteArrayInputStream(bytes);
        }

        @Override
        public void clean() {
        }
    }
}
