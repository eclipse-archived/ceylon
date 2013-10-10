package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.ceylon.RepoUsingTool;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.compiler.Options;
import com.redhat.ceylon.compiler.loader.JsModuleManagerFactory;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;

@Summary("Compiles Ceylon source code to JavaScript and directly produces " +
        "module and source archives in a module repository")
public class CeylonCompileJsTool extends RepoUsingTool {

    private boolean profile = false;
    private boolean optimize = false;
    private boolean modulify = true;
    private boolean indent = true;
    private boolean comments = false;
    private boolean skipSrc = false;

    private String user = null;
    private String pass = null;
    private String out = "modules";
    private String encoding;

    private List<String> src = Collections.singletonList("source");
    private List<String> files = Collections.emptyList();

    public CeylonCompileJsTool() {
        super(CeylonCompileJsMessages.RESOURCE_BUNDLE);
    }

    @OptionArgument(argumentName="encoding")
    @Description("Sets the encoding used for reading source files (default: platform-specific)")
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding(){
        return encoding;
    }

    public boolean isProfile() {
        return profile;
    }

    @Option
    @Description("Time the compilation phases (results are printed to standard error)")
    public void setProfile(boolean profile) {
        this.profile = profile;
    }

    public boolean isOptimize() {
        return optimize;
    }

    @Option
    @Description("Create prototype-style JS code")
    public void setOptimize(boolean optimize) {
        this.optimize = optimize;
    }

    public boolean isModulify() {
        return modulify;
    }

    @Option(longName="no-module")
    @Description("Do **not** wrap generated code as CommonJS module")
    public void setNoModulify(boolean nomodulify) {
        this.modulify = !nomodulify;
    }

