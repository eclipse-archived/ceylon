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

package ceylon.modules.jboss.runtime;

import java.util.Map;

import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoader;

import ceylon.lang.modules.ModuleVersion;
import ceylon.modules.spi.Constants;

/**
 * Default Ceylon Modules runtime.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class JBossRuntime extends AbstractJBossRuntime
{
   public ClassLoader createClassLoader(Map<String, String> args) throws Exception
   {
      String exe = args.get(Constants.MODULE.toString());
      int p = exe.indexOf("/");
      if (p == 0)
         throw new IllegalArgumentException("Missing runnable info: " + exe);
      if (p == exe.length() - 1)
         throw new IllegalArgumentException("Missing version info: " + exe);

      String name = exe.substring(0, p > 0 ? p : exe.length());
      String version = (p > 0 ? exe.substring(p + 1) : ModuleVersion.DEFAULT_VERSION.toString());

      ModuleIdentifier moduleIdentifier = ModuleIdentifier.fromString(name + ":" + version);
      ModuleLoader moduleLoader = createModuleLoader(args);
      Module module = moduleLoader.loadModule(moduleIdentifier);
      return SecurityActions.getClassLoader(module);
   }
}
