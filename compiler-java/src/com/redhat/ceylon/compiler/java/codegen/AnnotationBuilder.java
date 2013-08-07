package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.util.ListBuffer;

public class AnnotationBuilder {

    final ExpressionTransformer exprGen;
    
    private ListBuffer<JCExpression> arguments = ListBuffer.<JCExpression>lb();

    private JCExpression annotationType;
    
    public AnnotationBuilder(ExpressionTransformer exprGen, JCExpression annotationType) {
        this.exprGen = exprGen;
        this.annotationType = annotationType;
    }
    
    public AnnotationBuilder appendArgument(JCExpression expr) {
        arguments.append(expr);
        return this;
    }
    
    JCAnnotation build(Node at) {
        return exprGen.at(at).Annotation(annotationType, 
                arguments.toList());
    }
    
}
