package com.redhat.ceylon.compiler.loader;

import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public abstract class SourceDeclarationVisitor extends Visitor{
    
    abstract public void loadFromSource(Tree.Declaration decl);
    
    @Override
    public void visit(Tree.TypeAliasDeclaration that) {
        loadFromSource(that);
    }

    @Override
    public void visit(Tree.AnyClass that) {
        loadFromSource(that);
    }
    
    @Override
    public void visit(Tree.AnyInterface that) {
        loadFromSource(that);
    }
    
    @Override
    public void visit(Tree.ObjectDefinition that) {
        loadFromSource(that);
    }

    @Override
    public void visit(Tree.AnyMethod that) {
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