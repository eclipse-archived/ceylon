package com.redhat.ceylon.cmr.impl;

import java.io.File;
import java.util.List;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.RepositoryManager;
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

    public LazyArtifactResult(RepositoryManager manager, String name, String version, ImportType importType) {
        super(null, name, version);
        this.manager = manager;
        this.importType = importType;
    }

    private synchronized ArtifactResult getDelegate() {
        if (delegate == null) {
            final ArtifactContext context = new ArtifactContext(name(), version());
            context.setThrowErrorIfMissing(importType() != ImportType.OPTIONAL);
            delegate = manager.getArtifactResult(context);
        }
        return delegate;
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