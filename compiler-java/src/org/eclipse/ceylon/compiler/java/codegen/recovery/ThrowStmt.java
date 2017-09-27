package org.eclipse.ceylon.compiler.java.codegen.recovery;

import org.eclipse.ceylon.compiler.typechecker.tree.Message;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;

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