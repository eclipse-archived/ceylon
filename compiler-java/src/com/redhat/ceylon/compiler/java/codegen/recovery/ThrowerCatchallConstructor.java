package com.redhat.ceylon.compiler.java.codegen.recovery;

import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;

public class ThrowerCatchallConstructor
        extends TransformationPlan 
        implements LocalizedError {

    protected ThrowerCatchallConstructor(Node node, Message message) {
        super(200, node, message);
    }

}