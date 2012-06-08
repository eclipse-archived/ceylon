package com.redhat.ceylon.compiler.js;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.impl.JULLogger;
import com.redhat.ceylon.compiler.Options;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.tree.AnalysisMessage;
import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisWarning;
import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisError;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.parser.RecognitionError;
import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class JsCompiler {
    
    protected final TypeChecker tc;
    protected final Options opts;
    protected final RepositoryManager outRepo;

    private boolean stopOnErrors = true;
    private int errCount = 0;
    protected File root;

    protected List<Message> errors = new ArrayList<Message>();
    protected List<Message> unitErrors = new ArrayList<Message>();
    private final Map<Module, Writer> output = new HashMap<Module, Writer>();

    private final Visitor unitVisitor = new Visitor() {
        @Override
        public void visitAny(Node that) {
            for (Message err: that.getErrors()) {
                unitErrors.add(err);
            }
            super.visitAny(that);
        }
    };

    public JsCompiler(TypeChecker tc, Options options) {
        this.tc = tc;
        opts = options;
        outRepo = com.redhat.ceylon.compiler.java.util.Util.makeOutputRepositoryManager(options.getOutDir(), new JULLogger(), options.getUser(), options.getPass());
        root = new File(options.getOutDir());
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

    /** Specifies whether the compiler should stop when errors are found in a compilation unit (default true). */
    public JsCompiler stopOnErrors(boolean flag) {
        stopOnErrors = flag;
        return this;
    }

    public List<Message> listErrors() {
        return Collections.unmodifiableList(errors);
    }

    /** Compile one phased unit.
     * @return The errors found for the unit. */
    public List<Message> compileUnit(PhasedUnit pu, JsIdentifierNames names) throws IOException {
        unitErrors.clear();
        pu.getCompilationUnit().visit(unitVisitor);
        if (errCount == 0 || !stopOnErrors) {
            GenerateJsVisitor jsv = new GenerateJsVisitor(getWriter(pu), opts.isOptimize(), names);
            jsv.setAddComments(opts.isComment());
            jsv.setIndent(opts.isIndent());
            pu.getCompilationUnit().visit(jsv);
            pu.getCompilationUnit().visit(unitVisitor);
        }
        return unitErrors;
    }

    /** Indicates if compilation should stop, based on whether there were errors
     * in the last compilation unit and the stopOnErrors flag is set. */
    protected boolean stopOnError() {
        for (Message err : unitErrors) {
            if (err instanceof AnalysisError || !(err instanceof AnalysisWarning)) {
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
        try {
            JsIdentifierNames names = new JsIdentifierNames(opts.isOptimize());
            for (PhasedUnit pu: tc.getPhasedUnits().getPhasedUnits()) {
                String name = pu.getUnitFile().getName();
                if (!"module.ceylon".equals(name) && !"package.ceylon".equals(name)) {
                    compileUnit(pu, names);
                    if (stopOnError()) {
                        System.err.println("Errors found. Compilation stopped.");
                        break;
                    }
                }
            }
        } finally {
            finish();
        }
        return errCount == 0;
    }

    /** Creates a writer if needed. Right now it's one file per package. */
    protected Writer getWriter(PhasedUnit pu) throws IOException {
        Module mod = pu.getPackage().getModule();
        Writer writer = output.get(mod);
        if (writer==null) {
            writer = new StringWriter();
            output.put(mod, writer);
            if (opts.isModulify()) {
                beginWrapper(writer);
            }
        }
        return writer;
    }

    /** Closes all output writers and puts resulting artifacts in the output repo. */
    protected void finish() throws IOException {
        for (Map.Entry<Module,Writer> entry: output.entrySet()) {
            if (opts.isModulify()) {
                endWrapper(entry.getValue());
            }
            String out = ((StringWriter)entry.getValue()).getBuffer().toString();
            ArtifactContext artifact = new ArtifactContext(entry.getKey().getNameAsString(), entry.getKey().getVersion());
            artifact.setFetchSingleArtifact(true);
            artifact.setSuffix(".js");
            outRepo.putArtifact(artifact, new ByteArrayInputStream(out.getBytes()));
        }
    }

    /** Print all the errors found during compilation to the specified stream. */
    public void printErrors(PrintStream out) {
        int count = 0;
        for (Message err: errors) {
            if (err instanceof AnalysisWarning && !(err instanceof AnalysisError)) {
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

}
