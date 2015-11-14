package com.redhat.ceylon.compiler.java.codegen.recovery;

import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;

/** 
 * Recovery for statements and expressions:
 * generate a statement that throws UnresolvedCompilationError 
 * (declaration error which is recoverable)
 */
public class ThrowStmt 
    extends TransformationPlan implements LocalizedError {

    protected ThrowStmt(Node node, Message message) {
        super(100, node, message);
    }

}