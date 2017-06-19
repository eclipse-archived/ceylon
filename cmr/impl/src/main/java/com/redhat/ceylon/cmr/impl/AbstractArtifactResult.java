/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
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

package com.redhat.ceylon.cmr.impl;

import java.io.File;
import java.util.List;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.ArtifactResultType;
import com.redhat.ceylon.model.cmr.Exclusion;
import com.redhat.ceylon.model.cmr.ModuleScope;
import com.redhat.ceylon.model.cmr.PathFilter;
import com.redhat.ceylon.model.cmr.Repository;
import com.redhat.ceylon.model.cmr.RepositoryException;
import com.redhat.ceylon.model.cmr.VisibilityType;

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

