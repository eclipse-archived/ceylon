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
import java.util.List;

import com.redhat.ceylon.cmr.api.CmrRepository;
import com.redhat.ceylon.cmr.api.ModuleDependencyInfo;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.Overrides;
import com.redhat.ceylon.cmr.api.PathFilterParser;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.ArtifactResultType;
import com.redhat.ceylon.model.cmr.ImportType;
import com.redhat.ceylon.model.cmr.PathFilter;
import com.redhat.ceylon.model.cmr.Repository;
import com.redhat.ceylon.model.cmr.RepositoryException;

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
            Overrides overrides = ((CmrRepository)repository()).getRoot().getService(Overrides.class);
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
        for (ModuleDependencyInfo mi : getOrderedDependencies(infos)) {
            results.add(new LazyArtifactResult(manager,
                    mi.getName(),
                    mi.getVersion(),
                    mi.isOptional() ? ImportType.OPTIONAL : (mi.isExport() ? ImportType.EXPORT : ImportType.UNDEFINED)));
        }
        return results;
    }

    /**
     * {@code ModuleInfo} has dependencies as a {@code Set} (actually a 
     * {@code HashSet}), but {@link #dependencies()} needs to return a list.
     * The order matter much except when several dependencies can't be found.
     * That happens, in particular, when trying to run a module compiled with a 
     * more recent (but still BC) version of Ceylon. So for consistency 
     * on that case we will already return ceylon.language first. 
     */
    private List<ModuleDependencyInfo> getOrderedDependencies(ModuleInfo infos) {
        List<ModuleDependencyInfo> dependencies = new ArrayList<ModuleDependencyInfo>(infos.getDependencies());
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
}

