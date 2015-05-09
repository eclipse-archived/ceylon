package com.redhat.ceylon.compiler.typechecker.tree;

import org.antlr.runtime.Token;

/**
 * An error or warning relating to a node of the AST.
 */
public class AnalysisMessage implements Message {
	
    private Node treeNode;
    private String message;
    private int code;
    
    public AnalysisMessage(Node treeNode, String message) {
        this(treeNode, message, 0);
    }
    
    public AnalysisMessage(Node treeNode, String message, int code) {
        this.treeNode = treeNode;
        this.message = message;
        this.code = code;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
    
    public Node getTreeNode() {
        return treeNode;
    }
    
    @Override
    public int getCode() {
        return code;
    }
    
    @Override
    public int getLine() {
    	Token token = treeNode.getToken();
		return token==null ? -1 : token.getLine();
    }
    
    @Override
    public String toString() {
        return message;
    }
}
