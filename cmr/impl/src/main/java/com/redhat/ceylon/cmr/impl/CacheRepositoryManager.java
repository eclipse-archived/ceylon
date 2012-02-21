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

package com.redhat.ceylon.cmr.impl;

import com.redhat.ceylon.cmr.api.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache manager.
 * Cache remote lookups - hits and misses.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class CacheRepositoryManager extends AbstractRepositoryManager {

    private File root = getCacheDir();
    private Map<Repository, RepositoryManager> repositories = new ConcurrentHashMap<Repository, RepositoryManager>();
    private AbstractNodeRepositoryManager manager;

    private static File getCacheDir() {
        final File dir = new File(System.getProperty("user.home"), ".ceylon/cache");
        if (dir.exists() == false && dir.mkdirs() == false)
            throw new IllegalArgumentException("Cannot create Ceylon cache dir: " + dir);
        return dir;
    }

    public CacheRepositoryManager(Logger log) {
        super(log);
    }

    public ArtifactResult getArtifactResult(ArtifactContext context) throws IOException {
        return null;
    }

    public void putArtifact(ArtifactContext context, InputStream content) throws IOException {
    }

    public void removeArtifact(ArtifactContext context) throws IOException {
    }

    protected void addRepository(Repository repository) {
        if (repository.getRoot().isRemote()) {
            final FileContentStore fileContentStore = new FileContentStore(new File(root, "/todo")); // TODO
            final RepositoryManager rm = new SimpleRepositoryManager(fileContentStore, log);
            repositories.put(repository, rm);
        }
    }

    protected void prependRepository(Repository external) {
        manager.prependRepository(external);
        addRepository(external);
    }

    protected void appendRepository(Repository external) {
        manager.appendRepository(external);
        addRepository(external);
    }

    protected void removeRepository(Repository external) {
        manager.removeRepository(external);
        repositories.remove(external);
    }

    @Override
    public String toString() {
        return "CacheRepositoryManager: " + root;
    }
}
