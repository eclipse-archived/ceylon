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

package ceylon.modules.plugins.repository;

import java.io.File;
import java.util.Arrays;

import org.jboss.modules.ResourceLoader;

import ceylon.lang.modules.Module;
import ceylon.lang.modules.ModuleName;
import ceylon.lang.modules.ModuleVersion;
import ceylon.modules.spi.repository.Repository;

/**
 * Combined repository.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class CombinedRepository implements Repository
{
   private Repository[] repositories;
   private static ThreadLocal<Repository> current = new ThreadLocal<Repository>();

   public CombinedRepository(Repository... repositories)
   {
      if (repositories == null || repositories.length == 0)
         throw new IllegalArgumentException("Illegal repositories: " + Arrays.toString(repositories));
      this.repositories = repositories;
   }

   public void begin()
   {
   }

   public void end()
   {
      Repository repository = current.get();
      if (repository != null)
      {
         repository.end(); // end the current one
         current.remove();
      }
   }

   public File findModule(ModuleName name, ModuleVersion version)
   {
      for (Repository repository : repositories)
      {
         repository.begin();
         try
         {
            File file = repository.findModule(name, version);
            if (file != null)
            {
               current.set(repository);
               return file;
            }
         }
         finally
         {
            // end it since it's not used
            Repository c = current.get();
            if (c == null)
               repository.end();
         }
      }
      return null;
   }

   public Module readModule(ModuleName name, File moduleFile) throws Exception
   {
      Repository repository = current.get();
      if (repository == null)
         return null;

      return repository.readModule(name, moduleFile);      
   }

   public ResourceLoader createResourceLoader(ModuleName name, ModuleVersion version, File file)
   {
      Repository repository = current.get();
      if (repository == null)
         return null;

      return repository.createResourceLoader(name, version, file);
   }
}
