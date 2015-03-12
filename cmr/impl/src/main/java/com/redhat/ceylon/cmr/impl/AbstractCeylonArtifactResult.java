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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.cmr.api.ArtifactOverrides;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.ArtifactResultType;
import com.redhat.ceylon.cmr.api.DependencyOverride;
import com.redhat.ceylon.cmr.api.ImportType;
import com.redhat.ceylon.cmr.api.ModuleDependencyInfo;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.Overrides;
import com.redhat.ceylon.cmr.api.PathFilter;
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.api.RepositoryException;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.PathFilterParser;

/**
 * Abstract, use Jandex to read off Module info.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractCeylonArtifactResult extends AbstractArtifactResult {
    private RepositoryManager manager;
    private ModuleInfo infos;
    private boolean resolved = false;

    protected AbstractCeylonArtifactResult(Repository repository, RepositoryManager manager, String name, String version) {
        super(repository, name, version);
        this.manager = manager;
    }

    public ArtifactResultType type() {
        return ArtifactResultType.CEYLON;
    }

    protected ModuleInfo resolve(){
        if(!resolved){
            Overrides overrides = repository().getRoot().getService(Overrides.class);
            this.infos = Configuration.getResolvers(manager).resolve(this, overrides);
            resolved = true;
        }
        return infos;
    }

    protected RepositoryManager getManager(){
        return manager;
    }
    
    @Override
    public PathFilter filter(){
        ModuleInfo infos = resolve();
        if(infos == null || infos.getFilter() == null)
            return null;
        try {
            return PathFilterParser.parse(infos.getFilter());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public List<ArtifactResult> dependencies() throws RepositoryException {
        ModuleInfo infos = resolve();
        // TODO -- perhaps null is not valid?
        if (infos == null || infos.getDependencies().isEmpty())
            return Collections.emptyList();

        final List<ArtifactResult> results = new ArrayList<ArtifactResult>();
        for (ModuleDependencyInfo mi : infos.getDependencies()) {
            results.add(new LazyArtifactResult(manager,
                    mi.getName(),
                    mi.getVersion(),
                    mi.isOptional() ? ImportType.OPTIONAL : (mi.isExport() ? ImportType.EXPORT : ImportType.UNDEFINED)));
        }
        return results;
    }
}

