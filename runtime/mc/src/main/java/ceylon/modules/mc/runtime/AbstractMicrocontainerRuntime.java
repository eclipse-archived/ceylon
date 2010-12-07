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

import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloading.spi.dependency.policy.ClassLoaderPolicyModule;

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
    * Create classloader policy module.
    *
    * @param name the module name
    * @param version the module version
    * @param args the runtime args
    * @return the classloader policy
    * @throws Exception for any error
    */
   protected abstract ClassLoaderPolicyModule createClassLoaderPolicyModule(ModuleName name, ModuleVersion version, Map<String, String> args) throws Exception;

   public ClassLoader createClassLoader(ModuleName name, ModuleVersion version, Map<String, String> args) throws Exception
   {
      ClassLoaderPolicyModule policyModule = createClassLoaderPolicyModule(name, version, args);
      return registerModule(policyModule);
   }

   /**
    * Register module.
    *
    * @param policyModule the module
    * @return module's classloader
    */
   protected ClassLoader registerModule(ClassLoaderPolicyModule policyModule)
   {
      return policyModule.registerClassLoaderPolicy(system);
   }

   /**
    * Get classloader system.
    *
    * @return the classloader system
    */
   protected ClassLoaderSystem getSystem()
   {
      return system;
   }
}
