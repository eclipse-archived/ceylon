package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.OutputRepoUsingTool;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.config.DefaultToolOptions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.ParsedBy;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.StandardArgumentParsers;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tools.ModuleWildcardsHelper;
import com.redhat.ceylon.compiler.Options;
import com.redhat.ceylon.compiler.loader.JsModuleManagerFactory;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;

@Summary("Compiles Ceylon source code to JavaScript and directly produces " +
        "module and source archives in a module repository")
@RemainingSections(
        OutputRepoUsingTool.DOCSECTION_CONFIG_COMPILER +
        "\n\n" +
        OutputRepoUsingTool.DOCSECTION_REPOSITORIES)
public class CeylonCompileJsTool extends OutputRepoUsingTool {

    public static class AppendableWriter extends Writer {

        private Appendable out;

        public AppendableWriter(Appendable out) {
            this.out = out;
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            out.append(new String(cbuf, off, len));
        }

        @Override
        public void write(String str) throws IOException {
            out.append(str);
        }
    }

    private boolean profile = false;
    private boolean optimize = true;
    private boolean modulify = true;
    private boolean indent = true;
    private boolean comments = false;
    private boolean skipSrc = false;

    private String encoding;

    private List<File> roots = DefaultToolOptions.getCompilerSourceDirs();
    private List<File> resources = DefaultToolOptions.getCompilerResourceDirs();
    private String resourceRootName = DefaultToolOptions.getCompilerResourceRootName();
    private List<String> files = Arrays.asList("*");
    private DiagnosticListener diagnosticListener;
    private boolean throwOnError;

    public CeylonCompileJsTool() {
        super(CeylonCompileJsMessages.RESOURCE_BUNDLE);
    }

    @OptionArgument(shortName='E', argumentName="encoding")
    @Description("Sets the encoding used for reading source files (default: platform-specific)")
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding(){
        return encoding;
    }

    @Option
    @Description("Time the compilation phases (results are printed to standard error)")
    public void setProfile(boolean profile) {
        this.profile = profile;
    }

    @Option
    @Description("Create lexical scope-style JS code")
    public void setLexicalScopeStyle(boolean flag) {
        this.optimize = !flag;
    }

    @Option(longName="no-module")
    @Description("Do **not** wrap generated code as CommonJS module")
    public void setNoModulify(boolean nomodulify) {
        this.modulify = !nomodulify;
    }

    @Option
    @Description("Do **not** indent code")
    public void setNoIndent(boolean noindent) {
        this.indent = !noindent;
    }

    @Option
    @Description("Equivalent to `--no-indent` `--no-comments`")
    public void setCompact(boolean compact) {
        this.setNoIndent(compact);
        this.setNoComments(compact);
    }

    @Option
    @Description("Do **not** generate any comments")
    public void setNoComments(boolean nocomments) {
        this.comments = !nocomments;
    }

    public List<String> getFilesAsStrings(final List<File> files) {
        if (files != null) {
            List<String> result = new ArrayList<>(files.size());
            for (File f : files) {
                result.add(f.getPath());
            }
            return result;
        } else {
            return Collections.emptyList();
        }
    }

    @OptionArgument(shortName='s', longName="src", argumentName="dirs")
    @ParsedBy(StandardArgumentParsers.PathArgumentParser.class)
    @Description("Path to source files. " +
    		"Can be specified multiple times; you can also specify several " +
    		"paths separated by your operating system's `PATH` separator." +
            " (default: `./source`)")
    public void setSrc(List<File> src) {
        roots = src;
    }

    @OptionArgument(longName="source", argumentName="dirs")
    @ParsedBy(StandardArgumentParsers.PathArgumentParser.class)
    @Description("An alias for `--src`" +
            " (default: `./source`)")
    public void setSource(List<File> source) {
        setSrc(source);
    }

    @OptionArgument(shortName='r', longName="resource", argumentName="dirs")
    @ParsedBy(StandardArgumentParsers.PathArgumentParser.class)
    @Description("Path to directory containing resource files. " +
            "Can be specified multiple times; you can also specify several " +
            "paths separated by your operating system's `PATH` separator." +
            " (default: `./resource`)")
    public void setResource(List<File> resource) {
        this.resources = resource;
    }

