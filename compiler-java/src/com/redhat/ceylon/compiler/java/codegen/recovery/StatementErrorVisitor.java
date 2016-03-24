package com.redhat.ceylon.compiler.java.codegen.recovery;

import java.util.List;

import com.redhat.ceylon.compiler.typechecker.analyzer.UsageWarning;
import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Visitor for statements which determines whether there are any 
 * errors in the tree.
 * 
 * This visitor is fail fast.
 */
class StatementErrorVisitor 
        extends Visitor {
    
    public boolean hasErrors(Node target) {
        try{
            target.visit(this);
            return false;
        }catch(HasErrorException x){
            return true;
        }
    }

    public HasErrorException getFirstErrorMessage(Node target) {
        try{
            target.visit(this);
            return null;
        }catch(HasErrorException x){
            return x;
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
        throwIfError(that);
        super.visitAny(that);
    }

    protected void throwIfError(Node that) {
        Message m = hasError(that);
        if (m != null) {
            throw new HasErrorException(that, m);
        }
    }

    protected Message hasError(Node that) {
        // skip only usage warnings
        List<Message> errors = that.getErrors();
        for(int i=0,l=errors.size();i<l;i++){
            Message message = errors.get(i);
            if(isError(that, message))
                return message;
        }
        return null;
    }

    /** Is the given message on the given node considered an error */
    protected boolean isError(Node that, Message message) {
        return !(message instanceof UsageWarning);
    }

    
    @Override
    public void visit(Tree.Block that) {
        // don't go there
    }
    
    @Override
    public void visit(Tree.Declaration that) {
        // don't go there
    }
    
    @Override
    public void visit(Tree.Variable that) {
        // unlike other declarations, Variables are part of the 
        // statement, and if they have a pb we're screwed, so we
        // *do* want to visit them.
         visitAny(that);
    }
}