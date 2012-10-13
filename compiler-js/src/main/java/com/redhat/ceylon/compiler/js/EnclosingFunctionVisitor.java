package com.redhat.ceylon.compiler.js;

import com.redhat.ceylon.compiler.typechecker.tree.Tree.Block;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Declaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Statement;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/** This visitor is used to check if a block of code contains method definitions which use expressions
 * declared in the block's scope, to determine if the block should be enclosed in a function.
 * 
 * @author Enrique Zamudio
 */
public class EnclosingFunctionVisitor extends Visitor {

    /** Determines whether the specified block should be enclosed in a function. */
    public boolean encloseBlock(Block block) {
        // just check if the block contains a captured declaration
        for (Statement statement : block.getStatements()) {
            if (statement instanceof Declaration) {
                if (((Declaration) statement).getDeclarationModel().isCaptured()) {
                    return true;
                }
            }
        }
        return false;
    }

}
