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
import com.redhat.ceylon.cmr.impl.AbstractArtifactResult;
import com.redhat.ceylon.cmr.impl.MavenRepository;
import com.redhat.ceylon.cmr.spi.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Aether repository.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class AetherRepository extends MavenRepository {
    private final AetherUtils utils;

    private AetherRepository(AetherContentStore acs) {
        super(acs.createRoot());
        utils = acs.getUtils();
    }

    public static Repository createRepository(Logger log) {
        AetherContentStore acs = new AetherContentStore(log);
        return new AetherRepository(acs);
    }
    
    public static Repository createRepository(Logger log, String settingsXml) {
        AetherContentStore acs = new AetherContentStore(log);
        AetherRepository repo = new AetherRepository(acs);
        repo.utils.overrideSettingsXml(settingsXml);
        return repo;
    }
    
    @Override
    public Node findParent(ArtifactContext context) {
        if (context.getName().startsWith("ceylon.")) {
            return null;
        }
        return super.findParent(context);
    }
    
    public ArtifactResult getArtifactResultInternal(RepositoryManager manager, Node node) {
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

        return new AetherArtifactResult(context.getName(), context.getVersion(), artifact, dependecies);
    }

    private static class AetherArtifactResult extends AbstractArtifactResult {
        private File file;
        private List<ArtifactResult> dependencies;

        private AetherArtifactResult(String name, String version, File file, List<ArtifactResult> dependencies) {
            super(name, version);
            this.file = file;
            this.dependencies = dependencies;
        }

        public ArtifactResultType type() {
            return ArtifactResultType.MAVEN;
        }

        public File artifact() throws RepositoryException {
            return file;
        }

        public List<ArtifactResult> dependencies() throws RepositoryException {
            return Collections.unmodifiableList(dependencies);
        }
    }

    private static class SingleArtifactResult extends AbstractArtifactResult {
        private File file;

        private SingleArtifactResult(File file) {
            super(parseName(file), parseVersion(file));
            this.file = file;
        }

        public ArtifactResultType type() {
            return ArtifactResultType.MAVEN;
        }

        public File artifact() throws RepositoryException {
            return file;
        }

        public List<ArtifactResult> dependencies() throws RepositoryException {
            return Collections.emptyList();
        }
    }

    // temp massive hack !!

    private static int split(String name) {
        int p = 0;
        while (true) {
            p = name.indexOf("-", p);
            if (p < 0)
                throw new IllegalArgumentException("Cannot find name-version split: " + name);
            if (Character.isDigit(name.charAt(p + 1)))
                return p;
            p++;
        }
    }

    private static String parseName(File file) {
        String name = file.getName();
        return name.substring(0, split(name));
    }

    private static String parseVersion(File file) {
        String name = file.getName();
        return name.substring(split(name) + 1, name.length() - ".jar".length());
    }
}
