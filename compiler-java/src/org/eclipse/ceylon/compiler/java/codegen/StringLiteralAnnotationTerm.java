package org.eclipse.ceylon.compiler.java.codegen;

import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCAnnotation;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCExpression;

public class StringLiteralAnnotationTerm extends LiteralAnnotationTerm {
    
    /** 
     * Sometimes we need an instance just for calling 
     * {@link #makeAtValue(ExpressionTransformer, String, JCExpression)} on.
     */
    public static final LiteralAnnotationTerm FACTORY = new StringLiteralAnnotationTerm(null);
    
    final String value;
    public StringLiteralAnnotationTerm(String value) {
        super();
        this.value = value;
    }
    @Override
    protected org.eclipse.ceylon.langtools.tools.javac.util.List<JCAnnotation> makeAtValue(ExpressionTransformer exprGen, String name, JCExpression value) {
        return exprGen.makeAtStringValue(name, value);
    }
    @Override
    public JCExpression makeLiteral(ExpressionTransformer exprGen) {
        return exprGen.make().Literal(value);
    }
    @Override
    public org.eclipse.ceylon.langtools.tools.javac.util.List<JCAnnotation> makeExprs(ExpressionTransformer exprGen, org.eclipse.ceylon.langtools.tools.javac.util.List<JCAnnotation> value) {
        return exprGen.makeAtStringExprs(exprGen.make().NewArray(null,  null, AbstractTransformer.upcastExprList(value)));
    }
    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}