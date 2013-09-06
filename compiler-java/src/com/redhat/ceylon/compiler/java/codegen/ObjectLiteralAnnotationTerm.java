package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;

public class ObjectLiteralAnnotationTerm extends LiteralAnnotationTerm {
    /** 
     * Sometimes we need an instance just for calling 
     * {@link #makeAtValue(ExpressionTransformer, String, JCExpression)} on.
     */
    public static final LiteralAnnotationTerm FACTORY = new ObjectLiteralAnnotationTerm(null);
    
    final ProducedType value;
    
    public ObjectLiteralAnnotationTerm(ProducedType value) {
        super();
        this.value = value;
    }
    @Override
    public com.sun.tools.javac.util.List<JCAnnotation> makeAtValue(
            ExpressionTransformer exprGen, String name, JCExpression value) {
        return exprGen.makeAtObjectValue(name, value);
    }
    @Override
    public JCExpression makeLiteral(ExpressionTransformer exprGen) {
        return exprGen.makeClassLiteral(value);
    }
    @Override
    public com.sun.tools.javac.util.List<JCAnnotation> makeExprs(ExpressionTransformer exprGen, com.sun.tools.javac.util.List<JCAnnotation> value) {
        return exprGen.makeAtObjectExprs(exprGen.make().NewArray(null,  null,  (com.sun.tools.javac.util.List)value));
    }
    @Override
    public String toString() {
        return value.toString();
    }
}
