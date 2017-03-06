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
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.common.log.Logger;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.RepositoryException;

/**
 * Abstract repository manager.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractRepositoryManager implements RepositoryManager {

    protected final Logger log;
    protected Overrides overrides;

    public AbstractRepositoryManager(Logger log, Overrides overrides) {
        this.log = log;
        this.overrides=overrides;
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

    @Override
    public File[] resolve(String namespace, String name, String version) throws RepositoryException {
        final ArtifactContext context = new ArtifactContext(namespace, name, version);
        return resolve(context);
    }

    @Override
    public File[] resolve(ArtifactContext context) throws RepositoryException {
        final ArtifactResult result = getArtifactResult(context);
        return flatten(result);
    }

    @Override
    public File getArtifact(String namespace, String name, String version) throws RepositoryException {
        final ArtifactContext context = new ArtifactContext(namespace, name, version);
        return getArtifact(context);
    }

    @Override
    public File getArtifact(ArtifactContext context) throws RepositoryException {
        final ArtifactResult result = getArtifactResult(context);
        return (result != null) ? result.artifact() : null;
    }

    @Override
    public ArtifactResult getArtifactResult(String namespace, String name, String version) throws RepositoryException {
        final ArtifactContext context = new ArtifactContext(namespace, name, version);
        return getArtifactResult(context);
    }

    protected ArtifactResult getFolder(ArtifactContext context, Node node) throws RepositoryException {
        throw new RepositoryException("RepositoryManager doesn't support folder get: " + context);
    }

    protected ArtifactResult artifactNotFound(ArtifactContext context) throws RepositoryException {
        return null;
    }
    
    @Override
    public List<ArtifactResult> getArtifactResults(ArtifactContext context) throws RepositoryException {
        final List<ArtifactResult> results = new ArrayList<>();
        ArtifactResult result = null;
        LinkedHashSet<String> suffixes = new LinkedHashSet<>(Arrays.asList(context.getSuffixes()));
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
                Iterator<String> iter = suffixes.iterator();
                while (iter.hasNext() && !suffix.equals(iter.next())) {
                    iter.remove();
                }
                suffixes.remove(suffix);
                
                // TODO this shouldn't be hard-coded here but is to prevent
                // unnecessary queries to remote repositories
                boolean isLangMod = "ceylon.language".equals(context.getName());
                if (suffix.equals(ArtifactContext.CAR)) {
                    // If we found a car we can skip the jar and module descriptors.
                    // But we make an exception for the language module because it's
                    // the only .car file that also has a module.xml descriptor
                    suffixes.remove(ArtifactContext.JAR);
                    suffixes.remove(ArtifactContext.MODULE_PROPERTIES);
                    if (!isLangMod) {
                        suffixes.remove(ArtifactContext.MODULE_XML);
                    }
                } else if (suffix.equals(ArtifactContext.JAR)
                        || suffix.equals(ArtifactContext.MODULE_PROPERTIES)
                        || suffix.equals(ArtifactContext.MODULE_XML)) {
                    // or the exact opposite (and again an exception is made for the language module)
                    if (!isLangMod) {
                        suffixes.remove(ArtifactContext.CAR);
                    }
                }
            } else {
                // We didn't find anything (this time),
                // we can stop trying more artifact types
                break;
            }
        }
        return results;
    }

    @Override
    public void putArtifact(String namespace, String name, String version, InputStream content) throws RepositoryException {
        final ArtifactContext context = new ArtifactContext(namespace, name, version);
        putArtifact(context, content);
    }

    @Override
    public void putArtifact(String namespace, String name, String version, File content) throws RepositoryException {
        final ArtifactContext context = new ArtifactContext(namespace, name, version);
        putArtifact(context, content);
    }

    @Override
    public void putArtifact(ArtifactContext context, File content) throws RepositoryException {
        if (content == null)
            throw new IllegalArgumentException("Null file!");

        if (!isSameFile(context, content)) {
            // Not the same file so we can add it
            if (content.isDirectory())
                putFolder(context, content);
            else
                putArtifact(context, Helper.toInputStream(content));
        } else {
            // They are the same file so we skip it
            log.debug("  -> [skipping] source and destination are the same");
        }
    }

    public abstract boolean isSameFile(ArtifactContext context, File srcFile) throws RepositoryException;
    
    protected void putFolder(ArtifactContext context, File folder) throws RepositoryException {
        throw new RepositoryException("RepositoryManager doesn't support folder [" + folder + "] put: " + context);
    }

    @Override
    public void removeArtifact(String namespace, String name, String version) throws RepositoryException {
        final ArtifactContext context = new ArtifactContext(namespace, name, version);
        removeArtifact(context);
    }
    
    @Override
    public Overrides getOverrides() {
        return overrides;
    }
    
    @Override
    public void setOverrides(Overrides overrides) {
        this.overrides = overrides;
    }
}
