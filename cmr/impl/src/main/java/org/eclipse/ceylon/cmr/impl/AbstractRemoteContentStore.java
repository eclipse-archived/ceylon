/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.impl;

import java.io.File;
import java.util.Collections;

import org.eclipse.ceylon.cmr.api.ContentFinderDelegate;
import org.eclipse.ceylon.cmr.api.ModuleQuery;
import org.eclipse.ceylon.cmr.api.ModuleSearchResult;
import org.eclipse.ceylon.cmr.api.ModuleVersionQuery;
import org.eclipse.ceylon.cmr.api.ModuleVersionResult;
import org.eclipse.ceylon.cmr.api.Overrides;
import org.eclipse.ceylon.cmr.spi.ContentStore;
import org.eclipse.ceylon.cmr.spi.OpenNode;
import org.eclipse.ceylon.cmr.spi.StructureBuilder;
import org.eclipse.ceylon.common.log.Logger;

/**
 * Abstract remote content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractRemoteContentStore extends AbstractContentStore implements ContentFinderDelegate {

    protected AbstractRemoteContentStore(Logger log, boolean offline, int timeout) {
        super(log, offline, timeout);
    }

    public OpenNode createRoot() {
        final RemoteNode node = new RemoteRootNode();
        node.addService(ContentStore.class, this);
        node.addService(StructureBuilder.class, this);
        node.addService(ContentFinderDelegate.class, this);
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
    public void completeModules(ModuleQuery lookup, ModuleSearchResult result, Overrides overrides) {
        // remote content stores do not participate in completion for speed reasons
    }

    @Override
    public void completeVersions(ModuleVersionQuery lookup, ModuleVersionResult result, Overrides overrides) {
        // remote content stores do not participate in completion for speed reasons
    }

    @Override
    public void searchModules(ModuleQuery query, ModuleSearchResult result, Overrides overrides) {
        // remote content stores do not participate in search for speed reasons
    }
    
    @SuppressWarnings("serial")
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

    @SuppressWarnings("serial")
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
    

    @Override
    public Iterable<File> getBaseDirectories() {
        return Collections.emptyList();
    }
}
