package com.redhat.ceylon.compiler.typechecker.ui;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class FindDeclarationVisitor extends Visitor {
	
	private final Declaration declaration;
	private Tree.Declaration declarationNode;
	
	public FindDeclarationVisitor(Declaration declaration) {
		this.declaration = declaration;
	}
	
	public Tree.Declaration getDeclarationNode() {
		return declarationNode;
	}
	
    protected boolean equals(Declaration x, Declaration y) {
        return x==y;
    }
    
	@Override
	public void visit(Tree.Declaration that) {
		if (equals(that.getDeclarationModel(),declaration)) {
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
