package com.redhat.ceylon.launcher;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * This is the earliest bootstrap class for the Ceylon tool chain.
 * It does nothing more than trying to locate the system repository
 * and load an appropriate ceylon.bootstrap module.
 * Appropriate in this case means it will try to find the version this
 * class was compiled with (see <code>Versions.CEYLON_VERSION_NUMBER</code>)
 * or the version specified by the <code>CEYLON_VERSION</code> environment
 * variable.
 * After it locates the module it will pass the execution on to the
 * <code>Launcher.main()</code> it contains.
 * 
 * IMPORTANT This class should contain as little logic as possible and
 * delegate as soon as it can to the <code>Launcher</code> in the
 * ceylon.bootstrap module. This way we can maintain backward and forward
 * compatibility as much as possible.
 * 
 * @author Tako Schotanus
 */
public class Bootstrap {

    public static void main(String[] args) throws Throwable {
        run(args);
    }

    public static void run(String... args) throws Throwable {
        CeylonClassLoader cl = null;
        try {
            Method mainMethod = null;
            try {
                String ceylonVersion = LauncherUtil.determineSystemVersion();
                File module = CeylonClassLoader.getRepoJar("ceylon.bootstrap", ceylonVersion);
                cl = CeylonClassLoader.newInstance(Arrays.asList(module));
                Class<?> launcherClass = cl.loadClass("com.redhat.ceylon.launcher.Launcher");
                mainMethod = launcherClass.getMethod("main", String[].class);
            } catch (Exception e) {
                System.err.println("Fatal: Ceylon command could not be executed");
                throw e;
            }
            try {
                mainMethod.invoke(null, (Object)args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
            return;
        } finally {
            if (cl != null) {
                cl.clearCache();
                try {
                    cl.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }
}
