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
 * Boolean path filter -- all or nothing.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
final class BooleanPathFilter implements PathFilter
{
   private final boolean result;

   private BooleanPathFilter(final boolean result)
   {
      this.result = result;
   }

   public boolean accept(final String path)
   {
      return result;
   }

   static final BooleanPathFilter TRUE = new BooleanPathFilter(true);
   static final BooleanPathFilter FALSE = new BooleanPathFilter(false);

   public int hashCode()
   {
      return Boolean.valueOf(result).hashCode();
   }

   public boolean equals(final Object obj)
   {
      return obj == this;
   }

   public String toString()
   {
      return result ? "Accept" : "Reject";
   }
}
