package com.redhat.ceylon.common.tool;

import java.util.List;

/**
 * Helper methods for tools
 * @author tom
 */
public class Tools {

    private Tools() {}
    
    public static void printToolSuggestions(ToolLoader toolLoader, WordWrap wrap, String toolName) {
        wrap.append(Tools.progName() + ": " + toolName + " is not a ceylon command. See 'ceylon --help'.").newline();
        final List<String> suggestions = toolLoader.typo(toolName);
        if (suggestions != null 
                && !suggestions.isEmpty()) {
            wrap.newline();
            wrap.append("Did you mean:");
            wrap.setIndent(4);
            wrap.newline();
            for (String suggestion : suggestions) {
                wrap.append(suggestion).newline();
            }
            wrap.newline();
        }
    }

    public static String progName() {
        return System.getProperty("com.redhat.ceylon.common.tool.progname", "ceylon");
    }

}
