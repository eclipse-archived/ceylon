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
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.StructureBuilder;

import java.io.File;
import java.io.IOException;

/**
 * Simple repository.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class SimpleRepository extends AbstractNodeRepository {

    public SimpleRepository(StructureBuilder structureBuilder, Logger log) {
        super(log);
        if (structureBuilder == null)
            throw new IllegalArgumentException("Null structure builder!");

        setRoot(new DefaultArtifactContextAdapter(structureBuilder.createRoot()));
    }

    public SimpleRepository(ArtifactContextAdapter root, Logger log) {
        super(log);
        if (root == null)
            throw new IllegalArgumentException("Null root!");

        setRoot(root);
    }

    public File getArtifact(ArtifactContext context) throws IOException {
        Node node = getLeafNode(context);
        return (node != null) ? node.getContent(File.class) : null;
    }

    @Override
    public String toString() {
        return "SimpleRepository: " + getRoot();
    }
}
