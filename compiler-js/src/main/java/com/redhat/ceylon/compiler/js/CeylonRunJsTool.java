package com.redhat.ceylon.compiler.js;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.CmrRepository;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.ModuleQuery.Type;
import com.redhat.ceylon.cmr.impl.AssemblyRepositoryBuilder;
import com.redhat.ceylon.cmr.util.JarUtils;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.config.DefaultToolOptions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Rest;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tools.CeylonTool;
import com.redhat.ceylon.common.tools.RepoUsingTool;
import com.redhat.ceylon.compiler.js.loader.JsModuleSourceMapper;
import com.redhat.ceylon.compiler.js.util.JsIdentifierNames;
import com.redhat.ceylon.model.cmr.ArtifactResult;

@Summary("Executes a Ceylon program on Node.js")
@Description(
        "Executes the ceylon program specified as the `module` argument. " +
        "The `module` may optionally include a version."
)
@RemainingSections(
        RepoUsingTool.DOCSECTION_COMPILE_FLAGS +
        "\n\n" +
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

    private static String findNodeInPath(String path) {
        if (path != null) {
            String [] paths = path.split(File.pathSeparator);
            for (String p : paths) {
                String d = p.endsWith(File.separator) ? p : p + File.separator;
                String np = d + "node";
                if (isExe(np)) {
                    return np;
                }
                np = d + "node.exe";
                if (isExe(np)) {
                    return np;
                }
                np = d + "nodejs";
                if (isExe(np)) {
                    return np;
                }
                np = d + "nodejs.exe";
                if (isExe(np)) {
                    return np;
                }
            }
        }
        return null;
    }

    /** Finds the full path to the Node.js executable. */
    public static String findNode() {
        String path = getNodeExe();
        if (path != null && !path.isEmpty() && isExe(path)) {
            return path;
        }
        //Now let's look for the executable in all path elements
        String winpath = findNodeInPath(System.getenv("PATH"));
        if (winpath != null) {
            return winpath;
        }
        String errmsg = "Could not find 'node' executable. Please install Node.js (from http://nodejs.org)."
                + "\nMake sure the path to the node executable is included in your PATH environment variable."
                + "\nIf you have node installed in a non-standard location, you can either set the environment variable"
                + "\nNODE_EXE or the JVM system property '" + Constants.PROP_CEYLON_EXTCMD_NODE + "' with the full path to the node executable.";
        throw new CeylonRunJsException(errmsg);
    }

    private static boolean isExe(String p) {
        File f = new File(p);
        return f.exists() && f.canExecute();
    }

    private String module;
    private File assembly;
    private String func;
    private String compileFlags;
    private String exepath;
    private List<String> args;
    private PrintStream output;
    private boolean debug;
    
    public CeylonRunJsTool() {
        super(CeylonRunJsMessages.RESOURCE_BUNDLE);
    }

    /**
     * Ignored: this is now always true.
     * @deprecated this is now always true.
     */
    public void setThrowOnError(boolean throwOnError) {
    }
    
    /** Sets the PrintStream to use for output. Default is System.out. */
    public void setOutput(PrintStream value) {
        output = value;
    }

    @OptionArgument(argumentName="option")
    @Description("Passes an option to the underlying ceylon compiler.")
    public void setCompilerArguments(List<String> compilerArguments) {
        this.compilerArguments = compilerArguments;
    }
    
    @OptionArgument(argumentName="debug")
    @Description("Shows more detailed output in case of errors.")
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @OptionArgument(shortName='x', longName = "run", argumentName = "toplevel")
    @Description("Specifies the fully qualified name of a toplevel method or class to run. " +
            "The indicated declaration must be shared by the `module` and have no parameters. " +
            "The format is: `qualified.package.name::classOrMethodName` with `::` acting as separator " +
            "between the package name and the toplevel class or method name. (default: `module.name::run`)")
    public void setRun(String func) {
        this.func = func;
    }

    @Option(shortName='c')
    @OptionArgument(argumentName = "flags")
    @Description("Determines if and how compilation should be handled. " +
            "Allowed flags include: `never`, `once`, `force`, `check`. " +
            "If no flags are specified, defaults to `check`.")
    public void setCompile(String compile) {
        this.compileFlags = compile;
    }

    @Argument(argumentName="module", multiplicity="?", order=1)
    public void setModuleVersion(String moduleVersion) {
        this.module= moduleVersion;
    }

    @OptionArgument(shortName='a', argumentName="archive")
    @Description("Specifies the path to a Ceylon archive that should be executed")
    public void setAssembly(File assembly) {
        this.assembly = assembly;
    }

    @Rest
    public void setArgs(List<String> args) {
        this.args = args;
    }

    @OptionArgument(argumentName="node-exe")
    @Description("The path to the Node.js executable. Will be searched in standard locations if not specified.")
    public void setNodeExe(String path) {
        this.exepath=path;
    }

    @Override
    protected Type getCompilerType() {
        return ModuleQuery.Type.JS;
    }

    private static String getNodePath() {
        return getFromEnv("NODE_PATH", "node.path");
    }

    private static String getNodeExe() {
        return getFromEnv("NODE_EXE", Constants.PROP_CEYLON_EXTCMD_NODE);
    }

    private static String getCeylonRepo() {
        return System.getProperty(Constants.PROP_CEYLON_SYSTEM_REPO);
    }

    private static String getFromEnv(String env, String prop){
        String path = System.getProperty(prop);
        if (path != null) {
            return path;
        }
        return System.getenv(env);
    }

    /** Creates a ProcessBuilder ready to run the Node.js executable with the specified parameters.
     * @param module The module name and version (if it's not the default).
     * @param func The function name to run (must be specified)
     * @param args The optional command-line arguments to pass to the function
     * @param exepath The full path to the Node.js executable
     * @param repos The list of repository paths (used as module paths for Node.js)
     * @param output An optional PrintStream to write the output of the Node.js process to. */
    private ProcessBuilder buildProcess(final String module, final String version, String func, List<String> args,
            String exepath, final List<File> repos, PrintStream output) {
        final String node = exepath == null ? findNode() : exepath;
        if (exepath != null) {
            if (!isExe(exepath)) {
                throw new CeylonRunJsException("Specified Node.js executable is invalid.");
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
        if (JsIdentifierNames.isReservedWord(func)) {
            func = "$_" + func;
        }
        final boolean isDefault = ModuleUtil.isDefaultModule(module);
        String moduleString = isDefault ? module : module +"/"+version;
        final String overrideRequire = "function $$ceylon$require(a,b,c){return b(c[0].substr(0,16)=='ceylon/language/'?'ceylon/language/" +
                Versions.CEYLON_VERSION_NUMBER + "/ceylon.language-" + Versions.CEYLON_VERSION_NUMBER + "'+(c[0].substr(-6)=='-model'?'-model':''):c[0]);};";
        //The timeout is to have enough time to start reading on the process streams
        String eval = String.format("if(typeof setTimeout==='function'){setTimeout(function(){},50)};" +
                overrideRequire +
                "var __entry_point__=require('%s%s/%s%s').%s;if (__entry_point__===undefined){" +
                "console.log('The specified method \"%s\" does not exist or is not shared in the %s module');" +
                "process.exit(1);}else __entry_point__();",
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
                    System.out.println("Be warned, old timer: JavaScript code generated by the Ceylon compiler will most likely not run on Node.js versions older than 0.8");
                    System.out.println();
                }
            }
        } catch (IOException|NumberFormatException ex) {
            System.out.println("Cannot determine Node.js version; you should be using 0.8 or above");
        }
        final ProcessBuilder proc;
        if (args != null && !args.isEmpty()) {
            ArrayList<String> newargs = new ArrayList<String>(args.size() + 4);
            newargs.add(node);
            newargs.add("-e");
            newargs.add(eval);
            newargs.add("dummy"); // See https://github.com/ceylon/ceylon.language/issues/503
            newargs.addAll(args);
            proc = new ProcessBuilder(newargs.toArray(new String[0]));
        } else {
            proc = new ProcessBuilder(node, "-e", eval);
        }
        StringBuilder nodePath = new StringBuilder();
        appendToNodePath(nodePath, getNodePath());
        //Now append repositories
        for (File repo : repos) {
            appendToNodePath(nodePath, repo.getPath());
        }
        if (debug) {
            System.out.println("NODE_PATH=" + nodePath);
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
        final Map<String,Object> model = JsModuleSourceMapper.loadJsonModel(jsmod);
        if (model == null) {
            return Collections.emptyList();
        }
        @SuppressWarnings("unchecked")
        List<Object> deps = (List<Object>)model.get("$mod-deps");
        return deps;
    }

    protected void loadDependencies(List<File> repos, RepositoryManager repoman,
            File jsmod, Set<String> loaded) throws IOException {
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
                @SuppressWarnings("unchecked")
                final Map<String,Object> depmap = (Map<String,Object>)dep;
                depname = depmap.get("path").toString();
                optional = new Integer(1).equals(depmap.get("opt"));
            }
            if (!loaded.contains(depname)) {
                loaded.add(depname);
                //Module names have escaped forward slashes due to JSON encoding
                int idx = depname.indexOf('/');
                final String modname = depname.substring(0, idx);
                final String modvers = depname.substring(idx+1);
                File other = getArtifact(repoman, modname, modvers, optional);
                if (other != null) {
                    final File f = getRepoDir(modname, other);
                    if (!repos.contains(f)) {
                        repos.add(f);
                    }
                    loadDependencies(repos, repoman, other, loaded);
                }
            }
        }
    }

    @Override
    public void initialize(CeylonTool mainTool) throws Exception {
        super.initialize(mainTool);
        
        if (assembly != null) {
            // The --compile flag makes no sense in combination with --assembly
            compileFlags = COMPILE_NEVER;
            
            JarFile jar = JarUtils.validJar(assembly);
            if (jar != null) {
                Manifest manifest = jar.getManifest();
                if (manifest != null) {
                    // Add the assembly to the repositories
                    if (repos == null) {
                        repos = new ArrayList<URI>(1);
                    }
                    repos.add(0, new URI("assembly:" + assembly.getPath()));
                    
                    // Determine which module to execute
                    String mfMainMod = manifest.getMainAttributes().getValue(Constants.ATTR_ASSEMBLY_MAIN_MODULE);
                    if (mfMainMod == null) {
                        throw new IllegalArgumentException("Assembly manifest is missing required attribute '" + Constants.ATTR_ASSEMBLY_MAIN_MODULE + "'");
                    }
                    if (module != null) {
                        // This is a bit of a hack, but when we use the --assembly
                        // argument we can't specify a module name/version anymore,
                        // but we can still specify arguments, so if a module name
                        // was specified on the CLI it must be an argument and we
                        // add it to the beginning of the list of arguments
                        args.add(0, module);
                    }
                    module = mfMainMod;
                    
                    // See if there is an overrides file
                    String mfOverrides = manifest.getMainAttributes().getValue(Constants.ATTR_ASSEMBLY_OVERRIDES);
                    if (mfOverrides != null) {
                        // At this point we need to pre-unpack the assembly
                        File assemblyFolder = AssemblyRepositoryBuilder.registerAssembly(assembly);
                        File ovrFile = new File(assemblyFolder, mfOverrides);
                        overrides = ovrFile.getPath();
                    }
                    
                    // See if we need to execute a specific toplevel
                    String mfRun = manifest.getMainAttributes().getValue(Constants.ATTR_ASSEMBLY_RUN);
                    if (mfRun != null) {
                        func = mfRun;
                    }
                } else {
                    throw new IllegalArgumentException("The Assembly does not have a manifest");
                }
            } else {
                throw new IllegalArgumentException("The file specified by '--assembly' is not a valid Ceylon Assembly");
            }
        }
        
        if (module == null) {
            module = DefaultToolOptions.getRunToolModule(com.redhat.ceylon.common.Backend.JavaScript);
            if (module != null) {
                if (func == null) {
                    func = DefaultToolOptions.getRunToolRun(com.redhat.ceylon.common.Backend.JavaScript);
                }
                if (args == null || args.isEmpty()) {
                    args = DefaultToolOptions.getRunToolArgs(com.redhat.ceylon.common.Backend.JavaScript);
                }
            } else {
                throw new IllegalArgumentException("Missing required argument 'module' to command 'run'");
            }
        }
    }

    @Override
    public void run() throws Exception {
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

        compileFlags = processCompileFlags(compileFlags, DefaultToolOptions.getRunToolCompileFlags(com.redhat.ceylon.common.Backend.JavaScript));
        
        version = checkModuleVersionsOrShowSuggestions(
                modname, version, ModuleQuery.Type.JS,
                // JVM binary but don't care since JS
                null, null,
                Versions.JS_BINARY_MAJOR_VERSION, Versions.JS_BINARY_MINOR_VERSION, compileFlags);
        if (version == null) {
            return;
        }
        File jsmod = getArtifact(getRepositoryManager(), modname, version, false);
        // NB localRepos will contain a set of files pointing to the module repositories
        // where all the needed modules can be found
        List<File> localRepos = new ArrayList<>();
        for (CmrRepository r : getRepositoryManager().getRepositories()) {
            if (!r.getRoot().isRemote()) {
                File f = new File(r.getDisplayString());
                if (!localRepos.contains(f)) {
                    localRepos.add(f);
                }
            }
        }
        File rd = getRepoDir(modname, jsmod);
        if (!localRepos.contains(rd)) {
            localRepos.add(rd);
        }
        final Set<String> loadedDependencies = new HashSet<>();
        loadedDependencies.add(module);
        loadDependencies(localRepos, getRepositoryManager(), jsmod, loadedDependencies);
        customizeDependencies(localRepos, getRepositoryManager(), loadedDependencies);

        func = func != null ? func : "run";
        final ProcessBuilder proc = buildProcess(modname, version, func, args, exepath, localRepos, output);
        final Process nodeProcess = proc.start();
        //All this shit because inheritIO doesn't work on fucking Windows
        new ReadStream(nodeProcess.getInputStream(), output == null ? System.out : output).start();
        if (output == null) {
            new ReadErrorStream(nodeProcess.getErrorStream(), System.err, debug).start();
        }
        Thread stopThread = new Thread() {
            public void run() {
                try {
                    nodeProcess.destroy();
                } catch(Throwable t) {}
            };
        };
        try {
            try {
                Runtime.getRuntime().addShutdownHook(stopThread);
            } catch(Throwable t) {}
            int exitCode = nodeProcess.waitFor();
            if (exitCode != 0) {
                if(exitCode == 11)
                    exitCode = 2;
                throw new CeylonRunJsException("Node process exited with non-zero exit code: "+exitCode, exitCode);
            }
        } finally {
            try {
                Runtime.getRuntime().removeShutdownHook(stopThread);
            } catch(Throwable t) {}
        }
    }

    // Make sure JS and JS_MODEL artifacts exist and try to obtain the RESOURCES as well
    protected File getArtifact(RepositoryManager repoman, String modName, String modVersion, boolean optional) {
        final int colonIdx = modName.indexOf(':');
        String namespace = null;
        if (colonIdx > 0) {
            namespace = modName.substring(0,colonIdx);
            modName = modName.substring(colonIdx + 1);
        }
        ArtifactContext ac = new ArtifactContext(namespace, modName, modVersion, ArtifactContext.JS, ArtifactContext.JS_MODEL, ArtifactContext.RESOURCES);
        ac.setIgnoreDependencies(true);
        ac.setThrowErrorIfMissing(false);
        List<ArtifactResult> results = repoman.getArtifactResults(ac);
        ArtifactResult code = getArtifactType(results, ArtifactContext.JS);
        ArtifactResult model = getArtifactType(results, ArtifactContext.JS_MODEL);
        if (code == null || model == null) {
            if (optional) {
                return null;
            } else if (code != null && "npm".equals(namespace)) {
                return null;
            }
            throw new CeylonRunJsException("Cannot find module " + ModuleUtil.makeModuleName(modName, modVersion) + " in specified module repositories");
        }
        return model.artifact();
    }

    protected ArtifactResult getArtifactType(List<ArtifactResult> results, String suffix) {
        for (ArtifactResult r : results) {
            String s = ArtifactContext.getSuffixFromFilename(r.artifact().getName());
            if (s.equals(suffix)) {
                return r;
            }
        }
        return null;
    }

    protected File getRepoDir(String modname, File file) {
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

    protected void customizeDependencies(List<File> localRepos, RepositoryManager repoman, Set<String> loadedDependencies) throws IOException {
    }

    // use to test and debug:
//    public static void main(String[] args) throws Exception{
//    	CeylonRunJsTool tool = new CeylonRunJsTool();
//    	tool.setCwd(new File("../ceylon-js-tests"));
//    	tool.setModuleVersion("default");
//    	tool.run();
//    }
}
