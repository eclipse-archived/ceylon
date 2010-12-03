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

package ceylon.modules.jboss.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.jboss.modules.LocalLoader;
import org.jboss.modules.Module;
import org.jboss.modules.Resource;

/**
 * Module local loader.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class ModuleLocalLoader implements LocalLoader
{
   private final Module module;

   ModuleLocalLoader(Module module)
   {
      this.module = module;
   }

   public Class<?> loadClassLocal(String name, boolean resolve)
   {
      try
      {
         return module.getClassLoader().loadClass(name, resolve);
      }
      catch (ClassNotFoundException ignored)
      {
         return null;
      }
   }

   @SuppressWarnings({"unchecked"})
   public List<Resource> loadResourceLocal(final String name)
   {
      final Enumeration<URL> urls = module.getExportedResources(name);
      final List<Resource> list = new ArrayList<Resource>();
      while (urls.hasMoreElements())
         list.add(new URLResource(urls.nextElement()));
      return list;
   }

   public Resource loadResourceLocal(String root, String name)
   {
      return module.getExportedResource(root, name);
   }

   final static class URLResource implements Resource
   {
      private final URL url;

      public URLResource(final URL url)
      {
         this.url = url;
      }

      public String getName()
      {
         return url.getPath();
      }

      public URL getURL()
      {
         return url;
      }

      public InputStream openStream() throws IOException
      {
         return url.openStream();
      }

      public long getSize()
      {
         return 0L;
      }
   }
}
