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
    	String path = getNodeExe();
    	if(path != null && !path.isEmpty() && isExe(path))
    		return path;
        String[] paths = { "/usr/bin/node", "/usr/local/bin/node", "/bin/node", "/opt/bin/node", 
        		"C:\\Program Files\\nodejs\\node.exe", "C:\\Program Files (x86)\\nodejs\\node.exe" };
        for (String p : paths) {
        	if(isExe(p))
        		return p;
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
        System.err.println("Found node at "+node);
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
        String sep = File.separator;
//        if(File.separatorChar == '\\')
//        	sep = "\\\\"; // we need to double it in the eval arg on windows
        
        final String eval = String.format("require('%s%s%s%s%s').%s();",
                module.replace(".", sep), 
                isDefault ? "" : sep + version,
                sep,
                module, 
                isDefault ? "" : "-" + version, 
                func);
        System.err.println("Eval: "+eval);
        ProcessBuilder proc = new ProcessBuilder(node, "-e", eval);
        String nodePath = getNodePath() + File.pathSeparator + getCeylonRepo() + File.pathSeparator + repo;
        proc.environment().put("NODE_PATH", nodePath);
        System.err.println("Setting NODE_PATH to "+nodePath);
        proc.inheritIO();
        Process nodeProcess = proc.start();
        int exitCode = nodeProcess.waitFor();
        System.err.println("Exit code: "+exitCode);
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
		if(path != null)
			return path;
		return System.getProperty(prop);
	}

}
