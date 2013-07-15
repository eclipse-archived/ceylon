package com.redhat.ceylon.compiler.typechecker.analyzer;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.AnalysisMessage;

public class FilenameWarning extends AnalysisMessage {
    
	public FilenameWarning(Node treeNode, String message) {
		super(treeNode, message);
	}
    
}
