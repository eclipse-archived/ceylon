package com.redhat.ceylon.compiler.tree;

import org.antlr.runtime.tree.CommonTree;

import com.redhat.ceylon.compiler.model.Scope;
import com.redhat.ceylon.compiler.model.Structure;

public abstract class Node {
	
	private String text;
	private final CommonTree antlrTreeNode;
	private com.redhat.ceylon.compiler.model.Model modelNode;
	private Scope<Structure> scope;
	
	protected Node(CommonTree antlrTreeNode) {
		this.antlrTreeNode = antlrTreeNode; 
		text = antlrTreeNode.getText();
	}
	
	/**
	 * The scope within which the node occurs. 
	 */
	public Scope<Structure> getScope() {
		return scope;
	}

	public void setScope(Scope<Structure> scope) {
		this.scope = scope;
	}

	/**
	 * The corresponding model object. Note that there may be no
	 * corresponding model object, since the two data structures
	 * are not isomorphic.
	 */
	public com.redhat.ceylon.compiler.model.Model getModelNode() {
		return modelNode;
	}

	public void setModelNode(com.redhat.ceylon.compiler.model.Model modelNode) {
		this.modelNode = modelNode;
	}
    
	/**
	 * The text of the corresponding ANTLR node.
	 */
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * The text of the corresponding ANTLR tree node. Never null, 
	 * since the two trees are isomorphic.
	 */
	public CommonTree getAntlrTreeNode() {
		return antlrTreeNode;
	}
	
	public abstract void visitChildren(Visitor visitor);
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + text + ")"; 
	}
}
