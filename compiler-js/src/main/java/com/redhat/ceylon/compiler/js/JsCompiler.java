package com.redhat.ceylon.compiler.js;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactCreator;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.impl.ShaSigner;
import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.log.Logger;
import com.redhat.ceylon.compiler.js.loader.JsModuleSourceMapper;
import com.redhat.ceylon.compiler.js.loader.NpmAware;
import com.redhat.ceylon.compiler.js.util.JsIdentifierNames;
import com.redhat.ceylon.compiler.js.util.JsLogger;
import com.redhat.ceylon.compiler.js.util.JsOutput;
import com.redhat.ceylon.compiler.js.util.NpmDescriptorGenerator;
import com.redhat.ceylon.compiler.js.util.Options;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.analyzer.MissingNativeVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.Warning;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.typechecker.util.WarningSuppressionVisitor;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.ImportableScope;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Unit;

public class JsCompiler {

    protected final TypeChecker tc;
    protected final Options opts;
    protected final RepositoryManager outRepo;
    private final ErrorCollectingVisitor errorVisitor;

    private boolean stopOnErrors = true;
    private int errCount = 0;

    protected final List<VirtualFile> srcDirectories;
    protected List<File> srcFiles;
    protected List<File> resFiles;
    private final Map<Module, JsOutput> output = new HashMap<Module, JsOutput>();
    //You have to manually set this when compiling the language module
    private boolean compilingLanguageModule;
    private int exitCode = 0;
    private Logger logger;
    private JsIdentifierNames names;

    private class JsMissingNativeVisitor extends MissingNativeVisitor {
        
        public JsMissingNativeVisitor(File cwd) {
            super(Backend.JavaScript);
        }
        
        protected boolean checkNative(Node node, Declaration model) {
            if (model.getUnit() != node.getUnit()
                    && ModelUtil.isForBackend(
                            model.getUnit().getPackage().getModule().getNativeBackends(),
                            Backend.JavaScript)) {
                // If the model doesn't come from the current unit we're
                // compiling we check if it comes from a module that
                // supports JS and if so we assume everything is okay.
                // This is not actually correct! But it's HACK for the
                // IDE that doesn't really know about JS modules
                return true;
            }
            return canStitchNative(model);
        }
    }
    
    public int getExitCode() {
        return exitCode;
    }

    public JsCompiler(TypeChecker tc, Options options) {
        this(tc, options, false);
    }
    
    public JsCompiler(TypeChecker tc, Options options, boolean compilingLanguageModule) {
        this.tc = tc;
        this.opts = options;
        this.compilingLanguageModule = compilingLanguageModule;
        outRepo = CeylonUtils.repoManager()
                .cwd(options.getCwd())
                .outRepo(options.getOutRepo())
                .user(options.getUser())
                .password(options.getPass())
                .buildOutputManager();
        logger = opts.getLogger();
        if(logger == null) {
            logger = new JsLogger(opts);
        }
        errorVisitor = new ErrorCollectingVisitor(tc);
        srcDirectories = initSourceDirectories(opts);
    }

    protected List<VirtualFile> initSourceDirectories(Options opts) {
        ArrayList<VirtualFile> srcdirs = new ArrayList<VirtualFile>(opts.getSrcDirs().size());
        for (File srcdir : opts.getSrcDirs()) {
            srcdirs.add(sourceDirVirtual(srcdir));
        }
        return srcdirs;
    }
    
    protected VirtualFile sourceDirVirtual(File f) {
        return tc.getContext().getVfs().getFromFile(f);
    }
    
