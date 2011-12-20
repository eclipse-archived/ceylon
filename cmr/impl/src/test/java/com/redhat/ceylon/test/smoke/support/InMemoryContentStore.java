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

package com.redhat.ceylon.test.smoke.support;

import com.redhat.ceylon.cmr.impl.DefaultNode;
import com.redhat.ceylon.cmr.impl.IOUtils;
import com.redhat.ceylon.cmr.impl.RootNode;
import com.redhat.ceylon.cmr.spi.*;

import java.io.*;
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
        public InputStream getContentAsStream() throws IOException {
            return new ByteArrayInputStream(bytes);
        }

        @Override
        public File getContentAsFile() throws IOException {
            final File temp = File.createTempFile("in-memory-", ".car");
            IOUtils.copyStream(getContentAsStream(), new FileOutputStream(temp));
            temp.deleteOnExit();
            return temp;
        }

        @Override
        public void clean() {
        }
    }
}
