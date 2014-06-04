package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class UnsupportedVisitor extends Visitor {
    
    @Override
    public void visit(Tree.Annotation that) {
        String msg = AnnotationInvocationVisitor.checkForBannedJavaAnnotation(that);
        if (msg != null) {
            that.addError(msg);
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.DynamicStatement that) {
        that.addUnsupportedError("dynamic is not yet supported on this platform");
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.Dynamic that) {
        that.addUnsupportedError("dynamic is not yet supported on this platform");
        super.visit(that);
    }

    @Override
    public void visit(Tree.DynamicClause that) {
        that.addUnsupportedError("dynamic is not yet supported on this platform");
        super.visit(that);
    }

    @Override
    public void visit(Tree.DynamicModifier that) {
        that.addUnsupportedError("dynamic is not yet supported on this platform");
        super.visit(that);
    }

    @Override
    public void visit(Tree.FloatLiteral that) {
        try {
            ExpressionTransformer.literalValue(that);
        } catch (ErroneousException e) {
            that.addError(e.getMessage());
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.NaturalLiteral that) {
        try {
            ExpressionTransformer.literalValue(that);
        } catch (ErroneousException e) {
            that.addError(e.getMessage());
        }
        super.visit(that);
    }
    
    public void visit(Tree.NegativeOp that) {
        if (that.getTerm() instanceof Tree.NaturalLiteral) {
            try {
                ExpressionTransformer.literalValue(that);
            } catch (ErroneousException e) {
                that.addError(e.getMessage());
            }
        } else {
            super.visit(that);
        }
    }
}
