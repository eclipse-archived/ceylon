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

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.RepositoryException;
import com.redhat.ceylon.cmr.spi.ContentStore;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.StructureBuilder;
import com.redhat.ceylon.common.log.Logger;

/**
 * Abstract content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractContentStore implements ContentStore, StructureBuilder {

    protected static final String SEPARATOR = "/";

    protected boolean offline;
    protected Logger log;

    protected AbstractContentStore(Logger log, boolean offline) {
        this.log = log;
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
    public boolean isOffline() {
        return offline;
    }
    
    public void setOffline(boolean offline) {
        this.offline = offline;
    }
}
