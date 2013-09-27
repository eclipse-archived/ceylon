package com.redhat.ceylon.compiler.typechecker.analyzer;

import com.redhat.ceylon.compiler.typechecker.tree.Node;

/**
 * Represents situations that the typechecker accepts,
 * because they are in principle well-typed, but that
 * the backends don't yet support. 
 */
public class UnsupportedError extends AnalysisError {
    
	public UnsupportedError(Node treeNode, String message) {
		super(treeNode, message);
	}
    
}
