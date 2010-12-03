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

package net.something.xyz;

import ceylon.lang.Process;
import ceylon.lang.modules.Import;
import ceylon.lang.modules.ModuleName;
import ceylon.lang.modules.ModuleVersion;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Module
{
   public ceylon.lang.modules.Module getModule()
   {
      ModuleName name = new ModuleName("net.something.xyz");
      ModuleVersion version = new ModuleVersion(1, 0, 0, "Final");
      ceylon.lang.Runnable runnable = new ceylon.lang.Runnable()
      {
         public void run(Process process)
         {
            // test class on_demand
            org.jboss.acme.Module.run();
            
            // test resource on_demand
            ClassLoader cl = Module.this.getClass().getClassLoader();
            String resource = "si/alesj/ceylon/test/config.xml";
            Object url = cl.getResource(resource);
            if (url == null)
               throw new IllegalArgumentException("Null url: " + resource);
            System.out.println("URL: " + url);
         }
      };
      Import im1 = new Import(new ModuleName("org.jboss.acme"), new ModuleVersion(1, 0, 0, "CR1"), false, true);
      Import im2 = new Import(new ModuleName("si.alesj.ceylon"), new ModuleVersion(1, 0, 0, "GA"), false, true);
      return new ceylon.lang.modules.Module(name, version, null, null, runnable, im1, im2);
   }
}
