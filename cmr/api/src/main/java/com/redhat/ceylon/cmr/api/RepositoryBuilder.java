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

package com.redhat.ceylon.cmr.api;

import com.redhat.ceylon.cmr.spi.ContentTransformer;
import com.redhat.ceylon.cmr.spi.MergeStrategy;
import com.redhat.ceylon.cmr.spi.OpenNode;

import java.io.File;
import java.lang.reflect.Constructor;

/**
 * Repository builder API.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RepositoryBuilder {
    private final static String DEFAULT_DELEGATE = "com.redhat.ceylon.cmr.impl.RepositoryBuilderImpl";
    private RepositoryBuilder delegate;

    protected RepositoryBuilder() {
    }

    public RepositoryBuilder(Logger log) {
        try {
            Class<?> clazz = getClass().getClassLoader().loadClass(DEFAULT_DELEGATE);
            Constructor ctor = clazz.getConstructor(Logger.class);
            delegate = (RepositoryBuilder) ctor.newInstance(log);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public RepositoryBuilder(File mainRepository, Logger log) {
        try {
            Class<?> clazz = getClass().getClassLoader().loadClass(DEFAULT_DELEGATE);
            Constructor ctor = clazz.getConstructor(File.class, Logger.class);
            delegate = (RepositoryBuilder) ctor.newInstance(mainRepository, log);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected RepositoryBuilder getDelegate() {
        if (delegate == null)
            throw new IllegalArgumentException("Missing method impl / override!");
        return delegate;
    }

    public RootBuilder rootBuilder() {
        return getDelegate().rootBuilder();
    }

    public RepositoryBuilder mergeStrategy(MergeStrategy strategy) {
        getDelegate().mergeStrategy(strategy);
        return this;
    }

    public RepositoryBuilder contentTransformer(ContentTransformer transformer) {
        getDelegate().contentTransformer(transformer);
        return this;
    }

    public RepositoryBuilder cacheContent() {
        getDelegate().cacheContent();
        return this;
    }

    /**
     * It prepends ${ceylon.home} directory to roots, if it exists.
     *
     * @return this
     */
    public RepositoryBuilder addCeylonHome() {
        getDelegate().addCeylonHome();
        return this;
    }

    /**
     * It prepends ./modules directory to roots, if it exists.
     *
     * @return this
     */
    public RepositoryBuilder addModules() {
        getDelegate().addModules();
        return this;
    }

    /**
     * It appends http://modules.ceylon-lang.org to roots, if it exists.
     *
     * @return this
     */
    public RepositoryBuilder addModulesCeylonLangOrg() {
        getDelegate().addModulesCeylonLangOrg();
        return this;
    }

    public RepositoryBuilder prependExternalRoot(OpenNode externalRoot) {
        getDelegate().prependExternalRoot(externalRoot);
        return this;
    }

    public RepositoryBuilder appendExternalRoot(OpenNode externalRoot) {
        getDelegate().appendExternalRoot(externalRoot);
        return this;
    }

    public RepositoryBuilder prependRepository(Repository external) {
        getDelegate().prependRepository(external);
        return this;
    }

    public RepositoryBuilder appendRepository(Repository external) {
        getDelegate().appendRepository(external);
        return this;
    }

    public RepositoryManager buildRepository() {
        return getDelegate().buildRepository();
    }
}
