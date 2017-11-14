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
import org.eclipse.ceylon.cmr.api.CmrRepository;
import org.eclipse.ceylon.cmr.api.Overrides;
import org.eclipse.ceylon.cmr.spi.Node;
import org.eclipse.ceylon.cmr.spi.StructureBuilder;
import org.eclipse.ceylon.common.log.Logger;
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.cmr.RepositoryException;

/**
 * Simple repository.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class SimpleRepositoryManager extends AbstractNodeRepositoryManager {

    public SimpleRepositoryManager(StructureBuilder structureBuilder, Logger log) {
        super(log, null);
        if (structureBuilder == null)
            throw new IllegalArgumentException("Null structure builder!");

        setAddCacheAsRoot(true);
        setCache(new DefaultRepository(structureBuilder.createRoot()));
    }

    public SimpleRepositoryManager(CmrRepository root, Logger log) {
        this(root, log, null);
    }
    
    public SimpleRepositoryManager(CmrRepository root, Logger log, Overrides overrides) {
        super(log, overrides);
        if (root == null)
            throw new IllegalArgumentException("Null root!");

        setAddCacheAsRoot(true);
        setCache(root);
    }

    protected ArtifactResult getArtifactResult(ArtifactContext context, Node node) throws RepositoryException {
        return cache.getArtifactResult(this, node);
    }

    @Override
    public String toString() {
        return "SimpleRepositoryManager: " + getCache();
    }
}
