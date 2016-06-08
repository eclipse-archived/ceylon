package com.redhat.ceylon.cmr.impl;

import java.io.File;
import java.util.List;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.ArtifactResultType;
import com.redhat.ceylon.model.cmr.ImportType;
import com.redhat.ceylon.model.cmr.PathFilter;
import com.redhat.ceylon.model.cmr.Repository;
import com.redhat.ceylon.model.cmr.RepositoryException;

public class LazyArtifactResult extends AbstractArtifactResult {
    private ArtifactResult delegate;
    private final ImportType importType;
    private RepositoryManager manager;
    private String namespace;

    public LazyArtifactResult(RepositoryManager manager, String namespace, String name, String version, ImportType importType) {
        super(null, name, version);
        this.manager = manager;
        this.namespace = namespace;
        this.importType = importType;
        assert(ModuleUtil.validNamespace(namespace));
    }

    private synchronized ArtifactResult getDelegate() {
        if (delegate == null) {
            final ArtifactContext context = new ArtifactContext(null, name(), version());
            context.setThrowErrorIfMissing(importType() != ImportType.OPTIONAL);
            delegate = manager.getArtifactResult(context);
        }
        return delegate;
    }

    @Override
    public String namespace() {
        return namespace;
    }

    @Override
    public Repository repository() {
        return getDelegate().repository();
    }
    
    @Override
    public ImportType importType() {
        return importType;
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