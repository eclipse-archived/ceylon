package com.redhat.ceylon.compiler.tree;

import org.antlr.runtime.tree.CommonTree;

public abstract class Node {
	
	private String text;
	public final CommonTree treeNode;
	
	protected Node(CommonTree treeNode) {
		this.treeNode = treeNode; 
		text = treeNode.getText();
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public CommonTree getTreeNode() {
		return treeNode;
	}
	
	public abstract void visitChildren(Visitor visitor);
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + text + ")"; 
	}
}
