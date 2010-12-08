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

package eu.cloud.clazz;

import ceylon.lang.modules.Import;
import ceylon.lang.modules.ModuleName;
import ceylon.lang.modules.ModuleVersion;
import ceylon.lang.modules.PathFilter;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Module
{
   public ceylon.lang.modules.Module getModule()
   {
      ModuleName name = new ModuleName("eu.cloud.clazz");
      ModuleVersion version = new ModuleVersion(1, 0, 0, "GA");
      ceylon.lang.Runnable runnable = new ceylon.lang.Runnable()
      {
         public void run(ceylon.lang.Process process)
         {
            ClassLoader cl = Module.this.getClass().getClassLoader();
            try
            {
               cl.loadClass("org.jboss.filtered.spi.SomeSPI");
            }
            catch (ClassNotFoundException e)
            {
               throw new RuntimeException(e);
            }
/*
            try
            {
               cl.loadClass("org.jboss.filtered.api.SomeAPI");
               throw new RuntimeException("Fail, should not be here!");
            }
            catch (ClassNotFoundException ignored)
            {
            }
*/
            try
            {
               cl.loadClass("org.jboss.filtered.impl.SomeImpl");
               throw new RuntimeException("Fail, should not be here!");
            }
            catch (ClassNotFoundException ignored)
            {
            }
         }
      };
      PathFilter imports = new PathFilter()
      {
         public boolean accept(String path)
         {
            return path.contains("spi"); // only import spis
         }
      };
      Import im = new Import(new ModuleName("org.jboss.filtered"), new ModuleVersion(1, 0, 0, "Alpha1"), false, false, null, imports);
      return new ceylon.lang.modules.Module(name, version, null, null, runnable, im);
   }
}
