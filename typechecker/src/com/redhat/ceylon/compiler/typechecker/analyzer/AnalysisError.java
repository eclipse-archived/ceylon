package com.redhat.ceylon.compiler.typechecker.analyzer;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.AnalysisMessage;

/**
 * Any error condition that should prevent the backends
 * from generating code. 
 */
public class AnalysisError extends AnalysisMessage {

	public AnalysisError(Node treeNode, String message) {
		super(treeNode, message);
	}
    
    public AnalysisError(Node treeNode, String message, int code) {
        super(treeNode, message, code);
    }
    
}
