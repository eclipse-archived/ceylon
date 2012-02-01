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
    
    private final TypeChecker tc;
    
    private boolean optimize = false;
    private Writer systemOut = new OutputStreamWriter(System.out);
    
    private List<UnexpectedError> errors = new ArrayList<UnexpectedError>();
    
    public JsCompiler(TypeChecker tc) {
        this.tc = tc;
    }

    public JsCompiler optimize(boolean optimize) {
        this.optimize = optimize;
        return this;
    }
    
    public List<UnexpectedError> listErrors() {
        return Collections.unmodifiableList(errors);
    }
    
    public void generate() {
        errors.clear();
        try {
            for (PhasedUnit pu: tc.getPhasedUnits().getPhasedUnits()) {
                pu.getCompilationUnit().visit(new GenerateJsVisitor(getWriter(pu),optimize));
                pu.getCompilationUnit().visit(new Visitor() {
                    @Override
                    public void visitAny(Node that) {
                        for (Message err: that.getErrors()) {
                            if (err instanceof UnexpectedError) {
                                errors.add((UnexpectedError)err);
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
                });
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
            systemOut.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
