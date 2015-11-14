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
import java.lang.reflect.Method;
import java.net.Proxy;
import java.net.URI;

import com.redhat.ceylon.cmr.api.CmrRepository;
import com.redhat.ceylon.cmr.api.RepositoryBuilder;
import com.redhat.ceylon.cmr.spi.StructureBuilder;
import com.redhat.ceylon.common.log.Logger;

/**
 * Repository builder.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class RepositoryBuilderImpl implements RepositoryBuilder {

    private Logger log;
    private boolean offline;
    private int timeout;
    private Proxy proxy;
    
    RepositoryBuilderImpl(Logger log, boolean offline, int timeout, Proxy proxy) {
        this.log = log;
        this.offline = offline;
        this.timeout = timeout;
        this.proxy = proxy;
    }

    public CmrRepository buildRepository(String token) throws Exception {
        if (token == null)
            throw new IllegalArgumentException("Null repository");

        final String key = (token.startsWith("${") ? token.substring(2, token.length() - 1) : token);
        final String temp = SecurityActions.getProperty(key);
        if (temp != null)
            token = temp;

        StructureBuilder structureBuilder;
        if (token.startsWith("http:") || token.startsWith("https:")) {
            structureBuilder = new RemoteContentStore(token, log, offline, timeout, proxy);
        } else if (token.equals("jdk") || token.equals("jdk:")) {
            return new JDKRepository();
        } else if (token.equals("aether") || token.equals("aether:") || token.equals("mvn") || token.equals("mvn:")) {
            Class<?> aetherRepositoryClass = Class.forName("com.redhat.ceylon.cmr.maven.AetherRepository");
            Method createRepository = aetherRepositoryClass.getMethod("createRepository", Logger.class, String.class, boolean.class, int.class);
            return (CmrRepository) createRepository.invoke(null, log, null, offline, timeout);
        } else if (token.startsWith("aether:")) {
            return createMavenRepository(token, "aether:");
        } else if (token.startsWith("mvn:")) {
            return createMavenRepository(token, "mvn:");
        } else if (token.startsWith("flat:")) {
            final File file = new File(token.substring(5));
            if (file.exists() == false)
                throw new IllegalArgumentException("Directory does not exist: " + token);
            if (file.isDirectory() == false)
                throw new IllegalArgumentException("Repository exists but is not a directory: " + token);

            structureBuilder = new FileContentStore(file);
            return new FlatRepository(structureBuilder.createRoot());
        } else {
            final File file = (token.startsWith("file:") ? new File(new URI(token)) : new File(token));
            if (file.exists() == false)
                throw new IllegalArgumentException("Directory does not exist: " + token);
            if (file.isDirectory() == false)
                throw new IllegalArgumentException("Repository exists but is not a directory: " + token);

            structureBuilder = new FileContentStore(file);
        }
        return new DefaultRepository(structureBuilder.createRoot());
    }

    protected CmrRepository createMavenRepository(String token, String prefix) throws Exception {
        String config = token.substring(prefix.length());
        // backwards compat: ignore overrides from here, previously located after | symbol
        int p = config.indexOf("|");
        String settingsXml = null;
        if (p < 0) {
            settingsXml = config;
        } else {
            settingsXml = config.substring(0, p);
        }
        Class<?> aetherRepositoryClass = Class.forName("com.redhat.ceylon.cmr.maven.AetherRepository");
        Method createRepository = aetherRepositoryClass.getMethod("createRepository", Logger.class, String.class, boolean.class, int.class);
        return (CmrRepository) createRepository.invoke(null, log, settingsXml, offline, timeout);
    }
}
