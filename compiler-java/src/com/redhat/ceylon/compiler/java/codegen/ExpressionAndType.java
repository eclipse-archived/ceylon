package com.redhat.ceylon.compiler.java.codegen;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

/**
 * A transformed expression and it's transformed type
 * @author tom
 */
class ExpressionAndType {

    final JCExpression expression;
    final JCExpression type;
    
    public ExpressionAndType(JCExpression expression, JCExpression type) {
        this.expression = expression;
        this.type = type;
    }
    
    /**
     * Returns a list of the types in the given list
     */
    public static List<JCExpression> toTypeList(Iterable<ExpressionAndType> exprAndTypes) {
        ListBuffer<JCExpression> lb = ListBuffer.<JCExpression>lb();
        for (ExpressionAndType arg : exprAndTypes) {
            lb.append(arg.type);
        }
        return lb.toList();
    }

    /**
     * Returns a list of the expressions in the given list
     */
    public static List<JCExpression> toExpressionList(Iterable<ExpressionAndType> exprAndTypes) {
        ListBuffer<JCExpression> lb = ListBuffer.<JCExpression>lb();
        for (ExpressionAndType arg : exprAndTypes) {
            lb.append(arg.expression);
        }
        return lb.toList();
    }
    
    public String toString() {
        return type + " " + expression;
    }
    
}
