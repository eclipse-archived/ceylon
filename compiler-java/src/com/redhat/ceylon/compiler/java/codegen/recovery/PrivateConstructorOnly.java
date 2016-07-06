package com.redhat.ceylon.compiler.java.codegen.recovery;

import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;

public class PrivateConstructorOnly extends TransformationPlan 
    implements LocalizedError {

    protected PrivateConstructorOnly(Node node, Message message) {
        super(200, node, message);
    }

}