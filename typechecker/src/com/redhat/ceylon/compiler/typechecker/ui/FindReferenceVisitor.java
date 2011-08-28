package com.redhat.ceylon.compiler.typechecker.ui;

import java.util.HashSet;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class FindReferenceVisitor extends Visitor {
	
	private final Declaration declaration;
	private final Set<Node> nodes = new HashSet<Node>();
	
	public FindReferenceVisitor(Declaration declaration) {
		this.declaration = declaration;
	}
	
	public Set<Node> getNodes() {
		return nodes;
	}
	
	@Override
	public void visit(Tree.MemberOrTypeExpression that) {
		//TODO: handle refinement!
		if (that.getDeclaration()==declaration) {
			nodes.add(that);
		}
		super.visit(that);
	}
		
	@Override
	public void visit(Tree.NamedArgument that) {
		if (that.getParameter()==declaration) {
			nodes.add(that);
		}
		super.visit(that);
	}
		
	@Override
	public void visit(Tree.Type that) {
		if (that.getTypeModel().getDeclaration()==declaration) {
			nodes.add(that);
		}
		super.visit(that);
	}
		
	@Override
	public void visit(Tree.ImportMemberOrType that) {
		if (that.getDeclarationModel()==declaration) {
			nodes.add(that);
		}
		super.visit(that);
	}
		
}
