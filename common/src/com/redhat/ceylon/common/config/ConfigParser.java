package com.redhat.ceylon.common.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.redhat.ceylon.common.FileUtil;

/**
 * This class' main function is to locate Ceylon config files, read them
 * using ConfigReader and return CeylonConfig objects containing the
 * information contained in those files.
 * 
 * Several static helper functions exist that implement several of Ceylon's
 * configuration lookup strategies.
 * 
 * @author Tako Schotanus (tako@ceylon-lang.org)
 */
public class ConfigParser {
    private File configFile;
    private File currentDir;
    private CeylonConfig config;
    private InputStream in;
    
    /**
     * Name of system property that can be sued to override the location
     * of the user's configuration file
     */
    public static final String PROP_CEYLON_CONFIG_FILE = "ceylon.config";
    
    /**
     * Returns the configuration using the default lookup strategy, which
     * is: first the system configuration, then the user's, and then in
     * reverse order going from the localDir folder up the file system
     * hierarchy all the configuration files encountered in any .ceylon
     * subfolders encountered. Values from later files override earlier ones.
     * 
     * @param localDir The local folder from which to start
     * @return The configuration from all the files combined
     */
    public static CeylonConfig loadDefaultConfig(File localDir) {
        CeylonConfig config = new CeylonConfig();
        try {
            CeylonConfig system = ConfigParser.loadSystemConfig();
            config.merge(system);
        } catch (IOException e) {
            // Just ignore any errors
        }
        try {
            CeylonConfig user = ConfigParser.loadUserConfig();
            config.merge(user);
        } catch (IOException e) {
            // Just ignore any errors
        }
        try {
            CeylonConfig local = ConfigParser.loadLocalConfig(localDir);
            config.merge(local);
        } catch (IOException e) {
            // Just ignore any errors
        }
        return config;
    }
    
    /**
     * Returns the location for the system configuration file
     * @return File pointing to the system configuration file
     */
    public static File findSystemConfig() throws IOException {
        File configDir = FileUtil.getSystemConfigDir();
        if (configDir != null) {
            return new File(configDir, "config");
        } else {
            return null;
        }
    }
    
    /**
     * Returns the system configuration. Depending on the operating system this
     * is normally "/etc/ceylon/config" or "%ALLUSERSPROFILE%/ceylon/config".
     * @return CeylonConfig object containing the system configuration.
     * If the file was not found the configuration will contain no values.
     * @throws IOException Either actual file-related IO exceptions or
     * InvalidPropertiesFormatException when problems with the file format
     * are detected
     */
    public static CeylonConfig loadSystemConfig() throws IOException {
        File configFile = findSystemConfig();
        return (new ConfigParser(configFile)).parse(true);
    }
    
