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
import java.util.List;

import com.redhat.ceylon.cmr.api.CmrRepository;
import com.redhat.ceylon.cmr.api.Overrides;
import com.redhat.ceylon.cmr.api.RepositoryBuilder;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.RepositoryManagerBuilder;
import com.redhat.ceylon.cmr.spi.ContentTransformer;
import com.redhat.ceylon.cmr.spi.MergeStrategy;
import com.redhat.ceylon.cmr.spi.OpenNode;
import com.redhat.ceylon.common.log.Logger;

/**
 * Root repository builder.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RepositoryManagerBuilderImpl extends RepositoryManagerBuilder {

    private final RootRepositoryManager repository;
    private final Logger log;
    private final boolean offline;
    private final int timeout;
    private final Proxy proxy;

    public RepositoryManagerBuilderImpl(Logger log, boolean offline, int timeout, Proxy proxy, Overrides overrides) {
        repository = new RootRepositoryManager(log, overrides);
        this.log = log;
        this.offline = offline;
        this.timeout = timeout;
        this.proxy = proxy;
        init();
    }

    public RepositoryManagerBuilderImpl(File mainRepository, Logger log, boolean offline, int timeout, Proxy proxy, Overrides overrides) {
        repository = new RootRepositoryManager(mainRepository, log, overrides);
        this.log = log;
        this.offline = offline;
        this.timeout = timeout;
        this.proxy = proxy;
        init();
    }

    protected void init() {
        if(getCache() != null)
            getCache().addService(MergeStrategy.class, new DefaultMergeStrategy());
    }

    private OpenNode getCache() {
        return repository.getCache();
    }

    @Override
    public RepositoryBuilder repositoryBuilder() {
        return new RepositoryBuilderImpl(log, offline, timeout, proxy);
    }

    public RepositoryManagerBuilderImpl mergeStrategy(MergeStrategy strategy) {
        if(getCache() != null)
            getCache().addService(MergeStrategy.class, strategy);
        return this;
    }

    public RepositoryManagerBuilderImpl contentTransformer(ContentTransformer transformer) {
        if(getCache() != null)
            getCache().addService(ContentTransformer.class, transformer);
        return this;
    }

    public RepositoryManagerBuilderImpl cacheContent() {
        if(getCache() != null)
            getCache().addService(ContentTransformer.class, new CachingContentTransformer());
        return this;
    }

    protected RepositoryManagerBuilderImpl addExternalRoot(OpenNode externalRoot) {
        repository.addRepository(new DefaultRepository(externalRoot));
        return this;
    }

    public RepositoryManagerBuilderImpl addRepository(CmrRepository externalRoot) {
        repository.addRepository(externalRoot);
        return this;
    }

    public List<String> getRepositoriesDisplayString() {
        return repository.getRepositoriesDisplayString();
    }

    public RepositoryManager buildRepository() {
        return repository;
    }

    @Override
    public boolean hasMavenRepository() {
        return repository.hasMavenRepository();
    }
}
