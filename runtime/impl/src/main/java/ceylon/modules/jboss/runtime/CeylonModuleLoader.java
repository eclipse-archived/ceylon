/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package ceylon.modules.jboss.runtime;

import ceylon.language.descriptor.Import;
import ceylon.language.descriptor.Module;
import ceylon.modules.api.runtime.AbstractRuntime;
import ceylon.modules.api.util.CeylonToJava;
import ceylon.modules.jboss.repository.ResourceLoaderProvider;
import com.redhat.ceylon.cmr.api.Repository;
import org.jboss.modules.DependencySpec;
import org.jboss.modules.LocalLoader;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleLoader;
import org.jboss.modules.ModuleSpec;
import org.jboss.modules.ResourceLoader;
import org.jboss.modules.ResourceLoaderSpec;
import org.jboss.modules.filter.PathFilter;
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
 * Ceyon JBoss Module loader.
 * It understands Ceylon repository notion.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class CeylonModuleLoader extends ModuleLoader {
    private static final ModuleIdentifier LANGUAGE;
    private static final Set<String> CEYLON_RUNTIME_PATHS;

    static {
        final String defaultVersion = System.getProperty("ceylon.version", "0.1");
        LANGUAGE = ModuleIdentifier.create("ceylon.language", defaultVersion);

        CEYLON_RUNTIME_PATHS = new HashSet<String>();
        CEYLON_RUNTIME_PATHS.add(CeylonToJava.class.getPackage().getName().replace(".", "/"));
    }

    private Repository repository;
    private Map<ModuleIdentifier, List<DependencySpec>> dependencies = new ConcurrentHashMap<ModuleIdentifier, List<DependencySpec>>();
    private Graph<ModuleIdentifier, ModuleIdentifier, Boolean> graph = new Graph<ModuleIdentifier, ModuleIdentifier, Boolean>();

    public CeylonModuleLoader(Repository repository) {
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
    protected org.jboss.modules.Module preloadModule(ModuleIdentifier identifier) throws ModuleLoadException {
        return super.preloadModule(identifier);
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
        URL url = moduleFile.toURI().toURL();
        ClassLoader cl = new URLClassLoader(new URL[]{url});
        String modulePath = mi.getName() + AbstractRuntime.MODULE_INFO_CLASS;
        return AbstractRuntime.loadModule(cl, modulePath);
    }

    @Override
    protected ModuleSpec findModule(ModuleIdentifier moduleIdentifier) throws ModuleLoadException {
        try {
            File moduleFile = repository.getArtifact(moduleIdentifier.getName(), moduleIdentifier.getSlot());
            if (moduleFile == null)
                return null;

            Module module = readModule(moduleIdentifier, moduleFile);
            if (module == null)
                throw new ModuleLoadException("No module descriptor in module: " + moduleFile);

            final List<DependencySpec> deps = new ArrayList<DependencySpec>();
            ModuleSpec.Builder builder = ModuleSpec.build(moduleIdentifier);
            ResourceLoader resourceLoader = ResourceLoaderProvider.getResourceLoader(moduleIdentifier, repository, moduleFile);
            ResourceLoaderSpec rls = ResourceLoaderSpec.createResourceLoaderSpec(resourceLoader, PathFilters.acceptAll());
            builder.addResourceRoot(rls);

            Graph.Vertex<ModuleIdentifier, Boolean> vertex = graph.createVertex(moduleIdentifier, moduleIdentifier);

            PathFilter exportFilter = new PathFilterWrapper(module.getExports());
            PathFilter importFilter = new PathFilterWrapper(module.getImports());

            DependencySpec lds = DependencySpec.createLocalDependencySpec(importFilter, exportFilter);
            builder.addDependency(lds); // local resources
            deps.add(lds);

            Iterable<? extends Import> imports = CeylonToJava.toIterable(module.getDependencies());
            if (imports != null) {
                Node<Import> root = new Node<Import>();
                for (Import i : imports) {
                    if (i.getOnDemand()) {
                        String path = i.getName().toString();
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
                    boolean export = i.getExports() != ceylon.language.descriptor.PathFilters.rejectAll();
                    Graph.Edge.create(export, vertex, dv);
                }
                if (root.isEmpty() == false) {
                    LocalLoader onDemandLoader = new OnDemandLocalLoader(moduleIdentifier, this, root);
                    builder.setFallbackLoader(onDemandLoader);
                }
            }

            createModuleDependency(vertex, deps, builder, LANGUAGE);

            // add system paths
            final DependencySpec sds = DependencySpec.createSystemDependencySpec(
                    PathFilters.match("ceylon/*"),
                    PathFilters.rejectAll(),
                    CEYLON_RUNTIME_PATHS
            );
            builder.addDependency(sds);
            deps.add(sds);

            dependencies.put(moduleIdentifier, deps);

            return builder.create();
        } catch (ModuleLoadException mle) {
            throw mle;
        } catch (Exception e) {
            throw new ModuleLoadException(e);
        }
    }

    protected void createModuleDependency(Graph.Vertex<ModuleIdentifier, Boolean> vertex, List<DependencySpec> deps, ModuleSpec.Builder builder, ModuleIdentifier mi) {
        final DependencySpec dependency = DependencySpec.createModuleDependencySpec(
                PathFilters.acceptAll(),
                PathFilters.rejectAll(),
                this,
                mi,
                false
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
        ModuleIdentifier mi = createModuleIdentifier(i);
        PathFilter exportFilter = new PathFilterWrapper(i.getExports());
        PathFilter importFilter = new PathFilterWrapper(i.getImports());
        return DependencySpec.createModuleDependencySpec(importFilter, exportFilter, this, mi, i.getOptional());
    }

    /**
     * Create module identifier.
     *
     * @param i the import
     * @return module identifer
     */
    static ModuleIdentifier createModuleIdentifier(Import i) {
        return ModuleIdentifier.create(i.getName().toString(), i.getVersion().toString());
    }

    public String toString() {
        return "Ceylon ModuleLoader: " + repository;
    }
}
