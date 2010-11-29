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

package ceylon.modules.spi.repository;

import java.io.File;

import org.jboss.modules.ResourceLoader;

import ceylon.lang.modules.Module;
import ceylon.lang.modules.ModuleName;
import ceylon.lang.modules.ModuleVersion;

/**
 * Ceylon Module repository.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface Repository
{
   /**
    * Begin lookup.
    */
   void begin();

   /**
    * End lookup.
    */
   void end();

   /**
    * Find the matching module.
    * Null if no such module is found.
    *
    * @param name the module name, must not be null
    * @param version the module version, can be null
    * @return module's file or null if not found
    */
   File findModule(ModuleName name, ModuleVersion version);

   /**
    * Initial read of the module instance.
    * Should not load runnable.
    *
    * @param name the module name
    * @param moduleFile the module file, must not be null
    * @return initial read of the module instance or null if no descriptor
    * @throws Exception for any error
    */
   Module readModule(ModuleName name, File moduleFile) throws Exception;

   /**
    * Create resource loader.
    * Null if no module or module descriptior is found.
    *
    * @param name the module name, must not be null
    * @param version the module version, can be null
    * @param file the module file
    *
    * @return new resource loader instance or null if not found
    */
   ResourceLoader createResourceLoader(ModuleName name, ModuleVersion version, File file);
}
