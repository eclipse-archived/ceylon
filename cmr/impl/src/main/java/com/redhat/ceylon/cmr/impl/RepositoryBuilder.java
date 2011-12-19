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

import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.spi.ContentTransformer;
import com.redhat.ceylon.cmr.spi.MergeStrategy;
import com.redhat.ceylon.cmr.spi.OpenNode;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Root repository builder.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RepositoryBuilder {

    private RootRepository repository;

    public RepositoryBuilder() {
        repository = new RootRepository();
        init();
    }

    public RepositoryBuilder(File mainRepository) {
        repository = new RootRepository(mainRepository);
        init();
    }

    protected void init() {
        getRoot().addService(MergeStrategy.class, new DefaultMergeStrategy());
    }

    private OpenNode getRoot() {
        return repository.getRoot();
    }

    public RepositoryBuilder mergeStrategy(MergeStrategy strategy) {
        getRoot().addService(MergeStrategy.class, strategy);
        return this;
    }

    public RepositoryBuilder contentTransformer(ContentTransformer transformer) {
        getRoot().addService(ContentTransformer.class, transformer);
        return this;
    }

    public RepositoryBuilder cacheContent() {
        getRoot().addService(ContentTransformer.class, new CachingContentTransformer());
        return this;
    }

    public RepositoryBuilder addCeylonHome() {
        final String ceylonHome = SecurityActions.getProperty("ceylon.home");
        if (ceylonHome != null) {
            final File repo = new File(ceylonHome, "repo");
            if (repo.exists() && repo.isDirectory())
                prependExternalRoot(new FileContentStore(repo).createRoot());
            else
                Logger.getLogger(RepositoryBuilder.class.getName()).warning("Invalid CEYLON_HOME/repo: " + repo);
        }
        return this;
    }

    public RepositoryBuilder addModules() {
        final File modules = new File("modules");
        if (modules.exists() && modules.isDirectory())
            appendExternalRoot(new FileContentStore(modules).createRoot());
        else
            Logger.getLogger(RepositoryBuilder.class.getName()).warning("No such ./modules directory: " + modules);
        return this;
    }

    public RepositoryBuilder addModulesCeylonLangOrg() {
        InputStream is = null;
        try {
            final URL url = new URL(Repository.MODULES_CEYLON_LANG_ORG);
            is = url.openStream();
            appendExternalRoot(new RemoteContentStore(Repository.MODULES_CEYLON_LANG_ORG).createRoot());
        } catch (Exception ignored) {
            Logger.getLogger(RepositoryBuilder.class.getName()).info("Ceylon repository '" + Repository.MODULES_CEYLON_LANG_ORG + "' not yet available.");
        } finally {
            IOUtils.safeClose(is);
        }
        return this;
    }

    public RepositoryBuilder prependExternalRoot(OpenNode externalRoot) {
        repository.prependExternalRoot(externalRoot);
        return this;
    }

    public RepositoryBuilder appendExternalRoot(OpenNode externalRoot) {
        repository.appendExternalRoot(externalRoot);
        return this;
    }

    public Repository buildRepository() {
        return repository;
    }

}
