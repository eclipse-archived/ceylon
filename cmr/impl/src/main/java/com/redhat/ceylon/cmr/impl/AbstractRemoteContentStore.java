/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
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

import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.spi.ContentStore;
import com.redhat.ceylon.cmr.spi.OpenNode;
import com.redhat.ceylon.cmr.spi.StructureBuilder;

/**
 * Abstract remote content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractRemoteContentStore extends AbstractContentStore {

    protected AbstractRemoteContentStore(Logger log) {
        super(log);
    }

    public void addSuffix(String suffix) {
        suffixes.add(suffix);
    }

    public OpenNode createRoot() {
        final RemoteNode node = new RemoteRootNode();
        node.addService(ContentStore.class, this);
        node.addService(StructureBuilder.class, this);
        node.setHandle(DefaultNode.HANDLE_MARKER);
        return node;
    }

    protected RemoteNode createNode(String label) {
        return new RemoteNode(label);
    }

    protected static class RemoteNode extends DefaultNode {
        public RemoteNode(String label) {
            super(label);
        }

        public boolean isRemote() {
            return true;
        }
    }

    protected static class RemoteRootNode extends RemoteNode {
        public RemoteRootNode() {
            super("");
        }

        @Override
        public boolean equals(Object obj) {
            return (obj == this);
        }
    }
}
