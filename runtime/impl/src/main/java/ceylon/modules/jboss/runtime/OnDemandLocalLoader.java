/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * 
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

package ceylon.modules.jboss.runtime;

import java.util.Collections;
import java.util.List;

import org.jboss.modules.DependencySpec;
import org.jboss.modules.LocalLoader;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.Resource;

import com.redhat.ceylon.model.cmr.ArtifactResult;

/**
 * Load modules on demand.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class OnDemandLocalLoader implements LocalLoader {
    private ModuleIdentifier target;
    private CeylonModuleLoader loader;
    private Node<ArtifactResult> root;

    OnDemandLocalLoader(ModuleIdentifier target, CeylonModuleLoader loader, Node<ArtifactResult> root) {
        this.target = target;
        this.loader = loader;
        this.root = root;
    }

    protected LocalLoader doUpdate(String[] tokens) {
        Node<ArtifactResult> current = root;
        for (String token : tokens) {
            current = current.getChild(token);
            if (current == null)
                return null;

            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (current) {
                ArtifactResult i = current.getValue();
                if (i != null) {
                    current.remove(); // remove, so we don't loop; should not happen though

                    DependencySpec mds = loader.createModuleDependency(i, false);
                    try {
                        Module owner = loader.preloadModule(target);
                        loader.updateModule(owner, mds); // update / add lazy dep

                        Module module = loader.loadModule(CeylonModuleLoader.createModuleIdentifier(i));
                        return new ModuleLocalLoader(module);
                    } catch (ModuleLoadException ignored) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    public Class<?> loadClassLocal(String name, boolean resolve) {
        String[] tokens = name.split("\\.");
        LocalLoader ll = doUpdate(tokens);
        return (ll != null ? ll.loadClassLocal(name, resolve) : null);
    }

    public List<Resource> loadResourceLocal(String name) {
        String[] tokens = name.split("/");
        LocalLoader ll = doUpdate(tokens);
        return (ll != null ? ll.loadResourceLocal(name) : Collections.<Resource>emptyList());
    }

    public Package loadPackageLocal(String name) {
        String[] tokens = name.split("\\.");
        LocalLoader ll = doUpdate(tokens);
        return (ll != null ? ll.loadPackageLocal(name) : null);
    }
}
