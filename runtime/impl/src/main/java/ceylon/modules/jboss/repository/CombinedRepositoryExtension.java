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

import org.jboss.modules.ResourceLoader;

import ceylon.lang.modules.ModuleName;
import ceylon.lang.modules.ModuleVersion;
import ceylon.modules.api.repository.CombinedRepository;
import ceylon.modules.spi.repository.Repository;

/**
 * Combined repository extension.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class CombinedRepositoryExtension extends CombinedRepository implements RepositoryExtension
{
   public CombinedRepositoryExtension(Repository... repositories)
   {
      super(repositories);
   }

   public ResourceLoader createResourceLoader(ModuleName name, ModuleVersion version, File file)
   {
      RepositoryExtension repository = (RepositoryExtension) current();
      if (repository == null)
         return null;

      return repository.createResourceLoader(name, version, file);
   }
}
