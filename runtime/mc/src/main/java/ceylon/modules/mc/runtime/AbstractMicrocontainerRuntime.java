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

package ceylon.modules.mc.runtime;

import java.util.Map;

import org.jboss.classloader.spi.ClassLoaderPolicy;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.ParentPolicy;

import ceylon.lang.modules.ModuleName;
import ceylon.lang.modules.ModuleVersion;
import ceylon.modules.api.runtime.AbstractRuntime;

/**
 * Abstarct Microcontainer based runtime.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractMicrocontainerRuntime extends AbstractRuntime
{
   private ClassLoaderSystem system = createClassLoaderSystem();

   /**
    * Create classloader system.
    *
    * @return the classloader system.
    */
   protected abstract ClassLoaderSystem createClassLoaderSystem();

   /**
    * Create parent policy.
    *
    * @return the parent policy
    */
   protected abstract ParentPolicy createParentPolicy();

   /**
    * Create classloader policy.
    *
    * @param name the module name
    * @param version the module version
    * @param args the runtime args
    * @return the classloader policy
    */
   protected abstract ClassLoaderPolicy createClassLoaderPolicy(ModuleName name, ModuleVersion version, Map<String, String> args);

   public ClassLoader createClassLoader(ModuleName name, ModuleVersion version, Map<String, String> args) throws Exception
   {
      ParentPolicy parentPolicy = createParentPolicy();
      ClassLoaderPolicy policy = createClassLoaderPolicy(name, version, args);
      return system.registerClassLoaderPolicy(ClassLoaderSystem.DEFAULT_DOMAIN_NAME, parentPolicy, policy);
   }
}
