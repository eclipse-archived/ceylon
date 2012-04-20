package com.redhat.ceylon.common;

import java.io.IOException;
import java.util.HashMap;

public class CeylonConfig {
    private static HashMap<String, String[]> options;
    
    private CeylonConfig() {}
    
    private static synchronized void load() {
        if (options == null) {
            try {
                options = (new ConfigParser()).parse();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                options = new HashMap<String, String[]>();
            }
        }
    }
    
    public static boolean isOptionDefined(String key) {
        if (options == null) load();
        return options.containsKey(key);
    }
    
    public static String[] getOptionValues(String key) {
        if (options == null) load();
        return options.get(key);
    }
    
    public static void setOptionValues(String key, String[] values) {
        if (options == null) load();
        options.put(key, values);
    }
    
    public static String getOption(String key) {
        String[] result = getOptionValues(key);
        return (result != null) ? result[0] : null;
    }
    
    public static String getOption(String key, String def) {
        String result = getOption(key);
        return (result != null) ? result : def;
    }
    
    public static void setOption(String key, String value) {
        setOptionValues(key, new String[] { value });
    }
    
    public static Long getNumberOption(String key) {
        String result = getOption(key);
        if (result != null) {
            try {
                return Long.valueOf(result);
            } catch (NumberFormatException e) {
                // Do nothing (logging would spam in case of a user configuration error)
            }
        }
        return null;
    }
    
    public static long getNumberOption(String key, long def) {
        String result = getOption(key);
        if (result != null) {
            try {
                return Long.parseLong(result);
            } catch (NumberFormatException e) {
                // Do nothing (logging would spam in case of a user configuration error)
            }
        }
        return def;
    }
    
    public static void setNumberOption(String key, long value) {
        setOption(key, Long.toString(value));
    }
    
    public static Boolean getBoolOption(String key) {
        String result = getOption(key);
        if (result != null) {
            return "true".equals(result) || "on".equals(result) || "yes".equals(result) || "1".equals(result);
        }
        return null;
    }
    
    public static boolean getBoolOption(String key, boolean def) {
        Boolean result = getBoolOption(key);
        if (result != null) {
            return result.booleanValue();
        }
        return def;
    }
    
    public static void setBoolOption(String key, boolean value) {
        setOption(key, Boolean.toString(value));
    }
}
