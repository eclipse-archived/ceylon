package com.redhat.ceylon.common;

public abstract class Constants {

    private Constants() {
    }

    public static final String CEYLON_SUFFIX = ".ceylon";
    public static final String JAVA_SUFFIX = ".java";
    public static final String JS_SUFFIX = ".js";
    
    public static final String CEYLON_CONFIG_DIR = ".ceylon";
    public static final String CEYLON_CONFIG_FILE = "config";
    
    public static final String CEYLON_BIN_DIR = "bin";

    public static final String MODULE_DESCRIPTOR = "module.ceylon";
    
    public static final String ENV_CEYLON_HOME_DIR = "CEYLON_HOME";
    public static final String ENV_CEYLON_VERSION = "CEYLON_VERSION";

    public static final String PROP_CEYLON_SYSTEM_VERSION = "ceylon.system.version";

    public static final String PROP_CEYLON_HOME_DIR = "ceylon.home";
    public static final String PROP_CEYLON_CONFIG_DIR = "ceylon.config.dir";
    public static final String PROP_CEYLON_CONFIG_FILE = "ceylon.config";
    public static final String PROP_CEYLON_USER_DIR = "ceylon.user.dir";
    public static final String PROP_CEYLON_SYSLIBS_DIR = "ceylon.system.libs";
    
    public static final String PROP_CEYLON_SYSTEM_REPO = "ceylon.system.repo";
    public static final String PROP_CEYLON_USER_REPO = "ceylon.user.repo";
    public static final String PROP_CEYLON_CACHE_REPO = "ceylon.cache.repo";
    
    public static final String DEFAULT_SOURCE_DIR = "source";
    public static final String DEFAULT_RESOURCE_DIR = "resource";
    public static final String DEFAULT_SCRIPT_DIR = "script";
    public static final String DEFAULT_DOC_DIR = "doc";
    public static final String DEFAULT_MODULE_DIR = "modules";
    
    public static final String DEFAULT_RESOURCE_ROOT = "ROOT";
    
    public static final String DEFAULT_RUNTOOL_COMPILATION_FLAGS = "never";
    public static final String DEFAULT_TESTTOOL_COMPILATION_FLAGS = "never";
    
    public static final String REPO_URL_CEYLON = "http://modules.ceylon-lang.org/repo/1";

    // The default timeout for establishing connections is set to 20 seconds
    // The multiplier is applied on top of that for read timeouts
    public static final int DEFAULT_TIMEOUT = 20000;
    public static final int READ_TIMEOUT_MULTIPLIER = 10;
}
