package com.redhat.ceylon.compiler.typechecker.analyzer;

import com.redhat.ceylon.compiler.typechecker.tree.Node;

/**
 * Deprecated. Use UnsupportedError instead.
 */
@Deprecated
public class AnalysisWarning extends UnsupportedError {
    
	public AnalysisWarning(Node treeNode, String message) {
		super(treeNode, message);
	}
    
}
