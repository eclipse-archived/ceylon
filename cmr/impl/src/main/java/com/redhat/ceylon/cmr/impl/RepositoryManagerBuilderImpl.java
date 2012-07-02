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

import com.redhat.ceylon.cmr.api.*;
import com.redhat.ceylon.cmr.spi.ContentTransformer;
import com.redhat.ceylon.cmr.spi.MergeStrategy;
import com.redhat.ceylon.cmr.spi.OpenNode;

import java.io.File;

/**
 * Root repository builder.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RepositoryManagerBuilderImpl extends RepositoryManagerBuilder {

    private RootRepositoryManager repository;
    private Logger log;

    public RepositoryManagerBuilderImpl(Logger log) {
        repository = new RootRepositoryManager(log);
        this.log = log;
        init();
    }

    public RepositoryManagerBuilderImpl(File mainRepository, Logger log) {
        repository = new RootRepositoryManager(mainRepository, log);
        this.log = log;
        init();
    }

    protected void init() {
        getRoot().addService(MergeStrategy.class, new DefaultMergeStrategy());
    }

    private OpenNode getRoot() {
        return repository.getRoot();
    }

    @Override
    public RepositoryBuilder repositoryBuilder() {
        return new RepositoryBuilderImpl(log);
    }

    public RepositoryManagerBuilderImpl mergeStrategy(MergeStrategy strategy) {
        getRoot().addService(MergeStrategy.class, strategy);
        return this;
    }

    public RepositoryManagerBuilderImpl contentTransformer(ContentTransformer transformer) {
        getRoot().addService(ContentTransformer.class, transformer);
        return this;
    }

    public RepositoryManagerBuilderImpl cacheContent() {
        getRoot().addService(ContentTransformer.class, new CachingContentTransformer());
        return this;
    }

    /**
     * It prepends ${ceylon.home} directory to roots, if it exists.
     *
     * @return this
     */
    public RepositoryManagerBuilderImpl addCeylonHome() {
        final String ceylonHome = SecurityActions.getProperty("ceylon.home");
        if (ceylonHome != null) {
            final File repo = new File(ceylonHome, "repo");
            if (repo.exists() && repo.isDirectory())
                prependExternalRoot(new FileContentStore(repo).createRoot());
            else
                log.warning("Invalid CEYLON_HOME/repo: " + repo);
        }
        return this;
    }

    /**
     * It prepends ./modules directory to roots, if it exists.
     *
     * @return this
     */
    public RepositoryManagerBuilderImpl prependModules() {
        final File modules = new File("modules");
        if (modules.exists() && modules.isDirectory())
            prependExternalRoot(new FileContentStore(modules).createRoot());
        else
            log.debug("No such ./modules directory: " + modules);
        return this;
    }

    /**
     * It appends http://modules.ceylon-lang.org to roots, if it exists.
     *
     * @return this
     */
    public RepositoryManagerBuilderImpl addModulesCeylonLangOrg() {
        appendExternalRoot(new RemoteContentStore(RepositoryManager.MODULES_CEYLON_LANG_ORG, log).createRoot());
        return this;
    }

    protected RepositoryManagerBuilderImpl prependExternalRoot(OpenNode externalRoot) {
        repository.prependRepository(new DefaultRepository(externalRoot));
        return this;
    }

    protected RepositoryManagerBuilderImpl appendExternalRoot(OpenNode externalRoot) {
        repository.appendRepository(new DefaultRepository(externalRoot));
        return this;
    }

    public RepositoryManagerBuilderImpl prependRepository(Repository externalRoot) {
        repository.prependRepository(externalRoot);
        return this;
    }

    public RepositoryManagerBuilderImpl appendRepository(Repository externalRoot) {
        repository.appendRepository(externalRoot);
        return this;
    }

    public RepositoryManager buildRepository() {
        return repository;
    }

}
