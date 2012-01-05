/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.compiler.java.codegen;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.redhat.ceylon.compiler.java.util.Decl;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BinaryOperatorExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.LogicalOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierExpression;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCConditional;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCForLoop;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCNewArray;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCUnary;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

/**
 * This transformer deals with expressions only
 */
public class ExpressionTransformer extends AbstractTransformer {

    private boolean inStatement = false;
    private boolean needDollarThis = false;
    
    public static ExpressionTransformer getInstance(Context context) {
        ExpressionTransformer trans = context.get(ExpressionTransformer.class);
        if (trans == null) {
            trans = new ExpressionTransformer(context);
            context.put(ExpressionTransformer.class, trans);
        }
        return trans;
    }

	private ExpressionTransformer(Context context) {
        super(context);
    }

	//
	// Statement expressions
	
    public JCStatement transform(Tree.ExpressionStatement tree) {
        // ExpressionStatements do not return any value, therefore we don't care about the type of the expressions.
        inStatement = true;
        JCStatement result = at(tree).Exec(transformExpression(tree.getExpression(), BoxingStrategy.INDIFFERENT, null));
        inStatement = false;
        return result;
    }
    
    public JCStatement transform(Tree.SpecifierStatement op) {
        // SpecifierStatement do not return any value, therefore we don't care about the type of the expressions.
        inStatement = true;
        JCStatement result = at(op).Exec(transformAssignment(op, op.getBaseMemberExpression(), op.getSpecifierExpression().getExpression()));
        inStatement = false;
        return result;
    }
    
    //
    // Any sort of expression
    
    JCExpression transformExpression(final Tree.Term expr) {
        return transformExpression(expr, BoxingStrategy.BOXED, null);
    }

    JCExpression transformExpression(final Tree.Term expr, BoxingStrategy boxingStrategy, ProducedType expectedType) {
        if (expr == null) {
            return null;
        }
        
        at(expr);
        if (inStatement && boxingStrategy != BoxingStrategy.INDIFFERENT) {
            // We're not directly inside the ExpressionStatement anymore
            inStatement = false;
        }
        
        CeylonVisitor v = new CeylonVisitor(gen());
        // FIXME: shouldn't that be in the visitor?
        if (expr instanceof Tree.Expression) {
            // Cope with things like ((expr))
            Tree.Expression expr2 = (Tree.Expression)expr;
            while(((Tree.Expression)expr2).getTerm() instanceof Tree.Expression) {
                expr2 = (Tree.Expression)expr2.getTerm();
            }
            expr2.visitChildren(v);
        } else {
            expr.visit(v);
        }
        
        if (!v.hasResult()) {
        	return make().Erroneous();
        }
        
        JCExpression result = v.getSingleResult();

        result = applyErasureAndBoxing(result, expr, boxingStrategy, expectedType);

        return result;
    }
    
    //
    // Boxing and erasure of expressions
    
    private JCExpression applyErasureAndBoxing(JCExpression result, Tree.Term expr, BoxingStrategy boxingStrategy, ProducedType expectedType) {
        ProducedType exprType = expr.getTypeModel();
        boolean exprBoxed = !Util.isUnBoxed(expr);
        return applyErasureAndBoxing(result, exprType, exprBoxed, boxingStrategy, expectedType);
    }
    
    private JCExpression applyErasureAndBoxing(JCExpression result, ProducedType exprType,
            boolean exprBoxed,
            BoxingStrategy boxingStrategy, ProducedType expectedType) {
        
        if (expectedType != null
                && !(expectedType.getDeclaration() instanceof TypeParameter) 
                && willEraseToObject(exprType)
                // don't add cast to an erased type 
                && !willEraseToObject(expectedType)
                // don't add cast for null
                && !isNothing(exprType)) {
            // Erased types need a type cast
            JCExpression targetType = makeJavaType(expectedType, AbstractTransformer.TYPE_ARGUMENT);
            exprType = expectedType;
            result = make().TypeCast(targetType, result);
        }

        // we must to the boxing after the cast to the proper type
        return boxUnboxIfNecessary(result, exprBoxed, exprType, boxingStrategy);
    }

    //
    // Literals
    
    JCExpression ceylonLiteral(String s) {
        JCLiteral lit = make().Literal(s);
        return lit;
    }

    public JCExpression transform(Tree.StringLiteral string) {
        // FIXME: this is appalling
        String value = string
                .getText()
                .substring(1, string.getText().length() - 1)
                .replace("\r\n", "\n")
                .replace("\r", "\n")
                .replace("\\b", "\b")
                .replace("\\t", "\t")
                .replace("\\n", "\n")
                .replace("\\f", "\f")
                .replace("\\r", "\r")
                .replace("\\\\", "\\")
                .replace("\\\"", "\"")
                .replace("\\'", "'")
                .replace("\\`", "`");
        at(string);
        return ceylonLiteral(value);
    }

    public JCExpression transform(Tree.QuotedLiteral string) {
        String value = string
                .getText()
                .substring(1, string.getText().length() - 1);
        JCExpression result = makeSelect(makeIdent(syms().ceylonQuotedType), "instance");
        return at(string).Apply(null, result, List.<JCTree.JCExpression>of(make().Literal(value)));
    }

    public JCExpression transform(Tree.CharLiteral lit) {
        // FIXME: go unicode, but how?
        JCExpression expr = make().Literal(TypeTags.CHAR, (int) lit.getText().charAt(1));
        // XXX make().Literal(lit.value) doesn't work here... something
        // broken in javac?
        return expr;
    }

    public JCExpression transform(Tree.FloatLiteral lit) {
        JCExpression expr = make().Literal(Double.parseDouble(lit.getText()));
        return expr;
    }

    public JCExpression transform(Tree.NaturalLiteral lit) {
        JCExpression expr = make().Literal(Long.parseLong(lit.getText()));
        return expr;
    }

    public JCExpression transformStringExpression(Tree.StringTemplate expr) {
        at(expr);
        JCExpression builder;
        builder = make().NewClass(null, null, makeQualIdentFromString("java.lang.StringBuilder"), List.<JCExpression>nil(), null);

        java.util.List<Tree.StringLiteral> literals = expr.getStringLiterals();
        java.util.List<Tree.Expression> expressions = expr.getExpressions();
        for (int ii = 0; ii < literals.size(); ii += 1) {
            Tree.StringLiteral literal = literals.get(ii);
            if (!"\"\"".equals(literal.getText())) {// ignore empty string literals
                at(literal);
                builder = make().Apply(null, makeSelect(builder, "append"), List.<JCExpression>of(transform(literal)));
            }
            if (ii == expressions.size()) {
                // The loop condition includes the last literal, so break out
                // after that because we've already exhausted all the expressions
                break;
            }
            Tree.Expression expression = expressions.get(ii);
            at(expression);
            // Here in both cases we don't need a type cast for erasure
            if (isCeylonBasicType(expression.getTypeModel())) {// TODO: Test should be erases to String, long, int, boolean, char, byte, float, double
                // If erases to a Java primitive just call append, don't box it just to call format. 
                builder = make().Apply(null, makeSelect(builder, "append"), List.<JCExpression>of(transformExpression(expression, BoxingStrategy.UNBOXED, null)));
            } else {
                JCMethodInvocation formatted = make().Apply(null, makeSelect(transformExpression(expression), "toString"), List.<JCExpression>nil());
                builder = make().Apply(null, makeSelect(builder, "append"), List.<JCExpression>of(formatted));
            }
        }

        return make().Apply(null, makeSelect(builder, "toString"), List.<JCExpression>nil());
    }

    public JCExpression transform(Tree.SequenceEnumeration value) {
        at(value);
        if (value.getExpressionList() == null) {
            return makeEmpty();
        } else {
            java.util.List<Tree.Expression> list = value.getExpressionList().getExpressions();
            ProducedType seqElemType = value.getTypeModel().getTypeArgumentList().get(0);
            return makeSequence(list, seqElemType);
        }
    }

    public JCTree transform(Tree.This expr) {
        at(expr);
        if (needDollarThis) {
            return makeUnquotedIdent("$this");
        } else {
            return makeUnquotedIdent("this");
        }
    }

    public JCTree transform(Tree.Super expr) {
        at(expr);
        return makeUnquotedIdent("super");
    }

    public JCTree transform(Tree.Outer expr) {
        at(expr);
        ProducedType outerClass = com.redhat.ceylon.compiler.typechecker.model.Util.getOuterClassOrInterface(expr.getScope());
        return makeSelect(outerClass.getDeclaration().getName(), "this");
    }

    //
    // Unary and Binary operators that can be overridden
    
    private static class OperatorTranslation {
        String ceylonMethod;
        int javacOperator;
        OperatorTranslation(String ceylonMethod, int javacOperator) {
            this.ceylonMethod = ceylonMethod;
            this.javacOperator = javacOperator;
        }
        OperatorTranslation(String ceylonMethod) {
            this(ceylonMethod, -1);
        }
        boolean isOptimisable(){
            return javacOperator >= 0;
        }
    }
    
    private static class AssignmentOperatorTranslation {
        int javacOperator;
        Class<? extends BinaryOperatorExpression> binaryOperator;

        AssignmentOperatorTranslation(Class<? extends Tree.BinaryOperatorExpression> binaryOperator,
                int javacOperator) {
            this.javacOperator = javacOperator;
            this.binaryOperator = binaryOperator;
        }
        
    }
    
    private static Map<Class<? extends Tree.UnaryOperatorExpression>, OperatorTranslation> unaryOperators;
    private static Map<Class<? extends Tree.BinaryOperatorExpression>, OperatorTranslation> binaryOperators;
    private static Map<Class<? extends Tree.AssignmentOp>, AssignmentOperatorTranslation> assignmentOperators;

