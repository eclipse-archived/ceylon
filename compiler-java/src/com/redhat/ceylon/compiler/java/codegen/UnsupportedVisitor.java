package com.redhat.ceylon.compiler.java.codegen;

import java.util.EnumSet;
import java.util.List;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Annotation;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.typechecker.util.NativeUtil;
import com.redhat.ceylon.model.loader.model.OutputElement;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;

public class UnsupportedVisitor extends Visitor {
    
    static final String DYNAMIC_UNSUPPORTED_ERR = "dynamic is not supported on the JVM";
    private final EeVisitor eeVisitor;

    public UnsupportedVisitor(EeVisitor eeVisitor) {
        this.eeVisitor = eeVisitor;
    }

    @Override
    public void visit(Tree.Annotation that) {
        String msg = AnnotationInvocationVisitor.checkForBannedJavaAnnotation(that);
        if (msg != null) {
            that.addError(msg, Backend.Java);
        }
        super.visit(that);
    }

    @Override
    public void visit(Tree.FloatLiteral that) {
        try {
            ExpressionTransformer.literalValue(that);
        } catch (ErroneousException e) {
            that.addError(e.getMessage(), Backend.Java);
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.NaturalLiteral that) {
        try {
            ExpressionTransformer.literalValue(that);
        } catch (ErroneousException e) {
            that.addError(e.getMessage(), Backend.Java);
        }
        super.visit(that);
    }
    
    public void visit(Tree.NegativeOp that) {
        if (that.getTerm() instanceof Tree.NaturalLiteral) {
            try {
                ExpressionTransformer.literalValue(that);
            } catch (ErroneousException e) {
                that.addError(e.getMessage(), Backend.Java);
            }
        } else {
            super.visit(that);
        }
    }

    public void visit(Tree.AttributeGetterDefinition that) {
        if (!NativeUtil.isForBackend(that, Backend.Java))
            return;
        interopAnnotationTargeting(AnnotationUtil.outputs(that), that.getAnnotationList(), that);
        super.visit(that);
    }

    public void visit(Tree.AttributeSetterDefinition that) {
        if (!NativeUtil.isForBackend(that, Backend.Java))
            return;
        interopAnnotationTargeting(AnnotationUtil.outputs(that), that.getAnnotationList(), that);
        super.visit(that);
    }
    
    public void visit(Tree.AttributeDeclaration that) {
        if (!NativeUtil.isForBackend(that, Backend.Java))
            return;
        interopAnnotationTargeting(AnnotationUtil.outputs(that), that.getAnnotationList(), that);
        super.visit(that);
    }
    
    public void visit(Tree.ObjectDefinition that) {
        if (!NativeUtil.isForBackend(that, Backend.Java))
            return;
        interopAnnotationTargeting(AnnotationUtil.outputs(that), that.getAnnotationList(), that);
        super.visit(that);
    }
    
    public void visit(Tree.AnyClass that) {
        if (!NativeUtil.isForBackend(that, Backend.Java))
            return;
        interopAnnotationTargeting(AnnotationUtil.outputs(that), that.getAnnotationList(), that);
        super.visit(that);
    }
    
    public void visit(Tree.Constructor that) {
        if (!NativeUtil.isForBackend(that, Backend.Java))
            return;
        interopAnnotationTargeting(AnnotationUtil.outputs(that), that.getAnnotationList(), that);
        super.visit(that);
    }
    
    public void visit(Tree.Enumerated that) {
        if (!NativeUtil.isForBackend(that, Backend.Java))
            return;
        interopAnnotationTargeting(AnnotationUtil.outputs(that), that.getAnnotationList(), that);
        super.visit(that);
    }
    
    public void visit(Tree.AnyInterface that) {
        if (!NativeUtil.isForBackend(that, Backend.Java))
            return;
        interopAnnotationTargeting(AnnotationUtil.outputs(that), that.getAnnotationList(), that);
        super.visit(that);
    }
    
    public void visit(Tree.AnyMethod that) {
        if (!NativeUtil.isForBackend(that, Backend.Java))
            return;
        interopAnnotationTargeting(AnnotationUtil.outputs(that), that.getAnnotationList(), that);
        super.visit(that);
    }

    private void interopAnnotationTargeting(EnumSet<OutputElement> outputs,
            Tree.AnnotationList annotationList,
            Tree.Declaration annotated) {
        OutputElement target = null;
        // in theory we could do this for every type of decl that has a natural target, but
        // let's do them as they come. See https://github.com/ceylon/ceylon/issues/5751
        if(annotated instanceof Tree.AttributeDeclaration){
            if(((Tree.AttributeDeclaration) annotated).getSpecifierOrInitializerExpression() instanceof Tree.LazySpecifierExpression)
                target = OutputElement.METHOD;
            else
                target = OutputElement.FIELD;
        }
        Declaration useSite = annotated.getDeclarationModel();
        List<Annotation> annotations = annotationList.getAnnotations();
        for (Tree.Annotation annotation : annotations) {
            Function annoCtorDecl = ((Function)((Tree.BaseMemberExpression)annotation.getPrimary()).getDeclaration());
            boolean addWarnings = true;
            // only add warnings if we don't have a natural target to pick from
            if(target != null){
                addWarnings = !AnnotationUtil.isNaturalTarget(annoCtorDecl, useSite, target);
            }
            AnnotationUtil.interopAnnotationTargeting(eeVisitor.isEeMode(useSite), outputs, annotation, true, addWarnings, annotated.getDeclarationModel());
        }
        AnnotationUtil.duplicateInteropAnnotation(eeVisitor.isEeMode(useSite), outputs, annotations, annotated.getDeclarationModel());
    }    

}
