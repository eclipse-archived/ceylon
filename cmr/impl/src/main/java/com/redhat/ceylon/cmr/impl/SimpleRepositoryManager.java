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

import com.redhat.ceylon.cmr.api.*;
import com.redhat.ceylon.cmr.spi.StructureBuilder;

/**
 * Simple repository.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class SimpleRepositoryManager extends AbstractNodeRepositoryManager {

    public SimpleRepositoryManager(StructureBuilder structureBuilder, Logger log) {
        super(log);
        if (structureBuilder == null)
            throw new IllegalArgumentException("Null structure builder!");

        setRoot(new DefaultRepository(structureBuilder.createRoot()));
    }

    public SimpleRepositoryManager(Repository root, Logger log) {
        super(log);
        if (root == null)
            throw new IllegalArgumentException("Null root!");

        setRoot(root);
    }

    public ArtifactResult getArtifactResult(ArtifactContext context) throws RepositoryException {
        return root.getArtifactResult(this, getLeafNode(context));
    }

    @Override
    public String toString() {
        return "SimpleRepositoryManager: " + getRoot();
    }
}
