package com.redhat.ceylon.cmr.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.CmrRepository;
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
    
    private static class NpmArtifactResult extends AbstractArtifactResult {
        private Node node;

        private NpmArtifactResult(CmrRepository repository, RepositoryManager manager, String name, String version, Node node) {
            super(repository, NAMESPACE, name, version);
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

        @Override
        public List<ArtifactResult> dependencies() throws RepositoryException {
            //Get the package.json file
            return Collections.emptyList(); // dunno how to grab deps
        }
        
        @Override
        public String repositoryDisplayString() {
            return NodeUtils.getRepositoryDisplayString(node);
        }
    }
}
