package com.redhat.ceylon.compiler.typechecker.analyzer;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.AnalysisMessage;

/**
 * A warning to the user about a condition that is not 
 * serious enough to prevent generation of code.
 */
public class UsageWarning extends AnalysisMessage {
    
	public UsageWarning(Node treeNode, String message) {
		super(treeNode, message);
	}
    
}
