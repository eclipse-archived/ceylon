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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Abstract repository.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractRepository implements Repository {

    protected Logger log;

    public AbstractRepository(Logger log) {
        this.log = log;
    }

    public File getArtifact(String name, String version) throws IOException {
        ArtifactContext context = new ArtifactContext();
        context.setName(name);
        context.setVersion(version);
        return getArtifact(context);
    }

    public void putArtifact(String name, String version, InputStream content) throws IOException {
        ArtifactContext context = new ArtifactContext();
        context.setName(name);
        context.setVersion(version);
        putArtifact(context, content);
    }

    public void putArtifact(String name, String version, File content) throws IOException {
        ArtifactContext context = new ArtifactContext();
        context.setName(name);
        context.setVersion(version);
        putArtifact(context, content);
    }

    public void putArtifact(ArtifactContext context, File content) throws IOException {
        if (content == null)
            throw new IllegalArgumentException("Null file!");

        if (content.isDirectory())
            putFolder(context, content);
        else
            putArtifact(context, new FileInputStream(content));
    }

    protected void putFolder(ArtifactContext context, File folder) throws IOException {
        throw new IOException("Repository doesn't support folder [" + folder + "] put: " + context);
    }

    public void removeArtifact(String name, String version) throws IOException {
        ArtifactContext context = new ArtifactContext();
        context.setName(name);
        context.setVersion(version);
        removeArtifact(context);
    }
}
