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

import java.util.logging.Logger;

import com.redhat.ceylon.cmr.api.DependencyResolver;
import com.redhat.ceylon.cmr.api.DependencyResolvers;
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.api.RepositoryManager;

/**
 * Simple config holder.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Configuration {
    private static DependencyResolver mavenResolver;

    public static final String MAVEN_RESOLVER_CLASS = "com.redhat.ceylon.cmr.maven.MavenDependencyResolver";
    
    public static DependencyResolvers getResolvers(RepositoryManager manager) {
        DependencyResolvers resolvers = new DependencyResolvers();
        resolvers.addResolver(BytecodeUtils.INSTANCE);
        resolvers.addResolver(JSUtils.INSTANCE);
        resolvers.addResolver(PropertiesDependencyResolver.INSTANCE);
        resolvers.addResolver(XmlDependencyResolver.INSTANCE);
        resolvers.addResolver(OSGiDependencyResolver.INSTANCE);
        if (usesMaven(manager)) {
            if (mavenResolver == null) {
                mavenResolver = getResolver(MAVEN_RESOLVER_CLASS);
            }
            if (mavenResolver != null) {
                resolvers.addResolver(mavenResolver);
            }
        }
        return resolvers;
    }

    private static boolean usesMaven(RepositoryManager manager) {
        if (manager != null) {
            for (Repository repo : manager.getRepositories()) {
                if (repo instanceof MavenRepository) {
                    return true;
                }
            }
        }
        return false;
    }

    private static DependencyResolver getResolver(String className) {
        try {
            ClassLoader cl = Configuration.class.getClassLoader();
            DependencyResolver resolver = (DependencyResolver) cl.loadClass(className).newInstance();
            return resolver;
        } catch (Throwable t) {
            Logger.getLogger(Configuration.class.getName()).warning(String.format("Cannot add resolver %s - %s", className, t));
        }
        return null;
    }
}
