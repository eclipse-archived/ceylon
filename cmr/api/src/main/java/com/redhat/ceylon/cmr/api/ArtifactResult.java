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
import java.util.List;

/**
 * Artifact result.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface ArtifactResult {
    /**
     * Get name.
     *
     * @return the artifact name.
     */
    String name();

    /**
     * Get version.
     *
     * @return the version.
     */
    String version();

    /**
     * Get import type.
     *
     * @return the import type
     */
    ImportType importType();

    /**
     * The result type.
     *
     * @return the type
     */
    ArtifactResultType type();

    /**
     * Get visibility type.
     *
     * @return visibility type
     */
    VisibilityType visibilityType();

    /**
     * The requested artifact.
     *
     * @return the requested artifact
     * @throws RepositoryException for any I/O error
     */
    File artifact() throws RepositoryException;

    /**
     * Dependencies.
     * <p/>
     * They could be lazily recursively fetched
     * or they could be fetched in one go.
     *
     * @return dependencies, empty list if none
     * @throws RepositoryException for any I/O error
     */
    List<ArtifactResult> dependencies() throws RepositoryException;
}
