/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted
 * by the author tags.
 *
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

package ceylon.modules.jboss.runtime;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ceylon.modules.api.runtime.LogChecker;
import ceylon.modules.api.util.ModuleVersion;
import ceylon.modules.jboss.repository.ResourceLoaderProvider;
import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.ImportType;
import com.redhat.ceylon.cmr.api.JDKUtils;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.Versions;
import org.jboss.modules.DependencySpec;
import org.jboss.modules.LocalLoader;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleLoader;
import org.jboss.modules.ModuleSpec;
import org.jboss.modules.ModuleSpec.Builder;
import org.jboss.modules.NativeLibraryResourceLoader;
import org.jboss.modules.ResourceLoader;
import org.jboss.modules.ResourceLoaderSpec;
import org.jboss.modules.filter.PathFilters;

/**
 * Ceylon JBoss Module loader.
 * It understands Ceylon repository notion.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class CeylonModuleLoader extends ModuleLoader {
    private static final ModuleIdentifier LANGUAGE;
    private static final ModuleIdentifier COMMON;
    private static final ModuleIdentifier CMR;
    private static final ModuleIdentifier TYPECHECKER;
    private static final ModuleIdentifier COMPILER;
    private static final ModuleIdentifier MAVEN;
    private static final ModuleIdentifier MODULES;
    private static final ModuleIdentifier JANDEX;
    private static final ModuleIdentifier LOGMANAGER;
    private static final ModuleIdentifier RUNTIME;

    private static final String CEYLON_RUNTIME_PATH;
    private static final Set<ModuleIdentifier> BOOTSTRAP;

    private static final DependencySpec JDK_DEPENDENCY;
    private static final Set<String> JDK_MODULE_NAMES;

    private static final List<LogChecker> checkers;

    static {
        final String defaultVersion = System.getProperty(Constants.PROP_CEYLON_SYSTEM_VERSION, Versions.CEYLON_VERSION_NUMBER);
        LANGUAGE = ModuleIdentifier.create("ceylon.language", defaultVersion);
        COMMON = ModuleIdentifier.create("com.redhat.ceylon.common", defaultVersion);
        CMR = ModuleIdentifier.create("com.redhat.ceylon.module-resolver", defaultVersion);
        TYPECHECKER = ModuleIdentifier.create("com.redhat.ceylon.typechecker", defaultVersion);
        COMPILER = ModuleIdentifier.create("com.redhat.ceylon.compiler.java", defaultVersion);
        MAVEN = ModuleIdentifier.create("com.redhat.ceylon.maven-support", "1.0");
        MODULES = ModuleIdentifier.create("org.jboss.modules", "1.1.3.GA");
        JANDEX = ModuleIdentifier.create("org.jboss.jandex", "1.0.3.Final");
        LOGMANAGER = ModuleIdentifier.create("org.jboss.logmanager", "1.4.0.Final");
        RUNTIME = ModuleIdentifier.create("ceylon.runtime", defaultVersion);

        CEYLON_RUNTIME_PATH = ModuleVersion.class.getPackage().getName().replace(".", "/");

        BOOTSTRAP = new HashSet<>();
        BOOTSTRAP.add(LANGUAGE);
        BOOTSTRAP.add(COMMON);
        BOOTSTRAP.add(CMR);
        BOOTSTRAP.add(TYPECHECKER);
        BOOTSTRAP.add(COMPILER);
        BOOTSTRAP.add(MAVEN);
        BOOTSTRAP.add(MODULES);
        BOOTSTRAP.add(JANDEX);
        BOOTSTRAP.add(LOGMANAGER);
        BOOTSTRAP.add(RUNTIME);

        Set<String> jdkPaths = new HashSet<>();
        JDK_MODULE_NAMES = new HashSet<>();
        // JDK
        for (String module : JDKUtils.getJDKModuleNames()) {
            Set<String> paths = JDKUtils.getJDKPathsByModule(module);
            jdkPaths.addAll(paths);
            JDK_MODULE_NAMES.add(module);
        }
        // Oracle
        for (String module : JDKUtils.getOracleJDKModuleNames()) {
            Set<String> paths = JDKUtils.getOracleJDKPathsByModule(module);
            JDK_MODULE_NAMES.add(module);
            jdkPaths.addAll(paths);
        }
        // always exported implicitely
        JDK_DEPENDENCY = DependencySpec.createSystemDependencySpec(jdkPaths, true);

        // add log checkers
        checkers = new ArrayList<>();
        for (LogChecker checker : ServiceLoader.load(LogChecker.class)) {
            checkers.add(checker);
        }
        checkers.add(DefaultLogChecker.INSTANCE);
    }

    private RepositoryManager repository;
    private Map<ModuleIdentifier, List<DependencySpec>> dependencies = new ConcurrentHashMap<>();
    private Graph<ModuleIdentifier, ModuleIdentifier, Boolean> graph = new Graph<>();

    public CeylonModuleLoader(RepositoryManager repository) throws Exception {
        if (repository == null)
            throw new IllegalArgumentException("Null repository adapter");
        this.repository = repository;
        // initialise runtime modules
        init();
    }

    protected void init() throws Exception {
        // The runtime model needs knowledge of these modules existing at runtime, since the language module
        // implementation contains types from these modules
        for (ModuleIdentifier initialModule : Arrays.asList(LANGUAGE, COMMON, TYPECHECKER, COMPILER, CMR)) {
            org.jboss.modules.Module module = org.jboss.modules.Module.getBootModuleLoader().loadModule(initialModule);
            ArtifactResult moduleArtifactResult = findArtifact(initialModule);
            UtilRegistryTransformer.registerModule(initialModule.getName(), initialModule.getSlot(), moduleArtifactResult, SecurityActions.getClassLoader(module));
        }
    }

    /**
     * Update module.
     * Should be thread safe per module.
     *
     * @param module         the module to update
     * @param dependencySpec new dependency
     * @throws ModuleLoadException for any error
     */
    void updateModule(org.jboss.modules.Module module, DependencySpec dependencySpec) throws ModuleLoadException {
        ModuleIdentifier mi = module.getIdentifier();
        List<DependencySpec> deps = dependencies.get(mi);
        if (deps == null) // should not really happen
            return;

        deps.add(dependencySpec);

        setAndRelinkDependencies(module, deps);
        refreshResourceLoaders(module);

        relink(mi, new HashSet<ModuleIdentifier>());
    }

    /**
     * Relink modules.
     *
     * @param mi      the current module identifier
     * @param visited already visited modules
     * @throws ModuleLoadException for any modules error
     */
    @SuppressWarnings({"unchecked"})
    private void relink(ModuleIdentifier mi, Set<ModuleIdentifier> visited) throws ModuleLoadException {
        if (visited.add(mi) == false)
            return;

        Graph.Vertex v = graph.getVertex(mi);
        if (v == null)
            return;

        org.jboss.modules.Module module = preloadModule(mi);
        relink(module);

        Set<Graph.Edge<ModuleIdentifier, Boolean>> in = v.getIn();
        for (Graph.Edge<ModuleIdentifier, Boolean> edge : in) {
            if (edge.getCost()) {
                Graph.Vertex<ModuleIdentifier, Boolean> from = edge.getFrom();
                relink(from.getValue(), visited);
            }
        }
    }

    @Override
    protected org.jboss.modules.Module preloadModule(ModuleIdentifier mi) throws ModuleLoadException {
        if (BOOTSTRAP.contains(mi))
            return org.jboss.modules.Module.getBootModuleLoader().loadModule(mi);

        return super.preloadModule(mi);
    }

    /**
     * Unload module.
     *
     * @param module the module
     */
    void unloadModule(org.jboss.modules.Module module) {
        dependencies.remove(module.getIdentifier());
        unloadModuleLocal(module);
    }

    protected ArtifactResult findArtifact(ModuleIdentifier mi) {
        final ArtifactContext context = new ArtifactContext(mi.getName(), mi.getSlot(), ArtifactContext.CAR, ArtifactContext.JAR);
        return repository.getArtifactResult(context);
    }

    protected boolean isLogging(List<DependencySpec> deps, Builder builder, ArtifactResult result) {
        for (LogChecker checker : checkers) {
            if (checker.match(result)) {
                addLoggingModule(builder, deps);
                return true;
            }
        }
        return false;
    }

    @Override
    protected ModuleSpec findModule(ModuleIdentifier moduleIdentifier) throws ModuleLoadException {
        try {
            final ArtifactResult artifact = findArtifact(moduleIdentifier);
            if (artifact == null)
                return null;

            final File moduleFile = artifact.artifact();
            final boolean isDefault = RepositoryManager.DEFAULT_MODULE.equals(moduleIdentifier.getName());

            final List<DependencySpec> deps = new ArrayList<>();

            ModuleSpec.Builder builder = ModuleSpec.build(moduleIdentifier);
            // add module's jar
            ResourceLoader resourceLoader = ResourceLoaderProvider.getResourceLoader(moduleIdentifier, repository, moduleFile);
            ResourceLoaderSpec rls = ResourceLoaderSpec.createResourceLoaderSpec(resourceLoader, PathFilters.acceptAll());
            builder.addResourceRoot(rls);
            // add potential native lib lookup
            ResourceLoader nativeLoader = new NativeLibraryResourceLoader(new File(moduleFile.getParent(), "lib"));
            builder.addResourceRoot(ResourceLoaderSpec.createResourceLoaderSpec(nativeLoader));

            Graph.Vertex<ModuleIdentifier, Boolean> vertex = graph.createVertex(moduleIdentifier, moduleIdentifier);

            DependencySpec lds = DependencySpec.createLocalDependencySpec();
            builder.addDependency(lds); // local resources
            deps.add(lds);

            if (isDefault == false) {
                Node<ArtifactResult> root = new Node<>();
                for (ArtifactResult i : artifact.dependencies()) {
                    final String name = i.name();

                    // route logging to JBoss LogManager
                    if (isLogging(deps, builder, i)) {
                        continue;
                    }

                    // skip JDK modules
                    if (JDK_MODULE_NAMES.contains(name)) {
                        continue;
                    }

                    if (i.importType() == ImportType.OPTIONAL) {
                        Node<ArtifactResult> current = root;
                        String[] tokens = name.split("\\.");
                        for (String token : tokens) {
                            Node<ArtifactResult> child = current.getChild(token);
                            if (child == null)
                                child = current.addChild(token);
                            current = child;
                        }
                        current.setValue(i);
                    } else {
                        DependencySpec mds = createModuleDependency(i);
                        builder.addDependency(mds);
                        deps.add(mds);
                    }

                    ModuleIdentifier mi = createModuleIdentifier(i);
                    Graph.Vertex<ModuleIdentifier, Boolean> dv = graph.createVertex(mi, mi);
                    Graph.Edge.create(i.importType() == ImportType.EXPORT, vertex, dv);
                }
                if (root.isEmpty() == false) {
                    LocalLoader onDemandLoader = new OnDemandLocalLoader(moduleIdentifier, this, root);
                    builder.setFallbackLoader(onDemandLoader);
                }
            }

            // automagically import the JDK module
            builder.addDependency(JDK_DEPENDENCY);
            // no need to track system deps -- cannot be updated anyway

            createModuleDependency(vertex, deps, builder, LANGUAGE, false);

            // add runtime utils
            final DependencySpec sds = DependencySpec.createModuleDependencySpec(
                PathFilters.match(CEYLON_RUNTIME_PATH),
                PathFilters.rejectAll(),
                this,
                RUNTIME,
                true
            );
            builder.addDependency(sds);
            deps.add(sds);
            Graph.Vertex<ModuleIdentifier, Boolean> sdsv = graph.createVertex(RUNTIME, RUNTIME);
            Graph.Edge.create(false, vertex, sdsv);

            dependencies.put(moduleIdentifier, deps);

            builder.setClassFileTransformer(new UtilRegistryTransformer(moduleIdentifier, artifact));

            return builder.create();
        } catch (Exception e) {
            throw new ModuleLoadException(e);
        }
    }

    private void addLoggingModule(Builder builder, List<DependencySpec> deps) {
        final DependencySpec dependency = DependencySpec.createModuleDependencySpec(
            PathFilters.acceptAll(),
            PathFilters.rejectAll(),
            this,
            LOGMANAGER,
            false
        );
        builder.addDependency(dependency);
        deps.add(dependency);
    }

    protected void createModuleDependency(Graph.Vertex<ModuleIdentifier, Boolean> vertex, List<DependencySpec> deps, ModuleSpec.Builder builder, ModuleIdentifier mi, boolean optional) {
        final DependencySpec dependency = DependencySpec.createModuleDependencySpec(
            PathFilters.acceptAll(),
            PathFilters.rejectAll(),
            this,
            mi,
            optional
        );
        builder.addDependency(dependency);
        deps.add(dependency);

        Graph.Vertex<ModuleIdentifier, Boolean> lv = graph.createVertex(mi, mi);
        Graph.Edge.create(false, vertex, lv);
    }

    /**
     * Create module dependency from import.
     *
     * @param i the import
     * @return new module dependency
     */
    DependencySpec createModuleDependency(ArtifactResult i) {
        // this should never happen
        if (JDK_MODULE_NAMES.contains(i.name()))
            return JDK_DEPENDENCY;

        final ModuleIdentifier mi = createModuleIdentifier(i);
        final boolean export = (i.importType() == ImportType.EXPORT);
        return DependencySpec.createModuleDependencySpec(
            PathFilters.getMetaInfSubdirectoriesWithoutMetaInfFilter(), // import everything?
            (export ? PathFilters.acceptAll() : PathFilters.rejectAll()),
            this,
            mi,
            i.importType() == ImportType.OPTIONAL
        );
    }

    /**
     * Create module identifier.
     *
     * @param i the import
     * @return module identifer
     */
    static ModuleIdentifier createModuleIdentifier(ArtifactResult i) {
        return ModuleIdentifier.create(i.name(), i.version());
    }

    @Override
    public String toString() {
        return "Ceylon ModuleLoader: " + repository;
    }
}
