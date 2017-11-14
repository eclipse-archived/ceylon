/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.impl;

import java.io.File;
import java.net.Proxy;
import java.util.List;

import org.eclipse.ceylon.cmr.api.CmrRepository;
import org.eclipse.ceylon.cmr.api.Overrides;
import org.eclipse.ceylon.cmr.api.RepositoryBuilder;
import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.cmr.api.RepositoryManagerBuilder;
import org.eclipse.ceylon.cmr.spi.ContentTransformer;
import org.eclipse.ceylon.cmr.spi.MergeStrategy;
import org.eclipse.ceylon.cmr.spi.OpenNode;
import org.eclipse.ceylon.common.log.Logger;

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
    private final String currentDirectory;

    public RepositoryManagerBuilderImpl(Logger log, boolean offline, int timeout, Proxy proxy, String currentDirectory, Overrides overrides) {
        this(log, offline, timeout, proxy, currentDirectory, overrides, true);
    }
    public RepositoryManagerBuilderImpl(Logger log, boolean offline, int timeout, Proxy proxy, String currentDirectory, Overrides overrides, boolean upgradeDist) {
        repository = new RootRepositoryManager(log, overrides, upgradeDist);
        this.log = log;
        this.offline = offline;
        this.timeout = timeout;
        this.proxy = proxy;
        this.currentDirectory = currentDirectory;
        init();
    }

    public RepositoryManagerBuilderImpl(File mainRepository, Logger log, boolean offline, int timeout, Proxy proxy, String currentDirectory, Overrides overrides) {
        this(mainRepository, log, offline, timeout, proxy, currentDirectory, overrides, true);
    }
    
    public RepositoryManagerBuilderImpl(File mainRepository, Logger log, boolean offline, int timeout, Proxy proxy, String currentDirectory, Overrides overrides, boolean upgradeDist) {
        repository = new RootRepositoryManager(mainRepository, log, overrides, upgradeDist);
        this.log = log;
        this.offline = offline;
        this.timeout = timeout;
        this.proxy = proxy;
        this.currentDirectory = currentDirectory;
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
        return new RepositoryBuilderImpl(log, offline, timeout, proxy, currentDirectory);
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
}
