/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ceylon.modules.api.util;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * Simple ModuleVersion mock.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ModuleVersion implements Serializable, Comparable<ModuleVersion> {
    /**
     * The serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * The sperator
     */
    private static final String SEPARATOR = ".";

    /**
     * The raw pattern
     */
    private static final String PATTERN = "[a-zA-Z0-9_-]*";

    /**
     * The qualifier pattern
     */
    private static final Pattern QUALIFIER_PATTERN = Pattern.compile(PATTERN);

    /**
     * The default version
     */
    public static final ModuleVersion DEFAULT_VERSION = new ModuleVersion(0, 0, 0);

    /**
     * The major part of the version
     */
    private int major;

    /**
     * The minor part of the version
     */
    private int minor;

    /**
     * The micro part of the version
     */
    private int micro;

    /**
     * The qualifier part of the version
     */
    private String qualifier;

    /**
     * Get the version from a string
     *
     * @param string the string
     * @return the version
     */
    public static ModuleVersion valueOf(String string) {
        return parseVersion(string);
    }

    /**
     * Create a new Version.
     *
     * @param major the major part
     * @param minor the minor part
     * @param micro the micro part
     */
    public ModuleVersion(int major, int minor, int micro) {
        this(major, minor, micro, null);
    }

    /**
     * Create a new VersionImpl.
     *
     * @param major     the major part
     * @param minor     the minor part
     * @param micro     the micro part
     * @param qualifier the qualifier
     */
    public ModuleVersion(int major, int minor, int micro, String qualifier) {
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
    private ModuleVersion(String version) {
        if (version == null)
            throw new IllegalArgumentException("Null version");

        int major;
        int minor = 0;
        int micro = 0;
        String qualifier = "";

        try {
            StringTokenizer st = new StringTokenizer(version, SEPARATOR, true);
            major = Integer.parseInt(st.nextToken().trim());

            if (st.hasMoreTokens()) {
                st.nextToken();
                minor = Integer.parseInt(st.nextToken().trim());

                if (st.hasMoreTokens()) {
                    st.nextToken();
                    micro = Integer.parseInt(st.nextToken().trim());

                    if (st.hasMoreTokens()) {
                        st.nextToken();
                        qualifier = st.nextToken().trim();

                        if (st.hasMoreTokens()) {
                            throw new IllegalArgumentException("Invalid version format, too many seperators: " + version);
                        }
                    }
                }
            }
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("Invalid version format: " + version);
        } catch (NumberFormatException e) {
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
    protected void validate() {
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
    public static ModuleVersion parseVersion(String version) {
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
    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    /**
     * Returns the minor component of this version identifier.
     *
     * @return The minor component.
     */
    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    /**
     * Returns the micro component of this version identifier.
     *
     * @return The micro component.
     */
    public int getMicro() {
        return micro;
    }

    public void setMicro(int micro) {
        this.micro = micro;
    }

    /**
     * Returns the qualifier component of this version identifier.
     *
     * @return The qualifier component.
     */
    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(major).append(SEPARATOR).append(minor).append(SEPARATOR).append(micro);
        if (qualifier.length() > 0)
            builder.append(SEPARATOR).append(qualifier);
        return builder.toString();
    }

    @Override
    public int hashCode() {
        return (major << 24) + (minor << 16) + (micro << 8) + qualifier.hashCode();
    }

    @Override
    public boolean equals(Object object) {
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
    public int compareTo(ModuleVersion version) {
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
