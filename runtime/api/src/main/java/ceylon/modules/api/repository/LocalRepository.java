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

package ceylon.modules.api.repository;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import ceylon.lang.modules.Module;
import ceylon.lang.modules.ModuleName;
import ceylon.lang.modules.ModuleVersion;
import ceylon.modules.api.runtime.AbstractRuntime;

/**
 * Local repository.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class LocalRepository extends AbstractRepository
{
   private File repositoryDir;

   protected LocalRepository()
   {
      // if this ctor is used, one must override getPath()!
   }

   public LocalRepository(String path)
   {
      if (path == null)
         throw new IllegalArgumentException("Null path");

      repositoryDir = new File(path);
      if (repositoryDir.exists() == false)
         throw new IllegalArgumentException("No such repository directory: " + path);
   }

   /**
    * Get the local repository path.
    *
    * @return the local repository path
    */
   protected String getPath()
   {
      return repositoryDir != null ? repositoryDir.getPath() : null;
   }

   /**
    * Get repository directory.
    *
    * @return the repository directory
    */
   protected synchronized File getRepositoryDirectory()
   {
      if (repositoryDir == null)
      {
         String path = getPath();
         if (path == null)
            throw new IllegalArgumentException("Null path");

         repositoryDir = new File(path);
         if (repositoryDir.exists() == false)
            throw new IllegalArgumentException("No such repository directory: " + path);
      }
      return repositoryDir;
   }

   public File findModule(ModuleName name, ModuleVersion version)
   {
      File moduleDir = new File(getRepositoryDirectory(), toPathString(name, version));
      if (moduleDir.exists() == false)
         throw new IllegalArgumentException("No such module directory: " + moduleDir);

      File moduleFile = new File(moduleDir, toFileString(name, version));
      if (moduleFile.exists() == false)
         throw new IllegalArgumentException("No such module file: " + moduleFile);

      return moduleFile;
   }

   public Module readModule(ModuleName name, File moduleFile) throws Exception
   {
      URL url = moduleFile.toURI().toURL();
      ClassLoader cl = new URLClassLoader(new URL[]{url});
      String modulePath = name.getName() + ".Module";
      return AbstractRuntime.loadModule(cl, modulePath);
   }

   /**
    * Get Ceylon SDK modules.
    *
    * @return the Ceylon SDK modules
    */
   protected static String getCeylonSDKModules()
   {
      String sdk = getSystemProperty("CEYLON_HOME");
      if (sdk == null)
         throw new IllegalArgumentException("No Ceylon home defined.");

      if (sdk.endsWith(File.separator) == false)
         sdk += File.separator;

      return sdk + "modules";
   }

   /**
    * Get maven repo.
    *
    * @param path the path
    * @return the mvn repo path
    */
   protected static String getMavenRepositoryPath(String path)
   {
      if (path.equals(RepositoryConstants.MAVEN))
      {
         String m2home = getSystemProperty("M2_HOME");
         if (m2home == null)
            m2home = getSystemProperty("MAVEN_HOME");
         if (m2home == null)
            throw new IllegalArgumentException("No Maven2 home set in env variables: " + System.getProperties());
         return m2home;
      }
      else
      {
         return path.substring((RepositoryConstants.MAVEN + "://").length());
      }
   }
}
