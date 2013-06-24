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
package com.redhat.ceylon.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.common.tool.ArgumentParser;
import com.redhat.ceylon.common.tool.EnumerableParser;
import com.redhat.ceylon.common.tool.FatalToolError;
import com.redhat.ceylon.common.tool.OptionArgumentException;
import com.redhat.ceylon.common.tool.OptionArgumentException.ArgumentMultiplicityException;
import com.redhat.ceylon.common.tool.OptionArgumentException.InvalidArgumentValueException;
import com.redhat.ceylon.common.tool.OptionArgumentException.InvalidOptionValueException;
import com.redhat.ceylon.common.tool.OptionArgumentException.OptionMultiplicityException;
import com.redhat.ceylon.common.tool.OptionArgumentException.OptionWithoutArgumentException;
import com.redhat.ceylon.common.tool.OptionArgumentException.ToolInitializationException;
import com.redhat.ceylon.common.tool.OptionArgumentException.UnknownOptionException;
import com.redhat.ceylon.common.tool.OptionModel;
import com.redhat.ceylon.common.tool.ToolError;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.common.tool.Tools;
import com.redhat.ceylon.common.tool.WordWrap;

/**
 * Responsible for generating usage messages, trying hard to be helpful
 */
class Usage {

    // The following from Apache commons lang3 StringUtils.java
    /**
     * <p>Find the Levenshtein distance between two Strings.</p>
     *
     * <p>This is the number of changes needed to change one String into
     * another, where each change is a single character modification (deletion,
     * insertion or substitution).</p>
     *
     * <p>The previous implementation of the Levenshtein distance algorithm
     * was from <a href="http://www.merriampark.com/ld.htm">http://www.merriampark.com/ld.htm</a></p>
     *
     * <p>Chas Emerick has written an implementation in Java, which avoids an OutOfMemoryError
     * which can occur when my Java implementation is used with very large strings.<br>
     * This implementation of the Levenshtein distance algorithm
     * is from <a href="http://www.merriampark.com/ldjava.htm">http://www.merriampark.com/ldjava.htm</a></p>
     *
     * <pre>
     * StringUtils.getLevenshteinDistance(null, *)             = IllegalArgumentException
     * StringUtils.getLevenshteinDistance(*, null)             = IllegalArgumentException
     * StringUtils.getLevenshteinDistance("","")               = 0
     * StringUtils.getLevenshteinDistance("","a")              = 1
     * StringUtils.getLevenshteinDistance("aaapppp", "")       = 7
     * StringUtils.getLevenshteinDistance("frog", "fog")       = 1
     * StringUtils.getLevenshteinDistance("fly", "ant")        = 3
     * StringUtils.getLevenshteinDistance("elephant", "hippo") = 7
     * StringUtils.getLevenshteinDistance("hippo", "elephant") = 7
     * StringUtils.getLevenshteinDistance("hippo", "zzzzzzzz") = 8
     * StringUtils.getLevenshteinDistance("hello", "hallo")    = 1
     * </pre>
     *
     * @param s  the first String, must not be null
     * @param t  the second String, must not be null
     * @return result distance
     * @throws IllegalArgumentException if either String input {@code null}
     * @since 3.0 Changed signature from getLevenshteinDistance(String, String) to
     * getLevenshteinDistance(CharSequence, CharSequence)
     */
    public static int getLevenshteinDistance(CharSequence s, CharSequence t) {
        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        /*
           The difference between this impl. and the previous is that, rather
           than creating and retaining a matrix of size s.length() + 1 by t.length() + 1,
           we maintain two single-dimensional arrays of length s.length() + 1.  The first, d,
           is the 'current working' distance array that maintains the newest distance cost
           counts as we iterate through the characters of String s.  Each time we increment
           the index of String t we are comparing, d is copied to p, the second int[].  Doing so
           allows us to retain the previous cost counts as required by the algorithm (taking
           the minimum of the cost count to the left, up one, and diagonally up and to the left
           of the current cost count being calculated).  (Note that the arrays aren't really
           copied anymore, just switched...this is clearly much better than cloning an array
           or doing a System.arraycopy() each time  through the outer loop.)

           Effectively, the difference between the two implementations is this one does not
           cause an out of memory condition when calculating the LD over two very large strings.
         */

        int n = s.length(); // length of s
        int m = t.length(); // length of t

        if (n == 0) {
            return m;
        } else if (m == 0) {
            return n;
        }

        if (n > m) {
            // swap the input strings to consume less memory
            CharSequence tmp = s;
            s = t;
            t = tmp;
            n = m;
            m = t.length();
        }

        int p[] = new int[n + 1]; //'previous' cost array, horizontally
        int d[] = new int[n + 1]; // cost array, horizontally
        int _d[]; //placeholder to assist in swapping p and d

        // indexes into strings s and t
        int i; // iterates through s
        int j; // iterates through t

        char t_j; // jth character of t

        int cost; // cost

        for (i = 0; i <= n; i++) {
            p[i] = i;
        }

        for (j = 1; j <= m; j++) {
            t_j = t.charAt(j - 1);
            d[0] = j;

            for (i = 1; i <= n; i++) {
                cost = s.charAt(i - 1) == t_j ? 0 : 1;
                // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
                d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
            }

            // copy current distance counts to 'previous row' distance counts
            _d = p;
            p = d;
            d = _d;
        }

        // our last action in the above loop was to switch d and p, so p now
        // actually has the most recent cost counts
        return p[n];
    }
    
