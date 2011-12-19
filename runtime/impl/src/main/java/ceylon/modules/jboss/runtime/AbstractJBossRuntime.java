/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
        builder.addModules();
        // any user defined repos
        for (String token : conf.repositories) {
            try {
                final RootBuilder rb = new RootBuilder(token);
                builder.addExternalRoot(rb.buildRoot());
            } catch (Exception e) {
                Logger.getLogger("ceylon.runtime").log(Level.WARNING, "Failed to add repository: " + token, e);
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
