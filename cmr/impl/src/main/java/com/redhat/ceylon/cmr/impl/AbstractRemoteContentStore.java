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

import com.redhat.ceylon.cmr.api.ContentFinder;
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleSearchResult;
import com.redhat.ceylon.cmr.api.ModuleVersionQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionResult;
import com.redhat.ceylon.cmr.spi.ContentStore;
import com.redhat.ceylon.cmr.spi.OpenNode;
import com.redhat.ceylon.cmr.spi.StructureBuilder;

/**
 * Abstract remote content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractRemoteContentStore extends AbstractContentStore implements ContentFinder {

    protected AbstractRemoteContentStore(Logger log, boolean offline) {
        super(log, offline);
    }

    public void addSuffix(String suffix) {
        suffixes.add(suffix);
    }

    public OpenNode createRoot() {
        final RemoteNode node = new RemoteRootNode();
        node.addService(ContentStore.class, this);
        node.addService(StructureBuilder.class, this);
        node.addService(ContentFinder.class, this);
        node.setHandle(DefaultNode.HANDLE_MARKER);
        return node;
    }

    protected RemoteNode createNode(String label) {
        return new RemoteNode(label);
    }


    @Override
    public boolean isSearchable() {
        // remote content stores do not participate in completion for speed reasons
        return false;
    }

    @Override
    public void completeModules(ModuleQuery lookup, ModuleSearchResult result) {
        // remote content stores do not participate in completion for speed reasons
    }

    @Override
    public void completeVersions(ModuleVersionQuery lookup, ModuleVersionResult result) {
        // remote content stores do not participate in completion for speed reasons
    }

    @Override
    public void searchModules(ModuleQuery query, ModuleSearchResult result) {
        // remote content stores do not participate in search for speed reasons
    }
    
    protected static class RemoteNode extends DefaultNode {
        private String cachedString;

        public RemoteNode(String label) {
            super(label);
        }

        public boolean isRemote() {
            return true;
        }

        @Override
        public String toString() {
            if (cachedString == null) {
                StringBuilder builder = new StringBuilder("RemoteNode for ");
                builder.append(findService(ContentStore.class));
                builder.append(" -> [").append(NodeUtils.getFullPath(this)).append("]");
                cachedString = builder.toString();
            }
            return cachedString;
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

        @Override
        public String getDisplayString() {
            return getService(ContentStore.class).getDisplayString();
        }
    }
}
