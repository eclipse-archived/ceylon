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

package ceylon.modules.plugins.compiler;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Compiler adapter factory.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class CompilerAdapterFactory
{
   private static CompilerAdapterFactory instance = new CompilerAdapterFactory();
   private List<CompilerAdapter> adapters = new CopyOnWriteArrayList<CompilerAdapter>();

   private CompilerAdapterFactory()
   {
      // add default compilers
      addCompilerAdapter(new CopyCompilerAdapter());
      addCompilerAdapter(JavaCompilerAdapter.INSTANCE);
   }

   /**
    * Get compiler adapter factory instance.
    *
    * @return the cmopiler adapter factory instance
    */
   public static CompilerAdapterFactory getInstance()
   {
      return instance;
   }

   /**
    * Find the source and compile it.
    *
    * @param sourceRoot the source root
    * @param name the resource name
    * @param classesRoot the classes root
    * @return compiled file or null if no compilation happened
    * @throws IOException for any I/O error
    */
   public File findAndCompile(File sourceRoot, String name, File classesRoot) throws IOException
   {
      File source = null;
      CompilerAdapter adapter = null;
      for (CompilerAdapter ca : adapters)
      {
         source = ca.findSource(sourceRoot, name);
         if (source != null)
         {
            adapter = ca;
            break;
         }
      }

      if (adapter == null)
         return null;

      return adapter.compile(source, name, classesRoot);
   }

   /**
    * Compile the source.
    *
    * @param source the source file
    * @param name the resource name
    * @param classesRoot the classes root
    * @return compiled file or null if no compilation happened
    * @throws IOException for any I/O error
    */
   public File compile(File source, String name, File classesRoot) throws IOException
   {
      for (CompilerAdapter ca : adapters)
      {
         File output = ca.compile(source, name, classesRoot);
         if (output != null)
            return output;
      }

      return null;
   }

   /**
    * Add compiler adapter.
    *
    * @param adapter the compiler adapter
    */
   public void addCompilerAdapter(CompilerAdapter adapter)
   {
      if (adapter == null)
         throw new IllegalArgumentException("Null adapter");
      adapters.add(0, adapter); // making sure copy is always the last one
   }

   /**
    * Remove compiler adapter.
    *
    * @param adapter the compiler adapter
    */
   public void removeCompilerAdapter(CompilerAdapter adapter)
   {
      if (adapter == null)
         throw new IllegalArgumentException("Null adapter");
      adapters.remove(adapter);
   }
}