    private static void addUnaryOperator(Class<? extends Tree.UnaryOperatorExpression> ceylonTreeType, String ceylonMethodName, int javacOperator) {
        unaryOperators.put(ceylonTreeType, new OperatorTranslation(ceylonMethodName, javacOperator));
    }

    private static void addBinaryOperator(Class<? extends Tree.BinaryOperatorExpression> ceylonTreeType, String ceylonMethodName, int javacOperator) {
        binaryOperators.put(ceylonTreeType, new OperatorTranslation(ceylonMethodName, javacOperator));
    }

    private static void addBinaryOperator(Class<? extends Tree.BinaryOperatorExpression> ceylonTreeType, String ceylonMethodName) {
        binaryOperators.put(ceylonTreeType, new OperatorTranslation(ceylonMethodName));
    }

    private static void addAssignmentOperator(Class<? extends Tree.AssignmentOp> ceylonTreeType,  
            Class<? extends Tree.BinaryOperatorExpression> ceylonOperator,
            int javacOperator) {
        assignmentOperators.put(ceylonTreeType, new AssignmentOperatorTranslation(ceylonOperator, javacOperator));
    }

    static {
        unaryOperators = new HashMap<Class<? extends Tree.UnaryOperatorExpression>, OperatorTranslation>();
        binaryOperators = new HashMap<Class<? extends Tree.BinaryOperatorExpression>, OperatorTranslation>();
        assignmentOperators = new HashMap<Class<? extends Tree.AssignmentOp>, AssignmentOperatorTranslation>();

        // Unary operators
        addUnaryOperator(Tree.PositiveOp.class, "positiveValue", JCTree.POS);
        addUnaryOperator(Tree.NegativeOp.class, "negativeValue", JCTree.NEG);
        addUnaryOperator(Tree.PostfixIncrementOp.class, "getSuccessor", JCTree.POSTINC);
        addUnaryOperator(Tree.PostfixDecrementOp.class, "getPredecessor", JCTree.POSTDEC);
        addUnaryOperator(Tree.IncrementOp.class, "getSuccessor", JCTree.PREINC);
        addUnaryOperator(Tree.DecrementOp.class, "getPredecessor", JCTree.PREDEC);
        
        // Binary operators
        addBinaryOperator(Tree.SumOp.class, "plus", JCTree.PLUS);
        addBinaryOperator(Tree.DifferenceOp.class, "minus", JCTree.MINUS);
        addBinaryOperator(Tree.ProductOp.class, "times", JCTree.MUL);
        addBinaryOperator(Tree.QuotientOp.class, "divided", JCTree.DIV);
        addBinaryOperator(Tree.PowerOp.class, "power");
        addBinaryOperator(Tree.RemainderOp.class, "remainder", JCTree.MOD);
        addBinaryOperator(Tree.IntersectionOp.class, "and", JCTree.BITAND);
        addBinaryOperator(Tree.UnionOp.class, "or", JCTree.BITOR);
        addBinaryOperator(Tree.XorOp.class, "xor", JCTree.BITXOR);
        addBinaryOperator(Tree.EqualOp.class, "equals", JCTree.EQ);
        addBinaryOperator(Tree.CompareOp.class, "compare");

        // Binary operators that act on intermediary Comparison objects
        addBinaryOperator(Tree.LargerOp.class, "largerThan", JCTree.GT);
        addBinaryOperator(Tree.SmallerOp.class, "smallerThan", JCTree.LT);
        addBinaryOperator(Tree.LargeAsOp.class, "asLargeAs", JCTree.GE);
        addBinaryOperator(Tree.SmallAsOp.class, "asSmallAs", JCTree.LE);
        
        // Assignment operators
        addAssignmentOperator(Tree.AddAssignOp.class, Tree.SumOp.class, JCTree.PLUS_ASG);
        addAssignmentOperator(Tree.SubtractAssignOp.class, Tree.DifferenceOp.class, JCTree.MINUS_ASG);
        addAssignmentOperator(Tree.MultiplyAssignOp.class, Tree.ProductOp.class, JCTree.MUL_ASG);
        addAssignmentOperator(Tree.DivideAssignOp.class, Tree.QuotientOp.class, JCTree.DIV_ASG);
        addAssignmentOperator(Tree.RemainderAssignOp.class, Tree.RemainderOp.class, JCTree.MOD_ASG);
        addAssignmentOperator(Tree.AndAssignOp.class, Tree.AndOp.class, JCTree.BITAND_ASG);
        addAssignmentOperator(Tree.OrAssignOp.class, Tree.OrOp.class, JCTree.BITOR_ASG);
    }

    //
    // Unary operators

    public JCExpression transform(Tree.NotOp op) {
        // No need for an erasure cast since Term must be Boolean and we never need to erase that
        JCExpression term = transformExpression(op.getTerm(), Util.getBoxingStrategy(op), null);
        JCUnary jcu = at(op).Unary(JCTree.NOT, term);
        return jcu;
    }

    public JCExpression transform(Tree.IsOp op) {
        // we don't need any erasure type cast for an "is" test
        JCExpression expression = transformExpression(op.getTerm());
        at(op);
        return makeTypeTest(expression, op.getType().getTypeModel());
    }

    public JCTree transform(Tree.Nonempty op) {
        // we don't need any erasure type cast for a "nonempty" test
        JCExpression expression = transformExpression(op.getTerm());
        at(op);
        return makeTypeTest(expression, op.getUnit().getSequenceDeclaration().getType());
    }

    public JCTree transform(Tree.Exists op) {
        // we don't need any erasure type cast for an "exists" test
        JCExpression expression = transformExpression(op.getTerm());
        at(op);
        return  make().Binary(JCTree.NE, expression, makeNull());
    }

    public JCExpression transform(Tree.PositiveOp op) {
        return transformOverridableUnaryOperator(op, op.getUnit().getInvertableDeclaration());
    }

    public JCExpression transform(Tree.NegativeOp op) {
        return transformOverridableUnaryOperator(op, op.getUnit().getInvertableDeclaration());
    }

    public JCExpression transform(Tree.UnaryOperatorExpression op) {
        return transformOverridableUnaryOperator(op, (ProducedType)null);
    }

    private JCExpression transformOverridableUnaryOperator(Tree.UnaryOperatorExpression op, Interface compoundType) {
        ProducedType leftType = getSupertype(op.getTerm(), compoundType);
        return transformOverridableUnaryOperator(op, leftType);
    }
    
    private JCExpression transformOverridableUnaryOperator(Tree.UnaryOperatorExpression op, ProducedType expectedType) {
        at(op);
        Tree.Term term = op.getTerm();

        OperatorTranslation operator = unaryOperators.get(op.getClass());
        if (operator == null) {
            return make().Erroneous();
        }

        boolean isUnboxed = term.getUnboxed();
        if(isUnboxed && operator.isOptimisable()){
            // optimisation for unboxed types
            return make().Unary(operator.javacOperator, transformExpression(term, BoxingStrategy.UNBOXED, expectedType));
        }
        
        return make().Apply(null, makeSelect(transformExpression(term, BoxingStrategy.BOXED, expectedType), 
                Util.getGetterName(operator.ceylonMethod)), List.<JCExpression> nil());
    }

    //
    // Binary operators
    
    public JCExpression transform(Tree.NotEqualOp op) {
        // we want it boxed only if the operator itself is boxed, otherwise we're optimising it
        // we don't care about the left erased type, since equals() is on Object
        JCExpression left = transformExpression(op.getLeftTerm(), getBoxingStrategy(op), null);
        // we don't care about the right erased type, since equals() is on Object
        JCExpression expr = transformOverridableBinaryOperator(op, Tree.EqualOp.class, left, null);
        return at(op).Unary(JCTree.NOT, expr);
    }

    public JCExpression transform(Tree.RangeOp op) {
        // we need to get the range bound type
        ProducedType comparableType = getSupertype(op.getLeftTerm(), op.getUnit().getComparableDeclaration());
        ProducedType paramType = getTypeArgument(comparableType);
        JCExpression lower = transformExpression(op.getLeftTerm(), BoxingStrategy.BOXED, paramType);
        JCExpression upper = transformExpression(op.getRightTerm(), BoxingStrategy.BOXED, paramType);
        ProducedType rangeType = typeFact().getRangeType(op.getLeftTerm().getTypeModel());
        JCExpression typeExpr = makeJavaType(rangeType, CeylonTransformer.CLASS_NEW);
        return at(op).NewClass(null, null, typeExpr, List.<JCExpression> of(lower, upper), null);
    }

    public JCExpression transform(Tree.EntryOp op) {
        // no erasure cast needed for both terms
        JCExpression key = transformExpression(op.getLeftTerm());
        JCExpression elem = transformExpression(op.getRightTerm());
        ProducedType entryType = typeFact().getEntryType(op.getLeftTerm().getTypeModel(), op.getRightTerm().getTypeModel());
        JCExpression typeExpr = makeJavaType(entryType, CeylonTransformer.CLASS_NEW);
        return at(op).NewClass(null, null, typeExpr , List.<JCExpression> of(key, elem), null);
    }

    public JCTree transform(Tree.DefaultOp op) {
        JCExpression left = transformExpression(op.getLeftTerm());
        JCExpression right = transformExpression(op.getRightTerm());
        String varName = tempName();
        JCExpression varIdent = makeUnquotedIdent(varName);
        JCExpression test = at(op).Binary(JCTree.NE, varIdent, makeNull());
        JCExpression cond = make().Conditional(test , varIdent, right);
        JCExpression typeExpr = makeJavaType(op.getTypeModel(), NO_PRIMITIVES);
        return makeLetExpr(varName, null, typeExpr, left, cond);
    }

    public JCTree transform(Tree.ThenOp op) {
        JCExpression left = transformExpression(op.getLeftTerm(), Util.getBoxingStrategy(op.getLeftTerm()), typeFact().getBooleanDeclaration().getType());
        JCExpression right = transformExpression(op.getRightTerm());
        return make().Conditional(left , right, makeNull());
    }
    
