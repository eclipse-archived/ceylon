package com.redhat.ceylon.compiler.tree;

import org.antlr.runtime.tree.CommonTree;

public class Node {
	
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
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + text + ")"; 
	}
}
