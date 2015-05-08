package com.redhat.ceylon.compiler.js;

import static com.redhat.ceylon.compiler.typechecker.tree.Util.formatPath;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.isForBackend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactCreator;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.impl.ShaSigner;
import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.log.Logger;
import com.redhat.ceylon.compiler.js.util.JsIdentifierNames;
import com.redhat.ceylon.compiler.js.util.JsLogger;
import com.redhat.ceylon.compiler.js.util.JsOutput;
import com.redhat.ceylon.compiler.js.util.Options;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisError;
import com.redhat.ceylon.compiler.typechecker.analyzer.UsageWarning;
import com.redhat.ceylon.compiler.typechecker.analyzer.Warning;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.model.ImportableScope;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.parser.RecognitionError;
import com.redhat.ceylon.compiler.typechecker.tree.AnalysisMessage;
import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ModuleDescriptor;
import com.redhat.ceylon.compiler.typechecker.tree.UnexpectedError;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.typechecker.util.WarningSuppressionVisitor;

public class JsCompiler {

    protected final TypeChecker tc;
    protected final Options opts;
    protected final RepositoryManager outRepo;

    private boolean stopOnErrors = true;
    private int errCount = 0;

    protected Set<Message> errors = new HashSet<Message>();
    protected Set<Message> unitErrors = new HashSet<Message>();
    protected List<File> srcFiles;
    protected List<File> resFiles;
    private final Map<Module, JsOutput> output = new HashMap<Module, JsOutput>();
    //You have to manually set this when compiling the language module
    static boolean compilingLanguageModule;
    private int exitCode = 0;
    private Logger logger;
    private JsIdentifierNames names;

    public int getExitCode() {
        return exitCode;
    }

    private final Visitor unitVisitor = new Visitor() {
        private boolean hasErrors(Node that) {
            boolean r=false;
            for (Message m: that.getErrors()) {
                unitErrors.add(m);
                r |= m instanceof AnalysisError;
            }
            return r;
        }
        @Override
        public void visitAny(Node that) {
            super.visitAny(that);
            hasErrors(that);
        }
        @Override
        public void visit(Tree.Declaration that) {
            if (isForBackend(that.getAnnotationList(), Backend.JavaScript, that.getUnit())) {
                super.visit(that);
            }
        }
        @Override
        public void visit(Tree.Import that) {
            if (hasErrors(that)) {
                exitCode = 1;
                return;
            }
            super.visit(that);
        }
        @Override
        public void visit(Tree.ImportMemberOrType that) {
            if (hasErrors(that)) return;
            if (that.getImportModel() == null || that.getImportModel().getDeclaration() == null)return;
            Unit u = that.getImportModel().getDeclaration().getUnit();
            if (nonCeylonUnit(u)) {
                that.addUnexpectedError("cannot import Java declarations in Javascript");
            }
            super.visit(that);
        }
        @Override
        public void visit(Tree.ImportModule that) {
            if (hasErrors(that)) {
                exitCode = 1;
            } else if (that.getImportPath() != null && that.getImportPath().getModel() instanceof Module &&
                    ((Module)that.getImportPath().getModel()).isJava()) {
                that.getImportPath().addUnexpectedError("cannot import Java modules in Javascript");
            } else {
                super.visit(that);
            }
        }
        @Override
        public void visit(Tree.BaseMemberOrTypeExpression that) {
            if (hasErrors(that)) return;
            if (that.getDeclaration() != null && that.getDeclaration().getUnit() != null) {
                Unit u = that.getDeclaration().getUnit();
                if (nonCeylonUnit(u)) {
                    that.addUnexpectedError("cannot call Java declarations in Javascript");
                }
            }
            super.visit(that);
        }
        @Override
        public void visit(Tree.QualifiedMemberOrTypeExpression that) {
            if (hasErrors(that)) return;
            if (that.getDeclaration() != null && that.getDeclaration().getUnit() != null) {
                Unit u = that.getDeclaration().getUnit();
                if (nonCeylonUnit(u)) {
                    that.addUnexpectedError("cannot call Java declarations in Javascript");
                }
            }
            super.visit(that);
        }
    };

