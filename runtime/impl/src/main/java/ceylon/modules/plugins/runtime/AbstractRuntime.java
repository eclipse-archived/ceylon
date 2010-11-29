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

package ceylon.modules.plugins.runtime;

import java.lang.reflect.Method;
import java.util.Map;

import org.jboss.modules.ModuleLoader;

import ceylon.lang.modules.Module;
import ceylon.modules.plugins.repository.RepositoryFactory;
import ceylon.modules.spi.repository.Repository;

/**
 * Abstract Ceylon Modules runtime.
 * Useful for potential extension.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractRuntime implements ceylon.modules.spi.runtime.Runtime
{
   /**
    * Load module instance.
    *
    * @param cl the classloader used to load the module descriptor.
    * @param moduleClassName the module descriptor class name
    * @return new module instance or null if no such descriptor
    * @throws Exception for any error
    */
   public static Module loadModule(ClassLoader cl, String moduleClassName) throws Exception
   {
      Class<?> moduleClass;
      try
      {
         moduleClass = cl.loadClass(moduleClassName);
      }
      catch (ClassNotFoundException ignored)
      {
         return null; // looks like no such Module class is available
      }

      Object module = moduleClass.newInstance();
      Method getModule = module.getClass().getMethod("getModule");
      return (Module) getModule.invoke(module);
   }

   public Repository createRepository(Map<String, String> args)
   {
      return RepositoryFactory.createRepository(args);
   }

   public ModuleLoader createModuleLoader(Map<String, String> args)
   {
      Repository repository = createRepository(args);
      return new CeylonModuleLoader(repository);
   }
}
