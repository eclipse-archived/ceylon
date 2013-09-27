package com.redhat.ceylon.compiler.js;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minidev.json.JSONObject;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.SourceArchiveCreator;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.impl.JULLogger;
import com.redhat.ceylon.cmr.impl.ShaSigner;
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
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class JsCompiler {

    protected final TypeChecker tc;
    protected final Options opts;
    protected final RepositoryManager outRepo;

    private boolean stopOnErrors = true;
    private int errCount = 0;

    protected Set<Message> errors = new HashSet<Message>();
    protected Set<Message> unitErrors = new HashSet<Message>();
    protected List<String> files;
    private final Map<Module, JsOutput> output = new HashMap<Module, JsOutput>();
    //You have to manually set this when compiling the language module
    static boolean compilingLanguageModule;
    private TypeUtils types;

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
            if ((u.getFilename() != null && !u.getFilename().endsWith(".ceylon"))
                    || u.getPackage().getModule().isJava()) {
                that.addUnexpectedError("cannot import Java declarations in Javascript");
            }
            super.visit(that);
        }
        @Override
        public void visit(Tree.ImportModule that) {
            if (hasErrors(that)) return;
            if (((Module)that.getImportPath().getModel()).isJava()) {
                that.getImportPath().addUnexpectedError("cannot import Java modules in Javascript");
            }
            super.visit(that);
        }
        @Override
        public void visit(Tree.BaseMemberOrTypeExpression that) {
            if (hasErrors(that)) return;
            if (that.getDeclaration() != null && that.getDeclaration().getUnit() != null) {
                Unit u = that.getDeclaration().getUnit();
                if ((u.getFilename() != null && !u.getFilename().endsWith(".ceylon"))
                        || (u.getPackage() != null && u.getPackage().getModule() != null && u.getPackage().getModule().isJava())) {
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
                if ((u.getFilename() != null && !u.getFilename().endsWith(".ceylon"))
                        || (u.getPackage() != null && u.getPackage().getModule() != null && u.getPackage().getModule().isJava())) {
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
                .outRepo(options.getOutDir())
                .user(options.getUser())
                .password(options.getPass())
                .buildOutputManager();
        String outDir = options.getOutDir();
        if(!isURL(outDir)){
        	File root = new File(outDir);
        	if (root.exists()) {
        		if (!(root.isDirectory() && root.canWrite())) {
        			System.err.printf("Cannot write to %s. Stop.%n", root);
        		}
        	} else {
        		if (!root.mkdirs()) {
        			System.err.printf("Cannot create %s. Stop.%n", root);
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
    public void setFiles(List<String> files) {
        this.files = files;
    }

    public Set<Message> listErrors() {
        return getErrors();
    }

    /** Compile one phased unit.
     * @return The errors found for the unit. */
    public Set<Message> compileUnit(PhasedUnit pu, JsIdentifierNames names) throws IOException {
        unitErrors.clear();
        pu.getCompilationUnit().visit(unitVisitor);
        if (errCount == 0 || !stopOnErrors) {
            if (opts.isVerbose()) {
                System.out.printf("%nCompiling %s to JS%n", pu.getUnitFile().getPath());
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
        }
        return unitErrors;
    }

    /** Indicates if compilation should stop, based on whether there were errors
     * in the last compilation unit and the stopOnErrors flag is set. */
    protected boolean stopOnError() {
        errCount = 0;
        for (Message err : unitErrors) {
            if (err instanceof AnalysisError) {
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
        output.clear();
        try {
            if (opts.isVerbose()) {
                System.out.println("Generating metamodel...");
            }
            //First generate the metamodel
            for (PhasedUnit pu: tc.getPhasedUnits().getPhasedUnits()) {
                String pathFromVFS = pu.getUnitFile().getPath();
                // VFS talks in terms of URLs while files are platform-dependent, so make it
                // platform-dependent too
                String path = pathFromVFS.replace('/', File.separatorChar);
                if (files == null || files.contains(path)) {
                    pu.getCompilationUnit().visit(getOutput(pu).mmg);
                    if (opts.isVerbose()) {
                        System.out.println(pu.getCompilationUnit());
                    }
                }
            }
            //Then write it out
            if (!compilingLanguageModule) {
                for (Map.Entry<Module,JsOutput> e : output.entrySet()) {
                    e.getValue().getWriter().write("var $$METAMODEL$$=");
                    e.getValue().getWriter().write(JSONObject.toJSONString(e.getValue().mmg.getModel()));
                    e.getValue().getWriter().write(";\nexports.$$METAMODEL$$=function(){return $$METAMODEL$$;};\n");
                }
            }

            //Then generate the JS code
            JsIdentifierNames names = new JsIdentifierNames();
            if (files == null) {
                for (PhasedUnit pu: tc.getPhasedUnits().getPhasedUnits()) {
                    String pathFromVFS = pu.getUnitFile().getPath();
                    // VFS talks in terms of URLs while files are platform-dependent, so make it
                    // platform-dependent too
                    String path = pathFromVFS.replace('/', File.separatorChar);
                    if (files == null || files.contains(path)) {
                        compileUnit(pu, names);
                        if (stopOnError()) {
                            System.err.println("Errors found. Compilation stopped.");
                            break;
                        }
                        getOutput(pu).addSource(pu.getUnit().getFullPath());
                    } else {
                        if (opts.isVerbose()) {
                            System.err.println("Files does not contain "+path);
                            for (String p : files) {
                                System.err.println(" Files: "+p);
                            }
                        }
                    }
                }
            } else if(!tc.getPhasedUnits().getPhasedUnits().isEmpty()){
                final List<PhasedUnit> units = tc.getPhasedUnits().getPhasedUnits();
                PhasedUnit lastUnit = units.get(0);
                for (String path : files) {
                    if (path.endsWith(".js")) {
                        //Just output the file
                        File f = new File(path);
                        final JsOutput lastOut = getOutput(lastUnit);
                        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                lastOut.getWriter().write(line);
                                lastOut.getWriter().write('\n');
                            }
                        } finally {
                            lastOut.addSource(path);
                        }
                    } else {
                        //Find the corresponding compilation unit
                        for (PhasedUnit pu : units) {
                            String unitPath = pu.getUnitFile().getPath().replace('/', File.separatorChar);
                            if (path.equals(unitPath)) {
                                compileUnit(pu, names);
                                if (stopOnError()) {
                                    System.err.println("Errors found. Compilation stopped.");
                                    break;
                                }
                                getOutput(pu).addSource(pu.getUnit().getFullPath());
                                lastUnit = pu;
                            }
                        }
                    }
                }
            }else{
                System.err.println("No source units found to compile");
            }
        } finally {
            finish();
        }
        return errCount == 0;
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
            //Create the JS file
            File jsart = entry.getValue().close();
            ArtifactContext artifact = new ArtifactContext(moduleName, moduleVersion, ArtifactContext.JS);
            if (entry.getKey().isDefault()) {
                System.err.println("Created module "+moduleName);
            } else {
                System.err.println("Created module "+moduleName+"/"+moduleVersion);
            }
            outRepo.putArtifact(artifact, jsart);
            //js file signature
            artifact.setForceOperation(true);
            ArtifactContext sha1Context = artifact.getSha1Context();
            sha1Context.setForceOperation(true);
            File sha1File = ShaSigner.sign(jsart, new JULLogger(), opts.isVerbose());
            outRepo.putArtifact(sha1Context, sha1File);
            //Create the src archive
            if (opts.isGenerateSourceArchive()) {
                Set<File> sourcePaths = new HashSet<File>();
                for (String sp : opts.getSrcDirs()) {
                    sourcePaths.add(new File(sp));
                }
                SourceArchiveCreator sac = CeylonUtils.makeSourceArchiveCreator(outRepo, sourcePaths,
                        moduleName, moduleVersion, opts.isVerbose(), new JULLogger());
                sac.copySourceFiles(jsout.getSources());
            }
            sha1File.deleteOnExit();
            jsart.deleteOnExit();
        }
    }

    /** Print all the errors found during compilation to the specified stream. */
    public int printErrors(PrintStream out) {
        int count = 0;
        for (Message err: errors) {
            if (err instanceof UsageWarning) {
                out.print("warning");
            } else {
                out.print("error");
            }
            out.printf(" encountered [%s]", err.getMessage());
            if (err instanceof AnalysisMessage) {
                Node n = ((AnalysisMessage)err).getTreeNode();
                out.printf(" at %s of %s", n.getLocation(), n.getUnit().getFilename());
            } else if (err instanceof RecognitionError) {
                RecognitionError rer = (RecognitionError)err;
                out.printf(" at %d:%d", rer.getLine(), rer.getCharacterInLine());
            }
            out.println();
            count++;
        }
        return count;
    }

    /** Returns the list of errors found during compilation. */
    public Set<Message> getErrors() {
        return Collections.unmodifiableSet(errors);
    }

    public void printErrorsAndCount(PrintStream out) {
        int count = printErrors(out);
        out.printf("%d errors.%n", count);
    }

    /** Writes the beginning of the wrapper function for a JS module. */
    public void beginWrapper(Writer writer) throws IOException {
        writer.write("(function(define) { define(function(require, exports, module) {\n");
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

    /** Returns true if the compiler is currently compiling the language module. */
    public static boolean isCompilingLanguageModule() {
        return compilingLanguageModule;
    }
}
