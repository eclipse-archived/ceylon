package com.redhat.ceylon.compiler.typechecker.analyzer;

import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;

public class AnalysisError implements Message {
    
    private Node treeNode;
    private String message;
    
    public AnalysisError(Node treeNode, String message) {
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
