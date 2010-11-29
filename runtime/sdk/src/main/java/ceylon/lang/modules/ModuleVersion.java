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

package ceylon.lang.modules;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * Simple ModuleVersion mock.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ModuleVersion implements Serializable, Comparable<ModuleVersion>
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1L;

   /** The sperator */
   private static final String SEPARATOR = ".";

   /** The raw pattern */
   private static final String PATTERN = "[a-zA-Z0-9_-]*";

   /** The qualifier pattern */
   private static final Pattern QUALIFIER_PATTERN = Pattern.compile(PATTERN);

   /** The default version */
   public static final ModuleVersion DEFAULT_VERSION = new ModuleVersion(0, 0, 0);

   /** The major part of the version */
   private int major;

   /** The minor part of the version */
   private int minor;

   /** The micro part of the version */
   private int micro;

   /** The qualifier part of the version */
   private String qualifier;

   /**
    * Get the version from a string
    *
    * @param string the string
    * @return the version
    */
   public static ModuleVersion valueOf(String string)
   {
      return parseVersion(string);
   }

   /**
    * Create a new Version.
    *
    * @param major the major part
    * @param minor the minor part
    * @param micro the micro part
    */
   public ModuleVersion(int major, int minor, int micro)
   {
      this(major, minor, micro, null);
   }

   /**
    * Create a new VersionImpl.
    *
    * @param major the major part
    * @param minor the minor part
    * @param micro the micro part
    * @param qualifier the qualifier
    */
   public ModuleVersion(int major, int minor, int micro, String qualifier)
   {
      this.major = major;
      this.minor = minor;
      this.micro = micro;
      if (qualifier == null)
         qualifier = "";
      this.qualifier = qualifier;
      validate();
   }

   /**
    * Create a new VersionImpl.
    *
    * @param version the version as a string
    * @throws IllegalArgumentException for a null version or invalid format
    */
   private ModuleVersion(String version)
   {
      if (version == null)
         throw new IllegalArgumentException("Null version");

      int major;
      int minor = 0;
      int micro = 0;
      String qualifier = "";

      try
      {
         StringTokenizer st = new StringTokenizer(version, SEPARATOR, true);
         major = Integer.parseInt(st.nextToken().trim());

         if (st.hasMoreTokens())
         {
            st.nextToken();
            minor = Integer.parseInt(st.nextToken().trim());

            if (st.hasMoreTokens())
            {
               st.nextToken();
               micro = Integer.parseInt(st.nextToken().trim());

               if (st.hasMoreTokens())
               {
                  st.nextToken();
                  qualifier = st.nextToken().trim();

                  if (st.hasMoreTokens())
                  {
                     throw new IllegalArgumentException("Invalid version format, too many seperators: " + version);
                  }
               }
            }
         }
      }
      catch (NoSuchElementException e)
      {
         throw new IllegalArgumentException("Invalid version format: " + version);
      }
      catch (NumberFormatException e)
      {
         throw new IllegalArgumentException("Invalid version format: " + version, e);
      }

      this.major = major;
      this.minor = minor;
      this.micro = micro;
      this.qualifier = qualifier;
      validate();
   }

   /**
    * Validate arguments.
    */
   protected void validate()
   {
      if (major < 0)
         throw new IllegalArgumentException("negative major: " + major);
      if (minor < 0)
         throw new IllegalArgumentException("negative minor: " + minor);
      if (micro < 0)
         throw new IllegalArgumentException("negative micro: " + micro);

      if (QUALIFIER_PATTERN.matcher(qualifier).matches() == false)
         throw new IllegalArgumentException("Invalid qualifier, it must be " + PATTERN + ": " + qualifier);
   }

   /**
    * Parses a version identifier from the specified string.
    * See <code>Version(String)</code> for the format of the version string.
    *
    * @param version String representation of the version identifier. Leading
    *                and trailing whitespace will be ignored.
    * @return A <code>Version</code> object representing the version
    *         identifier. If <code>version</code> is <code>null</code> or
    *         the empty string then <code>emptyVersion</code> will be
    *         returned.
    * @throws IllegalArgumentException If <code>version</code> is improperly
    *                                  formatted.
    */
   public static ModuleVersion parseVersion(String version)
   {
      if (version == null)
         return DEFAULT_VERSION;

      version = version.trim();
      if (version.length() == 0)
         return DEFAULT_VERSION;

      return new ModuleVersion(version);
   }

   /**
    * Returns the major component of this version identifier.
    *
    * @return The major component.
    */
   public int getMajor()
   {
      return major;
   }

   public void setMajor(int major)
   {
      this.major = major;
   }

   /**
    * Returns the minor component of this version identifier.
    *
    * @return The minor component.
    */
   public int getMinor()
   {
      return minor;
   }

   public void setMinor(int minor)
   {
      this.minor = minor;
   }

   /**
    * Returns the micro component of this version identifier.
    *
    * @return The micro component.
    */
   public int getMicro()
   {
      return micro;
   }

   public void setMicro(int micro)
   {
      this.micro = micro;
   }

   /**
    * Returns the qualifier component of this version identifier.
    *
    * @return The qualifier component.
    */
   public String getQualifier()
   {
      return qualifier;
   }

   public void setQualifier(String qualifier)
   {
      this.qualifier = qualifier;
   }

   @Override
   public String toString()
   {
      StringBuilder builder = new StringBuilder();
      builder.append(major).append(SEPARATOR).append(minor).append(SEPARATOR).append(micro);
      if (qualifier.length() > 0)
         builder.append(SEPARATOR).append(qualifier);
      return builder.toString();
   }

   @Override
   public int hashCode()
   {
      return (major << 24) + (minor << 16) + (micro << 8) + qualifier.hashCode();
   }

   @Override
   public boolean equals(Object object)
   {
      if (object == this)
         return true;

      if (object == null || (object instanceof ModuleVersion == false))
         return false;

      ModuleVersion other = (ModuleVersion) object;
      return compareTo(other) == 0;
   }

   /**
    * Compare two Versions.
    *
    * @param version the other version
    * @return compare result
    */
   public int compareTo(ModuleVersion version)
   {
      if (version == this)
         return 0;

      int result = major - version.major;
      if (result != 0)
         return result;

      result = minor - version.minor;
      if (result != 0)
         return result;

      result = micro - version.micro;
      if (result != 0)
         return result;

      return qualifier.compareTo(version.qualifier);
   }
}
