package com.redhat.ceylon.compiler.tree;

import org.antlr.runtime.tree.CommonTree;

public abstract class Node {
	
	private String text;
	private final CommonTree antlrTreeNode;
	private com.redhat.ceylon.compiler.model.Model modelNode;
	
	protected Node(CommonTree antlrTreeNode) {
		this.antlrTreeNode = antlrTreeNode; 
		text = antlrTreeNode.getText();
	}
	
	public com.redhat.ceylon.compiler.model.Model getModelNode() {
		return modelNode;
	}

	public void setModelNode(com.redhat.ceylon.compiler.model.Model modelNode) {
		this.modelNode = modelNode;
	}

	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public CommonTree getAntlrTreeNode() {
		return antlrTreeNode;
	}
	
	public abstract void visitChildren(Visitor visitor);
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + text + ")"; 
	}
}
