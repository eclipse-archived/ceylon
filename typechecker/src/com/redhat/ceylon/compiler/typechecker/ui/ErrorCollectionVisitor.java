package com.redhat.ceylon.compiler.typechecker.ui;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.NaturalVisitor;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class ErrorCollectionVisitor extends Visitor implements NaturalVisitor {
    
    private final List<Message> errors = new ArrayList<Message>();
    private boolean withinDeclaration;
    private boolean includingChildren;
    
    private final Tree.Declaration declaration;

    public ErrorCollectionVisitor(Tree.Declaration declaration, 
            Boolean includingChildren) {
        this.declaration = declaration;
        this.includingChildren = includingChildren;
    }
    
    public List<Message> getErrors() {
        return errors;
    }
    
    @Override
    public void visit(Tree.Declaration that) {
        if (that==declaration) {
            withinDeclaration=true;
            super.visit(that);
            withinDeclaration=false;
        }
        else if (includingChildren) {
            super.visit(that);
        }
        else {
            boolean outer = withinDeclaration;
            withinDeclaration = false;
            super.visit(that);
            withinDeclaration = outer;
        }
    }
    
    @Override
    public void visitAny(Node that) {
        if (withinDeclaration) {
            errors.addAll(that.getErrors());
        }
        super.visitAny(that);
    }
    
}