    public JCTree transform(Tree.InOp op) {
        JCExpression left = transformExpression(op.getLeftTerm(), BoxingStrategy.BOXED, typeFact().getEqualityDeclaration().getType());
        JCExpression right = transformExpression(op.getRightTerm(), BoxingStrategy.BOXED, typeFact().getCategoryDeclaration().getType());
        String varName = tempName();
        JCExpression varIdent = makeUnquotedIdent(varName);
        JCExpression contains = at(op).Apply(null, makeSelect(right, "contains"), List.<JCExpression> of(varIdent));
        JCExpression typeExpr = makeJavaType(op.getLeftTerm().getTypeModel(), NO_PRIMITIVES);
        return makeLetExpr(varName, null, typeExpr, left, contains);
    }

    // Logical operators
    
    public JCExpression transform(Tree.LogicalOp op) {
        // Both terms are Booleans and can't be erased to anything
        JCExpression left = transformExpression(op.getLeftTerm(), BoxingStrategy.UNBOXED, null);
        return transformLogicalOp(op, op.getClass(), left, op.getRightTerm());
    }

    private JCExpression transformLogicalOp(Node op, Class<? extends Tree.LogicalOp> operatorClass, 
            JCExpression left, Tree.Term rightTerm) {
        // Both terms are Booleans and can't be erased to anything
        JCExpression right = transformExpression(rightTerm, BoxingStrategy.UNBOXED, null);

        JCBinary jcb = null;
        if (operatorClass == Tree.AndOp.class) {
            jcb = at(op).Binary(JCTree.AND, left, right);
        }else if (operatorClass == Tree.OrOp.class) {
            jcb = at(op).Binary(JCTree.OR, left, right);
        }else{
            log.error("ceylon", "Not supported yet: "+op.getNodeType());
            return at(op).Erroneous(List.<JCTree>nil());
        }
        return jcb;
    }

    // Comparison operators
    
    public JCExpression transform(Tree.ComparisonOp op) {
        return transformOverridableBinaryOperator(op, op.getUnit().getComparableDeclaration());
    }

    public JCExpression transform(Tree.CompareOp op) {
        return transformOverridableBinaryOperator(op, op.getUnit().getComparableDeclaration());
    }

    // Arithmetic operators
    
    public JCExpression transform(Tree.ArithmeticOp op) {
        return transformOverridableBinaryOperator(op, op.getUnit().getNumericDeclaration());
    }
    
    public JCExpression transform(Tree.SumOp op) {
        return transformOverridableBinaryOperator(op, op.getUnit().getSummableDeclaration());
    }

    public JCExpression transform(Tree.RemainderOp op) {
        return transformOverridableBinaryOperator(op, op.getUnit().getIntegralDeclaration());
    }

    // Overridable binary operators
    
    public JCExpression transform(Tree.BinaryOperatorExpression op) {
        return transformOverridableBinaryOperator(op, null, null);
    }

    private JCExpression transformOverridableBinaryOperator(Tree.BinaryOperatorExpression op, Interface compoundType) {
        ProducedType leftType = getSupertype(op.getLeftTerm(), compoundType);
        ProducedType rightType = getTypeArgument(leftType);
        return transformOverridableBinaryOperator(op, leftType, rightType);
    }

    private JCExpression transformOverridableBinaryOperator(Tree.BinaryOperatorExpression op, ProducedType leftType, ProducedType rightType) {
        JCExpression left = transformExpression(op.getLeftTerm(), getBoxingStrategy(op), leftType);
        return transformOverridableBinaryOperator(op, op.getClass(), left, rightType);
    }

    private JCExpression transformOverridableBinaryOperator(Tree.BinaryOperatorExpression op, 
            Class<? extends Tree.OperatorExpression> operatorClass, 
            JCExpression left, ProducedType rightType) {
        JCExpression result = null;
        
        JCExpression right = transformExpression(op.getRightTerm(), getBoxingStrategy(op), rightType);
        
        if (operatorClass == Tree.IdenticalOp.class) {
            // FIXME: move this out of here no? It's not overridable and doesn't require boxing/unboxing or
            // even unerasure
            result = at(op).Binary(JCTree.EQ, left, right);
        } else {
            OperatorTranslation originalOperator = binaryOperators.get(operatorClass);
            if (originalOperator == null) {
                return make().Erroneous();
            }

            // optimise if we can
            if(op.getUnboxed() && originalOperator.isOptimisable()){
                return make().Binary(originalOperator.javacOperator, left, right);
            }

            boolean loseComparison = 
                    operatorClass == Tree.SmallAsOp.class 
                    || operatorClass == Tree.SmallerOp.class 
                    || operatorClass == Tree.LargerOp.class
                    || operatorClass == Tree.LargeAsOp.class;
    
            // for comparisons we need to invoke compare()
            OperatorTranslation actualOperator = originalOperator;
            if (loseComparison) {
                actualOperator = binaryOperators.get(Tree.CompareOp.class);
                if (actualOperator == null) {
                    return make().Erroneous();
                }
            }

            result = at(op).Apply(null, makeSelect(left, actualOperator.ceylonMethod), List.of(right));
    
            if (loseComparison) {
                result = at(op).Apply(null, makeSelect(result, actualOperator.ceylonMethod), List.<JCExpression> nil());
            }
        }

        return result;
    }

    //
    // Operator-Assignment expressions

    public JCExpression transform(final Tree.ArithmeticAssignmentOp op){
        final AssignmentOperatorTranslation operator = assignmentOperators.get(op.getClass());
        if(operator == null){
            log.error("ceylon", "Not supported yet: "+op.getNodeType());
            return at(op).Erroneous(List.<JCTree>nil());
        }

        // see if we can optimise it
        if(op.getUnboxed()){
            return optimiseAssignmentOperator(op, operator);
        }
        
        // find the proper type
        Interface compoundType = op.getUnit().getNumericDeclaration();
        if(op instanceof Tree.AddAssignOp){
            compoundType = op.getUnit().getSummableDeclaration();
        }else if(op instanceof Tree.RemainderAssignOp){
            compoundType = op.getUnit().getIntegralDeclaration();
        }
        
        final ProducedType leftType = getSupertype(op.getLeftTerm(), compoundType);
        final ProducedType rightType = getTypeArgument(leftType, 0);

        // we work on boxed types
        return transformAssignAndReturnOperation(op, op.getLeftTerm(), true, 
                leftType, rightType, 
                new AssignAndReturnOperationFactory(){
            @Override
            public JCExpression getNewValue(JCExpression previousValue) {
                // make this call: previousValue OP RHS
                return transformOverridableBinaryOperator(op, operator.binaryOperator, previousValue, rightType);
            }
        });
    }

    public JCExpression transform(Tree.BitwiseAssignmentOp op){
        log.error("ceylon", "Not supported yet: "+op.getNodeType());
        return at(op).Erroneous(List.<JCTree>nil());
    }

    public JCExpression transform(final Tree.LogicalAssignmentOp op){
        final AssignmentOperatorTranslation operator = assignmentOperators.get(op.getClass());
        if(operator == null){
            log.error("ceylon", "Not supported yet: "+op.getNodeType());
            return at(op).Erroneous(List.<JCTree>nil());
        }
        
        // optimise if we can
        if(op.getUnboxed()){
            return optimiseAssignmentOperator(op, operator);
        }
        
        ProducedType valueType = op.getLeftTerm().getTypeModel();
        // we work on unboxed types
        return transformAssignAndReturnOperation(op, op.getLeftTerm(), false, 
                valueType, valueType, new AssignAndReturnOperationFactory(){
            @SuppressWarnings("unchecked")
            @Override
            public JCExpression getNewValue(JCExpression previousValue) {
                // make this call: previousValue OP RHS
                return transformLogicalOp(op, (Class<? extends LogicalOp>) operator.binaryOperator, 
                        previousValue, op.getRightTerm());
            }
        });
    }

    private JCExpression optimiseAssignmentOperator(final Tree.AssignmentOp op, final AssignmentOperatorTranslation operator) {
        // we don't care about their types since they're unboxed and we know it
        JCExpression left = transformExpression(op.getLeftTerm(), BoxingStrategy.UNBOXED, null);
        JCExpression right = transformExpression(op.getRightTerm(), BoxingStrategy.UNBOXED, null);
        return at(op).Assignop(operator.javacOperator, left, right);
    }

    // Postfix operator
    
