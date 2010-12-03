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

package ceylon.modules.api.compiler;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import java.io.*;
import java.util.Collections;

/**
 * Java source compiler adapter.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class JavaCompilerAdapter extends AbstractCompilerAdapter
{
   public static CompilerAdapter INSTANCE = new JavaCompilerAdapter();

   private JavaCompilerAdapter()
   {
      super(".java");
   }

   public File compile(File source, String name, final File classesRoot) throws IOException
   {
      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
      fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singleton(classesRoot));
      JavaFileObject sourceJFO = new FileJavaFileObject(source);
      JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, Collections.singleton(sourceJFO));
      if (task.call() == false) // start compilation
         throw new IllegalArgumentException("Cannot compile: " + source);

      return new File(classesRoot, name.replace(".", "/") + ".class");
   }

   private static class FileJavaFileObject extends SimpleJavaFileObject
   {
      private File source;

      private FileJavaFileObject(File source)
      {
         super(source.toURI(), Kind.SOURCE);
         if (source == null)
            throw new IllegalArgumentException("Null source");
         this.source = source;
      }

      public InputStream openInputStream() throws IOException
      {
         return new FileInputStream(source);
      }

      public OutputStream openOutputStream() throws IOException
      {
         return new FileOutputStream(source);
      }

      public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException
      {
         StringWriter writer = new StringWriter();
         InputStream is = openInputStream();
         try
         {
            int b;
            while ((b = is.read()) >= 0)
               writer.write(b);

            writer.flush();
         }
         finally
         {
            safeClose(is);
            safeClose(writer);
         }
         return writer.getBuffer();
      }

      public long getLastModified()
      {
         return source.lastModified();
      }

      public boolean delete()
      {
         return source.delete();
      }
   }
}