    public static boolean isSuggestionFor(String given, String possibleSuggestion) {
        int ld = getLevenshteinDistance(given, possibleSuggestion);
        return ld <= Math.min(given.length() / 2, 4);
    }
    
    private static final int LD_SUGGEST = 4;

    private final CeylonTool rootTool;
    private final String toolName;
    private final Exception t;
    private final WordWrap out;
    
    Usage(CeylonTool rootTool, String toolName, Exception t) {
        this.rootTool = rootTool;
        this.toolName = toolName;
        this.t = t;
        this.out = new WordWrap(System.err);
    }
    
    static void handleException(CeylonTool rootTool, String toolName, Exception t) throws Exception {
        new Usage(rootTool, toolName, t).run();
    }
    
    void run() throws Exception {
        if (!validToolName()) {
            printFirstLineBadToolName(toolName);
            if (t instanceof InvalidArgumentValueException) {
                printSuggestions(toolName, rootTool.getPluginLoader().getToolNames());
            }
            printHelpInvocation();
            out.flush();
        } else {
            printErrorMessage();
            if (t instanceof OptionArgumentException) {
                printUsage((OptionArgumentException)t);
            }
            
            if (rootTool.getStacktraces() 
                    || t instanceof FatalToolError
                    || t instanceof ToolError == false) {
                out.flush();
                t.printStackTrace(System.err);
            }
            out.flush();
        }
    }

