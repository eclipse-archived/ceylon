package org.eclipse.ceylon.compiler.java.codegen.recovery;

import org.eclipse.ceylon.compiler.typechecker.tree.Message;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;

public class PrivateConstructorOnly extends TransformationPlan 
    implements LocalizedError {

    protected PrivateConstructorOnly(Node node, Message message) {
        super(200, node, message);
    }

}