package com.redhat.ceylon.compiler.js;

import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.formatPath;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.getNativeBackend;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.isForBackend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
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
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.compiler.typechecker.parser.RecognitionError;
import com.redhat.ceylon.compiler.typechecker.tree.AnalysisMessage;
import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ImportModule;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ModuleDescriptor;
import com.redhat.ceylon.compiler.typechecker.tree.TreeUtil;
import com.redhat.ceylon.compiler.typechecker.tree.UnexpectedError;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.typechecker.util.WarningSuppressionVisitor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Import;
import com.redhat.ceylon.model.typechecker.model.ImportableScope;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.ModuleImport;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Unit;

public class JsCompiler {

    protected final TypeChecker tc;
    protected final Options opts;
    protected final RepositoryManager outRepo;

    private boolean stopOnErrors = true;
    private int errCount = 0;

    private List<PositionedMessage> analErrors = new ArrayList<PositionedMessage>();
    private List<PositionedMessage> recogErrors = new ArrayList<PositionedMessage>();
    
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

    public static class PositionedMessage {
        public Node node;
        public Message message;
        
        PositionedMessage(Node that, RecognitionError err) {
            node = that;
            message = err;
        }
        
        PositionedMessage(AnalysisMessage msg) {
            node = msg.getTreeNode();
            message = msg;
        }
    }
    
    private final Visitor errorVisitor = new Visitor() {
        private void addErrors(Node that) {
            for (Message m: that.getErrors()) {
                if (m instanceof AnalysisMessage) {
                    analErrors.add(new PositionedMessage((AnalysisMessage)m));
                } else {
                    recogErrors.add(new PositionedMessage(that, (RecognitionError)m));
                }
            }
        }
        @Override
        public void visitAny(Node that) {
            super.visitAny(that);
            addErrors(that);
        }
        @Override
        public void visit(Tree.Declaration that) {
            if (isForBackend(that.getAnnotationList(), Backend.JavaScript, that.getUnit())
                    || isForBackend(that.getAnnotationList(), Backend.None, that.getUnit())) {
                super.visit(that);
            }
        }
        @Override
        public void visit(Tree.ModuleDescriptor that) {
            if (isForBackend(that.getAnnotationList(), Backend.JavaScript, that.getUnit())
                    || isForBackend(that.getAnnotationList(), Backend.None, that.getUnit())) {
                super.visit(that);
            } else {
                addErrors(that);
            }
        }
        @Override
        public void visit(Tree.ImportModule that) {
            if (isForBackend(that.getAnnotationList(), Backend.JavaScript, that.getUnit())
                    || isForBackend(that.getAnnotationList(), Backend.None, that.getUnit())) {
                super.visit(that);
            }
        }
    };
    