    private void printErrorMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(Tools.progName());
        if (toolName != null) {
            sb.append(' ').append(toolName);
        }
        sb.append(": ");
        if (t instanceof FatalToolError) {
            sb.append(CeylonToolMessages.msg("fatal.error")).append(": ");
            sb.append(((FatalToolError)t).getErrorMessage());
        } else if (t instanceof ToolError) {
            sb.append(((ToolError)t).getErrorMessage());
        } else {
            sb.append(t.getLocalizedMessage());
        }
        String[] lines = sb.toString().split("\n");
        for (String line : lines) { 
            out.append(line).newline();
        }
    }
    
    private void printFirstLineBadToolName(String toolName) {
        StringBuilder sb = new StringBuilder();
        sb.append(Tools.progName()).append(": ");
        sb.append(CeylonToolMessages.msg("bad.tool.name", toolName));
        out.append(sb.toString()).newline();
    }
    
    private void printUsage(OptionArgumentException t) throws Exception {
        // It would be much more natural for OptionArgumentException to have a method
        // for this, unfortunately the implementation depends on the help tool
        // which isn't part of the tool API
        if (t instanceof UnknownOptionException) {
            UnknownOptionException e = (UnknownOptionException)t;
            printSynopsis(e.getToolModel());
            printOptionSuggestions(e);
        } else if (t instanceof OptionMultiplicityException) {
            OptionMultiplicityException e = (OptionMultiplicityException)t;
            printSynopsis(e.getToolModel());
            printOptions(e.getOptionModel().getLongName());
        } else if (t instanceof ArgumentMultiplicityException) {
            ArgumentMultiplicityException e = (ArgumentMultiplicityException)t;
            printSynopsis(e.getToolModel());
        } else if (t instanceof OptionWithoutArgumentException) {
            OptionWithoutArgumentException e = (OptionWithoutArgumentException)t;
            printSynopsis(e.getToolModel());
            printOptions(e.getOptionModel().getLongName());
            printSuggestions(e.getOptionModel().getArgument().getParser(), null);
        } else if (t instanceof InvalidArgumentValueException) {
            InvalidArgumentValueException e = (InvalidArgumentValueException)t;
            printSynopsis(e.getToolModel());
            printSuggestions(e.getArgumentModel().getParser(), e.getBadValue());
        } else if (t instanceof InvalidOptionValueException) {
            InvalidOptionValueException e = (InvalidOptionValueException)t;
            printSynopsis(e.getToolModel());
            printOptions(e.getOptionModel().getLongName());
            printSuggestions(e.getOptionModel().getArgument().getParser(), e.getBadValue());
        } else {
            printSynopsis(null);
        } 
        
        printHelpInvocation();
    }

    private void printSynopsis(ToolModel<?> toolModel) throws Exception {
        if (!validToolName()) {
            return;
        }
        String name = toolName;
        while (toolModel != null) {
            name = toolModel.getName();
            toolModel = toolModel.getParentTool();
        }
        // Call the help tool to generate the usage
        out.newline();
        out.append(CeylonToolMessages.msg("usage")).newline();
        out.flush();
        // Can't call rootTool.bootstrap() because that would replace the 
        // rootTool's toolName, which we need when printing option suggestions
        CeylonTool r = new CeylonTool();
        r.setToolLoader(rootTool.getPluginLoader());
        r.setCommand("help");
        
        r.setCommandArguments(Arrays.asList("--synopsis", name));
        r.run();
        out.newline();
    }
    
    private void printOptions(String option) throws Exception {
        // Call the help tool to generate the usage
        out.flush();
        // TODO Because the help tool doesn't understand subcommands
        // it is unable to help about options to subcommands
        
        // Can't call rootTool.bootstrap() because that would replace the 
        // rootTool's toolName, which we need when printing option suggestions
        CeylonTool r = new CeylonTool();
        r.setToolLoader(rootTool.getPluginLoader());
        r.setCommand("help");
        r.setCommandArguments(Arrays.asList(
                option == null ? "--options" :"--options=" + option, 
                toolName));
        r.run();
    }
    
    private void printHelpInvocation() {
        String helpInvocation = Tools.progName()+ " help";
        if (toolName != null
                && validToolName()) {
            helpInvocation += " " + toolName;
        }
        out.append(CeylonToolMessages.msg("run.ceylon.help", helpInvocation));
        out.newline();
    }

    private void printSuggestions(ArgumentParser<?> parser, String badValue) {
        if (parser instanceof EnumerableParser) {
            EnumerableParser<?> enumerableParser = (EnumerableParser<?>)parser;
            printSuggestions(badValue, enumerableParser.possibilities());
        }
    }
    
    private void printSuggestions(String badValue, Iterable<String> valids) {
        List<String> l = new ArrayList<>();
        for (String valid : valids) {
            if (badValue == null || isSuggestionFor(badValue, valid)) {
                l.add(valid);
            }
        }
        Collections.sort(l);
        printSuggestions(l);   
    }
    
    private void printOptionSuggestions(UnknownOptionException e) {
        if (e.getAggregating() == null || e.getAggregating().size() == 0) {
            /*if (!validToolName()) {
                return;
            }*/
            List<String> suggestions = new ArrayList<>();
            for (OptionModel<?> model : e.getToolModel().getOptions()) {
                if (e.getLongName() != null
                        && isSuggestionFor(e.getLongName(), model.getLongName())) {
                    suggestions.add("--"+model.getLongName());
                }
                if (e.getShortName() != null) {
                    if (model.getShortName() != null) {
                        suggestions.add("-"+model.getShortName());
                    }
                    if (getLevenshteinDistance(model.getLongName(), e.getShortName().toString()) <= 1) {
                        suggestions.add("--"+model.getLongName());
                    }
                }
            }
            printSuggestions(suggestions);
        }
    }

    private boolean validToolName() {
        return rootTool.getToolModel() != null;
    }

    private void printSuggestions(List<String> l) {
        if (l.isEmpty()) {
            return;
        }
        out.append(CeylonToolMessages.msg("did.you.mean")).newline();
        out.setIndent(4);
        for (String valid : l) {
            out.append(valid).newline();
        }
        out.setIndent(0);
        out.newline();
    }
    
}
