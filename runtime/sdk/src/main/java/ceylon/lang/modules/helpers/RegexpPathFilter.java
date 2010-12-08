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

import java.util.regex.Pattern;

import ceylon.lang.modules.PathFilter;

/**
 * Regexp implementation of PathFilter.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class RegexpPathFilter implements PathFilter
{
   private final String regexp;
   private final Pattern pattern;

   /**
    * Construct a new instance.
    *
    * @param regexp the path regexp to match
    */
   RegexpPathFilter(final String regexp)
   {
      pattern = getPattern(regexp);
      this.regexp = regexp;
   }

   /**
    * Get pattern.
    *
    * @param regexp the regexp
    * @return the pattern
    */
   protected Pattern getPattern(String regexp)
   {
      return Pattern.compile(regexp);     
   }

   /**
    * Determine whether a path should be accepted.
    *
    * @param path the path to check
    * @return true if the path should be accepted, false if not
    */
   public boolean accept(final String path)
   {
      return pattern.matcher(path).matches();
   }

   public int hashCode()
   {
      return regexp.hashCode();
   }

   public boolean equals(final Object obj)
   {
      return obj instanceof RegexpPathFilter && equals((RegexpPathFilter) obj);
   }

   public boolean equals(final RegexpPathFilter obj)
   {
      return obj != null && obj.pattern.equals(pattern);
   }

   public String toString()
   {
      final StringBuilder b = new StringBuilder();
      b.append("match ");
      if (regexp != null)
      {
         b.append('"').append(regexp).append('"');
      }
      else
      {
         b.append('/').append(pattern).append('/');
      }
      return b.toString();
   }
}
