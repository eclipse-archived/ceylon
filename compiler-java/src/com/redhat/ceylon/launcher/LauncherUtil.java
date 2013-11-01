package com.redhat.ceylon.launcher;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.OSUtil;
import com.redhat.ceylon.common.Versions;

public class LauncherUtil {
    private LauncherUtil() {}
    
    private static final String CEYLON_REPO = "repo";
    private static final String CEYLON_LIBS = "lib";
    
    public static File determineHome() throws URISyntaxException {
        // Determine the Ceylon home/install folder
        File ceylonHome;
        // First try the ceylon.home system property
        String ceylonHomeStr = System.getProperty(Constants.PROP_CEYLON_HOME_DIR);
        if (ceylonHomeStr == null) {
            // Second try the CEYLON_HOME environment variable
            ceylonHomeStr = System.getenv(Constants.ENV_CEYLON_HOME_DIR);
        }
        if (ceylonHomeStr == null) {
            // Then try to deduce it from the location of the current JAR file
            // (assuming $CEYLON_HOME/lib/ceylon-bootstrap.jar)
            File jar = determineRuntimeJar();
            ceylonHome = jar.getParentFile().getParentFile();
            if (!checkHome(ceylonHome)) {
                // As a last ditch effort see if we can find "ceylon" in the system's shell
                // path and decuce the home folder from that (assuming $CEYLON_HOME/bin/ceylon)
                File script = findCeylonScript();
                if (script != null) {
                    ceylonHome = script.getParentFile().getParentFile();
                }
            }
        } else {
            ceylonHome = new File(ceylonHomeStr);
        }
        return ceylonHome;
    }

    public static File determineRepo() throws URISyntaxException {
        // Determine the Ceylon system repository folder
        File ceylonRepo;
        String ceylonSystemRepo = System.getProperty(Constants.PROP_CEYLON_SYSTEM_REPO);
        if (ceylonSystemRepo != null) {
            ceylonRepo = new File(ceylonSystemRepo);
        } else {
            ceylonRepo = new File(determineHome(), CEYLON_REPO);
        }
        return ceylonRepo;
    }
    
    public static File determineLibs() throws URISyntaxException {
        // Determine the Ceylon system library folder
        File ceylonLib;
        String ceylonSystemRepo = System.getProperty(Constants.PROP_CEYLON_SYSLIBS_DIR);
        if (ceylonSystemRepo != null) {
            ceylonLib = new File(ceylonSystemRepo);
        } else {
            ceylonLib = new File(determineHome(), CEYLON_LIBS);
        }
        return ceylonLib;
    }
    
    public static String determineSystemVersion() {
        // Determine the Ceylon system/language/runtime version
        String ceylonSystemVersion = System.getProperty(Constants.PROP_CEYLON_SYSTEM_VERSION);
        if (ceylonSystemVersion == null) {
            // Second try the constant defined in Versions
            ceylonSystemVersion = Versions.CEYLON_VERSION_NUMBER;
        }
        return ceylonSystemVersion;
    }
    
    public static File determineRuntimeJar() throws URISyntaxException {
        return new File(CeylonClassLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
    }
    
    private static File findCeylonScript() {
        String path = System.getenv("PATH");
        if (path != null) {
            String ceylonScriptName;
            if (OSUtil.isWindows()) {
                ceylonScriptName = "ceylon.bat";
            } else {
                ceylonScriptName = "ceylon";
            }
            String[] elems = path.split(File.pathSeparator);
            for (String elem : elems) {
                File script = new File(elem, ceylonScriptName);
                if (script.isFile()) {
                    try {
                        return script.getCanonicalFile();
                    } catch (IOException e) {
                        // Ignore errors and keep on trying
                    }
                }
            }
        }
        return null;
    }
    
    private static boolean checkHome(File ceylonHome) {
        return (new File(ceylonHome, CEYLON_REPO)).isDirectory() && (new File(ceylonHome, CEYLON_LIBS)).isDirectory();
    }
}
