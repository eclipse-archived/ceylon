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

import com.redhat.ceylon.cmr.api.AbstractRepository;
import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.Repository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Output repository.
 * Treat compiler output differently from runtime repos.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class OutputRepository extends AbstractRepository {

    private final Repository output;
    private final Repository repository; // default root repository

    private static File getOutputDir() {
        return new File("output"); // TODO
    }

    public OutputRepository(Repository repository, Logger log) {
        this(getOutputDir(), repository, log);
    }

    public OutputRepository(File outputDir, Repository repository, Logger log) {
        this(new RootRepository(outputDir, log), repository, log);
    }

    public OutputRepository(Repository output, Repository repository, Logger log) {
        super(log);
        if (output == null)
            throw new IllegalArgumentException("Output is null!");
        if (repository == null)
            throw new IllegalArgumentException("Repository is null!");

        this.output = output;
        this.repository = repository;
    }

    public File getArtifact(ArtifactContext context) throws IOException {
        final File artifact = output.getArtifact(context);
        if (artifact != null) {
            return artifact;
        } else {
            return repository.getArtifact(context);
        }
    }

    public void putArtifact(ArtifactContext context, InputStream content) throws IOException {
        output.putArtifact(context, content);
    }

    public void removeArtifact(ArtifactContext context) throws IOException {
        output.removeArtifact(context);
    }

    @Override
    public String toString() {
        return "OutputRepository: " + output;
    }
}
