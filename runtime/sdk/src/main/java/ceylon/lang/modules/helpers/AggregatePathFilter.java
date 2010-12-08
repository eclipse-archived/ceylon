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

import java.util.Arrays;

import ceylon.lang.modules.PathFilter;

/**
 * PathFilter implementation that aggregates multiple other filters.
 *
 * @author John E. Bailey
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
final class AggregatePathFilter implements PathFilter
{
   private final PathFilter[] delegates;
   private final boolean any;

   /**
    * Construct a new instance.
    *
    * @param any       {@code true} if this is an "any" filter, {@code false} if this an "all" filter
    * @param delegates the delegate filter list
    */
   AggregatePathFilter(final boolean any, final PathFilter... delegates)
   {
      this.any = any;
      this.delegates = delegates;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean accept(String path)
   {
      for (PathFilter filter : delegates)
      {
         if (filter.accept(path) == any)
         {
            return any;
         }
      }
      return !any;
   }


   public int hashCode()
   {
      return Boolean.valueOf(any).hashCode() ^ Arrays.hashCode(delegates);
   }

   public boolean equals(final Object obj)
   {
      return obj instanceof AggregatePathFilter && equals((AggregatePathFilter) obj);
   }

   public boolean equals(final AggregatePathFilter obj)
   {
      return obj != null && obj.any == any && Arrays.equals(obj.delegates, delegates);
   }

   public String toString()
   {
      final StringBuilder b = new StringBuilder();
      b.append(any ? "Any " : "All ").append("of (");
      for (int idx = 0; idx < delegates.length; idx++)
      {
         final PathFilter delegate = delegates[idx];
         b.append(delegate);
         if (idx < delegates.length - 1)
         {
            b.append(',');
         }
      }
      b.append(')');
      return b.toString();
   }
}
