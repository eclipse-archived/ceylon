package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.OutputRepoUsingTool;
import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.config.DefaultToolOptions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.EnumUtil;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.ParsedBy;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.StandardArgumentParsers;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tools.CeylonTool;
import com.redhat.ceylon.common.tools.SourceArgumentsResolver;
import com.redhat.ceylon.compiler.js.util.Options;
import com.redhat.ceylon.compiler.loader.JsModuleManagerFactory;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.analyzer.Warning;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.model.typechecker.context.TypeCache;

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
    private EnumSet<Warning> suppwarns = EnumUtil.enumsFromStrings(Warning.class,
            DefaultToolOptions.getCompilerSuppressWarnings());

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
        return (out != null) ? out : DefaultToolOptions.getCompilerOutputRepo();
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
    public void initialize(CeylonTool mainTool) throws IOException {
    }

    @Override
    public void run() throws Exception {
        setSystemProperties();
        AppendableWriter writer = new AppendableWriter(getOutAppendable());
        final Options opts = new Options()
                .cwd(cwd)
                .repos(getRepositoryAsStrings())
                .sourceDirs(roots)
                .resourceDirs(resources)
                .resourceRootName(resourceRootName)
                .systemRepo(systemRepo)
                .outRepo(getOut())
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
                .outWriter(writer)
                .suppressWarnings(suppwarns);
        final TypeChecker typeChecker;
        if (opts.hasVerboseFlag("cmr")) {
            append("Using repositories: "+getRepositoryAsStrings());
            newline();
        }
        final RepositoryManager repoman = getRepositoryManager();
        long t0, t1, t2, t3, t4;
        final TypeCheckerBuilder tcb;
        List<File> onlySources = null;
        List<File> onlyResources = null;
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
                @Override
                public int compareTo(VirtualFile o) {
                    return getPath().compareTo(o.getPath());
                }
            };
            t0 = System.nanoTime();
            tcb = new TypeCheckerBuilder()
                .addSrcDirectory(src);
        } else {
            t0=System.nanoTime();
            tcb = new TypeCheckerBuilder();
            
            SourceArgumentsResolver resolver = new SourceArgumentsResolver(roots, resources, Constants.CEYLON_SUFFIX, Constants.JS_SUFFIX);
            resolver
                .cwd(cwd)
                .expandAndParse(files, Backend.JavaScript);
            onlySources = resolver.getSourceFiles();
            onlyResources = resolver.getResourceFiles();
            
            if (opts.isVerbose()) {
                append("Adding source directories to typechecker:" + roots).newline();
            }
            for (File root : roots) {
                File cwdRoot = applyCwd(root);
                if (cwdRoot.exists() && cwdRoot.isDirectory()) {
                    tcb.addSrcDirectory(cwdRoot);
                }
            }
            tcb.setSourceFiles(onlySources);
            if (!resolver.getSourceModules().isEmpty()) {
                tcb.setModuleFilters(resolver.getSourceModules());
            }
            tcb.statistics(opts.isProfile());
            JsModuleManagerFactory.setVerbose(opts.hasVerboseFlag("loader"));
            tcb.moduleManagerFactory(new JsModuleManagerFactory(encoding));
        }
        //getting the type checker does process all types in the source directory
        tcb.verbose(opts.hasVerboseFlag("ast")).setRepositoryManager(repoman);
        tcb.usageWarnings(false).encoding(encoding);

        typeChecker = tcb.getTypeChecker();
        t1=System.nanoTime();
        TypeCache.doWithoutCaching(new Runnable() {
            @Override
            public void run() {
                typeChecker.process(true);
            }
        });
        
        t2=System.nanoTime();
        JsCompiler jsc = new JsCompiler(typeChecker, opts);
        if (onlySources != null) {
            if (opts.isVerbose()) {
                append("Only these files will be compiled: " + onlySources).newline();
            }
            jsc.setSourceFiles(onlySources);
        }
        if (onlyResources != null) {
            jsc.setResourceFiles(onlyResources);
        }
        t3=System.nanoTime();
        if (!jsc.generate()) {
            if (jsc.getExitCode() != 0) {
                if(throwOnError)
                    throw new RuntimeException("Compiler exited with non-zero exit code: "+jsc.getExitCode());
                else {
                    jsc.printErrorsAndCount(writer);
                    System.exit(jsc.getExitCode());
                }
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

    @Option(shortName='W')
    @OptionArgument(argumentName = "warnings")
    @Description("Suppress the reporting of the given warnings. " +
            "If no `warnings` are given then suppresss the reporting of all warnings, " +
            "otherwise just suppresss those which are present. " +
            "Allowed flags include: " +
            "`filenameNonAscii`, `filenameClaselessCollision`, `deprecation`, "+
            "`compilerAnnotation`, `doclink`, `expressionTypeNothing`, "+
            "`unusedDeclaration`, `unusedImport`, `ceylonNamespace`, "+
            "`javaNamespace`, `suppressedAlready`, `suppressesNothing`.")
    public void setSuppressWarning(EnumSet<Warning> warnings) {
        suppwarns = warnings;
    }
}
