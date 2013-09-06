package com.redhat.ceylon.compiler.java.codegen;

import java.util.ArrayList;
import java.util.List;

import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCNewArray;
import com.sun.tools.javac.util.ListBuffer;

public class CollectionLiteralAnnotationTerm extends LiteralAnnotationTerm {
    private final List<AnnotationTerm> elements;
    private final LiteralAnnotationTerm factory;
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
            JCNewArray array = exprGen.make().NewArray(null,  null,  lb.toList());
            return factory.makeAtValue(exprGen, null, array);
            
        }
        return com.sun.tools.javac.util.List.<JCAnnotation>nil();
    }
    @Override
    public String toString() {
        return elements.toString();
    }
    @Override
    protected JCExpression makeLiteral(ExpressionTransformer exprGen) {
        ListBuffer<JCExpression> lb = ListBuffer.lb();
        for (LiteralAnnotationTerm term : (List<LiteralAnnotationTerm>)(List)elements) {
            lb.add(term.makeLiteral(exprGen));
        }
        JCNewArray array = exprGen.make().NewArray(null,  null,  lb.toList());
        return array;
    }
    @Override
    protected com.sun.tools.javac.util.List<JCAnnotation> makeAtValue(
            ExpressionTransformer exprGen, String name, JCExpression value) {
        return factory.makeAtValue(exprGen, name, value);
    }
    @Override
    public com.sun.tools.javac.util.List<JCAnnotation> makeExprs(ExpressionTransformer exprGen, com.sun.tools.javac.util.List<JCAnnotation> value) {
        return factory.makeExprs(exprGen, value);
    }
}