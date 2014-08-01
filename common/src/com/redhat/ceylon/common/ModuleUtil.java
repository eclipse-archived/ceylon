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

    public static String pathToModule(File modulePath) {
        return modulePath.getPath().replace('/', '.').replace('\\', '.');
    }
    
    public static boolean isModuleFolder(Iterable<File> paths, File modPath) {
        File modDesc = new File(modPath, Constants.MODULE_DESCRIPTOR);
        File path = FileUtil.searchPaths(paths, modDesc.getPath());
        return path != null && (new File(path, modDesc.getPath())).isFile();
    }

    public static File moduleFolder(Iterable<File> paths, File relFile) {
        while (relFile != null && !isModuleFolder(paths, relFile)) {
            relFile = relFile.getParentFile();
        }
        return relFile;
    }
    
    public static String moduleName(Iterable<File> paths, File relFile) {
        File modPath = moduleFolder(paths, relFile);
        if (modPath != null) {
            return ModuleUtil.pathToModule(modPath);
        } else {
            return "default";
        }
    }

}
