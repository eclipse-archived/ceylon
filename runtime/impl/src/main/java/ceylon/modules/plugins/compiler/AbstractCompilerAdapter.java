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

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 * Abstract compiler adapter.
 * 
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractCompilerAdapter implements CompilerAdapter
{
   private String languageSuffix;

   /**
    * Create new cmopiler adapter.
    * The *true* lang suffix must contain a dot.
    *
    * @param languageSuffix the language suffix
    */
   protected AbstractCompilerAdapter(String languageSuffix)
   {
      if (languageSuffix == null)
         throw new IllegalArgumentException("Null language suffix");
      this.languageSuffix = languageSuffix;
   }

   public String languageSuffix()
   {
      return languageSuffix;
   }

   public File findSource(File root, String name)
   {
      File file = new File(root, name + languageSuffix);
      return file.exists() ? file : null;
   }

   /**
    * Safe close.
    *
    * @param closeable the closeable
    */
   protected static void safeClose(final Closeable closeable)
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
}
