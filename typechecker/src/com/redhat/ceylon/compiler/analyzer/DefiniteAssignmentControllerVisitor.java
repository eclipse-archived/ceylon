package com.redhat.ceylon.compiler.analyzer;

import com.redhat.ceylon.compiler.model.Typed;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Tree.CompilationUnit;
import com.redhat.ceylon.compiler.tree.Visitor;

public class DefiniteAssignmentControllerVisitor extends Visitor {
    
    CompilationUnit unit;
    
    public DefiniteAssignmentControllerVisitor(CompilationUnit unit) {
        this.unit = unit;
    }

    @Override
    public void visit(Tree.AttributeDeclaration that) {
        if (that.getSpecifierOrInitializerExpression()==null) {
            unit.visit(new DefiniteAssignmentVisitor((Typed) that.getModelNode()));
        }
    }
    
    @Override
    public void visit(Tree.MethodDeclaration that) {
        if (that.getSpecifierExpression()==null && that.getBlock()==null) {
            unit.visit(new DefiniteAssignmentVisitor((Typed) that.getModelNode()));
        }
    }
}
