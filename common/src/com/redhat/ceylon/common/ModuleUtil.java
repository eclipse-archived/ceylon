package com.redhat.ceylon.common;

import java.io.File;

public abstract class ModuleUtil {

    private ModuleUtil() {
    }

    public static String moduleName(String moduleNameOptVersion) {
        int p = moduleNameOptVersion.indexOf('/');
        if (p == -1) {
            return moduleNameOptVersion;
        } else {
            return moduleNameOptVersion.substring(0, p);
        }
    }
    
    public static String moduleVersion(String moduleNameOptVersion) {
        int p = moduleNameOptVersion.indexOf('/');
        if (p == -1 || (p == (moduleNameOptVersion.length() - 1))) {
            return null;
        } else {
            return moduleNameOptVersion.substring(p + 1);
        }
    }

    public static boolean isDefaultModule(String moduleName) {
        return "default".equals(moduleName);
    }

    public static String makeModuleName(String moduleName, String version) {
        if (isDefaultModule(moduleName) || version == null) {
            return moduleName;
        } else {
            return moduleName + "/" + version;
        }
    }

    public static File moduleToPath(String moduleName) {
        return new File(moduleName.replace('.', File.separatorChar));
    }

}
