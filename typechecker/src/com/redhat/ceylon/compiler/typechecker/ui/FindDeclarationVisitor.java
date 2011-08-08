package com.redhat.ceylon.compiler.typechecker.ui;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class FindDeclarationVisitor extends Visitor {
	
	private Declaration declaration;
	private Tree.Declaration declarationNode;
	
	public FindDeclarationVisitor(Declaration declaration) {
		this.declaration = declaration;
	}
	
	public Tree.Declaration getDeclarationNode() {
		return declarationNode;
	}
	
	@Override
	public void visit(Tree.Declaration that) {
		if (that.getDeclarationModel()==declaration) {
			declarationNode = that;
		}
		super.visit(that);
	}
	
	public void visitAny(Node node) {
		if (declarationNode==null) {
			super.visitAny(node);
		}
	}
	
}
