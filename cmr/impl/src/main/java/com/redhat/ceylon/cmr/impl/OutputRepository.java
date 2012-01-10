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
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.spi.Node;

import java.io.File;
import java.io.IOException;

/**
 * Output repository.
 * Treat compiler output differently from runtime repos.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class OutputRepository extends AbstractNodeRepository {

    private final FileContentStore fileContentStore;
    private final Repository repository; // default root repository

    private static File getOutputDir() {
        return new File("output"); // TODO
    }

    public OutputRepository(Repository repository) {
        this(getOutputDir(), repository);
    }

    public OutputRepository(File outputDir, Repository repository) {
        if (outputDir == null)
            throw new IllegalArgumentException("Null output dir!");
        if (outputDir.exists() == false)
            throw new IllegalArgumentException("Output dir doesn't exist: " + outputDir);
        if (outputDir.isDirectory() == false)
            throw new IllegalArgumentException("Output dir is not a directory: " + outputDir);
        if (repository == null)
            throw new IllegalArgumentException("Repository is null!");

        fileContentStore = new FileContentStore(outputDir);
        setRoot(new RootNode(fileContentStore, fileContentStore));
        this.repository = repository;
    }

    public File getArtifact(ArtifactContext context) throws IOException {
        Node node = getLeafNode(context);
        if (node != null) {
            return node.getContent(File.class);
        } else {
            return repository.getArtifact(context);
        }
    }

    @Override
    public String toString() {
        return "OutputRepository: " + fileContentStore;
    }
}
