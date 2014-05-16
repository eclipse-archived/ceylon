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
import java.util.Set;

import com.redhat.ceylon.cmr.spi.Node;

/**
 * Dependency resolver spi / api.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface DependencyResolver {
    /**
     * Resolve dependecies.
     *
     * @param context the context
     * @return dependencies list or null if cannot resolve
     */
    Set<ModuleInfo> resolve(DependencyContext context);

    /**
     * Resolve dependecies.
     *
     * @param result the result
     * @return dependencies list or null if cannot resolve
     */
    Set<ModuleInfo> resolve(ArtifactResult result);

    /**
     * Reads module dependencies from the given file, if supported. This is only implemented to read
     * module.properties and module.xml files.
     * 
     * @param file the file to read to list dependencies 
     * @return dependencies list or null if cannot resolve
     */
    Set<ModuleInfo> resolveFromFile(File file);

    /**
     * Reads module dependencies from the given InputStream, if supported. This is only implemented to read
     * module.properties and module.xml files.
     * 
     * @param stream the stream to read to list dependencies 
     * @return dependencies list or null if cannot resolve
     */
    Set<ModuleInfo> resolveFromInputStream(InputStream stream);

    /**
     * Get descriptor if exists.
     *
     * @param artifact the artifact
     * @return descriptor or null
     */
    Node descriptor(Node artifact);
}

