package com.redhat.ceylon.common.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.redhat.ceylon.common.FileUtil;

/**
 * Class with helper methods for expanding a list of module names/specs
 * that possibly contain wildcards into an expanded list of actual modules
 * that were found on the file system
 * @author Tako Schotanus
 */
public abstract class ModuleWildcardsHelper {

    private static final Pattern idPattern = Pattern.compile("\\p{Lower}[\\p{Alnum}_]*");
    
    /**
     * Given a source directory and a list of ModuleSpecs
     * that possibly contain wildcards it returns a expanded list of
     * ModuleSpecs of modules that were actually found in the given
     * source directory. ModuleSpecs that didn't contain wildcards
     * are left alone (it's not checked if they exist or not).
     * @param dirs The source directory
     * @param names The list of ModuleSpecs
     * @return An expanded list of ModuleSpecs
     */
    public static List<ModuleSpec> expandSpecWildcards(File dir, List<ModuleSpec> modules) {
        List<File> dirs = new ArrayList<>();
        dirs.add(dir);
        return expandSpecWildcards(dirs, modules);
    }

    /**
     * Given a list of source directories and a list of ModuleSpecs
     * that possibly contain wildcards it returns a expanded list of
     * ModuleSpecs of modules that were actually found in the given
     * source directories. ModuleSpecs that didn't contain wildcards
     * are left alone (it's not checked if they exist or not).
     * @param dirs The list of source directories
     * @param names The list of ModuleSpecs
     * @return An expanded list of ModuleSpecs
     */
    public static List<ModuleSpec> expandSpecWildcards(List<File> dirs, List<ModuleSpec> modules) {
        List<ModuleSpec> result = new ArrayList<>(modules.size());
        for (ModuleSpec spec : modules) {
            List<String> names = new ArrayList<>();
            expandWildcard(names, dirs, spec.getName());
            for (String name : names) {
                result.add(new ModuleSpec(name, spec.getVersion()));
            }
        }
        return result;
    }

    /**
     * Given a source directory and a list of module names
     * that possibly contain wildcards it returns a expanded list of
     * module names of modules that were actually found in the given
     * source directory. Module names that didn't contain wildcards
     * are left alone (it's not checked if they exist or not).
     * @param dirs The source directory
     * @param names The list of module names
     * @return An expanded list of module names
     */
    public static List<String> expandWildcards(File dir, List<String> modules) {
        List<File> dirs = new ArrayList<>();
        dirs.add(dir);
        return expandWildcards(dirs, modules);
    }

    /**
     * Given a list of source directories and a list of module names
     * that possibly contain wildcards it returns a expanded list of
     * module names of modules that were actually found in the given
     * source directories. Module names that didn't contain wildcards
     * are left alone (it's not checked if they exist or not).
     * @param dirs The list of source directories
     * @param names The list of module names
     * @return An expanded list of module names
     */
    public static List<String> expandWildcards(List<File> dirs, List<String> names) {
        List<String> result = new ArrayList<>(names.size());
        for (String name : names) {
            expandWildcard(result, dirs, name);
        }
        return result;
    }

    public static void expandWildcard(List<String> result, List<File> dirs, String name) {
        if ("*".equals(name)) {
            List<String> modules = findModules(dirs, ".");
            result.addAll(modules);
            return;
        } else if (name.endsWith(".*")) {
            String modName = name.substring(0, name.length() - 2);
            if (isValidModuleDir(dirs, modName)) {
                String modPath = modName.replace('.', File.separatorChar);
                List<String> modules = findModules(dirs, modPath);
                result.addAll(modules);
                return;
            }
        }
        result.add(name);
    }

    public static boolean isValidModuleDir(List<File> dirs, String name) {
        if (isModuleName(name)) {
            String path = name.replace('.', File.separatorChar);
            if (existsSourceSubDir(dirs, path)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isModuleName(String name) {
        String[] parts = name.split("\\.");
        for (String part : parts) {
            if (part.isEmpty()) {
                return false;
            }
            Matcher m = idPattern.matcher(part);
            if (!m.matches()) {
                return false;
            }
        }
        return true;
    }

    private static boolean existsSourceSubDir(List<File> dirs, String file) {
        for (File dir : dirs) {
            File subDir = new File(dir, file);
            if (subDir.isDirectory() && subDir.canRead()) {
                return true;
            }
        }
        return false;
    }

    private static List<String> findModules(List<File> dirs, String modPath) {
        List<String> modules = new ArrayList<>();
        for (File dir : dirs) {
            File modDir = new File(dir, modPath);
            if (modDir.isDirectory() && modDir.canRead()) {
                findModules(modules, dir, modDir);
            }
        }
        return modules;
    }
    
    private static void findModules(List<String> modules, File root, File dir) {
        File descriptor = new File(dir, "module.ceylon");
        if (descriptor.isFile()) {
            File modDir = FileUtil.relativeFile(root, dir);
            String modName = modDir.getPath().replace(File.separatorChar, '.');
            modules.add(modName);
        } else {
            File[] files = dir.listFiles();
            for (File f : files) {
                if (f.isDirectory() && f.canRead()) {
                    findModules(modules, root, f);
                }
            }
        }
    }
    
}
