package com.redhat.ceylon.cmr.impl;

import java.io.File;
import java.util.List;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.ArtifactResultType;
import com.redhat.ceylon.model.cmr.PathFilter;
import com.redhat.ceylon.model.cmr.Repository;
import com.redhat.ceylon.model.cmr.RepositoryException;

public class LazyArtifactResult extends AbstractArtifactResult {
    private ArtifactResult delegate;
    private RepositoryManager manager;
    private boolean optional;
    private boolean exported;

    public LazyArtifactResult(RepositoryManager manager, String namespace, String name, String version, 
            boolean exported, boolean optional) {
        super(null, namespace, name, version);
        this.manager = manager;
        this.exported = exported;
        this.optional = optional;
        assert(ModuleUtil.validNamespace(namespace));
    }

    private synchronized ArtifactResult getDelegate() {
        if (delegate == null) {
            final ArtifactContext context = new ArtifactContext(null, name(), version());
            context.setThrowErrorIfMissing(!optional);
            delegate = manager.getArtifactResult(context);
        }
        return delegate;
    }

    @Override
    public Repository repository() {
        return getDelegate().repository();
    }
    
    @Override
    public boolean exported() {
        return exported;
    }
    
    @Override
    public boolean optional() {
        return optional;
    }
    
    public ArtifactResultType type() {
        return getDelegate().type();
    }

    protected File artifactInternal() throws RepositoryException {
        return getDelegate().artifact();
    }

    public List<ArtifactResult> dependencies() throws RepositoryException {
        return getDelegate().dependencies();
    }

    @Override
    public String repositoryDisplayString() {
        return getDelegate().repositoryDisplayString();
    }
    
    @Override
    public PathFilter filter(){
        return getDelegate().filter();
    }
}