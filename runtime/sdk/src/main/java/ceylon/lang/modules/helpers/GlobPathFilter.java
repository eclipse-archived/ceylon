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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ceylon.lang.modules.PathFilter;

/**
 * Default implementation of PathFilter.  Uses glob based includes and excludes to determine whether to export.
 *
 * @author John E. Bailey
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
final class GlobPathFilter implements PathFilter
{
   private static final Pattern GLOB_PATTERN = Pattern.compile("(\\*\\*?)|(\\?)|(\\\\.)|(/+)|([^*?]+)");

   private final String glob;
   private final Pattern pattern;

   /**
    * Construct a new instance.
    *
    * @param glob the path glob to match
    */
   GlobPathFilter(final String glob)
   {
      pattern = getGlobPattern(glob);
      this.glob = glob;
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

   /**
    * Get a regular expression pattern which accept any path names which match the given glob.  The glob patterns
    * function similarly to {@code ant} file patterns.  Valid metacharacters in the glob pattern include:
    * <ul>
    * <li><code>"\"</code> - escape the next character (treat it literally, even if it is itself a recognized metacharacter)</li>
    * <li><code>"?"</code> - match any non-slash character</li>
    * <li><code>"*"</code> - match zero or more non-slash characters</li>
    * <li><code>"**"</code> - match zero or more characters, including slashes</li>
    * <li><code>"/"</code> - match one or more slash characters.  Consecutive {@code /} characters are collapsed down into one.</li>
    * </ul>
    * In addition, any glob pattern matches all subdirectories thereof.  A glob pattern ending in {@code /} is equivalent
    * to a glob pattern ending in <code>/**</code> in that the named directory is not itself included in the glob.
    * <p/>
    * <b>See also:</b> <a href="http://ant.apache.org/manual/dirtasks.html#patterns">"Patterns" in the Ant Manual</a>
    *
    * @param glob the glob to match
    * @return the pattern
    */
   private static Pattern getGlobPattern(final String glob)
   {
      StringBuilder patternBuilder = new StringBuilder();
      final Matcher m = GLOB_PATTERN.matcher(glob);
      boolean lastWasSlash = false;
      while (m.find())
      {
         lastWasSlash = false;
         String grp;
         if ((grp = m.group(1)) != null)
         {
            // match a * or **
            if (grp.length() == 2)
            {
               // it's a **
               patternBuilder.append(".*");
            }
            else
            {
               // it's a *
               patternBuilder.append("[^/]*");
            }
         }
         else if ((grp = m.group(2)) != null)
         {
            // match a '?' glob pattern; any non-slash character
            patternBuilder.append("[^/]");
         }
         else if ((grp = m.group(3)) != null)
         {
            // backslash-escaped value
            patternBuilder.append(grp.charAt(1));
         }
         else if ((grp = m.group(4)) != null)
         {
            // match any number of / chars
            patternBuilder.append("/+");
            lastWasSlash = true;
         }
         else
         {
            // some other string
            patternBuilder.append(Pattern.quote(m.group()));
         }
      }
      if (lastWasSlash)
      {
         // ends in /, append **
         patternBuilder.append(".*");
      }
      else
      {
         patternBuilder.append("(?:/.*)?");
      }
      return Pattern.compile(patternBuilder.toString());
   }

   public int hashCode()
   {
      return glob.hashCode();
   }

   public boolean equals(final Object obj)
   {
      return obj instanceof GlobPathFilter && equals((GlobPathFilter) obj);
   }

   public boolean equals(final GlobPathFilter obj)
   {
      return obj != null && obj.pattern.equals(pattern);
   }

   public String toString()
   {
      final StringBuilder b = new StringBuilder();
      b.append("match ");
      if (glob != null)
      {
         b.append('"').append(glob).append('"');
      }
      else
      {
         b.append('/').append(pattern).append('/');
      }
      return b.toString();
   }
}
