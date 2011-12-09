package com.redhat.ceylon.compiler.codegen;

import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisWarning;
import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.NaturalVisitor;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class ErrorVisitor extends Visitor implements NaturalVisitor {

    public ErrorVisitor() {
        super();
    }

    public boolean hasErrors(Node target) {
        try{
            visitAny(target);
            return false;
        }catch(HasErrorException x){
            return true;
        }
    }

    @Override
    public void handleException(Exception e, Node that) {
        // rethrow
        throw (RuntimeException)e;
    }
    
    @Override
    public void visitAny(Node that) {
        // fast termination
        if(hasAnyError(that))
            throw new HasErrorException();
        super.visitAny(that);
    }

    private boolean hasAnyError(Node that) {
        // skip warnings
        for(Message message : that.getErrors())
            if(!(message instanceof AnalysisWarning))
                return true;
        return false;
    }

}