package com.redhat.ceylon.common.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.redhat.ceylon.common.FileUtil;

/**
 * This class' main function is to parse configuration files.
 * It reads them using ConfigReader and returns CeylonConfig objects
 * containing the information stored in those files.
 * 
 * @author Tako Schotanus (tako@ceylon-lang.org)
 */
public class ConfigParser {
    private File configFile;
    private File currentDir;
    private CeylonConfig config;
    private InputStream in;
    
    public ConfigParser(File configFile) {
        this.configFile = configFile;
        this.currentDir = configFile.getParentFile();
    }
    
    public ConfigParser(InputStream in, File currentDir) {
        this.in = in;
        this.currentDir = currentDir;
    }
    
    public CeylonConfig parse(final boolean replaceVars) throws IOException {
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
                        // and a couple of other special directories
                        if (value.startsWith("${DIR}")) {
                            value = FileUtil.absoluteFile(currentDir).getPath() + value.substring(6);
                        } else if (value.startsWith("${USER_DIR}")) {
                            value = FileUtil.absoluteFile(FileUtil.getUserDir()).getPath() + value.substring(11);
                        } else if (value.startsWith("${SYSTEM_DIR}")) {
                            value = FileUtil.absoluteFile(FileUtil.getSystemConfigDir()).getPath() + value.substring(13);
                        } else if (value.startsWith("${CEYLON_HOME}") || value.startsWith("${INSTALL_DIR}")) {
                            if (FileUtil.getInstallDir() != null) {
                                value = FileUtil.absoluteFile(FileUtil.getInstallDir()).getPath() + value.substring(14);
                            }
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
