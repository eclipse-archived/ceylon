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
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

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

        List<File> files = new ArrayList<>();
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

    public List<ArtifactResult> getArtifactResults(ArtifactContext context) throws RepositoryException {
        final List<ArtifactResult> results = new ArrayList<>();
        ArtifactResult result = null;
        TreeSet<String> suffixes = new TreeSet<>(Arrays.asList(context.getSuffixes()));
        while (!suffixes.isEmpty()) {
            // Try to get one of the artifacts
            String[] sfx = suffixes.toArray(new String[suffixes.size()]);
            ArtifactContext ac;
            if (result == null) {
                ac = context;
            } else {
                // We re-use the previous result so we can efficiently
                // retrieve further artifacts from the same module
                ac = context.getSibling(result, sfx);
            }
            result = getArtifactResult(ac);
            
            if (result != null) {
                results.add(result);
                
                // make sure we don't try to get the same artifact twice
                String suffix = ArtifactContext.getSuffixFromFilename(result.artifact().getName());
                // but we don't just remove the one we got, but all of suffixes
                // up to the one we found, because we know the list is ordered
                // and the others were tried first
                while (!suffixes.first().equals(suffix)) {
                    suffixes.remove(suffixes.first());
                }
                
                // TODO this shouldn't be hard-coded here but is to prevent
                // unnecessary queries to remote repositories
                if (suffix.equals(ArtifactContext.CAR)) {
                    // if we found a car we can skip the jar and module descriptors
                    suffixes.remove(ArtifactContext.JAR);
                    suffixes.remove(ArtifactContext.MODULE_PROPERTIES);
                    suffixes.remove(ArtifactContext.MODULE_XML);
                } else if (suffix.equals(ArtifactContext.JAR)
                        || suffix.equals(ArtifactContext.MODULE_PROPERTIES)
                        || suffix.equals(ArtifactContext.MODULE_XML)) {
                    // or the exact opposite
                    suffixes.remove(ArtifactContext.CAR);
                }
            } else {
                // We didn't find anything (this time),
                // we can stop trying more artifact types
                break;
            }
        }
        return results;
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