    private final Visitor unitVisitor = new Visitor() {

        private boolean hasErrors(Node that) {
            boolean r=false;
            for (Message m: that.getErrors()) {
                r |= m instanceof AnalysisError;
            }
            return r;
        }
        @Override
        public void visitAny(Node that) {
            super.visitAny(that);
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
            Import importModel = that.getImportModel();
            if (that.getImportModel() == null) {
                return;
            }
            Declaration importedDeclaration = nativeImplToJavascript(importModel.getDeclaration());
            if (importedDeclaration == null) {
                return;
            }
            
            Unit importedDeclarationUnit = importedDeclaration.getUnit();
            if (importedDeclarationUnit != null && nonCeylonUnit(importedDeclarationUnit)) {
                if (!providedByAJavaNativeModuleImport(that.getUnit(), importedDeclarationUnit)) {
                    that.addUnsupportedError("cannot import Java declarations in Javascript", Backend.JavaScript);
                }
            }
            super.visit(that);
        }
        @Override
        public void visit(Tree.ImportModule that) {
            if (hasErrors(that)) {
                exitCode = 1;
            } else {
                boolean isJavaModule = false;
                String importNativeBackend = null;
                String moduleNativeBackend = null;
                if (that.getImportPath() != null && that.getImportPath().getModel() instanceof Module) {
                    Module importedModule = (Module) that.getImportPath().getModel();
                    if (importedModule.isJava()) {
                        isJavaModule = true;
                        moduleNativeBackend = importedModule.getNativeBackend();
                    }
                }
                if (that.getQuotedLiteral() != null) {
                    isJavaModule = true;
                }
                if (isJavaModule) {
                    importNativeBackend = TreeUtil.getNativeBackend(that.getAnnotationList(), that.getUnit());
                    if (!Backend.Java.nativeAnnotation.equals(importNativeBackend)
                            && !Backend.Java.nativeAnnotation.equals(moduleNativeBackend)) {
                        that.getImportPath().addUnsupportedError("cannot import Java modules in Javascript", Backend.JavaScript);
                    }
                } else {
                    super.visit(that);
                }
            }
        }
        
        private Declaration nativeImplToJavascript(Declaration declaration) {
            if (declaration == null) {
                return null;
            }
            
            if (declaration.isNative()) {
                Declaration header = ModelUtil.getNativeHeader(declaration);
                if (header != null) {
                    Declaration javaScriptDecl = ModelUtil.getNativeDeclaration(header, Backend.JavaScript);
                    if (javaScriptDecl != null) {
                        declaration = javaScriptDecl;
                    }
                }
            }
            return declaration;
        }
        
        @Override
        public void visit(Tree.MemberOrTypeExpression that) {
            if (hasErrors(that)) return;
            Declaration declaration = nativeImplToJavascript(that.getDeclaration());
            Unit declarationUnit = null;
            if (declaration != null) {
                declarationUnit = declaration.getUnit();
            }
            
            if (declarationUnit != null && nonCeylonUnit(declarationUnit)) {
                if (!providedByAJavaNativeModuleImport(that.getUnit(), declarationUnit)) {
                    that.addUnsupportedError("cannot call Java declarations in Javascript", Backend.JavaScript);
                    return;
                }
            }
            super.visit(that);
        }
        
        protected boolean providedByAJavaNativeModuleImport(
                Unit currentUnit, Unit declarationUnit) {
            boolean isFromAJavaNativeModuleImport;
            Module declarationModule = declarationUnit.getPackage().getModule();
            String moduleNativeBackend = declarationModule.getNativeBackend();
            String importNativeBackend = null;
            for(ModuleImport moduleImport : currentUnit.getPackage().getModule().getImports()) {
                if (declarationModule.equals(moduleImport.getModule())) {
                    importNativeBackend = moduleImport.getNativeBackend();
                    break;
                }
            }
            isFromAJavaNativeModuleImport = Backend.Java.nativeAnnotation.equals(importNativeBackend)
                    || Backend.Java.nativeAnnotation.equals(moduleNativeBackend);
            return isFromAJavaNativeModuleImport;
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

    /** Compile one phased unit. */
    private void compileUnit(PhasedUnit pu, JsIdentifierNames names) throws IOException {
        pu.getCompilationUnit().visit(unitVisitor);
        if (exitCode != 0) {
            pu.getCompilationUnit().visit(errorVisitor);
            return;
        }
        if (errCount == 0 || !stopOnErrors) {
            if (opts.isVerbose()) {
                logger.debug("Compiling "+pu.getUnitFile().getPath()+" to JS");
            }
            JsOutput jsout = getOutput(pu);
            GenerateJsVisitor jsv = new GenerateJsVisitor(jsout, opts, names, pu.getTokens());
            pu.getCompilationUnit().visit(jsv);
            pu.getCompilationUnit().visit(unitVisitor);
            pu.getCompilationUnit().visit(errorVisitor);
            if (jsv.getExitCode() != 0) {
                exitCode = jsv.getExitCode();
            }
        }
    }

    /** Indicates if compilation should stop, based on whether there were errors
     * in the last compilation unit and the stopOnErrors flag is set. */
    protected boolean stopOnError() {
        for (PositionedMessage pm : analErrors) {
            if (pm.message instanceof AnalysisError ||
                pm.message instanceof UnexpectedError) {
                errCount++;
            }
        }
        return stopOnErrors && errCount > 0;
    }

    /** Compile all the phased units in the typechecker.
     * @return true is compilation was successful (0 errors/warnings), false otherwise. */
    public boolean generate() throws IOException {
        recogErrors.clear();
        analErrors.clear();
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
                    if (FileUtil.containsFile(srcFiles, path)) {
                        phasedUnits.add(pu);
                    }
                }
            }
            boolean generatedCode = false;
            
            checkInvalidNativePUs(phasedUnits);
            
            //First generate the metamodel
            final Module defmod = tc.getContext().getModules().getDefaultModule();
            for (PhasedUnit pu: phasedUnits) {
                //#416 default module with packages
                Module mod = pu.getPackage().getModule();
                if (mod.getVersion() == null && !mod.isDefault()) {
                    //Switch with the default module
                    for (com.redhat.ceylon.model.typechecker.model.Package pkg : mod.getPackages()) {
                        defmod.getPackages().add(pkg);
                        pkg.setModule(defmod);
                    }
                }
                if (opts.getSuppressWarnings() != null) {
                    pu.getCompilationUnit().visit(
                            new WarningSuppressionVisitor<Warning>(Warning.class, opts.getSuppressWarnings()));
                }
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
            names = new JsIdentifierNames();
            if (!compilingLanguageModule) {
                for (Map.Entry<Module,JsOutput> e : output.entrySet()) {
                    e.getValue().encodeModel(names);
                }
            }
            
            checkInvalidNativeImports(phasedUnits);
            
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

            for (PhasedUnit pu: phasedUnits) {
                pu.getCompilationUnit().visit(importVisitor);
            }

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
            } else if(srcFiles != null && !srcFiles.isEmpty()
                         // For the specific case of the Stitcher
                         && !typecheckerPhasedUnits.isEmpty() ){
                PhasedUnit lastUnit;
                if (phasedUnits.isEmpty()) {
                    // For the specific case of the Stitcher
                    lastUnit = typecheckerPhasedUnits.get(0);
                } else {
                    lastUnit = phasedUnits.get(0);
                }
                
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
                        for (PhasedUnit pu : phasedUnits) {
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
            if (md != null) {
                String be = getNativeBackend(md.getAnnotationList(), md.getUnit());
                if (be != null) {
                    if (be.isEmpty()) {
                        md.addError("missing backend argument for native annotation on module: " + formatPath(md.getImportPath().getIdentifiers()), Backend.JavaScript);
                    } else if (!isForBackend(be, Backend.JavaScript)) {
                        md.addError("module not meant for this backend: " + formatPath(md.getImportPath().getIdentifiers()), Backend.JavaScript);
                    }
                }
            }
        }
    }
    
    private void checkInvalidNativeImports(List<PhasedUnit> phasedUnits) {
        for (PhasedUnit pu : phasedUnits) {
            ModuleDescriptor md = pu.findModuleDescriptor();
            if (md != null) {
                for (ImportModule im : md.getImportModuleList().getImportModules()) {
                    String be = getNativeBackend(im.getAnnotationList(), im.getUnit());
                    if (im.getImportPath() != null) {
                        Module m = (Module)im.getImportPath().getModel();
                        if (be != null && m.isNative() && !be.equals(m.getNativeBackend())) {
                            im.addError("native backend name conflicts with imported module: '\"" + 
                                    be + "\"' is not '\"" + m.getNativeBackend() + "\"'", Backend.JavaScript);
                        }
                    }
                }
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

    public int printErrorsAndCount(Writer out) throws IOException {
        return printErrors(out, true);
    }

    /** Print all the errors found during compilation to the specified stream. 
     * @throws IOException */
    public int printErrors(Writer out) throws IOException {
        return printErrors(out, false);
    }
    
    private int printErrors(Writer out, boolean printCount) throws IOException {
        int warnings = 0;
        int count = 0;
        DiagnosticListener diagnosticListener = opts.getDiagnosticListener();
        List<PositionedMessage> errors = (!recogErrors.isEmpty()) ? recogErrors : analErrors;
        for (PositionedMessage pm : errors) {
            Message err = pm.message;
            final boolean suppress = err instanceof UsageWarning && ((UsageWarning)err).isSuppressed();
            if (suppress) {
                continue;
            }
            Node node = TreeUtil.getIdentifyingNode(pm.node);
            int line = err.getLine();
            int position = -1;
            if(err instanceof AnalysisMessage){
                if(node != null && node.getToken() != null)
                    position = node.getToken().getCharPositionInLine();
            }else if(err instanceof RecognitionError){
                position = ((RecognitionError) err).getCharacterInLine();
            }
            String fileName = (node.getUnit() != null) ? node.getUnit().getFullPath() : "unknown";
            out.write(fileName);
            out.write(":");
            out.write(String.format("%d", line));
            out.write(": ");
            if (err instanceof UsageWarning) {
                out.write("warning");
                warnings++;
            } else {
                out.write("error");
                count++;
            }
            out.write(": ");
            out.write(err.getMessage());
            out.write(System.lineSeparator());
            String ln = getErrorSourceLine(pm);
            if (ln != null) {
                out.write(ln);
                out.write(System.lineSeparator());
                out.write(getErrorMarkerLine(position));
                out.write(System.lineSeparator());
            }
            
            if(diagnosticListener != null){
                File file = null;
                boolean warning = err instanceof UsageWarning;
                if(node.getUnit() != null && node.getUnit().getFullPath() != null)
                    file = new File(node.getUnit().getFullPath()).getAbsoluteFile();
                if(position != -1)
                    position++; // make it 1-based
                if(warning)
                    diagnosticListener.warning(file, line, position, err.getMessage());
                else
                    diagnosticListener.error(file, line, position, err.getMessage());
            }
        }
        if (printCount) {
            if (count > 0)
                out.write(String.format("%d %s%n", count, count==1?"error":"errors"));
            if (warnings > 0)
                out.write(String.format("%d %s%n", warnings, warnings==1?"warning":"warnings"));
        }
        out.flush();
        return count;
    }

    private String getErrorSourceLine(PositionedMessage pm) {
        if (pm.node.getUnit() != null) {
            int lineNr = pm.message.getLine();
            File file = new File(pm.node.getUnit().getFullPath());
            VirtualFile vfile = tc.getContext().getVfs().getFromFile(file);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(vfile.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (--lineNr <= 0) {
                        return line;
                    }
                }
            } catch (IOException e) {
                // Ignore
            }
        }
        return null;
    }

    private String getErrorMarkerLine(int position) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < position; i++) {
            str.append(" ");
        }
        str.append("^");
        return str.toString();
    }

    /** Returns the list of errors found during compilation. */
    public Set<Message> getErrors() {
        Set<Message> result = new LinkedHashSet<Message>();
        if (!recogErrors.isEmpty()) {
            for (PositionedMessage pm : recogErrors) {
                result.add(pm.message);
            }
        } else {
            for (PositionedMessage pm : analErrors) {
                result.add(pm.message);
            }
        }
        return result;
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
