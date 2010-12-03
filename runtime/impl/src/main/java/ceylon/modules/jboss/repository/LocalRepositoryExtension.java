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
import java.util.jar.JarFile;

import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.PathFilters;
import org.jboss.modules.ResourceLoader;

import ceylon.lang.modules.ModuleName;
import ceylon.lang.modules.ModuleVersion;
import ceylon.modules.api.repository.LocalRepository;

/**
 * Local repository.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class LocalRepositoryExtension extends LocalRepository implements RepositoryExtension
{
   protected LocalRepositoryExtension()
   {
      super();
   }

   public LocalRepositoryExtension(String path)
   {
      super(path);
   }

   public ResourceLoader createResourceLoader(ModuleName name, ModuleVersion version, File file)
   {
      ModuleIdentifier moduleIdentifier = ModuleIdentifier.create(name.getName(), version.toString());
      try
      {
         String rootName = moduleIdentifier + ".car"; // TODO -- ok?
         return new CarFileResourceLoader(new JarFile(file), rootName, PathFilters.getDefaultExportFilter());
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }
}
