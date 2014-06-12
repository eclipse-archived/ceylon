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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.ArtifactResultType;
import com.redhat.ceylon.cmr.api.ImportType;
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.RepositoryException;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.impl.AbstractArtifactResult;
import com.redhat.ceylon.cmr.impl.IOUtils;
import com.redhat.ceylon.cmr.impl.NodeUtils;
import com.redhat.ceylon.cmr.spi.Node;
import org.jboss.shrinkwrap.resolver.api.ResolutionException;
import org.jboss.shrinkwrap.resolver.api.Resolvers;
import org.jboss.shrinkwrap.resolver.api.maven.ConfigurableMavenResolverSystem;
import org.jboss.shrinkwrap.resolver.api.maven.MavenArtifactInfo;
import org.jboss.shrinkwrap.resolver.api.maven.MavenFormatStage;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolvedArtifact;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.jboss.shrinkwrap.resolver.api.maven.MavenStrategyStage;
import org.jboss.shrinkwrap.resolver.api.maven.PackagingType;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenCoordinate;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenCoordinates;

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
    private boolean offline;
    private String settingsXml;
    private Overrides overrides;

    AetherUtils(Logger log, boolean offline) {
        this.log = log;
        this.offline = offline;
        settingsXml = getDefaultMavenSettings();
    }

    MavenArtifactInfo[] getDependencies(File pomXml) {
        MavenResolverSystem system = getResolver();
        PomEquippedResolveStage resolverStage = system.loadPomFromFile(pomXml).importDependencies(SCOPES);
        MavenStrategyStage strategyStage = resolverStage.resolve();
        MavenFormatStage formatStage = strategyStage.using(SCOPED_STRATEGY);
        return formatStage.asResolvedArtifact();
    }

    MavenArtifactInfo[] getDependencies(InputStream pomXml) {
        File tempFile = null;
        try {
            tempFile = IOUtils.toTempFile(pomXml);
            return getDependencies(tempFile);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (tempFile != null) {
                //noinspection ResultOfMethodCallIgnored
                tempFile.delete();
            }
        }
    }

    static boolean isOptional(MavenArtifactInfo info) {
        return !(info.getScope() == ScopeType.COMPILE || info.getScope() == ScopeType.RUNTIME);
    }

    void overrideSettingsXml(String settingsXml) {
        if (settingsXml != null) {
            this.settingsXml = settingsXml;
        }
    }

    void setOverrides(String overridesXml) {
        if (overridesXml != null) {
            File overridesFile = new File(overridesXml);
            if (overridesFile.exists() == false) {
                throw new IllegalArgumentException("No such Maven overrides file: " + overridesFile);
            }
            try {
                this.overrides = Overrides.parse(new FileInputStream(overridesFile));
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private ArtifactOverrides findArtifactOverrides(MavenCoordinate mc) {
        return (overrides != null) ? overrides.getArtifactOverrides(mc) : null;
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

        String repositoryDisplayString = NodeUtils.getRepositoryDisplayString(node);

        if (CeylonUtils.arrayContains(ac.getSuffixes(), ArtifactContext.MAVEN_SRC)) {
            return fetchWithClassifier(groupId, artifactId, version, "sources", repositoryDisplayString);
        }

        return fetchDependencies(groupId, artifactId, version, fetchSingleArtifact != null ? fetchSingleArtifact : ac.isFetchSingleArtifact(), repositoryDisplayString);
    }

    private ArtifactResult fetchDependencies(String groupId, String artifactId, String version, boolean fetchSingleArtifact, String repositoryDisplayString) {
        MavenCoordinate mc = MavenCoordinates.createCoordinate(groupId, artifactId, version, PackagingType.JAR, null);
        ArtifactOverrides ao = findArtifactOverrides(mc);
        if (ao != null && ao.getReplace() != null) {
            DependencyOverride replace = ao.getReplace();
            log.debug(String.format("[Maven-Overrides] Replacing %s with %s.", mc, replace.getMvn()));
            // replace fetched dependency
            groupId = replace.getMvn().getGroupId();
            artifactId = replace.getMvn().getArtifactId();
            version = replace.getMvn().getVersion();
            // new AO
            mc = MavenCoordinates.createCoordinate(groupId, artifactId, version, PackagingType.JAR, null);
            ao = findArtifactOverrides(mc);
        }

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
                return new SingleArtifactResult(name, version, info.asFile(), repositoryDisplayString);
            } else {
                final List<ArtifactResult> dependencies = new ArrayList<>();

                final MavenArtifactInfo[] infos = info.getDependencies();
                for (MavenArtifactInfo dep : infos) {
                    final MavenCoordinate dCo = dep.getCoordinate();

                    if (ao != null){
                        if(ao.isRemoved(dCo)) {
                            log.debug(String.format("[Maven-Overrides] Removing %s from %s.", dCo, mc));
                            continue; // skip dependency
                        }
                        if(ao.isAdded(dCo)) {
                            log.debug(String.format("[Maven-Overrides] Replacing %s from %s.", dCo, mc));
                            continue; // skip dependency
                        }
                    }

                    ArtifactResult dr = createArtifactResult(dCo, false, repositoryDisplayString);
                    dependencies.add(dr);
                }

                if (ao != null) {
                    for (DependencyOverride addon : ao.getAdd()) {
                        dependencies.add(createArtifactResult(addon.getMvn(), addon.isShared(), repositoryDisplayString));
                        log.debug(String.format("[Maven-Overrides] Added %s to %s.", addon.getMvn(), mc));
                    }
                }

                return new AetherArtifactResult(name, version, info.asFile(), dependencies, repositoryDisplayString);
            }
        } catch (ResolutionException e) {
            log.debug("Could not resolve artifact [" + coordinates + "] : " + e);
            return null;
        }
    }

    protected ArtifactResult createArtifactResult(final MavenCoordinate dCo, final boolean shared, final String repositoryDisplayString) {
        final String dName = toCanonicalForm(dCo.getGroupId(), dCo.getArtifactId());
        final String dVersion = dCo.getVersion();

        return new MavenArtifactResult(dName, dVersion, repositoryDisplayString) {
            private ArtifactResult result;

            @Override
            public ImportType importType() {
                return shared ? ImportType.EXPORT : ImportType.UNDEFINED;
            }

            private synchronized ArtifactResult getResult() {
                if (result == null) {
                    result = fetchDependencies(dCo.getGroupId(), dCo.getArtifactId(), dVersion, false, repositoryDisplayString);
                }
                return result;
            }

            protected File artifactInternal() throws RepositoryException {
                return getResult().artifact();
            }

            public List<ArtifactResult> dependencies() throws RepositoryException {
                return getResult().dependencies();
            }
        };
    }

    private ArtifactResult fetchWithClassifier(String groupId, String artifactId, String version, String classifier, String repositoryDisplayString) {
        final String name = toCanonicalForm(groupId, artifactId);
        final String coordinates = toCanonicalForm(toCanonicalForm(toCanonicalForm(name, "jar"), classifier), version);
        try {
            final MavenStrategyStage source_mss = getResolver().resolve(coordinates);
            final MavenFormatStage source_mfs = source_mss.using(SCOPED_STRATEGY);
            final MavenResolvedArtifact info = source_mfs.asSingleResolvedArtifact();
            if (info != null) {
                return new SingleArtifactResult(name, version, info.asFile(), repositoryDisplayString);
            }
        } catch (ResolutionException e) {
            log.debug("Could not resolve " + classifier + " for artifact [" + coordinates + "] : " + e);
        }

        log.debug("No artifact found: " + coordinates);
        return null;
    }

    static String toCanonicalForm(String groupId, String artifactId) {
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
        ClassLoader classLoader = AetherUtils.class.getClassLoader();
        if (classLoader == null)
            classLoader = ClassLoader.getSystemClassLoader();

        ConfigurableMavenResolverSystem factory = Resolvers.configure(ConfigurableMavenResolverSystem.class, classLoader).workOffline(offline);
        MavenResolverSystem resolver;
        if (settingsXml.startsWith("classpath:")) {
            resolver = factory.fromClassloaderResource(settingsXml.substring(10), classLoader);
        } else {
            resolver = factory.fromFile(settingsXml);
        }
        return resolver;
    }

    private static abstract class MavenArtifactResult extends AbstractArtifactResult {
        private String repositoryDisplayString;

        protected MavenArtifactResult(String name, String version, String repositoryDisplayString) {
            super(name, version);
            this.repositoryDisplayString = repositoryDisplayString;
        }

        public ArtifactResultType type() {
            return ArtifactResultType.MAVEN;
        }

        @Override
        public String repositoryDisplayString() {
            return repositoryDisplayString;
        }
    }

    private static class SingleArtifactResult extends MavenArtifactResult {
        private File file;

        private SingleArtifactResult(String name, String version, File file, String repositoryDisplayString) {
            super(name, version, repositoryDisplayString);
            this.file = file;
        }

        protected File artifactInternal() throws RepositoryException {
            return file;
        }

        public List<ArtifactResult> dependencies() throws RepositoryException {
            return Collections.emptyList();
        }
    }

    private static class AetherArtifactResult extends SingleArtifactResult {
        private List<ArtifactResult> dependencies;

        private AetherArtifactResult(String name, String version, File file, List<ArtifactResult> dependencies, String repositoryDisplayString) {
            super(name, version, file, repositoryDisplayString);
            this.dependencies = dependencies;
        }

        public List<ArtifactResult> dependencies() throws RepositoryException {
            return Collections.unmodifiableList(dependencies);
        }
    }
}
