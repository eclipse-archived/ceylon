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
import java.io.IOException;
import java.io.InputStream;

/**
 * Repository API.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface Repository {
    static final String MODULES_CEYLON_LANG_ORG = "http://modules.ceylon-lang.org";
    static final String NO_VERSION = "**NO_VERSION**";

    File getArtifact(String name, String version) throws IOException;

    File getArtifact(ArtifactContext context) throws IOException;

    void putArtifact(String name, String version, InputStream content) throws IOException;

    void putArtifact(String name, String version, File content) throws IOException;

    void putArtifact(ArtifactContext context, InputStream content) throws IOException;

    void putArtifact(ArtifactContext context, File content) throws IOException;

    void removeArtifact(String name, String version) throws IOException;

    void removeArtifact(ArtifactContext context) throws IOException;
}