    public JCExpression transform(Tree.PostfixOperatorExpression expr) {
        OperatorTranslation operator = unaryOperators.get(expr.getClass());
        if(operator == null){
            log.error("ceylon", "Not supported yet: "+expr.getNodeType());
            return at(expr).Erroneous(List.<JCTree>nil());
        }
        
        if(expr.getUnboxed() && operator.isOptimisable()){
            JCExpression term = transformExpression(expr.getTerm(), BoxingStrategy.UNBOXED, expr.getTypeModel());
            return at(expr).Unary(operator.javacOperator, term);
        }
        
        Interface compoundType = expr.getUnit().getOrdinalDeclaration();
        ProducedType valueType = getSupertype(expr.getTerm(), compoundType);
        ProducedType returnType = getTypeArgument(valueType, 0);

        Tree.Term term = expr.getTerm();
        List<JCVariableDecl> decls = List.nil();
        List<JCStatement> stats = List.nil();
        JCExpression result = null;
        // attr++
        // (let $tmp = attr; attr = $tmp.getSuccessor(); $tmp;)
        if(term instanceof Tree.BaseMemberExpression){
            JCExpression getter = transform((Tree.BaseMemberExpression)term, null);
            at(expr);
            // Type $tmp = attr
            JCExpression exprType = makeJavaType(returnType, NO_PRIMITIVES);
            Name varName = names().fromString(tempName("op"));
            // make sure we box the results if necessary
            getter = applyErasureAndBoxing(getter, term, BoxingStrategy.BOXED, returnType);
            JCVariableDecl tmpVar = make().VarDef(make().Modifiers(0), varName, exprType, getter);
            decls = decls.prepend(tmpVar);

            // attr = $tmp.getSuccessor()
            JCExpression successor = make().Apply(null, 
                                                  makeSelect(make().Ident(varName), operator.ceylonMethod), 
                                                  List.<JCExpression>nil());
            // make sure the result is boxed if necessary, the result of successor/predecessor is always boxed
            successor = boxUnboxIfNecessary(successor, true, term.getTypeModel(), Util.getBoxingStrategy(term));
            JCExpression assignment = transformAssignment(expr, term, successor);
            stats = stats.prepend(at(expr).Exec(assignment));

            // $tmp
            // always return boxed
            result = make().Ident(varName);
        }
        else if(term instanceof Tree.QualifiedMemberExpression){
            // e.attr++
            // (let $tmpE = e, $tmpV = $tmpE.attr; $tmpE.attr = $tmpV.getSuccessor(); $tmpV;)
            Tree.QualifiedMemberExpression qualified = (Tree.QualifiedMemberExpression) term;

            // transform the primary, this will get us a boxed primary 
            JCExpression e = transformQualifiedMemberPrimary(qualified);
            at(expr);
            
            // Type $tmpE = e
            JCExpression exprType = makeJavaType(qualified.getTarget().getQualifyingType(), NO_PRIMITIVES);
            Name varEName = names().fromString(tempName("opE"));
            JCVariableDecl tmpEVar = make().VarDef(make().Modifiers(0), varEName, exprType, e);

            // Type $tmpV = $tmpE.attr
            JCExpression attrType = makeJavaType(returnType, NO_PRIMITIVES);
            Name varVName = names().fromString(tempName("opV"));
            JCExpression getter = transformMemberExpression(qualified, make().Ident(varEName), null);
            // make sure we box the results if necessary
            getter = applyErasureAndBoxing(getter, term, BoxingStrategy.BOXED, returnType);
            JCVariableDecl tmpVVar = make().VarDef(make().Modifiers(0), varVName, attrType, getter);

            // define all the variables
            decls = decls.prepend(tmpVVar);
            decls = decls.prepend(tmpEVar);
            
            // $tmpE.attr = $tmpV.getSuccessor()
            JCExpression successor = make().Apply(null, 
                                                  makeSelect(make().Ident(varVName), operator.ceylonMethod), 
                                                  List.<JCExpression>nil());
            // make sure the result is boxed if necessary, the result of successor/predecessor is always boxed
            successor = boxUnboxIfNecessary(successor, true, term.getTypeModel(), Util.getBoxingStrategy(term));
            JCExpression assignment = transformAssignment(expr, term, make().Ident(varEName), successor);
            stats = stats.prepend(at(expr).Exec(assignment));
            
            // $tmpV
            // always return boxed
            result = make().Ident(varVName);
        }else{
            log.error("ceylon", "Not supported yet");
            return at(expr).Erroneous(List.<JCTree>nil());
        }
        // e?.attr++ is probably not legal
        // a[i]++ is not for M1 but will be:
        // (let $tmpA = a, $tmpI = i, $tmpV = $tmpA.item($tmpI); $tmpA.setItem($tmpI, $tmpV.getSuccessor()); $tmpV;)
        // a?[i]++ is probably not legal
        // a[i1..i1]++ and a[i1...]++ are probably not legal
        // a[].attr++ and a[].e.attr++ are probably not legal

        return make().LetExpr(decls, stats, result);
    }
    
    // Prefix operator
    
    public JCExpression transform(Tree.PrefixOperatorExpression expr) {
        final OperatorTranslation operator = unaryOperators.get(expr.getClass());
        if(operator == null){
            log.error("ceylon", "Not supported yet: "+expr.getNodeType());
            return at(expr).Erroneous(List.<JCTree>nil());
        }
        
        if(expr.getUnboxed() && operator.isOptimisable()){
            JCExpression term = transformExpression(expr.getTerm(), BoxingStrategy.UNBOXED, expr.getTypeModel());
            return at(expr).Unary(operator.javacOperator, term);
        }

        Interface compoundType = expr.getUnit().getOrdinalDeclaration();
        ProducedType valueType = getSupertype(expr.getTerm(), compoundType);
        ProducedType returnType = getTypeArgument(valueType, 0);
        // we work on boxed types
        return transformAssignAndReturnOperation(expr, expr.getTerm(), true, 
                valueType, returnType, new AssignAndReturnOperationFactory(){
            @Override
            public JCExpression getNewValue(JCExpression previousValue) {
                // make this call: previousValue.getSuccessor() or previousValue.getPredecessor()
                return make().Apply(null, makeSelect(previousValue, operator.ceylonMethod), List.<JCExpression>nil());
            }
        });
    }
    
    //
    // Function to deal with expressions that have side-effects
    
    private interface AssignAndReturnOperationFactory {
        JCExpression getNewValue(JCExpression previousValue);
    }
    
    private JCExpression transformAssignAndReturnOperation(Node operator, Tree.Term term, 
            boolean boxResult, ProducedType valueType, ProducedType returnType, 
            AssignAndReturnOperationFactory factory){
        
        List<JCVariableDecl> decls = List.nil();
        List<JCStatement> stats = List.nil();
        JCExpression result = null;
        // attr
        // (let $tmp = OP(attr); attr = $tmp; $tmp)
        if(term instanceof Tree.BaseMemberExpression){
            JCExpression getter = transform((Tree.BaseMemberExpression)term, null);
            at(operator);
            // Type $tmp = OP(attr);
            JCExpression exprType = makeJavaType(returnType, boxResult ? NO_PRIMITIVES : 0);
            Name varName = names().fromString(tempName("op"));
            // make sure we box the results if necessary
            getter = applyErasureAndBoxing(getter, term, boxResult ? BoxingStrategy.BOXED : BoxingStrategy.UNBOXED, valueType);
            JCExpression newValue = factory.getNewValue(getter);
            // no need to box/unbox here since newValue and $tmpV share the same boxing type
            JCVariableDecl tmpVar = make().VarDef(make().Modifiers(0), varName, exprType, newValue);
            decls = decls.prepend(tmpVar);

            // attr = $tmp
            // make sure the result is unboxed if necessary, $tmp may be boxed
            JCExpression value = make().Ident(varName);
            value = boxUnboxIfNecessary(value, boxResult, term.getTypeModel(), Util.getBoxingStrategy(term));
            JCExpression assignment = transformAssignment(operator, term, value);
            stats = stats.prepend(at(operator).Exec(assignment));
            
            // $tmp
            // return, with the box type we asked for
            result = make().Ident(varName);
        }
        else if(term instanceof Tree.QualifiedMemberExpression){
            // e.attr
            // (let $tmpE = e, $tmpV = OP($tmpE.attr); $tmpE.attr = $tmpV; $tmpV;)
            Tree.QualifiedMemberExpression qualified = (Tree.QualifiedMemberExpression) term;

            // transform the primary, this will get us a boxed primary 
            JCExpression e = transformQualifiedMemberPrimary(qualified);
            at(operator);
            
            // Type $tmpE = e
            JCExpression exprType = makeJavaType(qualified.getTarget().getQualifyingType(), NO_PRIMITIVES);
            Name varEName = names().fromString(tempName("opE"));
            JCVariableDecl tmpEVar = make().VarDef(make().Modifiers(0), varEName, exprType, e);

            // Type $tmpV = OP($tmpE.attr)
            JCExpression attrType = makeJavaType(returnType, boxResult ? NO_PRIMITIVES : 0);
            Name varVName = names().fromString(tempName("opV"));
            JCExpression getter = transformMemberExpression(qualified, make().Ident(varEName), null);
            // make sure we box the results if necessary
            getter = applyErasureAndBoxing(getter, term, boxResult ? BoxingStrategy.BOXED : BoxingStrategy.UNBOXED, valueType);
            JCExpression newValue = factory.getNewValue(getter);
            // no need to box/unbox here since newValue and $tmpV share the same boxing type
            JCVariableDecl tmpVVar = make().VarDef(make().Modifiers(0), varVName, attrType, newValue);

            // define all the variables
            decls = decls.prepend(tmpVVar);
            decls = decls.prepend(tmpEVar);
            
            // $tmpE.attr = $tmpV
            // make sure $tmpV is unboxed if necessary
            JCExpression value = make().Ident(varVName);
            value = boxUnboxIfNecessary(value, boxResult, term.getTypeModel(), Util.getBoxingStrategy(term));
            JCExpression assignment = transformAssignment(operator, term, make().Ident(varEName), value);
            stats = stats.prepend(at(operator).Exec(assignment));
            
            // $tmpV
            // return, with the box type we asked for
            result = make().Ident(varVName);
        }else{
            log.error("ceylon", "Not supported yet");
            return at(operator).Erroneous(List.<JCTree>nil());
        }
        // OP(e?.attr) is probably not legal
        // OP(a[i]) is not for M1 but will be:
        // (let $tmpA = a, $tmpI = i, $tmpV = OP($tmpA.item($tmpI)); $tmpA.setItem($tmpI, $tmpV); $tmpV;)
        // OP(a?[i]) is probably not legal
        // OP(a[i1..i1]) and OP(a[i1...]) are probably not legal
        // OP(a[].attr) and OP(a[].e.attr) are probably not legal

        return make().LetExpr(decls, stats, result);
    }


