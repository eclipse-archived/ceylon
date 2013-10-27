package com.redhat.ceylon.common.config;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.FileUtil;

public class DefaultToolOptions {
    
    public final static String DEFAULTS_ENCODING = "defaults.encoding";
    public final static String DEFAULTS_OFFLINE = "defaults.offline";
    
    public final static String COMPILER_SOURCE = "compiler.source";
    public final static String COMPILER_RESOURCE = "compiler.resource";
    
    private DefaultToolOptions() {
    }
    
    public static String getDefaultEncoding() {
        return getDefaultEncoding(CeylonConfig.get());
    }
    
    public static String getDefaultEncoding(CeylonConfig config) {
        return config.getOption(DEFAULTS_ENCODING);
    }
    
    public static boolean getDefaultOffline() {
        return getDefaultOffline(CeylonConfig.get());
    }
    
    public static boolean getDefaultOffline(CeylonConfig config) {
        return config.getBoolOption(DEFAULTS_OFFLINE, false);
    }
    
    public static List<File> getCompilerSourceDirs() {
        return getCompilerSourceDirs(CeylonConfig.get());
    }
    
    public static List<File> getCompilerSourceDirs(CeylonConfig config) {
        String[] dirs = config.getOptionValues(COMPILER_SOURCE);
        if (dirs != null) {
            return Arrays.asList(FileUtil.pathsToFileArray(dirs));
        } else {
            return Collections.singletonList(new File(Constants.DEFAULT_SOURCE_DIR));
        }
    }
    
    public static List<File> getCompilerResourceDirs() {
        return getCompilerResourceDirs(CeylonConfig.get());
    }

    public static List<File> getCompilerResourceDirs(CeylonConfig config) {
        String[] dirs = config.getOptionValues(COMPILER_RESOURCE);
        if (dirs != null) {
            return Arrays.asList(FileUtil.pathsToFileArray(dirs));
        } else {
            return Collections.singletonList(new File(Constants.DEFAULT_RESOURCE_DIR));
        }
    }
    
    public static File getCompilerOutDir() {
        return getCompilerOutDir(CeylonConfig.get());
    }

    public static File getCompilerOutDir(CeylonConfig config) {
        return new File(Repositories.withConfig(config).getOutputRepository().getUrl());
    }
}
