package com.redhat.ceylon.compiler.typechecker.analyzer;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.AnalysisMessage;

public class AnalysisWarning extends AnalysisMessage {
    
	public AnalysisWarning(Node treeNode, String message) {
		super(treeNode, message);
	}
    
}
