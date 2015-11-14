package com.redhat.ceylon.compiler.java.codegen;

import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.util.ListBuffer;

/**
 * An argument to an annotation class instantiation that is a 'literal'.
 * Despite the name this is used for the top level objects {@code true}
 * and {@code false} as well as Number, Float, Character and 
 * String literals.
 */
public abstract class LiteralAnnotationTerm extends AnnotationTerm {

    public LiteralAnnotationTerm() {}
    
    public abstract String toString();
    
    @Override
    public int encode(AbstractTransformer gen, ListBuffer<JCExpression> instantiations) {
        return Short.MIN_VALUE;
    }
    
    @Override
    public JCExpression makeAnnotationArgumentValue(
            ExpressionTransformer exprGen, AnnotationInvocation ai,
            com.sun.tools.javac.util.List<AnnotationFieldName> fieldPath) {
        return makeLiteral(exprGen);
    }

    @Override
    public com.sun.tools.javac.util.List<JCAnnotation> makeDpmAnnotations(
            ExpressionTransformer exprGen) {
        return makeAtValue(exprGen, null, makeLiteral(exprGen));
    }
    
    protected abstract JCExpression makeLiteral(ExpressionTransformer exprGen);
    
    protected abstract com.sun.tools.javac.util.List<JCAnnotation> makeAtValue(ExpressionTransformer exprGen, String name, JCExpression value);
    
    @Override
    public final com.sun.tools.javac.util.List<JCAnnotation> makeExprAnnotations(
            ExpressionTransformer exprGen, AnnotationInvocation toplevel,
            com.sun.tools.javac.util.List<AnnotationFieldName> fieldPath) {
        return makeAtValue(exprGen, Naming.getAnnotationFieldName(fieldPath), makeLiteral(exprGen));
    }
    
    @Override
    public abstract com.sun.tools.javac.util.List<JCAnnotation> makeExprs(ExpressionTransformer exprGen, com.sun.tools.javac.util.List<JCAnnotation> value);
}

