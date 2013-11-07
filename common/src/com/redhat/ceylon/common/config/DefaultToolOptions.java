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
    public final static String COMPILER_DOC = "compiler.doc";
    
    public final static String RUNTOOL_COMPILE = "runtool.compile";
    public final static String TESTTOOL_COMPILE = "testtool.compile";
    
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

    public static List<File> getCompilerDocDirs() {
        return getCompilerDocDirs(CeylonConfig.get());
    }

    public static List<File> getCompilerDocDirs(CeylonConfig config) {
        String[] dirs = config.getOptionValues(COMPILER_DOC);
        if (dirs != null) {
            return Arrays.asList(FileUtil.pathsToFileArray(dirs));
        } else {
            return Collections.singletonList(new File(Constants.DEFAULT_DOC_DIR));
        }
    }

    public static File getCompilerOutDir() {
        return getCompilerOutDir(CeylonConfig.get());
    }

    public static File getCompilerOutDir(CeylonConfig config) {
        return new File(Repositories.withConfig(config).getOutputRepository().getUrl());
    }
    
    public static String getRunToolCompileFlags() {
        return getRunToolCompileFlags(CeylonConfig.get());
    }

    public static String getRunToolCompileFlags(CeylonConfig config) {
        return config.getOption(RUNTOOL_COMPILE, Constants.DEFAULT_RUNTOOL_COMPILATION_FLAGS);
    }
    
    public static String getTestToolCompileFlags() {
        return getTestToolCompileFlags(CeylonConfig.get());
    }

    public static String getTestToolCompileFlags(CeylonConfig config) {
        return config.getOption(TESTTOOL_COMPILE, Constants.DEFAULT_TESTTOOL_COMPILATION_FLAGS);
    }
}
