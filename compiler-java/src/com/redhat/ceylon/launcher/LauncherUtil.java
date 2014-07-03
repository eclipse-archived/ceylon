package com.redhat.ceylon.launcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.OSUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.ToolError;

public class LauncherUtil {
    private LauncherUtil() {}
    
    private static final String CEYLON_REPO = "repo";
    private static final String CEYLON_LIBS = "lib";
    
    // Can't use OSUtil.isWindows() here because these classes are put in the
    // ceylon-bootstrap.jar that doesn't have access to ceylon-common
    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0;
    
    public static File determineHome() throws URISyntaxException {
        // Determine the Ceylon home/install folder
        File ceylonHome;
        // First try the ceylon.home system property
        String ceylonHomeStr = System.getProperty(Constants.PROP_CEYLON_HOME_DIR);
        if (ceylonHomeStr == null) {
            // Second try to deduce it from the location of the current JAR file
            // (assuming $CEYLON_HOME/lib/ceylon-bootstrap.jar)
            File jar = determineRuntimeJar();
            ceylonHome = jar.getParentFile().getParentFile();
            if (!checkHome(ceylonHome)) {
                // Third try the CEYLON_HOME environment variable
                ceylonHomeStr = System.getenv(Constants.ENV_CEYLON_HOME_DIR);
                if (ceylonHomeStr == null) {
                    // As a last ditch effort see if we can find "ceylon" in the system's shell
                    // path and decuce the home folder from that (assuming $CEYLON_HOME/bin/ceylon)
                    File script = findCeylonScript();
                    if (script != null) {
                        ceylonHome = script.getParentFile().getParentFile();
                    }
                }
            }
        } else {
            ceylonHome = new File(ceylonHomeStr);
        }
        return ceylonHome;
    }

    public static File determineRepo(File ceylonHome) throws URISyntaxException {
        // Determine the Ceylon system repository folder
        File ceylonRepo;
        String ceylonSystemRepo = System.getProperty(Constants.PROP_CEYLON_SYSTEM_REPO);
        if (ceylonSystemRepo != null) {
            ceylonRepo = new File(ceylonSystemRepo);
        } else {
            ceylonRepo = new File(ceylonHome, CEYLON_REPO);
        }
        return ceylonRepo;
    }
    
    public static File determineLibs(File ceylonHome) throws URISyntaxException {
        // Determine the Ceylon system library folder
        File ceylonLib;
        String ceylonSystemRepo = System.getProperty(Constants.PROP_CEYLON_SYSLIBS_DIR);
        if (ceylonSystemRepo != null) {
            ceylonLib = new File(ceylonSystemRepo);
        } else {
            ceylonLib = new File(ceylonHome, CEYLON_LIBS);
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
            if (IS_WINDOWS) {
                ceylonScriptName = "ceylon.bat";
            } else {
                ceylonScriptName = "ceylon";
            }
            String[] elems = path.split(File.pathSeparator);
            for (String elem : elems) {
                File script = new File(elem, ceylonScriptName);
                if (script.isFile() && script.canExecute() && isSameScriptVersion(script)) {
                    try {
                        // only if the version is compatible with this version!
                        return script.getCanonicalFile();
                    } catch (IOException e) {
                        // Ignore errors and keep on trying
                    }
                }
            }
        }
        return null;
    }
    
    private static boolean isSameScriptVersion(File script) {
        List<String> args = new ArrayList<String>(4);
        if (IS_WINDOWS) {
            args.add("cmd.exe");
            args.add("/C");
        }
        args.add(script.getAbsolutePath());
        args.add("--version");
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        try{
            Process process = processBuilder.start();
            InputStream in = process.getInputStream();
            InputStreamReader inread = new InputStreamReader(in);
            BufferedReader bufferedreader = new BufferedReader(inread);
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedreader.readLine()) != null) {
                sb.append(line);
            }
            int exit = process.waitFor();
            bufferedreader.close();
            if(exit != 0)
                return false;
            return sb.toString().startsWith("ceylon version "+Versions.CEYLON_VERSION_MAJOR+"."+Versions.CEYLON_VERSION_MINOR);
        }catch(Throwable t){
            return false;
        }
    }

    private static boolean checkHome(File ceylonHome) {
        return (new File(ceylonHome, CEYLON_REPO)).isDirectory() && (new File(ceylonHome, CEYLON_LIBS)).isDirectory();
    }
}
