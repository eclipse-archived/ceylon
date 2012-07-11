package com.redhat.ceylon.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class CeylonConfig {
    private HashMap<String, String[]> options;
    private HashMap<String, HashSet<String>> sectionNames;
    private HashMap<String, HashSet<String>> optionNames;
    
    private static CeylonConfig instance;
    
    public static CeylonConfig get() {
        if (instance == null) {
            try {
                instance = ConfigParser.loadDefaultConfig();
            } catch (IOException e) {
                instance = new CeylonConfig();
            }
        }
        return instance;
    }
    
    public static void set(CeylonConfig config) {
        instance = config;
    }
    
    public static String get(String key) {
        return get().getOption(key);
    }
    
    public static String get(String key, String defaultValue) {
        return get().getOption(key, defaultValue);
    }
    
    public CeylonConfig() {
        options = new HashMap<String, String[]>();
        sectionNames = new HashMap<String, HashSet<String>>();
        optionNames = new HashMap<String, HashSet<String>>();
    }
    
    private void initLookupKey(String key) {
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
    
    public boolean isOptionDefined(String key) {
        return options.containsKey(key);
    }
    
    public String[] getOptionValues(String key) {
        return options.get(key);
    }
    
    public void setOptionValues(String key, String[] values) {
        options.put(key, values);
        initLookupKey(key);
    }
    
    public String getOption(String key) {
        String[] result = getOptionValues(key);
        return (result != null) ? result[0] : null;
    }
    
    public String getOption(String key, String defaultValue) {
        String result = getOption(key);
        return (result != null) ? result : defaultValue;
    }
    
    public void setOption(String key, String value) {
        setOptionValues(key, new String[] { value });
    }
    
    public Long getNumberOption(String key) {
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
    
    public long getNumberOption(String key, long defaultValue) {
        String result = getOption(key);
        if (result != null) {
            try {
                return Long.parseLong(result);
            } catch (NumberFormatException e) {
                // Do nothing (logging would spam in case of a user configuration error)
            }
        }
        return defaultValue;
    }
    
    public void setNumberOption(String key, long value) {
        setOption(key, Long.toString(value));
    }
    
    public Boolean getBoolOption(String key) {
        String result = getOption(key);
        if (result != null) {
            return "true".equals(result) || "on".equals(result) || "yes".equals(result) || "1".equals(result);
        }
        return null;
    }
    
    public boolean getBoolOption(String key, boolean defaultValue) {
        Boolean result = getBoolOption(key);
        if (result != null) {
            return result.booleanValue();
        }
        return defaultValue;
    }
    
    public void setBoolOption(String key, boolean value) {
        setOption(key, Boolean.toString(value));
    }
    
    public String[] getSectionNames(String section) {
        if (section == null) {
            section = "";
        }
        HashSet<String> sn = sectionNames.get(section);
        String[] res = new String[sn.size()];
        return sn.toArray(res);
    }
    
    public String[] getOptionNames(String section) {
        HashSet<String> on = sectionNames.get(section);
        String[] res = new String[on.size()];
        return on.toArray(res);
    }
}
