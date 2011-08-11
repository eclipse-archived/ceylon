package com.redhat.ceylon.compiler.typechecker.tree;

public class AnalysisMessage implements Message {
	
    private Node treeNode;
    private String message;
    
    public AnalysisMessage(Node treeNode, String message) {
        this.treeNode = treeNode;
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Node getTreeNode() {
        return treeNode;
    }
    
    @Override
    public String toString() {
        return message;
    }
}