    public JsCompiler(TypeChecker tc, Options options) {
        this.tc = tc;
        opts = options;
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
    public void setSourceFiles(List<File> files) {
        this.srcFiles = files;
    }

    /** Sets the names of the resources to pack with the compiler output. */
    public void setResourceFiles(List<File> files) {
        this.resFiles = files;
    }

    public Set<Message> listErrors() {
        return getErrors();
    }

    /** Compile one phased unit.
     * @return The errors found for the unit. */
    public void compileUnit(PhasedUnit pu, JsIdentifierNames names) throws IOException {
        unitErrors.clear();
        pu.getCompilationUnit().visit(unitVisitor);
        if (exitCode != 0) {
            errors.addAll(unitErrors);
            return;
        }
        if (errCount == 0 || !stopOnErrors) {
            if (opts.isVerbose()) {
                logger.debug("Compiling "+pu.getUnitFile().getPath()+" to JS");
            }
            //Perform capture analysis
            for (com.redhat.ceylon.compiler.typechecker.model.Declaration d : pu.getDeclarations()) {
                if (d instanceof TypedDeclaration && d instanceof com.redhat.ceylon.compiler.typechecker.model.Setter == false) {
                    pu.getCompilationUnit().visit(new ValueVisitor((TypedDeclaration)d));
                }
            }
            JsOutput jsout = getOutput(pu);
            GenerateJsVisitor jsv = new GenerateJsVisitor(jsout, opts, names, pu.getTokens());
            pu.getCompilationUnit().visit(jsv);
            pu.getCompilationUnit().visit(unitVisitor);
            if (jsv.getExitCode() != 0) {
                exitCode = jsv.getExitCode();
            }
        }
    }

    /** Indicates if compilation should stop, based on whether there were errors
     * in the last compilation unit and the stopOnErrors flag is set. */
    protected boolean stopOnError() {
        for (Message err : unitErrors) {
            if (err instanceof AnalysisError ||
                err instanceof UnexpectedError) {
                errCount++;
            }
            errors.add(err);
        }
        return stopOnErrors && errCount > 0;
    }

    /** Compile all the phased units in the typechecker.
     * @return true is compilation was successful (0 errors/warnings), false otherwise. */
    public boolean generate() throws IOException {
        errors.clear();
        errCount = 0;
        output.clear();
        try {
            if (opts.isVerbose()) {
                logger.debug("Generating metamodel...");
            }
            List<PhasedUnit> phasedUnits = tc.getPhasedUnits().getPhasedUnits();
            boolean generatedCode = false;
            
            checkInvalidNativePUs(phasedUnits);
            
            //First generate the metamodel
            final Module defmod = tc.getContext().getModules().getDefaultModule();
            for (PhasedUnit pu: phasedUnits) {
                File path = getFullPath(pu);
                //#416 default module with packages
                Module mod = pu.getPackage().getModule();
                if (mod.getVersion() == null && !mod.isDefault()) {
                    //Switch with the default module
                    for (com.redhat.ceylon.compiler.typechecker.model.Package pkg : mod.getPackages()) {
                        defmod.getPackages().add(pkg);
                        pkg.setModule(defmod);
                    }
                }
                if (srcFiles == null || FileUtil.containsFile(srcFiles, path)) {
                    if (opts.getSuppressWarnings() != null) {
                        pu.getCompilationUnit().visit(
                                new WarningSuppressionVisitor<Warning>(Warning.class, opts.getSuppressWarnings()));
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
            }
            //Then write it out and output the reference in the module file
            names = new JsIdentifierNames();
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
                    if (scope instanceof Package && !((Package)scope).getModule().equals(_m)) {
                        output.get(_m).require(((Package) scope).getModule(), names);
                    }
                }
            };

            if (srcFiles == null && !phasedUnits.isEmpty()) {
                for (PhasedUnit pu: phasedUnits) {
                    pu.getCompilationUnit().visit(importVisitor);
                }
            } else if(!phasedUnits.isEmpty() && !srcFiles.isEmpty()){
                final List<PhasedUnit> units = tc.getPhasedUnits().getPhasedUnits();
                for (File path : srcFiles) {
                    if (!path.getPath().endsWith(ArtifactContext.JS)) {
                        for (PhasedUnit pu : units) {
                            File unitFile = getFullPath(pu);
                            if (FileUtil.sameFile(path, unitFile)) {
                                pu.getCompilationUnit().visit(importVisitor);
                            }
                        }
                    }
                }
            }

//            if (srcFiles == null && !phasedUnits.isEmpty()) {
//                MissingNativeVisitor mnv = new MissingNativeVisitor(Backend.JavaScript);
//                for (PhasedUnit pu: phasedUnits) {
//                    pu.getCompilationUnit().visit(mnv);
//                }
//            } else if(!phasedUnits.isEmpty() && !srcFiles.isEmpty()){
//                MissingNativeVisitor mnv = new MissingNativeVisitor(Backend.JavaScript);
//                final List<PhasedUnit> units = tc.getPhasedUnits().getPhasedUnits();
//                for (File path : srcFiles) {
//                    if (!path.getPath().endsWith(ArtifactContext.JS)) {
//                        for (PhasedUnit pu : units) {
//                            File unitFile = getFullPath(pu);
//                            if (FileUtil.sameFile(path, unitFile)) {
//                                pu.getCompilationUnit().visit(mnv);
//                            }
//                        }
//                    }
//                }
//            }

            //Then generate the JS code
            if (srcFiles == null && !phasedUnits.isEmpty()) {
                for (PhasedUnit pu: phasedUnits) {
                    compileUnit(pu, names);
                    generatedCode = true;
                    if (exitCode != 0) {
                        return false;
                    }
                    if (stopOnError()) {
                        logger.error("Errors found. Compilation stopped.");
                        break;
                    }
                    getOutput(pu).addSource(getFullPath(pu));
                }
            } else if(!phasedUnits.isEmpty() && !srcFiles.isEmpty()){
                final List<PhasedUnit> units = tc.getPhasedUnits().getPhasedUnits();
                PhasedUnit lastUnit = units.get(0);
                for (File path : srcFiles) {
                    if (path.getPath().endsWith(ArtifactContext.JS)) {
                        //Just output the file
                        final JsOutput lastOut = getOutput(lastUnit);
                        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
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
                        for (PhasedUnit pu : units) {
                            File unitFile = getFullPath(pu);
                            if (FileUtil.sameFile(path, unitFile)) {
                                compileUnit(pu, names);
                                generatedCode = true;
                                if (exitCode != 0) {
                                    return false;
                                }
                                if (stopOnError()) {
                                    logger.error("Errors found. Compilation stopped.");
                                    return false;
                                }
                                getOutput(pu).addSource(unitFile);
                                lastUnit = pu;
                            }
                        }
                    }
                }
            }
            if(!generatedCode){
                logger.error("No source units found to compile");
                exitCode = 2;
            }
        } finally {
            if (exitCode==0) {
                finish();
            }
        }
        return errCount == 0 && exitCode == 0;
    }

    public File getFullPath(PhasedUnit pu) {
        return new File(pu.getUnit().getFullPath());
    }

    private void checkInvalidNativePUs(List<PhasedUnit> phasedUnits) {
        for (PhasedUnit pu : phasedUnits) {
            ModuleDescriptor md = pu.findModuleDescriptor();
            if (md != null && !isForBackend(md.getAnnotationList(), Backend.JavaScript, md.getUnit())) {
                md.addError("Module not meant for this backend: " + formatPath(md.getImportPath().getIdentifiers()));
            }
        }
    }
    
    /** Creates a JsOutput if needed, for the PhasedUnit.
     * Right now it's one file per module. */
    private JsOutput getOutput(PhasedUnit pu) throws IOException {
        Module mod = pu.getPackage().getModule();
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
        return new JsOutput(m, opts.getEncoding());
    }

    JsOutput getOutputForModule(Module m) {
        return output.get(m);
    }
    JsIdentifierNames getNames() {
        return names;
    }

    /** Closes all output writers and puts resulting artifacts in the output repo. */
    protected void finish() throws IOException {
        String outDir = opts.getOutRepo();
        if(!isURL(outDir)){
            File root = new File(outDir);
            if (root.exists()) {
                if (!(root.isDirectory() && root.canWrite())) {
                    logger.error("Cannot write to "+root+". Stop.");
                    exitCode = 1;
                }
            } else {
                if (!root.mkdirs()) {
                    logger.error("Cannot create "+root+". Stop.");
                    exitCode = 1;
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
            if (entry.getKey().isDefault()) {
                logger.info("Created module "+moduleName);
            } else if (!compilingLanguageModule) {
                logger.info("Created module "+moduleName+"/"+moduleVersion);
            }
            if (compilingLanguageModule) {
                ArtifactContext artifact = new ArtifactContext("delete", "me", ArtifactContext.JS);
                artifact.setForceOperation(true);
                outRepo.putArtifact(artifact, jsart);
            } else {
                final ArtifactContext artifact = new ArtifactContext(moduleName, moduleVersion, ArtifactContext.JS);
                artifact.setForceOperation(true);
                outRepo.putArtifact(artifact, jsart);
                final ArtifactContext martifact = new ArtifactContext(moduleName, moduleVersion, ArtifactContext.JS_MODEL);
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
            }
            FileUtil.deleteQuietly(jsart);
            if (modart!=null) FileUtil.deleteQuietly(modart);
        }
    }

    private Collection<File> filterForModule(List<File> files, List<File> roots, String moduleName) {
        ArrayList<File> result = new ArrayList<File>(files.size());
        for (File f : files) {
            String rel = FileUtil.relativeFile(roots, f.getPath());
            if (rel.startsWith(moduleName + "/") || rel.startsWith(moduleName + "\\")
                    || ("default".equals(moduleName) && !(rel.contains("/") || rel.contains("\\")))) {
                result.add(f);
            }
        }
        return result;
    }

    /** Print all the errors found during compilation to the specified stream. 
     * @throws IOException */
    public int printErrors(Writer out) throws IOException {
        int count = 0;
        DiagnosticListener diagnosticListener = opts.getDiagnosticListener();
        for (Message err: errors) {
            final boolean suppress = err instanceof UsageWarning && ((UsageWarning)err).isSuppressed();
            if (suppress) {
                continue;
            }
            if (err instanceof UsageWarning) {
                out.write("warning");
            } else {
                out.write("error");
            }
            out.write(String.format(" encountered [%s]", err.getMessage()));
            if (err instanceof AnalysisMessage) {
                Node n = ((AnalysisMessage)err).getTreeNode();
                if(n != null)
                    n = com.redhat.ceylon.compiler.typechecker.tree.Util.getIdentifyingNode(n);
                out.write(String.format(" at %s of %s", n.getLocation(), n.getUnit().getFilename()));
            } else if (err instanceof RecognitionError) {
                RecognitionError rer = (RecognitionError)err;
                out.write(String.format(" at %d:%d", rer.getLine(), rer.getCharacterInLine()));
            }
            out.write(System.lineSeparator());
            count++;
            
            if(diagnosticListener != null){
                boolean warning = err instanceof UsageWarning;
                int position = -1;
                File file = null;
                if(err instanceof AnalysisMessage){
                    Node node = ((AnalysisMessage) err).getTreeNode();
                    if(node != null)
                        node = com.redhat.ceylon.compiler.typechecker.tree.Util.getIdentifyingNode(node);
                    if(node != null && node.getToken() != null)
                        position = node.getToken().getCharPositionInLine();
                    if(node.getUnit() != null && node.getUnit().getFullPath() != null)
                        file = new File(node.getUnit().getFullPath()).getAbsoluteFile();
                }else if(err instanceof RecognitionError){
                    // FIXME: file??
                    position = ((RecognitionError) err).getCharacterInLine();
                }
                if(position != -1)
                    position++; // make it 1-based
                if(warning)
                    diagnosticListener.warning(file, err.getLine(), position, err.getMessage());
                else
                    diagnosticListener.error(file, err.getLine(), position, err.getMessage());
            }
        }
        out.flush();
        return count;
    }

    /** Returns the list of errors found during compilation. */
    public Set<Message> getErrors() {
        return Collections.unmodifiableSet(errors);
    }

    public void printErrorsAndCount(Writer out) throws IOException {
        int count = printErrors(out);
        out.write(String.format("%d %s.%n", count, count==1?"error":"errors"));
    }

    /** Writes the beginning of the wrapper function for a JS module. */
    public static void beginWrapper(Writer writer) throws IOException {
        writer.write("(function(define) { define(function(require, ex$, module) {\n");
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
    public static boolean isCompilingLanguageModule() {
        return compilingLanguageModule;
    }

    /** Create a path for a require call to fetch the specified module. */
    public static String scriptPath(Module mod) {
        StringBuilder path = new StringBuilder(mod.getNameAsString().replace('.', '/')).append('/');
        if (!mod.isDefault()) {
            path.append(mod.getVersion()).append('/');
        }
        path.append(mod.getNameAsString());
        if (!mod.isDefault()) {
            path.append('-').append(mod.getVersion());
        }
        return path.toString();
    }

}
