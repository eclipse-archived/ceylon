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

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.jboss.modules.*;

import ceylon.modules.plugins.compiler.CompilerAdapterFactory;

/**
 * Source resource loader.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class SourceResourceLoader implements ResourceLoader
{
   private ModuleIdentifier moduleIdentifier;
   private File sourcesRoot;
   private File classesRoot;
   private String rootName;
   private PathFilter exportFilter;
   private Manifest manifest;

   public SourceResourceLoader(ModuleIdentifier moduleIdentifier, File sourcesRoot, File classesRoot, String rootName, PathFilter exportFilter)
   {
      if (moduleIdentifier == null)
         throw new IllegalArgumentException("Null module identifier");
      if (sourcesRoot == null)
         throw new IllegalArgumentException("Null sources root");
      if (classesRoot == null)
         throw new IllegalArgumentException("Null classes root");
      if (rootName == null)
         throw new IllegalArgumentException("Null root name");
      if (exportFilter == null)
         exportFilter = PathFilters.acceptAll();

      this.moduleIdentifier = moduleIdentifier;
      this.sourcesRoot = sourcesRoot;
      this.classesRoot = classesRoot;
      this.rootName = rootName;
      this.exportFilter = exportFilter;

      final File manifestFile = new File(sourcesRoot, "META-INF" + File.separator + "MANIFEST.MF");
      manifest = readManifestFile(manifestFile);
   }

   private static Manifest readManifestFile(final File manifestFile)
   {
      try
      {
         InputStream is = new FileInputStream(manifestFile);
         try
         {
            return new Manifest(is);
         }
         finally
         {
            safeClose(is);
         }
      }
      catch (IOException e)
      {
         return null;
      }
   }

   public String getRootName()
   {
      return rootName;
   }

   public ClassSpec getClassSpec(String name) throws IOException
   {
      final String compiledFile = fileNameOfClass(name, "class");
      File file = new File(classesRoot, compiledFile);
      if (file.exists())
      {
         return toClassSpec(file);
      }
      else
      {
         CompilerAdapterFactory factory = CompilerAdapterFactory.getInstance();
         file = factory.findAndCompile(sourcesRoot, name, classesRoot);
         return file != null ? toClassSpec(file) : null;
      }
   }

   /**
    * To class spec.
    *
    * @param file the file
    * @return new class spec instance
    * @throws IOException for any I/O error
    */
   protected ClassSpec toClassSpec(File file) throws IOException
   {
      final long size = file.length();
      final ClassSpec spec = new ClassSpec();
      final InputStream is = new FileInputStream(file);
      try
      {
         if (size <= (long) Integer.MAX_VALUE)
         {
            final int castSize = (int) size;
            byte[] bytes = new byte[castSize];
            int a = 0, res;
            while ((res = is.read(bytes, a, castSize - a)) > 0)
            {
               a += res;
            }
            // done
            is.close();
            spec.setBytes(bytes);
            return spec;
         }
         else
         {
            throw new IOException("Resource is too large to be a valid class file");
         }
      }
      finally
      {
         safeClose(is);
      }
   }

   private static void safeClose(final Closeable closeable)
   {
      if (closeable != null) try
      {
         closeable.close();
      }
      catch (IOException e)
      {
         // ignore
      }
   }

   /**
    * Get the file name of a class.
    *
    * @param className  the class name
    * @param typeSuffix the language type suffix
    * @return the name of the corresponding class file
    */
   static String fileNameOfClass(final String className, String typeSuffix)
   {
      return className.replace('.', '/') + "." + typeSuffix;
   }

   public PackageSpec getPackageSpec(String name) throws IOException
   {
      final PackageSpec spec = new PackageSpec();
      final Manifest manifest = this.manifest;
      if (manifest == null)
      {
         return spec;
      }

      final Attributes mainAttribute = manifest.getAttributes(name);
      final Attributes entryAttribute = manifest.getAttributes(name);
      spec.setSpecTitle(getDefinedAttribute(Attributes.Name.SPECIFICATION_TITLE, entryAttribute, mainAttribute));
      spec.setSpecVersion(getDefinedAttribute(Attributes.Name.SPECIFICATION_VERSION, entryAttribute, mainAttribute));
      spec.setSpecVendor(getDefinedAttribute(Attributes.Name.SPECIFICATION_VENDOR, entryAttribute, mainAttribute));
      spec.setImplTitle(getDefinedAttribute(Attributes.Name.IMPLEMENTATION_TITLE, entryAttribute, mainAttribute));
      spec.setImplVersion(getDefinedAttribute(Attributes.Name.IMPLEMENTATION_VERSION, entryAttribute, mainAttribute));
      spec.setImplVendor(getDefinedAttribute(Attributes.Name.IMPLEMENTATION_VENDOR, entryAttribute, mainAttribute));
      if (Boolean.parseBoolean(getDefinedAttribute(Attributes.Name.SEALED, entryAttribute, mainAttribute)))
      {
         spec.setSealBase(moduleIdentifier.toURL(rootName));
      }
      return spec;
   }

   private static String getDefinedAttribute(Attributes.Name name, Attributes entryAttribute, Attributes mainAttribute)
   {
      final String value = entryAttribute == null ? null : entryAttribute.getValue(name);
      return value == null ? mainAttribute == null ? null : mainAttribute.getValue(name) : value;
   }

   public Resource getResource(String name)
   {
      try
      {
         File file = new File(classesRoot, name);
         if (file.exists())
         {
            return new FileEntryResource(name, file, moduleIdentifier.toURL(rootName, name));
         }
         else
         {
            file = new File(sourcesRoot, name);
            if (file.exists() == false)
               return null;

            // TODO -- fix url!
            return new SourceEntryResource(name, file, moduleIdentifier.toURL(rootName, name), classesRoot);
         }
      }
      catch (MalformedURLException e)
      {
         // must be invalid...?  (TODO: check this out)
         return null;
      }
   }

   public String getLibrary(String name)
   {
      // Source cannot have lib
      return null;
   }

   public Collection<String> getPaths()
   {
      List<String> paths = new ArrayList<String>();
      buildIndex(paths, sourcesRoot, "");
      return paths;
   }

   public PathFilter getExportFilter()
   {
      return exportFilter;
   }

   private void buildIndex(final List<String> index, final File root, final String pathBase)
   {
      for (File file : root.listFiles())
      {
         if (file.isDirectory())
         {
            index.add(pathBase + file.getName());
            buildIndex(index, file, pathBase + file.getName() + "/");
         }
      }
   }
}
