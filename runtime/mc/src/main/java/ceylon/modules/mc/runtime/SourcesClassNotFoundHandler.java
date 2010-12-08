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

package ceylon.modules.mc.runtime;

import java.io.File;
import java.io.IOException;

import org.jboss.classloader.spi.ClassNotFoundEvent;
import org.jboss.classloader.spi.ClassNotFoundHandler;

import ceylon.modules.api.compiler.CompilerAdapterFactory;

/**
 * Sources class not found handler
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class SourcesClassNotFoundHandler implements ClassNotFoundHandler
{
   private File sources;
   private File classes;

   SourcesClassNotFoundHandler(File sources, File classes)
   {
      this.sources = sources;
      this.classes = classes;
   }

   public boolean classNotFound(ClassNotFoundEvent event)
   {
      try
      {
         CompilerAdapterFactory factory = CompilerAdapterFactory.getInstance();
         return factory.findAndCompile(sources, event.getClassName(), classes) != null;
      }
      catch (IOException e)
      {
         return false;
      }
   }
}
