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

package org.jboss.ceylon.test.modules;

import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.util.file.Files;

import ceylon.modules.Main;
import ceylon.modules.jboss.runtime.JBossRuntime;
import ceylon.modules.spi.Constants;
import org.junit.Assert;

/**
 * Modules test helper.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class ModulesTest
{
   private Class<? extends ceylon.modules.spi.runtime.Runtime> runtimeClazz = JBossRuntime.class; // TODO -- fixme; //MicrocontainerRuntime.class;
   
   protected File createModuleFile(File tmpdir, Archive module) throws Exception
   {
      String fullName = module.getName();
      int p = fullName.indexOf("-");
      if (p < 0)
         throw new IllegalArgumentException("No name and version split: " + fullName);

      String name = fullName.substring(0, p);
      String version = fullName.substring(p + 1, fullName.lastIndexOf("."));

      File targetDir = new File(tmpdir, toPathString(name, version));
      if (targetDir.exists() == false)
         Assert.assertTrue(targetDir.mkdirs());
      File targetFile = new File(targetDir, fullName);

      ZipExporter exporter = module.as(ZipExporter.class);
      exporter.exportZip(targetFile, true);

      return targetDir;
   }

   protected void testArchive(Archive module, Archive... libs) throws Exception
   {
      File tmpdir = AccessController.doPrivileged(new PrivilegedAction<File>()
      {
         public File run()
         {
            return new File(System.getProperty("java.io.tmpdir"));
         }
      });

      List<File> files = new ArrayList<File>();
      try
      {
         files.add(createModuleFile(tmpdir, module));
         for (Archive lib : libs)
            files.add(createModuleFile(tmpdir, lib));

         String fullName = module.getName();
         int p = fullName.indexOf("-");
         if (p < 0)
            throw new IllegalArgumentException("No name and version split: " + fullName);

         String name = fullName.substring(0, p);
         String version = fullName.substring(p + 1, fullName.lastIndexOf("."));

         Map<Constants, String> args = new HashMap<Constants, String>();
         args.put(Constants.EXECUTABLE, runtimeClazz.getName());
         args.put(Constants.MODULE, name + "/" + version);
         args.put(Constants.REPOSITORY, tmpdir.toString());

         execute(args);
      }
      finally
      {
         for (File file : files)
            Files.delete(file);
      }
   }

   protected void src(String module, String src) throws Exception
   {
      src(module, src, Collections.<Constants, String>emptyMap());
   }

   protected void src(String module, String src, Map<Constants, String> extra) throws Exception
   {
      Map<Constants, String> args = new HashMap<Constants, String>();
      args.put(Constants.EXECUTABLE, runtimeClazz.getName());
      args.put(Constants.MODULE, module);
      args.put(Constants.SOURCE, src);
      args.put(Constants.DEFAULT, "false");
      args.putAll(extra);

      execute(args);
   }

   protected void execute(Map<Constants, String> args) throws Exception
   {
      List<String> strings = new ArrayList<String>();
      for (Map.Entry<Constants, String> entry : args.entrySet())
      {
         strings.add("-" + entry.getKey());
         strings.add(entry.getValue());
      }

      Main.execute(strings.toArray(new String[strings.size()]));
   }

   protected static String toPathString(String name, String version)
   {
      final StringBuilder builder = new StringBuilder();
      builder.append(name.replace('.', File.separatorChar));
      builder.append(File.separatorChar).append(version);
      builder.append(File.separatorChar);
      return builder.toString();
   }
}

