package com.redhat.ceylon.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

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
     * Turns the given file into the best absolute representation available 
     * @param file A file
     * @return A canonical or absolute file
     */
    public static File absoluteFile(File file) {
        if (file != null) {
            try {
                file = file.getCanonicalFile();
            } catch (IOException e) {
                file = file.getAbsoluteFile();
            }
        }
        return file;
    }

    /**
     * Given a list of files and a "current working directory"
     * returns the list where the files that were relative are
     * now absolute after having the "CWD" applied to them
     * as their parent directory. Files in the list that were
     * already absolute are returned unmodified.
     * @param cwd The current working directory
     * @param files A list of files
     * @return A list of absolute files
     */
    public static List<File> applyCwd(File cwd, List<File> files) {
        List<File> result = new ArrayList<>(files.size());
        for (File f : files) {
            result.add(applyCwd(cwd, f));
        }
        return result;
    }

    /**
     * Given a file and a "current working directory" returns
     * an absolute file after having the "CWD" applied to it
     * first as its parent directory. If the file was already
     * absolute to begin with it is returned unmodified.
     * @param cwd The current working directory
     * @param files A file
     * @return An absolute file
     */
    public static File applyCwd(File cwd, File file) {
        if (cwd != null && !file.isAbsolute()) {
            File absCwd = absoluteFile(cwd);
            file = new File(absCwd, file.getPath());
        }
        return file;
    }

    /**
     * Given a file and a possible parent folder returns
     * the file relative to the parent. If the file wasn't
     * relative to the parent it is returned unmodified.
     * @param root A possible parent file
     * @param file A file
     * @return A file relative to the parent
     */
    public static File relativeFile(File root, File file) {
        if (root != null && file != null) {
            String absRoot = absoluteFile(root).getPath();
            String absFile = absoluteFile(file).getPath();
            if (absFile.startsWith(absRoot)) {
                String path = absFile.substring(absRoot.length());
                if (path.startsWith(File.separator)) {
                    path = path.substring(1);
                }
                file = new File(path);
            }
        }
        return file;
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
    
    /**
     * Returns the environment variable {@code PATH} as an array of {@code File}.
     * If the value was empty a array of size 0 will be returned.
     */
    public static File[] getExecPath() {
        String path = System.getenv("PATH");
        if (path != null && !path.isEmpty()) {
            String[] items = path.split(Pattern.quote(File.pathSeparator));
            File[] result = new File[items.length];
            for (int i = 0; i < items.length; i++) {
                result[i] = new File(items[i]);
            }
            return result;
        } else {
            return EMPTY_FILES;
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
    
    /**
     * Given an absolute file path and a list of "search paths"
     * returns the relative path of the file (relative to the one
     * search path that matched)
     * @param paths A list of folders
     * @param file An absolute path to a file 
     * @return The relative file path or the original file path if no match was found
     */
    public static String relativeFile(Iterable<? extends File> paths, String file){
        // find the matching path prefix
        int srcDirLength = 0;
        for (File prefixFile : paths) {
            String prefix = prefixFile.getPath();
            if (file.startsWith(prefix) && prefix.length() > srcDirLength) {
                srcDirLength = prefix.length();
            }
            String absPrefix;
            try {
                absPrefix = prefixFile.getCanonicalPath();
            } catch (IOException e) {
                absPrefix = prefixFile.getAbsolutePath();
            }
            if (file.startsWith(absPrefix) && absPrefix.length() > srcDirLength) {
                srcDirLength = absPrefix.length();
            }
        }
        
        String path = file.substring(srcDirLength);
        if (path.startsWith(File.separator)) {
            path = path.substring(1);
        }
        
        return path;
    }

    public static boolean sameFile(File a, File b) {
        if(a == null)
            return b == null;
        if(b == null)
            return false;
        try {
            String aPath = a.getCanonicalPath();
            String bPath = b.getCanonicalPath();
            return aPath.equals(bPath);
        } catch (IOException e) {
            return a.equals(b);
        }
    }

    // duplicated in /ceylon-compiler/src/com/redhat/ceylon/ant/Util.java because FileUtil is not in Ant's ClassPath
    public static boolean isChildOfOrEquals(File parent, File child){
        // doing a single comparison is likely cheaper than walking up to the root
        try {
            String parentPath = parent.getCanonicalPath();
            String childPath = child.getCanonicalPath();
            if(parentPath.equals(childPath))
                return true;
            // make sure we compare with a separator, otherwise /foo would be considered a parent of /foo-bar
            if(!parentPath.endsWith(File.separator))
                parentPath += File.separator;
            return childPath.startsWith(parentPath);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Returns true if the specified folder contains at least one regular file
     */
    public static boolean containsFile(File dir) {
        try {
            final boolean[] found = new boolean[1];
            found[0] = false;
            Files.walkFileTree(dir.toPath(), new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attributes) throws IOException {
                    if(Files.isRegularFile(path)){
                        found[0] = true;
                        return FileVisitResult.TERMINATE;
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
            return found[0];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Recursively copy every file/folder from root to dest
     */
    public static void copy(File root, File dest) throws IOException {
        for(File child : root.listFiles()){
            File childDest = new File(dest, child.getName());
            if(child.isDirectory()){
                if(!childDest.mkdirs())
                    throw new IOException("Failed to create dir "+childDest.getPath());
                copy(child, childDest);
            }else{
                Files.copy(child.toPath(), childDest.toPath());
            }
        }
    }
}
