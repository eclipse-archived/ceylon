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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Output manager.
 * Treat compiler output differently from runtime repos.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class OutputRepositoryManager extends AbstractRepositoryManager {

    private final RepositoryManager output;
    private final RepositoryManager manager; // default root manager

    private static File getOutputDir() {
        return new File("output"); // TODO
    }

    public OutputRepositoryManager(RepositoryManager manager, Logger log) {
        this(getOutputDir(), manager, log);
    }

    public OutputRepositoryManager(File outputDir, RepositoryManager manager, Logger log) {
        this(new RootRepositoryManager(outputDir, log), manager, log);
    }

    public OutputRepositoryManager(RepositoryManager output, RepositoryManager manager, Logger log) {
        super(log);
        if (output == null)
            throw new IllegalArgumentException("Output is null!");
        if (manager == null)
            throw new IllegalArgumentException("Manager is null!");

        this.output = output;
        this.manager = manager;
    }

    @Override
    public List<Repository> getRepositories() {
        List<Repository> repos = new ArrayList<>();
        repos.addAll(output.getRepositories());
        repos.addAll(manager.getRepositories());
        return repos;
    }

    public List<String> getRepositoriesDisplayString() {
        List<String> displayStrings = new ArrayList<String>();
        displayStrings.addAll(output.getRepositoriesDisplayString());
        displayStrings.addAll(manager.getRepositoriesDisplayString());
        return displayStrings;
    }

    public ArtifactResult getArtifactResult(ArtifactContext context) throws RepositoryException {
        final ArtifactResult result = output.getArtifactResult(context);
        if (result != null) {
            return result;
        } else {
            return manager.getArtifactResult(context);
        }
    }

    public void putArtifact(ArtifactContext context, InputStream content) throws RepositoryException {
        output.putArtifact(context, content);
    }

    public void putArtifact(ArtifactContext context, File content) throws RepositoryException {
        output.putArtifact(context, content);
    }

    public void removeArtifact(ArtifactContext context) throws RepositoryException {
        output.removeArtifact(context);
    }

    @Override
    public String toString() {
        return "OutputRepositoryManager: " + output;
    }
    
    @Override
    public ModuleSearchResult completeModules(ModuleQuery query) {
        return new ModuleSearchResult();
    }
    
    @Override
    public ModuleVersionResult completeVersions(ModuleVersionQuery query) {
        return new ModuleVersionResult(query.getName());
    }
    
    @Override
    public ModuleSearchResult searchModules(ModuleQuery query) {
        return new ModuleSearchResult();
    }
    
    @Override
    public void refresh(boolean recurse) {
        output.refresh(recurse);
        manager.refresh(recurse);
    }
}
