package com.redhat.ceylon.common.config;

import java.io.File;
import java.io.IOException;

import com.redhat.ceylon.common.Constants;

public class CeylonConfigFinder {
    public static final ConfigFinder DEFAULT = new ConfigFinder(Constants.CEYLON_CONFIG_FILE, Constants.PROP_CEYLON_CONFIG_FILE);
    
    public static CeylonConfig loadDefaultConfig(File localDir) {
        return DEFAULT.loadDefaultConfig(localDir);
    }
    
    public static CeylonConfig loadLocalConfig(File dir) throws IOException {
        return DEFAULT.loadLocalConfig(dir);
    }
    
    public static CeylonConfig loadConfigFromFile(File configFile) throws IOException {
        return DEFAULT.loadConfigFromFile(configFile);
    }
    
    public static CeylonConfig loadOriginalConfigFromFile(File configFile) throws IOException {
        return DEFAULT.loadOriginalConfigFromFile(configFile);
    }
}
