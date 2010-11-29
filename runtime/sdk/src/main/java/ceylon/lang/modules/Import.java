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

/**
 * Simple Import mock.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Import
{
   private ModuleName name;
   private ModuleVersion version;
   private Boolean optional = Boolean.FALSE;
   private Boolean onDemand = Boolean.FALSE;
   private Boolean export = Boolean.FALSE;

   public Import(ModuleName name, ModuleVersion version)
   {
      this.name = name;
      this.version = version;
   }

   public Import(ModuleName name, ModuleVersion version, Boolean optional, Boolean onDemand, Boolean export)
   {
      this.name = name;
      this.version = version;
      this.optional = toBoolean(optional);
      this.onDemand = toBoolean(onDemand);
      this.export = toBoolean(export);
   }

   private static Boolean toBoolean(Boolean value)
   {
      return value != null && Boolean.TRUE.equals(value);
   }

   public ModuleName getName()
   {
      return name;
   }

   public ModuleVersion getVersion()
   {
      return version;
   }

   public Boolean isOptional()
   {
      return optional;
   }

   public Boolean isOnDemand()
   {
      return onDemand;
   }

   public Boolean isExport()
   {
      return export;
   }
}
