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

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.common.log.Logger;

/**
 * Abstract repository manager.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractRepositoryManager implements RepositoryManager {

    protected Logger log;

    public AbstractRepositoryManager(Logger log) {
        this.log = log;
    }

    /**
     * Flatten.
     *
     * @param result the artifact result
     * @return all dependencies
     * @throws RepositoryException for any I/O error
     */
    public static File[] flatten(ArtifactResult result) throws RepositoryException {
        if (result == null)
            return null;

        List<File> files = new ArrayList<File>();
        recurse(files, result);
        return files.toArray(new File[files.size()]);
    }

    private static void recurse(final List<File> files, final ArtifactResult current) throws RepositoryException {
        files.add(current.artifact());
        for (ArtifactResult ar : current.dependencies())
            recurse(files, ar);
    }

    public File[] resolve(String name, String version) throws RepositoryException {
        final ArtifactContext context = new ArtifactContext(name, version);
        return resolve(context);
    }

    public File[] resolve(ArtifactContext context) throws RepositoryException {
        final ArtifactResult result = getArtifactResult(context);
        return flatten(result);
    }

    public File getArtifact(String name, String version) throws RepositoryException {
        ArtifactContext context = new ArtifactContext();
        context.setName(name);
        context.setVersion(version);
        return getArtifact(context);
    }

    public File getArtifact(ArtifactContext context) throws RepositoryException {
        final ArtifactResult result = getArtifactResult(context);
        return (result != null) ? result.artifact() : null;
    }

    public ArtifactResult getArtifactResult(String name, String version) throws RepositoryException {
        ArtifactContext context = new ArtifactContext();
        context.setName(name);
        context.setVersion(version);
        return getArtifactResult(context);
    }

    public void putArtifact(String name, String version, InputStream content) throws RepositoryException {
        ArtifactContext context = new ArtifactContext();
        context.setName(name);
        context.setVersion(version);
        putArtifact(context, content);
    }

    public void putArtifact(String name, String version, File content) throws RepositoryException {
        ArtifactContext context = new ArtifactContext();
        context.setName(name);
        context.setVersion(version);
        putArtifact(context, content);
    }

    public void putArtifact(ArtifactContext context, File content) throws RepositoryException {
        if (content == null)
            throw new IllegalArgumentException("Null file!");

        if (content.isDirectory())
            putFolder(context, content);
        else
            putArtifact(context, Helper.toInputStream(content));
    }

    protected void putFolder(ArtifactContext context, File folder) throws RepositoryException {
        throw new RepositoryException("RepositoryManager doesn't support folder [" + folder + "] put: " + context);
    }

    public void removeArtifact(String name, String version) throws RepositoryException {
        ArtifactContext context = new ArtifactContext();
        context.setName(name);
        context.setVersion(version);
        removeArtifact(context);
    }
}
