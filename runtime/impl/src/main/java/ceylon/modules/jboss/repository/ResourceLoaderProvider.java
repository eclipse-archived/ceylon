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

package ceylon.modules.jboss.repository;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

import com.redhat.ceylon.cmr.api.RepositoryManager;
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
            JarFile jarFile = new JarFile(moduleFile);
            String rootName = moduleFile.getName();
            return ResourceLoaders.createJarResourceLoader(rootName, jarFile);
        }
    }
}
