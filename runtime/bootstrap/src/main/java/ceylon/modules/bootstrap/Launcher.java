package ceylon.modules.bootstrap;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;

public class Launcher {

    public static void main(String[] args) throws Throwable {
        CeylonClassLoader loader = new CeylonClassLoader();

        // We actually need to construct and set a new class path for the compiler
        // which doesn't use the actual class path used by the JVM but it constructs
        // it's own list looking at the arguments passed on the command line or
        // at the system property "env.class.path" which we will be using here.
        List<File> cp = CeylonClassLoader.getClassPath();
        StringBuilder classPath = new StringBuilder();
        for (File f : cp) {
            if (classPath.length() > 0) {
                classPath.append(File.pathSeparatorChar);
            }
            classPath.append(f.getAbsolutePath());
        }
        
        // Set some important system properties
        System.setProperty("env.class.path", classPath.toString());
        System.setProperty("ceylon.home", CeylonClassLoader.determineHome().getAbsolutePath());
        System.setProperty("ceylon.system.repo", CeylonClassLoader.determineRepo().getAbsolutePath());
        
        if (hasArgument(args, "--verbose")) {
            System.err.println("INFO: Ceylon home directory is '" + CeylonClassLoader.determineHome() + "'");
            for (File f : cp) {
                System.err.println("INFO: path = " + f);
            }
        }
        
        // Set context class loader for current thread
        Thread.currentThread().setContextClassLoader(loader);
        
        Class<?> testClass = loader.loadClass("com.redhat.ceylon.compiler.java.metadata.Package");
        System.err.println(testClass);

        // Find the proper class and method to execute
        Class<?> mainClass = loader.loadClass("com.redhat.ceylon.tools.CeylonTool");
        Method mainMethod = mainClass.getMethod("main", args.getClass());

        // Invoke the actual ceylon tool
        mainMethod.invoke(null, (Object)args);
    }
    
    private static boolean hasArgument(String[] args, String test) {
        for (String arg : args) {
            if (arg.equals(test)) {
                return true;
            }
        }
        return false;
    }
}
