package com.redhat.ceylon.cmr.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.cmr.api.AbstractDependencyResolverAndModuleInfoReader;
import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.CmrRepository;
import com.redhat.ceylon.cmr.api.ModuleDependencyInfo;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.Overrides;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.spi.ContentStore;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.ArtifactResultType;
import com.redhat.ceylon.model.cmr.RepositoryException;

/**
 * Repository for NodeJS NPM modules
 *
 * @author Tako Schotanus (tako@ceylon-lang.org)
 */
public class NpmRepository extends AbstractRepository {

    public static final String NAMESPACE = "npm";
    
    public NpmRepository(OpenNode root) {
        super(root);
    }

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

    protected List<String> getDefaultParentPathInternal(ArtifactContext context) {
        //npm simply creates a dir with the module name and puts files inside
        final String name = context.getName();
        final List<String> tokens = new ArrayList<String>();
        tokens.addAll(Arrays.asList(name.split("\\.")));
        return tokens;
    }

    public String[] getArtifactNames(ArtifactContext context) {
        List<String> suffixes = Arrays.asList(context.getSuffixes());
        if (suffixes.contains(ArtifactContext.JS)
                || suffixes.contains(ArtifactContext.JS_MODEL)) {
            return getArtifactNames(context.getName(), context.getVersion(), new String[] { ArtifactContext.JS, ArtifactContext.NPM_DESCRIPTOR });
        } else {
            return new String[0];
        }
    }

    @Override
    protected ArtifactResult getArtifactResultInternal(
            RepositoryManager manager, Node node) {
        ArtifactContext context = ArtifactContext.fromNode(node);
        if (context == null) {
            return null;
        }
        List<String> suffixes = Arrays.asList(context.getSuffixes());
        if (!suffixes.contains(ArtifactContext.JS)
                && !suffixes.contains(ArtifactContext.NPM_DESCRIPTOR)) {
            return null;
        }
        return new NpmArtifactResult(this, manager, context.getName(), context.getVersion(), node);
    }

    @Override
    public String getDisplayString() {
        return "[NPM] " + super.getDisplayString();
    }

    public void setNpmCommand(String npmCommand) {
        NpmContentStore store = (NpmContentStore) getRoot().getService(ContentStore.class);
        if (store != null) {
            store.setNpmCommand(npmCommand);
        }
    }
    
    public void setPathForRunningNpm(String npmCommand) {
        NpmContentStore store = (NpmContentStore) getRoot().getService(ContentStore.class);
        if (store != null) {
            store.setPathForRunningNpm(npmCommand);
        }
    }

    public Node findParent(ArtifactContext context) {
        final List<String> tokens = getDefaultParentPath(context);
        Node n = NodeUtils.getNode(getRoot(), tokens);
        try {
            if (n.getContent(File.class) == null) {
                StringBuilder sb = new StringBuilder();
                for (String t : tokens) {
                    if (sb.length() > 0) {
                        sb.append('.');
                    }
                    sb.append(t);
                }
                n = NodeUtils.getNode(getRoot(), Collections.singletonList(sb.toString()));
            }
        } catch (IOException ex) {
            throw new RepositoryException(ex);
        }
        return n;
    }

    private static class NpmArtifactResult extends AbstractArtifactResult {
        private RepositoryManager manager;
        private Node node;

        private ModuleInfo infos;
        private boolean resolved = false;
        
        private NpmArtifactResult(CmrRepository repository, RepositoryManager manager, String name, String version, Node node) {
            super(repository, NAMESPACE, name, version);
            this.manager = manager;
            this.node = node;
        }

        @Override
        public ArtifactResultType type() {
            return ArtifactResultType.OTHER;
        }

        @Override
        protected File artifactInternal() throws RepositoryException {
            try {
                return node.getContent(File.class);
            } catch (IOException e) {
                throw new RepositoryException(e);
            }
        }

        protected ModuleInfo resolve(){
            if(!resolved){
                Overrides overrides = ((CmrRepository)repository()).getRoot().getService(Overrides.class);
                this.infos = Configuration.getResolvers(manager).resolve(this, overrides);
                resolved = true;
            }
            return infos;
        }

        @Override
        public List<ArtifactResult> dependencies() throws RepositoryException {
            ModuleInfo infos = resolve();
            // TODO -- perhaps null is not valid?
            if (infos == null || infos.getDependencies().isEmpty())
                return Collections.emptyList();

            final List<ArtifactResult> results = new ArrayList<>();
            for (ModuleDependencyInfo mi : getOrderedDependencies(infos)) {
                results.add(new LazyArtifactResult(manager,
                        mi.getNamespace(),
                        mi.getName(),
                        mi.getVersion(),
                        mi.isExport(),
                        mi.isOptional(),
                        mi.getModuleScope()));
            }
            return results;
        }

        private List<ModuleDependencyInfo> getOrderedDependencies(ModuleInfo infos) {
            List<ModuleDependencyInfo> dependencies = new ArrayList<>(infos.getDependencies());
            for (int index = 0; index < dependencies.size(); index++) {
                ModuleDependencyInfo dep = dependencies.get(index);
                if ("ceylon.language".equals(dep.getName())) {
                    if (index != 0) {
                        dependencies.remove(index);
                        dependencies.add(0, dep);
                    }
                    break;
                }
            }
            return dependencies;
        }
        
        @Override
        public String repositoryDisplayString() {
            return NodeUtils.getRepositoryDisplayString(node);
        }
        
        @Override
        public String artifactId() {
            return null;
        }
        
        @Override
        public String groupId() {
            return null;
        }
    }
}
