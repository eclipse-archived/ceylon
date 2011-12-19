/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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