package com.redhat.ceylon.common.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.FileUtil;

/**
 * This class' main function is to locate configuration files, parse them
 * using ConfigParser and return CeylonConfig objects containing the
 * information contained in those files.
 * A "transformer" can be specified that will allow a configuration to
 * be adjusted in any way just after it was read and before it will be
 * returned, merged or otherwise used.
 * 
 * Several static helper functions exist that implement several of Ceylon's
 * configuration lookup strategies.
 * 
 * @author Tako Schotanus (tako@ceylon-lang.org)
 */
public class ConfigFinder {
    private String configName;
    private String systemPropertyOverride;
    private ConfigTransformer transformer;
    
    public static interface ConfigTransformer {
        CeylonConfig transform(File file, CeylonConfig config);
    }
    
    private static final ConfigTransformer NOOP = new ConfigTransformer() {
        @Override
        public CeylonConfig transform(File file, CeylonConfig config) {
            return config;
        }
    };
    
    /**
     * @param configName The name of the configuration file
     * @param systemPropertyOverride The name of the system property that
     * can be used to override the location of the configuration file
     * (can be 'null' if this feature is not required)
     */
    public ConfigFinder(String configName, String systemPropertyOverride) {
        this(configName, systemPropertyOverride, NOOP);
    }
    
    /**
     * @param configName The name of the configuration file
     * @param systemPropertyOverride The name of the system property that
     * can be used to override the location of the configuration file
     * (can be 'null' if this feature is not required)
     * @param transformer The transformer to use just after reading
     * a configuration
     */
    public ConfigFinder(String configName, String systemPropertyOverride, ConfigTransformer transformer) {
        this.configName = configName;
        this.systemPropertyOverride = systemPropertyOverride;
        this.transformer = transformer;
    }
    
    /**
     * Returns the configuration using the default lookup strategy in reverse,
     * which is: first the system configuration, then the user's, and then in
     * reverse order going from the localDir folder up the file system
     * hierarchy all the configuration files found in any .ceylon
     * subfolders that exist. Values from later files override earlier ones.
     * 
     * @param localDir The local folder from which to start
     * @return The configuration from all the files combined
     */
    public CeylonConfig loadDefaultConfig(File localDir) {
        CeylonConfig config = new CeylonConfig();
        try {
            CeylonConfig system = loadSystemConfig();
            merge(config, system);
        } catch (IOException e) {
            // Just ignore any errors
        }
        try {
            CeylonConfig user = loadUserConfig();
            merge(config, user);
        } catch (IOException e) {
            // Just ignore any errors
        }
        try {
            CeylonConfig local = loadLocalConfig(localDir);
            merge(config, local);
        } catch (IOException e) {
            // Just ignore any errors
        }
        CeylonConfig local = loadConfigFromProperties();
        merge(config, local);
        return config;
    }

    /**
     * Returns the configuration using the default lookup strategy which is:
     * going from the localDir folder up the file system hierarchy any
     * configuration file found in any .ceylon subfolder that exists.
     * If no file was found it will then look in the user's Ceylon folder
     * and finally the system's.
     * 
     * @param localDir The local folder from which to start
     * @return The configuration from the first file found
     */
    public CeylonConfig loadFirstConfig(File localDir) {
        try {
            File configFile = findLocalConfig(localDir);
            if (configFile != null) {
                CeylonConfig localConfig = loadConfigFromFile(configFile);
                return localConfig;
            }
        } catch (IOException e) {
            // Just ignore any errors
        }
        try {
            CeylonConfig user = loadUserConfig();
            return user;
        } catch (IOException e) {
            // Just ignore any errors
        }
        try {
            CeylonConfig system = loadSystemConfig();
            return system;
        } catch (IOException e) {
            // Just ignore any errors
        }
        CeylonConfig config = new CeylonConfig();
        return config;
    }
    
    /**
     * Returns the location for the system configuration file
     * @return File pointing to the system configuration file
     */
    public File findSystemConfig() throws IOException {
        File configDir = FileUtil.getSystemConfigDir();
        if (configDir != null) {
            return new File(configDir, configName);
        } else {
            return null;
        }
    }
    
    /**
     * Returns the system configuration. Depending on the operating system this is
     * normally "/etc/ceylon/{configName}" or "%ALLUSERSPROFILE%/ceylon/{configName}".
     * @return CeylonConfig object containing the system configuration.
     * If the file was not found the configuration will contain no values.
     * @throws IOException Either actual file-related IO exceptions or
     * InvalidPropertiesFormatException when problems with the file format
     * are detected
     */
    public CeylonConfig loadSystemConfig() throws IOException {
        File configFile = findSystemConfig();
        if (configFile != null) {
            return loadConfigFromFile(configFile);
        } else {
            return new CeylonConfig();
        }
    }
    
    /**
     * Returns the location for the user configuration file.
     * This is normally "~/.ceylon/{configName}" but can be overridden by the
     * system property as optionally passed into the constructor.
     * @return File pointing to the user configuration file
     */
    public File findUserConfig() throws IOException {
        File configFile;
        String configFilename = null;
        if (systemPropertyOverride != null) {
            configFilename = System.getProperty(systemPropertyOverride);
        }
        if (configFilename != null) {
            configFile = new File(configFilename);
        } else {
            configFile = new File(new File(System.getProperty("user.home"), Constants.CEYLON_CONFIG_DIR), configName);
        }
        return configFile;
    }
    
