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

package ceylon.lang.modules;

import java.net.URL;

import ceylon.lang.modules.helpers.PathFilters;

/**
 * Simple Module mock.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Module extends Filterable
{
   private ModuleName name;
   private ModuleVersion version;
   private String doc;
   private URL license;
   private ceylon.lang.Runnable process;
   private Import[] dependencies;

   public Module(
         ModuleName name,
         ModuleVersion version,
         String doc,
         URL license,
         ceylon.lang.Runnable process,
         Import... dependencies)
   {
      this(name, version, doc, license, process, null, null, dependencies);
   }

   public Module(
         ModuleName name,
         ModuleVersion version,
         String doc,
         URL license,
         ceylon.lang.Runnable process,
         PathFilter exports,
         PathFilter imports,
         Import... dependencies)
   {
      super(exports, imports);
      this.name = name;
      this.version = version;
      this.doc = doc;
      this.license = license;
      this.process = process;
      this.dependencies = dependencies;
   }

   protected PathFilter getDefaultExports()
   {
      return PathFilters.acceptAll();
   }

   protected PathFilter getDefaultImports()
   {
      return PathFilters.acceptAll();
   }

   public ModuleName getName()
   {
      return name;
   }

   public ModuleVersion getVersion()
   {
      return version;
   }

   public String getDoc()
   {
      return doc;
   }

   public URL getLicense()
   {
      return license;
   }

   public ceylon.lang.Runnable getProcess()
   {
      return process;
   }

   public Import[] getDependencies()
   {
      return dependencies;
   }
}
