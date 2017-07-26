package com.redhat.ceylon.common.config;

import java.io.File;
import java.net.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.FileUtil;

public class DefaultToolOptions {
    
    public final static String SECTION_DEFAULTS = "defaults";
    
    public final static String DEFAULTS_ENCODING = "defaults.encoding";
    public final static String DEFAULTS_OFFLINE = "defaults.offline";
    public final static String DEFAULTS_TIMEOUT = "defaults.timeout";
    // BACKWARDS-COMPAT
    public final static String DEFAULTS_MAVENOVERRIDES = "defaults.mavenoverrides";
    public final static String DEFAULTS_OVERRIDES = "defaults.overrides";
    public final static String DEFAULTS_FLAT_CLASSPATH = "defaults.flatclasspath";
    public final static String DEFAULTS_AUTO_EXPORT_MAVEN_DEPENDENCIES = "defaults.autoexportmavendependencies";
    public final static String DEFAULTS_FULLY_EXPORT_MAVEN_DEPENDENCIES = "defaults.fullyexportmavendependencies";
    public final static String DEFAULTS_LINK_WITH_CURRENT_DISTRIBUTION = "defaults.linkwithcurrentdistribution";
    
    public final static String SECTION_COMPILER = "compiler";
    
    public final static String COMPILER_SOURCE = "compiler.source";
    public final static String COMPILER_RESOURCE = "compiler.resource";
    public final static String COMPILER_RESOURCE_ROOT = "compiler.resourceroot";
    public final static String COMPILER_SCRIPT = "compiler.script";
    public final static String COMPILER_DOC = "compiler.doc";
    public final static String COMPILER_SUPPRESSWARNING = "compiler.suppresswarning";
    public final static String COMPILER_PROGRESS = "compiler.progress";
    public final static String COMPILER_MODULES = "compiler.module";
    public final static String COMPILER_INCLUDE_DEPENDENCIES = "compiler.includedependencies";
    public final static String COMPILER_INCREMENTAL = "compiler.incremental";
    public final static String COMPILER_ARGUMENTS = "compiler.arguments";
    // JVM-only, needing backward compatibility (only for pre-1.3.0 options)
    public final static String COMPILER_NOOSGI = "compiler.jvm.noosgi";
    public final static String COMPILER_OSGIPROVIDEDBUNDLES = "compiler.jvm.osgiprovidedbundles";
    public final static String COMPILER_NOPOM = "compiler.jvm.nopom";
    public final static String COMPILER_GENERATE_MODULE_INFO = "compiler.jvm.generatemoduleinfo";
    public final static String COMPILER_PACK200 = "compiler.jvm.pack200";
    public final static String COMPILER_JDKPROVIDER = "compiler.jvm.jdkprovider";
    public final static String COMPILER_APT = "compiler.jvm.apt";
    // JVM-only, not needing backward compatibility (all new options should go here)
    public final static String COMPILER_TARGET_VERSION = "compiler.jvm.target";
    public final static String COMPILER_JAVAC = "compiler.jvm.javac";
    public final static String COMPILER_EE = "compiler.jvm.ee";
    public final static String COMPILER_EE_IMPORT = "compiler.jvm.eeimport";
    public final static String COMPILER_EE_ANNOTATION = "compiler.jvm.eeannotation";
    
    public final static String SECTION_RUNTOOL = "runtool";
    
    public final static String RUNTOOL_COMPILE = "runtool.compile";
    public final static String RUNTOOL_RUN = "runtool.run";
    public final static String RUNTOOL_MODULE = "runtool.module";
    public final static String RUNTOOL_ARG = "runtool.arg";

    public final static String SECTION_TESTTOOL = "testtool";
    
    public final static String TESTTOOL_COMPILE = "testtool.compile";

    public final static String KEY_COMPILE = "compile";
    public final static String KEY_MODULE = "module";
    public final static String KEY_ARG = "arg";
    public final static String KEY_RUN = "run";
    
