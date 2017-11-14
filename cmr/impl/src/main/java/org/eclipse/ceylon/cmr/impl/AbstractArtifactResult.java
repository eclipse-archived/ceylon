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
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.cmr.ArtifactResultType;
import org.eclipse.ceylon.model.cmr.Exclusion;
import org.eclipse.ceylon.model.cmr.ModuleScope;
import org.eclipse.ceylon.model.cmr.PathFilter;
import org.eclipse.ceylon.model.cmr.Repository;
import org.eclipse.ceylon.model.cmr.RepositoryException;
import org.eclipse.ceylon.model.cmr.VisibilityType;

/**
 * Abstract artifact result.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractArtifactResult implements ArtifactResult {

    private final String namespace;
    private final String name;
    private final String version;

    private volatile File artifact;
    private volatile boolean checked;

    private PathFilter filter;
    
    private Repository repository;
    private List<Exclusion> exclusions;

    protected AbstractArtifactResult(Repository repository, String namespace, String name, String version) {
        this.repository = repository;
        this.namespace = namespace;
        this.name = name;
        this.version = version;
    }

    @Override
    public String namespace() {
        return namespace;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String version() {
        return version;
    }
    
    @Override
    public String classifier() {
        return null;
    }

    @Override
    public boolean exported() {
        return false;
    }
    
    @Override
    public boolean optional() {
        return false;
    }

    @Override
    public ModuleScope moduleScope() {
        return ModuleScope.COMPILE;
    }
    
    @Override
    public VisibilityType visibilityType() {
        if (type() == ArtifactResultType.CEYLON) {
            File file = artifact();
            if (file != null && file.getName().endsWith(ArtifactContext.CAR)) {
                return VisibilityType.STRICT;
            }
        }
        return VisibilityType.LOOSE;
    }

    @Override
    public File artifact() throws RepositoryException {
        if (artifact == null && checked == false) {
            checked = true;
            artifact = artifactInternal();
        }
        return artifact;
    }

    protected abstract File artifactInternal();

    @Override
    public PathFilter filter() {
        return filter;
    }

    protected void setFilterInternal(PathFilter filter) {
        this.filter = filter;
    }

    @Override
    public String toString() {
        StringBuffer txt = new StringBuffer();
        txt.append("[Artifact result: ");
        if (namespace() != null) {
            txt.append(namespace());
            txt.append(":");
        }
        txt.append(name);
        txt.append("/");
        txt.append(version);
        if (artifact != null) {
            try {
                String suffix = ArtifactContext.getSuffixFromFilename(artifact().getName());
                txt.append(" (");
                txt.append(suffix);
                txt.append(")");
            } catch (RepositoryException ignored) {
            }
        }
        if (moduleScope() != ModuleScope.COMPILE) {
            txt.append(" ");
            txt.append(moduleScope());
        }
        txt.append("]");
        return txt.toString();
    }

    @Override
    public Repository repository() {
        return repository;
    }
    
    @Override
    public List<Exclusion> getExclusions() {
        return exclusions;
    }

    public void setExclusions(List<Exclusion> exclusions) {
        this.exclusions = exclusions;
    }
}

