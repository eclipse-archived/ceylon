package com.redhat.ceylon.compiler.typechecker.ui;

import java.util.HashSet;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class SearchVisitor extends Visitor {
	
	interface Matcher {
		boolean matches(String string);
		boolean includeDeclarations();
		boolean includeReferences();
	}
	
	private final Matcher matcher;
	private final Set<Node> nodes = new HashSet<Node>();
	
	public SearchVisitor(Matcher matcher) {
		this.matcher = matcher;
	}
	
	public Set<Node> getNodes() {
		return nodes;
	}
	
	@Override
	public void visit(Tree.StaticMemberOrTypeExpression that) {
		if (matcher.includeReferences() &&
				that.getIdentifier()!=null && 
				matcher.matches(that.getIdentifier().getText())) {
			nodes.add(that);
		}
		super.visit(that);
	}
		
	@Override
	public void visit(Tree.SimpleType that) {
		if (matcher.includeReferences() &&
				that.getIdentifier()!=null && 
				matcher.matches(that.getIdentifier().getText())) {
			nodes.add(that);
		}
		super.visit(that);
	}
		
	@Override
	public void visit(Tree.Declaration that) {
		if (matcher.includeDeclarations() &&
				that.getIdentifier()!=null && 
				matcher.matches(that.getIdentifier().getText())) {
			nodes.add(that);
		}
		super.visit(that);
	}
		
}
