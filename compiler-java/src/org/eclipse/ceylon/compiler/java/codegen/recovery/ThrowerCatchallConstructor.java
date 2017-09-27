package org.eclipse.ceylon.compiler.java.codegen.recovery;

import org.eclipse.ceylon.compiler.typechecker.tree.Message;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;

public class ThrowerCatchallConstructor
        extends TransformationPlan 
        implements LocalizedError {

    protected ThrowerCatchallConstructor(Node node, Message message) {
        super(200, node, message);
    }

}