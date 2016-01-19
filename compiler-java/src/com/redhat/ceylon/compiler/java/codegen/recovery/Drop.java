package com.redhat.ceylon.compiler.java.codegen.recovery;

import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;

/**
 * The most drastic plan of all: Don't transform the declaration at all
 * (declaration error which is unrecoverable)
 */

public class Drop 
        extends TransformationPlan implements LocalizedError {
    Drop(Node node, Message message) {
        super(1000, node, message);
    }
}