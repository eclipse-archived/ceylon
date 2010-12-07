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

package ceylon.modules.api.repository;

import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;

import ceylon.lang.modules.ModuleName;
import ceylon.lang.modules.ModuleVersion;
import ceylon.modules.spi.repository.Repository;

/**
 * Abstract repository.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractRepository implements Repository
{
   public void begin()
   {
   }

   public void end()
   {
   }

   public File getCompileDirectory()
   {
      return null;
   }

   /**
    * Get system property.
    *
    * @param key the key
    * @return system property or null if it doesn't exist
    */
   protected static String getSystemProperty(final String key)
   {
      return AccessController.doPrivileged(new PrivilegedAction<String>()
      {
         @Override
         public String run()
         {
            return System.getProperty(key);
         }
      });
   }

   /**
    * Get property from args.
    *
    * @param args the initial arguments
    * @param key the property key
    * @param defaultValue the default value
    * @return the property
    */
   protected static String getProperty(Map<String, String> args, String key, String defaultValue)
   {
      String value = args.get(key);
      return value != null ? value : defaultValue;
   }

   /**
    * Get module path.
    *
    * @param name the module name
    * @param version the module version
    * @return module's path
    */
   protected String toPathString(ModuleName name, ModuleVersion version)
   {
      final StringBuilder builder = new StringBuilder();
      builder.append(name.getName().replace('.', File.separatorChar));
      if (ModuleVersion.DEFAULT_VERSION.equals(version) == false)
         builder.append(File.separatorChar).append(version);
      builder.append(File.separatorChar);
      return builder.toString();
   }

   /**
    * Get module file.
    *
    * @param name the module name
    * @param version the module version
    * @return module's file
    */
   protected String toFileString(ModuleName name, ModuleVersion version)
   {
      final StringBuilder builder = new StringBuilder();
      builder.append(name.getName());
      if (ModuleVersion.DEFAULT_VERSION.equals(version) == false)
         builder.append("-").append(version);
      builder.append(".car");
      return builder.toString();
   }
}
