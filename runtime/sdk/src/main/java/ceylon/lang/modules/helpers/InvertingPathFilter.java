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

package ceylon.lang.modules.helpers;

import ceylon.lang.modules.PathFilter;

/**
 * A path filter which simply inverts the result of another path filter.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
final class InvertingPathFilter implements PathFilter
{
   private final PathFilter delegate;

   /**
    * Construct a new instance.
    *
    * @param delegate the filter to delegate to
    */
   InvertingPathFilter(final PathFilter delegate)
   {
      if (delegate == null)
      {
         throw new IllegalArgumentException("delegate is null");
      }
      this.delegate = delegate;
   }

   /**
    * {@inheritDoc}
    */
   public boolean accept(final String path)
   {
      return !delegate.accept(path);
   }

   public int hashCode()
   {
      return 47 * delegate.hashCode();
   }

   public boolean equals(final Object obj)
   {
      return obj instanceof InvertingPathFilter && equals((InvertingPathFilter) obj);
   }

   public boolean equals(final InvertingPathFilter obj)
   {
      return obj != null && obj.delegate.equals(delegate);
   }

   public String toString()
   {
      return "not " + delegate.toString();
   }
}
