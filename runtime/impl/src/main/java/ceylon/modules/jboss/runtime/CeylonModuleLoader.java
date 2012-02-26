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

import ceylon.modules.api.runtime.AbstractRuntime;
import ceylon.modules.api.util.ModuleVersion;
import ceylon.modules.jboss.repository.ResourceLoaderProvider;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.compiler.java.metadata.Import;
import com.redhat.ceylon.compiler.java.metadata.Module;
import org.jboss.modules.DependencySpec;
import org.jboss.modules.LocalLoader;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleLoader;
import org.jboss.modules.ModuleSpec;
import org.jboss.modules.ResourceLoader;
import org.jboss.modules.ResourceLoaderSpec;
import org.jboss.modules.filter.PathFilters;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Ceylon JBoss Module loader.
 * It understands Ceylon repository notion.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class CeylonModuleLoader extends ModuleLoader {
    private static final ModuleIdentifier LANGUAGE;
    private static final ModuleIdentifier CMR;
    private static final ModuleIdentifier MODULES;
    private static final ModuleIdentifier RUNTIME;

    private static final String CEYLON_RUNTIME_PATH;
    private static final Set<ModuleIdentifier> BOOTSTRAP;

    // TODO -- filter out java.* when available, add proper loaderPaths to this system dependency
    private static final String JAVA = "java";
    private static final Set<String> JAVA_SYSTEM_PATHS;

    static {
        final String defaultVersion = System.getProperty("ceylon.version", "0.1");
        LANGUAGE = ModuleIdentifier.create("ceylon.language", defaultVersion);
        CMR = ModuleIdentifier.create("com.redhat.ceylon.cmr");
        MODULES = ModuleIdentifier.create("org.jboss.modules");
        RUNTIME = ModuleIdentifier.create("ceylon.runtime");

        CEYLON_RUNTIME_PATH = ModuleVersion.class.getPackage().getName().replace(".", "/");

        BOOTSTRAP = new HashSet<ModuleIdentifier>();
        BOOTSTRAP.add(LANGUAGE);
        BOOTSTRAP.add(CMR);
        BOOTSTRAP.add(MODULES);
        BOOTSTRAP.add(RUNTIME);

        JAVA_SYSTEM_PATHS = new HashSet<String>();
        JAVA_SYSTEM_PATHS.add("java/lang");
        JAVA_SYSTEM_PATHS.add("java/util");
    }

    private RepositoryManager repository;
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

    protected Module readModule(ModuleIdentifier mi, File moduleFile) throws Exception {
        // TODO -- handle directory
        final URL url = moduleFile.toURI().toURL();
        final ClassLoader parent = getClass().getClassLoader();
        final ClassLoader cl = new URLClassLoader(new URL[]{url}, parent);
        return AbstractRuntime.loadModule(cl, mi.getName());
    }

    @Override
    protected ModuleSpec findModule(ModuleIdentifier moduleIdentifier) throws ModuleLoadException {
        try {
            File moduleFile = repository.getArtifact(moduleIdentifier.getName(), moduleIdentifier.getSlot());
            if (moduleFile == null)
                return null;

            final boolean isDefault = RepositoryManager.DEFAULT_MODULE.equals(moduleIdentifier.getName());

            Module module = null;
            if (!isDefault) {
                module = readModule(moduleIdentifier, moduleFile);
                if (module == null)
                    throw new ModuleLoadException("No module descriptor in module: " + moduleFile);
            }

            final List<DependencySpec> deps = new ArrayList<DependencySpec>();
            ModuleSpec.Builder builder = ModuleSpec.build(moduleIdentifier);
            ResourceLoader resourceLoader = ResourceLoaderProvider.getResourceLoader(moduleIdentifier, repository, moduleFile);
            ResourceLoaderSpec rls = ResourceLoaderSpec.createResourceLoaderSpec(resourceLoader, PathFilters.acceptAll());
            builder.addResourceRoot(rls);

            Graph.Vertex<ModuleIdentifier, Boolean> vertex = graph.createVertex(moduleIdentifier, moduleIdentifier);

            DependencySpec lds = DependencySpec.createLocalDependencySpec();
            builder.addDependency(lds); // local resources
            deps.add(lds);

            if (module != null) {
                Node<Import> root = new Node<Import>();
                for (Import i : module.dependencies()) {
                    if (i.optional()) {
                        String path = i.name();
                        Node<Import> current = root;
                        String[] tokens = path.split("\\.");
                        for (String token : tokens) {
                            Node<Import> child = current.getChild(token);
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
                    Graph.Edge.create(i.export(), vertex, dv);
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

            dependencies.put(moduleIdentifier, deps);

            return builder.create();
        } catch (ModuleLoadException mle) {
            throw mle;
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
    DependencySpec createModuleDependency(Import i) {
        if (JAVA.equalsIgnoreCase(i.name())) {
            return DependencySpec.createSystemDependencySpec(JAVA_SYSTEM_PATHS);
        } else {
            final ModuleIdentifier mi = createModuleIdentifier(i);
            final boolean export = i.export();
            return DependencySpec.createModuleDependencySpec(
                    PathFilters.getDefaultImportFilterWithServices(), // import everything?
                    (export ? PathFilters.acceptAll() : PathFilters.rejectAll()),
                    this,
                    mi,
                    i.optional()
            );
        }
    }

    /**
     * Create module identifier.
     *
     * @param i the import
     * @return module identifer
     */
    static ModuleIdentifier createModuleIdentifier(Import i) {
        return ModuleIdentifier.create(i.name(), i.version());
    }

    public String toString() {
        return "Ceylon ModuleLoader: " + repository;
    }
}
