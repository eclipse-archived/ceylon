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

package ceylon.modules.api.runtime;

import java.lang.reflect.Method;
import java.util.Map;

import ceylon.lang.modules.Module;
import ceylon.lang.modules.ModuleName;
import ceylon.lang.modules.ModuleVersion;
import ceylon.modules.spi.Constants;

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

   public void execute(Map<String, String> args) throws Exception
   {
      String exe = args.get(Constants.MODULE.toString());
      if (exe == null)
         throw new IllegalArgumentException("No initial module defined: " + args);

      int p = exe.indexOf("/");
      if (p == 0)
         throw new IllegalArgumentException("Missing runnable info: " + exe);
      if (p == exe.length() - 1)
         throw new IllegalArgumentException("Missing version info: " + exe);

      String name = exe.substring(0, p > 0 ? p : exe.length());
      ModuleVersion version = (p > 0 ? ModuleVersion.parseVersion(exe.substring(p + 1)) : ModuleVersion.DEFAULT_VERSION);

      ClassLoader cl = createClassLoader(new ModuleName(name), version, args);
      String moduleClassName = name + ".Module"; // TODO -- allow for top level method
      ceylon.lang.modules.Module runtimeModule = loadModule(cl, moduleClassName);
      if (runtimeModule == null)
         throw new IllegalArgumentException("Something went very wrong, missing runtime module!");

      ceylon.lang.Runnable runnable = runtimeModule.getProcess();
      if (runnable != null)
         runnable.run(new ceylon.lang.Process(args));
   }
}
