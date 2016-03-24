package com.redhat.ceylon.compiler.js;

import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;

public class ReturnConstructorVisitor extends Visitor {

    private final Tree.Declaration node;
    private final Constructor d;
    private boolean ret;

    public ReturnConstructorVisitor(Tree.Declaration constructorNode) {
        node = constructorNode;
        final Tree.Block block;
        if (constructorNode instanceof Tree.Constructor) {
            d = ((Tree.Constructor)node).getConstructor();
            block = ((Tree.Constructor)node).getBlock();
        } else if (constructorNode instanceof Tree.Enumerated) {
            d = ((Tree.Enumerated)node).getEnumerated();
            block = ((Tree.Enumerated)node).getBlock();
        } else {
            throw new CompilerErrorException("Bug in the JS compiler. "+
                    "A ReturnConstructorVisitor must be created with an Enumerated or Constructor node.");
        }
        block.visit(this);
    }

    public void visit(Tree.Return that) {
        if (that.getExpression() == null && !ret) {
            if (d == ModelUtil.getContainingDeclarationOfScope(that.getScope())) {
                ret = true;
            }
        }
    }

    public boolean isReturns() {
        return ret;
    }

}
