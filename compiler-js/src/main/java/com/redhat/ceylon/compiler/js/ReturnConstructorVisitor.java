package com.redhat.ceylon.compiler.js;

import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Util;

public class ReturnConstructorVisitor extends Visitor {

    private final Tree.Constructor node;
    private final Constructor d;
    private boolean ret;

    public ReturnConstructorVisitor(Tree.Constructor constructorNode) {
        node = constructorNode;
        d = node.getDeclarationModel();
        node.getBlock().visit(this);
    }

    public void visit(Tree.Return that) {
        if (that.getExpression() == null && !ret) {
            if (d == Util.getContainingDeclarationOfScope(that.getScope())) {
                ret = true;
            }
        }
    }

    public boolean isReturns() {
        return ret;
    }

}
