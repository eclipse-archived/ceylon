package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;

public class IntegerLiteralAnnotationTerm extends LiteralAnnotationTerm {
    final long value;
    final ProducedType producedType;
    public IntegerLiteralAnnotationTerm(long value, ProducedType producedType) {
        super();
        this.value = value;
        this.producedType = producedType;
    }
    @Override
    public com.sun.tools.javac.util.List<JCAnnotation> makeAtValue(
            ExpressionTransformer exprGen, String name, JCExpression value) {
        return exprGen.makeAtIntegerValue(name, value);
    }
    @Override
    public JCExpression makeLiteral(ExpressionTransformer exprGen) {
        return producedType != null && "int".equals(producedType.getUnderlyingType()) ? exprGen.make().Literal((int)value) : exprGen.make().Literal(value);
    }
    @Override
    public com.sun.tools.javac.util.List<JCAnnotation> makeExprs(ExpressionTransformer exprGen, com.sun.tools.javac.util.List<JCAnnotation> value) {
        return exprGen.makeAtIntegerExprs(exprGen.make().NewArray(null,  null,  (com.sun.tools.javac.util.List)value));
    }
    @Override
    public String toString() {
        return Long.toString(value);
    }
}
