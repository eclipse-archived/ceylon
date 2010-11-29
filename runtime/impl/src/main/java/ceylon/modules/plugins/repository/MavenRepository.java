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

/**
 * Maven repository.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class MavenRepository extends LocalRepository
{
   public MavenRepository(String path)
   {
      super(getMavenRepositoryPath(path));
   }

   private static String getMavenRepositoryPath(String path)
   {
      if (path.equals(RepositoryFactory.MAVEN))
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
         return path.substring((RepositoryFactory.MAVEN + "://").length());
      }
   }
}
