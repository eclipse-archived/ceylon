/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.impl;

import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.spi.ContentStore;
import org.eclipse.ceylon.cmr.spi.Node;
import org.eclipse.ceylon.cmr.spi.StructureBuilder;
import org.eclipse.ceylon.common.log.Logger;
import org.eclipse.ceylon.model.cmr.RepositoryException;

/**
 * Abstract content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractContentStore implements ContentStore, StructureBuilder {

    protected static final String SEPARATOR = "/";

    protected int timeout;
    protected boolean offline;
    protected Logger log;

    protected AbstractContentStore(Logger log, boolean offline, int timeout) {
        this.log = log;
        this.timeout = timeout;
        this.offline = offline;
    }

    protected static String getFullPath(Node parent, String child) {
        final StringBuilder sb = new StringBuilder(NodeUtils.getFullPath(parent, SEPARATOR));
        if (parent.hasBinaries() == false)
            sb.append(SEPARATOR);
        sb.append(child);
        return sb.toString();
    }

    protected boolean hasContent(String child) {
        try {
            ArtifactContext.getSuffixFromFilename(child);
            return true;
        } catch (RepositoryException ex) {
            return false;
        }
    }
    
    @Override
    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public boolean isOffline() {
        return offline;
    }
    
    public void setOffline(boolean offline) {
        this.offline = offline;
    }
}
