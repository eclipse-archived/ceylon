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

package ceylon.modules.jboss.repository;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.jboss.modules.Resource;

/**
 * Car archive entry.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
final class CarEntryResource implements Resource
{
   private final JarFile jarFile;
   private final JarEntry entry;
   private final URL resourceURL;

   CarEntryResource(final JarFile jarFile, final JarEntry entry, final URL resourceURL)
   {
      this.jarFile = jarFile;
      this.entry = entry;
      this.resourceURL = resourceURL;
   }

   public String getName()
   {
      return entry.getName();
   }

   public URL getURL()
   {
      return resourceURL;
   }

   public InputStream openStream() throws IOException
   {
      return jarFile.getInputStream(entry);
   }

   public long getSize()
   {
      final long size = entry.getSize();
      return size == -1 ? 0 : size;
   }
}
