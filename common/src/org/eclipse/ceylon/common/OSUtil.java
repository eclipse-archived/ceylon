/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common;

public class OSUtil {
    private static final char ESCAPE = '\033';
    private static final String RESET = ESCAPE + "[0m";

    public static enum Color {
        reset, red, green, yellow, blue;
        
        public String escape() {
            if (!supportsColors()) {
                return null;
            }
            String esc = System.getProperty("org.eclipse.ceylon.common.tool.terminal.color." + name());
            if (esc == null || esc.isEmpty()) {
                String term = System.getenv("TERM");
                if (term != null && (term.startsWith("xterm")
                        || term.startsWith("screen")
                        || term.startsWith("linux")
                        || term.startsWith("ansi"))) {
                    switch (this) {
                    case red:
                        esc = ESCAPE + "[31m";
                        break;
                    case green:
                        esc = ESCAPE + "[32m";
                        break;
                    case yellow:
                        esc = ESCAPE + "[33m";
                        break;
                    case blue:
                        esc = ESCAPE + "[34m";
                        break;
                    case reset:
                        esc = RESET;
                        break;
                    default:
                        esc = null;
                        break;
                    }
                }
            }
            return esc;
        }
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
    
    public static boolean supportsColors() {
        return isUnix() || isMac();
    }
    
    public static boolean useColors() {
        String use = System.getenv(Constants.ENV_CEYLON_TERM_COLORS);
        if (use == null) {
            use = System.getProperty(Constants.PROP_CEYLON_TERM_COLORS);
        }
        if (use != null && !use.isEmpty()) {
            if (use.equalsIgnoreCase("true") || use.equalsIgnoreCase("on") || use.equalsIgnoreCase("yes")) {
                return supportsColors();
            } else if (!use.equalsIgnoreCase("auto")) {
                // `use` is "false", "off", "no" or any other unknown value
                return false;
            }
        }
        // `use` is undefined or "auto"
        boolean haveConsole = System.console() != null;
        return supportsColors() && haveConsole;
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
            String col = color.escape();
            String res = Color.reset.escape();
            if (col != null && !col.isEmpty() && res != null && !res.isEmpty()) {
                txt = col + txt + res;
            }
        }
        return txt;
    }
}
