

package org.eclipse.ceylon.cmr.impl;

import java.io.File;
import java.net.Proxy;
import java.util.ServiceLoader;

import org.eclipse.ceylon.cmr.api.CmrRepository;
import org.eclipse.ceylon.cmr.api.RepositoryBuilder;
import org.eclipse.ceylon.common.log.Logger;

/**
 * "Meta" Repository builder. It uses the Java services mechanism
 * to obtain a list of all available RepositoryBuilder implementations
 * and passes them the given token one by one until one of them
 * returns a result.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 * @author Tako Schotanus (tako@ceylon-lang.org)
 */
class RepositoryBuilderImpl implements RepositoryBuilder {

    private final RepositoryBuilderConfig defaultConfig;
    
    RepositoryBuilderImpl(Logger log, boolean offline, int timeout, Proxy proxy, String currentDirectory) {
        this.defaultConfig = new RepositoryBuilderConfig(log, offline, timeout, proxy, currentDirectory);
    }

    private static final ServiceLoader<RepositoryBuilder> builders;
    private static final LocalRepositoryBuilder localBuilder;
    
    static {
        builders = ServiceLoader.load(RepositoryBuilder.class, RepositoryBuilderImpl.class.getClassLoader());
        localBuilder = new LocalRepositoryBuilder();
    }
    
    public CmrRepository[] buildRepository(String token) throws Exception {
        return buildRepository(token, defaultConfig);
    }

    public CmrRepository[] buildRepository(String token, RepositoryBuilderConfig config) throws Exception {
        if (token == null)
            throw new IllegalArgumentException("Null repository");

        final String key = (token.startsWith("${") ? token.substring(2, token.length() - 1) : token);
        final String temp = SecurityActions.getProperty(key);
        if (temp != null)
            token = temp;

        for (RepositoryBuilder builder : builders) {
            CmrRepository[] repos = builder.buildRepository(token, config);
            if (repos != null) {
                return repos;
            }
        }
        
        return localBuilder.buildRepository(token, config);
    }

    @Override
    public String absolute(File cwd, String token) throws Exception {
        for (RepositoryBuilder builder : builders) {
            String abstoken = builder.absolute(cwd, token);
            if (abstoken != null) {
                return abstoken;
            }
        }
        return localBuilder.absolute(cwd, token);
    }
    
}
