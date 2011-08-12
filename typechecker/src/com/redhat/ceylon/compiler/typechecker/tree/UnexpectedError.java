package com.redhat.ceylon.compiler.typechecker.tree;


public class UnexpectedError extends AnalysisMessage {

	public UnexpectedError(Node treeNode, String message) {
		super(treeNode, message);
	}
    
}
