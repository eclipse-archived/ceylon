package com.redhat.ceylon.compiler.java.codegen.recovery;

import java.util.List;

import com.redhat.ceylon.compiler.typechecker.analyzer.UsageWarning;
import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.UnexpectedError;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;

/**
 * Visitor for expressions which determines whether there are any 
 * errors in the tree.
 * 
 * This visitor is fail fast.
 */
public class ExpressionErrorVisitor 
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

    protected UnexpectedError refersToBrokenDeclaration(Node that) {
        return new UnexpectedError(that, "refers to a declaration with compiler errors");
    }

    /** Is the given message on the given node considered an error */
    protected boolean isError(Node that, Message message) {
        return !(message instanceof UsageWarning);
    }

    public void visit(Tree.MetaLiteral that) {
        Declaration declaration = that.getDeclaration();
        if (declaration instanceof TypedDeclaration) {
            checkType(that, ((TypedDeclaration)declaration).getTypedReference().getFullType());
        } else if (declaration instanceof TypeDeclaration) {
            checkType(that,  ((TypeDeclaration)declaration).getType());
        }
        super.visit(that);
    }

    protected void checkType(Tree.MetaLiteral that, Type type) {
        if (type == null
                ||  type.containsUnknowns()) {
            throw new HasErrorException(that, refersToBrokenDeclaration(that));
        }
    }
    
    @Override
    public void visit(Tree.MemberOrTypeExpression that) {
        if (that.getDeclaration() != null
                && that.getDeclaration().isDropped()) {
            throw new HasErrorException(that, refersToBrokenDeclaration(that));
        }
        super.visit(that);
    }
    
}
