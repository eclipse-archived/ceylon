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

package com.redhat.ceylon.cmr.api;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * RepositoryManager API.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface RepositoryManager {
    static final String MODULES_CEYLON_LANG_ORG = "http://modules.ceylon-lang.org/test";
    static final String DEFAULT_MODULE = "default";

    /**
     * Resolve dependencies for the context.
     *
     * @param name    the module name
     * @param version the module version
     * @return all dependencies, null if they cannot be found
     * @throws RepositoryException for any I/O error
     */
    File[] resolve(String name, String version) throws RepositoryException;

    /**
     * Resolve dependencies for the context.
     *
     * @param context the artifact context to resolve
     * @return all dependencies, null if they cannot be found
     * @throws RepositoryException for any I/O error
     */
    File[] resolve(ArtifactContext context) throws RepositoryException;

    File getArtifact(String name, String version) throws RepositoryException;

    File getArtifact(ArtifactContext context) throws RepositoryException;

    ArtifactResult getArtifactResult(String name, String version) throws RepositoryException;

    ArtifactResult getArtifactResult(ArtifactContext context) throws RepositoryException;

    void putArtifact(String name, String version, InputStream content) throws RepositoryException;

    void putArtifact(String name, String version, File content) throws RepositoryException;

    void putArtifact(ArtifactContext context, InputStream content) throws RepositoryException;

    void putArtifact(ArtifactContext context, File content) throws RepositoryException;

    void removeArtifact(String name, String version) throws RepositoryException;

    void removeArtifact(ArtifactContext context) throws RepositoryException;

    /**
     * Gather repositories display strings.
     *
     * @return the display strings
     */
    List<String> getRepositoriesDisplayString();
    
    ModuleResult completeModules(ModuleQuery lookup);
    ModuleVersionResult completeVersions(ModuleVersionQuery lookup);
    
    ModuleSearchResult searchModules(ModuleQuery query);
}
