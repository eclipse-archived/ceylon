package com.redhat.ceylon.compiler.java.codegen;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.isBooleanFalse;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.isBooleanTrue;
import static com.sun.tools.javac.code.Flags.FINAL;
import static com.sun.tools.javac.code.Flags.PUBLIC;
import static com.sun.tools.javac.code.Flags.STATIC;

import java.util.List;

import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.ListBuffer;

/**
 * An argument to an annotation class instantiation that is a 'literal'.
 * Despite the name this is used for the top level objects {@code true}
 * and {@code false} as well as Number, Float, Character and 
 * String literals.
 */
public class LiteralAnnotationTerm extends AnnotationTerm {

    private Tree.Term field;
    
    public String toString() {
        return "/* literal */";
    }

    public void setTerm(Tree.Term field) {
        this.field = field;
    }
    
    public Tree.Term getField() {
        return field;
    }

    @Override
    public int encode(AbstractTransformer gen, ListBuffer<JCExpression> instantiations) {
        return Short.MIN_VALUE;
    }
    
    @Override
    public JCExpression makeAnnotationArgumentValue(
            ExpressionTransformer exprGen, AnnotationInvocation ai,
            com.sun.tools.javac.util.List<AnnotationFieldName> fieldPath) {
        return exprGen.naming.makeQuotedQualIdent(
                exprGen.naming.makeName(ai.getConstructorDeclaration(), 
                        Naming.NA_FQ | Naming.NA_WRAPPER ),
                Naming.getAnnotationFieldName(fieldPath));
    }

    @Override
    public void makeLiteralAnnotationFields(
            ExpressionTransformer exprGen,
            AnnotationInvocation toplevel,
            com.sun.tools.javac.util.List<AnnotationFieldName> fieldPath,
            ListBuffer<JCStatement> staticArgs) {
        Tree.Term term = getField();
        JCExpression expr;
        if (term instanceof Tree.Literal) {
            expr = exprGen.transform((Tree.Literal)term);
        } else if (term instanceof Tree.NegativeOp) {
            expr = exprGen.transform((Tree.NegativeOp)term);
        } else if (term instanceof Tree.BaseMemberExpression
                && isBooleanTrue(((Tree.BaseMemberExpression) term).getDeclaration())) {
            expr = exprGen.make().Literal(true);
        } else if (term instanceof Tree.BaseMemberExpression
                && isBooleanFalse(((Tree.BaseMemberExpression) term).getDeclaration())) {
            expr = exprGen.make().Literal(false);
        } else {
            expr = exprGen.makeErroneous(term);
        }
        JCVariableDecl field = exprGen.makeVar(STATIC | FINAL | (toplevel.getConstructorDeclaration().isShared() ? PUBLIC : 0), 
                Naming.getAnnotationFieldName(fieldPath),
                exprGen.makeJavaType(term.getTypeModel()), 
                expr);
        staticArgs.append(field);
    
    }
}