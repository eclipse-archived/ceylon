package com.redhat.ceylon.compiler.java.codegen;

import java.util.EnumSet;

import com.redhat.ceylon.compiler.loader.model.AnnotationProxyClass;
import com.redhat.ceylon.compiler.loader.model.AnnotationProxyMethod;
import com.redhat.ceylon.compiler.loader.model.AnnotationTarget;
import com.redhat.ceylon.compiler.loader.model.OutputElement;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
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
        // do not report an error for a dynamic clause as that can only occur inside a dynamic statement and
        // we already reported an error for that one
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

    public void visit(Tree.AttributeGetterDefinition that) {
        if (!Decl.isForBackend(that))
            return;
        interopAnnotationTargeting(OutputElement.outputs(that), that.getAnnotationList());
        super.visit(that);
    }

    public void visit(Tree.AttributeSetterDefinition that) {
        if (!Decl.isForBackend(that))
            return;
        interopAnnotationTargeting(OutputElement.outputs(that), that.getAnnotationList());
        super.visit(that);
    }
    
    public void visit(Tree.AttributeDeclaration that) {
        if (!Decl.isForBackend(that))
            return;
        interopAnnotationTargeting(OutputElement.outputs(that), that.getAnnotationList());
        super.visit(that);
    }
    
    public void visit(Tree.ObjectDefinition that) {
        if (!Decl.isForBackend(that))
            return;
        interopAnnotationTargeting(OutputElement.outputs(that), that.getAnnotationList());
        super.visit(that);
    }
    
    public void visit(Tree.AnyClass that) {
        if (!Decl.isForBackend(that))
            return;
        interopAnnotationTargeting(OutputElement.outputs(that), that.getAnnotationList());
        super.visit(that);
    }
    
    public void visit(Tree.AnyInterface that) {
        if (!Decl.isForBackend(that))
            return;
        interopAnnotationTargeting(OutputElement.outputs(that), that.getAnnotationList());
        super.visit(that);
    }
    
    public void visit(Tree.AnyMethod that) {
        if (!Decl.isForBackend(that))
            return;
        interopAnnotationTargeting(OutputElement.outputs(that), that.getAnnotationList());
        super.visit(that);
    }

    private void interopAnnotationTargeting(EnumSet<OutputElement> outputs,
            Tree.AnnotationList annotationList) {
        for (Tree.Annotation annotation : annotationList.getAnnotations()) {
            AnnotationTarget.interopAnnotationTargeting(outputs, annotation, true);
        }
    }

}
