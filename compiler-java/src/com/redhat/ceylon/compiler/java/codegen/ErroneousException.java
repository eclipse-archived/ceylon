package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.sun.tools.javac.tree.JCTree.JCExpression;

public class ErroneousException extends Exception {

    private final Node node;

    public ErroneousException(Node node, String message, Throwable cause) {
        super(message, cause);
        this.node = node;
    }
    
    public ErroneousException(Node node, String message) {
        this(node, message, null);
    }
    
    public JCExpression makeErroneous(AbstractTransformer gen) {
        return gen.makeErroneous(node, getMessage());
    }
    
    public void logError(AbstractTransformer gen) {
        gen.log.error(gen.position(node), "ceylon", getMessage());
    }
}
