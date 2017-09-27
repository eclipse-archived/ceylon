package org.eclipse.ceylon.compiler.js;

import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Scope;

public class BlockWithCaptureVisitor extends Visitor {

    private boolean hasCapture;
    private final Scope scope;

    public BlockWithCaptureVisitor(Tree.Block block) {
        scope = ModelUtil.getRealScope(block.getScope());
        block.visit(this);
    }

    public void visit(Tree.Declaration that) {
        if (that.getDeclarationModel() != null && that.getDeclarationModel().isJsCaptured()) {
            hasCapture |= scope == ModelUtil.getRealScope(that.getDeclarationModel().getScope());
        }
        super.visit(that);
    }

    public void visit(Tree.BaseMemberExpression that) {
        if (that.getDeclaration() != null && that.getDeclaration().isJsCaptured()) {
            hasCapture |= scope == ModelUtil.getRealScope(that.getDeclaration().getScope());
        }
        super.visit(that);
    }

    public boolean hasCapture() {
        return hasCapture;
    }
}
