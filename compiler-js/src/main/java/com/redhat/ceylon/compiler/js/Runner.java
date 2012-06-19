package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

/** Command-line runner for ceylonc-js compiled code, using node.js
 *
 * @author Enrique Zamudio
 */
public class Runner {

    /** A thread dedicated to reading from a stream and storing the result to return it as a String. */
    public static class ReadStream extends Thread {
        private final InputStream in;
        private final PrintStream out;
        private final byte[] buf  = new byte[16384];
        public ReadStream(InputStream from, PrintStream to) {
            this.in = from;
            this.out = to;
        }
        public void run() {
            try {
                int count = in.read(buf);
                while (count > 0) {
                    out.write(buf, 0, count);
                    count = in.read(buf);
                }
            } catch (IOException ex) {
                ex.printStackTrace(out);
            }
        }
    }

    private static void usage() {
        System.out.println("Usage:");
        System.out.println("        ceylon-js [-rep path to modules directory] [-run function to run] moduleName/version");
        System.out.println("                   -rep defaults to ./modules");
        System.out.println("                   -run defaults to 'run' which must be an exported function of the js module");
        System.out.println("                   -help prints this message");
        System.out.println("                   -version Prints version number");
    }

    /** Finds the full path to the node.js executable. */
    public static String findNode() {
        String path = getNodeExe();
        if (path != null && !path.isEmpty() && isExe(path)) {
            return path;
        }
        String[] paths = { "/usr/bin/node", "/usr/local/bin/node", "/bin/node", "/opt/bin/node",
                "C:\\Program Files\\nodejs\\node.exe", "C:\\Program Files (x86)\\nodejs\\node.exe" };
        for (String p : paths) {
            if (isExe(p)) {
                return p;
            }
        }
        //Now let's look for the executable in all path elements
        String winpath = System.getenv("Path");
        if (winpath != null) {
            paths = winpath.split(File.pathSeparator);
            for (String p : paths) {
                String np = p.endsWith(File.separator) ? p + "node.exe" : p + File.separator + "node.exe";
                if (isExe(np)) {
                    return np;
                }
            }
        }
        System.err.println("Could not find 'node' executable. Please install node.js (from http://nodejs.org).");
        System.exit(2);
        return null;
    }

    private static boolean isExe(String p) {
        File f = new File(p);
        return f.exists() && f.canExecute();
    }

    public static String findOptionValue(String optionName, List<String> options) {
        int idx = options.indexOf(optionName);
        if (idx >=0 && idx < options.size() - 2 && !options.get(idx+1).startsWith("-")) {
            return options.get(idx+1);
        }
        return null;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length == 0) {
            usage();
            System.exit(1);
            return;
        }
        List<String> opts = Arrays.asList(args);
        if (opts.contains("-version")) {
            System.out.println("Ceylon Javascript Runner version 0.3");
        }
        if (opts.contains("-help")) {
            usage();
            return;
        }
        final String node = findNode();
        String repo = findOptionValue("-rep", opts);
        String func = findOptionValue("-run", opts);
        if (repo == null) {
            repo = "modules";
        }
        if (func == null) {
            func = "run";
        }
        String module = opts.get(opts.size()-1);
        final boolean isDefault = "default".equals(module);
        String version = "";
        if (!isDefault && !module.contains("/")) {
            System.out.println("Specified module is not default and is missing version.");
            usage();
            return;
        }
        if (!isDefault) {
            version = module.substring(module.indexOf('/')+1);
            module = module.substring(0, module.indexOf('/'));
        }
        String sepForEval = File.separator;
        // on windows for eval we need to escape the backslash
        if(sepForEval == "\\")
        	sepForEval = "\\\\";

        final String eval = String.format("setTimeout(function(){},50);require('%s%s%s%s%s').%s();",
                module.replace(".", sepForEval),
                isDefault ? "" : sepForEval + version,
                sepForEval,
                module,
                isDefault ? "" : "-" + version,
                func);
        ProcessBuilder proc = new ProcessBuilder(node, "-e", eval);
        String nodePath = getNodePath();
        String ceylonRepo = getCeylonRepo();
        if (nodePath == null) {
            if (ceylonRepo == null) {
                nodePath = repo;
            } else {
                nodePath = ceylonRepo + File.pathSeparator + repo;
            }
        } else if (ceylonRepo == null) {
            nodePath = nodePath + File.pathSeparator + repo;
        } else {
            nodePath = nodePath + File.pathSeparator + ceylonRepo + File.pathSeparator + repo;
        }
        proc.environment().put("NODE_PATH", nodePath);
        Process nodeProcess = proc.start();
        //All this shit because inheritIO doesn't work on fucking Windows
        new ReadStream(nodeProcess.getInputStream(), System.out).start();
        new ReadStream(nodeProcess.getErrorStream(), System.err).start();
        int exitCode = nodeProcess.waitFor();
        if (exitCode != 0) {
            System.err.println("Exit code: "+exitCode);
        }
        System.exit(exitCode);
    }

    private static String getNodePath() {
        return getFromEnv("NODE_PATH", "node.path");
    }

    private static String getNodeExe() {
        return getFromEnv("NODE_EXE", "node.exe");
    }

    private static String getCeylonRepo() {
        return getFromEnv("CEYLON_REPO", "ceylon.repo");
    }

    private static String getFromEnv(String env, String prop){
        String path = System.getenv(env);
        if (path != null) {
            return path;
        }
        return System.getProperty(prop);
    }

}
