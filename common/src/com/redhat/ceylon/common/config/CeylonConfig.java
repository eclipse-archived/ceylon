package com.redhat.ceylon.common.config;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * Class to hold, retrieve and set Ceylon configuration values (options).
 * This is basically a Map from key strings to value strings.
 * Ceylon options are grouped in sections and which can be part of
 * yet other sections. Even though this results in a hierarchical
 * option "tree" the option names are "fully qualified" to keep the
 * API as simple as possible.
 * 
 * This means that a configuration section like this:
 * 
 *    [testsection]
 *    option1=foo
 *    option2=bar
 *    
 *    [testsection.subsection]
 *    option1=baz
 *    
 * Is actually represented as the following 3 options:
 * 
 *    testsection.option1=foo
 *    testsection.option2=bar
 *    testsection.subsection.option1=baz
 *    
 * Several static helper methods exists for easy access to the default
 * Ceylon configuration.
 * 
 * @author Tako Schotanus (tako@ceylon-lang.org)
 */
public class CeylonConfig {
    private HashMap<String, String[]> options;
    private HashMap<String, HashSet<String>> sectionNames;
    private HashMap<String, HashSet<String>> optionNames;
    
    private volatile static CeylonConfig instance;
    
    /**
     * Retrieves the default configuration for the current JVM.
     * WARNING: This actually uses the current working directory of the
     * virtual machine, but the configuration is only determined once,
     * so if the CWD changes the configuration will still reflect the
     * state according to the old value! 
     * @return Default CeylonConfig object
     */
    public static CeylonConfig get() {
        if (instance == null) {
            synchronized (CeylonConfig.class) {
                if (instance == null) {
                    instance = createFromLocalDir(new File("."));
                }
            }
        }
        return instance;
    }

    /**
     * Set or override the default configuration
     * @param config The CelyonConfig object to use as the default 
     */
    public static void set(CeylonConfig config) {
        instance = config;
    }

    /**
     * Retrieve the given option value from the default configuration
     * @param key The name of the option to retrieve
     * @return The value of the option or "null" if it wasn't found
     */
    public static String get(String key) {
        return get().getOption(key);
    }

    /**
     * Retrieve the given option value from the default configuration
     * @param key The name of the option to retrieve
     * @param defaultValue The default value to use if the option wasn't found
     * @return The value of the option or the default value
     */
    public static String get(String key, String defaultValue) {
        return get().getOption(key, defaultValue);
    }

    /**
     * Returns a newly created default configuration (see ConfigParser.loadDefaultConfig())
     * @param localDir The directory to start looking for local configuration files
     * @return Default CeylonConfig object
     */
    public static CeylonConfig createFromLocalDir(File localDir) {
        return ConfigParser.loadDefaultConfig(localDir);
    }
    
    public CeylonConfig() {
        options = new LinkedHashMap<String, String[]>();
        sectionNames = new LinkedHashMap<String, HashSet<String>>();
        sectionNames.put("", new LinkedHashSet<String>());
        optionNames = new LinkedHashMap<String, HashSet<String>>();
    }
    
    static class Key {
        private String subsectionName; 
        private String optionName;
        private String sectionName;
        private String parentSectionName;
        
        public String getSubsectionName() {
            return subsectionName;
        }


        public String getOptionName() {
            return optionName;
        }


        public String getSectionName() {
            return sectionName;
        }


        public String getParentSectionName() {
            return parentSectionName;
        }

