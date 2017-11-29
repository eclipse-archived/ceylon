/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.api;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class FilterRule {
    protected Boolean include;
    
    FilterRule(boolean include){
        this.include = include ? Boolean.TRUE : Boolean.FALSE;
    }
    
    abstract Boolean accept(String path);
    
    static class SetFilterRule extends FilterRule {

        private final Set<String> paths;

        SetFilterRule(final Set<String> paths, boolean include) {
            super(include);
            this.paths = paths;
        }

        @Override
        Boolean accept(final String path) {
            return paths.contains(path) ? include : null;
        }
    }

    static class IsChildOfFilterRule extends FilterRule {
        private final String prefix;

        IsChildOfFilterRule(final String path, boolean include) {
            super(include);
            prefix = path.charAt(path.length() - 1) == '/' ? path : path + "/";
        }

        @Override
        Boolean accept(String path) {
            return path.startsWith(prefix) ? include : null;
        }
    }
    
    static class IsFilterRule extends FilterRule {
        private String path;
        IsFilterRule(String path, boolean include){
            super(include);
            this.path = path;
        }
        @Override
        Boolean accept(String path) {
            return this.path.equals(path) ? include : null;
        }
    }
    
    static class MatchFilterRule extends FilterRule {
        private static final Pattern GLOB_PATTERN = Pattern.compile("(\\*\\*?)|(\\?)|(\\\\.)|(/+)|([^*?]+)");

        private final Pattern pattern;

        /**
         * Construct a new instance.
         *
         * @param glob the path glob to match
         */
        MatchFilterRule(String glob, boolean include) {
            super(include);
            pattern = getGlobPattern(glob);
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
         *
         * @return the pattern
         */
        private static Pattern getGlobPattern(final String glob) {
            StringBuilder patternBuilder = new StringBuilder();
            final Matcher m = GLOB_PATTERN.matcher(glob);
            boolean lastWasSlash = false;
            while (m.find()) {
                lastWasSlash = false;
                String grp;
                if ((grp = m.group(1)) != null) {
                    // match a * or **
                    if (grp.length() == 2) {
                        // it's a **
                        patternBuilder.append(".*");
                    } else {
                        // it's a *
                        patternBuilder.append("[^/]*");
                    }
                } else if ((grp = m.group(2)) != null) {
                    // match a '?' glob pattern; any non-slash character
                    patternBuilder.append("[^/]");
                } else if ((grp = m.group(3)) != null) {
                    // backslash-escaped value
                    patternBuilder.append(Pattern.quote(m.group().substring(1)));
                } else if ((grp = m.group(4)) != null) {
                    // match any number of / chars
                    patternBuilder.append("/+");
                    lastWasSlash = true;
                } else {
                    // some other string
                    patternBuilder.append(Pattern.quote(m.group()));
                }
            }
            if (lastWasSlash) {
                // ends in /, append **
                patternBuilder.append(".*");
            } else {
                patternBuilder.append("(?:/.*)?");
            }
            return Pattern.compile(patternBuilder.toString());
        }

        @Override
        Boolean accept(String path) {
            return pattern.matcher(path).matches() ? include : null;
        }
    }

}