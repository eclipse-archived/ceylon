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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ceylon.modules.api.util.ModuleVersion;
import ceylon.modules.jboss.repository.ResourceLoaderProvider;
import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.ImportType;
import com.redhat.ceylon.cmr.api.JDKUtils;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.VisibilityType;
import com.redhat.ceylon.common.Versions;
import org.jboss.modules.DependencySpec;
import org.jboss.modules.LocalLoader;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleLoader;
import org.jboss.modules.ModuleSpec;
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
    private static final ModuleIdentifier RUNTIME;

    private static final String CEYLON_RUNTIME_PATH;
    private static final Set<ModuleIdentifier> BOOTSTRAP;

    private static final Map<String, DependencySpec> SYSTEM_DEPENDENCIES;

    static {
        final String defaultVersion = System.getProperty("ceylon.version", Versions.CEYLON_VERSION_NUMBER);
        LANGUAGE = ModuleIdentifier.create("ceylon.language", defaultVersion);
        COMMON = ModuleIdentifier.create("com.redhat.ceylon.common", defaultVersion);
        CMR = ModuleIdentifier.create("com.redhat.ceylon.module-resolver", defaultVersion);
        TYPECHECKER = ModuleIdentifier.create("com.redhat.ceylon.typechecker", defaultVersion);
        COMPILER = ModuleIdentifier.create("com.redhat.ceylon.compiler.java", defaultVersion);
        MAVEN = ModuleIdentifier.create("com.redhat.ceylon.maven-support");
        MODULES = ModuleIdentifier.create("org.jboss.modules");
        JANDEX = ModuleIdentifier.create("org.jboss.jandex");
        RUNTIME = ModuleIdentifier.create("ceylon.runtime", defaultVersion);

        CEYLON_RUNTIME_PATH = ModuleVersion.class.getPackage().getName().replace(".", "/");

        BOOTSTRAP = new HashSet<ModuleIdentifier>();
        BOOTSTRAP.add(LANGUAGE);
        BOOTSTRAP.add(COMMON);
        BOOTSTRAP.add(CMR);
        BOOTSTRAP.add(TYPECHECKER);
        BOOTSTRAP.add(COMPILER);
        BOOTSTRAP.add(MAVEN);
        BOOTSTRAP.add(MODULES);
        BOOTSTRAP.add(JANDEX);
        BOOTSTRAP.add(RUNTIME);

        SYSTEM_DEPENDENCIES = new HashMap<String, DependencySpec>();
        // JDK
        for (String module : JDKUtils.getJDKModuleNames()) {
            Set<String> paths = JDKUtils.getJDKPathsByModule(module);
            DependencySpec dependencySpec = DependencySpec.createSystemDependencySpec(paths);
            SYSTEM_DEPENDENCIES.put(module, dependencySpec);
        }
        // Oracle
        for (String module : JDKUtils.getOracleJDKModuleNames()) {
            Set<String> paths = JDKUtils.getOracleJDKPathsByModule(module);
            DependencySpec dependencySpec = DependencySpec.createSystemDependencySpec(paths);
            SYSTEM_DEPENDENCIES.put(module, dependencySpec);
        }
    }

    private RepositoryManager repository;
    private Map<ModuleIdentifier, ArtifactResult> artifacts = new ConcurrentHashMap<ModuleIdentifier, ArtifactResult>();
    private Map<ModuleIdentifier, List<DependencySpec>> dependencies = new ConcurrentHashMap<ModuleIdentifier, List<DependencySpec>>();
    private Graph<ModuleIdentifier, ModuleIdentifier, Boolean> graph = new Graph<ModuleIdentifier, ModuleIdentifier, Boolean>();

    public CeylonModuleLoader(RepositoryManager repository) {
        if (repository == null)
            throw new IllegalArgumentException("Null repository adapter");
        this.repository = repository;
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
        if (BOOTSTRAP.contains(mi)){
            return org.jboss.modules.Module.getBootModuleLoader().loadModule(mi);
        }

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
        final ArtifactContext context = new ArtifactContext(mi.getName(), mi.getSlot(), ArtifactContext.CAR);
        ArtifactResult result = repository.getArtifactResult(context);
        if (result == null) {
            context.setSuffix(ArtifactContext.JAR);
            result = repository.getArtifactResult(context);
        }
        return result;
    }

    @Override
    protected ModuleSpec findModule(ModuleIdentifier moduleIdentifier) throws ModuleLoadException {
        try {
            final ArtifactResult artifact = findArtifact(moduleIdentifier);
            if (artifact == null)
                return null;

            final File moduleFile = artifact.artifact();
            final boolean isDefault = RepositoryManager.DEFAULT_MODULE.equals(moduleIdentifier.getName());

            final List<DependencySpec> deps = new ArrayList<DependencySpec>();
            ModuleSpec.Builder builder = ModuleSpec.build(moduleIdentifier);
            ResourceLoader resourceLoader = ResourceLoaderProvider.getResourceLoader(moduleIdentifier, repository, moduleFile);
            ResourceLoaderSpec rls = ResourceLoaderSpec.createResourceLoaderSpec(resourceLoader, PathFilters.acceptAll());
            builder.addResourceRoot(rls);

            Graph.Vertex<ModuleIdentifier, Boolean> vertex = graph.createVertex(moduleIdentifier, moduleIdentifier);

            DependencySpec lds = DependencySpec.createLocalDependencySpec();
            builder.addDependency(lds); // local resources
            deps.add(lds);

            // used moduleDependencies
            Set<String> moduleDependencies = new HashSet<String>();

            if (isDefault == false) {
                Node<ArtifactResult> root = new Node<ArtifactResult>();
                for (ArtifactResult i : artifact.dependencies()) {
                    final String name = i.name();
                    moduleDependencies.add(name); // track used module dependencies

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

                    // no need to track system deps -- cannot be updated anyway
                    if (SYSTEM_DEPENDENCIES.containsKey(name) == false) {
                        ModuleIdentifier mi = createModuleIdentifier(i);
                        Graph.Vertex<ModuleIdentifier, Boolean> dv = graph.createVertex(mi, mi);
                        Graph.Edge.create(i.importType() == ImportType.EXPORT, vertex, dv);
                    }
                }
                if (root.isEmpty() == false) {
                    LocalLoader onDemandLoader = new OnDemandLocalLoader(moduleIdentifier, this, root);
                    builder.setFallbackLoader(onDemandLoader);
                }
            }

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

            // add JDK moduleDependencies to loose artifacts
            if (artifact.visibilityType() == VisibilityType.LOOSE) {
                for (String module : JDKUtils.getJDKModuleNames()) {
                    if (moduleDependencies.contains(module) == false) {
                        DependencySpec ds = SYSTEM_DEPENDENCIES.get(module);
                        builder.addDependency(ds);
                        // no need to track system deps -- cannot be updated anyway
                    }
                }
            }

            dependencies.put(moduleIdentifier, deps);

            ModuleSpec moduleSpec = builder.create();
            // add on successful spec build
            artifacts.put(moduleIdentifier, artifact);

            return moduleSpec;
        } catch (Exception e) {
            throw new ModuleLoadException(e);
        }
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
        final DependencySpec dependencySpec = SYSTEM_DEPENDENCIES.get(i.name());
        if (dependencySpec != null)
            return dependencySpec;

        final ModuleIdentifier mi = createModuleIdentifier(i);
        final boolean export = (i.importType() == ImportType.EXPORT);
        return DependencySpec.createModuleDependencySpec(
                PathFilters.getDefaultImportFilterWithServices(), // import everything?
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

    public String toString() {
        return "Ceylon ModuleLoader: " + repository;
    }

    public void setupRuntimeModuleSystem() throws ModuleLoadException {
        Module languageModule = org.jboss.modules.Module.getBootModuleLoader().loadModule(LANGUAGE);
        ArtifactResult languageModuleArtifactResult = findArtifact(LANGUAGE);
        com.redhat.ceylon.compiler.java.Util.loadModule(LANGUAGE.getName(), LANGUAGE.getSlot(), languageModuleArtifactResult, SecurityActions.getClassLoader(languageModule));

        for(Entry<ModuleIdentifier, ArtifactResult> moduleSpec : artifacts.entrySet()){
            ModuleIdentifier mi = moduleSpec.getKey();
            final Module module = findLoadedModuleLocal(mi);
            com.redhat.ceylon.compiler.java.Util.loadModule(mi.getName(), mi.getSlot(), moduleSpec.getValue(), SecurityActions.getClassLoader(module));
        }
    }
}