    public JCExpression transform(Tree.Parameter param) {
        // Transform the expression marking that we're inside a defaulted parameter for $this-handling
        needDollarThis  = true;
        SpecifierExpression spec = param.getDefaultArgument().getSpecifierExpression();
        JCExpression expr = expressionGen().transformExpression(spec.getExpression(), Util.getBoxingStrategy(param.getDeclarationModel()), param.getDeclarationModel().getType());
        needDollarThis = false;
        return expr;
    }
    
    //
    // Invocations
    
    public JCExpression transform(Tree.InvocationExpression ce) {
        if (ce.getPositionalArgumentList() != null) {
            return transformPositionalInvocation(ce);
        } else if (ce.getNamedArgumentList() != null) {
            return transformNamedInvocation(ce);
        } else {
            throw new RuntimeException("Illegal State");
        }
    }
    
    // Named invocation
    
    private JCExpression transformNamedInvocation(final Tree.InvocationExpression ce) {
        ListBuffer<JCVariableDecl> vars = ListBuffer.lb();
        ListBuffer<JCExpression> args = ListBuffer.lb();

        java.util.List<ProducedType> typeArgumentModels = getTypeArguments(ce);
        List<JCExpression> typeArgs = transformTypeArguments(typeArgumentModels);
        boolean isRaw = typeArgs.isEmpty();
        String callVarName = null;
        
        Declaration primaryDecl = ce.getPrimary().getDeclaration();
        if (primaryDecl != null) {
            java.util.List<ParameterList> paramLists = ((Functional)primaryDecl).getParameterLists();
            java.util.List<Tree.NamedArgument> namedArguments = ce.getNamedArgumentList().getNamedArguments();
            java.util.List<Parameter> declaredParams = paramLists.get(0).getParameters();
            Parameter lastDeclared = declaredParams.size() > 0 ? declaredParams.get(declaredParams.size() - 1) : null;
            boolean boundSequenced = false;
            String varBaseName = aliasName("arg");
            callVarName = varBaseName + "$callable$";
            
            int numDeclared = declaredParams.size();
            int numDeclaredFixed = (lastDeclared != null && lastDeclared.isSequenced()) ? numDeclared - 1 : numDeclared;
            int numPassed = namedArguments.size();
            int idx = 0;
            for (Tree.NamedArgument namedArg : namedArguments) {
                at(namedArg);
                Tree.Expression expr = ((Tree.SpecifiedArgument)namedArg).getSpecifierExpression().getExpression();
                Parameter declaredParam = namedArg.getParameter();
                int index;
                BoxingStrategy boxType;
                ProducedType type;
                if (declaredParam != null) {
                    if (declaredParam.isSequenced()) {
                        boundSequenced = true;
                    }
                    index = declaredParams.indexOf(declaredParam);
                    boxType = Util.getBoxingStrategy(declaredParam);
                    type = getTypeForParameter(declaredParam, isRaw, typeArgumentModels);
                } else {
                    // Arguments of overloaded methods don't have a reference to parameter
                    index = idx++;
                    boxType = BoxingStrategy.UNBOXED;
                    type = expr.getTypeModel();
                }
                String varName = varBaseName + "$" + index;
                // if we can't pick up on the type from the declaration, revert to the type of the expression
                if(isTypeParameter(type))
                    type = expr.getTypeModel();
                JCExpression typeExpr = makeJavaType(type, (boxType == BoxingStrategy.BOXED) ? TYPE_ARGUMENT : 0);
                JCExpression argExpr = transformExpression(expr, boxType, type);
                JCVariableDecl varDecl = makeVar(varName, typeExpr, argExpr);
                vars.append(varDecl);
            }
            
            if (!Decl.isOverloaded(primaryDecl) && numPassed < numDeclaredFixed) {
                boolean needsThis = false;
                if (Decl.withinClassOrInterface(primaryDecl)) {
                    // first append $this
                    ProducedType thisType = getThisType(primaryDecl);
                    vars.append(makeVar(varBaseName + "$this$", makeJavaType(thisType, NO_PRIMITIVES), makeUnquotedIdent(callVarName)));
                    needsThis = true;
                }
                // append any arguments for defaulted parameters
                for (int ii = 0; ii < numDeclaredFixed; ii++) {
                    Parameter param = declaredParams.get(ii);
                    if (containsParameter(namedArguments, param)) {
                        continue;
                    }
                    String varName = varBaseName + "$" + ii;
                    String methodName = Util.getDefaultedParamMethodName(primaryDecl, param);
                    List<JCExpression> arglist = makeThisVarRefArgumentList(varBaseName, ii, needsThis);
                    JCExpression argExpr;
                    if (!param.isSequenced()) {
                        Declaration container = param.getDeclaration().getRefinedDeclaration();
                        if (!container.isToplevel()) {
                            container = (Declaration)container.getContainer();
                        }
                        String className = Util.getCompanionClassName(container.getName());
                        argExpr = at(ce).Apply(null, makeQualIdent(makeQualIdentFromString(container.getQualifiedNameString()), className, methodName), arglist);
                    } else {
                        argExpr = makeEmpty();
                    }
                    BoxingStrategy boxType = Util.getBoxingStrategy(param);
                    ProducedType type = getTypeForParameter(param, isRaw, typeArgumentModels);
                    JCExpression typeExpr = makeJavaType(type, (boxType == BoxingStrategy.BOXED) ? TYPE_ARGUMENT : 0);
                    JCVariableDecl varDecl = makeVar(varName, typeExpr, argExpr);
                    vars.append(varDecl);
                }
            }
            
            Tree.SequencedArgument sequencedArgument = ce.getNamedArgumentList().getSequencedArgument();
            if (sequencedArgument != null) {
                at(sequencedArgument);
                String varName = varBaseName + "$" + numDeclaredFixed;
                JCExpression typeExpr = makeJavaType(lastDeclared.getType(), AbstractTransformer.WANT_RAW_TYPE);
                JCExpression argExpr = makeSequenceRaw(sequencedArgument.getExpressionList().getExpressions());
                JCVariableDecl varDecl = makeVar(varName, typeExpr, argExpr);
                vars.append(varDecl);
            } else if (lastDeclared != null 
                    && lastDeclared.isSequenced() 
                    && !boundSequenced) {
                String varName = varBaseName + "$" + numDeclaredFixed;
                JCExpression typeExpr = makeJavaType(lastDeclared.getType(), AbstractTransformer.WANT_RAW_TYPE);
                JCVariableDecl varDecl = makeVar(varName, typeExpr, makeEmpty());
                vars.append(varDecl);
            }
            
            if (!Decl.isOverloaded(primaryDecl)) {
                args.appendList(makeVarRefArgumentList(varBaseName, numDeclared));
            } else {
                // For overloaded methods (and therefore Java interop) we just pass the arguments we have
                args.appendList(makeVarRefArgumentList(varBaseName, numPassed));
            }
        }

        return makeInvocation(ce, vars, args, typeArgs, callVarName);
    }
    
    private boolean containsParameter(java.util.List<Tree.NamedArgument> namedArguments, Parameter param) {
        for (Tree.NamedArgument namedArg : namedArguments) {
            Parameter declaredParam = namedArg.getParameter();
            if (param == declaredParam) {
                return true;
            }
        }
        return false;
    }

    // Positional invocation
    
