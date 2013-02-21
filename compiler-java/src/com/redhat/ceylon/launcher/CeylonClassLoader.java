package com.redhat.ceylon.launcher;


import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;

import com.redhat.ceylon.common.Versions;

public class CeylonClassLoader extends URLClassLoader {
    
    public CeylonClassLoader() throws URISyntaxException, MalformedURLException, FileNotFoundException {
        super(getClassPathUrls());
    }

    public CeylonClassLoader(ClassLoader parentLoader) throws URISyntaxException, MalformedURLException, FileNotFoundException {
        super(getClassPathUrls(), parentLoader);
    }

    private static URL[] getClassPathUrls() throws URISyntaxException, MalformedURLException, FileNotFoundException {
        List<File> cp = getClassPath();
        URL[] urls = new URL[cp.size()];
        int i = 0;
        for (File f : cp) {
            urls[i++] = f.toURI().toURL();
        }
        return urls;
    }
    
    public static List<File> getClassPath() throws URISyntaxException, FileNotFoundException {
        // Determine the necessary folders
        File ceylonHome = determineHome();
        File ceylonRepo = determineRepo();
        File ceylonLib = determineLibs();
        
        // Perform some sanity checks
        if (!ceylonHome.isDirectory()) {
            throw new FileNotFoundException("Could not determine the Ceylon home directory (" + ceylonHome + ")");
        }
        if (!ceylonRepo.isDirectory()) {
            throw new FileNotFoundException("The Ceylon system repository could not be found (" + ceylonRepo + ")");
        }
        if (!ceylonLib.isDirectory()) {
            throw new FileNotFoundException("The Ceylon system libraries could not be found (" + ceylonLib + ")");
        }
        
        List<File> archives = new LinkedList<File>();
        
        // List all the JARs we find in the LIB directory
        findLibraries(archives, ceylonLib);
        
        // List all the necessary Ceylon JARs and CARs
        String version = determineSystemVersion();
        archives.add(getRepoJar(ceylonRepo, "com.redhat.ceylon.compiler.java", version));
        archives.add(getRepoCar(ceylonRepo, "ceylon.language", version));
        archives.add(getRepoJar(ceylonRepo, "ceylon.runtime", version));
        archives.add(getRepoJar(ceylonRepo, "com.redhat.ceylon.compiler.js", version));
        archives.add(getRepoJar(ceylonRepo, "com.redhat.ceylon.typechecker", version));
        archives.add(getRepoJar(ceylonRepo, "com.redhat.ceylon.common", version));
        archives.add(getRepoJar(ceylonRepo, "com.redhat.ceylon.module-resolver", version));
        archives.add(getRepoJar(ceylonRepo, "com.redhat.ceylon.maven-support", "main"));
        archives.add(getRepoJar(ceylonRepo, "org.jboss.jandex", "main"));
        archives.add(getRepoJar(ceylonRepo, "org.jboss.modules", "main"));
        
        return archives;
    }
    
    public static File determineHome() throws URISyntaxException {
        // Determine the Ceylon home/install folder
        File ceylonHome;
        // First try the ceylon.home system property
        String ceylonHomeStr = System.getProperty("ceylon.home");
        if (ceylonHomeStr == null) {
            // Second try the CEYLON_HOME environment variable
            ceylonHomeStr = System.getenv("CEYLON_HOME");
        }
        if (ceylonHomeStr == null) {
            // Finally try to deduce it from the location of the current JAR file
            File jar = new File(CeylonClassLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            ceylonHome = jar.getParentFile().getParentFile();
        } else {
            ceylonHome = new File(ceylonHomeStr);
        }
        return ceylonHome;
    }
    
    public static File determineRepo() throws URISyntaxException {
        // Determine the Ceylon system repository folder
        File ceylonRepo;
        String ceylonSystemRepo = System.getProperty("ceylon.system.repo");
        if (ceylonSystemRepo != null) {
            ceylonRepo = new File(ceylonSystemRepo);
        } else {
            ceylonRepo = new File(determineHome(), "repo");
        }
        return ceylonRepo;
    }
    
    public static File determineLibs() throws URISyntaxException {
        // Determine the Ceylon system library folder
        File ceylonLib;
        String ceylonSystemRepo = System.getProperty("ceylon.system.libs");
        if (ceylonSystemRepo != null) {
            ceylonLib = new File(ceylonSystemRepo);
        } else {
            ceylonLib = new File(determineHome(), "lib");
        }
        return ceylonLib;
    }
    
    public static String determineSystemVersion() {
        // Determine the Ceylon system/language/runtime version
        String ceylonSystemVersion = System.getProperty("ceylon.system.version");
        if (ceylonSystemVersion == null) {
            // Second try the constant defined in Versions
            ceylonSystemVersion = Versions.CEYLON_VERSION_NUMBER;
        }
        return ceylonSystemVersion;
    }
    
    private static File getRepoJar(File repo, String moduleName, String version) {
        return getRepoUrl(repo, moduleName, version, "jar");
    }
    
    private static File getRepoCar(File repo, String moduleName, String version) {
        return getRepoUrl(repo, moduleName, version, "car");
    }
    
    private static File getRepoUrl(File repo, String moduleName, String version, String extension) {
        return new File(repo, moduleName.replace('.', '/') + "/" + version + "/" + moduleName + "-" + version + "." + extension);
    }
    
    private static void findLibraries(List<File> libs, File folder) {
        File[] items = folder.listFiles();
        for (File f : items) {
            if (f.isDirectory()) {
                findLibraries(libs, f);
            } else if (f.getName().toLowerCase().endsWith(".jar")) {
                libs.add(f);
            }
        }
    }
}
