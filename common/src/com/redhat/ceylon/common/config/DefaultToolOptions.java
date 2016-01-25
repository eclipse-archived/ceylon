package com.redhat.ceylon.common.config;

import java.io.File;
import java.net.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.FileUtil;

public class DefaultToolOptions {
    
    public final static String DEFAULTS_ENCODING = "defaults.encoding";
    public final static String DEFAULTS_OFFLINE = "defaults.offline";
    public final static String DEFAULTS_TIMEOUT = "defaults.timeout";
    // BACKWARDS-COMPAT
    public final static String DEFAULTS_MAVENOVERRIDES = "defaults.mavenoverrides";
    public final static String DEFAULTS_OVERRIDES = "defaults.overrides";
    public final static String DEFAULTS_FLAT_CLASSPATH = "defaults.flatclasspath";
    public final static String DEFAULTS_AUTO_EPORT_MAVEN_DEPENDENCIES = "defaults.autoexportmavendependencies";
    
    public final static String COMPILER_SOURCE = "compiler.source";
    public final static String COMPILER_RESOURCE = "compiler.resource";
    public final static String COMPILER_RESOURCE_ROOT = "compiler.resourceroot";
    public final static String COMPILER_SCRIPT = "compiler.script";
    public final static String COMPILER_DOC = "compiler.doc";
    public final static String COMPILER_SUPPRESSWARNING = "compiler.suppresswarning";
    public final static String COMPILER_NOOSGI = "compiler.noosgi";
    public final static String COMPILER_OSGIPROVIDEDBUNDLES = "compiler.osgiprovidedbundles";
    public final static String COMPILER_NOPOM = "compiler.nopom";
    public final static String COMPILER_GENERATE_MODULE_INFO = "compiler.generatemoduleinfo";
    public final static String COMPILER_PACK200 = "compiler.pack200";
    public final static String COMPILER_TARGET = "compiler.target";
    
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
    
    public static long getDefaultTimeout() {
        return getDefaultTimeout(CeylonConfig.get());
    }
    
    public static long getDefaultTimeout(CeylonConfig config) {
        return timeoutFromString(config.getOption(DEFAULTS_TIMEOUT), Constants.DEFAULT_TIMEOUT);
    }
    
    public static int timeoutFromString(String num, int defaultTimeout) {
        if (num != null) {
            int fact = 1000;
            if (num.endsWith("ms")) {
                num = num.substring(0, num.length() - 2);
                fact = 1;
            }
            return Integer.parseInt(num) * fact;
        } else {
            return defaultTimeout;
        }
    }
    
    public static Proxy getDefaultProxy() {
        return getDefaultProxy(CeylonConfig.get());
    }
    
    public static Proxy getDefaultProxy(CeylonConfig config) {
        Authentication auth = Authentication.fromConfig(config);
        return auth.getProxy();
    }
    
    public static String getDefaultOverrides() {
        return getDefaultOverrides(CeylonConfig.get());
    }
    
    public static String getDefaultOverrides(CeylonConfig config) {
        String ov = config.getOption(DEFAULTS_OVERRIDES);
        if(ov != null)
            return ov;
        // backwards compat
        return config.getOption(DEFAULTS_MAVENOVERRIDES);
    }

    public static boolean getDefaultFlatClasspath() {
        return getDefaultFlatClasspath(CeylonConfig.get());
    }

    public static boolean getDefaultFlatClasspath(CeylonConfig config) {
        return config.getBoolOption(DEFAULTS_FLAT_CLASSPATH, false);
    }
    
    public static boolean getDefaultAutoExportMavenDependencies() {
        return getDefaultAutoExportMavenDependencies(CeylonConfig.get());
    }

    public static boolean getDefaultAutoExportMavenDependencies(CeylonConfig config) {
        return config.getBoolOption(DEFAULTS_AUTO_EPORT_MAVEN_DEPENDENCIES, false);
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

    public static List<File> getCompilerScriptDirs() {
        return getCompilerScriptDirs(CeylonConfig.get());
    }

    public static List<File> getCompilerScriptDirs(CeylonConfig config) {
        String[] dirs = config.getOptionValues(COMPILER_SCRIPT);
        if (dirs != null) {
            return Arrays.asList(FileUtil.pathsToFileArray(dirs));
        } else {
            return Collections.singletonList(new File(Constants.DEFAULT_SCRIPT_DIR));
        }
    }

    public static String getCompilerResourceRootName() {
        return getCompilerResourceRootName(CeylonConfig.get());
    }

    public static String getCompilerResourceRootName(CeylonConfig config) {
        return config.getOption(COMPILER_RESOURCE_ROOT, Constants.DEFAULT_RESOURCE_ROOT);
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

    public static String getCompilerOutputRepo() {
        return getCompilerOutputRepo(CeylonConfig.get());
    }

    public static String getCompilerOutputRepo(CeylonConfig config) {
        return Repositories.withConfig(config).getOutputRepository().getUrl();
    }

    public static List<String> getCompilerSuppressWarnings() {
        return getCompilerSuppressWarnings(CeylonConfig.get());
    }

    public static List<String> getCompilerSuppressWarnings(CeylonConfig config) {
        String[] warnings = config.getOptionValues(COMPILER_SUPPRESSWARNING);
        if (warnings != null) {
            return Arrays.asList(warnings);
        } else {
            return null;
        }
    }
    
    public static boolean getCompilerNoOsgi() {
        return getCompilerNoOsgi(CeylonConfig.get());
    }
    
    public static boolean getCompilerNoOsgi(CeylonConfig config) {
        return config.getBoolOption(COMPILER_NOOSGI, false);
    }

    public static String getCompilerOsgiProvidedBundles() {
        return getCompilerOsgiProvidedBundles(CeylonConfig.get());
    }
    
    public static String getCompilerOsgiProvidedBundles(CeylonConfig config) {
        return config.getOption(COMPILER_OSGIPROVIDEDBUNDLES, "");
    }

    public static boolean getCompilerNoPom() {
        return getCompilerNoPom(CeylonConfig.get());
    }

    public static boolean getCompilerNoPom(CeylonConfig config) {
        return config.getBoolOption(COMPILER_NOPOM, false);
    }

    public static boolean getCompilerGenerateModuleInfo() {
        return getCompilerGenerateModuleInfo(CeylonConfig.get());
    }

    public static boolean getCompilerGenerateModuleInfo(CeylonConfig config) {
        return config.getBoolOption(COMPILER_GENERATE_MODULE_INFO, false);
    }

    public static boolean getCompilerPack200() {
        return getCompilerPack200(CeylonConfig.get());
    }
    
    public static boolean getCompilerPack200(CeylonConfig config) {
        return config.getBoolOption(COMPILER_PACK200, false);
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

    public static String getCompilerTarget() {
        return getCompilerTarget(CeylonConfig.get());
    }

    public static String getCompilerTarget(CeylonConfig config) {
        return config.getOption(COMPILER_TARGET);
    }
}
