package com.redhat.ceylon.launcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.redhat.ceylon.common.Constants;

public class Launcher {
    private static volatile CeylonClassLoader ceylonClassLoader;

    public static void main(String[] args) throws Throwable {
        System.exit(run(args));
    }

    public static int run(String... args) throws Throwable {
        Java7Checker.check();

        // If the --sysrep option was set on the command line we set the corresponding system property
        String ceylonSystemRepo = getArgument(args, "--sysrep", false);
        if (ceylonSystemRepo != null) {
            System.setProperty(Constants.PROP_CEYLON_SYSTEM_REPO, ceylonSystemRepo);
        }

        // If the --ceylonversion option was set on the command line we set the corresponding system property
        String ceylonSystemVersion = getArgument(args, "--ceylonversion", false);
        if (ceylonSystemVersion != null) {
            System.setProperty(Constants.PROP_CEYLON_SYSTEM_VERSION, ceylonSystemVersion);
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

        // Find the main tool class
        String verbose = null;
        Class<?> mainClass = loader.loadClass("com.redhat.ceylon.tools.CeylonTool");
        
        // Set up the arguments for the tool
        Object mainTool = mainClass.newInstance();
        Method setupMethod = mainClass.getMethod("setup", args.getClass());
        Integer result = (Integer)setupMethod.invoke(mainTool, (Object)args);
        if (result == 0 /* SC_OK */) {
            try {
                Method toolGetter = mainClass.getMethod("getTool");
                Object tool = toolGetter.invoke(mainTool);
                Method verboseGetter = tool.getClass().getMethod("getVerbose");
                verbose = (String)verboseGetter.invoke(tool);
            } catch (Exception ex) {
                // Probably doesn't have a --verbose option
            }
            
            //boolean verbose = hasArgument(args, "--verbose") && getArgument(args, "--verbose", true) == null;
            initGlobalLogger(verbose);

            if (hasVerboseFlag(verbose, "loader")) {
                Logger log = Logger.getLogger("");
                log.info("Ceylon home directory is '" + LauncherUtil.determineHome() + "'");
                for (File f : cp) {
                    log.info("path = " + f + " (" + (f.exists() ? "OK" : "Not found!") + ")");
                }
            }

            // And finally execute the tool
            Method execMethod = mainClass.getMethod("execute");
            result = (Integer)execMethod.invoke(mainTool);
        }
        
        return result.intValue();
    }

    public static CeylonClassLoader getClassLoader() throws MalformedURLException, FileNotFoundException, URISyntaxException {
        // Check if we need to create a CeylonClassLoader or if we can use the existing one
        synchronized (CeylonClassLoader.class) {
            if (ceylonClassLoader == null) {
                // Create the class loader that knows where to find all the Ceylon dependencies
                ceylonClassLoader = new CeylonClassLoader();
            }
        }

        // Set context class loader for current thread
        Thread.currentThread().setContextClassLoader(ceylonClassLoader);

        // Set some important system properties
        System.setProperty("ceylon.home", LauncherUtil.determineHome().getAbsolutePath());
        System.setProperty(Constants.PROP_CEYLON_SYSTEM_REPO, LauncherUtil.determineRepo().getAbsolutePath());
        System.setProperty(Constants.PROP_CEYLON_SYSTEM_VERSION, LauncherUtil.determineSystemVersion());

        return ceylonClassLoader;
    }

    private static boolean hasArgument(final String[] args, final String test) {
        for (String arg : args) {
            if ("--".equals(arg)) {
                break;
            }
            if (arg.equals(test) || arg.startsWith(test + "=")) {
                return true;
            }
        }
        return false;
    }

    private static String getArgument(final String[] args, final String test, boolean optionalArgument) {
        for (int i=0; i < args.length; i++) {
            String arg = args[i];
            if ("--".equals(arg)) {
                break;
            }
            if (!optionalArgument && i < (args.length - 1) && arg.equals(test)) {
                return args[i + 1];
            }
            if (arg.startsWith(test + "=")) {
                return arg.substring(test.length() + 1);
            }
        }
        return null;
    }

    private static void initGlobalLogger(String verbose) {
        try {
            //if no log Manager specified use JBoss LogManager
            String logManager = System.getProperty("java.util.logging.manager");
            if (logManager == null) {
                System.setProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager");
            }

            boolean handlersExists = false;
            for (Handler handler : Logger.getLogger("").getHandlers()) {
                handlersExists = true;

                //TODO Should we remove this hack? If handler are configured then levels should be too.
                // This is a hack, but at least it works. With a property file our log
                // formatter has to be in the boot class path. This way it doesn't.
                if (handler instanceof ConsoleHandler) {
                    handler.setFormatter(CeylonLogFormatter.INSTANCE);
                    if (verbose != null) {
                        handler.setLevel(Level.ALL);
                    }
                }
            }
            if (verbose != null) {
                //TODO do not configure root logger, make it flags aware
                Logger logger = Logger.getLogger("");
                logger.setLevel(Level.ALL);
                if (handlersExists == false) {
                    ConsoleHandler handler = new ConsoleHandler();
                    handler.setFormatter(CeylonLogFormatter.INSTANCE);
                    handler.setLevel(Level.ALL);
                    logger.addHandler(handler);
                }
            }
        } catch (Throwable ex) {
            System.err.println("Warning: log configuration failed: " + ex.getMessage());
        }
    }

    // Returns true if one of the argument passed matches one of the flags given to
    // --verbose=... on the command line or if one of the flags is "all"
    private static boolean hasVerboseFlag(String verbose, String flag) {
        if (verbose == null) {
            return false;
        }
        if (verbose.isEmpty()) {
            return true;
        }
        List<String> lst = Arrays.asList(verbose.split(","));
        if (lst.contains("all")) {
            return true;
        }
        return lst.contains(flag);
    }
}
