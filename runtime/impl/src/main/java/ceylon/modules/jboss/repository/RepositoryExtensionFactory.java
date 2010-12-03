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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ceylon.modules.spi.Constants;
import ceylon.modules.spi.repository.Repository;

/**
 * Repository factory.
 * Simple util to create repository instance from -rep value.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RepositoryExtensionFactory
{
   static String HTTP = "http";
   static String MAVEN = "maven";

   private RepositoryExtensionFactory()
   {
   }

   /**
    * Create repository.
    *
    * @param args the initial arguments
    * @return new repository instance
    */
   public static RepositoryExtension createRepository(Map<String, String> args)
   {
      List<Repository> repositories = new ArrayList<Repository>();

      String rep = args.get(Constants.REPOSITORY.toString());
      if (rep != null)
      {
         if (rep.startsWith(HTTP))
            repositories.add(new RemoteRepositoryExtension(rep, args));
         else if (rep.startsWith(MAVEN))
            repositories.add(new MavenRepositoryExtension(rep));
         else
            repositories.add(new LocalRepositoryExtension(rep));
      }

      String src = args.get(Constants.SOURCE.toString());
      if (src != null)
      {
         repositories.add(new SourceRepositoryExtension(src, args));
      }

      if (args.containsKey(Constants.DEFAULT.toString()) == false)
         repositories.add(new DefaultRepositoryExtension());

      if (repositories.isEmpty())
         throw new IllegalArgumentException("No repository defined: " + args);

      return new CombinedRepositoryExtension(repositories.toArray(new Repository[repositories.size()]));
   }
}
