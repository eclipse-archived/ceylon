package com.redhat.ceylon.common.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.redhat.ceylon.common.FileUtil;

public class ConfigParser {
    private File configFile;
    private File currentDir;
    private CeylonConfig config;
    private InputStream in;
    
    public static final String PROP_CEYLON_CONFIG_FILE = "ceylon.config";
    
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
    
    public static File findSystemConfig() throws IOException {
        File configDir = FileUtil.getSystemConfigDir();
        if (configDir != null) {
            return new File(configDir, "config");
        } else {
            return null;
        }
    }
    
    public static CeylonConfig loadSystemConfig() throws IOException {
        File configFile = findSystemConfig();
        return (new ConfigParser(configFile)).parse(true);
    }
    
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
    
    public static CeylonConfig loadUserConfig() throws IOException {
        File configFile = findUserConfig();
        return (new ConfigParser(configFile)).parse(true);
    }
    
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
    
    public static CeylonConfig loadConfigFromFile(File configFile) throws IOException {
        return (new ConfigParser(configFile)).parse(true);
    }
    
    public static CeylonConfig loadOriginalConfigFromFile(File configFile) throws IOException {
        return (new ConfigParser(configFile)).parse(false);
    }
    
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
            reader.process();
        } else {
            throw new FileNotFoundException("Couldn't open configuration file");
        }

        return config;
    }
}
