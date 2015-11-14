package com.redhat.ceylon.compiler.java.codegen.recovery;

import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;

/** 
 * Recovery for declarations: 
 * generate a method/attribute that throws {@code UnresolvedCompilationError} 
 * (declaration error which is recoverable).
 * 
 * This can only be used in limited circumstances where we know enough about 
 * what the declaration should look like that we can be confident the
 * recovery code generated won't itself cause problems for javac
 */
public class ThrowerMethod 
        extends TransformationPlan implements LocalizedError {

    protected ThrowerMethod(Node node, Message message) {
        super(200, node, message);
    }
    
}