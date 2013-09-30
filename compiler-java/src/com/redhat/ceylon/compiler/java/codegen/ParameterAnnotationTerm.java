package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.util.ListBuffer;

/**
 * An argument to an annotation class instantiation that is a parameter
 * of the annotation constructor.
 */
public class ParameterAnnotationTerm extends AnnotationTerm implements AnnotationFieldName {
    
    private Parameter sourceParameter;
    private boolean spread;

    /**
     * The annotation constructor parameter
     */
    public Parameter getSourceParameter() {
        return sourceParameter;
    }

    public void setSourceParameter(Parameter sourceParameter) {
        this.sourceParameter = sourceParameter;
    }

    /**
     * Whether the argument is spread
     */
    public boolean isSpread() {
        return spread;
    }

    public void setSpread(boolean spread) {
        this.spread = spread;
    }
    
    public String toString() {
        return (isSpread() ? "*" : "") + getSourceParameter().getName();
    }
    
    @Override
    public String getFieldNamePart() {
        return "default$" + sourceParameter.getName();
    }

    @Override
    public Parameter getAnnotationField() {
        return getSourceParameter();
    }

    @Override
    public int encode(AbstractTransformer gen, ListBuffer<JCExpression> instantiations) {
        Parameter parameter = getSourceParameter();
        int index = ((Functional)parameter.getDeclaration()).getParameterLists().get(0).getParameters().indexOf(parameter);
        if (isSpread()) {
            index += 256;
        }
        return index;
    }

    @Override
    public JCExpression makeAnnotationArgumentValue(
            ExpressionTransformer exprGen, AnnotationInvocation ai,
            com.sun.tools.javac.util.List<AnnotationFieldName> fieldPath) {
        // The value of the argument is the value of the caller's argument
        // TODO WTF is going on here? surely this first loop should return, or do *something*?
        AnnotationTerm defaultArgument = null;
        for (AnnotationArgument aa : ai.getAnnotationArguments()) {
            if (aa.getParameter().equals(this.getSourceParameter())) {
                aa.getTerm().makeAnnotationArgumentValue(exprGen, ai, fieldPath);
            }
        }
        
        if (ai.getConstructorParameters() != null) {
            for (AnnotationConstructorParameter p : ai.getConstructorParameters()) {
                if (p.getParameter().equals(getSourceParameter())) {
                    defaultArgument = p.getDefaultArgument();
                    if (defaultArgument != null) {
                        return defaultArgument.makeAnnotationArgumentValue(exprGen, ai, com.sun.tools.javac.util.List.<AnnotationFieldName>of(this));
                    } else if (Strategy.hasEmptyDefaultArgument(p.getParameter())) {
                        return exprGen.make().NewArray(null, null, com.sun.tools.javac.util.List.<JCExpression>nil());
                    }
                }
            }
        }
        return exprGen.makeErroneous(null, "compiler bug: not implemented yet");
    }

    @Override
    public com.sun.tools.javac.util.List<JCAnnotation> makeDpmAnnotations(
            ExpressionTransformer exprGen) {
        // TODO I suppose we can have a constructor like (X x, X y=x) so we do need to support this.
        // Or even (X x1, X x2, X[] x3=[x1, x2])
        return exprGen.makeAtParameterValue(makeLiteral(exprGen));
    }
    
    public JCExpression makeLiteral(ExpressionTransformer exprGen) {
        return exprGen.make().Literal(getSourceParameter().getName());
    }

    @Override
    public com.sun.tools.javac.util.List<JCAnnotation> makeExprs(
            ExpressionTransformer exprGen,
            com.sun.tools.javac.util.List<JCAnnotation> value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public com.sun.tools.javac.util.List<JCAnnotation> makeExprAnnotations(
            ExpressionTransformer exprGen, AnnotationInvocation toplevel,
            com.sun.tools.javac.util.List<AnnotationFieldName> fieldPath) {
        // TODO Auto-generated method stub
        return null;
    }

}