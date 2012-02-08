package com.redhat.ceylon.compiler.js;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.UnexpectedError;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class JsCompiler {
    
    protected final TypeChecker tc;
    
    private boolean optimize = false;
    private boolean stopOnErrors = true;
    private Writer systemOut = new OutputStreamWriter(System.out);

    protected List<UnexpectedError> errors = new ArrayList<UnexpectedError>();
    protected List<UnexpectedError> unitErrors = new ArrayList<UnexpectedError>();
    
    private final Visitor unitVisitor = new Visitor() {
        @Override
        public void visitAny(Node that) {
            for (Message err: that.getErrors()) {
                if (err instanceof UnexpectedError) {
                    unitErrors.add((UnexpectedError)err);
                    Node n = ((UnexpectedError) err).getTreeNode();
                    System.err.println(
                        "error encountered [" +
                        err.getMessage() + "] at " + 
                        n.getLocation() + " of " +
                        n.getUnit().getFilename());
                }
            }
            super.visitAny(that);
        }
    };

    public JsCompiler(TypeChecker tc) {
        this.tc = tc;
    }

    /** Specifies whether the compiler should stop when errors are found in a compilation unit. */
    public JsCompiler stopOnErrors(boolean flag) {
        stopOnErrors = flag;
        return this;
    }

    public JsCompiler optimize(boolean optimize) {
        this.optimize = optimize;
        return this;
    }
    
    public List<UnexpectedError> listErrors() {
        return Collections.unmodifiableList(errors);
    }

    public List<UnexpectedError> compile(PhasedUnit pu) {
        unitErrors.clear();
        pu.getCompilationUnit().visit(new GenerateJsVisitor(getWriter(pu),optimize));
        pu.getCompilationUnit().visit(unitVisitor);
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

    public void generate() {
        errors.clear();
        try {
            for (PhasedUnit pu: tc.getPhasedUnits().getPhasedUnits()) {
                compile(pu);
                if (stopOnError()) {
                    System.err.println("Errors found. Compilation stopped.");
                    break;
                }
            }
        } finally {
            finish();
        }
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
}
