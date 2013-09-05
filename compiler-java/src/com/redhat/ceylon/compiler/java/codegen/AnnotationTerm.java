package com.redhat.ceylon.compiler.java.codegen;

import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.util.ListBuffer;

/**
 * Represents an argument in the invocation of an annotation class or
 * annotation constructor
 */
public abstract class AnnotationTerm {
    
    /**
     * On the JVM the number of method parameters is limited to 255
     * So 0-255 are for unmodified parameter expressions being used as arguments
     * 256-511 are for spread parameter expressions being used as arguments
     */
    public abstract int encode(AbstractTransformer gen, ListBuffer<JCExpression> instantiations);
    
    public abstract JCExpression makeAnnotationArgumentValue(
            ExpressionTransformer exprGen, AnnotationInvocation ai,
            com.sun.tools.javac.util.List<AnnotationFieldName> fieldPath);
    
    public static AnnotationTerm decode(List<Parameter> sourceParameters, AnnotationInvocation info, int code) {
        AnnotationTerm result;
        if (code == Short.MIN_VALUE) {
            result = new LiteralAnnotationTerm();
        } else if (code < 0) {
            InvocationAnnotationTerm invocation = new InvocationAnnotationTerm();
            result = invocation;
        } else if (code >= 0 && code < 512) {
            ParameterAnnotationTerm parameterArgument = new ParameterAnnotationTerm();
            boolean spread = false;
            if (code >= 256) {
                spread = true;
                code-=256;
            }
            
            parameterArgument.setSpread(spread);
            Parameter sourceParameter = sourceParameters.get(code);
            parameterArgument.setSourceParameter(sourceParameter);
            //result.setTargetParameter(sourceParameter);
            result = parameterArgument;
        } else {
            throw Assert.fail();
        }
        return result;
    }

    public abstract void makeLiteralAnnotationFields(
            ExpressionTransformer exprGen, 
            AnnotationInvocation toplevel,
            com.sun.tools.javac.util.List<AnnotationFieldName> fieldPath,
            ListBuffer<JCStatement> staticArgs, ProducedType expectedType);

    public abstract com.sun.tools.javac.util.List<JCAnnotation> makeDpmAnnotations(ExpressionTransformer exprGen);
    
    public abstract com.sun.tools.javac.util.List<JCAnnotation> makeExprs(ExpressionTransformer exprGen, com.sun.tools.javac.util.List<JCAnnotation> value);

    public abstract com.sun.tools.javac.util.List<JCAnnotation> makeExprAnnotations(
            ExpressionTransformer exprGen, AnnotationInvocation toplevel,
            com.sun.tools.javac.util.List<AnnotationFieldName> fieldPath);
}