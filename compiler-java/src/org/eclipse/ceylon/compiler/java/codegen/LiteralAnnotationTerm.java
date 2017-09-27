package org.eclipse.ceylon.compiler.java.codegen;

import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCAnnotation;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCExpression;
import org.eclipse.ceylon.langtools.tools.javac.util.ListBuffer;

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
            org.eclipse.ceylon.langtools.tools.javac.util.List<AnnotationFieldName> fieldPath) {
        return makeLiteral(exprGen);
    }

    @Override
    public org.eclipse.ceylon.langtools.tools.javac.util.List<JCAnnotation> makeDpmAnnotations(
            ExpressionTransformer exprGen) {
        return makeAtValue(exprGen, null, makeLiteral(exprGen));
    }
    
    protected abstract JCExpression makeLiteral(ExpressionTransformer exprGen);
    
    protected abstract org.eclipse.ceylon.langtools.tools.javac.util.List<JCAnnotation> makeAtValue(ExpressionTransformer exprGen, String name, JCExpression value);
    
    @Override
    public final org.eclipse.ceylon.langtools.tools.javac.util.List<JCAnnotation> makeExprAnnotations(
            ExpressionTransformer exprGen, AnnotationInvocation toplevel,
            org.eclipse.ceylon.langtools.tools.javac.util.List<AnnotationFieldName> fieldPath) {
        return makeAtValue(exprGen, Naming.getAnnotationFieldName(fieldPath), makeLiteral(exprGen));
    }
    
    @Override
    public abstract org.eclipse.ceylon.langtools.tools.javac.util.List<JCAnnotation> makeExprs(ExpressionTransformer exprGen, org.eclipse.ceylon.langtools.tools.javac.util.List<JCAnnotation> value);
}

