package com.redhat.ceylon.launcher;


import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;

public class Launcher {

    public static void main(String[] args) throws Throwable {
        System.exit(run(args));
    }

    public static int run(String... args) throws Throwable {
        Java7Checker.check();
        
        // If the --sysrep option was set on the command line we set the corresponding system property
        String ceylonSystemRepo = getArgument(args, "--sysrep");
        if (ceylonSystemRepo != null) {
            System.setProperty("ceylon.system.repo", ceylonSystemRepo);
        }
        
        // If the --system option was set on the command line we set the corresponding system property
        String ceylonSystemVersion = getArgument(args, "--system");
        if (ceylonSystemVersion != null) {
            System.setProperty("ceylon.system.version", ceylonSystemVersion);
        }
        
        CeylonClassLoader loader = getClassLoader();
        
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
        System.setProperty("env.class.path", classPath.toString());
        
        // Total HACK to make an error with JBoss Modules go away
        Properties sp = System.getProperties();
        sp.remove("javax.xml.parsers.DocumentBuilderFactory");
        sp.remove("javax.xml.parsers.SAXParserFactory");
        sp.remove("javax.xml.stream.XMLInputFactory");
        sp.remove("javax.xml.stream.XMLEventFactory");
        sp.remove("javax.xml.transform.TransformerFactory");
        sp.remove("javax.xml.stream.XMLOutputFactory");
        sp.remove("javax.xml.datatype.DatatypeFactory");
        sp.remove("org.xml.sax.driver");
        
        if (hasArgument(args, "--verbose")) {
            System.err.println("INFO: Ceylon home directory is '" + CeylonClassLoader.determineHome() + "'");
            for (File f : cp) {
                System.err.println("INFO: path = " + f + " (" + (f.exists() ? "OK" : "Not found!") + ")");
            }
        }
        
        // Find the proper class and method to execute
        Class<?> mainClass = loader.loadClass("com.redhat.ceylon.tools.CeylonTool");
        Method mainMethod = mainClass.getMethod("start", args.getClass());

        // Invoke the actual ceylon tool
        Object result = mainMethod.invoke(null, (Object)args);
        
        return ((Integer)result).intValue();
    }
    
    public static CeylonClassLoader getClassLoader() throws MalformedURLException, FileNotFoundException, URISyntaxException {
        // Check if we need to create a CeylonClassLoader or if we can use the existing one
        CeylonClassLoader loader = null;
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();;
        if (currentClassLoader instanceof CeylonClassLoader) {
            // Use existing class loader
            loader = (CeylonClassLoader)currentClassLoader;
        } else {
            // Create the class loader that knows where to find all the Ceylon dependencies
            loader = new CeylonClassLoader();
            // Set context class loader for current thread
            Thread.currentThread().setContextClassLoader(loader);
        }
        
        // Set some important system properties
        System.setProperty("ceylon.home", CeylonClassLoader.determineHome().getAbsolutePath());
        System.setProperty("ceylon.system.repo", CeylonClassLoader.determineRepo().getAbsolutePath());

        return loader;
    }
    
    private static boolean hasArgument(final String[] args, final String test) {
        for (String arg : args) {
            if (arg.equals(test) || arg.startsWith(test + "=")) {
                return true;
            }
        }
        return false;
    }
    
    private static String getArgument(final String[] args, final String test) {
        for (int i=0; i < args.length; i++) {
            if (i < (args.length - 1) && args[i].equals(test)) {
                return args[i + 1];
            }
            if (args[i].startsWith(test + "=")) {
                return args[i].substring(test.length() + 1);
            }
        }
        return null;
    }
}
