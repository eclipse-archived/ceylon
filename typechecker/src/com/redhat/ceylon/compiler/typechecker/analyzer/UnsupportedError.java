package com.redhat.ceylon.compiler.typechecker.analyzer;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.AnalysisMessage;

public class UnsupportedError extends AnalysisMessage {
    
	public UnsupportedError(Node treeNode, String message) {
		super(treeNode, message);
	}
    
}
