package com.redhat.ceylon.compiler.js;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.compiler.Options;

/** Command-line runner for ceylonc-js compiled code, using node.js
 *
 * @author Enrique Zamudio
 */
@Deprecated
public class Runner {

    /** A thread dedicated to reading from a stream and storing the result to return it as a String. */
    public static class ReadErrorStream extends Thread {
        protected final BufferedReader in;
        protected final PrintStream out;
        protected final byte[] buf  = new byte[16384];
        protected boolean printing = true;
        protected boolean debug = false;
        public ReadErrorStream(InputStream from, PrintStream to, boolean debug) {
            this.in = new BufferedReader(new InputStreamReader(from));
            this.out = to;
            this.debug = debug;
        }
        public void run() {
            try {
                String line = in.readLine();
                while (line != null) {
                    if (line.trim().startsWith("throw ")) {
                        printing = false || debug;
                    } else if (line.startsWith("Error: Cannot find module ")) {
                        out.println(line);
                        printing = false || debug;
                    } else if (!printing) {
                        printing = !(line.isEmpty() || line.startsWith("    at ")) || debug;
                    }
                    if (printing) {
                        out.println(line);
                    }
                    line = in.readLine();
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
    public static String findNode() throws FileNotFoundException {
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
        throw new FileNotFoundException("Could not find 'node' executable. Please install node.js (from http://nodejs.org).");
    }

    private static boolean isExe(String p) {
        File f = new File(p);
        return f.exists() && f.canExecute();
    }

    public static int run(List<String> repos, String module, String func, PrintStream output, boolean debug, String node)
            throws IOException, InterruptedException {
        if (node == null) {
            try {
                node = findNode();
            } catch (FileNotFoundException ex) {
                System.out.println(ex.getMessage());
                return -1;
            }
        } else if (!isExe(node)) {
            throw new IllegalArgumentException("Specified node executable is invalid: " + node);
        }
        //The timeout is to have enough time to start reading on the process streams
        final boolean isDefault = "default".equals(module);
        String version = "";
        if (!isDefault) {
            version = module.substring(module.indexOf('/')+1);
            module = module.substring(0, module.indexOf('/'));
        }
        final String eval = String.format("setTimeout(function(){},50);require('%s%s/%s%s').%s();",
                module.replace(".", "/"),
                isDefault ? "" : "/" + version,
                module,
                isDefault ? "" : "-" + version,
                func);
        ProcessBuilder proc = new ProcessBuilder(node, "-e", eval);
        String nodePath = getNodePath();
        String ceylonRepo = getCeylonRepo();
        if (nodePath == null || nodePath.isEmpty()) {
            nodePath = ceylonRepo;
        } else if (ceylonRepo != null && !ceylonRepo.isEmpty()) {
            nodePath = nodePath + File.pathSeparator + ceylonRepo;
        }
        //Now append repositories
        for (String repo : repos) {
            if (nodePath == null || nodePath.isEmpty()) {
                nodePath = repo;
            } else {
                nodePath = nodePath + File.pathSeparator + repo;
            }
        }
        proc.environment().put("NODE_PATH", nodePath);
        Process nodeProcess = proc.start();
        //All this shit because inheritIO doesn't work on fucking Windows
        new CeylonRunJsTool.ReadStream(nodeProcess.getInputStream(),
                output == null ? System.out : output).start();
        new ReadErrorStream(nodeProcess.getErrorStream(),
                output == null ? System.err : output, debug).start();
        return nodeProcess.waitFor();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length == 0) {
            usage();
            System.exit(1);
            return;
        }
        List<String> opts = new ArrayList<String>();
        opts.addAll(Arrays.asList(args));
        if (Options.findOption("-version", opts, true)) {
            System.err.println("Version: ceylon-js " + Versions.CEYLON_VERSION);
        }
        if (Options.findOption("-help", opts, true)) {
            usage();
            return;
        }
        boolean debug = Options.findOption("-debug", opts, true);
        List<String> repos = Options.findRepos(opts, true);
        String func = Options.findOptionValue("-run", opts, true);
        if (repos.isEmpty()) {
            repos.add("modules");
        }
        if (func == null) {
            func = "run";
        }
        if (opts.isEmpty()) {
            System.err.println("ceylon-js: You must specify a module name and version.");
            usage();
            return;
        }
        String module = opts.remove(0);
        if (!opts.isEmpty()) {
            System.err.println("ceylon-js: Command-line arguments not supported yet.");
        }
        final boolean isDefault = "default".equals(module);
        if (!isDefault && !module.contains("/")) {
            System.out.println("Specified module is not default and is missing version.");
            usage();
            return;
        }
        int exitCode = run(repos, module, func, null, debug, null);
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
