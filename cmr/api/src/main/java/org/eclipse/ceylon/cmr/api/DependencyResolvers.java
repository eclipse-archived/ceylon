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

package org.eclipse.ceylon.cmr.api;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.ceylon.cmr.spi.Node;
import org.eclipse.ceylon.model.cmr.ArtifactResult;

/**
 * Plugable dependencies utils mechanism.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class DependencyResolvers {
    private final List<DependencyResolver> resolvers = new CopyOnWriteArrayList<>();

    public void addResolver(DependencyResolver resolver) {
        addResolver(resolver, resolvers.size());
    }

    public void addResolver(DependencyResolver resolver, int index) {
        resolvers.add(index, resolver);
    }

    public void removeResolver(DependencyResolver resolver) {
        resolvers.remove(resolver);
    }

    public ModuleInfo resolve(ArtifactResult result, Overrides overrides) {
        for (DependencyResolver dr : resolvers) {
            try {
                ModuleInfo info = dr.resolve(result, overrides);
                if (info != null) {
                    return info;
                }
            } catch (Exception ex) {}
        }
        return null;
    }

    public Node descriptor(Node artifact) {
        for (DependencyResolver dr : resolvers) {
            try {
                Node descriptor = dr.descriptor(artifact);
                if (descriptor != null)
                    return descriptor;
            } catch (Exception ex) {}
        }
        return null;
    }

    @Override
    public String toString() {
        return resolvers.toString();
    }
}

