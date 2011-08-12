package com.redhat.ceylon.compiler.codegen;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.UnexpectedError;

/**
 * Represents a bug in the code generator. 
 * @author Tom Bentley
 */
public class CodeGenError extends UnexpectedError {

    private Exception cause;
    
    public CodeGenError(Node treeNode, String message, Exception cause) {
        super(treeNode, message);
        this.cause = cause;
    }
    
    public Exception getCause() {
        return cause;
    }

}
