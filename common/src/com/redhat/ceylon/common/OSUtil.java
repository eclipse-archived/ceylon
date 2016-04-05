package com.redhat.ceylon.common;

public class OSUtil {
    private static final char ESCAPE = '\033';
    private static final String RESET = ESCAPE + "[0m";

    public static enum Color {
        red, green, yellow, blue;
    }
    
    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0;
    private static final boolean IS_MAC = System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0;
    
    public static boolean isWindows() {
        return IS_WINDOWS;
    }
    
    public static boolean isMac() {
        return IS_MAC;
    }
    
    public static boolean isUnix() {
        return !IS_WINDOWS && !IS_MAC;
    }
    
    private static boolean useColors() {
        boolean colorSupported = isUnix() || isMac();
        String use = System.getenv(Constants.ENV_CEYLON_TERM_COLORS);
        if (use == null) {
            use = System.getProperty(Constants.PROP_CEYLON_TERM_COLORS);
        }
        if (use != null && !use.isEmpty()) {
            if (use.equalsIgnoreCase("true") || use.equalsIgnoreCase("on") || use.equalsIgnoreCase("yes")) {
                return colorSupported;
            } else if (!use.equalsIgnoreCase("auto")) {
                // `use` is "false", "off", "no" or any other unknown value
                return false;
            }
        }
        // `use` is undefined or "auto"
        boolean haveConsole = System.console() != null;
        return colorSupported && haveConsole;
    }
    
    /**
     * Returns the given text with colorizing escape sequences embedded
     * to turn it into a text that will be shown with the desired color
     * once printed to a terminal.
     * Will only work for terminals that colorize output using embedded
     * escape sequences (so Windows is excluded).
     * The default behavior is to allow colors when running on any Unix-like
     * operating system and if the TERM environment variable is set to one
     * of the known supported terminals and the output hasn't been redirected.
     * That behavior can be overridden with the `CEYLON_TERM_COLORS`
     * environment variable or the `ceylon.terminal.usecolors` System
     * property. Setting any of them to "yes" will force colors, using "no"
     * will turn coloring off while "auto" is the default behavior.
     * @param txt The text to colorize
     * @param color The desired color
     * @return The given text with colorizing escape sequences (when possible)
     */
    public static String color(String txt, Color color) {
        if (useColors()) {
            String col = System.getProperty("com.redhat.ceylon.common.tool.terminal.color." + color.name());
            String res = System.getProperty("com.redhat.ceylon.common.tool.terminal.color.reset");
            if (col == null || col.isEmpty() || res == null || res.isEmpty()) {
                String term = System.getenv("TERM");
                if (term != null && term.startsWith("xterm")
                        || term.startsWith("screen")
                        || term.startsWith("linux")
                        || term.startsWith("ansi")) {
                    switch (color) {
                    case red:
                        col = ESCAPE + "[31m";
                        break;
                    case green:
                        col = ESCAPE + "[32m";
                        break;
                    case yellow:
                        col = ESCAPE + "[33m";
                        break;
                    case blue:
                        col = ESCAPE + "[34m";
                        break;
                    default:
                        col = null;
                        break;
                    }
                    res = RESET;
                }
            }
            if (col != null && !col.isEmpty() && res != null && !res.isEmpty()) {
                txt = col + txt + res;
            }
        }
        return txt;
    }
}
