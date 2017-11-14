/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.impl;

import java.io.File;
import java.util.List;

import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.common.ModuleUtil;
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.cmr.ArtifactResultType;
import org.eclipse.ceylon.model.cmr.ModuleScope;
import org.eclipse.ceylon.model.cmr.PathFilter;
import org.eclipse.ceylon.model.cmr.Repository;
import org.eclipse.ceylon.model.cmr.RepositoryException;

public class LazyArtifactResult extends AbstractArtifactResult {
    private ArtifactResult delegate;
    private RepositoryManager manager;
    private boolean optional;
    private boolean exported;
    private ModuleScope scope;

    public LazyArtifactResult(RepositoryManager manager, String namespace, String name, String version, 
            boolean exported, boolean optional, ModuleScope scope) {
        super(null, namespace, name, version);
        this.manager = manager;
        this.exported = exported;
        this.optional = optional;
        this.scope = scope;
        assert(ModuleUtil.validNamespace(namespace));
    }

    private synchronized ArtifactResult getDelegate() {
        if (delegate == null) {
            final ArtifactContext context = new ArtifactContext(null, name(), version(), ArtifactContext.CAR, ArtifactContext.JAR);
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
 
    @Override
    public ModuleScope moduleScope() {
        return scope;
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
    
    @Override
    public String artifactId() {
        return getDelegate().artifactId();
    }
    
    @Override
    public String groupId() {
        return getDelegate().groupId();
    }
}