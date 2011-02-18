/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package ceylon.modules.jboss.repository;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ResourceLoader;
import org.jboss.modules.ResourceLoaders;
import org.jboss.modules.filter.PathFilters;

import ceylon.modules.spi.repository.Repository;

/**
 * Provide proper resource loader.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ResourceLoaderProvider
{
   /**
    * Get resource loader.
    *
    * @param moduleIdentifier the module identifier
    * @param repository the repository
    * @param moduleFile the module file
    * @return new resource loader
    * @throws IOException for any I/O error
    */
   public static ResourceLoader getResourceLoader(
         ModuleIdentifier moduleIdentifier,
         Repository repository,
         File moduleFile) throws IOException
   {
      File classesRoot = repository.getCompileDirectory();
      if (classesRoot != null)
      {
         return new SourceResourceLoader(moduleFile, classesRoot, "", PathFilters.acceptAll());
      }
      else
      {
         JarFile jarFile = new JarFile(moduleFile);
         String rootName = moduleIdentifier + ".car"; // TODO -- ok?
         return ResourceLoaders.createJarResourceLoader(rootName, jarFile);
      }
   }
}
