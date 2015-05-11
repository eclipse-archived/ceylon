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
import com.redhat.ceylon.model.cmr.ArtifactResult;

/**
 * Dependency resolver spi / api.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface DependencyResolver {
    /**
     * Resolve dependencies.
     *
     * @param context the context
     * @param overrides the version overrides or null
     * @return module info or null if cannot resolve
     */
    ModuleInfo resolve(DependencyContext context, Overrides overrides);

    /**
     * Resolve dependencies.
     *
     * @param result the result
     * @param overrides the version overrides or null
     * @return module info or null if cannot resolve
     */
    ModuleInfo resolve(ArtifactResult result, Overrides overrides);

    /**
     * Reads module dependencies from the given file, if supported. This is only implemented to read
     * module.properties and module.xml files.
     * 
     * @param file the file to read to list dependencies 
     * @param name the module name we're reading the descriptor for
     * @param version the module version we're reading the descriptor for
     * @param overrides the version overrides or null
     * @return dependencies list or null if cannot resolve
     */
    ModuleInfo resolveFromFile(File file, String name, String version, Overrides overrides);

    /**
     * Reads module dependencies from the given InputStream, if supported. This is only implemented to read
     * module.properties and module.xml files.
     * 
     * @param stream the stream to read to list dependencies 
     * @param name the module name we're reading the descriptor for
     * @param version the module version we're reading the descriptor for
     * @param overrides the version overrides or null
     * @return dependencies list or null if cannot resolve
     */
    ModuleInfo resolveFromInputStream(InputStream stream, String name, String version, Overrides overrides);

    /**
     * Get descriptor if exists.
     *
     * @param artifact the artifact
     * @return descriptor or null
     */
    Node descriptor(Node artifact);
}

