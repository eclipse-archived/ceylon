package com.redhat.ceylon.compiler.java.codegen;

import java.util.ArrayList;
import java.util.List;

import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.util.ListBuffer;

public class CollectionLiteralAnnotationTerm extends LiteralAnnotationTerm {
    final List<AnnotationTerm> elements;
    final LiteralAnnotationTerm factory;
    public CollectionLiteralAnnotationTerm(LiteralAnnotationTerm factory) {
        super();
        this.factory = factory;
        this.elements = new ArrayList<AnnotationTerm>(); 
    }
    public boolean isTuple() {
        return factory == null;
    }
    public void addElement(AnnotationTerm element) {
        if (factory != null && !factory.getClass().isInstance(element)) {
            throw new RuntimeException("Different types in sequence " + factory.getClass() + " vs " + element.getClass());
        }
        elements.add(element);
    }
    @Override
    public com.sun.tools.javac.util.List<JCAnnotation> makeDpmAnnotations(
            ExpressionTransformer exprGen) {
        if (factory == null) {// A tuple
            // TODO @TupleValue({elements...})
        } else {// A sequence
            ListBuffer<JCExpression> lb = ListBuffer.lb();
            for (LiteralAnnotationTerm term : (List<LiteralAnnotationTerm>)(List)elements) {
                lb.add(term.makeLiteral(exprGen));
            }
            return factory.makeAtValue(exprGen, null, exprGen.make().NewArray(null,  null,  lb.toList()));
            
        }
        return com.sun.tools.javac.util.List.<JCAnnotation>nil();
    }
    @Override
    public String toString() {
        return elements.toString();
    }
    @Override
    protected JCExpression makeLiteral(ExpressionTransformer exprGen) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    protected com.sun.tools.javac.util.List<JCAnnotation> makeAtValue(
            ExpressionTransformer exprGen, String name, JCExpression value) {
        // TODO Auto-generated method stub
        return null;
    }
}