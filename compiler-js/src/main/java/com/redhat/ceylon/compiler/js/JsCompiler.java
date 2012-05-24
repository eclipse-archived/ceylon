package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.Options;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.tree.AnalysisMessage;
import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisWarning;
import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisError;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.parser.RecognitionError;
import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class JsCompiler {
    
    protected final TypeChecker tc;
    protected final Options opts;

    private boolean stopOnErrors = true;
    private int errCount = 0;
    protected File root;

    protected List<Message> errors = new ArrayList<Message>();
    protected List<Message> unitErrors = new ArrayList<Message>();
    private final Map<Package, Writer> output = new HashMap<Package, Writer>();

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
        boolean modDone = false;
        errors.clear();
        try {
            JsIdentifierNames names = new JsIdentifierNames(opts.isOptimize());
            for (PhasedUnit pu: tc.getPhasedUnits().getPhasedUnits()) {
                String name = pu.getUnitFile().getName();
                if (!"module.ceylon".equals(name) && !"package.ceylon".equals(name)) {
                    if (opts.isModulify() && !modDone) {
                        beginWrapper(getWriter(pu));
                        modDone = true;
                    }
                    compileUnit(pu, names);
                    if (stopOnError()) {
                        System.err.println("Errors found. Compilation stopped.");
                        break;
                    }
                }
            }
            if (opts.isModulify()) {
                endWrapper(getWriter(tc.getPhasedUnits().getPhasedUnits().get(tc.getPhasedUnits().getPhasedUnits().size()-1)));
            }
        } finally {
            finish();
        }
        return errCount == 0;
    }

    protected Writer getWriter(PhasedUnit pu) throws IOException {
        Package pkg = pu.getPackage();
        Writer writer = output.get(pkg);
        if (writer==null) {
            String pkgName = pkg.getNameAsString();
            if (pkgName.isEmpty()) pkgName = "default";
            String path = String.format("%s%s/%s.js",
                pkg.getModule().getNameAsString().replace('.', '/'),
                (pkg.getModule().isDefault() ? "" : "/" + pkg.getModule().getVersion() ),
                pkgName);
            File file = new File(root, path);
            file.getParentFile().mkdirs();
            if (file.exists()) file.delete();
            file.createNewFile();
            writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            beginWrapper(writer);
            output.put(pkg, writer);
        }
        return writer;
    }
    
    protected void finish() throws IOException {
        for (Writer writer: output.values()) {
            endWrapper(writer);
            writer.flush();
            writer.close();
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
