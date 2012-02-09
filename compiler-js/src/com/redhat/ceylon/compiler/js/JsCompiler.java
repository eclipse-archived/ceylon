package com.redhat.ceylon.compiler.js;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.tree.AnalysisMessage;
import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class JsCompiler {
    
    protected final TypeChecker tc;
    
    private boolean optimize = false;
    private boolean stopOnErrors = true;
    private Writer systemOut = new OutputStreamWriter(System.out);

    protected List<AnalysisMessage> errors = new ArrayList<AnalysisMessage>();
    protected List<AnalysisMessage> unitErrors = new ArrayList<AnalysisMessage>();
    
    private final Visitor unitVisitor = new Visitor() {
        @Override
        public void visitAny(Node that) {
            for (Message err: that.getErrors()) {
                if (err instanceof AnalysisMessage) {
                    unitErrors.add((AnalysisMessage)err);
                }
            }
            super.visitAny(that);
        }
    };

    public JsCompiler(TypeChecker tc) {
        this.tc = tc;
    }

    /** Specifies whether the compiler should stop when errors are found in a compilation unit (default true). */
    public JsCompiler stopOnErrors(boolean flag) {
        stopOnErrors = flag;
        return this;
    }

    public JsCompiler optimize(boolean optimize) {
        this.optimize = optimize;
        return this;
    }
    
    public List<AnalysisMessage> listErrors() {
        return Collections.unmodifiableList(errors);
    }

    /** Compile one phased unit.
     * @return The errors found for the unit. */
    public List<AnalysisMessage> compileUnit(PhasedUnit pu) {
        unitErrors.clear();
        pu.getCompilationUnit().visit(unitVisitor);
        if (unitErrors.isEmpty() || !stopOnErrors) {
            pu.getCompilationUnit().visit(new GenerateJsVisitor(getWriter(pu),optimize));
        }
        return unitErrors;
    }

    /** Indicates if compilation should stop, based on whether there were errors
     * in the last compilation unit and the stopOnErrors flag is set. */
    protected boolean stopOnError() {
        if (!unitErrors.isEmpty()) {
            errors.addAll(unitErrors);
            return stopOnErrors;
        }
        return false;
    }

    /** Compile all the phased units in the typechecker.
     * @return true is compilation was successful (0 errors/warnings), false otherwise. */
    public boolean generate() {
        errors.clear();
        try {
            for (PhasedUnit pu: tc.getPhasedUnits().getPhasedUnits()) {
                compileUnit(pu);
                if (stopOnError()) {
                    System.err.println("Errors found. Compilation stopped.");
                    break;
                }
            }
        } finally {
            finish();
        }
        return errors.isEmpty();
    }

    protected Writer getWriter(PhasedUnit pu) {
        return systemOut;
    }
    
    protected void finish() {
        try {
            systemOut.flush();
            //systemOut.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /** Print all the errors found during compilation to the specified stream. */
    public void printErrors(PrintStream out) {
        int count = 0;
        for (AnalysisMessage err: errors) {
            Node n = err.getTreeNode();
            out.printf("error encountered [%s] at %s of %s%n",
                err.getMessage(), n.getLocation(), n.getUnit().getFilename());
            count++;
        }
        out.printf("%d errors.%n", count);
    }

}
