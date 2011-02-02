package com.redhat.ceylon.compiler.model;

import org.antlr.runtime.tree.CommonTree;

public class Node {
	CommonTree treeNode;
	public CommonTree getTreeNode() {
		return treeNode;
	}
	public void setTreeNode(CommonTree treeNode) {
		this.treeNode = treeNode;
	}
}
