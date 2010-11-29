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

import java.util.Map;

import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoader;
import org.jboss.modules.SimpleModuleLoaderSelector;

import ceylon.lang.modules.ModuleVersion;
import ceylon.modules.spi.Constants;

/**
 * Default Ceylon Modules runtime.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class DefaultRuntime extends AbstractRuntime
{
   public void execute(Map<String, String> args) throws Exception
   {
      String exe = args.get(Constants.MODULE.toString());
      int p = exe.indexOf("/");
      if (p == 0)
         throw new IllegalArgumentException("Missing runnable info: " + exe);
      if (p == exe.length() - 1)
         throw new IllegalArgumentException("Missing version info: " + exe);

      String name = exe.substring(0, p > 0 ? p : exe.length());
      String version = (p > 0 ? exe.substring(p + 1) : ModuleVersion.DEFAULT_VERSION.toString());
      String moduleClassName = name + ".Module"; // TODO -- allow for top level method

      ModuleIdentifier moduleIdentifier = ModuleIdentifier.fromString(name + ":" + version);
      ModuleLoader moduleLoader = createModuleLoader(args);
      Module.setModuleLoaderSelector(new SimpleModuleLoaderSelector(moduleLoader));
      Module module = moduleLoader.loadModule(moduleIdentifier);
      ClassLoader cl = SecurityActions.getClassLoader(module);

      ceylon.lang.modules.Module runtimeModule = loadModule(cl, moduleClassName);
      ceylon.lang.Runnable runnable = runtimeModule.getProcess();
      if (runnable != null)
         runnable.run(new ceylon.lang.Process(args));
   }
}