    /**
     * Returns the location for the user configuration file.
     * This is normally "~/.ceylon/config" but can be overridden by the
     * "ceylon.config" system property.
     * @return File pointing to the user configuration file
     */
    public static File findUserConfig() throws IOException {
        File configFile;
        String configFilename = System.getProperty(PROP_CEYLON_CONFIG_FILE);
        if (configFilename != null) {
            configFile = new File(configFilename);
        } else {
            configFile = new File(new File(System.getProperty("user.home"), ".ceylon"), "config");
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
    public static CeylonConfig loadUserConfig() throws IOException {
        File configFile = findUserConfig();
        return (new ConfigParser(configFile)).parse(true);
    }
    
    /**
     * Returns the location for the local configuration file.
     * Ceylon will look for "./.ceylon/config".
     * @return File pointing to the user configuration file
     * or "null" of no such file was found.
     */
    public static File findLocalConfig(File dir) throws IOException {
        if (dir != null) {
            File userConfig1 = (new File(FileUtil.getDefaultUserDir(), "config")).getCanonicalFile();
            File userConfig2 = (new File(FileUtil.getUserDir(), "config")).getCanonicalFile();
            dir = dir.getCanonicalFile();
            while (dir != null) {
                File configFile = new File(dir, ".ceylon/config");
                if (configFile.equals(userConfig1) || configFile.equals(userConfig2)) {
                    // We stop if we reach $HOME/.ceylon/config or whatever is defined by -Dceylon.user.config
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
     * file exists ("./.ceylon/config"). When reaching the root of the
     * file system all configurations will be applied in reverse order
     * (from "shallowest", closest to root, to "deepest") merging all
     * configurations found into a single result.
     * @return CeylonConfig object containing the merged configuration.
     * If the file was not found the configuration will contain no values.
     * @throws IOException Either actual file-related IO exceptions or
     * InvalidPropertiesFormatException when problems with the file format
     * are detected
     */
    public static CeylonConfig loadLocalConfig(File dir) throws IOException {
        File configFile = findLocalConfig(dir);
        if (configFile != null) {
            CeylonConfig parentConfig = loadLocalConfig(dir.getCanonicalFile().getParentFile());
            CeylonConfig localConfig = new ConfigParser(configFile).parse(true);
            return parentConfig.merge(localConfig);
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
    public static CeylonConfig loadConfigFromFile(File configFile) throws IOException {
        return (new ConfigParser(configFile)).parse(true);
    }
    
    /**
     * Returns the configuration contained in the given file but NO variable
     * substitution will be performed
     * @param configFile The file to read
     * @return CeylonConfig object containing the requested configuration.
     * If the file was not found the configuration will contain no values.
     * @throws IOException Either actual file-related IO exceptions or
     * InvalidPropertiesFormatException when problems with the file format
     * are detected
     */
    public static CeylonConfig loadOriginalConfigFromFile(File configFile) throws IOException {
        return (new ConfigParser(configFile)).parse(false);
    }
    
    /**
     * Returns the configuration contained in the given input stream.
     * If the "currentDir" parameter is given variable substitution will
     * occur for the values in the stream, specifically occurrences of
     * ${DIR} will be replaced by the path pointed to by the parameter.
     * If the apraemter is "null" no substitutions will occur.
     * @param stream The InputStream to read from
     * @param currentDir The folder to use for ${DIR} substitutions or null
     * if no such substitutions should be performed
     * @return CeylonConfig object containing the requested configuration
     * @throws IOException Either actual file-related IO exceptions or
     * InvalidPropertiesFormatException when problems with the file format
     * are detected
     */
    public static CeylonConfig loadConfigFromStream(InputStream stream, File currentDir) throws IOException {
        return (new ConfigParser(stream, currentDir)).parse(currentDir != null);
    }
    
    private ConfigParser(File configFile) {
        this.configFile = configFile;
        this.currentDir = configFile.getParentFile();
    }
    
    private ConfigParser(InputStream in, File currentDir) {
        this.in = in;
        this.currentDir = currentDir;
    }
    
    private CeylonConfig parse(final boolean replaceVars) throws IOException {
        config = new CeylonConfig();
        if (configFile == null || configFile.isFile()) {
            if (configFile != null) {
                in = new FileInputStream(configFile);
            }
            ConfigReader reader = new ConfigReader(in, new ConfigReaderListener() {

                @Override
                public void setup() throws IOException {
                    // We ignore the setup
                }

                @Override
                public void onSection(String section, String text) {
                    // We ignore sections
                }

                @Override
                public void onOption(String name, String value, String text) {
                    if (replaceVars) {
                        // Special "variable" to get the current directory for this config file
                        if (value.startsWith("${DIR}")) {
                            try {
                                value = currentDir.getCanonicalPath() + value.substring(6);
                            } catch (IOException e) { }
                        }
                    }
                    
                    String[] oldval = config.getOptionValues(name);
                    if (oldval == null) {
                        config.setOption(name, value);
                    } else {
                        String[] newVal = Arrays.copyOf(oldval, oldval.length + 1);
                        newVal[oldval.length] = value;
                        config.setOptionValues(name, newVal);
                    }
                }

                @Override
                public void onComment(String text) {
                    // We ignore comments
                }

                @Override
                public void onWhitespace(String text) {
                    // We ignore white space
                }

                @Override
                public void cleanup() throws IOException {
                    // We ignore the cleanup
                }
                
            });
            try {
                reader.process();
            } catch (IOException ex) {
                System.err.print("Error parsing configuration");
                if (configFile != null) {
                    System.err.print(" '" + FileUtil.relativeFile(configFile).getPath() + "'");
                }
                System.err.println(": " + ex.getMessage());
            }
        } else {
            throw new FileNotFoundException("Couldn't open configuration file");
        }

        return config;
    }
}
