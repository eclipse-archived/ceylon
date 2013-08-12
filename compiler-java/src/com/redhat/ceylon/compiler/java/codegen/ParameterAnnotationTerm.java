package com.redhat.ceylon.compiler.java.codegen;

import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCStatement;
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
    
    
    public JCExpression makePrimitiveDefaultExpr(
            ExpressionTransformer exprGen, 
            AnnotationInvocation anno,
            List<AnnotationFieldName> parameterPath) {
        String constructorParameterName = Naming.getAnnotationFieldName(parameterPath);
        if (anno.isInterop()) {
            return null;
        }
        return exprGen.naming.makeQuotedQualIdent(
                exprGen.naming.makeName(anno.getConstructorDeclaration(), 
                        Naming.NA_FQ | Naming.NA_WRAPPER ),
                constructorParameterName);
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
                    } else if (p.getParameter().isSequenced()) {
                        return exprGen.make().NewArray(null, null, com.sun.tools.javac.util.List.<JCExpression>nil());
                    }
                }
            }
        }
        return exprGen.makeErroneous(null, "Not implemented yet");
    }

    @Override
    public void makeLiteralAnnotationFields(
            ExpressionTransformer exprGen,
            AnnotationInvocation toplevel,
            com.sun.tools.javac.util.List<AnnotationFieldName> fieldPath,
            ListBuffer<JCStatement> staticArgs, ProducedType expectedType) {
        // Do nothing since we don't need to produced any constants for a parameter term
    }
}