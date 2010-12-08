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

import java.io.File;
import java.util.Map;

import org.jboss.classloader.plugins.filter.NegatingClassFilter;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.ClassNotFoundEvent;
import org.jboss.classloader.spi.ClassNotFoundHandler;
import org.jboss.classloading.plugins.metadata.ModuleRequirement;
import org.jboss.classloading.spi.dependency.ClassLoading;
import org.jboss.classloading.spi.dependency.ResolutionContext;
import org.jboss.classloading.spi.dependency.Resolver;
import org.jboss.classloading.spi.dependency.policy.ClassLoaderPolicyModule;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaData;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaDataFactory;
import org.jboss.classloading.spi.metadata.Requirement;
import org.jboss.classloading.spi.metadata.RequirementsMetaData;
import org.jboss.classloading.spi.version.Version;
import org.jboss.classloading.spi.version.VersionComparatorRegistry;
import org.jboss.classloading.spi.version.VersionRange;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;
import org.jboss.vfs.util.automount.Automounter;

import ceylon.lang.modules.Import;
import ceylon.lang.modules.Module;
import ceylon.lang.modules.ModuleName;
import ceylon.lang.modules.ModuleVersion;
import ceylon.lang.modules.helpers.PathFilters;
import ceylon.modules.api.repository.RepositoryFactory;
import ceylon.modules.spi.repository.Repository;

/**
 * Microcontainer based runtime.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class MicrocontainerRuntime extends AbstractMicrocontainerRuntime implements Resolver, ClassNotFoundHandler
{
   private ClassLoading classLoading;
   private Node<Import> root;
   private Repository repository;

   public MicrocontainerRuntime()
   {
      classLoading = new ClassLoading();
      root = new Node<Import>();
      VersionComparatorRegistry.getInstance().registerVersionComparator(ModuleVersion.class, new ModulesVersionComparator());
   }

   protected ClassLoaderSystem createClassLoaderSystem()
   {
      return ClassLoaderSystem.getInstance();
   }

   protected ClassLoaderPolicyModule createClassLoaderPolicyModule(ModuleName name, ModuleVersion version, Map<String, String> args) throws Exception
   {
      repository = RepositoryFactory.createRepository(args);
      classLoading.addResolver(this);
      getSystem().addClassNotFoundHandler(this);

      return createModule(name, version);
   }

   protected ClassLoaderPolicyModule createModule(ModuleName name, ModuleVersion version) throws Exception
   {
      File moduleFile = repository.findModule(name, version);
      if (moduleFile == null)
         throw new IllegalArgumentException("No such module found: " + name + ":" + version);

      Module module = repository.readModule(name, moduleFile);
      ClassLoadingMetaData clmd = new ClassLoadingMetaData();
      Import[] imports = module.getDependencies();
      if (imports != null && imports.length > 0)
      {
         ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
         RequirementsMetaData requirements = clmd.getRequirements();

         for (Import i : imports)
         {
            String path = i.getName().getName();
            if (i.isOnDemand())
            {
               Node<Import> current = root;
               String[] tokens = path.split("\\.");
               for (String token : tokens)
               {
                  Node<Import> child = current.getChild(token);
                  if (child == null)
                     child = current.addChild(token);
                  current = child;
               }
               current.setValue(i);
            }
            else
            {
               VersionRange range = new VersionRange(i.getVersion(), true, i.getVersion(), true);
               boolean reExport = i.getExports() != PathFilters.rejectAll();
               // TODO -- more exact filtering?
               Requirement requirement = factory.createRequireModule(path, range, i.isOptional(), reExport, false);
               requirements.addRequirement(requirement);
            }
         }
      }
      clmd.setIncluded(new ClassFilterWrapper(module.getImports()));
      clmd.setExcludedExport(new NegatingClassFilter(new ClassFilterWrapper(module.getExports())));
      clmd.setParentPolicy(CustomParentPolicyMetaData.INSTANCE);

      VirtualFile root = VFS.getChild(moduleFile.toURI());

      ClassNotFoundHandler sources = null;
      File classes = repository.getCompileDirectory();
      if (classes != null)
      {
         sources = new SourcesClassNotFoundHandler(moduleFile, classes);
         root = VFS.getChild(classes.toURI());
      }
      else
      {
         Automounter.mount(this, root);
      }

      VFSClassLoaderPolicyModule clpm = new VFSClassLoaderPolicyModule(clmd, name + ":" + version, root);
      clpm.setClassNotFoundHandler(sources);
      classLoading.addModule(clpm);

      return clpm;
   }

   public boolean resolve(ResolutionContext context)
   {
      try
      {
         ModuleRequirement requirement = (ModuleRequirement) context.getRequirement();
         ModuleName name = new ModuleName(requirement.getName());
         Version v = requirement.getFromVersion();
         ModuleVersion version = new ModuleVersion(v.getMajor(), v.getMinor(), v.getMicro(), v.getQualifier());
         return registerModule(createModule(name, version)) != null;
      }
      catch (Exception e)
      {
         return false;
      }
   }

   public boolean classNotFound(ClassNotFoundEvent event)
   {
      String[] tokens = event.getClassName().split("\\.");
      Node<Import> current = root;
      for (String token : tokens)
      {
         current = current.getChild(token);
         if (current == null)
            return false;

         //noinspection SynchronizationOnLocalVariableOrMethodParameter
         synchronized (current)
         {
            Import i = current.getValue();
            if (i != null)
            {
               current.remove(); // remove, so we don't loop; should not happen though

               try
               {
                  registerModule(createModule(i.getName(), i.getVersion()));
                  return true;
               }
               catch (Exception ignored)
               {
                  return false;
               }
            }
         }
      }
      return false;
   }
}
