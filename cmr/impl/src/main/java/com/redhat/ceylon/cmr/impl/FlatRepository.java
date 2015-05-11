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
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.CmrRepository;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.Overrides;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;
import com.redhat.ceylon.model.cmr.ArtifactResult;

/**
 * Repository which looks modules up from a flat repository.
 *
 * @author Stephane Epardaud
 */
public class FlatRepository extends DefaultRepository {

    public FlatRepository(OpenNode root) {
        super(root);
    }

    @Override
    protected List<String> getDefaultParentPathInternal(ArtifactContext context) {
        // search at the root
        return Collections.emptyList();
    }
    
    @Override
    protected ArtifactResult getArtifactResultInternal(RepositoryManager manager, Node node) {
        return new FlatArtifactResult(this, manager, node);
    }
    
    protected static class FlatArtifactResult extends DefaultArtifactResult {

        public FlatArtifactResult(CmrRepository repository, RepositoryManager manager, Node node) {
            super(repository, manager, node);
        }
        
        @Override
        protected ModuleInfo resolve() {
            ModuleInfo dependencies = super.resolve();
            if(dependencies == null){
                // try to resolve them from other flat repos
                for(CmrRepository repo : getManager().getRepositories()){
                    if(repo instanceof FlatRepository){
                        dependencies = getExternalDescriptor(repo, XmlDependencyResolver.INSTANCE);
                        if(dependencies == null)
                            dependencies = getExternalDescriptor(repo, PropertiesDependencyResolver.INSTANCE);
                        // stop looking if we have found it
                        if(dependencies != null)
                            break;
                    }
                }
            }
            return dependencies;
        }

        private ModuleInfo getExternalDescriptor(CmrRepository repo, ModulesDependencyResolver resolver) {
            String moduleXml = resolver.getQualifiedToplevelDescriptorName(name(), version());
            Overrides overrides = repo.getRoot().getService(Overrides.class);
            Node moduleXmlNode = repo.getRoot().getChild(moduleXml);
            if(moduleXmlNode != null){
                File f = null;
                try {
                    f = moduleXmlNode.getContent(File.class);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if(f != null)
                    return resolver.resolveFromFile(f, name(), version(), overrides);
            }
            return null;
        }
    }
}
