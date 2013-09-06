package com.redhat.ceylon.compiler.java.codegen;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.isBooleanFalse;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.isBooleanTrue;
import static com.sun.tools.javac.code.Flags.FINAL;
import static com.sun.tools.javac.code.Flags.PUBLIC;
import static com.sun.tools.javac.code.Flags.STATIC;

import com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.BoxingStrategy;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
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
public abstract class LiteralAnnotationTerm extends AnnotationTerm {

    private Tree.Term term;
    private ProducedType literalObject;
    
    public LiteralAnnotationTerm() {}
    
    public String toString() {
        return "/* literal */";
    }
    
    @Deprecated
    public void setTerm(Tree.Term field) {
        this.term = field;
    }
    
    @Deprecated
    public Tree.Term getTerm() {
        return term;
    }
    
    @Deprecated
    public ProducedType getLiteralObject() {
        return literalObject;
    }
    
    @Deprecated
    public void setLiteralObject(ProducedType literalObject) {
        this.literalObject = literalObject;
    }
    
    @Override
    public int encode(AbstractTransformer gen, ListBuffer<JCExpression> instantiations) {
        return Short.MIN_VALUE;
    }
    
    @Override
    public JCExpression makeAnnotationArgumentValue(
            ExpressionTransformer exprGen, AnnotationInvocation ai,
            com.sun.tools.javac.util.List<AnnotationFieldName> fieldPath) {
        return makeLiteral(exprGen);
    }

    @Override
    public void makeLiteralAnnotationFields(
            ExpressionTransformer exprGen,
            AnnotationInvocation toplevel,
            final com.sun.tools.javac.util.List<AnnotationFieldName> fieldPath,
            ListBuffer<JCStatement> staticArgs, final ProducedType type) {
        Tree.Term term = getTerm();
        JCExpression expr = null;
        JCExpression typeExpr = null;
        if (term instanceof Tree.Literal) {
            expr = exprGen.transform((Tree.Literal)term);
        } else if (term instanceof Tree.NegativeOp) {
            expr = exprGen.transform((Tree.NegativeOp)term);
        } else if (term instanceof Tree.TypeLiteral) {
            expr = exprGen.makeDeclarationLiteralForAnnotation((Tree.TypeLiteral)term);
            typeExpr = exprGen.makeJavaType(exprGen.typeFact().getStringDeclaration().getType());
        } else if (term instanceof Tree.MemberLiteral) {
            expr = exprGen.makeDeclarationLiteralForAnnotation((Tree.MemberLiteral)term);
            typeExpr = exprGen.makeJavaType(exprGen.typeFact().getStringDeclaration().getType());
        } else if (term instanceof Tree.BaseMemberExpression) {
            Tree.BaseMemberExpression bme = (Tree.BaseMemberExpression)term;
            if (isBooleanTrue(bme.getDeclaration())) {
                expr = exprGen.make().Literal(true);
            } else if (isBooleanFalse(bme.getDeclaration())) {
                expr = exprGen.make().Literal(false);
            } else if (Decl.isAnonCaseOfEnumeratedType(bme)) {
                // Javac can't inline java.lang.Class constant fields
                // so we use the @DefaultedObject annotation on the DPM
                return;
            } else if (bme.getDeclaration().isParameter()) {
                return;
            }
        } else if (term instanceof Tree.Tuple
                || term instanceof Tree.SequenceEnumeration) {
            return;
        }
        
        if (expr == null) {
            expr = exprGen.makeErroneous(term, "Literal cannot be transformed into a constant expression");
        }
        
        if (typeExpr == null) {
            typeExpr = exprGen.makeJavaType(type);
        }
        // If the annotation class's parameter is 'hash' we need to cast the 
        // literal to an int and make an int field, so we can't use the term's type
        expr = exprGen.applyErasureAndBoxing(expr, term.getTypeModel(), 
                false, BoxingStrategy.UNBOXED, type);
        JCVariableDecl field = exprGen.makeVar(STATIC | FINAL | (toplevel.getConstructorDeclaration().isShared() ? PUBLIC : 0), 
                Naming.getAnnotationFieldName(fieldPath),
                typeExpr, 
                expr);
        staticArgs.append(field);
    
    }

    @Override
    public com.sun.tools.javac.util.List<JCAnnotation> makeDpmAnnotations(
            ExpressionTransformer exprGen) {
        return makeAtValue(exprGen, null, makeLiteral(exprGen));
    }
    
    protected abstract JCExpression makeLiteral(ExpressionTransformer exprGen);
    
    protected abstract com.sun.tools.javac.util.List<JCAnnotation> makeAtValue(ExpressionTransformer exprGen, String name, JCExpression value);
    
    public final com.sun.tools.javac.util.List<JCAnnotation> makeExprAnnotations(
            ExpressionTransformer exprGen, AnnotationInvocation toplevel,
            com.sun.tools.javac.util.List<AnnotationFieldName> fieldPath) {
        return makeAtValue(exprGen, Naming.getAnnotationFieldName(fieldPath), makeLiteral(exprGen));
    }
    public com.sun.tools.javac.util.List<JCAnnotation> makeExprs(ExpressionTransformer exprGen, com.sun.tools.javac.util.List<JCAnnotation> value) {
        throw new RuntimeException();
    }
}