    /**
     * Returns the user configuration
     * @return CeylonConfig object containing the user configuration.
     * If the file was not found the configuration will contain no values.
     * @throws IOException Either actual file-related IO exceptions or
     * InvalidPropertiesFormatException when problems with the file format
     * are detected
     */
    public CeylonConfig loadUserConfig() throws IOException {
        File configFile = findUserConfig();
        return loadConfigFromFile(configFile);
    }
    
    /**
     * Returns the location for the local configuration file.
     * Ceylon will look for "./.ceylon/{configName}".
     * @return File pointing to the user configuration file
     * or "null" of no such file was found.
     */
    public File findLocalConfig(File dir) throws IOException {
        if (dir != null) {
            File userConfig1 = (new File(FileUtil.getDefaultUserDir(), configName)).getCanonicalFile();
            File userConfig2 = (new File(FileUtil.getUserDir(), configName)).getCanonicalFile();
            dir = dir.getCanonicalFile();
            while (dir != null) {
                File configFile = new File(new File(dir, Constants.CEYLON_CONFIG_DIR), configName);
                if (configFile.equals(userConfig1) || configFile.equals(userConfig2)) {
                    // We stop if we reach $HOME/.ceylon/{configName} or whatever is defined by -Dceylon.user.config
                    break;
                }
                if (configFile.isFile()) {
                    return configFile;
                }
                dir = dir.getParentFile();
            }
        }
        // We didn't find any local config file
        return null;
    }
    
    /**
     * Returns the local configuration. This is done by recursively going
     * up the file system hierachy starting with the directory passed to
     * the method. In each folder Ceylon will look if a local configuration
     * file exists ("./.ceylon/{configName}"). When reaching the root of the
     * file system all configurations will be applied in reverse order
     * (from "shallowest", closest to root, to "deepest") merging all
     * configurations found into a single result.
     * @return CeylonConfig object containing the merged configuration.
     * If the file was not found the configuration will contain no values.
     * @throws IOException Either actual file-related IO exceptions or
     * InvalidPropertiesFormatException when problems with the file format
     * are detected
     */
    public CeylonConfig loadLocalConfig(File dir) throws IOException {
        File configFile = findLocalConfig(dir);
        if (configFile != null) {
            CeylonConfig parentConfig = loadLocalConfig(dir.getCanonicalFile().getParentFile());
            CeylonConfig localConfig = loadConfigFromFile(configFile);
            return merge(parentConfig, localConfig);
        } else {
            // No config file, just return an empty CeylonConfig
            return new CeylonConfig();
        }
    }
    
    /**
     * Returns the configuration contained in the given file
     * @param configFile The file to read
     * @return CeylonConfig object containing the requested configuration.
     * If the file was not found the configuration will contain no values.
     * @throws IOException Either actual file-related IO exceptions or
     * InvalidPropertiesFormatException when problems with the file format
     * are detected
     */
    public CeylonConfig loadConfigFromFile(File configFile) throws IOException {
        return transformer.transform(configFile, (new ConfigParser(configFile)).parse(true));
    }
    
    /**
     * Returns the configuration contained in the given file but NO variable
     * substitution or other transformations will be performed
     * @param configFile The file to read
     * @return CeylonConfig object containing the requested configuration.
     * If the file was not found the configuration will contain no values.
     * @throws IOException Either actual file-related IO exceptions or
     * InvalidPropertiesFormatException when problems with the file format
     * are detected
     */
    public CeylonConfig loadOriginalConfigFromFile(File configFile) throws IOException {
        return (new ConfigParser(configFile)).parse(false);
    }
    
    /**
     * Returns the configuration contained in the given input stream.
     * If the "currentDir" parameter is given variable substitution will
     * occur for the values in the stream, specifically occurrences of
     * ${DIR} will be replaced by the path pointed to by the parameter.
     * If the parameter is "null" no substitutions will occur.
     * @param stream The InputStream to read from
     * @param currentDir The folder to use for ${DIR} substitutions or null
     * if no such substitutions should be performed
     * @return CeylonConfig object containing the requested configuration
     * @throws IOException Either actual file-related IO exceptions or
     * InvalidPropertiesFormatException when problems with the file format
     * are detected
     */
    public CeylonConfig loadConfigFromStream(InputStream stream, File currentDir) throws IOException {
        return transformer.transform(null, (new ConfigParser(stream, currentDir)).parse(currentDir != null));
    }
    
    /**
     * Returns a configuration initialized with the values encountered
     * in the System properties. Values with the name "ceylon.config.xxxx"
     * are turned into CeylonConfig options of the name "xxxx".
     * @return CeylonConfig object containing the configuration values
     * encountered in the System properties
     */
    public CeylonConfig loadConfigFromProperties() {
        CeylonConfig cfg = new CeylonConfig();
        for (String key : System.getProperties().stringPropertyNames()) {
            if (key.startsWith("ceylon.config.")) {
                String nm = key.substring(14);
                cfg.setOption(nm, System.getProperty(key));
            }
        }
        return cfg;
    }
    
    private CeylonConfig merge(CeylonConfig pool, CeylonConfig config) {
        return pool.merge(config);
    }
}
