package com.redhat.ceylon.common.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.ModuleDescriptorReader;

/**
 * Class with helper methods for expanding a list of module names/specs
 * that possibly contain wildcards into an expanded list of actual modules
 * that were found on the file system
 * @author Tako Schotanus
 */
public abstract class ModuleWildcardsHelper {

    private static final Pattern idPattern = Pattern.compile("\\p{IsLowercase}[\\p{IsAlphabetic}\\p{IsDigit}_]*");
    
    /**
     * Given a source directory and a list of ModuleSpecs
     * that possibly contain wildcards it returns a expanded list of
     * ModuleSpecs of modules that were actually found in the given
     * source directory. ModuleSpecs that didn't contain wildcards
     * are left alone (it's not checked if they exist or not).
     * If a Backend is passed expanded modules will be checked if
     * they support it (they either don't have a native annotation
     * or it is for the correct backend).
     * @param dirs The source directory
     * @param names The list of ModuleSpecs
     * @param forBackend The Backend for which we work or null
     * @return An expanded list of ModuleSpecs
     */
    public static List<ModuleSpec> expandSpecWildcards(File dir, List<ModuleSpec> modules, Backend forBackend) {
        List<File> dirs = new ArrayList<File>();
        dirs.add(dir);
        return expandSpecWildcards(dirs, modules, forBackend);
    }

    /**
     * Given a list of source directories and a list of ModuleSpecs
     * that possibly contain wildcards it returns a expanded list of
     * ModuleSpecs of modules that were actually found in the given
     * source directories. ModuleSpecs that didn't contain wildcards
     * are left alone (it's not checked if they exist or not).
     * If a Backend is passed expanded modules will be checked if
     * they support it (they either don't have a native annotation
     * or it is for the correct backend).
     * @param dirs The list of source directories
     * @param names The list of ModuleSpecs
     * @param forBackend The Backend for which we work or null
     * @return An expanded list of ModuleSpecs
     */
    public static List<ModuleSpec> expandSpecWildcards(List<File> dirs, List<ModuleSpec> modules, Backend forBackend) {
        List<ModuleSpec> result = new ArrayList<ModuleSpec>(modules.size());
        for (ModuleSpec spec : modules) {
            List<String> names = new ArrayList<String>();
            expandWildcard(names, dirs, spec.getName(), forBackend);
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
     * If a Backend is passed expanded modules will be checked if
     * they support it (they either don't have a native annotation
     * or it is for the correct backend).
     * @param dirs The source directory
     * @param names The list of module names
     * @param forBackend The Backend for which we work or null
     * @return An expanded list of module names
     */
    public static List<String> expandWildcards(File dir, List<String> modules, Backend forBackend) {
        List<File> dirs = new ArrayList<File>();
        dirs.add(dir);
        return expandWildcards(dirs, modules, forBackend);
    }

    /**
     * Given a list of source directories and a list of module names
     * that possibly contain wildcards it returns a expanded list of
     * module names of modules that were actually found in the given
     * source directories. Module names that didn't contain wildcards
     * are left alone (it's not checked if they exist or not).
     * If a Backend is passed expanded modules will be checked if
     * they support it (they either don't have a native annotation
     * or it is for the correct backend).
     * @param dirs The list of source directories
     * @param names The list of module names
     * @param forBackend The Backend for which we work or null
     * @return An expanded list of module names
     */
    public static List<String> expandWildcards(Iterable<File> dirs, List<String> names, Backend forBackend) {
        List<String> result = new ArrayList<String>(names.size());
        for (String name : names) {
            expandWildcard(result, dirs, name, forBackend);
        }
        return result;
    }

    public static void expandWildcard(List<String> result, Iterable<File> dirs, String name, Backend forBackend) {
        if ("*".equals(name)) {
            List<String> modules = findModules(dirs, ".", forBackend);
            result.addAll(modules);
            return;
        } else if (name.endsWith(".*")) {
            String modName = name.substring(0, name.length() - 2);
            if (isValidModuleDir(dirs, modName)) {
                String modPath = modName.replace('.', File.separatorChar);
                List<String> modules = findModules(dirs, modPath, forBackend);
                result.addAll(modules);
                return;
            }
        }
        result.add(name);
    }

    public static boolean isValidModuleDir(Iterable<File> dirs, String name) {
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

    private static boolean existsSourceSubDir(Iterable<File> dirs, String file) {
        for (File dir : dirs) {
            File subDir = new File(dir, file);
            if (subDir.isDirectory() && subDir.canRead()) {
                return true;
            }
        }
        return false;
    }

    private static List<String> findModules(Iterable<File> dirs, String modPath, Backend forBackend) {
        List<String> modules = new ArrayList<String>();
        for (File dir : dirs) {
            File modDir = new File(dir, modPath);
            if (modDir.isDirectory() && modDir.canRead()) {
                findModules(modules, dir, modDir, forBackend);
            }
        }
        return modules;
    }
    
    private static void findModules(List<String> modules, File root, File dir, Backend forBackend) {
        File descriptor = new File(dir, Constants.MODULE_DESCRIPTOR);
        if (descriptor.isFile()) {
            File modDir = FileUtil.relativeFile(root, dir);
            String modName = modDir.getPath().replace(File.separatorChar, '.');
            if (includeModule(modName, root, forBackend)) {
                modules.add(modName);
            }
        } else {
            File[] files = dir.listFiles();
            for (File f : files) {
                if (f.isDirectory() && f.canRead() && isModuleName(f.getName())) {
                    findModules(modules, root, f, forBackend);
                }
            }
        }
    }
    
    private static boolean includeModule(String name, File sourceRoot, Backend forBackend) {
        if (forBackend != null) {
            try {
                ModuleDescriptorReader mdr = new ModuleDescriptorReader(name, sourceRoot);
                String backend = mdr.getModuleBackend();
                return (backend == null) || backend.equals(forBackend.nativeAnnotation);
            } catch(ModuleDescriptorReader.NoSuchModuleException x) {
                x.printStackTrace();
            }
        }
        return true;
    }
}
