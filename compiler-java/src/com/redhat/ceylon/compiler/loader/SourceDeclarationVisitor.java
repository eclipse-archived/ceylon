package com.redhat.ceylon.compiler.loader;

import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public abstract class SourceDeclarationVisitor extends Visitor{
    
    abstract public void loadFromSource(Tree.Declaration decl);
    
    @Override
    public void visit(Tree.ClassDefinition that) {
        loadFromSource(that);
    }
    
    @Override
    public void visit(Tree.InterfaceDefinition that) {
        loadFromSource(that);
    }
    
    @Override
    public void visit(Tree.ObjectDefinition that) {
        loadFromSource(that);
    }

    @Override
    public void visit(Tree.MethodDefinition that) {
        loadFromSource(that);
    }

    @Override
    public void visit(Tree.MethodDeclaration that) {
        loadFromSource(that);
    }

    @Override
    public void visit(Tree.AttributeDeclaration that) {
        loadFromSource(that);
    }

    @Override
    public void visit(Tree.AttributeGetterDefinition that) {
        loadFromSource(that);
    }
}