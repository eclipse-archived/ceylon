package com.redhat.ceylon.compiler.js;

import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Scope;

public class BlockWithCaptureVisitor extends Visitor {

    private boolean hasCapture;
    private final Scope scope;

    public BlockWithCaptureVisitor(Tree.Block block) {
        scope = ModelUtil.getRealScope(block.getScope());
        block.visit(this);
    }

    public void visit(Tree.Declaration that) {
        if (that.getDeclarationModel() != null && that.getDeclarationModel().isCaptured()) {
            hasCapture |= scope == ModelUtil.getRealScope(that.getDeclarationModel().getScope());
        }
        super.visit(that);
    }

    public void visit(Tree.BaseMemberExpression that) {
        if (that.getDeclaration() != null && that.getDeclaration().isCaptured()) {
            hasCapture |= scope == ModelUtil.getRealScope(that.getDeclaration().getScope());
        }
        super.visit(that);
    }

    public boolean hasCapture() {
        return hasCapture;
    }
}
