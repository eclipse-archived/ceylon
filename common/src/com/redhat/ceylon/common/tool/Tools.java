package com.redhat.ceylon.common.tool;

import java.util.List;

/**
 * Helper methods for tools
 * @author tom
 */
public class Tools {

    private Tools() {}
    
    public static void printToolSuggestions(ToolLoader toolLoader, WordWrap wrap, String toolName) {
        
        wrap.append(ToolMessages.msg("tool.unknown", Tools.progName(), toolName)).newline();
        final List<String> suggestions = toolLoader.typo(toolName);
        if (suggestions != null 
                && !suggestions.isEmpty()) {
            wrap.newline();
            wrap.append(ToolMessages.msg("tool.unknown.suggest"));
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
