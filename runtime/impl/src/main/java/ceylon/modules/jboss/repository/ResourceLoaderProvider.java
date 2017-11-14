/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.modules.jboss.repository;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ResourceLoader;
import org.jboss.modules.ResourceLoaders;

/**
 * Provide proper resource loader.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ResourceLoaderProvider {
    /**
     * Get resource loader.
     *
     * @param moduleIdentifier the module identifier
     * @param repository       the repository
     * @param moduleFile       the module file
     * @return new resource loader
     * @throws IOException for any I/O error
     */
    public static ResourceLoader getResourceLoader(
            ModuleIdentifier moduleIdentifier,
            RepositoryManager repository,
            File moduleFile) throws IOException {
        File classesRoot = null; // TODO repository.getCompileDirectory();
        if (classesRoot != null) {
            return new SourceResourceLoader(moduleFile, classesRoot, "");
        } else {
            try {
                JarFile jarFile = new JarFile(moduleFile);
                String rootName = moduleFile.getName();
                return ResourceLoaders.createJarResourceLoader(rootName, jarFile);
            }catch(ZipException x){
                ZipException x2 = new ZipException(x.getMessage()+": "+moduleFile);
                x2.initCause(x);
                throw x2;
            }
        }
    }
}
