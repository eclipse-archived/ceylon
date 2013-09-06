package com.redhat.ceylon.compiler.java.codegen;

import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;

public class DeclarationLiteralAnnotationTerm extends LiteralAnnotationTerm {
    final String value;
    public DeclarationLiteralAnnotationTerm(String value) {
        super();
        this.value = value;
    }
    public String getValue() {
        return value;
    }
    @Override
    public com.sun.tools.javac.util.List<JCAnnotation> makeAtValue(
            ExpressionTransformer exprGen, String name, JCExpression value) {
        return exprGen.makeAtDeclarationValue(name, value);
    }
    @Override
    public JCExpression makeLiteral(ExpressionTransformer exprGen) {
        return exprGen.make().Literal(this.value);
    }
    @Override
    public com.sun.tools.javac.util.List<JCAnnotation> makeExprs(ExpressionTransformer exprGen, com.sun.tools.javac.util.List<JCAnnotation> value) {
        return exprGen.makeAtDeclarationExprs(exprGen.make().NewArray(null,  null,  (com.sun.tools.javac.util.List)value));
    }
    @Override
    public String toString() {
        return value.toString();
    }
}
