package com.redhat.ceylon.compiler.typechecker.tree;

import com.redhat.ceylon.common.Backend;

/**
 * An unexpected error from the backend. Usually a bug
 * in the compiler. 
 */
public class UnexpectedError extends AnalysisMessage {

	public UnexpectedError(Node treeNode, String message) {
		super(treeNode, message);
	}
    
    public UnexpectedError(Node treeNode, String message, Backend backend) {
        super(treeNode, message, backend);
    }
    
}
