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

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.List;

/**
 * RepositoryManager builder API.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RepositoryManagerBuilder {
    private final static String DEFAULT_DELEGATE = "com.redhat.ceylon.cmr.impl.RepositoryManagerBuilderImpl";
    private RepositoryManagerBuilder delegate;

    protected RepositoryManagerBuilder() {
    }

    protected Class<? extends RepositoryManagerBuilder> getDelegateClass() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        if (classLoader == null)
            classLoader = ClassLoader.getSystemClassLoader();

        //noinspection unchecked
        return (Class<? extends RepositoryManagerBuilder>) classLoader.loadClass(DEFAULT_DELEGATE);
    }

    public RepositoryManagerBuilder(Logger log, boolean offline) {
        try {
            Constructor<? extends RepositoryManagerBuilder> ctor = getDelegateClass().getConstructor(Logger.class, boolean.class);
            delegate = ctor.newInstance(log, offline);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public RepositoryManagerBuilder(File mainRepository, Logger log, boolean offline) {
        try {
            Constructor<? extends RepositoryManagerBuilder> ctor = getDelegateClass().getConstructor(File.class, Logger.class, boolean.class);
            delegate = ctor.newInstance(mainRepository, log, offline);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected RepositoryManagerBuilder getDelegate() {
        if (delegate == null)
            throw new IllegalArgumentException("Missing method impl / override!");
        return delegate;
    }

    public RepositoryBuilder repositoryBuilder() {
        return getDelegate().repositoryBuilder();
    }

    public RepositoryManagerBuilder mergeStrategy(MergeStrategy strategy) {
        getDelegate().mergeStrategy(strategy);
        return this;
    }

    public RepositoryManagerBuilder contentTransformer(ContentTransformer transformer) {
        getDelegate().contentTransformer(transformer);
        return this;
    }

    public RepositoryManagerBuilder cacheContent() {
        getDelegate().cacheContent();
        return this;
    }

    public RepositoryManagerBuilder prependRepository(Repository external) {
        getDelegate().prependRepository(external);
        return this;
    }

    public RepositoryManagerBuilder appendRepository(Repository external) {
        getDelegate().appendRepository(external);
        return this;
    }

    public List<String> getRepositoriesDisplayString() {
        return getDelegate().getRepositoriesDisplayString();
    }

    public RepositoryManager buildRepository() {
        return getDelegate().buildRepository();
    }
}
