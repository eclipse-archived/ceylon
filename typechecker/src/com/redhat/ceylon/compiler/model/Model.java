package com.redhat.ceylon.compiler.model;

import com.redhat.ceylon.compiler.tree.Node;

public class Model {
	
	Node treeNode;
	
	public Node getTreeNode() {
		return treeNode;
	}
	public void setTreeNode(Node treeNode) {
		this.treeNode = treeNode;
	}
	
}