    private JCExpression transformPositionalInvocation(Tree.InvocationExpression ce) {
        ListBuffer<JCVariableDecl> vars = null;
        ListBuffer<JCExpression> args = new ListBuffer<JCExpression>();

        Declaration primaryDecl = ce.getPrimary().getDeclaration();
        Tree.PositionalArgumentList positional = ce.getPositionalArgumentList();

        java.util.List<ProducedType> typeArgumentModels = getTypeArguments(ce);
        List<JCExpression> typeArgs = transformTypeArguments(typeArgumentModels);
        boolean isRaw = typeArgs.isEmpty();
        String callVarName = null;
        
        java.util.List<ParameterList> paramLists = ((Functional)primaryDecl).getParameterLists();
        java.util.List<Parameter> declaredParams = paramLists.get(0).getParameters();
        int numDeclared = declaredParams.size();
        int numPassed = positional.getPositionalArguments().size();
        Parameter lastDeclaredParam = declaredParams.isEmpty() ? null : declaredParams.get(declaredParams.size() - 1); 
        if (lastDeclaredParam != null 
                && lastDeclaredParam.isSequenced()
                && positional.getEllipsis() == null // foo(sequence...) syntax => no need to box
                && numPassed >= (numDeclared -1)) {
            // => call to a varargs method
            // first, append the normal args
            for (int ii = 0; ii < numDeclared - 1; ii++) {
                Tree.PositionalArgument arg = positional.getPositionalArguments().get(ii);
                args.append(transformArg(arg.getExpression(), arg.getParameter(), isRaw, typeArgumentModels));
            }
            JCExpression boxed;
            // then, box the remaining passed arguments
            if (numDeclared -1 == numPassed) {
                // box as Empty
                boxed = makeEmpty();
            } else {
                // box with an ArraySequence<T>
                List<Tree.Expression> x = List.<Tree.Expression>nil();
                for (int ii = numDeclared - 1; ii < numPassed; ii++) {
                    Tree.PositionalArgument arg = positional.getPositionalArguments().get(ii);
                    x = x.append(arg.getExpression());
                }
                boxed = makeSequenceRaw(x);
            }
            args.append(boxed);
        } else if (numPassed < numDeclared) {
            vars = ListBuffer.lb();
            String varBaseName = aliasName("arg");
            callVarName = varBaseName + "$callable$";
            boolean needsThis = false;
            if (Decl.withinClassOrInterface(primaryDecl)) {
                // first append $this
                ProducedType thisType = getThisType(primaryDecl);
                vars.append(makeVar(varBaseName + "$this$", makeJavaType(thisType, NO_PRIMITIVES), makeUnquotedIdent(callVarName)));
                needsThis = true;
            }
            // append the normal args
            int idx = 0;
            for (Tree.PositionalArgument arg : positional.getPositionalArguments()) {
                at(arg);
                Tree.Expression expr = arg.getExpression();
                Parameter declaredParam = arg.getParameter();
                int index;
                BoxingStrategy boxType;
                ProducedType type;
                if (declaredParam != null) {
                    index = declaredParams.indexOf(declaredParam);
                    boxType = Util.getBoxingStrategy(declaredParam);
                    type = getTypeForParameter(declaredParam, isRaw, typeArgumentModels);
                } else {
                    // Arguments of overloaded methods don't have a reference to parameter
                    index = idx++;
                    boxType = BoxingStrategy.UNBOXED;
                    type = expr.getTypeModel();
                }
                String varName = varBaseName + "$" + index;
                // if we can't pick up on the type from the declaration, revert to the type of the expression
                if(isTypeParameter(type))
                    type = expr.getTypeModel();
                JCExpression typeExpr = makeJavaType(type, (boxType == BoxingStrategy.BOXED) ? TYPE_ARGUMENT : 0);
                JCExpression argExpr = transformExpression(expr, boxType, type);
                JCVariableDecl varDecl = makeVar(varName, typeExpr, argExpr);
                vars.append(varDecl);
            }
            if (!Decl.isOverloaded(primaryDecl)) {
                // append any arguments for defaulted parameters
                for (int ii = numPassed; ii < numDeclared; ii++) {
                    Parameter param = declaredParams.get(ii);
                    String varName = varBaseName + "$" + ii;
                    String methodName = Util.getDefaultedParamMethodName(primaryDecl, param);
                    List<JCExpression> arglist = makeThisVarRefArgumentList(varBaseName, ii, needsThis);
                    JCExpression argExpr;
                    if (!param.isSequenced()) {
                        Declaration container = param.getDeclaration().getRefinedDeclaration();
                        if (!container.isToplevel()) {
                            container = (Declaration)container.getContainer();
                        }
                        String className = Util.getCompanionClassName(container.getName());
                        argExpr = at(ce).Apply(null, makeQualIdent(makeQualIdentFromString(container.getQualifiedNameString()), className, methodName), arglist);
                    } else {
                        argExpr = makeEmpty();
                    }
                    BoxingStrategy boxType = Util.getBoxingStrategy(param);
                    ProducedType type = getTypeForParameter(param, isRaw, typeArgumentModels);
                    JCExpression typeExpr = makeJavaType(type, (boxType == BoxingStrategy.BOXED) ? TYPE_ARGUMENT : 0);
                    JCVariableDecl varDecl = makeVar(varName, typeExpr, argExpr);
                    vars.append(varDecl);
                }
                args.appendList(makeVarRefArgumentList(varBaseName, numDeclared));
            } else {
                // For overloaded methods (and therefore Java interop) we just pass the arguments we have
                args.appendList(makeVarRefArgumentList(varBaseName, numPassed));
            }
        } else {
            // append the normal args
            for (Tree.PositionalArgument arg : positional.getPositionalArguments()) {
                args.append(transformArg(arg.getExpression(), arg.getParameter(), isRaw, typeArgumentModels));
            }
        }

        return makeInvocation(ce, vars, args, typeArgs, callVarName);
    }

    // Make a list of $arg0, $arg1, ... , $argN
    private List<JCExpression> makeVarRefArgumentList(String varBaseName, int argCount) {
        List<JCExpression> names = List.<JCExpression> nil();
        for (int i = 0; i < argCount; i++) {
            names = names.append(makeUnquotedIdent(varBaseName + "$" + i));
        }
        return names;
    }

    // Make a list of $arg$this$, $arg0, $arg1, ... , $argN
    private List<JCExpression> makeThisVarRefArgumentList(String varBaseName, int argCount, boolean needsThis) {
        List<JCExpression> names = List.<JCExpression> nil();
        if (needsThis) {
            names = names.append(makeUnquotedIdent(varBaseName + "$this$"));
        }
        names = names.appendList(makeVarRefArgumentList(varBaseName, argCount));
        return names;
    }

    private JCExpression makeInvocation(
            final Tree.InvocationExpression ce,
            final ListBuffer<JCVariableDecl> vars,
            final ListBuffer<JCExpression> args,
            final List<JCExpression> typeArgs,
            final String callVarName) {
        at(ce);
        JCExpression result = transformPrimary(ce.getPrimary(), new TermTransformer() {

            @Override
            public JCExpression transform(JCExpression primaryExpr, String selector) {
                JCExpression actualPrimExpr = null;
                if (vars != null && primaryExpr != null && selector != null) {
                    // Prepare the first argument holding the primary for the call
                    JCExpression callVarExpr = makeUnquotedIdent(callVarName);
                    ProducedType type = ((Tree.QualifiedMemberExpression)ce.getPrimary()).getTarget().getQualifyingType();
                    JCVariableDecl callVar = makeVar(callVarName, makeJavaType(type, NO_PRIMITIVES), primaryExpr);
                    vars.prepend(callVar);
                    actualPrimExpr = callVarExpr;
                } else {
                    actualPrimExpr = primaryExpr;
                }
                
                JCExpression resultExpr;
                if (ce.getPrimary() instanceof Tree.BaseTypeExpression) {
                    ProducedType classType = (ProducedType)((Tree.BaseTypeExpression)ce.getPrimary()).getTarget();
                    resultExpr = make().NewClass(null, null, makeJavaType(classType, CLASS_NEW), args.toList(), null);
                } else if (ce.getPrimary() instanceof Tree.QualifiedTypeExpression) {
                    resultExpr = make().NewClass(actualPrimExpr, null, makeQuotedIdent(selector), args.toList(), null);
                } else {
                    resultExpr = make().Apply(typeArgs, makeQualIdent(actualPrimExpr, selector), args.toList());
                }

                if (vars != null) {
                    ProducedType returnType = ce.getTypeModel();
                    if (isVoid(returnType)) {
                        // void methods get wrapped like (let $arg$1=expr, $arg$0=expr in call($arg$0, $arg$1); null)
                        return make().LetExpr(vars.toList(), List.<JCStatement>of(make().Exec(resultExpr)), makeNull());
                    } else {
                        // all other methods like (let $arg$1=expr, $arg$0=expr in call($arg$0, $arg$1))
                        return make().LetExpr(vars.toList(), resultExpr);
                    }
                } else {
                    return resultExpr;
                }
            }
            
        });
        return result;
    }

    // Invocation helper functions
    
    private java.util.List<ProducedType> getTypeArguments(Tree.InvocationExpression ce) {
        if(ce.getPrimary() instanceof Tree.StaticMemberOrTypeExpression){
            return ((Tree.StaticMemberOrTypeExpression)ce.getPrimary()).getTypeArguments().getTypeModels();
        }
        return null;
    }

    private List<JCExpression> transformTypeArguments(java.util.List<ProducedType> typeArguments) {
        List<JCExpression> result = List.<JCExpression> nil();
        if(typeArguments != null){
            for (ProducedType arg : typeArguments) {
                // cancel type parameters and go raw if we can't specify them
                if(willEraseToObject(arg)
                        || isTypeParameter(arg))
                    return List.nil();
                result = result.append(makeJavaType(arg, AbstractTransformer.TYPE_ARGUMENT));
            }
        }
        return result;
    }
    
    // used by ClassDefinitionBuilder too, for super()
    JCExpression transformArg(Tree.Term expr, Parameter parameter, boolean isRaw, java.util.List<ProducedType> typeArgumentModels) {
        if (parameter != null) {
            ProducedType type = getTypeForParameter(parameter, isRaw, typeArgumentModels);
            return transformExpression(expr, 
                    Util.getBoxingStrategy(parameter), 
                    type);
        } else {
            // Overloaded methods don't have a reference to a parameter
            // so we have to treat them differently. Also knowing it's
            // overloaded we know we're dealing with Java code so we unbox
            ProducedType type = expr.getTypeModel();
            return transformExpression(expr, 
                    BoxingStrategy.UNBOXED, 
                    type);
        }
    }

    private ProducedType getTypeForParameter(Parameter parameter, boolean isRaw, java.util.List<ProducedType> typeArgumentModels) {
        ProducedType type = parameter.getType();
        if(isTypeParameter(type)){
            TypeParameter tp = (TypeParameter) type.getDeclaration();
            if(!isRaw && typeArgumentModels != null){
                // try to use the inferred type if we're not going raw
                Scope scope = parameter.getContainer();
                int typeParamIndex = getTypeParameterIndex(scope, tp);
                if(typeParamIndex != -1)
                    return typeArgumentModels.get(typeParamIndex);
            }
            if(tp.getSatisfiedTypes().size() >= 1){
                // try the first satisfied type
                type = tp.getSatisfiedTypes().get(0).getType();
                // unless it's erased, in which case try for more specific
                if(!willEraseToObject(type))
                    return type;
            }
        }
        return type;
    }

    private int getTypeParameterIndex(Scope scope, TypeParameter tp) {
        if(scope instanceof Method)
            return ((Method)scope).getTypeParameters().indexOf(tp);
        return ((ClassOrInterface)scope).getTypeParameters().indexOf(tp);
    }

    //
    // Member expressions

    private interface TermTransformer {
        JCExpression transform(JCExpression primaryExpr, String selector);
    }

    // Qualified members
    
    public JCExpression transform(Tree.QualifiedMemberExpression expr) {
        return transform(expr, null);
    }
    
