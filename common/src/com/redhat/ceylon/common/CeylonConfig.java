package com.redhat.ceylon.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class CeylonConfig {
    private static HashMap<String, String[]> options;
    private static HashMap<String, HashSet<String>> sectionNames;
    private static HashMap<String, HashSet<String>> optionNames;
    
    private CeylonConfig() {}
    
    private static synchronized void load() {
        if (options == null) {
            try {
                options = (new ConfigParser()).parse();
                sectionNames = new HashMap<String, HashSet<String>>();
                optionNames = new HashMap<String, HashSet<String>>();
                initLookups();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                options = new HashMap<String, String[]>();
            }
        }
    }
    
    private static void initLookups() {
        for (String key : options.keySet()) {
            initLookupKey(key);
        }
    }

    private static void initLookupKey(String key) {
        String[] parts = key.split("\\.");
        
        String subsectionName = parts[parts.length - 2]; 
        String optionName = parts[parts.length - 1];
        String sectionName;
        String parentSectionName = "";
        if (parts.length > 2) {
            for (int i = 0; i < parts.length - 2; i++) {
                if (i > 0) {
                    parentSectionName += '.';
                }
                parentSectionName += parts[i];
            }
            if (parts.length > 3) {
                initLookupKey(parentSectionName + ".#");
            }
            sectionName = parentSectionName + '.' + subsectionName;
        } else {
            sectionName = subsectionName;
        }
        
        HashSet<String> sn = sectionNames.get(parentSectionName);
        if (sn == null) {
            sn = new HashSet<String>();
            sectionNames.put(parentSectionName, sn);
        }
        sn.add(subsectionName);
        
        if (!"#".equals(optionName)) {
            HashSet<String> on = optionNames.get(sectionName);
            if (on == null) {
                on = new HashSet<String>();
                optionNames.put(sectionName, on);
            }
            on.add(optionName);
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
    
    public static String[] getSectionNames(String section) {
        if (options == null) load();
        if (section == null) {
            section = "";
        }
        HashSet<String> sn = sectionNames.get(section);
        String[] res = new String[sn.size()];
        return sn.toArray(res);
    }
    
    public static String[] getOptionNames(String section) {
        if (options == null) load();
        HashSet<String> on = sectionNames.get(section);
        String[] res = new String[on.size()];
        return on.toArray(res);
    }
}
