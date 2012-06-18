package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/** Command-line runner for ceylonc-js compiled code, using node.js
 * 
 * @author Enrique Zamudio
 */
public class Runner {

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
        //TODO also look for windows executable
        String[] paths = { "/usr/bin/node", "/usr/local/bin/node", "/bin/node", "/opt/bin/node" };
        for (String p : paths) {
            File f = new File(p);
            if (f.exists() && f.canExecute()) {
                return p;
            }
        }
        System.err.println("Could not find 'node' executable. Please install node.js and retry.");
        System.exit(2);
        return null;
    }

    public static String findOptionValue(String optionName, List<String> options) {
        int idx = options.indexOf(optionName);
        if (idx >=0 && idx < options.size() - 2 && !options.get(idx+1).startsWith("-")) {
            return options.get(idx+1);
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
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
            repo = "./modules";
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
        final String eval = String.format("require('%s%s/%s%s').%s();",
                module.replace('.', File.separatorChar), isDefault ? "" : "/" + version,
                module, isDefault ? "" : "-" + version, func);
        ProcessBuilder proc = new ProcessBuilder(node, "-e", eval).directory(new File("."));
        proc.environment().put("NODE_PATH", System.getenv("NODE_PATH") + ":" + System.getenv("CEYLON_REPO") + ":"+ repo);
        proc.inheritIO();
        proc.start();
    }

}
