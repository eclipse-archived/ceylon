package com.redhat.ceylon.common;

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

}