    public final static List<String> DEFAULT_COMPILER_MODULES = Arrays.asList(new String[] { "*" });
    
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
        return config.getBoolOption(DEFAULTS_AUTO_EXPORT_MAVEN_DEPENDENCIES, false);
    }

    public static boolean getDefaultFullyExportMavenDependencies() {
        return getDefaultFullyExportMavenDependencies(CeylonConfig.get());
    }

    public static boolean getDefaultFullyExportMavenDependencies(CeylonConfig config) {
        return config.getBoolOption(DEFAULTS_FULLY_EXPORT_MAVEN_DEPENDENCIES, false);
    }

    public static boolean getLinkWithCurrentDistribution() {
        return getLinkWithCurrentDistribution(CeylonConfig.get());
    }

    public static boolean getLinkWithCurrentDistribution(CeylonConfig config) {
        return config.getBoolOption(DEFAULTS_LINK_WITH_CURRENT_DISTRIBUTION, true);
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
    
    public static boolean getCompilerProgress() {
        return getCompilerProgress(CeylonConfig.get());
    }

    public static boolean getCompilerProgress(CeylonConfig config) {
        return config.getBoolOption(COMPILER_PROGRESS, false);
    }
    
    public static List<String> getCompilerModules(Backend backend) {
        return getCompilerModules(CeylonConfig.get(), backend);
    }
    
    public static List<String> getCompilerModules(CeylonConfig config, Backend backend) {
        String[] modules = config.getOptionValues(key(SECTION_COMPILER, KEY_MODULE, backend));
        if (modules == null) {
            modules = config.getOptionValues(key(SECTION_COMPILER, KEY_MODULE));
        }
        if (modules != null) {
            return Arrays.asList(modules);
        } else {
            return DEFAULT_COMPILER_MODULES;
        }
    }
    
    public static boolean getCompilerNoOsgi() {
        return getCompilerNoOsgi(CeylonConfig.get());
    }
    
    public static boolean getCompilerNoOsgi(CeylonConfig config) {
        return config.getBoolOption(COMPILER_NOOSGI,
                config.getBoolOption(oldkey(COMPILER_NOOSGI), false));
    }

    public static String getCompilerOsgiProvidedBundles() {
        return getCompilerOsgiProvidedBundles(CeylonConfig.get());
    }
    
    public static String getCompilerOsgiProvidedBundles(CeylonConfig config) {
        return config.getOption(COMPILER_OSGIPROVIDEDBUNDLES,
                config.getOption(oldkey(COMPILER_OSGIPROVIDEDBUNDLES)));
    }

    public static boolean getCompilerNoPom() {
        return getCompilerNoPom(CeylonConfig.get());
    }

    public static boolean getCompilerNoPom(CeylonConfig config) {
        return config.getBoolOption(COMPILER_NOPOM,
                config.getBoolOption(oldkey(COMPILER_NOPOM), false));
    }

    public static boolean getCompilerGenerateModuleInfo() {
        return getCompilerGenerateModuleInfo(CeylonConfig.get());
    }

    public static boolean getCompilerGenerateModuleInfo(CeylonConfig config) {
        return config.getBoolOption(COMPILER_GENERATE_MODULE_INFO,
                config.getBoolOption(oldkey(COMPILER_GENERATE_MODULE_INFO), false));
    }

    public static boolean getCompilerPack200() {
        return getCompilerPack200(CeylonConfig.get());
    }
    
    public static boolean getCompilerPack200(CeylonConfig config) {
        return config.getBoolOption(COMPILER_PACK200,
                config.getBoolOption(oldkey(COMPILER_PACK200), false));
    }
    
    public static String getCompilerIncludeDependencies() {
        return getCompilerIncludeDependencies(CeylonConfig.get());
    }

    public static String getCompilerIncludeDependencies(CeylonConfig config) {
        return config.getOption(COMPILER_INCLUDE_DEPENDENCIES, Constants.DEFAULT_COMPILER_COMPILATION_FLAGS);
    }
    
    public static boolean getCompilerIncremental() {
        return getCompilerIncremental(CeylonConfig.get());
    }

    public static boolean getCompilerIncremental(CeylonConfig config) {
        return config.getBoolOption(COMPILER_INCREMENTAL, false);
    }
    
    public static String getCompilerJdkProvider() {
        return getCompilerJdkProvider(CeylonConfig.get());
    }
    
    public static String getCompilerJdkProvider(CeylonConfig config) {
        return config.getOption(COMPILER_JDKPROVIDER,
                config.getOption(oldkey(COMPILER_JDKPROVIDER)));
    }

    public static String[] getCompilerAptModules() {
        return getCompilerAptModules(CeylonConfig.get());
    }

    public static String[] getCompilerAptModules(CeylonConfig config) {
        String[] apts = config.getOptionValues(COMPILER_APT);
        if (apts == null) {
            apts = config.getOptionValues(oldkey(COMPILER_APT));
        }
        return apts;
    }

    public static List<String> getCompilerJavac() {
        return getCompilerJavac(CeylonConfig.get());
    }
    
    public static List<String> getCompilerJavac(CeylonConfig config) {
        String[] javacs = config.getOptionValues(COMPILER_JAVAC);
        if (javacs != null) {
            return Arrays.asList(javacs);
        } else {
            return Collections.emptyList();
        }
    }

    public static List<String> getCompilerArguments() {
        return getCompilerArguments(CeylonConfig.get());
    }

    public static List<String> getCompilerArguments(CeylonConfig config) {
        String[] args = config.getOptionValues(COMPILER_ARGUMENTS);
        if (args != null) {
            return Arrays.asList(args);
        } else {
            return Collections.emptyList();
        }
    }

    private static Long getDefaultTarget() {
        String dottedVersion = System.getProperty("java.version");
        String[] parts = dottedVersion.split("\\.|_|-");
        long major = Long.parseLong(parts[0]);
        if(major == 1)
            return Long.parseLong(parts[1]);
        return major;
    }
    
    public static long getCompilerTargetVersion() {
        return getCompilerTargetVersion(CeylonConfig.get());
    }

    public static long getCompilerTargetVersion(CeylonConfig config) {
        return config.getNumberOption(COMPILER_TARGET_VERSION, getDefaultTarget());
    }
    
    public static boolean getCompilerEe() {
        return getCompilerEe(CeylonConfig.get());
    }

    public static boolean getCompilerEe(CeylonConfig config) {
        return config.getBoolOption(COMPILER_EE, false);
    }
    
    public static List<String> getCompilerEeImport() {
        return getCompilerEeImport(CeylonConfig.get());
    }

    public static List<String> getCompilerEeImport(CeylonConfig config) {
        String[] values = config.getOptionValues(COMPILER_EE_IMPORT);
        return values != null ? Arrays.asList(values) : null;
    }
    
    public static List<String> getCompilerEeAnnotation() {
        return getCompilerEeAnnotation(CeylonConfig.get());
    }

    public static List<String> getCompilerEeAnnotation(CeylonConfig config) {
        String[] values = config.getOptionValues(COMPILER_EE_ANNOTATION);
        return values != null ? Arrays.asList(values) : null;
    }
    
    public static String getRunToolCompileFlags(Backend backend) {
        return getRunToolCompileFlags(CeylonConfig.get(), backend);
    }

    public static String getRunToolCompileFlags(CeylonConfig config, Backend backend) {
        return config.getOption(key(SECTION_RUNTOOL, KEY_COMPILE, backend),
                config.getOption(key(SECTION_RUNTOOL, KEY_COMPILE),
                        Constants.DEFAULT_RUNTOOL_COMPILATION_FLAGS));
    }
    
    public static String getRunToolModule(Backend backend) {
        return getRunToolModule(CeylonConfig.get(), backend);
    }

    public static String getRunToolModule(CeylonConfig config, Backend backend) {
        return config.getOption(key(SECTION_RUNTOOL, KEY_MODULE, backend),
                config.getOption(key(SECTION_RUNTOOL, KEY_MODULE)));
    }
    
    public static String getRunToolRun(Backend backend) {
        return getRunToolRun(CeylonConfig.get(), backend);
    }

    public static String getRunToolRun(CeylonConfig config, Backend backend) {
        return config.getOption(key(SECTION_RUNTOOL, KEY_RUN, backend),
                config.getOption(key(SECTION_RUNTOOL, KEY_RUN)));
    }
    
    public static List<String> getRunToolArgs(Backend backend) {
        return getRunToolArgs(CeylonConfig.get(), backend);
    }
    
    public static List<String> getRunToolArgs(CeylonConfig config, Backend backend) {
        String[] args = config.getOptionValues(key(SECTION_RUNTOOL, KEY_ARG, backend));
        if (args == null) {
            args = config.getOptionValues(key(SECTION_RUNTOOL, KEY_ARG));
        }
        if (args != null) {
            return Arrays.asList(args);
        } else {
            return Collections.emptyList();
        }
    }
    
    public static String getTestToolCompileFlags(Backend backend) {
        return getTestToolCompileFlags(CeylonConfig.get(), backend);
    }

    public static String getTestToolCompileFlags(CeylonConfig config, Backend backend) {
        return config.getOption(key(SECTION_TESTTOOL, KEY_COMPILE, backend),
                config.getOption(key(SECTION_TESTTOOL, KEY_COMPILE),
                        Constants.DEFAULT_TESTTOOL_COMPILATION_FLAGS));
    }

    private static String key(String section, String key) {
        return key(section, key, null);
    }
    
    private static String key(String section, String key, Backend backend) {
        if (backend != null && backend != Backend.Header)  {
            return section + "." + backend.nativeAnnotation.toLowerCase() + "." + key;
        } else {
            return section + "." + key;
        }
    }
    
    // This method is used to turn the newer jvm-specific keys into their
    // old version for backward compatibility. Eg. "compiler.jvm.javac"
    // becomes "compiler.javac". Use this only for old JVM compiler options
    private static String oldkey(String key) {
        return key.replace(".jvm.", ".");
    }
}
