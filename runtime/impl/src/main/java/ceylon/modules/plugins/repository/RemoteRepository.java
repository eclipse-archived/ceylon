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
import java.util.Map;

import org.jboss.modules.ResourceLoader;

import ceylon.lang.modules.Module;
import ceylon.lang.modules.ModuleName;
import ceylon.lang.modules.ModuleVersion;
import ceylon.modules.spi.repository.Repository;

/**
 * Remote repository.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RemoteRepository implements Repository
{
   public RemoteRepository(String path, Map<String, String> args)
   {
   }

   public void begin()
   {
   }

   public void end()
   {
   }

   public File findModule(ModuleName name, ModuleVersion version)
   {
      return null; // TODO
   }

   public Module readModule(ModuleName name, File moduleFile) throws Exception
   {
      return null;  // TODO
   }

   public ResourceLoader createResourceLoader(ModuleName name, ModuleVersion version, File file)
   {
      return null;  // TODO
   }
}
