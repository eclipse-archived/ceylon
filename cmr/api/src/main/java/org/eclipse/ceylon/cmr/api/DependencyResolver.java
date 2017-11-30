/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.api;

import java.io.File;
import java.io.InputStream;

import org.eclipse.ceylon.cmr.spi.Node;
import org.eclipse.ceylon.model.cmr.ArtifactResult;

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

