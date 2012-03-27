package com.redhat.ceylon.compiler.js;

import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Block;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/** This visitor is used to check if a block of code contains method definitions which use expressions
 * declared in the block's scope, to determine if the block should be enclosed in a function.
 * 
 * @author Enrique Zamudio
 */
public class EnclosingFunctionVisitor extends Visitor {

    private Scope mainScope;
    private boolean check;
    private boolean enclose;

    /** Determines whether the specified block should be enclosed in a function. */
    public boolean encloseBlock(Block block) {
        enclose = false;
        check = false;
        if (block.getStatements().isEmpty()) {
            return false;
        } else {
            mainScope = block.getStatements().get(0).getScope();
        }
        block.visit(this);
        mainScope = null;
        return enclose;
    }

    /** Enables checking of base member expressions. */
    public void visit(MethodDefinition that) {
        check = true;
        super.visit(that);
    }

    /** Checks if the expression is used in a scope different from the main one, and if the declaration
     * of this expression is in the main scope. */
    public void visit(BaseMemberExpression that) {
        if (check && !enclose) {
            enclose = that.getScope()!=mainScope && that.getDeclaration().getContainer()==mainScope;
        }
        super.visit(that);
    }

}
