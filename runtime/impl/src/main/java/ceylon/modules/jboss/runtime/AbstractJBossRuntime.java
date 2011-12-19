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

import ceylon.modules.Configuration;
import ceylon.modules.Main;
import ceylon.modules.api.runtime.AbstractRuntime;
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.impl.RepositoryBuilder;
import com.redhat.ceylon.cmr.impl.RootBuilder;
import com.redhat.ceylon.cmr.spi.ContentTransformer;
import com.redhat.ceylon.cmr.spi.MergeStrategy;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoader;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract Ceylon JBoss Modules runtime.
 * Useful for potential extension.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractJBossRuntime extends AbstractRuntime {
    @Override
    public ClassLoader createClassLoader(String name, String version, Configuration conf) throws Exception {
        ModuleLoader moduleLoader = createModuleLoader(conf);
        ModuleIdentifier moduleIdentifier = ModuleIdentifier.fromString(name + ":" + version);
        Module module = moduleLoader.loadModule(moduleIdentifier);
        return SecurityActions.getClassLoader(module);
    }

    /**
     * Get repository extension.
     *
     * @param conf the configuration
     * @return repository extension
     */
    protected Repository createRepository(Configuration conf) {
        final RepositoryBuilder builder = new RepositoryBuilder();

        // add default repos - if they exist
        builder.addCeylonHome();
        if (conf.repositories.isEmpty()) {
            builder.addModules();
        } else {
            // any user defined repos
            for (String token : conf.repositories) {
                try {
                    final RootBuilder rb = new RootBuilder(token);
                    builder.appendExternalRoot(rb.buildRoot());
                } catch (Exception e) {
                    Logger.getLogger("ceylon.runtime").log(Level.WARNING, "Failed to add repository: " + token, e);
                }
            }
        }
        // add remote module repo
        builder.addModulesCeylonLangOrg();

        final MergeStrategy ms = getService(MergeStrategy.class, conf);
        if (ms != null)
            builder.mergeStrategy(ms);

        if (conf.cacheContent)
            builder.cacheContent();

        final ContentTransformer ct = getService(ContentTransformer.class, conf);
        if (ct != null)
            builder.contentTransformer(ct);

        return builder.buildRepository();
    }

    /**
     * Get repository service.
     *
     * @param serviceType the service type
     * @param conf        the configuration
     * @return service instance or null
     */
    protected <T> T getService(Class<T> serviceType, Configuration conf) {
        try {
            String impl = conf.impl.get(serviceType.getName());
            return (impl != null) ? Main.instantiate(serviceType, impl) : null;
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot instantiate service: " + serviceType.getName(), e);
        }
    }

    /**
     * Create module loader.
     *
     * @param conf the configuration
     * @return the module loader
     */
    protected abstract ModuleLoader createModuleLoader(Configuration conf);
}
