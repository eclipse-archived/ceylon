package com.redhat.ceylon.compiler.java.codegen;

import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;


public class InvocationAnnotationTerm extends AnnotationTerm {

    AnnotationInvocation instantiation;

    public AnnotationInvocation getInstantiation() {
        return instantiation;
    }

    public void setInstantiation(AnnotationInvocation instantiation) {
        this.instantiation = instantiation;
    }
    
    @Override
    public String toString() {
        return String.valueOf(instantiation);
    }

    @Override
    public int encode(AbstractTransformer gen, ListBuffer<JCExpression> instantiations) {
        instantiations.add(instantiation.encode(gen, instantiations));
        return -instantiations.size();
    }

    @Override
    public JCExpression makeAnnotationArgumentValue(
            ExpressionTransformer exprGen, AnnotationInvocation ai,
            List<AnnotationFieldName> fieldPath) {
        return instantiation.makeAnnotation(exprGen, ai, fieldPath);
    }

    @Override
    public List<JCAnnotation> makeDpmAnnotations(ExpressionTransformer exprGen) {
        List<JCAnnotation> statics = getInstantiation().makeExprAnnotations(exprGen, null, List.<AnnotationFieldName>nil());
        if (statics == null) {
            statics = List.<JCAnnotation>nil();
        }
        return statics.prepend(exprGen.classGen().makeAtAnnotationInstantiation(getInstantiation()));
    }

    @Override
    public List<JCAnnotation> makeExprs(ExpressionTransformer exprGen,
            List<JCAnnotation> value) {
        // TODO Auto-generated method stub
        return value;
    }

    @Override
    public com.sun.tools.javac.util.List<JCAnnotation> makeExprAnnotations(
            ExpressionTransformer exprGen, AnnotationInvocation toplevel,
            com.sun.tools.javac.util.List<AnnotationFieldName> fieldPath) {
        // Recurse to our instantiation, since it may have constants
        return getInstantiation().makeExprAnnotations(exprGen, toplevel, fieldPath);
    }

}
