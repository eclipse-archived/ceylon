package com.redhat.ceylon.compiler.typechecker.util;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class DeprecationVisitor extends Visitor {
    @Override
    public void visit(Tree.MemberOrTypeExpression that) {
        super.visit(that);
        Declaration d = that.getDeclaration();
		if (d!=null && d.isDeprecated()) {
        	that.addUsageWarning("declaration is deprecated: " + 
        			d.getName());
        }
    }
    @Override
    public void visit(Tree.SimpleType that) {
    	super.visit(that);
        TypeDeclaration d = that.getDeclarationModel();
		if (d!=null && d.isDeprecated()) {
        	that.addUsageWarning("type is deprecated: " + 
        			d.getName());
        }
    }
    @Override
    public void visit(Tree.ImportMemberOrType that) {
        super.visit(that);
        Declaration d = that.getDeclarationModel();
		if (d!=null && d.isDeprecated()) {
        	that.addUsageWarning("imported declaration is deprecated: " + 
        			d.getName());
        }
    }
}
