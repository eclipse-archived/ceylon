package com.redhat.ceylon.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileUtil {
    
    /**
     * Create a temporary directory
     * @param prefix The prefix to use for the directory name
     * @return a File pointing to the new directory
     * @throws RuntimeException if the directory could not be created
     */
    public static File makeTempDir(String prefix){
        try {
            File dir = File.createTempFile(prefix, "");
            if(!dir.delete() || !dir.mkdirs()) {
                throw new RuntimeException("Failed to create tmp dir: "+dir);
            }
            return dir;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Delete a file or directory
     * @param f The file or directory to be deleted
     * @throws RuntimeException if the file or directory could not be deleted
     */
    public static void delete(File f){
        if (f.exists()) {
            if (f.isDirectory()) {
                for (File c : f.listFiles()) {
                    delete(c);
                }
            }
            if (!f.delete()) {
                throw new RuntimeException("Failed to delete file: " + f.getPath());
            }
        }
    }
    
    /**
     * Turns the given path, if absolute, into a path relative to the
     * VM's current working directory and leaves it alone otherwise 
     * @param f The File to make relative
     * @return A relative File
     */
    public static File relativeFile(File f) {
        if (f.isAbsolute()) {
            File cwd = new File(".");
            String path = f.getAbsolutePath();
            if (path.startsWith(cwd.getAbsolutePath())) {
                path = "./" + path.substring(cwd.getAbsolutePath().length());
                f = new File(path);
            } else if (path.startsWith(System.getProperty("user.home"))) {
                path = "~/" + path.substring(System.getProperty("user.home").length() + 1);
                f = new File(path);
            }
        }
        return f;
    }
    
    /**
     * The OS-specific directory where global application data can be stored.
     * As given by the {@code ceylon.config.dir} system property or the default
     * OS-dependent directory if the property doesn't exist. (/etc/ceylon on
     * Unix-like systems and %ALLUSERSPROFILE%/ceylon on WIndows)
     */
    public static File getSystemConfigDir() throws IOException {
        File configDir = null;
        String ceylonConfigDir = System.getProperty(Constants.PROP_CEYLON_CONFIG_DIR);
        if (ceylonConfigDir != null) {
            configDir = new File(ceylonConfigDir);
        } else {
            if (OSUtil.isWindows()) {
                String appDir = System.getenv("ALLUSERSPROFILE");
                if (appDir != null) {
                    configDir = new File(appDir, "ceylon");
                }
            } else if (OSUtil.isMac()) {
                configDir = new File("/etc/ceylon");
            } else {
                // Assume a "regular" unix OS
                configDir = new File("/etc/ceylon");
            }
        }
        return configDir;
    }

    /**
     * The installation directory. As given by the {@code ceylon.home}
     * system property
     */
    public static File getInstallDir() {
        String ceylonHome = System.getProperty(Constants.PROP_CEYLON_HOME_DIR);
        if (ceylonHome != null) {
            return new File(ceylonHome);
        } else {
            return null;
        }
    }

    /**
     * The default user directory, that is {@code ~/.ceylon}.
     */
    public static File getDefaultUserDir() {
        String userHome = System.getProperty("user.home");
        return new File(userHome, ".ceylon");
    }

    /**
     * The effective user directory, checking the {@code ceylon.user.dir}
     * system property then defaulting to {@link getDefaultUserDir}.
     */
    public static File getUserDir() {
        String ceylonUserDir = System.getProperty(Constants.PROP_CEYLON_USER_DIR);
        if (ceylonUserDir != null) {
            return new File(ceylonUserDir);
        } else {
            return getDefaultUserDir();
        }
    }
    
    private static final String[] EMPTY_STRINGS = new String[0];
    private static final File[] EMPTY_FILES = new File[0];
    
    public static List<File> pathsToFileList(List<String> paths) {
        if (paths != null) {
            List<File> result = new ArrayList<File>(paths.size());
            for (String s : paths) {
                result.add(new File(s));
            }
            return result;
        } else {
            return Collections.emptyList();
        }
        
    }
    
    public static File[] pathsToFileArray(String[] paths) {
        if (paths != null) {
            File[] result = new File[paths.length];
            int idx = 0;
            for (String s : paths) {
                result[idx++] = new File(s);
            }
            return result;
        } else {
            return EMPTY_FILES;
        }
        
    }
    
    public static List<String> filesToPathList(List<File> files) {
        if (files != null) {
            List<String> result = new ArrayList<String>(files.size());
            for (File f : files) {
                result.add(f.getPath());
            }
            return result;
        } else {
            return Collections.emptyList();
        }
        
    }
    
    public static String[] filesToPathArray(File[] files) {
        if (files != null) {
            String[] result = new String[files.length];
            int idx = 0;
            for (File f : files) {
                result[idx++] = f.getPath();
            }
            return result;
        } else {
            return EMPTY_STRINGS;
        }
        
    }
}
