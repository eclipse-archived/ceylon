package com.redhat.ceylon.compiler.js;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.SourceArchiveCreator;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.impl.ShaSigner;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.log.Logger;
import com.redhat.ceylon.compiler.Options;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisError;
import com.redhat.ceylon.compiler.typechecker.analyzer.UsageWarning;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.parser.RecognitionError;
import com.redhat.ceylon.compiler.typechecker.tree.AnalysisMessage;
import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.UnexpectedError;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

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
    private TypeUtils types;
    private int exitCode = 0;
    private Logger logger;

    public int getExitCode() {
        return exitCode;
    }

    private final Visitor unitVisitor = new Visitor() {
        private boolean hasErrors(Node that) {
            for (Message m: that.getErrors()) {
                if (m instanceof AnalysisError) {
                    return true;
                }
            }
            return false;
        }
        @Override
        public void visitAny(Node that) {
            super.visitAny(that);
            for (Message err: that.getErrors()) {
                unitErrors.add(err);
            }
        }
        @Override
        public void visit(Tree.ImportMemberOrType that) {
            if (hasErrors(that)) return;
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
        if(logger == null)
            logger = new JsLogger(opts);
        String outDir = options.getOutRepo();
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
        types = new TypeUtils(tc.getContext().getModules().getLanguageModule());
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
    public void setResourceFiles(List<File> resFiles) {
        this.resFiles = resFiles;
    }

    public Set<Message> listErrors() {
        return getErrors();
    }

    /** Compile one phased unit.
     * @return The errors found for the unit. */
    public void compileUnit(PhasedUnit pu, JsIdentifierNames names) throws IOException {
        unitErrors.clear();
        pu.getCompilationUnit().visit(unitVisitor);
        if (exitCode != 0)return;
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
            GenerateJsVisitor jsv = new GenerateJsVisitor(jsout, opts, names, pu.getTokens(), types);
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
            
            //First generate the metamodel
            for (PhasedUnit pu: phasedUnits) {
                File path = new File(pu.getUnitFile().getPath());
                if (srcFiles == null || FileUtil.containsFile(srcFiles, path)) {
                    pu.getCompilationUnit().visit(getOutput(pu).mmg);
                    if (opts.isVerbose()) {
                        logger.debug(pu.getCompilationUnit().toString());
                    }
                }
            }
            //Then write it out and output the reference in the module file
            if (!compilingLanguageModule) {
                for (Map.Entry<Module,JsOutput> e : output.entrySet()) {
                    e.getValue().encodeModel(e.getKey());
                }
            }

            //Then generate the JS code
            JsIdentifierNames names = new JsIdentifierNames();
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
                    if (path.getPath().endsWith(".js")) {
                        //Just output the file
                        final JsOutput lastOut = getOutput(lastUnit);
                        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                if (!opts.isIndent() || opts.isMinify()) {
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
                            File unitFile = new File(pu.getUnitFile().getPath());
                            if (FileUtil.sameFile(path, unitFile)) {
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

    /** Creates a JsOutput if needed, for the PhasedUnit.
     * Right now it's one file per module. */
    private JsOutput getOutput(PhasedUnit pu) throws IOException {
        Module mod = pu.getPackage().getModule();
        JsOutput jsout = output.get(mod);
        if (jsout==null) {
            jsout = newJsOutput(mod);
            output.put(mod, jsout);
            if (opts.isModulify()) {
                beginWrapper(jsout.getWriter());
            }
        }
        return jsout;
    }

    /** This exists solely so that the web IDE can override it and use a different JsOutput */
    protected JsOutput newJsOutput(Module m) throws IOException {
        return new JsOutput(m, opts.getEncoding());
    }

    /** Closes all output writers and puts resulting artifacts in the output repo. */
    protected void finish() throws IOException {
        for (Map.Entry<Module,JsOutput> entry: output.entrySet()) {
            JsOutput jsout = entry.getValue();

            if (opts.isModulify()) {
                endWrapper(jsout.getWriter());
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
                outRepo.putArtifact(artifact, jsart);
                artifact.setForceOperation(true);
            } else {
                final ArtifactContext artifact = new ArtifactContext(moduleName, moduleVersion, ArtifactContext.JS);
                outRepo.putArtifact(artifact, jsart);
                final ArtifactContext martifact = new ArtifactContext(moduleName, moduleVersion, ArtifactContext.JS_MODEL);
                outRepo.putArtifact(martifact, modart);
                //js file signature
                artifact.setForceOperation(true);
                martifact.setForceOperation(true);
                ArtifactContext sha1Context = artifact.getSha1Context();
                sha1Context.setForceOperation(true);
                File sha1File = ShaSigner.sign(jsart, logger, opts.isVerbose());
                outRepo.putArtifact(sha1Context, sha1File);
                sha1Context = martifact.getSha1Context();
                sha1Context.setForceOperation(true);
                sha1File = ShaSigner.sign(modart, new JsJULLogger(), opts.isVerbose());
                outRepo.putArtifact(sha1Context, sha1File);
                //Create the src archive
                if (opts.isGenerateSourceArchive()) {
                    SourceArchiveCreator sac = CeylonUtils.makeSourceArchiveCreator(outRepo, opts.getSrcDirs(),
                            moduleName, moduleVersion, opts.isVerbose(), logger);
                    sac.copySourceFiles(FileUtil.filesToPathList(jsout.getSources()));
                }
                sha1File.deleteOnExit();
                createResourcesFile(moduleName, moduleVersion);
            }
            jsart.deleteOnExit();
            if (modart!=null)modart.deleteOnExit();
        }
    }

    /** Print all the errors found during compilation to the specified stream. 
     * @throws IOException */
    public int printErrors(Writer out) throws IOException {
        int count = 0;
        DiagnosticListener diagnosticListener = opts.getDiagnosticListener();
        for (Message err: errors) {
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
        out.write(String.format("%d errors.%n", count));
    }

    /** Writes the beginning of the wrapper function for a JS module. */
    public void beginWrapper(Writer writer) throws IOException {
        writer.write("(function(define) { define(function(require, ex$, module) {\n");
    }

    /** Writes the ending of the wrapper function for a JS module. */
    public void endWrapper(Writer writer) throws IOException {
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

    private ZipEntry getZipEntry(final String moduleName, File resource) {
        String entry = FileUtil.relativeFile(opts.getResourceDirs(), resource.getPath()).replace(File.separatorChar, '/');
        String modulePath = ModuleUtil.moduleToPath(moduleName).getPath().replace(File.separatorChar, '/');
        if (moduleName.isEmpty() || "default".equals(moduleName) || entry.startsWith(modulePath + '/')) {
            String rootName = opts.getResourceRootName();
            if (rootName == null) {
                rootName = Constants.DEFAULT_RESOURCE_ROOT;
            }
            String rrp = modulePath + "/" + rootName + "/";
            if (!rrp.isEmpty() && entry.startsWith(rrp)) {
                entry = entry.substring(rrp.length());
            }
            return new ZipEntry(entry);
        } else {
            return null;
        }
    }

    private void createResourcesFile(final String moduleName, final String moduleVersion) throws IOException {
        if (resFiles == null  || resFiles.isEmpty()) {
            return;
        }
        final List<ZipEntry> entries = new ArrayList<>(resFiles.size());
        for (File res : resFiles) {
            final ZipEntry e = getZipEntry(moduleName, res);
            if (e != null) {
                entries.add(e);
            }
        }
        if (entries.isEmpty()) {
            return;
        }
        //Filter the resources for the current module
        final File resfile = File.createTempFile("jsres", "zip");
        try (ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(resfile))) {
            for (int i = 0; i < resFiles.size(); i++) {
                //Add to the stream
                zip.putNextEntry(entries.get(i));
                //Copy the file
                Files.copy(resFiles.get(i).toPath(), zip);
            }
        }
        final ArtifactContext rsartifact = new ArtifactContext(moduleName,
                moduleVersion, ArtifactContext.JS_RESOURCES);
        outRepo.putArtifact(rsartifact, resfile);
        final ArtifactContext sha1Context = rsartifact.getSha1Context();
        sha1Context.setForceOperation(true);
        final File sha1File = ShaSigner.sign(resfile, new JsJULLogger(), opts.isVerbose());
        outRepo.putArtifact(sha1Context, sha1File);
        resfile.deleteOnExit();
    }

}
