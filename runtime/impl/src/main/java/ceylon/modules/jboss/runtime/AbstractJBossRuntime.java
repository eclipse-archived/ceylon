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

import ceylon.modules.Main;
import ceylon.modules.api.runtime.AbstractRuntime;
import ceylon.modules.spi.Constants;
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.impl.RootRepositoryBuilder;
import com.redhat.ceylon.cmr.spi.ContentTransformer;
import com.redhat.ceylon.cmr.spi.MergeStrategy;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoader;

import java.io.File;
import java.util.Map;

/**
 * Abstract Ceylon JBoss Modules runtime.
 * Useful for potential extension.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractJBossRuntime extends AbstractRuntime {
    public ClassLoader createClassLoader(String name, String version, Map<String, String> args) throws Exception {
        ModuleLoader moduleLoader = createModuleLoader(args);
        ModuleIdentifier moduleIdentifier = ModuleIdentifier.fromString(name + ":" + version);
        Module module = moduleLoader.loadModule(moduleIdentifier);
        return SecurityActions.getClassLoader(module);
    }

    /**
     * Get repository extension.
     *
     * @param args the args
     * @return repository extension
     */
    protected Repository createRepository(Map<String, String> args) {
        RootRepositoryBuilder builder = null;
        String root = args.get(Constants.REPOSITORY.toString());
        if (root != null) {
            File rootDir = new File(root);
            if (rootDir.exists() && rootDir.isDirectory())
                builder = new RootRepositoryBuilder(rootDir);
        }
        if (builder == null)
            builder = new RootRepositoryBuilder();

        final MergeStrategy ms = getService(MergeStrategy.class, args);
        if (ms != null)
            builder.mergeStrategy(ms);

        if (Boolean.TRUE.equals(Boolean.valueOf(args.get(Constants.CACHE_CONTENT.toString()))))
            builder.cacheContent();

        final ContentTransformer ct = getService(ContentTransformer.class, args);
        if (ct != null)
            builder.contentTransformer(ct);

        return builder.buildRepository();
    }

    /**
     * Get repository service.
     *
     * @param serviceType the service type
     * @param args        the args
     * @return service instance or null
     */
    protected <T> T getService(Class<T> serviceType, Map<String, String> args) {
        try {
            String impl = args.get(serviceType.getName());
            return (impl != null) ? Main.instantiate(serviceType, impl) : null;
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot instantiate service: " + serviceType.getName(), e);
        }
    }

    /**
     * Create module loader.
     *
     * @param args the args
     * @return the module loader
     */
    protected abstract ModuleLoader createModuleLoader(Map<String, String> args);
}