    @OptionArgument(shortName='R', argumentName="folder-name")
    @Description("Sets the special resource folder name whose files will " +
            "end up in the root of the resulting module CAR file (default: ROOT).")
    public void setResourceRoot(String resourceRootName) {
        this.resourceRootName = resourceRootName;
    }
    
    public String getOut() {
        return (out != null) ? out : DefaultToolOptions.getCompilerOutDir().getPath();
    }

    @Option
    @Description("Do **not** generate .src archive - useful when doing joint compilation")
    public void setSkipSrcArchive(boolean skip) {
        skipSrc = skip;
    }
    public boolean isSkipSrcArchive() {
        return skipSrc;
    }

    @Argument(argumentName="moduleOrFile", multiplicity="*")
    public void setModule(List<String> moduleOrFile) {
        this.files = moduleOrFile;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void run() throws Exception {
        setSystemProperties();
        AppendableWriter writer = new AppendableWriter(getOutAppendable());
        final Options opts = new Options()
                .cwd(cwd)
                .repos(getRepositoryAsStrings())
                .sources(getFilesAsStrings(roots))
                .systemRepo(systemRepo)
                .outDir(getOut())
                .user(user)
                .pass(pass)
                .optimize(optimize)
                .modulify(modulify)
                .indent(indent)
                .comment(comments)
                .verbose(getVerbose())
                .profile(profile)
                .stdin(false)
                .generateSourceArchive(!skipSrc)
                .encoding(encoding)
                .diagnosticListener(diagnosticListener)
                .outWriter(writer);
        final TypeChecker typeChecker;
        if (opts.hasVerboseFlag("cmr")) {
            append("Using repositories: "+getRepositoryAsStrings());
            newline();
        }
        final RepositoryManager repoman = getRepositoryManager();
        final List<String> onlyFiles = new ArrayList<>();
        final List<File> onlyRes   = new ArrayList<>();
        long t0, t1, t2, t3, t4;
        final TypeCheckerBuilder tcb;
        final List<File> resrcs = FileUtil.applyCwd(cwd, resources);
        if (opts.isStdin()) {
            VirtualFile src = new VirtualFile() {
                @Override
                public boolean isFolder() {
                    return false;
                }
                @Override
                public String getName() {
                    return "SCRIPT.ceylon";
                }
                @Override
                public String getPath() {
                    return getName();
                }
                @Override
                public InputStream getInputStream() {
                    return System.in;
                }
                @Override
                public List<VirtualFile> getChildren() {
                    return Collections.emptyList();
                }
                @Override
                public int hashCode() {
                    return getPath().hashCode();
                }
                @Override
                public boolean equals(Object obj) {
                    if (obj instanceof VirtualFile) {
                        return ((VirtualFile) obj).getPath().equals(getPath());
                    }
                    else {
                        return super.equals(obj);
                    }
                }
            };
            t0 = System.nanoTime();
            tcb = new TypeCheckerBuilder()
                .addSrcDirectory(src);
        } else {
            t0=System.nanoTime();
            tcb = new TypeCheckerBuilder();
            final Set<String> modfilters = new HashSet<>();
            
            final List<File> srcs = FileUtil.applyCwd(cwd, roots);
            final List<String> expandedModulesOrFiles = ModuleWildcardsHelper.expandWildcards(srcs , files);
            for (String filedir : expandedModulesOrFiles) {
                File f = new File(filedir);
                boolean once=false;
                if (f.exists() && f.isFile()) {
                    for (File root : roots) {
                        if (f.getAbsolutePath().startsWith(root.getAbsolutePath() + File.separatorChar)) {
                            if (opts.isVerbose()) {
                                append("Adding "+filedir+" to compilation set");
                                newline();
                            }
                            onlyFiles.add(normalizePath(filedir));
                            once=true;
                            break;
                        }
                    }
                    if (!once) {
                        for (File r : resrcs) {
                            if (f.getAbsolutePath().startsWith(r.getAbsolutePath() + File.separatorChar)) {
                                if (opts.isVerbose()) {
                                    append("Adding "+filedir+" to resource set");
                                    newline();
                                }
                                onlyRes.add(f);
                                once=true;
                                break;
                            }
                        }
                        if (!once) {
                            throw new CompilerErrorException(String.format("%s is not in any source path: %n", f.getAbsolutePath()));
                        }
                    }
                } else if ("default".equals(filedir)) {
                    //Default module: load every file in the source directories recursively,
                    //except any file that exists in directories and subdirectories where we find a module.ceylon file
                    //Typechecker takes care of all that if we add default to module filters
                    if (opts.isVerbose()) {
                        append("Adding default module filter"); newline();
                    }
                    for (File root : roots) {
                        addFilesToCompilationSet(opts.isVerbose(), root, onlyFiles, true);
                    }
                    for (File r : resources) {
                        addFilesToResourceSet(opts.isVerbose(), r, onlyRes);
                    }
                    modfilters.add("default");
                    f = null;
                } else {
                    //Parse, may be a module name
                    String[] modpath = filedir.split("\\.");
                    f = null;
                    for (File root : roots) {
                        File _f = root;
                        for (String pe : modpath) {
                            _f = new File(_f, pe);
                            if (!(_f.exists() && _f.isDirectory())) {
                                _f=null;
                                break;
                            }
                        }
                        if (_f != null) {
                            f = _f;
                            if (opts.isVerbose()) {
                                append("Adding dir to module filters: " + f.getAbsolutePath()); newline();
                            }
                            addFilesToCompilationSet(opts.isVerbose(), f, onlyFiles, false);
                            modfilters.add(filedir);
                        }
                        //TODO mirror resource dir?
                    }
                    if (f == null) {
                        String msg;
                        if (ModuleWildcardsHelper.isModuleName(filedir)) {
                            msg = String.format("ceylonc-js: Could not find source files for module: %s%n", filedir);
                        } else {
                            msg = String.format("ceylonc-js: file not found: %s%n", filedir);
                        }
                        throw new CompilerErrorException(msg);
                    }
                }
                if (f != null) {
                    if (Constants.MODULE_DESCRIPTOR.equals(f.getName().toLowerCase())) {
                        String _f = f.getParentFile().getAbsolutePath();
                        for (File root : roots) {
                            if (root.getAbsolutePath().startsWith(_f)) {
                                _f = _f.substring(root.getAbsolutePath().length()+1).replace(File.separator, ".");
                                modfilters.add(_f);
                                if (opts.isVerbose()) {
                                    append("Adding file to module filters: " + _f);newline();
                                }
                            }
                        }
                    } else {
                        for (File root : roots) {
                            File middir = f.getParentFile();
                            while (middir != null && !middir.getAbsolutePath().equals(root.getAbsolutePath())) {
                                if (new File(middir, Constants.MODULE_DESCRIPTOR).exists()) {
                                    String _f = middir.getAbsolutePath().substring(root.getAbsolutePath().length()+1).replace(
                                            File.separatorChar, '.');
                                    modfilters.add(_f);
                                    if (opts.isVerbose()) {
                                        append("Adding file to module filters: " + _f);newline();
                                    }
                                }
                                middir = middir.getParentFile();
                            }
                        }
                    }
                } //f!= null
            } //loop over files

            if (opts.isVerbose()) {
                append("Adding source directories to typechecker:" + roots);newline();
            }
            for (File root : roots) {
                tcb.addSrcDirectory(root);
            }
            if (!modfilters.isEmpty()) {
                tcb.setModuleFilters(new ArrayList<>(modfilters));
            }
            tcb.statistics(opts.isProfile());
            JsModuleManagerFactory.setVerbose(opts.isVerbose());
            tcb.moduleManagerFactory(new JsModuleManagerFactory(encoding));
        }
        //getting the type checker does process all types in the source directory
        tcb.verbose(opts.isVerbose()).setRepositoryManager(repoman);
        tcb.usageWarnings(false).encoding(encoding);

        typeChecker = tcb.getTypeChecker();
        if (!onlyFiles.isEmpty()) {
            for (PhasedUnit pu : typeChecker.getPhasedUnits().getPhasedUnits()) {
                if (!onlyFiles.contains(normalizePath(pu.getUnitFile().getPath()))) {
                    if (opts.isVerbose()) {
                        append("Removing phased unit " + pu);newline();
                    }
                    typeChecker.getPhasedUnits().removePhasedUnitForRelativePath(pu.getPathRelativeToSrcDir());
                }
            }
        }
        t1=System.nanoTime();
        typeChecker.process(true);
        
        t2=System.nanoTime();
        JsCompiler jsc = new JsCompiler(typeChecker, opts);
        if (!onlyFiles.isEmpty()) {
            if (opts.isVerbose()) {
                append("Only these files will be compiled: " + onlyFiles);newline();
            }
            jsc.setFiles(onlyFiles);
        }
        if (onlyRes.isEmpty()) {
            for (File f : resrcs) {
                if (f.exists() && f.isDirectory()) {
                    addFilesToResourceSet(opts.isVerbose(), f, onlyRes);
                }
            }
        }
        if (!onlyRes.isEmpty()) {
            opts.resources(getFilesAsStrings(onlyRes))
                .resourceDirs(getFilesAsStrings(resrcs))
                .resourceRootName(resourceRootName);
        }
        t3=System.nanoTime();
        if (!jsc.generate()) {
            if (jsc.getExitCode() != 0) {
                if(throwOnError)
                    throw new RuntimeException("Compiler exited with non-zero exit code: "+jsc.getExitCode());
                else
                    System.exit(jsc.getExitCode());
            }
            int count = jsc.printErrors(writer);
            throw new CompilerErrorException(String.format("%d errors.", count));
        }
        t4=System.nanoTime();
        if (opts.isProfile() || opts.hasVerboseFlag("benchmark")) {
            System.err.println("PROFILING INFORMATION");
            System.err.printf("TypeChecker creation:   %6d nanos%n", t1-t0);
            System.err.printf("TypeChecker processing: %6d nanos%n", t2-t1);
            System.err.printf("JS compiler creation:   %6d nanos%n", t3-t2);
            System.err.printf("JS compilation:         %6d nanos%n", t4-t3);
            System.out.println("Compilation finished.");
        }
    }

    private static void addFilesToCompilationSet(boolean verbose, File dir, List<String> onlyFiles, boolean skipModules) {
        if (skipModules) {
            File module = new File(dir, Constants.MODULE_DESCRIPTOR);
            if (module.isFile()) {
                // Seems this folder contains a module, so we skip it
                return;
            }
        }
        for (File e : dir.listFiles()) {
            String n = e.getName().toLowerCase();
            if (e.isFile() && (n.endsWith(Constants.CEYLON_SUFFIX) || n.endsWith(Constants.JS_SUFFIX))) {
                String path = normalizePath(e.getPath());
                if (verbose) {
                    System.out.println("Adding to compilation set: " + path);
                }
                if (!onlyFiles.contains(path)) {
                    onlyFiles.add(path);
                }
            } else if (e.isDirectory()) {
                addFilesToCompilationSet(verbose, e, onlyFiles, skipModules);
            }
        }
    }

    private static void addFilesToResourceSet(boolean verbose, File dir, List<File> onlyFiles) {
        for (File e : dir.listFiles()) {
            if (e.isFile()) {
                String path = normalizePath(e.getPath());
                if (verbose) {
                    System.out.println("Adding to resource set: " + path);
                }
                if (!onlyFiles.contains(path)) {
                    onlyFiles.add(new File(path));
                }
            } else if (e.isDirectory()) {
                addFilesToResourceSet(verbose, e, onlyFiles);
            }
        }
    }

    private static String normalizePath(String path) {
    	return path.replace('\\', '/');
    }

    /**
     * Sets the diagnostic listener. Not part of the command-line contract, only used by APIs.
     */
    public void setDiagnosticListener(DiagnosticListener diagnosticListener) {
        this.diagnosticListener = diagnosticListener;
    }
    
    /**
     * Tell the tool not to exit on a non-zero exit code from node, but throw otherwise. This is not
     * used by the command-line, but can be useful when invoked via the API.
     * @param throwOnError true to throw instead of calling System.exit. Defaults to false.
     */
    public void setThrowOnError(boolean throwOnError) {
        this.throwOnError = throwOnError;
    }
    
    /**
     * Check if we throw on a non-zero exit code from node, rather than exit. This is not
     * used by the command-line, but can be useful when invoked via the API.
     * @return true to throw instead of calling System.exit. Defaults to false.
     */
    public boolean isThrowOnError() {
        return throwOnError;
    }
}
