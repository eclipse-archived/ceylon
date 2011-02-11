package com.redhat.ceylon.compiler.analyzer;

import com.redhat.ceylon.compiler.model.Typed;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Tree.CompilationUnit;
import com.redhat.ceylon.compiler.tree.Visitor;

/**
 * Scan the compilation unit again looking for declarations
 * without bodies. Kick off definite assignment validation
 * for each such declaration. TODO: this is a bit of a
 * waste of effort- we don't really need to walk the whole 
 * tree  again looking for declarations we already found.
 * 
 * @author Gavin King
 *
 */
public class LazyDeclarationVisitor extends Visitor {
    
    CompilationUnit unit;
    
    public LazyDeclarationVisitor(CompilationUnit unit) {
        this.unit = unit;
    }

    @Override
    public void visit(Tree.AttributeDeclaration that) {
        if (that.getSpecifierOrInitializerExpression()==null) {
            unit.visit(new SpecificationVisitor((Typed) that.getModelNode()));
        }
    }
    
    @Override
    public void visit(Tree.MethodDeclaration that) {
        if (that.getSpecifierExpression()==null && that.getBlock()==null) {
            unit.visit(new SpecificationVisitor((Typed) that.getModelNode()));
        }
    }
    
}
