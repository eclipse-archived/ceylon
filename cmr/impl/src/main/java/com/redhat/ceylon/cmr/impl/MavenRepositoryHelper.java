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
import com.redhat.ceylon.cmr.spi.OpenNode;

import java.io.File;

/**
 * Maven repository helper.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class MavenRepositoryHelper {

    static File getMavenHome() {
        File mvnHome = new File(System.getenv("MAVEN_HOME"), "repository");
        if (mvnHome.exists())
            return mvnHome;

        mvnHome = new File(System.getProperty("user.home"), ".m2/repository");
        if (mvnHome.exists())
            return mvnHome;

        final String property = System.getProperty("maven.home");
        if (property != null) {
            mvnHome = new File(property, "repository");
            if (mvnHome.exists())
                return mvnHome;
        }

        throw new IllegalArgumentException("No Maven home specified!");
    }

    public static ArtifactContextAdapter getMavenArtifactContextAdapter() {
        return new MavenArtifactContextAdapter(new MavenContentStore().createRoot());
    }

    public static ArtifactContextAdapter getMavenArtifactContextAdapter(File mvnRepository) {
        return new MavenArtifactContextAdapter(new MavenContentStore(mvnRepository).createRoot());
    }

    public static ArtifactContextAdapter getMavenArtifactContextAdapter(String repositoryURL, Logger log) {
        return new MavenArtifactContextAdapter(new RemoteContentStore(repositoryURL, log).createRoot());
    }

    private static class MavenArtifactContextAdapter extends AbstractArtifactContextAdapter {
        private MavenArtifactContextAdapter(OpenNode root) {
            super(root);
        }

        public String getArtifactName(ArtifactContext context) {
            String name = context.getName();
            final int p = name.lastIndexOf(".");
            return getArtifactName(p >= 0 ? name.substring(p + 1) : name, context.getVersion(), ArtifactContext.JAR);
        }
    }

    private static class MavenContentStore extends FileContentStore {
        private MavenContentStore() {
            this(getMavenHome());
        }

        private MavenContentStore(File root) {
            super(root);
        }

        @Override
        protected void delete(File file, Node node) {
            // cannot delete from Maven repo
        }
    }
}
