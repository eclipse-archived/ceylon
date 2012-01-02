package com.redhat.ceylon.compiler.js;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.UnexpectedError;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class JsCompiler {
    
    private final OutputStreamWriter systemOut = new OutputStreamWriter(System.out);
    private final TypeChecker tc;
    private final boolean optimize;
    
    public JsCompiler(TypeChecker tc, boolean optimize) {
        this.tc = tc;
        this.optimize = optimize;
    }

    public void generate() {
        for (PhasedUnit pu: tc.getPhasedUnits().getPhasedUnits()) {
            pu.getCompilationUnit().visit(new GenerateJsVisitor(getWriter(pu),optimize));
            pu.getCompilationUnit().visit(new Visitor() {
                @Override
                public void visitAny(Node that) {
                    for (Message err: that.getErrors()) {
                        if (err instanceof UnexpectedError) {
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
        finish();
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
