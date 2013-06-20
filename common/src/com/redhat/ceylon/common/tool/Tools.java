package com.redhat.ceylon.common.tool;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Helper methods for tools
 * @author tom
 */
public class Tools {

    private Tools() {}
    
    private static boolean isToolExecutionException(Throwable t) {
        for (Class<?> i : t.getClass().getInterfaces()) {
            if (ToolError.class.getName().equals(i.getName())) {
                return true;
                
            }
        }
        return false;
    }
    
    public static boolean isFatal(Throwable t) {
        if (isToolExecutionException(t)) {
            try {
                return (Boolean)t.getClass().getMethod("isFatal").invoke(t);
            } catch (ReflectiveOperationException e) {
                // fall through
            }
        }
        return true;
    }
    
    public static boolean writeErrorMessage(Throwable t, WordWrap w) {
        if (isToolExecutionException(t)) {
            for (Method m : t.getClass().getMethods()) {
                try {
                    if ("writeErrorMessage".equals(m.getName())) {
                        m.invoke(t, w);
                        return true;
                    }
                } catch (Exception e) {
                    Class<?> c1 = m.getParameterTypes()[0];
                    System.err.println(c1+ ", "+ c1.getClassLoader());
                    Class<?> c2 = w.getClass();
                    System.err.println(c2+ ", "+ c2.getClassLoader());
                    e.printStackTrace();
                    break;
                }
            }
        }
        return false;
    }
    
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
