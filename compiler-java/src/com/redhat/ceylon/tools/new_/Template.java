/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.tools.new_;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Template {

    private static final Pattern placeholder = Pattern.compile("@\\[([a-zA-Z][_a-zA-Z0-9.]*)\\]");
    
    private final CharSequence source;

    public Template(CharSequence source) {
        this.source = source;
    }
    
    public interface Substitution {
        public String getReplacement(String token, Matcher matcher, CharSequence source);
    }
    
    public static class SimpleSubstitution implements Substitution {
        private final Environment env;
        private final String missing;
        public SimpleSubstitution(Environment env, String missing) {
            this.env = env;
            this.missing = missing;
        }
        public SimpleSubstitution(Environment env) {
            this(env, null);
        }
        @Override
        public String getReplacement(String token, Matcher matcher, CharSequence source) {
            String replacement = env.get(token);
            if (replacement == null) {
                replacement = missing;
            }
            if (replacement == null) {
                throwMissingReplacement(token, matcher, source);
            }
            return replacement;
        }
    }
    
    public String eval(Substitution subs) {
        Matcher matcher = placeholder.matcher(source);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String token = matcher.group(1);
            String replacement = subs.getReplacement(token, matcher, source);
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);
        return result.toString();
    }
    
    public String eval(Environment env) {
        return eval(new SimpleSubstitution(env));
    }
    
    protected static void throwMissingReplacement(String token, Matcher matcher, CharSequence source) {
        throw new RuntimeException("No replacement for " + token + " at line " + lineNumber(source, matcher.start()));   
    }
    
    protected static int lineNumber(CharSequence source, int index) {
        int line = 1;
        for (int ii = 0; ii < source.length(); ii++) {
            char ch = source.charAt(ii);
            if (ch == '\n'
                    || ch == '\r') {
                line++;
                if (ii < source.length() - 1
                        && source.charAt(ii+1) != ch
                        && (source.charAt(ii+1) == '\n'
                        || source.charAt(ii+1) == '\r')) {
                    ii++;
                }
            }
        }
        return line;
    }
    
}