    public boolean isIndent() {
        return indent;
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

    public boolean isComments() {
        return comments;
    }

    @Option
    @Description("Do **not** generate any comments")
    public void setNoComments(boolean nocomments) {
        this.comments = !nocomments;
    }

    public String getUser() {
        return user;
    }

    @OptionArgument(argumentName="name")
    @Description("Sets the user name for use with an authenticated output repository" +
            "(no default).")
    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    @OptionArgument(argumentName="secret")
    @Description("Sets the password for use with an authenticated output repository" +
            "(no default).")
    public void setPass(String pass) {
        this.pass = pass;
    }

    public List<String> getRepos() {
        return getRepositoryAsStrings();
    }

    public List<String> getSrc() {
        return src;
    }

    @OptionArgument(longName="src", argumentName="dirs")
    @Description("Path to source files. " +
    		"Can be specified multiple times; you can also specify several " +
    		"paths separated by your operating system's `PATH` separator." +
            " (default: `./source`)")
    public void setSrc(List<String> src) {
        this.src = src;
    }

    public String getOut() {
        return out;
    }

    @Option
    @Description("Do **not** generate .src archive - useful when doing joint compilation")
    public void setSkipSrcArchive(boolean skip) {
        skipSrc = skip;
    }
    public boolean isSkipSrcArchive() {
        return skipSrc;
    }

    @OptionArgument(argumentName="url")
    @Description("Specifies the output module repository (which must be publishable). " +
            "(default: `./modules`)")
    public void setOut(String out) {
        this.out = out;
    }

    @Argument(argumentName="moduleOrFile", multiplicity="+")
    public void setModule(List<String> moduleOrFile) {
        this.files = moduleOrFile;
    }

    @Override
    public void run() throws Exception {
        final Options opts = new Options()
                .cwd(cwd)
                .repos(getRepos())
                .sources(getSrc())
                .systemRepo(systemRepo)
                .cacheRepo(cacheRepo)
                .outDir(getOut())
                .user(getUser())
                .pass(getPass())
                .optimize(isOptimize())
                .modulify(isModulify())
                .indent(isIndent())
                .comment(isComments())
                .verbose(getVerbose())
                .profile(isProfile())
                .stdin(false)
                .generateSourceArchive(!skipSrc)
                .encoding(encoding)
                .offline(offline)
                .noDefaultRepos(noDefRepos);
        run(opts, files);
    }

    private static void addFilesToCompilationSet(Options opts, File dir, List<String> onlyFiles) {
        for (File e : dir.listFiles()) {
            String n = e.getName().toLowerCase();
            if (e.isFile() && (n.endsWith(".ceylon") || n.endsWith(".js"))) {
                String path = normalizePath(e.getPath());
                if (opts.isVerbose()) {
                    System.out.println("Adding to compilation set: " + path);
                }
                if (!onlyFiles.contains(path)) {
                    onlyFiles.add(path);
                }
            } else if (e.isDirectory()) {
                addFilesToCompilationSet(opts, e, onlyFiles);
            }
        }
    }

    private static String normalizePath(String path) {
    	return path.replace('\\', '/');
    }
    
    public static void run(Options opts, List<String> files) throws IOException {
        final TypeChecker typeChecker;
        if (opts.hasVerboseFlag("cmr")) {
            System.out.printf("Using repositories: %s%n", opts.getRepos());
        }
        final RepositoryManager repoman = CeylonUtils.repoManager()
                .cwd(opts.getCwd())
                .systemRepo(opts.getSystemRepo())
                .cacheRepo(opts.getCacheRepo())
                .noDefaultRepos(opts.getNoDefaultRepos())
                .userRepos(opts.getRepos())
                .outRepo(opts.getOutDir())
                .offline(opts.getOffline())
                .buildManager();
        final List<String> onlyFiles = new ArrayList<String>();
        long t0, t1, t2, t3, t4;
        final TypeCheckerBuilder tcb;
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
                    return new ArrayList<VirtualFile>(0);
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
            final List<File> roots = new ArrayList<File>(opts.getSrcDirs().size());
            for (String _srcdir : opts.getSrcDirs()) {
                roots.add(new File(_srcdir));
            }
            final Set<String> modfilters = new HashSet<String>();
            
            for (String filedir : files) {
                File f = new File(filedir);
                boolean once=false;
                if (f.exists() && f.isFile()) {
                    for (File root : roots) {
                        if (f.getAbsolutePath().startsWith(root.getAbsolutePath() + File.separatorChar)) {
                            if (opts.isVerbose()) {
                                System.out.printf("Adding %s to compilation set%n", filedir);
                            }
                            onlyFiles.add(normalizePath(filedir));
                            once=true;
                            break;
                        }
                    }
                    if (!once) {
                        throw new CompilerErrorException(String.format("%s is not in any source path: %n", f.getAbsolutePath()));
                    }
                } else if ("default".equals(filedir)) {
                    //Default module: load every file in the source directories recursively,
                    //except any file that exists in directories and subdirectories where we find a module.ceylon file
                    //Typechecker takes care of all that if we add default to module filters
                    if (opts.isVerbose()) {
                        System.out.println("Adding default module filter");
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
                                System.err.printf("ceylonc-js: Could not find source files for module: %s%n", filedir);
                                _f=null;
                                break;
                            }
                        }
                        if (_f != null) {
                            f = _f;
                        }
                    }
                    if (f == null) {
                        throw new CompilerErrorException(String.format("ceylonc-js: file not found: %s%n", filedir));                        
                    } else {
                        if (opts.isVerbose()) {
                            System.out.println("Adding to module filters: " + filedir);
                        }
                        addFilesToCompilationSet(opts, f, onlyFiles);
                        modfilters.add(filedir);
                        f = null;
                    }
                }
                if (f != null) {
                    if ("module.ceylon".equals(f.getName().toLowerCase())) {
                        String _f = f.getParentFile().getAbsolutePath();
                        for (File root : roots) {
                            if (root.getAbsolutePath().startsWith(_f)) {
                                _f = _f.substring(root.getAbsolutePath().length()+1).replace(File.separator, ".");
                                modfilters.add(_f);
                                if (opts.isVerbose()) {
                                    System.out.println("Adding to module filters: " + _f);
                                }
                            }
                        }
                    } else {
                        for (File root : roots) {
                            File middir = f.getParentFile();
                            while (middir != null && !middir.getAbsolutePath().equals(root.getAbsolutePath())) {
                                if (new File(middir, "module.ceylon").exists()) {
                                    String _f = middir.getAbsolutePath().substring(root.getAbsolutePath().length()+1).replace(
                                            File.separatorChar, '.');
                                    modfilters.add(_f);
                                    if (opts.isVerbose()) {
                                        System.out.println("Adding to module filters: " + _f);
                                    }
                                }
                                middir = middir.getParentFile();
                            }
                        }
                    }
                } //f!= null
            } //loop over files

            for (File root : roots) {
                tcb.addSrcDirectory(root);
            }
            if (!modfilters.isEmpty()) {
                ArrayList<String> _modfilters = new ArrayList<String>(modfilters.size());
                _modfilters.addAll(modfilters);
                tcb.setModuleFilters(_modfilters);
            }
            tcb.statistics(opts.isProfile());
            JsModuleManagerFactory.setVerbose(opts.isVerbose());
            tcb.moduleManagerFactory(new JsModuleManagerFactory(opts.getEncoding()));
        }
        //getting the type checker does process all types in the source directory
        tcb.verbose(opts.isVerbose()).setRepositoryManager(repoman);
        tcb.usageWarnings(false);

        typeChecker = tcb.getTypeChecker();
        if (!onlyFiles.isEmpty()) {
            for (PhasedUnit pu : typeChecker.getPhasedUnits().getPhasedUnits()) {
                if (!onlyFiles.contains(normalizePath(pu.getUnitFile().getPath()))) {
                    typeChecker.getPhasedUnits().removePhasedUnitForRelativePath(pu.getPathRelativeToSrcDir());
                }
            }
        }
        t1=System.nanoTime();
        typeChecker.process();
        
        t2=System.nanoTime();
        JsCompiler jsc = new JsCompiler(typeChecker, opts);
        if (!onlyFiles.isEmpty()) { jsc.setFiles(onlyFiles); }
        t3=System.nanoTime();
        if (!jsc.generate()) {
            int count = jsc.printErrors(System.out);
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
}
