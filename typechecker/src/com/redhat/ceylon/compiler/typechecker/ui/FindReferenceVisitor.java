package com.redhat.ceylon.compiler.typechecker.ui;

import java.util.HashSet;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class FindReferenceVisitor extends Visitor {
	
	private Declaration declaration;
	private Set<Tree.MemberOrTypeExpression> expressionNodes = new HashSet<Tree.MemberOrTypeExpression>();
	
	public FindReferenceVisitor(Declaration declaration) {
		this.declaration = declaration;
	}
	
	public Set<Tree.MemberOrTypeExpression> getExpressionNodes() {
		return expressionNodes;
	}
	
	@Override
	public void visit(Tree.MemberOrTypeExpression that) {
		if (that.getDeclaration()==declaration) {
			expressionNodes.add(that);
		}
		super.visit(that);
	}
		
}
