package com.redhat.ceylon.compiler.java.codegen.recovery;

import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;

/**
 * Base class for different way of coping with errors
 */
public abstract class TransformationPlan {

    private final int drasticness;
    private final Node node;
    private final Message message;

    protected TransformationPlan(int drasticness, Node node, Message message) {
        this.drasticness = drasticness;
        this.node = node;
        this.message = message;
    }
    
    public boolean replaces(TransformationPlan o) {
        return this.drasticness > o.drasticness;
    }

    public int getOrder() {
        return drasticness;
    }

    public Node getNode() {
        return node;
    }

    public Message getErrorMessage() {
        return message;
    }
}