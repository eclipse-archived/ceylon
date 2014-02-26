package com.redhat.ceylon.compiler.js;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.RepoUsingTool;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Rest;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.Tools;
import com.redhat.ceylon.compiler.loader.JsModuleManager;

@Summary("Executes a Ceylon program")
@Description(
        "Executes the ceylon program specified as the `<module>` argument. " +
        "The `<module>` may optionally include a version."
)
@RemainingSections(
        "## Configuration file" +
        "\n\n" +
        "The run-js tool accepts the following option from the Ceylon configuration file: " +
        "`runtool.compile` " +
        "(the equivalent option on the command line always has precedence)." +
        "\n\n" +
        "## EXAMPLE" +
        "\n\n" +
        "The following would execute the `com.example.foobar` module:" +
         "\n\n" +
         "    ceylon run-js com.example.foobar/1.0.0"
)
public class CeylonRunJsTool extends RepoUsingTool {

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
                "C:\\Program Files\\nodejs\\node.exe", "C:\\Program Files (x86)\\nodejs\\node.exe",
                "C:\\Program Files\\nodejs\\nodejs.exe", "C:\\Program Files (x86)\\nodejs\\nodejs.exe" };
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

    private String func = "run";
    private String compileFlags;
    private String module;
    private String exepath;
    private List<String> args;
    private PrintStream output;
    private boolean debug;
    
    public CeylonRunJsTool() {
        super(CeylonRunJsMessages.RESOURCE_BUNDLE);
    }

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

    @Option
    @OptionArgument(argumentName = "flags")
    @Description("Determines if and how compilation should be handled. " +
            "Allowed flags include: `never`, `once`, `force`, `check`.")
    public void setCompile(String compile) {
        this.compileFlags = compile;
    }

    @Argument(argumentName="module", multiplicity="1", order=1)
    public void setModuleVersion(String moduleVersion) {
        this.module= moduleVersion;
    }

    @Rest
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
        return getFromEnv("CEYLON_REPO", Constants.PROP_CEYLON_SYSTEM_REPO);
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
    static ProcessBuilder buildProcess(final String module, final String version, String func, List<String> args,
            String exepath, Set<File> repos, PrintStream output) {
        final String node = exepath == null ? findNode() : exepath;
        if (exepath != null) {
            File _f = new File(exepath);
            if (!(_f.exists() && _f.canExecute())) {
                throw new CeylonRunJsException("Specified node.js executable is invalid.");
            }
        }

        //Rename func
        if (func.startsWith("::")) {
            func = func.substring(2);
        } else if (func.indexOf('.') > 0 || func.indexOf("::") > 0) {
            //Given a fully qualified name such as a.b.c.run, remove the module path first
            //then change what remains to run$subpackages i.e. module a.b then run$c
            if (func.contains("::")) {
                func = func.replace("::", ".");
            }
            if (func.startsWith(module)) {
                func = func.substring(module.length()+1);
            }
            if (func.indexOf('.') > 0) {
                final StringBuilder fsb = new StringBuilder();
                final int lastDot = func.lastIndexOf('.');
                fsb.append(func.substring(lastDot+1)).append('$');
                fsb.append(func.substring(0,lastDot).replaceAll("\\.", "\\$"));
                func = fsb.toString();
            }
        }
        final boolean isDefault = ModuleUtil.isDefaultModule(module);
        String moduleString = isDefault ? module : module +"/"+version;
        //The timeout is to have enough time to start reading on the process streams
        final String eval = String.format("if(typeof setTimeout==='function'){setTimeout(function(){},50)};var __entry_point__=require('%s%s/%s%s').%s;if (__entry_point__===undefined)console.log('The specified method \"%s\" does not exist or is not shared in the %s module');else __entry_point__();",
                module.replace(".", "/"),
                isDefault ? "" : "/" + version,
                module,
                isDefault ? "" : "-" + version,
                func, func, moduleString);
        final ProcessBuilder versionProc = new ProcessBuilder(java.util.Arrays.asList(node, "-v"));
        try {
            Process versionProcess = versionProc.start();
            BufferedReader lineReader = new BufferedReader(new InputStreamReader(versionProcess.getInputStream()));
            String nodeVersion = lineReader.readLine();
            versionProcess.destroy();
            if (nodeVersion.charAt(0)=='v') {
                nodeVersion = nodeVersion.substring(1);
            }
            String[] versionParts = nodeVersion.split("\\.");
            if (versionParts.length > 1) {
                if (Integer.parseInt(versionParts[0], 10) == 0 && Integer.parseInt(versionParts[1], 10) < 8) {
                    System.out.println("Be warned, old timer: JavaScript code generated by the Ceylon compiler will most likely not run on node.js versions older than 0.8");
                    System.out.println();
                }
            }
        } catch (IOException|NumberFormatException ex) {
            System.out.println("Cannot determine node.js version; you should be using 0.8 or above");
        }
        final ProcessBuilder proc;
        if (args != null && !args.isEmpty()) {
            args.add(0, node);
            args.add(1, "-e");
            args.add(2, eval);
            proc = new ProcessBuilder(args.toArray(new String[0]));
        } else {
            proc = new ProcessBuilder(node, "-e", eval);
        }
        StringBuilder nodePath = new StringBuilder();
        appendToNodePath(nodePath, getNodePath());
        //Now append repositories
        for (File repo : repos) {
            appendToNodePath(nodePath, repo.getPath());
        }
        proc.environment().put("NODE_PATH", nodePath.toString());
        if (output != null) {
            proc.redirectErrorStream();
        }
        return proc;
    }

    private static String appendToNodePath(StringBuilder nodePath, String repo) {
        if (repo == null || repo.isEmpty()) return "";
        if (nodePath.length() > 0) {
            nodePath.append(File.pathSeparator);
        }
        nodePath.append(repo);
        return repo;
    }

    private List<Object> getDependencies(File jsmod) throws IOException {
        final Map<String,Object> model = JsModuleManager.loadMetamodel(jsmod);
        if (model == null) {
            return Collections.emptyList();
        }
        @SuppressWarnings("unchecked")
        List<Object> deps = (List<Object>)model.get("$mod-deps");
        return deps;
    }

    private void loadDependencies(Set<File> repos, RepositoryManager repoman, File jsmod) throws IOException {
        final List<Object> deps = getDependencies(jsmod);
        if (deps == null) {
            return;
        }
        for (Object dep : deps) {
            final String depname;
            boolean optional = false;
            if (dep instanceof String) {
                //it's a mandatory dependency
                depname = (String)dep;
            } else {
                depname = (String)((Map)dep).get("path");
                optional = "1".equals(((Map)dep).get("opt"));
            }
            //Module names have escaped forward slashes due to JSON encoding
            int idx = depname.indexOf('/');
            ArtifactContext ac = new ArtifactContext(depname.substring(0, idx), depname.substring(idx+1), ".js");
            ac.setFetchSingleArtifact(true);
            ac.setThrowErrorIfMissing(!optional);
            File other = repoman.getArtifact(ac);
            if (other != null) {
                repos.add(getRepoDir(ac.getName(), other));
                loadDependencies(repos, repoman, other);
            }
        }
    }

    @Override
    public void initialize() {
    }

    @Override
    public void run() throws Exception {
        setSystemProperties();
        //The timeout is to have enough time to start reading on the process streams
        if (systemRepo == null) {
            systemRepo = getCeylonRepo();
        }
        final boolean isDefault = ModuleUtil.isDefaultModule(module);
        String version;
        final String modname;
        if (isDefault) {
            modname = module;
            version = "";
        } else {
            version = ModuleUtil.moduleVersion(module);
            modname = ModuleUtil.moduleName(module);
        }

        //Create a repository manager to load the js module we're going to run
        final RepositoryManager repoman = getRepositoryManager();
        
        version = checkModuleVersionsOrShowSuggestions(
                repoman, modname, version, ModuleQuery.Type.JS,
                Versions.JS_BINARY_MAJOR_VERSION, Versions.JS_BINARY_MINOR_VERSION, compileFlags);
        if (version == null) {
            return;
        }
        
        ArtifactContext ac = new ArtifactContext(modname, version, ".js");
        ac.setFetchSingleArtifact(true);
        ac.setThrowErrorIfMissing(false);
        File jsmod = repoman.getArtifact(ac);
        if (jsmod == null) {
            throw new CeylonRunJsException("Cannot find module " + module + " in specified module repositories");
        }
        // NB localRepos will contain a set of files pointing to the module repositories
        // where all the needed modules can be found
        Set<File> localRepos = new HashSet<File>();
        localRepos.add(getRepoDir(modname, jsmod));
        loadDependencies(localRepos, repoman, jsmod);
        
        final ProcessBuilder proc = buildProcess(modname, version, func, args, exepath, localRepos, output);
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

    private File getRepoDir(String modname, File file) {
        // A trippy way to get to the repo folder, but it works
        int count = modname.split("\\.").length + 1;
        if (!ModuleUtil.isDefaultModule(modname)) {
            count++;
        }
        for (int i=0; i < count; i++) {
            file = file.getParentFile();
        }
        return file;
    }

    // use to test and debug:
//    public static void main(String[] args) throws Exception{
//    	CeylonRunJsTool tool = new CeylonRunJsTool();
//    	tool.setCwd(new File("../ceylon-js-tests"));
//    	tool.setModuleVersion("default");
//    	tool.run();
//    }
}
