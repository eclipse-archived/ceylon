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

package ceylon.modules.plugins.repository;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

import org.jboss.modules.PathFilters;
import org.jboss.modules.ResourceLoader;

import ceylon.lang.modules.Module;
import ceylon.lang.modules.ModuleName;
import ceylon.lang.modules.ModuleVersion;
import ceylon.modules.plugins.compiler.CompilerAdapter;
import ceylon.modules.plugins.compiler.JavaCompilerAdapter;
import ceylon.modules.plugins.runtime.AbstractRuntime;
import ceylon.modules.spi.Constants;

/**
 * Source repository.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class SourceRepository extends AbstractRepository
{
   private File sourcesRoot;
   private File classesRoot;

   public SourceRepository(String path, Map<String, String> args)
   {
      if (path == null)
         throw new IllegalArgumentException("Null path");

      File root = new File(path);
      if (root.exists() == false)
         throw new IllegalArgumentException("No such root directory: " + path);

      String lang = getCompilerAdapter().languageSuffix().substring(1);
      String srcPath = getProperty(args, Constants.SOURCES.toString(), "src/main/" + lang);
      sourcesRoot = new File(root, srcPath);
      if (sourcesRoot.exists() == false)
         throw new IllegalArgumentException("No such src dir: " + sourcesRoot);

      String clsPath = getProperty(args, Constants.CLASSES.toString(), "target/classes");
      classesRoot = new File(root, clsPath);
      if (classesRoot.exists() == false)
         if (classesRoot.mkdirs() == false)
            throw new IllegalArgumentException("Cannot create classes dir: " + classesRoot);
   }

   /**
    * Get compiler adapter.
    *
    * @return the compiler adapter
    */
   protected CompilerAdapter getCompilerAdapter()
   {
      return JavaCompilerAdapter.INSTANCE;
   }

   public File findModule(ModuleName name, ModuleVersion version)
   {
      return sourcesRoot;
   }

   public Module readModule(ModuleName name, File moduleFile) throws Exception
   {
      String modulePath = name.getName().replace(".", "/");
      File moduleDescDir = new File(moduleFile, modulePath);
      if (moduleDescDir.exists() == false)
         return null;
      File moduleDescriptor = new File(moduleDescDir, "Module" + getCompilerAdapter().languageSuffix());
      if (moduleDescriptor.exists() == false)
         return null;

      CompilerAdapter ca = getCompilerAdapter();
      String moduleClassName = name.getName() + ".Module";
      File classFile = ca.compile(moduleDescriptor, moduleClassName, classesRoot);

      ClassLoader cl = new URLClassLoader(new URL[]{classFile.toURI().toURL()});
      return AbstractRuntime.loadModule(cl, moduleClassName);
   }

   public ResourceLoader createResourceLoader(ModuleName name, ModuleVersion version, File file)
   {
      return new SourceResourceLoader(sourcesRoot, classesRoot, "", PathFilters.acceptAll());
   }
}
