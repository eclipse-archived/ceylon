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

package com.redhat.ceylon.cmr.impl;

import java.io.File;
import java.net.Proxy;
import java.util.ServiceLoader;

import com.redhat.ceylon.cmr.api.CmrRepository;
import com.redhat.ceylon.cmr.api.RepositoryBuilder;
import com.redhat.ceylon.common.log.Logger;

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
    
    public CmrRepository buildRepository(String token) throws Exception {
        return buildRepository(token, defaultConfig);
    }

    public CmrRepository buildRepository(String token, RepositoryBuilderConfig config) throws Exception {
        if (token == null)
            throw new IllegalArgumentException("Null repository");

        final String key = (token.startsWith("${") ? token.substring(2, token.length() - 1) : token);
        final String temp = SecurityActions.getProperty(key);
        if (temp != null)
            token = temp;

        for (RepositoryBuilder builder : builders) {
            CmrRepository repo = builder.buildRepository(token, config);
            if (repo != null) {
                return repo;
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
