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
        public ReadErrorStream(InputStream from, PrintStream to) {
            this.in = new BufferedReader(new InputStreamReader(from));
            this.out = to;
        }
        public void run() {
            try {
                String line = in.readLine();
                while (line != null) {
                    if (line.trim().startsWith("throw new")) {
                        printing = false;
                    } else if (line.startsWith("Error: Cannot find module")) {
                        out.println(line);
                        printing = false;
                    } else if (!printing) {
                        printing = !(line.isEmpty() || line.startsWith("    at "));
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
    
    private List<String> repos = Collections.singletonList("modules");
    private String func = "run";
    private String module;
    
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
    
    @Argument(argumentName="module", multiplicity="1")
    public void setModuleVersion(String moduleVersion) {
        this.module= moduleVersion;
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
    
    @Override
    public void run() throws Exception {
        final String node = findNode();
        
        final boolean isDefault = "default".equals(module);
        String version = "";
        if (!isDefault && !module.contains("/")) {
            System.out.println("Specified module is not default and is missing version.");
            return;
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
        new ReadStream(nodeProcess.getInputStream(), System.out).start();
        new ReadErrorStream(nodeProcess.getErrorStream(), System.err).start();
        int exitCode = nodeProcess.waitFor();
        if (exitCode != 0) {
            throw new Exception(Tools.progName() + " node exit code: "+exitCode);
        }        
    }

}
