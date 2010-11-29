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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.jboss.modules.Resource;

import ceylon.modules.plugins.compiler.CompilerAdapterFactory;

/**
 * Source entry resource.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class SourceEntryResource implements Resource
{
   private String name;
   private File sourceFile;
   private File classFile;
   private URL url;
   private File classesRoot;

   public SourceEntryResource(String name, File sourceFile, URL url, File classesRoot)
   {
      this.name = name;
      this.sourceFile = sourceFile;
      this.url = url;
      this.classesRoot = classesRoot;
   }

   /**
    * Get class file; can be plain resource as well.
    *
    * @return the class file
    * @throws IOException for any I/O error
    */
   protected synchronized File getClassFile() throws IOException
   {
      if (classFile == null)
      {
         CompilerAdapterFactory factory = CompilerAdapterFactory.getInstance();
         classFile = factory.compile(sourceFile, name, classesRoot);
      }
      return classFile;
   }

   public String getName()
   {
      return name;
   }

   public URL getURL()
   {
      return url;
   }

   public InputStream openStream() throws IOException
   {
      return new FileInputStream(getClassFile());
   }

   public long getSize()
   {
      try
      {
         return getClassFile().length();
      }
      catch (IOException ignored)
      {
         return 0;
      }
   }
}
