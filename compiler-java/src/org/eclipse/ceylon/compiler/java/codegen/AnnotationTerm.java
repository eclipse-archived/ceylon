package org.eclipse.ceylon.compiler.java.codegen;

import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCAnnotation;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCExpression;
import org.eclipse.ceylon.langtools.tools.javac.util.ListBuffer;

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
            org.eclipse.ceylon.langtools.tools.javac.util.List<AnnotationFieldName> fieldPath);
    
    public abstract org.eclipse.ceylon.langtools.tools.javac.util.List<JCAnnotation> makeDpmAnnotations(ExpressionTransformer exprGen);
    
    public abstract org.eclipse.ceylon.langtools.tools.javac.util.List<JCAnnotation> makeExprs(ExpressionTransformer exprGen, org.eclipse.ceylon.langtools.tools.javac.util.List<JCAnnotation> value);

    public abstract org.eclipse.ceylon.langtools.tools.javac.util.List<JCAnnotation> makeExprAnnotations(
            ExpressionTransformer exprGen, AnnotationInvocation toplevel,
            org.eclipse.ceylon.langtools.tools.javac.util.List<AnnotationFieldName> fieldPath);
}