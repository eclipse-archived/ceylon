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

import ceylon.lang.modules.ModuleName;
import ceylon.lang.modules.ModuleVersion;
import ceylon.modules.api.runtime.AbstractRuntime;
import ceylon.modules.jboss.repository.RepositoryExtension;
import ceylon.modules.jboss.repository.RepositoryExtensionFactory;

/**
 * Abstract Ceylon JBoss Modules runtime.
 * Useful for potential extension.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractJBossRuntime extends AbstractRuntime
{
   public ClassLoader createClassLoader(ModuleName name, ModuleVersion version, Map<String, String> args) throws Exception
   {
      ModuleLoader moduleLoader = createModuleLoader(args);
      ModuleIdentifier moduleIdentifier = ModuleIdentifier.fromString(name + ":" + version);
      Module module = moduleLoader.loadModule(moduleIdentifier);
      return SecurityActions.getClassLoader(module);
   }
   /**
    * Get repository extension.
    *
    * @param args the args
    * @return repository extension
    */
   protected RepositoryExtension createRepository(Map<String, String> args)
   {
      return RepositoryExtensionFactory.createRepository(args);
   }

   /**
    * Create module loader.
    *
    * @param args the args
    * @return the module loader
    */
   protected abstract ModuleLoader createModuleLoader(Map<String, String> args);
}
