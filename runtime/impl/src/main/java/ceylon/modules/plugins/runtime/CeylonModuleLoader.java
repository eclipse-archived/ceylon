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

package ceylon.modules.plugins.runtime;

import java.io.File;
import java.util.Collections;

import org.jboss.modules.*;

import ceylon.lang.modules.Import;
import ceylon.lang.modules.Module;
import ceylon.lang.modules.ModuleName;
import ceylon.lang.modules.ModuleVersion;
import ceylon.modules.spi.repository.Repository;

/**
 * Ceyon Module loader.
 * It understands Ceylon repository notion.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class CeylonModuleLoader extends ModuleLoader
{
   private Repository repository;

   public CeylonModuleLoader(Repository repository)
   {
      if (repository == null)
         throw new IllegalArgumentException("Null repository");
      this.repository = repository;
   }

   /**
    * Update module.
    *
    * @param module the module to update
    * @param dependencySpec new dependency
    * @throws ModuleLoadException for any error
    */
   void updateModule(org.jboss.modules.Module module, DependencySpec dependencySpec) throws ModuleLoadException
   {
      setAndRelinkDependencies(module, Collections.singletonList(dependencySpec)); // TODO -- old deps
      refreshResourceLoaders(module);
   }

   @Override
   protected org.jboss.modules.Module preloadModule(final ModuleIdentifier identifier) throws ModuleLoadException
   {
      if (identifier.equals(ModuleIdentifier.SYSTEM))
         return org.jboss.modules.Module.getSystemModule();

      return super.preloadModule(identifier);
   }

   @Override
   protected ModuleSpec findModule(ModuleIdentifier moduleIdentifier) throws ModuleLoadException
   {
      ModuleName name = new ModuleName(moduleIdentifier.getName());
      ModuleVersion version = ModuleVersion.parseVersion(moduleIdentifier.getSlot());

      repository.begin();
      try
      {
         File moduleFile = repository.findModule(name, version);
         if (moduleFile == null)
            return null;

         Module module = repository.readModule(name, moduleFile);
         if (module == null)
            throw new ModuleLoadException("No module descriptor in module: " + moduleFile);

         ModuleSpec.Builder builder = ModuleSpec.build(moduleIdentifier);
         ResourceLoader resourceLoader = repository.createResourceLoader(name, version, moduleFile);
         builder.addResourceRoot(resourceLoader);
         builder.addDependency(DependencySpec.createLocalDependencySpec()); // local resources
         Import[] imports = module.getDependencies();
         if (imports != null && imports.length > 0)
         {
            Node<Import> root = new Node<Import>();
            for (Import i : imports)
            {
               if (i.isOnDemand())
               {
                  String path = i.getName().getName();
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
                  DependencySpec mds = createModuleDependency(i);
                  builder.addDependency(mds);
               }
            }
            if (root.isEmpty() == false)
            {
               LocalLoader onDemandLoader = new OnDemandLocalLoader(moduleIdentifier, this, root);
               builder.setFallbackLoader(onDemandLoader);
            }
         }
         // add system as a dependency to all modules, but filter it
         DependencySpec sds = DependencySpec.createModuleDependencySpec(
               PathFilters.match("ceylon/**"),
               PathFilters.rejectAll(),
               this,
               ModuleIdentifier.SYSTEM,
               false
         );
         builder.addDependency(sds);
         return builder.create();
      }
      catch (ModuleLoadException mle)
      {
         throw mle;
      }
      catch (Exception e)
      {
         throw new ModuleLoadException(e);
      }
      finally
      {
         repository.end();   
      }
   }

   /**
    * Create module dependency from import.
    *
    * @param i the import
    * @return new module dependency
    */
   static DependencySpec createModuleDependency(Import i)
   {
      ModuleIdentifier mi = createModuleIdentifier(i);
      PathFilter exportFilter = i.isExport() ? PathFilters.acceptAll() : PathFilters.rejectAll();
      return DependencySpec.createModuleDependencySpec(exportFilter, mi, i.isOptional());
   }

   /**
    * Create module identifier.
    * @param i the import
    * @return module identifer
    */
   static ModuleIdentifier createModuleIdentifier(Import i)
   {
      return ModuleIdentifier.create(i.getName().getName(), i.getVersion().toString());
   }

   public String toString()
   {
      return "Ceylon ModuleLoader: " + repository;
   }
}
