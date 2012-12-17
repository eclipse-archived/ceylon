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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.ArtifactResultType;
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.RepositoryException;
import com.redhat.ceylon.cmr.impl.AbstractArtifactResult;
import com.redhat.ceylon.cmr.spi.Node;
import org.jboss.shrinkwrap.resolver.api.ResolutionException;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenArtifactInfo;
import org.jboss.shrinkwrap.resolver.api.maven.MavenFormatStage;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolvedArtifact;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.jboss.shrinkwrap.resolver.api.maven.MavenStrategyStage;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenCoordinate;

/**
 * Aether utils.
 * <p/>
 * We actually use JBoss ShrinkWrap Resolver.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class AetherUtils {
    private static final ScopeType[] SCOPES = new ScopeType[]{ScopeType.COMPILE, ScopeType.PROVIDED, ScopeType.RUNTIME};
    private static final SingleScopedStrategy SCOPED_STRATEGY = new SingleScopedStrategy(SCOPES);

    private Logger log;
    private String settingsXml;

    AetherUtils(Logger log) {
        this.log = log;
        settingsXml = getDefaultMavenSettings();
    }

    void overrideSettingsXml(String settingsXml) {
        this.settingsXml = settingsXml;
    }

    File findDependency(Node node) {
        final ArtifactResult result = findDependencies(node, true);
        return (result != null) ? result.artifact() : null;
    }

    ArtifactResult findDependencies(Node node) {
        return findDependencies(node, null);
    }

    private ArtifactResult findDependencies(Node node, Boolean fetchSingleArtifact) {
        final ArtifactContext ac = ArtifactContext.fromNode(node);
        if (ac == null)
            return null;

        final String name = ac.getName();
        final int p = name.contains(":") ? name.lastIndexOf(":") : name.lastIndexOf(".");
        if (p == -1) {
            return null;
        }
        final String groupId = name.substring(0, p);
        final String artifactId = name.substring(p + 1);
        final String version = ac.getVersion();

        return fetchDependencies(groupId, artifactId, version, fetchSingleArtifact != null ? fetchSingleArtifact : ac.isFetchSingleArtifact());
    }

    private ArtifactResult fetchDependencies(String groupId, String artifactId, String version, boolean fetchSingleArtifact) {
        final String name = toCanonicalForm(groupId, artifactId);
        final String coordinates = toCanonicalForm(name, version);
        try {
            final MavenStrategyStage mss = getResolver().resolve(coordinates);
            final MavenFormatStage mfs = mss.using(SCOPED_STRATEGY);
            final MavenResolvedArtifact info = mfs.asSingleResolvedArtifact();
            if (info == null) {
                log.debug("No artifact found: " + coordinates);
                return null;
            }

            if (fetchSingleArtifact) {
                return new SingleArtifactResult(name, version, info.asFile());
            } else {
                final MavenArtifactInfo[] infos = info.getDependencies();
                final List<ArtifactResult> dependencies = new ArrayList<ArtifactResult>(infos.length);
                for (MavenArtifactInfo dep : infos) {
                    final MavenCoordinate dCo = dep.getCoordinate();
                    final String dName = toCanonicalForm(dCo.getGroupId(), dCo.getArtifactId());
                    final String dVersion = dCo.getVersion();

                    ArtifactResult dr = new MavenArtifactResult(dName, dVersion) {
                        private ArtifactResult result;

                        private synchronized ArtifactResult getResult() {
                            if (result == null) {
                                result = fetchDependencies(dCo.getGroupId(), dCo.getArtifactId(), dVersion, false);
                            }
                            return result;
                        }

                        public File artifact() throws RepositoryException {
                            return getResult().artifact();
                        }

                        public List<ArtifactResult> dependencies() throws RepositoryException {
                            return getResult().dependencies();
                        }
                    };
                    dependencies.add(dr);
                }
                return new AetherArtifactResult(name, version, info.asFile(), dependencies);
            }
        } catch (ResolutionException e) {
            log.debug("Could not resolve artifact [" + coordinates + "] : " + e);
            return null;
        }
    }

    private static String toCanonicalForm(String groupId, String artifactId) {
        return groupId + ":" + artifactId;
    }

    public static String getDefaultMavenSettings() {
        String path = System.getProperty("maven.repo.local");
        if (path != null) {
            File file = new File(path, "settings.xml");
            if (file.exists())
                return file.getAbsolutePath();
        }

        path = System.getProperty("user.home");
        if (path != null) {
            File file = new File(path, ".m2/settings.xml");
            if (file.exists())
                return file.getAbsolutePath();
        }

        path = System.getenv("M2_HOME");
        if (path != null) {
            File file = new File(path, "conf/settings.xml");
            if (file.exists())
                return file.getAbsolutePath();
        }

        return "classpath:settings.xml";
    }

    private MavenResolverSystem getResolver() {
        if (settingsXml.startsWith("classpath:"))
            return Maven.configureResolver().fromClassloaderResource(settingsXml.substring(10));
        return Maven.configureResolver().fromFile(settingsXml);
    }

    private static abstract class MavenArtifactResult extends AbstractArtifactResult {
        protected MavenArtifactResult(String name, String version) {
            super(name, version);
        }

        public ArtifactResultType type() {
            return ArtifactResultType.MAVEN;
        }
    }

    private static class SingleArtifactResult extends MavenArtifactResult {
        private File file;

        private SingleArtifactResult(String name, String version, File file) {
            super(name, version);
            this.file = file;
        }

        public File artifact() throws RepositoryException {
            return file;
        }

        public List<ArtifactResult> dependencies() throws RepositoryException {
            return Collections.emptyList();
        }
    }

    private static class AetherArtifactResult extends SingleArtifactResult {
        private List<ArtifactResult> dependencies;

        private AetherArtifactResult(String name, String version, File file, List<ArtifactResult> dependencies) {
            super(name, version, file);
            this.dependencies = dependencies;
        }

        public List<ArtifactResult> dependencies() throws RepositoryException {
            return Collections.unmodifiableList(dependencies);
        }
    }
}
