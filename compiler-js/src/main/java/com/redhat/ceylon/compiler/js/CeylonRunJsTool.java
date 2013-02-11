package com.redhat.ceylon.compiler.js;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.Tool;
import com.redhat.ceylon.common.tool.Tools;

@Summary("Executes a Ceylon program")
@Description(
"Executes the ceylon program specified as the `<module>` argument. "+
"The `<module>` should include a version."
)
@RemainingSections(
"##EXAMPLE\n" +
"\n" +
"The following would execute the `com.example.foobar` module:\n" +
"\n" +
"    ceylon run-js com.example.foobar/1.0.0"
)
public class CeylonRunJsTool implements Tool {

    /** A thread dedicated to reading from a stream and storing the result to return it as a String. */
    public static class ReadStream extends Thread {
        protected final InputStream in;
        protected final PrintStream out;
        protected final byte[] buf  = new byte[16384];
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

    /** Finds the full path to the node.js executable. */
    public static String findNode() {
        String path = getNodeExe();
        if (path != null && !path.isEmpty() && isExe(path)) {
            return path;
        }
        String[] paths = { "/usr/bin/node", "/usr/bin/nodejs", "/usr/local/bin/node", "/bin/node", "/opt/bin/node",
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
        throw new CeylonRunJsException("Could not find 'node' executable. Please install node.js (from http://nodejs.org).");
    }

    private static boolean isExe(String p) {
        File f = new File(p);
        return f.exists() && f.canExecute();
    }

    private List<String> repos = Collections.singletonList("modules");
    private String func = "run";
    private String module;
    private String exepath;
    private String sysrep;
    private List<String> args;
    private PrintStream output;
    private boolean debug;

    /** Sets the PrintStream to use for output. Default is System.out. */
    public void setOutput(PrintStream value) {
        output = value;
    }

    @OptionArgument(argumentName="debug")
    @Description("Shows more detailed output in case of errors.")
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @OptionArgument(argumentName="func")
    @Description("The function to run, which must be exported from the " +
    		"given `<module>`. (default: `run`).")
    public void setRun(String func) {
        this.func = func;
    }

    @OptionArgument(argumentName="url")
    @Description("A module repository. (default: `./modules`).")
    public void setRepositories(List<String> repos) {
        this.repos = repos;
    }
    @OptionArgument
    @Description("Specifies the system repository containing essential modules. "
            +"(default is the environment variable CEYLON_REPO)")
    public void setSysrep(String value) {
        sysrep = value;
    }

    @Argument(argumentName="module", multiplicity="1", order=1)
    public void setModuleVersion(String moduleVersion) {
        this.module= moduleVersion;
    }

    @Argument(argumentName="args", multiplicity="*", order=2)
    public void setArgs(List<String> args) {
        this.args = args;
    }

    @OptionArgument(argumentName="node-exe")
    @Description("The path to the node.js executable. Will be searched in standard locations if not specified.")
    public void setNodeExe(String path) {
        this.exepath=path;
    }

    private static String getNodePath() {
        return getFromEnv("NODE_PATH", "node.path");
    }

    private static String getNodeExe() {
        return getFromEnv("NODE_EXE", "node.exe");
    }

    private static String getCeylonRepo() {
        return getFromEnv("CEYLON_REPO", "ceylon.system.repo");
    }

    private static String getFromEnv(String env, String prop){
        String path = System.getenv(env);
        if (path != null) {
            return path;
        }
        return System.getProperty(prop);
    }

    /** Creates a ProcessBuilder ready to run the node.js executable with the specified parameters.
     * @param module The module name and version (if it's not the default).
     * @param func The function name to run (must be specified)
     * @param args The optional command-line arguments to pass to the function
     * @param exepath The full path to the node.js executable
     * @param repos The list of repository paths (used as module paths for node.js)
     * @param output An optional PrintStream to write the output of the node.js process to. */
    static ProcessBuilder buildProcess(String module, String func, List<String> args,
            String exepath, List<String> repos, String systemRepo, PrintStream output) {
        final String node = exepath == null ? findNode() : exepath;
        if (exepath != null) {
            File _f = new File(exepath);
            if (!(_f.exists() && _f.canExecute())) {
                throw new CeylonRunJsException("Specified node.js executable is invalid.");
            }
        }

        final boolean isDefault = "default".equals(module);
        String version = "";
        if (!isDefault && !module.contains("/")) {
            throw new CeylonRunJsException("Specified module is not default and is missing version.");
        }
        if (!isDefault) {
            version = module.substring(module.indexOf('/')+1);
            module = module.substring(0, module.indexOf('/'));
        }

        //The timeout is to have enough time to start reading on the process streams
        final String eval = String.format("setTimeout(function(){},50);require('%s%s/%s%s').%s();",
                module.replace(".", "/"),
                isDefault ? "" : "/" + version,
                module,
                isDefault ? "" : "-" + version,
                func);
        final ProcessBuilder proc;
        if (args != null && !args.isEmpty()) {
            args.add(0, node);
            args.add(1, "-e");
            args.add(2, eval);
            proc = new ProcessBuilder(args.toArray(new String[0]));
        } else {
            proc = new ProcessBuilder(node, "-e", eval);
        }
        String nodePath = getNodePath();
        if (nodePath == null || nodePath.isEmpty()) {
            nodePath = systemRepo;
        } else if (systemRepo != null && !systemRepo.isEmpty()) {
            nodePath = nodePath + File.pathSeparator + systemRepo;
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
        if (output != null) {
            proc.redirectErrorStream();
        }
        return proc;
    }

    @Override
    public void run() throws Exception {
        //The timeout is to have enough time to start reading on the process streams
        if (sysrep == null) {
            sysrep = getCeylonRepo();
        }
        final ProcessBuilder proc = buildProcess(module, func, args, exepath, repos, sysrep, output);
        Process nodeProcess = proc.start();
        //All this shit because inheritIO doesn't work on fucking Windows
        new ReadStream(nodeProcess.getInputStream(), output == null ? System.out : output).start();
        if (output == null) {
            new ReadErrorStream(nodeProcess.getErrorStream(), System.err, debug).start();
        }
        int exitCode = nodeProcess.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException(Tools.progName() + " node exit code: "+exitCode);
        }
    }

}