        public Key(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Illegal key");
            }
            String[] parts = key.split("\\.");
            if (parts.length < 2) {
                throw new IllegalArgumentException("Illegal key '" + key + "', needs a section name");
            }
            subsectionName = parts[parts.length - 2]; 
            optionName = parts[parts.length - 1];
            parentSectionName = "";
            if (parts.length > 2) {
                for (int i = 0; i < parts.length - 2; i++) {
                    if (i > 0) {
                        parentSectionName += '.';
                    }
                    parentSectionName += parts[i];
                }
                sectionName = parentSectionName + '.' + subsectionName;
            } else {
                sectionName = subsectionName;
            }
        }
    }
    
    private void initLookupKey(String key) {
        Key k = new Key(key);

        if (!k.getParentSectionName().isEmpty()) {
            initLookupKey(k.getParentSectionName() + ".#");
        }
        
        HashSet<String> psn = sectionNames.get(k.getParentSectionName());
        psn.add(k.getSubsectionName());
        
        HashSet<String> sn = sectionNames.get(k.getSectionName());
        if (sn == null) {
            sn = new LinkedHashSet<String>();
            sectionNames.put(k.getSectionName(), sn);
        }
        
        if (!"#".equals(k.getOptionName())) {
            HashSet<String> on = optionNames.get(k.getSectionName());
            if (on == null) {
                on = new LinkedHashSet<String>();
                optionNames.put(k.getSectionName(), on);
            }
            on.add(k.getOptionName());
        }
    }
    
    /**
     * Returns the "size" of the configuration which is defined
     * as the number of unique option names
     * @return The size of the configuration
     */
    public synchronized int size() {
        return options.size();
    }
    
    /**
     * Determines if an option with the given name exists
     * @param key The name of the option to check for
     * @return Boolean indicating if the option exists
     */
    public synchronized boolean isOptionDefined(String key) {
        return options.containsKey(key);
    }

    /**
     * Retrieves the array of values defined for the given option
     * @param key The name of the option to retrieve
     * @return The array of values or "null" if the option didn't exist
     */
    public synchronized String[] getOptionValues(String key) {
        return options.get(key);
    }

    /**
     * Defines the array of values for the given option, if passing
     * "null" the option will be removed from the configuration
     * @param key The name of the option to define
     * @param values Array of values to use or "null"
     */
    public synchronized void setOptionValues(String key, String[] values) {
        if (values != null && values.length > 0) {
            options.put(key, values);
            initLookupKey(key);
        } else {
            removeOption(key);
        }
    }

    /**
     * Retrieves a single value for the given option. If more than one
     * value exits only the first one is returned
     * @param key The name of the option to retrieve
     * @return The (first) value of the option or "null" if the option didn't exist 
     */
    public String getOption(String key) {
        String[] result = getOptionValues(key);
        return (result != null) ? result[0] : null;
    }
    
    /**
     * Retrieves a single value for the given option. If more than one
     * value exits only the first one is returned
     * @param key The name of the option to retrieve
     * @param defaultValue The default value to use if the option wasn't found
     * @return The (first) value of the option or the default value
     */
    public String getOption(String key, String defaultValue) {
        String result = getOption(key);
        return (result != null) ? result : defaultValue;
    }
    
    /**
     * Defines a sinlge value for the given option, if passing
     * "null" the option will be removed from the configuration
     * @param key The name of the option to define
     * @param value The value to use or "null"
     */
    public void setOption(String key, String value) {
        if (value != null) {
            setOptionValues(key, new String[] { value });
        } else {
            removeOption(key);
        }
    }
    
    /**
     * Retrieves a single numeric value for the given option. If more than one
     * value exits only the first one is returned
     * @param key The name of the option to retrieve
     * @return The (first) value of the option or "null" if the option didn't exist
     * or if it wasn't a valid number
     */
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
    
    /**
     * Retrieves a single numeric value for the given option. If more than one
     * value exits only the first one is returned
     * @param key The name of the option to retrieve
     * @param defaultValue The default value to use if the option wasn't found
     * @return The (first) value of the option, "null" if it wasn't a valid number
     * or the default value if the option didn't exist
     */
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
    
    /**
     * Defines a single numeric value for the given option
     * @param key The name of the option to define
     * @param value The numeric value to use
     */
    public void setNumberOption(String key, long value) {
        setOption(key, Long.toString(value));
    }
    
    /**
     * Retrieves a single boolean value for the given option. If more than one
     * value exits only the first one is returned. The strings "true", "on",
     * "yes" and "1" are considered to be "true", everything else is "false".
     * @param key The name of the option to retrieve
     * @return The (first) value of the option or "null" if the option didn't exist
     */
    public Boolean getBoolOption(String key) {
        String result = getOption(key);
        if (result != null) {
            return "true".equals(result) || "on".equals(result) || "yes".equals(result) || "1".equals(result);
        }
        return null;
    }
    
    /**
     * Retrieves a single boolean value for the given option. If more than one
     * value exits only the first one is returned. The strings "true", "on",
     * "yes" and "1" are considered to be "true", everything else is "false".
     * @param key The name of the option to retrieve
     * @param defaultValue The default value to use if the option wasn't found
     * @return The (first) value of the option or the default value if the option
     * didn't exist
     */
    public boolean getBoolOption(String key, boolean defaultValue) {
        Boolean result = getBoolOption(key);
        if (result != null) {
            return result.booleanValue();
        }
        return defaultValue;
    }
    
    /**
     * Defines a single boolean value for the given option
     * @param key The name of the option to define
     * @param value The boolean value to use
     */
    public void setBoolOption(String key, boolean value) {
        setOption(key, Boolean.toString(value));
    }

    /**
     * Removes the given option (does nothing if it doesn't exist)
     * @param key The name of the option to remove
     */
    public synchronized void removeOption(String key) {
        Key k = new Key(key);
        options.remove(key);
        HashSet<String> on = optionNames.get(k.getSectionName());
        if (on != null) {
            on.remove(k.getOptionName());
            if (on.isEmpty()) {
                cleanupSection(k.getSectionName());
            }
        }
    }
    
    private void cleanupSection(String sectionName) {
        HashSet<String> on = optionNames.get(sectionName);
        if (on != null && on.isEmpty()) {
            optionNames.remove(sectionName);
        }
        HashSet<String> sn = sectionNames.get(sectionName);
        if (sn != null && sn.isEmpty()) {
            sectionNames.remove(sectionName);
        }
        if (!optionNames.containsKey(sectionName) && !sectionNames.containsKey(sectionName)) {
            Key k = new Key(sectionName + ".dummy");
            HashSet<String> psn = sectionNames.get(k.getParentSectionName());
            psn.remove(k.getSubsectionName());
            if (!k.getParentSectionName().isEmpty()) {
                cleanupSection(k.getParentSectionName());
            }
        }
    }

    /**
     * Determines if a section with the given name exists
     * @param section The name of the section to check for
     * @return Boolean indicating if the section exists
     */
    public synchronized boolean isSectionDefined(String section) {
        return sectionNames.containsKey(section);
    }

    /**
     * Removes the given section and all its options
     * (does nothing if it doesn't exist)
     * @param key The name of the option to remove
     */
    public synchronized void removeSection(String section) {
        String sectionDot = section + ".";
        if (isSectionDefined(section)) {
            LinkedHashSet<String> keys = new LinkedHashSet<String>(options.keySet());
            for (String key : keys) {
                if (key.startsWith(sectionDot)) {
                    removeOption(key);
                }
            }
        }
    }
    
    /**
     * Returns the list of all section names, the root section names
     * or the sub section names of the given section depending on the
     * argument being passed
     * @param section Returns the subsections of the section being passed.
     * Will return the root section names if being passed an empty string.
     * And will return all section names if being passed null.
     * @return An array of the requested section names
     */
    public synchronized String[] getSectionNames(String section) {
        HashSet<String> sn;
        if (section != null) {
            sn = sectionNames.get(section);
        } else {
            sn = new LinkedHashSet<String>(sectionNames.keySet());
            sn.remove("");
        }
        String[] res = new String[sn.size()];
        return sn.toArray(res);
    }
    
    /**
     * Returns an array of option names in a given section or the list
     * of all option names if "null" is passed
     * @param section The name of the section or "null"
     * @return An array of option names or "null" if the section doesn't exist
     */
    public synchronized String[] getOptionNames(String section) {
        if (section == null) {
            String[] res = new String[options.keySet().size()];
            return options.keySet().toArray(res);
        } else {
            if (isSectionDefined(section)) {
                HashSet<String> on = optionNames.get(section);
                if (on != null) {
                    String[] res = new String[on.size()];
                    return on.toArray(res);
                } else {
                    // A section can have only subsections and
                    // no options of its own
                    return new String[0];
                }
            } else {
                return null;
            }
        }
    }

    /**
     * Merges the options from the given configuration with the current
     * one where duplicate options that exist locally will be overwritten
     * by the ones encountered in the given configuration.
     * @param local
     * @return
     */
    public synchronized CeylonConfig merge(CeylonConfig other) {
        for (String key : other.getOptionNames(null)) {
            String[] values = other.getOptionValues(key);
            setOptionValues(key, values);
        }
        return this;
    }

    /**
     * Returns an exact and safe copy of the current configuration
     * @return A clone of the current configuration
     */
    public CeylonConfig copy() {
        CeylonConfig cfg = new CeylonConfig();
        cfg.merge(this);
        return cfg;
    }

    @Override
    public String toString() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ConfigWriter.write(this, out);
            return out.toString("UTF-8");
        } catch (IOException e) {
            return super.toString();
        }
    }
}
