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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Plain copy compiler adapter.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class CopyCompilerAdapter extends AbstractCompilerAdapter
{
   CopyCompilerAdapter()
   {
      super("");
   }

   public File compile(File source, String name, File classesRoot) throws IOException
   {
      File copy = new File(classesRoot, name);
      File parent = copy.getParentFile();
      parent.mkdirs();

      FileInputStream fis = new FileInputStream(source);
      FileOutputStream fos = new FileOutputStream(copy);
      try
      {
         int b;
         while ((b = fis.read()) >= 0)
            fos.write(b);
      }
      finally
      {
         safeClose(fis);
         safeClose(fos);
      }

      return copy;
   }
}
