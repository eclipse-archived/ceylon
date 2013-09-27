package com.redhat.ceylon.compiler.typechecker.tree;

/**
 * An unexpected error from the backend. Usually a bug
 * in the compiler. 
 */
public class UnexpectedError extends AnalysisMessage {

	public UnexpectedError(Node treeNode, String message) {
		super(treeNode, message);
	}
    
}