    private JCExpression transform(Tree.QualifiedMemberExpression expr, TermTransformer transformer) {
        JCExpression result;
        if (expr.getMemberOperator() instanceof Tree.SafeMemberOp) {
            JCExpression primaryExpr = transformQualifiedMemberPrimary(expr);
            String tmpVarName = aliasName("safe");
            JCExpression typeExpr = makeJavaType(expr.getTarget().getQualifyingType(), NO_PRIMITIVES);
            JCExpression transExpr = transformMemberExpression(expr, makeUnquotedIdent(tmpVarName), transformer);
            transExpr = boxUnboxIfNecessary(transExpr, expr, expr.getTarget().getType(), BoxingStrategy.BOXED);
            JCExpression testExpr = make().Binary(JCTree.NE, makeUnquotedIdent(tmpVarName), makeNull());                
            JCExpression condExpr = make().Conditional(testExpr, transExpr, makeNull());
            result = makeLetExpr(tmpVarName, null, typeExpr, primaryExpr, condExpr);
        } else if (expr.getMemberOperator() instanceof Tree.SpreadOp) {
            result = transformSpreadOperator(expr, transformer);
        } else {
            JCExpression primaryExpr = transformQualifiedMemberPrimary(expr);
            result = transformMemberExpression(expr, primaryExpr, transformer);
        }
        return result;
    }

    private JCExpression transformSpreadOperator(Tree.QualifiedMemberExpression expr, TermTransformer transformer) {
        at(expr);
        
        String varBaseName = aliasName("spread");
        // sequence
        String srcSequenceName = varBaseName+"$0";
        ProducedType srcSequenceType = typeFact().getNonemptySequenceType(expr.getPrimary().getTypeModel());
        ProducedType srcElementType = typeFact().getElementType(srcSequenceType);
        JCExpression srcSequenceTypeExpr = makeJavaType(srcSequenceType, NO_PRIMITIVES);
        JCExpression srcSequenceExpr = transformExpression(expr.getPrimary(), BoxingStrategy.BOXED, srcSequenceType);

        // reset back here after transformExpression
        at(expr);

        // size, getSize() always unboxed, but we need to cast to int for Java array access
        String sizeName = varBaseName+"$2";
        JCExpression sizeType = make().TypeIdent(TypeTags.INT);
        JCExpression sizeExpr = make().TypeCast(syms().intType, make().Apply(null, 
                make().Select(makeUnquotedIdent(srcSequenceName), names().fromString("getSize")), 
                List.<JCTree.JCExpression>nil()));
        
        // new array
        String newArrayName = varBaseName+"$4";
        JCExpression arrayElementType = makeJavaType(expr.getTarget().getType(), NO_PRIMITIVES);
        JCExpression newArrayType = make().TypeArray(arrayElementType);
        JCNewArray newArrayExpr = make().NewArray(arrayElementType, List.of(makeUnquotedIdent(sizeName)), null);
        
        // return the new array
        JCExpression returnArrayType = makeJavaType(expr.getTarget().getType(), SATISFIES);
        JCExpression returnArrayIdent = make().QualIdent(syms().ceylonArraySequenceType.tsym);
        JCExpression returnArrayTypeExpr;
        // avoid putting type parameters such as j.l.Object
        if(returnArrayType != null)
            returnArrayTypeExpr = make().TypeApply(returnArrayIdent, List.of(returnArrayType));
        else // go raw
            returnArrayTypeExpr = returnArrayIdent;
        JCNewClass returnArray = make().NewClass(null, null, 
                returnArrayTypeExpr, 
                List.of(makeUnquotedIdent(newArrayName)), null);
        
        // for loop
        Name indexVarName = names().fromString(aliasName("index"));
        // int index = 0
        JCStatement initVarDef = make().VarDef(make().Modifiers(0), indexVarName, make().TypeIdent(TypeTags.INT), makeInteger(0));
        List<JCStatement> init = List.of(initVarDef);
        // index < size
        JCExpression cond = make().Binary(JCTree.LT, make().Ident(indexVarName), makeUnquotedIdent(sizeName));
        // index++
        JCExpression stepExpr = make().Unary(JCTree.POSTINC, make().Ident(indexVarName));
        List<JCExpressionStatement> step = List.of(make().Exec(stepExpr));
        
        // newArray[index]
        JCExpression dstArrayExpr = make().Indexed(makeUnquotedIdent(newArrayName), make().Ident(indexVarName));
        // srcSequence.item(box(index))
        // index is always boxed
        JCExpression boxedIndex = boxType(make().Ident(indexVarName), typeFact().getIntegerDeclaration().getType());
        JCExpression sequenceItemExpr = make().Apply(null, 
                make().Select(makeUnquotedIdent(srcSequenceName), names().fromString("item")),
                List.<JCExpression>of(boxedIndex));
        // item.member
        sequenceItemExpr = applyErasureAndBoxing(sequenceItemExpr, srcElementType, true, BoxingStrategy.BOXED, 
                expr.getTarget().getQualifyingType());
        JCExpression appliedExpr = transformMemberExpression(expr, sequenceItemExpr, transformer);
        // reset back here after transformMemberExpression
        at(expr);
        // we always need to box to put in array
        appliedExpr = boxUnboxIfNecessary(appliedExpr, expr, 
                expr.getTarget().getType(), BoxingStrategy.BOXED);
        // newArray[index] = box(srcSequence.item(box(index)).member)
        JCStatement body = make().Exec(make().Assign(dstArrayExpr, appliedExpr));
        
        // for
        JCForLoop forStmt = make().ForLoop(init, cond , step , body);
        
        // build the whole thing
        return makeLetExpr(varBaseName, 
                List.<JCStatement>of(forStmt), 
                srcSequenceTypeExpr, srcSequenceExpr,
                sizeType, sizeExpr,
                newArrayType, newArrayExpr,
                returnArray);
    }

    private JCExpression transformQualifiedMemberPrimary(Tree.QualifiedMemberOrTypeExpression expr) {
        if(expr.getTarget() == null)
            return at(expr).Erroneous(List.<JCTree>nil());
        return transformExpression(expr.getPrimary(), BoxingStrategy.BOXED, 
                expr.getTarget().getQualifyingType());
    }
    
    // Base members
    
    public JCExpression transform(Tree.BaseMemberExpression expr) {
        return transform(expr, null);
    }

    private JCExpression transform(Tree.BaseMemberOrTypeExpression expr, TermTransformer transformer) {
        return transformMemberExpression(expr, null, transformer);
    }
    
    // Type members
    
    public JCExpression transform(Tree.QualifiedTypeExpression expr) {
        return transform(expr, null);
    }
    
    private JCExpression transform(Tree.QualifiedTypeExpression expr, TermTransformer transformer) {
        JCExpression primaryExpr = transformQualifiedMemberPrimary(expr);
        return transformMemberExpression(expr, primaryExpr, transformer);
    }
    
    // Generic code for all primaries
    
    private JCExpression transformPrimary(Tree.Primary primary, TermTransformer transformer) {
        if (primary instanceof Tree.QualifiedMemberExpression) {
            return transform((Tree.QualifiedMemberExpression)primary, transformer);
        } else if (primary instanceof Tree.BaseMemberExpression) {
            return transform((Tree.BaseMemberExpression)primary, transformer);
        } else if (primary instanceof Tree.BaseTypeExpression) {
            return transform((Tree.BaseTypeExpression)primary, transformer);
        } else if (primary instanceof Tree.QualifiedTypeExpression) {
            return transform((Tree.QualifiedTypeExpression)primary, transformer);
        } else {
            return makeUnquotedIdent(primary.getDeclaration().getName());
        }
    }
    
    private JCExpression transformMemberExpression(Tree.StaticMemberOrTypeExpression expr, JCExpression primaryExpr, TermTransformer transformer) {
        JCExpression result = null;

        // do not throw, an error will already have been reported
        Declaration decl = expr.getDeclaration();
        if (decl == null) {
            return make().Erroneous(List.<JCTree>nil());
        }
        
        // Explanation: primaryExpr and qualExpr both specify what is to come before the selector
        // but the important difference is that primaryExpr is used for those situations where
        // the result comes from the actual Ceylon code while qualExpr is used for those situations
        // where we need to refer to synthetic objects (like wrapper classes for toplevel methods)
        
        JCExpression qualExpr = null;
        String selector = null;
        if (decl instanceof Getter) {
            // invoke the getter
            if (decl.isToplevel()) {
                primaryExpr = null;
                qualExpr = makeQualIdent(makeFQIdent(decl.getContainer().getQualifiedNameString()), Util.quoteIfJavaKeyword(decl.getName()), Util.getGetterName(decl.getName()));
                selector = null;
            } else if (decl.isClassMember()) {
                selector = Util.getGetterName(decl.getName());
            } else {
                // method local attr
                if (!isRecursiveReference(expr)) {
                    primaryExpr = makeQualIdent(primaryExpr, decl.getName() + "$getter");
                }
                selector = Util.getGetterName(decl.getName());
            }
        } else if (decl instanceof Value) {
            if (decl.isToplevel()) {
                // ERASURE
                if ("null".equals(decl.getName())) {
                    // FIXME this is a pretty brain-dead way to go about erase I think
                    result = makeNull();
                } else if (isBooleanTrue(decl)) {
                    result = makeBoolean(true);
                } else if (isBooleanFalse(decl)) {
                    result = makeBoolean(false);
                } else {
                    // it's a toplevel attribute
                    primaryExpr = makeQualIdent(makeFQIdent(decl.getContainer().getQualifiedNameString()), Util.quoteIfJavaKeyword(decl.getName()));
                    selector = Util.getGetterName(decl.getName());
                }
            } else if (Decl.isClassAttribute(decl)) {
                // invoke the getter
                selector = Util.getGetterName(decl.getName());
             } else if (decl.isCaptured() || decl.isShared()) {
                 // invoke the qualified getter
                 primaryExpr = makeQualIdent(primaryExpr, decl.getName());
                 selector = Util.getGetterName(decl.getName());
            }
        } else if (decl instanceof Method) {
            if (Decl.withinMethod(decl)) {
                java.util.List<String> path = new LinkedList<String>();
                if (!isRecursiveReference(expr)) {
                    path.add(decl.getName());
                }
                path.add(Util.quoteIfJavaKeyword(decl.getName()));
                primaryExpr = null;
                qualExpr = makeQualIdent(path);
                selector = null;
            } else if (decl.isToplevel()) {
                java.util.List<String> path = new LinkedList<String>();
                // FQN must start with empty ident (see https://github.com/ceylon/ceylon-compiler/issues/148)
                if (!decl.getContainer().getQualifiedNameString().isEmpty()) {
                    path.add("");
                	path.addAll(Arrays.asList(decl.getContainer().getQualifiedNameString().split("\\.")));
                } else {
                    path.add("");
                }
                // class
                path.add(Util.quoteIfJavaKeyword(decl.getName()));
                // method
                path.add(Util.quoteIfJavaKeyword(decl.getName()));
                primaryExpr = null;
                qualExpr = makeQualIdent(path);
                selector = null;
            } else {
                // not toplevel, not within method, must be a class member
                selector = Util.quoteMethodName(decl.getName());
            }
        }
        if (result == null) {
            boolean useGetter = !(decl instanceof Method);
            if (qualExpr == null && selector == null) {
                useGetter = decl.isClassOrInterfaceMember() && Util.isErasedAttribute(decl.getName());
                if (useGetter) {
                    selector = Util.quoteMethodName(decl.getName());
                } else {
                    selector = substitute(decl.getName());
                }
            }
            
            if (qualExpr == null) {
                qualExpr = primaryExpr;
            }
            
            if (qualExpr == null && needDollarThis && decl.isClassOrInterfaceMember() && expr.getScope() != decl.getContainer()) {
                qualExpr = makeUnquotedIdent("$this");
            }
            
            if (transformer != null) {
                result = transformer.transform(qualExpr, selector);
            } else {
                result = makeQualIdent(qualExpr, selector);
                if (useGetter) {
                    result = make().Apply(List.<JCTree.JCExpression>nil(),
                            result,
                            List.<JCTree.JCExpression>nil());
                }
            }
        }
        
        return result;
    }

