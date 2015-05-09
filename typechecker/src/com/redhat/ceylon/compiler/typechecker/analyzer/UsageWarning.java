package com.redhat.ceylon.compiler.typechecker.analyzer;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.AnalysisMessage;

/**
 * A warning to the user about a condition that is not 
 * serious enough to prevent generation of code.
 */
public class UsageWarning extends AnalysisMessage {
    
    private final String name;
    
    private boolean suppressed;
    
    public UsageWarning(Node treeNode, String message, String name) {
        super(treeNode, message);
        this.name = name;
        this.suppressed = false;
    }
    
    public String getWarningName() {
        return name;
    }
    
    public boolean isSuppressed() {
        return suppressed;
    }
    
    public void setSuppressed(boolean suppressed) {
        this.suppressed = suppressed;
    }
}
