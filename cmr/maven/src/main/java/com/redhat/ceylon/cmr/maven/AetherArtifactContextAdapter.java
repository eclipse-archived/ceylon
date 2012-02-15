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

package com.redhat.ceylon.cmr.maven;

import com.redhat.ceylon.cmr.api.*;
import com.redhat.ceylon.cmr.impl.MavenArtifactContextAdapter;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Aether adapter.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class AetherArtifactContextAdapter extends MavenArtifactContextAdapter {
    private final AetherUtils utils;

    public AetherArtifactContextAdapter(OpenNode root, Logger log) {
        super(root);
        utils = new AetherUtils(log);
    }

    public ArtifactResult getArtifactResult(Repository repository, Node node) {
        final File[] files = utils.findDependencies(node);
        if (files == null || files.length == 0)
            return null;

        if (files.length == 1)
            return new SingleArtifactResult(files[0]);

        final ArtifactContext context = ArtifactContext.fromNode(node);
        final String name = getArtifactName(context);

        File artifact = null;
        final List<ArtifactResult> dependecies = new ArrayList<ArtifactResult>();
        for (File file : files) {
            if (name.equalsIgnoreCase(file.getName()))
                artifact = file;
            else
                dependecies.add(new SingleArtifactResult(file));
        }

        if (artifact == null)
            throw new IllegalArgumentException("No matching artifact, should not happen: " + name);

        return new AetherArtifactResult(artifact, dependecies);
    }

    private static class AetherArtifactResult implements ArtifactResult {
        private File file;
        private List<ArtifactResult> dependencies;

        private AetherArtifactResult(File file, List<ArtifactResult> dependencies) {
            this.file = file;
            this.dependencies = dependencies;
        }

        public ArtifactResultType type() {
            return ArtifactResultType.MAVEN;
        }

        public File artifact() throws IOException {
            return file;
        }

        public List<ArtifactResult> dependecies() throws IOException {
            return Collections.unmodifiableList(dependencies);
        }
    }

    private static class SingleArtifactResult implements ArtifactResult {
        private File file;

        private SingleArtifactResult(File file) {
            this.file = file;
        }

        public ArtifactResultType type() {
            return ArtifactResultType.MAVEN;
        }

        public File artifact() throws IOException {
            return file;
        }

        public List<ArtifactResult> dependecies() throws IOException {
            return Collections.emptyList();
        }
    }

}
