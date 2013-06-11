package com.redhat.ceylon.compiler.typechecker.util;

import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class DeprecationVisitor extends Visitor {
    @Override
    public void visit(Tree.MemberOrTypeExpression that) {
        super.visit(that);
        if (that.getDeclaration().isDeprecated()) {
        	that.addUsageWarning("declaration is deprecated: " + 
        			that.getDeclaration().getName());
        }
    }
    @Override
    public void visit(Tree.SimpleType that) {
    	super.visit(that);
        if (that.getDeclarationModel().isDeprecated()) {
        	that.addUsageWarning("type is deprecated: " + 
        			that.getDeclarationModel().getName());
        }
    }
    @Override
    public void visit(Tree.ImportMemberOrType that) {
        super.visit(that);
        if (that.getDeclarationModel().isDeprecated()) {
        	that.addUsageWarning("imported declaration is deprecated: " + 
        			that.getDeclarationModel().getName());
        }
    }
}