    //
    // Array access

    public JCTree transform(Tree.IndexExpression access) {
        boolean safe = access.getIndexOperator() instanceof Tree.SafeIndexOp;

        // depends on the operator
        Tree.ElementOrRange elementOrRange = access.getElementOrRange();
        if(elementOrRange instanceof Tree.Element){
            Tree.Element element = (Tree.Element) elementOrRange;
            // let's see what types there are
            ProducedType leftType = access.getPrimary().getTypeModel();
            if(safe)
                leftType = access.getUnit().getDefiniteType(leftType);
            ProducedType leftCorrespondenceType = leftType.getSupertype(access.getUnit().getCorrespondenceDeclaration());
            ProducedType rightType = getTypeArgument(leftCorrespondenceType, 0);
            
            // do the index
            JCExpression index = transformExpression(element.getExpression(), BoxingStrategy.BOXED, rightType);

            // look at the lhs
            JCExpression lhs = transformExpression(access.getPrimary(), BoxingStrategy.BOXED, leftCorrespondenceType);

            if(!safe)
                // make a "lhs.item(index)" call
                return at(access).Apply(List.<JCTree.JCExpression>nil(), 
                        make().Select(lhs, names().fromString("item")), List.of(index));
            // make a (let ArrayElem tmp = lhs in (tmp != null ? tmp.item(index) : null)) call
            JCExpression arrayType = makeJavaType(leftCorrespondenceType);
            Name varName = names().fromString(tempName("safeaccess"));
            // tmpVar.item(index)
            JCExpression safeAccess = make().Apply(List.<JCTree.JCExpression>nil(), 
                    make().Select(make().Ident(varName), names().fromString("item")), List.of(index));

            at(access.getPrimary());
            // (tmpVar != null ? safeAccess : null)
            JCConditional conditional = make().Conditional(make().Binary(JCTree.NE, make().Ident(varName), makeNull()), 
                    safeAccess, makeNull());
            // ArrayElem tmp = lhs
            JCVariableDecl tmpVar = make().VarDef(make().Modifiers(0), varName, arrayType, lhs);
            // (let tmpVar in conditional)
            return make().LetExpr(tmpVar, conditional);
        }else{
            // find the types
            ProducedType leftType = access.getPrimary().getTypeModel();
            ProducedType leftRangedType = leftType.getSupertype(access.getUnit().getRangedDeclaration());
            ProducedType rightType = getTypeArgument(leftRangedType, 0);
            // look at the lhs
            JCExpression lhs = transformExpression(access.getPrimary(), BoxingStrategy.BOXED, leftRangedType);
            // do the indices
            Tree.ElementRange range = (Tree.ElementRange) elementOrRange;
            JCExpression start = transformExpression(range.getLowerBound(), BoxingStrategy.BOXED, rightType);
            JCExpression end;
            if(range.getUpperBound() != null)
                end = transformExpression(range.getUpperBound(), BoxingStrategy.BOXED, rightType);
            else
                end = makeNull();
            // make a "lhs.span(start, end)" call
            return at(access).Apply(List.<JCTree.JCExpression>nil(), 
                    make().Select(lhs, names().fromString("span")), List.of(start, end));
        }
    }

    //
    // Assignment

    public JCExpression transform(Tree.AssignOp op) {
        return transformAssignment(op, op.getLeftTerm(), op.getRightTerm());
    }

    private JCExpression transformAssignment(Node op, Tree.Term leftTerm, Tree.Term rightTerm) {
        // FIXME: can this be anything else than a Primary?
        TypedDeclaration decl = (TypedDeclaration) ((Tree.Primary)leftTerm).getDeclaration();

        // Remember and disable inStatement for RHS
        boolean tmpInStatement = inStatement;
        inStatement = false;
        
        // right side
        final JCExpression rhs = transformExpression(rightTerm, Util.getBoxingStrategy(decl), decl.getType());

        if (tmpInStatement) {
            return transformAssignment(op, leftTerm, rhs);
        } else {
            ProducedType valueType = leftTerm.getTypeModel();
            return transformAssignAndReturnOperation(op, leftTerm, Util.getBoxingStrategy(decl) == BoxingStrategy.BOXED, 
                    valueType, valueType, new AssignAndReturnOperationFactory(){
                @Override
                public JCExpression getNewValue(JCExpression previousValue) {
                    return rhs;
                }
            });
        }
    }
    
    private JCExpression transformAssignment(final Node op, Tree.Term leftTerm, JCExpression rhs) {
        // left hand side can be either BaseMemberExpression, QualifiedMemberExpression or array access (M2)
        // TODO: array access (M2)
        JCExpression expr = null;
        if(leftTerm instanceof Tree.BaseMemberExpression)
            expr = null;
        else if(leftTerm instanceof Tree.QualifiedMemberExpression){
            Tree.QualifiedMemberExpression qualified = ((Tree.QualifiedMemberExpression)leftTerm);
            expr = transformExpression(qualified.getPrimary(), BoxingStrategy.BOXED, qualified.getTarget().getQualifyingType());
        }else{
            log.error("ceylon", "Not supported yet: "+op.getNodeType());
            return at(op).Erroneous(List.<JCTree>nil());
        }
        return transformAssignment(op, leftTerm, expr, rhs);
    }
    
    private JCExpression transformAssignment(Node op, Tree.Term leftTerm, JCExpression lhs, JCExpression rhs) {
        JCExpression result = null;

        // FIXME: can this be anything else than a Primary?
        TypedDeclaration decl = (TypedDeclaration) ((Tree.Primary)leftTerm).getDeclaration();

        boolean variable = decl.isVariable();
        
        at(op);
        String selector = Util.getSetterName(decl.getName());
        if (decl.isToplevel()) {
            // must use top level setter
            lhs = makeQualIdent(makeFQIdent(decl.getContainer().getQualifiedNameString()), Util.quoteIfJavaKeyword(decl.getName()));
        } else if ((decl instanceof Getter)) {
            // must use the setter
            if (Decl.withinMethod(decl)) {
                lhs = makeQualIdent(lhs, decl.getName() + "$setter");
            }
        } else if (variable && (Decl.isClassAttribute(decl))) {
            // must use the setter, nothing to do
        } else if (variable && (decl.isCaptured() || decl.isShared())) {
            // must use the qualified setter
            lhs = makeQualIdent(lhs, decl.getName());
        } else {
            result = at(op).Assign(makeQualIdent(lhs, decl.getName()), rhs);
        }
        
        if (result == null) {
            result = make().Apply(List.<JCTree.JCExpression>nil(),
                    makeQualIdent(lhs, selector),
                    List.<JCTree.JCExpression>of(rhs));
        }
        
        return result;
    }
    
    //
    // Type helper functions

    private ProducedType getSupertype(Tree.Term term, Interface compoundType){
        return term.getTypeModel().getSupertype(compoundType);
    }

    private ProducedType getTypeArgument(ProducedType leftType) {
        if (leftType!=null && leftType.getTypeArguments().size()==1) {
            return leftType.getTypeArgumentList().get(0);
        }
        return null;
    }

    private ProducedType getTypeArgument(ProducedType leftType, int i) {
        if (leftType!=null && leftType.getTypeArguments().size() > i) {
            return leftType.getTypeArgumentList().get(i);
        }
        return null;
    }
    
    //
    // Helper functions
    
    private boolean isRecursiveReference(Tree.StaticMemberOrTypeExpression expr) {
        Declaration decl = expr.getDeclaration();
        Scope s = expr.getScope();
        while (!(s instanceof Declaration) && (s.getContainer() != s)) {
            s = s.getContainer();
        }
        return (s instanceof Declaration) && (s == decl);
    }

    private BoxingStrategy getBoxingStrategy(Tree.Term term) {
        return term.getUnboxed() ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED;
    }
}
