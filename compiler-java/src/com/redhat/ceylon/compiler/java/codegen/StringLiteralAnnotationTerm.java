package com.redhat.ceylon.compiler.java.codegen;

import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;

public class StringLiteralAnnotationTerm extends LiteralAnnotationTerm {
    final String value;
    public StringLiteralAnnotationTerm(String value) {
        super();
        this.value = value;
    }
    public String getValue() {
        return value;
    }
    
    @Override
    protected com.sun.tools.javac.util.List<JCAnnotation> makeAtValue(ExpressionTransformer exprGen, String name, JCExpression value) {
        return exprGen.makeAtStringValue(name, value);
    }
    @Override
    public JCExpression makeLiteral(ExpressionTransformer exprGen) {
        return exprGen.make().Literal(value);
    }
    @Override
    public com.sun.tools.javac.util.List<JCAnnotation> makeExprs(ExpressionTransformer exprGen, com.sun.tools.javac.util.List<JCAnnotation> value) {
        return exprGen.makeAtStringExprs(exprGen.make().NewArray(null,  null,  (com.sun.tools.javac.util.List)value));
    }
    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}