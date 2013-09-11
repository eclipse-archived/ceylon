package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.tree.NaturalVisitor;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * A Visitor which assigns ids sometimes neede by Naming.getLocalId() for 
 * determining the name munging necessary for "hoisting" interface declarations 
 * to top level.
 */
public class LocalIdVisitor extends Visitor implements NaturalVisitor {

    private final java.util.Map<Scope, Integer> locals = new java.util.HashMap<Scope, Integer>();
    
    private int localId = 0;
    
    private void resetLocals() {
        localId = 0;
    }
    
    private void local(Scope decl) {
        if (!locals.containsKey(decl)) {
            locals.put(decl, localId);
            localId++;
        }
    }
    
    void noteDecl(Declaration decl) {
        if (decl.isToplevel()) {
            resetLocals();
        } else if (Decl.isLocal(decl)){
            local(decl.getContainer());
        }
    }
    
    public java.util.Map<Scope, Integer> getLocalIds() {
        return locals;
    }
    
    @Override
    public void visit(Tree.ClassOrInterface that) {
        noteDecl(that.getDeclarationModel());
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.TypeAliasDeclaration that) {
        noteDecl(that.getDeclarationModel());
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.ObjectDefinition that) {
        noteDecl(that.getDeclarationModel());
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.ObjectArgument that) {
        noteDecl(that.getDeclarationModel());
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.AnyMethod that) {
        if (Decl.isLocal(that.getDeclarationModel())) {
            noteDecl(that.getDeclarationModel());
        }
        super.visit(that);
    }
}
