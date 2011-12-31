package com.redhat.ceylon.compiler.js;

import java.io.OutputStreamWriter;
import java.io.Writer;

import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;

public class JsCompiler {
    
    private final TypeChecker tc;
    
    public JsCompiler(TypeChecker tc) {
        this.tc = tc;
    }

    public void generate() {
        for (PhasedUnit pu: tc.getPhasedUnits().getPhasedUnits()) {
            pu.getCompilationUnit().visit(new GenerateJsVisitor(getWriter(pu)));
        }        
    }
    
    protected Writer getWriter(PhasedUnit pu) {
        return new OutputStreamWriter(System.out);
    }
}