    private boolean isURL(String path) {
        try {
            new URL(path);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    /** Specifies whether the compiler should stop when errors are found in a compilation unit (default true). */
    public JsCompiler stopOnErrors(boolean flag) {
        stopOnErrors = flag;
        return this;
    }

    /** Sets the names of the files to compile. By default this is null, which means all units from the typechecker
     * will be compiled. */
    public JsCompiler setSourceFiles(List<File> files) {
        if (files != null && !files.isEmpty()) {
            this.srcFiles = files;
        }
        return this;
    }

    /** Sets the names of the resources to pack with the compiler output. */
    public JsCompiler setResourceFiles(List<File> files) {
        this.resFiles = files;
        return this;
    }

    /** Compile one phased unit. */
    private int compileUnit(PhasedUnit pu, JsIdentifierNames names) throws IOException {
        if (opts.isVerbose()) {
            logger.debug("Compiling "+pu.getUnitFile().getPath()+" to JS");
        }
        JsOutput jsout = getOutput(pu);
        MissingNativeVisitor mnv = new JsMissingNativeVisitor(opts.getCwd());
        pu.getCompilationUnit().visit(mnv);
        GenerateJsVisitor jsv = new GenerateJsVisitor(this, jsout, opts, names, pu.getTokens());
        pu.getCompilationUnit().visit(jsv);
        pu.getCompilationUnit().visit(errorVisitor);
        return jsv.getExitCode();
    }

    /** Indicates if compilation should stop, based on whether there were errors
     * in the last compilation unit and the stopOnErrors flag is set. */
    protected boolean stopOnError() {
        errCount += errorVisitor.getErrorCount();
        return stopOnErrors && errCount > 0;
    }

    /** Compile all the phased units in the typechecker.
     * @return true is compilation was successful (0 errors/warnings), false otherwise. */
    public boolean generate() throws IOException {
        errorVisitor.clear();
        errCount = 0;
        output.clear();
        try {
            if (opts.isVerbose()) {
                logger.debug("Generating metamodel...");
            }
            List<PhasedUnit> typecheckerPhasedUnits = tc.getPhasedUnits().getPhasedUnits();
            List<PhasedUnit> phasedUnits = new ArrayList<>(typecheckerPhasedUnits.size());
            for (PhasedUnit pu : typecheckerPhasedUnits) {
                if (srcFiles == null) {
                    phasedUnits.add(pu);
                } else {
                    File path = getFullPath(pu);
                    if (srcFiles.contains(path)) {
                        phasedUnits.add(pu);
                    }
                }
            }
            boolean generatedCode = false;
            
            //First generate the metamodel
            final Module defmod = tc.getContext().getModules().getDefaultModule();
            for (PhasedUnit pu: phasedUnits) {
                //#416 default module with packages
                Module mod = pu.getPackage().getModule();
                if (mod.getVersion() == null && !mod.isDefaultModule()) {
                    //Switch with the default module
                    for (com.redhat.ceylon.model.typechecker.model.Package pkg : mod.getPackages()) {
                        defmod.getPackages().add(pkg);
                        pkg.setModule(defmod);
                    }
                }
                EnumSet<Warning> suppressedWarnings = opts.getSuppressWarnings();
                if (suppressedWarnings == null)
                    suppressedWarnings = EnumSet.noneOf(Warning.class);
                pu.getCompilationUnit().visit(
                            new WarningSuppressionVisitor<>(Warning.class, suppressedWarnings));
                //Perform capture analysis
                for (com.redhat.ceylon.model.typechecker.model.Declaration d : pu.getDeclarations()) {
                    if (d instanceof TypedDeclaration && d instanceof com.redhat.ceylon.model.typechecker.model.Setter == false) {
                        pu.getCompilationUnit().visit(new ValueVisitor((TypedDeclaration)d));
                    }
                }
                pu.getCompilationUnit().visit(getOutput(pu).mmg);
                if (opts.hasVerboseFlag("ast")) {
                    if (opts.getOutWriter() == null) {
                        logger.debug(pu.getCompilationUnit().toString());
                    } else {
                        opts.getOutWriter().write(pu.getCompilationUnit().toString());
                        opts.getOutWriter().write('\n');
                    }
                }
            }
            //Then write it out and output the reference in the module file
            names = new JsIdentifierNames(this);
            if (!compilingLanguageModule) {
                for (Map.Entry<Module,JsOutput> e : output.entrySet()) {
                    e.getValue().encodeModel(names);
                }
            }
            
            //Output all the require calls for any imports
            final Visitor importVisitor = new Visitor() {
                public void visit(Tree.Import that) {
                    ImportableScope scope =
                            that.getImportMemberOrTypeList().getImportList().getImportedScope();
                    Module _m = that.getUnit().getPackage().getModule();
                    if (scope instanceof Package) {
                        Package pkg = (Package)scope;
                        Module om = pkg.getModule();
                        if (!om.equals(_m) && (!om.isNative() ||
                                om.getNativeBackends().supports(Backend.JavaScript))) {
                            Module impmod = ((Package) scope).getModule();
                            if (impmod instanceof NpmAware && ((NpmAware)impmod).getNpmPath() != null) {
                                output.get(_m).requireFromNpm(impmod, names);
                            } else {
                                output.get(_m).require(impmod, names);
                            }
                        }
                    }
                }
                public void visit(Tree.ImportModule that) {
                    if (that.getImportPath() != null && that.getImportPath().getModel() instanceof Module) {
                        Module m = (Module)that.getImportPath().getModel();
                        //Binary version check goes here now
                        int binMajorVersion = m.getJsMajor();
                        int binMinorVersion = m.getJsMinor();
                        if (m.getJsMajor() == 0) {
                            //Check if it's something we're compiling
                            for (PhasedUnit pu : tc.getPhasedUnits().getPhasedUnits()) {
                                if (pu.getPackage() != null && pu.getPackage().getModule() == m) {
                                    m.setJsMajor(Versions.JS_BINARY_MAJOR_VERSION);
                                    m.setJsMinor(Versions.JS_BINARY_MINOR_VERSION);
                                    binMajorVersion = Versions.JS_BINARY_MAJOR_VERSION;
                                    binMinorVersion = Versions.JS_BINARY_MINOR_VERSION;
                                    break;
                                }
                            }
                            if (m.getJsMajor() == 0) {
                                //Load the module (most likely we're in the IDE if we need to do this)
                                ArtifactContext ac = new ArtifactContext(null, m.getNameAsString(),
                                        m.getVersion(), ArtifactContext.JS_MODEL);
                                ac.setIgnoreDependencies(true);
                                ac.setThrowErrorIfMissing(false);
                                ArtifactResult ar = tc.getContext().getRepositoryManager().getArtifactResult(ac);
                                if (ar == null) {
                                    return;
                                }
                                File js = ar.artifact();
                                if (js != null) {
                                    Map<String,Object> json = JsModuleSourceMapper.loadJsonModel(js);
                                    String binVersion = json.get("$mod-bin").toString();
                                    int p = binVersion.indexOf('.');
                                    binMajorVersion = Integer.valueOf(binVersion.substring(0, p));
                                    binMinorVersion = Integer.valueOf(binVersion.substring(p + 1));
                                }
                            }
                        }
                        if (!Versions.isJsBinaryVersionSupported(binMajorVersion, binMinorVersion)) {
                            that.addError(
                                    "version '"+ m.getVersion() + "' of module '" + m.getNameAsString() + 
                                    "' was compiled by an incompatible version of the compiler (binary version " +
                                    binMajorVersion + "." + binMinorVersion + " of module is not compatible with binary version " + 
                                    Versions.JS_BINARY_MAJOR_VERSION + "." + Versions.JS_BINARY_MINOR_VERSION + " of this compiler)");
                        }
                    }
                }
            };

            for (PhasedUnit pu: phasedUnits) {
                pu.getCompilationUnit().visit(importVisitor);
            }

            //Then generate the JS code
            List<PhasedUnit> pkgs = new ArrayList<>(4);
            if (srcFiles == null && !phasedUnits.isEmpty()) {
                for (PhasedUnit pu: phasedUnits) {
                    if ("module.ceylon".equals(pu.getUnitFile().getName())) {
                        final int t = compileUnit(pu);
                        generatedCode = true;
                        if (t != 0) {
                            return false;
                        }
                    }
                }
                for (PhasedUnit pu: phasedUnits) {
                    if ("package.ceylon".equals(pu.getUnitFile().getName())) {
                        pkgs.add(pu);
                        continue;
                    } else if ("module.ceylon".equals(pu.getUnitFile().getName())) {
                        continue;
                    }
                    final int t = compileUnit(pu);
                    generatedCode = true;
                    if (t == 1) {
                        return false;
                    } else if (t == 2) {
                        break;
                    }
                }
            } else if(srcFiles != null && !srcFiles.isEmpty()
                         // For the specific case of the Stitcher
                         && !typecheckerPhasedUnits.isEmpty()) {
                for (PhasedUnit pu: phasedUnits) {
                    if ("module.ceylon".equals(pu.getUnitFile().getName())) {
                        final int t = compileUnit(pu);
                        generatedCode = true;
                        if (t != 0) {
                            return false;
                        }
                    }
                }

                for (File path : srcFiles) {
                    if (path.getPath().endsWith(ArtifactContext.JS)) {
                        //Just output the file
                        File dir = path.getParentFile();
                        PhasedUnit lastUnit = phasedUnits.isEmpty() ? typecheckerPhasedUnits.get(0) : phasedUnits.get(0);
                        for (PhasedUnit pu : phasedUnits) {
                            if (pu.getUnitFile().getPath().startsWith(dir.getPath())) {
                                lastUnit = pu;
                                break;
                            }
                        }
                        final JsOutput lastOut = getOutput(lastUnit);
                        VirtualFile vpath = findFile(path);
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(vpath.getInputStream(), opts.getEncoding()))) {
                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                if (opts.isMinify()) {
                                    line = line.trim();
                                    if (!opts.isComment() && line.startsWith("//") && !line.contains("*/")) {
                                        continue;
                                    }
                                }
                                if (line.length()==0) {
                                    continue;
                                }
                                lastOut.getWriter().write(line);
                                lastOut.getWriter().write('\n');
                            }
                        } finally {
                            lastOut.addSource(path);
                        }
                        generatedCode = true;
                    } else {
                        //Find the corresponding compilation unit
                        for (PhasedUnit pu : phasedUnits) {
                            File unitFile = getFullPath(pu);
                            if (path.equals(unitFile)) {
                                if (path.getName().equals("package.ceylon")) {
                                    pkgs.add(pu);
                                    continue;
                                } else if (path.getName().equals("module.ceylon")) {
                                    continue;
                                }
                                final int t = compileUnit(pu);
                                generatedCode = true;
                                if (t == 1) {
                                    return false;
                                } else if (t == 2) {
                                    break;
                                }
                            }
                        }
                    }
                }
                if (resFiles!=null) {
                    for (Map.Entry<Module,JsOutput> entry: output.entrySet()) {
                        Module module = entry.getKey();
                        final JsOutput lastOut = getOutput(module);
                        for (File file: filterForModule(resFiles, opts.getResourceDirs(), module.getNameAsString())) {
                            String type = Files.probeContentType(file.toPath());
                            String fileName = file.getName();
                            boolean isResourceFile 
                                    = fileName.endsWith(".properties")
                                    || fileName.endsWith(".txt");
                            if (isResourceFile 
                                    || type!=null && type.startsWith("text")) {
                                Writer writer = lastOut.getWriter();
                                writer.write("ex$.");
                                writer.write(resourceKey(module, file));
                                writer.write("=\"");
                                Pattern pattern = Pattern.compile("\\\\|\\t|\\r|\\f|\\n");
                                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), opts.getEncoding()))) {
                                    String line = null;
                                    while ((line = reader.readLine()) != null) {
                                        if (isResourceFile && opts.isMinify()) {
                                            line = line.trim();
                                            if (line.length()==0) {
                                                continue;
                                            }
                                            if (!opts.isComment() && line.startsWith("#")) {
                                                continue;
                                            }
                                        }
                                        StringBuffer result = new StringBuffer();
                                        Matcher matcher = pattern.matcher(line);
                                        while (matcher.find()) {
                                            String escaped;
                                            switch (matcher.group(0)) {
                                            case "\\": escaped = "\\\\\\\\"; break;
                                            case "\t": escaped = "\\\\t"; break;
                                            case "\r": escaped = "\\\\r"; break;
                                            case "\f": escaped = "\\\\f"; break;
                                            case "\n": escaped = "\\\\n"; break;
                                            default: throw new IllegalStateException();
                                            }
                                            matcher.appendReplacement(result, escaped);
                                        }
                                        matcher.appendTail(result);
                                        writer.write(result.toString());
                                        writer.write("\\n");
                                    }
                                }
                                writer.write("\";\n");
                                generatedCode = true;
                            }
                        }
                    }
                }
            }
            for (PhasedUnit pu: pkgs) {
                final int t = compileUnit(pu);
                generatedCode = true;
                if (t == 1) {
                    return false;
                } else if (t == 2) {
                    break;
                }
            }
            if(!generatedCode){
                logger.error("No source units found to compile");
                exitCode = 2;
            }
        } finally {
            if (exitCode==0) {
                exitCode = finish();
            }
        }
        return errCount == 0 && exitCode == 0;
    }

    private String resourceKey(Module module, File file) {
        return module.getNameAsString().replace(".", "$$") 
                + "$$"
                + file.getName().replace(".", "$$");
    }

    private int compileUnit(PhasedUnit pu) throws IOException {
        exitCode = compileUnit(pu, names);
        if (exitCode != 0) {
            return 1;
        }
        if (stopOnError()) {
            logger.error("Errors found. Compilation stopped.");
            return 2;
        }
        getOutput(pu).addSource(getFullPath(pu));
        return 0;
    }

    VirtualFile findFile(File path) {
        for (VirtualFile root : srcDirectories) {
            String p = path.getPath().replace(File.separator, "/");
            String r = root.getPath();
            if (p.startsWith(r)) {
                File relPath = new File(p.substring(r.length() + 1));
                VirtualFile f = findFile(root, relPath);
                if (f != null) {
                    return f;
                }
            }
        }
        return null;
    }

    private VirtualFile findFile(VirtualFile root, File path) {
        VirtualFile result = root;
        String parts[] = path.getPath().split(Pattern.quote(File.separator));
        outer:
        for (String p : parts) {
            if (p.equals(".")) continue;
            for (VirtualFile f : result.getChildren()) {
                if (f.getName().equals(p)) {
                    result = f;
                    continue outer;
                }
            }
            return null;
        }
        return result;
    }
    
    public File getFullPath(PhasedUnit pu) {
        return new File(pu.getUnit().getFullPath());
    }
    
    /** Creates a JsOutput if needed, for the PhasedUnit.
     * Right now it's one file per module. */
    private JsOutput getOutput(PhasedUnit pu) throws IOException {
        Module mod = pu.getPackage().getModule();
        return getOutput(mod);
    }

    private JsOutput getOutput(Module mod) throws IOException {
        JsOutput jsout = output.get(mod);
        if (jsout==null) {
            jsout = newJsOutput(mod);
            output.put(mod, jsout);
            if (opts.isModulify()) {
                jsout.openWrapper();
            }
        }
        return jsout;
    }

    /** This exists solely so that the web IDE can override it and use a different JsOutput */
    protected JsOutput newJsOutput(Module m) throws IOException {
        return new JsOutput(m, isCompilingLanguageModule());
    }

    JsOutput getOutputForModule(Module m) {
        return output.get(m);
    }
    JsIdentifierNames getNames() {
        return names;
    }

    /** Closes all output writers and puts resulting artifacts in the output repo. */
    protected int finish() throws IOException {
        int result = 0;
        String outDir = CeylonUtils.resolveRepoUrl(opts.getOutRepo());
        if(!isURL(outDir)){
            File root = new File(outDir);
            if (root.exists()) {
                if (!(root.isDirectory() && root.canWrite())) {
                    logger.error("Cannot write to "+root+". Stop.");
                    result = 1;
                }
            } else {
                if (!FileUtil.mkdirs(root)) {
                    logger.error("Cannot create "+root+". Stop.");
                    result = 1;
                }
            }
        }
        for (Map.Entry<Module,JsOutput> entry: output.entrySet()) {
            JsOutput jsout = entry.getValue();

            if (!compilingLanguageModule) {
                jsout.publishUnsharedDeclarations(names);
            }
            if (opts.isModulify()) {
                jsout.closeWrapper();
            }
            String moduleName = entry.getKey().getNameAsString();
            String moduleVersion = entry.getKey().getVersion();
            
            if(opts.getDiagnosticListener() != null)
                opts.getDiagnosticListener().moduleCompiled(moduleName, moduleVersion);
            
            //Create the JS file
            final File jsart = jsout.close();
            final File modart = jsout.getModelFile();
            if (entry.getKey().isDefaultModule()) {
                logger.info("Created module "+moduleName);
            } else if (!compilingLanguageModule) {
                logger.info("Created module "+moduleName+"/"+moduleVersion);
            }
            if (compilingLanguageModule) {
                ArtifactContext artifact = new ArtifactContext(null, "delete", "me", ArtifactContext.JS);
                artifact.setForceOperation(true);
                outRepo.putArtifact(artifact, jsart);
            } else {
                final ArtifactContext artifact = new ArtifactContext(null, moduleName, moduleVersion, ArtifactContext.JS);
                artifact.setForceOperation(true);
                outRepo.putArtifact(artifact, jsart);
                final ArtifactContext martifact = new ArtifactContext(null, moduleName, moduleVersion, ArtifactContext.JS_MODEL);
                martifact.setForceOperation(true);
                outRepo.putArtifact(martifact, modart);
                //js file signature
                ShaSigner.signArtifact(outRepo, artifact, jsart, logger);
                ShaSigner.signArtifact(outRepo, martifact, modart, logger);
                //Create the src archive
                if (opts.isGenerateSourceArchive()) {
                    ArtifactCreator sac = CeylonUtils.makeSourceArtifactCreator(
                            outRepo,
                            opts.getSrcDirs(),
                            moduleName, moduleVersion,
                            opts.hasVerboseFlag("cmr"), logger);
                    sac.copy(FileUtil.filesToPathList(jsout.getSources()));
                }
                if (resFiles != null && !resFiles.isEmpty()) {
                    ArtifactCreator sac = CeylonUtils.makeResourceArtifactCreator(
                            outRepo,
                            opts.getSrcDirs(),
                            opts.getResourceDirs(),
                            opts.getResourceRootName(),
                            moduleName, moduleVersion,
                            opts.hasVerboseFlag("cmr"), logger);
                    sac.copy(FileUtil.filesToPathList(filterForModule(resFiles, opts.getResourceDirs(), moduleName)));
                }
                if (!entry.getKey().isDefaultModule()) {
                    String npmdesc = new NpmDescriptorGenerator(entry.getKey(),
                            opts.isGenerateSourceArchive(), resFiles != null && !resFiles.isEmpty())
                            .generateDescriptor();
                    File npmfile = File.createTempFile("npm", "json");
                    try{
                        try (FileWriter fw = new FileWriter(npmfile)) {
                            fw.write(npmdesc);
                        }
                        final ArtifactContext npmArtifact = new ArtifactContext(null, moduleName, moduleVersion, ArtifactContext.NPM_DESCRIPTOR);
                        npmArtifact.setForceOperation(true);
                        outRepo.putArtifact(npmArtifact, npmfile);
                    }finally{
                        npmfile.delete();
                    }
                }
            }
            FileUtil.deleteQuietly(jsart);
            if (modart!=null) {
                FileUtil.deleteQuietly(modart);
            }
        }
        return result;
    }

    private Collection<File> filterForModule(List<File> files, List<File> roots, String moduleName) {
        ArrayList<File> result = new ArrayList<File>(files.size());
        String moduleNamePath = moduleName.replace('.', File.separatorChar);
        for (File f : files) {
            String rel = FileUtil.relativeFile(roots, f.getPath());
            if (rel.startsWith(moduleNamePath + "/") || rel.startsWith(moduleNamePath + "\\")
                    // FIXME: this is wrong because default modules can contain resources in sub-folders
                    // as long as they don't belong to other modules
                    || ("default".equals(moduleName) && !(rel.contains("/") || rel.contains("\\")))) {
                result.add(f);
            }
        }
        return result;
    }

    public int printErrorsAndCount(Writer out) throws IOException {
        DiagnosticListener diagnosticListener = opts.getDiagnosticListener();
        return errorVisitor.printErrors(out, diagnosticListener, true, true);
    }

    /** Print all the errors found during compilation to the specified stream. 
     * @throws IOException */
    public int printErrors(Writer out) throws IOException {
        DiagnosticListener diagnosticListener = opts.getDiagnosticListener();
        return errorVisitor.printErrors(out, diagnosticListener, true, false);
    }
    
    /** Returns the list of errors found during compilation. */
    public Set<Message> getErrors() {
        return errorVisitor.getErrors();
    }

    /** Writes the beginning of the wrapper function for a JS module. */
    public static void beginWrapper(Writer writer) throws IOException {
        writer.write("(function(define) { define(function(require, ex$, module) {\n");
    }

    /** Writes the require wrapper handling for our wrapper */
    public static void requireWrapper(Writer writer, Module mod) throws IOException {
        writer.write("var $$req$ = require; require = (typeof $$ceylon$require == 'undefined') ? $$req$ : function() { return $$ceylon$require('" + JsCompiler.scriptPath(mod) + "', $$req$, Array.prototype.slice.call(arguments)); }\n");
    }

    /** Writes the ending of the wrapper function for a JS module. */
    public static void endWrapper(Writer writer) throws IOException {
        //Finish the wrapper
        writer.write("});\n");
        writer.write("}(typeof define==='function' && define.amd ? define : function (factory) {\n");
        writer.write("if (typeof exports!=='undefined') { factory(require, exports, module);\n");
        writer.write("} else { throw 'no module loader'; }\n");
        writer.write("}));\n");
    }

    protected boolean nonCeylonUnit(Unit u) {
        return (u.getFilename() != null && !(u.getFilename().isEmpty()||u.getFilename().endsWith(".ceylon")))
                || (u.getPackage() != null && u.getPackage().getModule() != null && u.getPackage().getModule().isJava());
    }

    /** Returns true if the compiler is currently compiling the language module. */
    public boolean isCompilingLanguageModule() {
        return compilingLanguageModule;
    }

    /** Create a path for a require call to fetch the specified module. */
    public static String scriptPath(Module mod) {
        StringBuilder path = new StringBuilder(mod.getNameAsString().replace('.', '/')).append('/');
        if (!mod.isDefaultModule()) {
            path.append(mod.getVersion()).append('/');
        }
        path.append(mod.getNameAsString());
        if (!mod.isDefaultModule()) {
            path.append('-').append(mod.getVersion());
        }
        return path.toString();
    }

    VirtualFile getStitchedFile(final Declaration d, final String suffix) {
        String fqn = d.getQualifiedNameString();
        String name = d.getName();
        if (name == null && d instanceof Constructor) {
            name = "default$constructor";
            if (fqn.endsWith(".null")) {
                fqn = fqn.substring(0, fqn.length()-4);
            }
            fqn = fqn + name;
        }
        if (fqn.startsWith("ceylon.language")) {
            fqn = fqn.substring(15);
        }
        if (fqn.startsWith("::")) {
            fqn=fqn.substring(2);
        }
        fqn = fqn.replace('.', '/').replace("::", "/");
        File path;
        if (isCompilingLanguageModule()) {
            path = new File(Stitcher.LANGMOD_JS_SRC, fqn + suffix);
        } else {
            PhasedUnit pu = tc.getPhasedUnitFromRelativePath(d.getUnit().getRelativePath());
            path = new File(getFullPath(pu).getParentFile(), name + suffix);
        }
        return findFile(path);
    }

    VirtualFile getStitchedConstructorFile(final Declaration d, final String suffix) {
        VirtualFile f;
        if (isCompilingLanguageModule()) {
            f = getStitchedFile(d, suffix + ArtifactContext.JS);
        } else {
            f = findFile(new File(new File(d.getUnit().getFullPath()).getParentFile(),
                    String.format("%s%s.js", names.name(d), suffix)));
        }
        return f;
    }
    
    boolean canStitchNative(final Declaration d) {
        if (isCompilingLanguageModule()) {
            switch(d.getName()){
            // in special files
            case "Integer":
            case "Float":
            case "true":
            case "false":
            case "modules":
                // only native on JVM really
            case "Tuple":
            case "Callable":
            case "Throwable":
            case "Exception":
            case "reach":
                return true;
            }
        }
        VirtualFile f = getStitchedFile(d, ArtifactContext.JS);
        if (f != null)
            return true;
        if (isCompilingLanguageModule()) {
            // try folder
            f = getStitchedFile(d, "");
            if (f != null && f.isFolder())
                return true;
        }
        return false;
    }
    
}
